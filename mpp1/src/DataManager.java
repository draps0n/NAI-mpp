import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DataManager {

    public static List<Observation> readFile(String path, char decimalSeparator) throws IOException {
        Path filePath = Paths.get(path);
        List<Observation> observationList = new ArrayList<>();

        try (Stream<String> linesStream = Files.lines(filePath)) {
            linesStream.forEach(line -> {
                Scanner scanner;

                if (decimalSeparator == '.') {
                    scanner = new Scanner(line).useLocale(Locale.ENGLISH);
                } else if (decimalSeparator == ',') {
                    scanner = new Scanner(line).useLocale(Locale.FRANCE);
                } else {
                    scanner = new Scanner(line);
                }

                Observation observation = new Observation();

                while (scanner.hasNextDouble()) {
                    observation.addAttribute(scanner.nextDouble());
                }
                observation.setCorrectDecision(scanner.next());

                observationList.add(observation);
            });
        }

        return observationList;
    }

    public static void saveTsvFile(String fileName, Map<Integer, BigDecimal> map, char decimalSeparator) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results/" + fileName + ".txt"))) {
            bufferedWriter.write("k");
            bufferedWriter.write("\t");
            bufferedWriter.write("percentage");
            bufferedWriter.newLine();

            char from = decimalSeparator == '.' ? ',' : '.';
            for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
                bufferedWriter.write(entry.getKey().toString());
                bufferedWriter.write("\t");
                bufferedWriter.write(entry.getValue().toString().replace(from, decimalSeparator));
                bufferedWriter.newLine();
            }
        }
    }
}
