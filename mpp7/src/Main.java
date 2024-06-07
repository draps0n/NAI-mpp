import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> {
                    JFrame frame = new JFrame("Huffman Coding");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(800, 600);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);

                    JTextArea textArea = new JTextArea();
                    frame.getContentPane().add(textArea, BorderLayout.CENTER);

                    JButton button = new JButton("Encode");
                    button.addActionListener(
                            (e) -> {
                                String text = textArea.getText();
                                if (text == null || text.isEmpty()) return;

                                text = text.trim();
//                                System.out.println("\n " + text);
                                Node[] arr = translateText(text);
//                                System.out.println(Arrays.toString(arr));
                                HuffmanTree huffmanTree = new HuffmanTree(arr);
//                                huffmanTree.printCodes();
                                Map<Node, String> codes = huffmanTree.getCodes();
//                                System.out.println(codes);

                                int bitsCount = 0;
                                for (var pair : codes.entrySet()) {
                                    System.out.println(
                                            pair.getKey().getLetter() + " (" +
                                                    (int) (pair.getKey().getLetter()) + "):" +
                                                    pair.getValue()
                                    );
                                    bitsCount += pair.getKey().getWeight() * pair.getValue().length();
                                }

                                System.out.println("Text length in bits: " + bitsCount);
                            }
                    );
                    frame.getContentPane().add(button, BorderLayout.SOUTH);
                }
        );

        // Tworzymy drzewo Huffmana
//        HuffmanTree huffmanTree = new HuffmanTree(FileUtil.getFileData(args[0]));

        // Wypisujemy kody jego liści
//        huffmanTree.printCodes();
    }

    private static Node[] translateText(String text) {
        Map<Character, Integer> freqeuncyMap = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            freqeuncyMap.put(letter, freqeuncyMap.getOrDefault(letter, 0) + 1);
        }

//        System.out.println(frequencyMap);
        Node[] arr = new Node[freqeuncyMap.size()];
        int counter = 0;
        for (Map.Entry<Character, Integer> e : freqeuncyMap.entrySet()) {
            arr[counter] = new Node(e.getKey(), e.getValue());
            counter++;
        }

        return arr;
    }
}
