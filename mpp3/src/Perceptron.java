import java.util.*;

public class Perceptron {
    private List<Double> weights;
    private final double eta;

    public Perceptron() {
        this.weights = new ArrayList<>();

        for (int i=0; i<26; i++) {
            weights.add(1.);
        }
//        weights.add(0.); // TODO : czy musi mieć próg?

        eta = 150;
    }

    public void adjustWeights(List<Double> inputs, double decision, double correctDecision) {
        List<Double> productList = multiplyMatrix(inputs, 0.5 * eta * (correctDecision - decision) * (1 - decision * decision)); // bipolarna sigmoidalna
        weights = addMatrices(weights, productList);
    }

    public double getOutputFor(List<Double> inputs) {
        double net = calculateDotProduct(weights, inputs);
        return bipolarSigmoidFunction(net);
    }

    private static double bipolarSigmoidFunction(double net) {
        return (2. / (1 + Math.exp(-net))) - 1.;
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
