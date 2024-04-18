import java.util.Collections;
import java.util.List;

public class Observation {
    private final List<Double> attributeList;
    private int correctOutput;

    public Observation(List<Double> attributeList) {
        this.attributeList = Collections.unmodifiableList(attributeList);
        correctOutput = -1;
    }

    public List<Double> getAttributeList() {
        return attributeList;
    }

    public int getAttributeCount() {
        return attributeList.size();
    }

    public int getCorrectOutput() {
        return correctOutput;
    }

    public void setCorrectOutput(int correctDecision) {
        this.correctOutput = correctDecision;
    }

    public boolean hasCorrectDecision() {
        return correctOutput >= 0;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "correctDecision='" + correctOutput + '\'' +
                '}';
    }
}
