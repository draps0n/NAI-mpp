import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Observation {
    private final Map<Character, Double> attributeMap;
    private final String correctOutput;

    public Observation(Map<Character, Double> attributeMap) {
        this.attributeMap = attributeMap;
        correctOutput = null;
    }

    public Observation(Map<Character, Double> attributeMap, String correctOutput) {
        this.attributeMap = attributeMap;
        this.correctOutput = correctOutput;
    }

    public String getCorrectOutput() {
        return correctOutput;
    }

    public List<Double> getInputs() {
        List<Double> inputs = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            inputs.add(getPercentageFor(c));
        }
        return inputs;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "correctDecision='" + correctOutput + '\'' +
                '}';
    }

    public Double getPercentageFor(char c) {
        return attributeMap.get(c);
    }
}