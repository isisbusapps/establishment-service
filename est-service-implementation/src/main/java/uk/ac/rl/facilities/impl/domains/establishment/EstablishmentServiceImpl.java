package uk.ac.rl.facilities.impl.domains.establishment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import uk.ac.rl.facilities.impl.exceptions.RorQueryException;
import uk.ac.rl.facilities.impl.mappers.CategoryMapper;
import uk.ac.rl.facilities.impl.mappers.EstablishmentAliasMapper;
import uk.ac.rl.facilities.impl.mappers.EstablishmentMapper;
import uk.rl.ac.facilities.api.domains.establishment.*;
import uk.rl.ac.facilities.api.exceptions.EntityNotFoundException;
import uk.rl.ac.facilities.facilities.api.generated.ror.*;

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


@ApplicationScoped
public class EstablishmentServiceImpl implements EstablishmentService {

    private static final Logger LOGGER = Logger.getLogger(EstablishmentService.class);

    public static final int EST_SEARCH_CUTOFF = 60;

    private EstablishmentRepository estRepo;
    private CategoryRepository categoryRepo;
    private EstablishmentCategoryLinkRepository estCatLinkRepo;
    private EstablishmentAliasRepository aliasRepo;
    private CountryRepository countryRepo;
    private EstablishmentMapper estMapper;
    private EstablishmentAliasMapper aliasMapper;
    private CategoryMapper categoryMapper;


    public EstablishmentServiceImpl() {}

    @Inject
    public EstablishmentServiceImpl(EstablishmentRepository estRepo,
                                    CategoryRepository categoryRepo,
                                    EstablishmentCategoryLinkRepository establishmentCategoryLinkRepo,
                                    EstablishmentAliasRepository aliasRepo,
                                    EstablishmentMapper mapper,
                                    EstablishmentAliasMapper aliasMapper,
                                    CategoryMapper categoryMapper) {
        this.estRepo = estRepo;
        this.categoryRepo = categoryRepo;
        this.estCatLinkRepo = establishmentCategoryLinkRepo;
        this.aliasRepo = aliasRepo;
        this.estMapper = mapper;
        this.aliasMapper = aliasMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<EstablishmentModel> getAllEstablishments() {return estMapper.toModel(estRepo.listAll());}

    @Override
    public EstablishmentModel getEstablishment(Long establishmentId) {
        return estMapper.toModel(estRepo.findByIdOptional(establishmentId).orElseThrow(
                () -> new EntityNotFoundException(Establishment.class.getName(), establishmentId)));
    }

    @Override
    public List<EstablishmentModel> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit) {
        List<Establishment>  allEst = onlyVerified?  estRepo.getVerified() : estRepo.getAll();

        List<Establishment> found = fuzzySearch(searchQuery, EST_SEARCH_CUTOFF, useAliases, allEst);
        return estMapper.toModel(found)
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<RorSchemaV21> getRorMatches(String establishmentName) {
        try {
            String encodedQuery = URLEncoder.encode(establishmentName, StandardCharsets.UTF_8);
            String url = "https://api.ror.org/v2/organizations?query=" + encodedQuery; //TODO fix this

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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public EstablishmentModel addRorDataToEstablishment(Long establishmentId, RorSchemaV21 ror){
        Establishment est = estRepo.findByIdOptional(establishmentId).orElseThrow(() -> new EntityNotFoundException(Establishment.class.getName(), establishmentId));

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
        return estMapper.toModel(est);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<AliasModel> addEstablishmentAliasesFromRor(Long establishmentId, RorSchemaV21 ror) {
        Set<Type> aliasTypes = Set.of(Type.ACRONYM, Type.ALIAS, Type.LABEL);

        List<String> aliasses = ror.getNames().stream()
                .filter(name -> name.getTypes().stream().anyMatch(aliasTypes::contains) &&
                        !name.getTypes().contains(Type.ROR_DISPLAY))
                .map(Name::getValue)
                .toList();
        return addEstablishmentAliases(establishmentId, aliasses);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<CategoryModel> addEstablishmentCategoryLinksFromRor(Long establishmentId, RorSchemaV21 ror) {
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
    public EstablishmentModel createUnverifiedEstablishment(String name) {
        Establishment unverifiedEst = new Establishment(name);
        unverifiedEst.setVerified(false);
        estRepo.persist(unverifiedEst);
        return estMapper.toModel(unverifiedEst);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void deleteEstablishment(Long estId) throws NoResultException {
        estRepo.deleteById(estId);
    }

    @Override
    public EstablishmentModel updateEstablishment(Long establishmentId, EstablishmentModel updateEst) {
        Establishment est = estRepo.findById(establishmentId);

        if (est == null) {
            LOGGER.info("No establishment found with establishment id: " + establishmentId);
            throw new NoResultException("No establishment found with establishment id: " + establishmentId);
        }

        est.setEstablishmentName(updateEst.getName());
        est.setRorId(updateEst.getRorID());
        est.setCountryName(updateEst.getCountry());
        est.setEstablishmentUrl(updateEst.getUrl().toString());
        est.setVerified(updateEst.getVerified());

        estRepo.persist(est);
        return estMapper.toModel(est);
    }

    @Override
    public List<EstablishmentModel> getUnverifiedEstablishments() {
        return estMapper.toModel(estRepo.getUnverified());
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<AliasModel> addEstablishmentAliases(Long establishmentId, List<String> aliasNames) {

        Establishment est = estRepo.findByIdOptional(establishmentId).orElseThrow(() -> new EntityNotFoundException(Establishment.class.getName(), establishmentId));
        if (est == null) {
            LOGGER.warn("No establishment found with establishment id: " + establishmentId);
            throw new NoResultException("No establishment found with establishment id: " + establishmentId);
        }

        Set<String> existingAliases = aliasRepo.getAliasesFromEstablishment(establishmentId).stream()
                .map(EstablishmentAlias::getAlias)
                .collect(Collectors.toSet());

        List<EstablishmentAlias> newAliases = aliasNames.stream()
                .filter(aliasName -> !existingAliases.contains(aliasName))
                .map(aliasName -> new EstablishmentAlias(est, aliasName))
                .toList();

        aliasRepo.persist(newAliases);
        return newAliases.stream().map(aliasMapper::toModel).collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<CategoryModel> addEstablishmentCategoryLinks(Long establishmentId, List<Long> categoryIds) {

        Establishment est = estRepo.findByIdOptional(establishmentId).orElseThrow(() -> new EntityNotFoundException(Establishment.class.getName(), establishmentId));

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
        return estMapper.toModel(estRepo.findByIdOptional(establishmentId).orElseThrow(RuntimeException::new)).getCategories();
    }

    @Override
    public List<CategoryModel> getCategoriesForEstablishment(Long establishmentId) {
        return  estCatLinkRepo.findCategoriesLinkedToEstablishment(establishmentId).stream().map(categoryMapper::toModel).toList();
    }

    @Override
    public List<AliasModel> getAliasesForEstablishment(Long establishmentId) {
        return aliasRepo.getAliasesFromEstablishment(establishmentId).stream().map(aliasMapper::toModel).toList();
    }

    @Override
    public List<CountryModel> getAllCountries() {
        return countryRepo.listAll().stream().map(countryMapper::toModel).toList();
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
                    }
                }
            }

            if (score >= cutoff) {
                scoredMatches.add(new ImmutablePair<>(score, estModel));
            }
        }

        scoredMatches.sort((a, b) -> Integer.compare(b.getKey(), a.getKey()));

        return scoredMatches.stream()
                .map(Pair::getValue)
                .toList();
    }
}
