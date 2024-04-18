import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Analyzer {

    private final List<Observation> trainingObservations;
    private final List<Pair<Double, Double>> minMaxList;
    private final int attributeCount;
    private final boolean logging;
    private int k;
    private int correct;
    private int checkedWithAnswer;

    public Analyzer(List<Observation> trainingObservations, int k, boolean logging) {
        this.trainingObservations = trainingObservations;
        attributeCount = trainingObservations.getFirst().getAttributeCount();
        minMaxList = new ArrayList<>();
        this.k = k;
        this.logging = logging;
        initMinMaxArr();
    }

    public void analyze(Observation observation) {
        PriorityQueue<Pair<Observation, Double>> priorityQueue = calculateEuclideanDistance(observation);

        List<Observation> observationList = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            Pair<Observation, Double> pair = priorityQueue.poll();
            observationList.add(pair.getFirst());
        }

        String result = getMostCommonType(observationList);
        if (logging) {
            System.out.print(observation.getAttributeList() + " ==> " + result);
        }

        if (observation.hasCorrectDecision()) {
            checkedWithAnswer++;
            boolean isCorrect = result.equals(observation.getCorrectDecision());

            if (isCorrect) {
                correct++;
                if (logging) {
                    System.out.println("\u001B[32m" + " (expected: " + observation.getCorrectDecision() + ")\u001B[0m");
                }
            } else if (logging) {
                System.out.println("\u001B[31m" + " (expected: " + observation.getCorrectDecision() + ")\u001B[0m");
            }
        } else if (logging){
            System.out.println();
        }
    }

    public void setK(int k) {
        this.k = k;
        correct = 0;
        checkedWithAnswer = 0;
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

    private String getMostCommonType(List<Observation> observationList) {
        Map<String, Integer> freqMap = new HashMap<>(); // W przypadku HashMap mniejsza celność

        for (Observation observation : observationList) {
            freqMap.put(observation.getCorrectDecision(), freqMap.getOrDefault(observation.getCorrectDecision(), 0) + 1);
        }

        String mostFrequent = "";
        int maxFreq = 0;

        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() > maxFreq) { // przy zmianie tutaj większa efektywność
                maxFreq = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        // Debugowanie
//        for (int i=0; i<observationList.size(); i++) {
//            System.out.println((i + 1) + ". " + observationList.get(i).getCorrectDecision());
//        }
        System.out.println();
        for (Map.Entry<String, Integer> e : freqMap.entrySet()) {
            System.out.println(e);
        }

        return mostFrequent;
    }

    private PriorityQueue<Pair<Observation, Double>> calculateEuclideanDistance(Observation observation) {
        PriorityQueue<Pair<Observation, Double>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair::getLast));

        for (Observation trainingObservation : trainingObservations) {
            double sum = 0;

            for (int i = 0; i < attributeCount; i++) {
                double attrOne = observation.getAttribute(i);
                double attrTwo = trainingObservation.getAttribute(i);

                // - 2 * min
//                sum += Math.pow((attrOne - attrTwo - 2 * minMaxList.get(i).getFirst()) / (minMaxList.get(i).getLast() - minMaxList.get(i).getFirst()), 2);

                // z zajęć
                sum += Math.pow((attrOne - attrTwo) / (minMaxList.get(i).getLast() - minMaxList.get(i).getFirst()), 2);

                // bez
//                sum += Math.pow((attrOne - attrTwo), 2);
            }

            priorityQueue.add(new Pair<>(trainingObservation, Math.sqrt(sum)));
        }

//        System.out.println(priorityQueue);

        return priorityQueue;
    }

    private void initMinMaxArr() {
        // Ustawienie początkowych wartości na wartości z 1. kwiatu
        Observation tmpObservation = trainingObservations.getFirst();

        for (int i = 0; i < attributeCount; i++) {
            minMaxList.add(i, new Pair<>(tmpObservation.getAttribute(i), tmpObservation.getAttribute(i)));
        }

        // Wybranie rzeczywistych min i max spośród kwiatów
        for (int i = 0; i < attributeCount; i++) {
            for (Observation observation : trainingObservations) {
                double attr = observation.getAttribute(i);

                if (attr > minMaxList.get(i).getLast()) {
                    minMaxList.get(i).setLast(attr);
                } else if (attr < minMaxList.get(i).getFirst()) {
                    minMaxList.get(i).setFirst(attr);
                }
            }
        }
    }

    public int getAttributeCount() {
        return attributeCount;
    }
}
