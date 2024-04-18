import java.io.IOException;
import java.util.*;

public class Main {
    private static Scanner scanner;
    private static Perceptron perceptron;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.println("Enter path to file with training data:");
        String path = scanner.nextLine();
        List<Observation> trainingObservations = getDataFromFile(path);

        perceptron = new Perceptron();
        perceptron.train(trainingObservations);

        System.out.println("Enter path to file with test data:");
        path = scanner.nextLine();
        List<Observation> testObservations = getDataFromFile(path);

        for (Observation observation : testObservations) {
            perceptron.analyze(observation);
        }

        System.out.println(perceptron.getStats());

        userInput();
    }

    private static List<Observation> getDataFromFile(String path) {
        char decimalSeparator;
        do {
            System.out.println("Enter decimal separator for this file (, or .):");
            decimalSeparator = scanner.nextLine().charAt(0);
        } while (decimalSeparator != '.' && decimalSeparator != ',');

        List<Observation> observationList;
        try {
            observationList = DataManager.readFile(path, decimalSeparator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return observationList;
    }

    private static void userInput() {
        String input;
        List<Double> attributeList = new ArrayList<>(perceptron.getAttributeCount());
        do {
            System.out.println("Do you want to check more observations? (yes/no)");
            input = scanner.next();

            if (input.equals("yes")) {
                System.out.println("\t!INPUT WITH ',' AS DECIMAL SEPARATOR!");
                for (int i = 0; i < perceptron.getAttributeCount(); i++) {
                    System.out.print("Enter attribute no. " + (i + 1) + ": ");
                    attributeList.add(i, scanner.nextDouble());
                }

                Observation obs = new Observation(attributeList);
                perceptron.analyze(obs);
                System.out.println();
            }
        } while (!input.equals("no"));
    }
}