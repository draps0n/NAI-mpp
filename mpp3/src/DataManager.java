import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.*;

public class DataManager {
    public static List<String> knownLanguages;

    public static Map<Character, Double> readFile(File file) throws IOException {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        int letterCounter = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                for (char c : line.toCharArray()) {
                    if (Character.isLetter(c)) {
                        letterCounter++;
                        frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
                    }
                }
            }
        }

        Map<Character, Double> percentageMap = new HashMap<>();
        for (char c = 'a'; c <= 'z'; c++) {
            double freq = (double) frequencyMap.getOrDefault(c, 0) / (double) letterCounter;
            percentageMap.put(c, freq);
        }

        return percentageMap;
    }

    public static Map<Character, Double> getCharacterMapFor(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        text = text.toLowerCase();
        int letterCounter = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                letterCounter++;
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }

        Map<Character, Double> percentageMap = new HashMap<>();
        for (char c = 'a'; c <= 'z'; c++) {
            double freq = (double) frequencyMap.getOrDefault(c, 0) / (double) letterCounter;
            percentageMap.put(c, freq);
        }

        return percentageMap;
    }

    public static List<Observation> readAllObservationsFromDirectory(String directoryName, ReadType readType) throws IOException {
        List<Observation> observations = new ArrayList<>();
        if (readType == ReadType.TRAINING) {
            knownLanguages = new ArrayList<>();
        }

        File mainFile = new File(directoryName);
        if (!mainFile.isDirectory())
            throw new NotDirectoryException(directoryName + " is not directory");

        File[] languagesFolders = mainFile.listFiles(File::isDirectory);
        if (languagesFolders == null)
            throw new IOException();

        for (File languageFolder : languagesFolders) {
            File[] textFiles = languageFolder.listFiles(File::isFile);

            if (textFiles == null)
                continue;

            String languageName = languageFolder.getName();
            if (readType == ReadType.TRAINING) {
                knownLanguages.add(languageName);
            }

            for (File textFile : textFiles) {
                Map<Character, Double> letterPercentageMap = readFile(textFile);
                Observation observation = new Observation(letterPercentageMap, languageName);
                observations.add(observation);
            }
        }

        return observations;
    }
}