import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Digitizer {
    private final int binsCount;
    private List<Double[]> limits;

    public Digitizer(int k) {
        binsCount = k;
    }

    public int getBinsCount() {
        return binsCount;
    }

    public void digitize(List<Observation> data) {
        if (limits == null) throw new IllegalStateException("Limits are not set");

        for (Observation observation : data) {
            digitize(observation);
        }
    }

    public void digitize(Observation observation) {
        int dimCount = observation.getDimensionsCount();
        for (int dim = 0; dim < dimCount; dim++) {
            double value = observation.getCoordinate(dim);
            boolean digitized = false;
            for (int binNumber = 0; binNumber < binsCount - 1; binNumber++) {
                if (value >= limits.get(dim)[binNumber] && value < limits.get(dim)[binNumber + 1]) {
                    observation.setCoordinate(dim, binNumber);
                    digitized = true;
                    break;
                }
            }

            if (!digitized) {
                observation.setCoordinate(dim, binsCount - 1);
            }
        }
    }

    public void findIntervals(List<Observation> data) {
        limits = new ArrayList<>();

        int dimCount = data.getFirst().getDimensionsCount();
        for (int i = 0; i < dimCount; i++) {
            Double[] limit = new Double[binsCount];
            double[] minMax = findMinMax(data, i);
            double span = Math.abs(minMax[1] - minMax[0]);
            double interval = span / binsCount;

            limit[0] = minMax[0];
            for (int j = 1; j < binsCount; j++) {
                limit[j] = limit[j - 1] + interval;
            }
            limits.add(limit);
        }

//        for (Double[] limit : limits) {
//            System.out.println(Arrays.toString(limit));
//        }

    }

    private double[] findMinMax(List<Observation> data, int colIndex) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (Observation datum : data) {
            double value = datum.getCoordinate(colIndex);
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        return new double[]{min, max};
    }
}
