package uk.ac.stfc.facilities.domains.establishment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EstablishmentRepositoryCSV implements EstablishmentRepository {

    private static final Logger LOGGER = LogManager.getLogger();
    private final List<Establishment> establishments = new ArrayList<>();

    public EstablishmentRepositoryCSV() {

        try {
            loadFromCsv("src/main/resources/establishment-sample-data/sample_establishments.csv");
        } catch (IOException e) {
            LOGGER.fatal("Failed to load sample_establishments.csv", e);
        }
    }

    private void loadFromCsv(String filePath) throws IOException {
        establishments.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] fields = line.split(",", -1);

                Long id = Long.parseLong(fields[0].trim());
                String name = fields[1].trim();
                String rorId = fields[2].trim();
                String country = fields[3].trim();
                String url = fields[4].trim();
                Instant fromDate = fields[5].isEmpty() ? null : Instant.parse(fields[5].trim());
                Instant thruDate = fields[6].isEmpty() ? null : Instant.parse(fields[6].trim());
                Boolean verified = fields[7].isEmpty() ? null : Boolean.parseBoolean(fields[7].trim());

                Establishment model = new Establishment(id, name, rorId, country, url, fromDate, thruDate, verified);
                establishments.add(model);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Establishment findById(Long estId) {
        return null;
    }

    @Override
    public List<Establishment> getAll() {
        return establishments;
    }

    @Override
    public List<Establishment> getVerified() {
        return establishments.stream()
                .filter(Establishment::getVerified)
                .collect(Collectors.toList());
    }

    @Override
    public List<Establishment> getUnverified() { return List.of(); }

    @Override
    public void save(Establishment establishment) {

    }

    @Override
    public void update(Long estId, Establishment establishment) {

    }

    @Override
    public void delete(Long estId) {

    }
}
