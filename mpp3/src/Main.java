import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Observation> trainingSet = DataManager.readAllObservationsFromDirectory("training-texts", ReadType.TRAINING);
        List<Observation> testSet = DataManager.readAllObservationsFromDirectory("test-texts", ReadType.TEST);

        PerceptronLayer perceptronLayer = new PerceptronLayer(DataManager.knownLanguages);

        perceptronLayer.trainLayer(trainingSet);
        perceptronLayer.testLayer(testSet);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setVisible(true);
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setTitle("Klasyfikator języka");

            JTextArea textField = new JTextArea();
            textField.setLineWrap(true);
            textField.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textField);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            JButton button = new JButton("Klasyfikuj");
            JPanel panel = new JPanel(new BorderLayout());

            panel.add(button, BorderLayout.PAGE_END);
            panel.add(scrollPane, BorderLayout.CENTER);
            frame.getContentPane().add(panel);

            button.addActionListener(e -> {
                String text = textField.getText();
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Wystąpił błąd! Nie wprowadzono żadnego tekstu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (text.length() < 100) {
                    JOptionPane.showMessageDialog(null, "Wystąpił błąd! Wprowadzony tekst jest zbyt krótki (min. 100 znaków).", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Map<Character, Double> map = DataManager.getCharacterMapFor(text);
                Observation observation = new Observation(map);
                String result = perceptronLayer.classify(observation);
                JOptionPane.showMessageDialog(null, "Ten tekst jest napisany po " + result, "Zaklasyfikowano!", JOptionPane.INFORMATION_MESSAGE);
            });
        });
    }
}