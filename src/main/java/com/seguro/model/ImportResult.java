package main.java.com.seguro.model;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private int processedCount;
    private List<String> errors = new ArrayList<>();

    public void addError(String error) {
        errors.add(error);
    }

    public void incrementProcessed() {
        processedCount++;
    }

    public int getProcessedCount() { return processedCount; }
    public List<String> getErrors() { return errors; }
}