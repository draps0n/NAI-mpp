import java.util.List;

public class Observation extends Point {
    private final String correctOutput;

    public Observation(List<Double> attributeList, String correctOutput) {
        super(attributeList);
        this.correctOutput = correctOutput;
    }

    public String getCorrectOutput() {
        return correctOutput;
    }

    @Override
    public String toString() {
        return correctOutput + super.getCoordinates();
    }
}
