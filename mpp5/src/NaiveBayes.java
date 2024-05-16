import java.util.*;

public class NaiveBayes {
    private Map<String, List<Observation>> dataMap;
    private final int digitizedTo;

    public NaiveBayes(int digitizedTo) {
        this.digitizedTo = digitizedTo;
    }

    public void fit(List<Observation> learningSet) {
        dataMap = new HashMap<>();
        for (Observation observation : learningSet) {
            List<Observation> tmp = dataMap.getOrDefault(observation.getCorrectOutput(), new ArrayList<>());
            tmp.add(observation);
            dataMap.put(observation.getCorrectOutput(), tmp);
        }
    }

    public void predict(List<Observation> testSet) {
        double correctCounter = 0;
        double totalCounter = testSet.size();
        Map<String, Map<String, Integer>> confusionMatrix = new TreeMap<>();
        for (String prediction : dataMap.keySet()) {
            Map<String, Integer> innerMap = new TreeMap<>();
            for (String pred2 : dataMap.keySet()) {
                innerMap.put(pred2, 0);
            }
            confusionMatrix.put(prediction, innerMap);
        }

        for (Observation observation : testSet) {
            String prediction = predict(observation);
            String expectedPrediction = observation.getCorrectOutput();

            Map<String, Integer> innerMap = confusionMatrix.get(expectedPrediction);
            innerMap.put(prediction, innerMap.get(prediction) + 1);
            confusionMatrix.put(expectedPrediction, innerMap);

            System.out.print(observation.getCoordinates() + " ==> " + prediction);
            if (prediction.equals(expectedPrediction)) {
                System.out.println("\u001B[32m" + " (expected: " + expectedPrediction + ")\u001B[0m");
                correctCounter++;
            } else {
                System.out.println("\u001B[31m" + " (expected: " + expectedPrediction + ")\u001B[0m");
            }
        }

        // Ogólna precyzja
        System.out.println("Accuracy = " + (correctCounter / totalCounter * 100) + "%\n");

        // Macierz omyłek
        System.out.println("Confusion matrix: ");
        for (Map.Entry<String, Map<String, Integer>> entry : confusionMatrix.entrySet()) {
            System.out.println(entry);
        }
        System.out.println();

        // Precyzja, pełność i F-miara dla gatunku
        for (Map.Entry<String, Map<String, Integer>> entry : confusionMatrix.entrySet()) {
            System.out.println(entry.getKey() + ":");
            Map<String, Integer> classConfusionMatrix = createClassConfusionMatrix(entry.getKey(), confusionMatrix);
            double precision = getPrecision(classConfusionMatrix);
            double recall = getRecall(classConfusionMatrix);
            double fMeasure = (2 * precision * recall) / (precision + recall);
            System.out.println("\t- " + classConfusionMatrix);
            System.out.println("\t- precision = " + precision);
            System.out.println("\t- recall = " + recall);
            System.out.println("\t- F-Measure = " + fMeasure);
            System.out.println();
        }
    }

    private Map<String, Integer> createClassConfusionMatrix(String className, Map<String, Map<String, Integer>> confusionMatrix) {
        Map<String, Integer> classConfusionMatrix = new TreeMap<>();
        classConfusionMatrix.put("TP", 0);
        classConfusionMatrix.put("TN", 0);
        classConfusionMatrix.put("FP", 0);
        classConfusionMatrix.put("FN", 0);

        for (Map.Entry<String, Map<String, Integer>> entry : confusionMatrix.entrySet()) {
            String expectedPrediction = entry.getKey();
            Map<String, Integer> innerMap = entry.getValue();

            if (className.equals(expectedPrediction)) {
                for (Map.Entry<String, Integer> innerEntry : innerMap.entrySet()) {
                    String prediction = innerEntry.getKey();
                    int count = innerEntry.getValue();
                    if (prediction.equals(className))
                        classConfusionMatrix.put("TP", count);
                    else
                        classConfusionMatrix.put("FN", classConfusionMatrix.get("FN") + count);
                }
            } else {
                for (Map.Entry<String, Integer> innerEntry : innerMap.entrySet()) {
                    String prediction = innerEntry.getKey();
                    int count = innerEntry.getValue();
                    if (prediction.equals(className))
                        classConfusionMatrix.put("FP", classConfusionMatrix.get("FP") + count);
                    else
                        classConfusionMatrix.put("TN", classConfusionMatrix.get("TN") + count);
                }
            }
        }

        return classConfusionMatrix;
    }

    private double getPrecision(Map<String, Integer> classConfusionMatrix) {
        return ((double) classConfusionMatrix.get("TP")) / (classConfusionMatrix.get("TP") + classConfusionMatrix.get("FP"));
    }

    private double getRecall(Map<String, Integer> classConfusionMatrix) {
        return ((double) classConfusionMatrix.get("TP")) / (classConfusionMatrix.get("TP") + classConfusionMatrix.get("FN"));
    }

    public String predict(Observation observation) {
        Map<String, Double> probabilitiesMap = new HashMap<>();
        int dimCount = observation.getDimensionsCount();

        for (String possibility : dataMap.keySet()) {
            double probability = 1.;

            for (int dim = 0; dim < dimCount; dim++) {
                double value = observation.getCoordinate(dim);
                probability *= calculateConditionalProbability(value, dim, possibility);
            }
            probability *= calculateYProbability(possibility);

            probabilitiesMap.put(possibility, probability);
        }

        return findMaxProbability(probabilitiesMap);
    }

    // P(X{dim}=value | Y=condition)
    private double calculateConditionalProbability(double value, int dim, String condition) {
        List<Observation> observations = dataMap.get(condition);

        double matchesCounter = 0;
        double totalAmount = observations.size();
        for (Observation observation : observations) {
            double currVal = observation.getCoordinate(dim);
            if (currVal == value) {
                matchesCounter++;
            }
        }

        double probability = matchesCounter / totalAmount;
        if (probability == 0.) {
            System.out.print("P(X" + dim + "=" + value + " | Y=" + condition + ") = ");
            System.out.print(probability + " -> ");
            matchesCounter++;
            totalAmount += digitizedTo;
            probability = matchesCounter / totalAmount;
            System.out.println(probability);
        }

        return probability;
    }

    // P(condition)
    private double calculateYProbability(String condition) {
        double probability = dataMap.get(condition).size();
        double totalSize = 0;
        for (List<Observation> value : dataMap.values()) {
            totalSize += value.size();
        }

        probability /= totalSize;

        return probability;
    }

    private String findMaxProbability(Map<String, Double> probabilityMap) {
        String possibility = null;
        Double maxProbability = Double.MIN_VALUE;

        for (Map.Entry<String, Double> entry : probabilityMap.entrySet()) {
            if (entry.getValue() > maxProbability) {
                maxProbability = entry.getValue();
                possibility = entry.getKey();
            }
        }

        return possibility;
    }
}
