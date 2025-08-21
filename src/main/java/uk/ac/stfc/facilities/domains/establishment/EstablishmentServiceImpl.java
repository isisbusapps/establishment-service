package uk.ac.stfc.facilities.domains.establishment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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

import static uk.ac.stfc.facilities.helpers.Constants.EST_SEARCH_CUTOFF;

@ApplicationScoped
public class EstablishmentServiceImpl implements EstablishmentService {


    private EstablishmentRepository repo;
    private EstablishmentAliasRepository aliasRepo;
    private EstablishmentTypeRepository typeRepo;

    @Inject
    public EstablishmentServiceImpl(EstablishmentRepository repo, EstablishmentAliasRepository aliasRepo, EstablishmentTypeRepository typeRepo) {
        this.repo = repo;
        this.aliasRepo = aliasRepo;
        this.typeRepo = typeRepo;
    }

    @Override
    public List<Establishment> getAllEstablishments() {
        return List.of();
    }

    @Override
    public List<Establishment> getEstablishmentsByQuery(String searchQuery, boolean useAliases, boolean onlyVerified, int limit) {
        List<Establishment>  allEst = onlyVerified?  repo.getVerified() : repo.getAll();

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
    public Establishment addRorDataToEstablishment(Establishment est, RorSchemaV21 ror){
       String establishmentName = ror.getNames().stream()
                .filter(name -> name.getTypes().contains(Type.ROR_DISPLAY))
                .findFirst()
                .map(Name::getValue)
                .orElse(null);

       String rorId = ror.getId();

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

       repo.save(est);
       return est;
    }

    @Override
    public List<EstablishmentAlias> createEstablishmentAliasesFromRor(Establishment est, RorSchemaV21 ror) {

        Set<Type> aliasTypes = Set.of(Type.ACRONYM, Type.ALIAS, Type.LABEL);

        List<String> aliases = ror.getNames().stream()
                .filter(name -> name.getTypes().stream().anyMatch(aliasTypes::contains) &&
                        !name.getTypes().contains(Type.ROR_DISPLAY))
                .map(Name::getValue)
                .toList();

        List<EstablishmentAlias> establishmentAliases = new ArrayList<>();

        for (String alias : aliases) {
            EstablishmentAlias establishmentAlias = new EstablishmentAlias(est.getEstablishmentId(), alias);
            establishmentAliases.add(establishmentAlias);
        }

        return establishmentAliases;
    }

    @Override
    public List<EstablishmentType> createEstablishmentTypesFromRor(Establishment est, RorSchemaV21 ror) {

        List<String> types = ror.getTypes().stream()
                .map(Type_::toString)
                .toList();

        List<EstablishmentType> establishmentTypes = new ArrayList<>();

        for (String type : types) {
            EstablishmentType establishmentType = new EstablishmentType(est.getEstablishmentId(), type);
            establishmentTypes.add(establishmentType);
        }

        return establishmentTypes;
    }

    @Override
    @Transactional
    public Establishment createUnverifiedEstablishment(String name) {
        Establishment unverifiedEst = new Establishment(name);
        unverifiedEst.setVerified(false);
        repo.save(unverifiedEst);
        return unverifiedEst;
    }

    @Override
    public Establishment deleteEstablishment(Long estId) throws NoResultException {
        return null;
    }

    @Override
    public Establishment updateEstablishment(Establishment existingEst, Establishment updateEst) {
        existingEst.setEstablishmentName(updateEst.getEstablishmentName());
        existingEst.setRorId(updateEst.getRorId());
        existingEst.setCountryName(updateEst.getCountryName());
        existingEst.setEstablishmentUrl(updateEst.getEstablishmentUrl());
        existingEst.setVerified(updateEst.getVerified());

        return existingEst;
    }

    @Override
    public Establishment getEstablishmentById(Long establishmentId) {
        return null;
    }

    @Override
    public List<Establishment> getUnverifiedEstablishments() {
        return repo.getUnverified();
    }

    @Override
    public List<EstablishmentAlias> addEstablishmentAliases(List<EstablishmentAlias> aliases) {
        aliasRepo.add(aliases);
        return aliases;
    }

    @Override
    public List<EstablishmentType> addEstablishmentTypes(List<EstablishmentType> types) {
        typeRepo.add(types);
        return types;
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
