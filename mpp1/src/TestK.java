import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TestK {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter path to file with training data:");
        String path = scanner.next();
        char decimalSeparator;
        do {
            System.out.println("Enter decimal separator for this file (, or .):");
            decimalSeparator = scanner.next().charAt(0);
        } while (decimalSeparator != '.' && decimalSeparator != ',');

        List<Observation> trainingObservations;
        try {
            trainingObservations = DataManager.readFile(path, decimalSeparator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Enter path to file with test data:");
        path = scanner.next();
        do {
            System.out.println("Enter decimal separator for this file (, or .):");
            decimalSeparator = scanner.next().charAt(0);
        } while (decimalSeparator != '.' && decimalSeparator != ',');

        List<Observation> testObservations;
        try {
            testObservations = DataManager.readFile(path, decimalSeparator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Analyzer analyzer = new Analyzer(trainingObservations, 0, false);

        Map<Integer, BigDecimal> kMap = new LinkedHashMap<>();
        for (int k = 1; k <= trainingObservations.size(); k++) {
            analyzer.setK(k);
            for (Observation observation : testObservations) {
                analyzer.analyze(observation);
            }

            System.out.print("k = " + k);
            System.out.println(analyzer.getStats());
            kMap.put(k, analyzer.getPercentage(6));
        }

        System.out.println("RESULTS: ");
        for (Map.Entry<Integer, BigDecimal> entry : kMap.entrySet()) {
            System.out.println("k = " + entry.getKey() + " ==> " + entry.getValue() + "%");
        }

        String input;
        do {
            System.out.println("\nWould you like to save the data to .tsv file? (yes/no)");
            input = scanner.next();

            if (input.equals("yes")) {
                System.out.println("Enter file name (without extension):");
                input = scanner.next();

                do {
                    System.out.println("Enter decimal separator for this file (, or .):");
                    decimalSeparator = scanner.next().charAt(0);
                } while (decimalSeparator != '.' && decimalSeparator != ',');

                try {
                    DataManager.saveTsvFile(input, kMap, decimalSeparator);
                    System.out.println("File saved to /results/" + input + ".txt");
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } while (!input.equals("no"));
    }
}
