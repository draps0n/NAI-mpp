import java.io.IOException;
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

                List<Double> attributeList = new ArrayList<>();
                while (scanner.hasNextDouble()) {
                    attributeList.add(scanner.nextDouble());
                }

                Observation observation = new Observation(attributeList);
                observation.setCorrectOutput(scanner.next().trim().equals("Iris-setosa") ? 1 : 0);

                observationList.add(observation);
            });
        }

        return observationList;
    }
}
