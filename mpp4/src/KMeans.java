import java.util.*;

public class KMeans {
    private final List<Point> centroids;
    private final List<List<Observation>> groups;
    private final int k;

    public KMeans(int k) {
        this.centroids = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.k = k;
    }

    private void initializeCentroids(List<Observation> points) {
        Set<Integer> chosenIndices = new HashSet<>();

        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            int index;
            do {
                index = rand.nextInt(points.size());
            } while (chosenIndices.contains(index));
            chosenIndices.add(index);
            centroids.add(new Point(points.get(index).getCoordinates()));
            groups.add(new ArrayList<>());
        }

//        for (int i=0; i<k; i++) {
//            centroids.add(new Point(points.get(i).getCoordinates()));
//            groups.add(new ArrayList<>());
//        }
    }

    private int getIndexOfClosestCentroid(Point point) {
        int minIndex = 0;
        double minDistance = calculateSquareOfEuclideanDistance(point, centroids.getFirst());

        for (int i = 1; i < k; i++) {
            double distance = calculateSquareOfEuclideanDistance(point, centroids.get(i));
            if (distance < minDistance) {
                minIndex = i;
                minDistance = distance;
            }
        }

        return minIndex;
    }

    private double calculateSquareOfEuclideanDistance(Point p1, Point p2) {
        double distance = 0;
        for (int i = 0; i < p1.getDimensionsCount(); i++) {
            distance += Math.pow(p1.getCoordinate(i) - p2.getCoordinate(i), 2);
        }
        return distance;
    }

    private int assignToNewGroups(List<Observation> observations) {
        int changesCounter = 0;
        for (Observation observation : observations) {
            int closestCentroidIndex = getIndexOfClosestCentroid(observation);

            if (closestCentroidIndex != observation.getGroupIndex()) {
                // Jeśli trzeba usuń ze starej grupy
                if (observation.getGroupIndex() != -1) {
                    groups.get(observation.getGroupIndex()).remove(observation);
                }

                // Przypisz do nowej grupy
                observation.setGroupIndex(closestCentroidIndex);
                groups.get(closestCentroidIndex).add(observation);
                changesCounter++;
            }
        }

        return changesCounter;
    }

    private void updateCentroids() {
        for (int i = 0; i < k; i++) {
            List<Observation> group = groups.get(i);

            if (group.isEmpty())
                continue;

            List<Double> newCentroidCoordinates = new ArrayList<>();
            int dimensionsCount = group.getFirst().getDimensionsCount();

            for (int j = 0; j < dimensionsCount; j++) {
                newCentroidCoordinates.add(0.);
            }

            for (Observation observation : group) {
                for (int j = 0; j < dimensionsCount; j++) {
                    newCentroidCoordinates.set(j, newCentroidCoordinates.get(j) + observation.getCoordinate(j));
                }
            }

            for (int j = 0; j < dimensionsCount; j++) {
                newCentroidCoordinates.set(j, newCentroidCoordinates.get(j) / group.size());
            }

            centroids.set(i, new Point(newCentroidCoordinates));
        }
    }

    public void groupData(List<Observation> observations) {
        // Losowa inicjalizacja grup punktami
        initializeCentroids(observations);

        int changesCounter;
        int rotation = 0;
        do {
            // Przypisanie punktów do nowych grup
            changesCounter = assignToNewGroups(observations);

            // Określenie centroid'ów grup
            updateCentroids();

            // Logowanie informacji
            printGroupDistanceFromCentroid(++rotation);
        } while (changesCounter != 0);

        printGroups();
    }

    private void printGroupDistanceFromCentroid(int rotation) {
        double sum = 0.0;

        System.out.println("Rotation " + rotation + ":");
        for (int i = 0; i < k; i++) {
            List<Observation> group = groups.get(i);
            Point centroid = centroids.get(i);

            double distance = 0.0;
            for (Observation observation : group) {
                distance += calculateSquareOfEuclideanDistance(observation, centroid);
            }

            sum += distance;
            System.out.println("\tGroup " + (i + 1) + "(" + group.size() + ") sum of squared distances from centroid: " + distance);
        }
        System.out.println("Sum: " + sum + "\n");
    }

    private void printGroups() {
        int counter = 0;
        for (List<Observation> group : groups) {
            System.out.println("Group " + (counter + 1) + ": ");
            System.out.println("Entropy: " + calculateEntropyForGroup(group));
            System.out.println("Centroid: " + centroids.get(counter));
            System.out.println(group);
            System.out.println();
            counter++;
        }
    }

    private double calculateEntropyForGroup(List<Observation> group) {
        Map<String, Integer> differentObservationsCounter = new HashMap<>();

        for (Observation observation : group) {
            String correctOutput = observation.getCorrectOutput();
            differentObservationsCounter.put(
                    correctOutput,
                    differentObservationsCounter.getOrDefault(correctOutput, 0) + 1
            );
        }

        double entropy = 0.0;
        for (Integer count : differentObservationsCounter.values()) {
            double probability = count / (double) group.size();
            entropy += (probability * (Math.log(probability) / Math.log(2)));
        }

        return entropy == 0.0 ? entropy : entropy * (-1);
    }
}
