package uk.ac.rl.facilities.impl.domains.establishment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EstablishmentAliasRepositoryCSV implements EstablishmentAliasRepository {

    private static final Logger LOGGER = LogManager.getLogger();
    private List<EstablishmentAlias> establishmentAliases = new ArrayList<>();

    public EstablishmentAliasRepositoryCSV() {

        try {
            loadFromCsv("src/main/resources/establishment-sample-data/sample_aliases.csv");
        } catch (IOException e) {
            LOGGER.fatal("Failed to load sample_establishments.csv", e);

        }
    }

    @Override
    public List<EstablishmentAlias> getAliasesFromEstablishment(Long establishmentId) {
        return establishmentAliases.stream()
                .filter(e -> e.getEstablishmentId().equals(establishmentId))
                .collect(Collectors.toList());
    }

    private void loadFromCsv(String filePath) throws IOException {
        establishmentAliases.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] fields = line.split(",", -1);  // -1 keeps empty trailing fields

                Long rid = Long.parseLong(fields[0].trim());
                Long establishmentId = Long.valueOf(fields[1].trim());
                String alias = fields[2].trim();

                EstablishmentAlias model = new EstablishmentAlias(rid, establishmentId, alias);
                establishmentAliases.add(model);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
