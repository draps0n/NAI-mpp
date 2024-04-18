import java.io.IOException;
import java.util.*;

public class Main {
    private static Scanner scanner;
    private static Analyzer analyzer;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

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

        int k;
        do {
            System.out.print("Enter k ∈ N ∧ k ∈ (0; " + trainingObservations.size() + "> : ");
            k = scanner.nextInt();
        } while (k > trainingObservations.size() || k <= 0);

        analyzer = new Analyzer(trainingObservations, k, true);

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
        for (Observation observation : testObservations) {
            analyzer.analyze(observation);
        }

        System.out.println(analyzer.getStats());

        userInput();
    }

    private static void userInput() {
        String input;
        List<Double> attributeList = new ArrayList<>(analyzer.getAttributeCount());
        do {
            System.out.println("Do you want to check more observations? (yes/no)");
            input = scanner.next();

            if (input.equals("yes")) {
                for (int i = 0; i < analyzer.getAttributeCount(); i++) {
                    System.out.print("Enter attribute no. " + (i + 1) + ": ");
                    attributeList.add(i, scanner.nextDouble());
                }

                Observation obs = new Observation(attributeList);
                analyzer.analyze(obs);
                System.out.println();
            }
        } while (!input.equals("no"));
    }
}