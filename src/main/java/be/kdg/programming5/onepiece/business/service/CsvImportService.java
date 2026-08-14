package be.kdg.programming5.onepiece.business.service;

import java.util.List;

public interface CsvImportService {

    void importCharacters(List<String> csvLines, String uploaderUsername);
}
