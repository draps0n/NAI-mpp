import java.util.List;

public class Observation extends Point {
    private int groupIndex;
    private final String correctOutput;

    public Observation(List<Double> attributeList, String correctOutput) {
        super(attributeList);
        this.correctOutput = correctOutput;
        this.groupIndex = -1;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getCorrectOutput() {
        return correctOutput;
    }

    @Override
    public String toString() {
        return correctOutput + super.getCoordinates();
    }
}
