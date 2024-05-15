import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            List<Observation> learningSet = DataManager.readFile("mpp4/data/iris_training.txt", ',');
            List<Observation> testSet = DataManager.readFile("mpp4/data/iris_test.txt", ',');

            // Dyskretyzacja danych na k grup
            Digitizer digitizer = new Digitizer(6);
            digitizer.findIntervals(learningSet);
            digitizer.digitize(learningSet);
            digitizer.digitize(testSet);

            NaiveBayes naiveBayes = new NaiveBayes(digitizer.getBinsCount());
            naiveBayes.fit(learningSet);
            naiveBayes.predict(testSet);

            Scanner sc = new Scanner(System.in);
            while(true) {
                System.out.println("Enter " + learningSet.getFirst().getDimensionsCount() + " dimensional vector:");
                String line = sc.nextLine();
                String[] parts = line.split(", ");
                if (parts.length != learningSet.getFirst().getDimensionsCount()) {
                    System.err.println("Wrong input!");
                    continue;
                }
                List<Double> attrList = new ArrayList<>();
                for (String part : parts) {
                    attrList.add(Double.parseDouble(part));
                }
                Observation observation = new Observation(attrList, "NaN");
                digitizer.digitize(observation);
                System.out.println(observation);
                String prediction = naiveBayes.predict(observation);
                System.out.println("It should be " + prediction);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
