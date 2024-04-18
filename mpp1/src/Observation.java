import java.util.ArrayList;
import java.util.List;

public class Observation {
    private final List<Double> attributeList;
    private String correctDecision;

    public Observation() {
        attributeList = new ArrayList<>();
    }

    public Observation(List<Double> attributeList) {
        this.attributeList = attributeList;
    }

    public void addAttribute(double number) {
        attributeList.add(number);
    }

    public double getAttribute(int index) {
        return attributeList.get(index);
    }

    public List<Double> getAttributeList() {
        return attributeList;
    }

    public int getAttributeCount() {
        return attributeList.size();
    }

    public String getCorrectDecision() {
        return correctDecision;
    }

    public void setCorrectDecision(String correctDecision) {
        this.correctDecision = correctDecision;
    }

    public boolean hasCorrectDecision() {
        return correctDecision != null;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "correctDecision='" + correctDecision + '\'' +
                '}';
    }
}
