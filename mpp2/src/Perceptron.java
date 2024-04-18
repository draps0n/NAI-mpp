import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Perceptron {
    private int attributeCount;
    private int correct;
    private int checkedWithAnswer;
    private List<Double> weights;
    private final double alpha;

    public Perceptron() {
        this.weights = new ArrayList<>();
        alpha = 1.;
    }

    public void train(List<Observation> trainingObservations) {
        attributeCount = trainingObservations.getFirst().getAttributeCount();

        for (int i = 0; i < attributeCount; i++) {
            weights.add(1.);
        }
        weights.add(0.);

        final int trainingSize = trainingObservations.size();
        int trainingHits;
        double lastAccuracy = 0;
        int trainingRounds = 0;

        while (lastAccuracy < 1. && trainingRounds < 500) {
            trainingRounds++;
            trainingHits = 0;

            for (Observation obs : trainingObservations) {
                List<Double> inputs = new ArrayList<>(obs.getAttributeList());
                inputs.add(-1.);

                double net = calculateDotProduct(weights, inputs);
                int output = net >= 0 ? 1 : 0;

                if (output == obs.getCorrectOutput()) {
                    trainingHits++;
                } else {
                    adjustWeights(inputs, output, obs.getCorrectOutput());
                }

                inputs.removeLast();
            }

            lastAccuracy = (double) trainingHits / trainingSize;
        }

        System.out.println("Training phase ended with " + getPercentage(lastAccuracy * 100, 2) + "% accuracy after "
                + trainingRounds + " repeats.");
    }

    private void adjustWeights(List<Double> inputs, int decision, int correctDecision) {
        List<Double> productList = multiplyMatrix(inputs, alpha * (correctDecision - decision));
        weights = addMatrices(weights, productList);
    }

    public int getOutputFor(Observation observation) {
        List<Double> inputs = new ArrayList<>(observation.getAttributeList());
        inputs.add(-1.);
        double net = calculateDotProduct(weights, inputs);
        inputs.removeLast();

        return net >= 0 ? 1 : 0;
    }

    public String translateOutput(int output) {
        return output == 1 ? "It is Iris-setosa" : "It is not Iris-setosa";
    }

    public void analyze(Observation observation) {
        int output = getOutputFor(observation);
        String result = translateOutput(output);
        System.out.print(observation.getAttributeList() + " ==> " + result);

        if (observation.hasCorrectDecision()) {
            checkedWithAnswer++;
            if (output == observation.getCorrectOutput()) {
                correct++;
                System.out.println("\u001B[32m" + " (expected: " + translateOutput(observation.getCorrectOutput()) + ")\u001B[0m");
            } else {
                System.out.println("\u001B[31m" + " (expected: " + translateOutput(observation.getCorrectOutput()) + ")\u001B[0m");
            }
        } else {
            System.out.println();
        }

    }

    public String getStats() {
        return "\nCorrect decisions: " +
                correct +
                "\nTotal no. of checked: " +
                checkedWithAnswer +
                "\nHit percentage: " +
                getPercentage(2) +
                " %\n";
    }

    public BigDecimal getPercentage(int precision) {
        double percentage = (double) correct / checkedWithAnswer * 100;
        return new BigDecimal(percentage).setScale(precision, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentage(double percentage, int precision) {
        return new BigDecimal(percentage).setScale(precision, RoundingMode.HALF_UP);
    }

    public int getAttributeCount() {
        return attributeCount;
    }

    private List<Double> addMatrices(List<Double> A, List<Double> B) {
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < A.size(); i++) {
            double tmp = A.get(i) + B.get(i);
            result.add(tmp);
        }

        return result;
    }

    private Double calculateDotProduct(List<Double> A, List<Double> B) {
        double result = 0;

        for (int i = 0; i < A.size(); i++) {
            double a = A.get(i);
            double b = B.get(i);

            result += a * b;
        }

        return result;
    }

    private List<Double> multiplyMatrix(List<Double> A, double x) {
        List<Double> result = new ArrayList<>();

        for (Double a : A) {
            double tmp = a * x;
            result.add(tmp);
        }

        return result;
    }
}
