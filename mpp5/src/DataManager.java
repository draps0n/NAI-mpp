import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
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

                String output = scanner.next().trim();
                Observation observation = new Observation(attributeList, output);

                observationList.add(observation);
            });
        }

        return observationList;
    }
}
