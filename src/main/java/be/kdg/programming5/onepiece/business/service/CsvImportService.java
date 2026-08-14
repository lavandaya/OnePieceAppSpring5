package be.kdg.programming5.onepiece.business.service;

import java.util.List;

public interface CsvImportService {

    // Runs on a different thread than the caller; invalid rows are skipped and logged.
    void importCharacters(List<String> csvLines, String uploaderUsername);
}
