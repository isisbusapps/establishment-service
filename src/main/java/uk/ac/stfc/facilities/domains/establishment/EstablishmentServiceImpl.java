package uk.ac.stfc.facilities.domains.establishment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import uk.ac.stfc.facilities.exceptions.RorQueryException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static uk.ac.stfc.facilities.helpers.Constants.EST_SEARCH_CUTOFF;

@ApplicationScoped
public class EstablishmentServiceImpl implements EstablishmentService {

    private static final Logger LOGGER = Logger.getLogger(EstablishmentService.class);

    private EstablishmentRepository estRepo;
    private CategoryRepository categoryRepo;
    private EstablishmentCategoryLinkRepository estCatLinkRepo;
    private EstablishmentAliasRepository aliasRepo;
    public EstablishmentServiceImpl() {}

    @Inject
    public EstablishmentServiceImpl(EstablishmentRepository estRepo, CategoryRepository categoryRepo,
                                    EstablishmentCategoryLinkRepository establishmentCategoryLinkRepo,
                                    EstablishmentAliasRepository aliasRepo) {
        this.estRepo = estRepo;
        this.categoryRepo = categoryRepo;
        this.estCatLinkRepo = establishmentCategoryLinkRepo;
        this.aliasRepo = aliasRepo;
    }

    @Override
    public List<Establishment> getAllEstablishments() {
        return List.of();
    }

    @Override
    public Establishment getEstablishment(Long establishmentId) {
        Establishment est = estRepo.findById(establishmentId);
        if (est == null) {
            LOGGER.warn("No establishment found with establishment id: " + establishmentId);
            throw new NoResultException("No establishment found with establishment id: " + establishmentId);
        }
        return est;
    }

    @Override
    public List<Establishment> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit) {
        List<Establishment>  allEst = onlyVerified?  estRepo.getVerified() : estRepo.getAll();

        return fuzzySearch(searchQuery, EST_SEARCH_CUTOFF, useAliases, allEst)
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<RorSchemaV21> getRorMatches(String establishmentName) {
        try {
            String encodedQuery = URLEncoder.encode(establishmentName, StandardCharsets.UTF_8);
            String url = "https://api.ror.org/v2/organizations?query=" + encodedQuery;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());
            return mapper.readerForListOf(RorSchemaV21.class)
                    .readValue(rootNode.get("items"));
        } catch (IOException | InterruptedException e) {
            throw new RorQueryException("Failed to fetch ROR matches for establishment: " + establishmentName, e);
        }
    }

    @Override
    public Establishment addRorDataToEstablishment(Long establishmentId, RorSchemaV21 ror){
        Establishment est = estRepo.findById(establishmentId);

        if (est == null) {
            LOGGER.warn("No establishment found with establishment id: " + establishmentId);
            throw new NoResultException("No establishment found with establishment id: " + establishmentId);
        }

        String establishmentName = ror.getNames().stream()
                .filter(name -> name.getTypes().contains(Type.ROR_DISPLAY))
                .findFirst()
                .map(Name::getValue)
                .orElse(null);

        String rorId = ror.getId();

        if (establishmentName == null || rorId == null) {
            LOGGER.warn("Cannot update establishment: missing essential ROR data");
            throw new IllegalArgumentException("Cannot update establishment: missing essential ROR data");
        }

        String countryName = ror.getLocations().stream()
                   .findFirst()
                   .map(location -> location.getGeonamesDetails().getCountryName())
                   .orElse(null);

        String establishmentUrl = ror.getLinks().stream()
                   .filter(link -> link.getType().equals(Link.Type.WEBSITE))
                   .findFirst()
                   .map(Link::getValue)
                   .orElse(null);

        est.setEstablishmentName(establishmentName);
        est.setRorId(rorId);
        est.setCountryName(countryName);
        est.setEstablishmentUrl(establishmentUrl);
        est.setVerified(true);

        estRepo.persist(est);
        return est;
    }

    @Override
    public List<EstablishmentAlias> addEstablishmentAliasesFromRor(Long establishmentId, RorSchemaV21 ror) {
        Set<Type> aliasTypes = Set.of(Type.ACRONYM, Type.ALIAS, Type.LABEL);

        List<String> aliasNames = ror.getNames().stream()
                .filter(name -> name.getTypes().stream().anyMatch(aliasTypes::contains) &&
                        !name.getTypes().contains(Type.ROR_DISPLAY))
                .map(Name::getValue)
                .toList();

        return addEstablishmentAliases(establishmentId, aliasNames);
    }

    @Override
    public List<EstablishmentCategoryLink> addEstablishmentCategoryLinksFromRor(Long establishmentId, RorSchemaV21 ror) {
        List<String> categoryNames = ror.getTypes().stream()
                .map(Type_::toString)
                .toList();

        List<Long> categoryIds = categoryNames.stream()
                .map(name -> {
                    Category category = categoryRepo.getByName(name);
                    if (category == null) {
                        throw new NoResultException("No establishment category not found with name: " + name);
                    }
                    return category.getCategoryId();
                })
                .toList();

        return addEstablishmentCategoryLinks(establishmentId, categoryIds);
    }

    @Override
    public Establishment createUnverifiedEstablishment(String name) {
        Establishment unverifiedEst = new Establishment(name);
        unverifiedEst.setVerified(false);
        estRepo.persist(unverifiedEst);
        return unverifiedEst;
    }

    @Override
    public void deleteEstablishment(Long estId) throws NoResultException {
        aliasRepo.delete("establishmentId", estId);
        estCatLinkRepo.delete("establishment.establishmentId", estId);
        estRepo.deleteById(estId);
    }

    @Override
    public Establishment updateEstablishment(Long establishmentId, Establishment updateEst) {
        Establishment est = estRepo.findById(establishmentId);

        if (est == null) {
            LOGGER.info("No establishment found with establishment id: " + establishmentId);
            throw new NoResultException("No establishment found with establishment id: " + establishmentId);
        }

        est.setEstablishmentName(updateEst.getEstablishmentName());
        est.setRorId(updateEst.getRorId());
        est.setCountryName(updateEst.getCountryName());
        est.setEstablishmentUrl(updateEst.getEstablishmentUrl());
        est.setVerified(updateEst.getVerified());

        estRepo.persist(est);
        return est;
    }

    @Override
    public List<Establishment> getUnverifiedEstablishments() {
        return estRepo.getUnverified();
    }

    @Override
    public List<EstablishmentAlias> addEstablishmentAliases(Long establishmentId, List<String> aliasNames) {

        if (estRepo.findById(establishmentId) == null) {
            LOGGER.warn("No establishment found with establishment id: " + establishmentId);
            throw new NoResultException("No establishment found with establishment id: " + establishmentId);
        }

        Set<String> existingAliases = aliasRepo.getAliasesFromEstablishment(establishmentId).stream()
                .map(EstablishmentAlias::getAlias)
                .collect(Collectors.toSet());

        List<EstablishmentAlias> newAliases = aliasNames.stream()
                .filter(aliasName -> !existingAliases.contains(aliasName))
                .map(aliasName -> new EstablishmentAlias(establishmentId, aliasName))
                .toList();

        aliasRepo.persist(newAliases);
        return newAliases;
    }

    @Override
    public List<EstablishmentCategoryLink> addEstablishmentCategoryLinks(Long establishmentId, List<Long> categoryIds) {

        Establishment est = estRepo.findById(establishmentId);

        if (est == null) {
            LOGGER.warn("No establishment found with establishment id: " + establishmentId);
            throw new NoResultException("No establishment found with establishment id: " + establishmentId);
        }

        Set<EstablishmentCategoryLinkId> existingIds = estCatLinkRepo.getFromEstablishment(est.getEstablishmentId())
                .stream()
                .map(EstablishmentCategoryLink::getId)
                .collect(Collectors.toSet());

        List<EstablishmentCategoryLink> newCategoryLinks = categoryIds.stream()
                .map(categoryId -> {
                    Category category = categoryRepo.findById(categoryId);
                    if (category == null) {
                        throw new NoResultException("Category not found for id: " + categoryId);
                    }
                    return new EstablishmentCategoryLink(est, category);
                })
                .filter(link -> !existingIds.contains(link.getId()))
                .toList();

        estCatLinkRepo.persist(newCategoryLinks);
        return newCategoryLinks;
    }

    private List<Establishment> fuzzySearch(String query, Integer cutoff, boolean useAliases, List<Establishment> establishments) {
        List<Pair<Integer, Establishment>> scoredMatches = new ArrayList<>();

        for  (Establishment estModel : establishments){
            int estScore = FuzzySearch.weightedRatio(query, estModel.getEstablishmentName());
            int score = estScore;

            if (useAliases) {
                List<EstablishmentAlias> aliases = aliasRepo.getAliasesFromEstablishment(estModel.getEstablishmentId());

                for (EstablishmentAlias alias : aliases) {
                    int aliasScore = FuzzySearch.weightedRatio(query, alias.getAlias());
                    if (aliasScore > score && aliasScore >= cutoff) {
                        score = aliasScore;
//                        System.out.println(aliasScore + ", " + alias.getAlias());
                    }
                }
            }

            if (score >= cutoff) {
                scoredMatches.add(new ImmutablePair<>(score, estModel));
//                System.out.println(estScore + ", " + estModel.getEstablishmentName());
            }
        }

        scoredMatches.sort((a, b) -> Integer.compare(b.getKey(), a.getKey()));

        return scoredMatches.stream()
                .map(Pair::getValue)
                .toList();
    }
}
