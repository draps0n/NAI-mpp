import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Point {
    private final List<Double> coordinates;

    public Point(List<Double> coordinates) {
        this.coordinates = new ArrayList<>();
        for (Double coordinate : coordinates) {
            this.coordinates.add(Double.valueOf(coordinate.toString()));
        }
    }

    public double getCoordinate(int i) {
        return coordinates.get(i);
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public int getDimensionsCount() {
        return coordinates.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return Objects.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coordinates);
    }

    @Override
    public String toString() {
        return "Point" + coordinates;
    }
}
