import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerceptronLayer {
    private final Map<String, Perceptron> perceptronMap;

    public PerceptronLayer(List<String> languages) {
        this.perceptronMap = new HashMap<>();

        for (String language : languages) {
            perceptronMap.put(language, new Perceptron());
        }
    }

    public void trainLayer(List<Observation> trainingSet) {
        double epochError = Double.MAX_VALUE;
        final int trainingSize = trainingSet.size();
        int trainingHits;
        int trainingRounds = 0;
        double lastAccuracy = 0;

        while (epochError > 0.01) {
            trainingHits = 0;
            epochError = 0.;
            trainingRounds++;
            for (Observation observation : trainingSet) {
                List<Double> inputs = observation.getInputs();
//                inputs.add(-1.); // TODO : czy musi mieć próg?

                Map<String, Double> outputsMap = new HashMap<>();

                for (Map.Entry<String, Perceptron> perceptronEntry : perceptronMap.entrySet()) {
                    String languageName = perceptronEntry.getKey();
                    Perceptron perceptron = perceptronEntry.getValue();

                    double expectedOutput = languageName.equals(observation.getCorrectOutput()) ? 1 : -1;
                    double output = perceptron.getOutputFor(inputs);
                    outputsMap.put(languageName, output);

//                    System.out.print(languageName + ": ");
                    perceptron.adjustWeights(inputs, output, expectedOutput);

//                    epochError += Math.abs(expectedOutput - output); // błąd "absolutny"
                    epochError += Math.pow(expectedOutput - output, 2) * 0.5; // błąd kwadratowy
                }
//                inputs.removeLast();

                String finalOutput = getResultForOutputs(outputsMap);
                if (finalOutput.equals(observation.getCorrectOutput())) {
                    trainingHits++;
                }
            }
            epochError /= trainingSet.size(); // TODO : czy to dzielenie w porządku?
            lastAccuracy = ((double) trainingHits / trainingSize);
            System.out.println(trainingRounds + " -> " + lastAccuracy * 100 + "% (error = " + epochError + " )");
        }
    }

    public void testLayer(List<Observation> testSet) {
        int correct = 0;
        final int toGuess = testSet.size();

        for (Observation observation : testSet) {
            List<Double> inputs = observation.getInputs();
//            inputs.add(-1.); // TODO : czy musi mieć próg?

            Map<String, Double> outputsMap = new HashMap<>();

            for (Map.Entry<String, Perceptron> perceptronEntry : perceptronMap.entrySet()) {
                String languageName = perceptronEntry.getKey();
                Perceptron perceptron = perceptronEntry.getValue();

                double net = perceptron.getOutputFor(inputs);
                outputsMap.put(languageName, net);
            }
//            inputs.removeLast();

            String finalOutput = getResultForOutputs(outputsMap);
            System.out.print("Result = " + finalOutput);
            if (finalOutput.equals(observation.getCorrectOutput())) {
                correct++;
                System.out.println("\u001B[32m" + " (expected: " + observation.getCorrectOutput() + ")\u001B[0m");
            } else {
                System.out.println("\u001B[31m" + " (expected: " + observation.getCorrectOutput() + ")\u001B[0m");
            }
        }

        double accuracy = ((double) correct / toGuess);
        System.out.println("Test Accuracy: " + accuracy * 100 + "%");
    }

    public String classify(Observation observation) {
        List<Double> inputs = observation.getInputs();
//        inputs.add(-1.); // TODO : czy musi mieć próg?

        Map<String, Double> outputsMap = new HashMap<>();
        for (Map.Entry<String, Perceptron> perceptronEntry : perceptronMap.entrySet()) {
            String languageName = perceptronEntry.getKey();
            Perceptron perceptron = perceptronEntry.getValue();

            double output = perceptron.getOutputFor(inputs);
            outputsMap.put(languageName, output);
        }
//        inputs.removeLast();

        return getResultForOutputs(outputsMap);
    }

    private String getResultForOutputs(Map<String, Double> outputs) {
        if (outputs == null || outputs.isEmpty()) {
            return null;
        }

        String maxKey = null;
        double maxValue = Double.NEGATIVE_INFINITY;

        for (Map.Entry<String, Double> entry : outputs.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                maxKey = entry.getKey();
            }
        }

        return maxKey;
    }
}
