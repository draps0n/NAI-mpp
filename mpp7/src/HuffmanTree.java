import java.util.*;

public class HuffmanTree {
    private Node root;

    public HuffmanTree(Node[] fileData) {
        buildTree(fileData);
    }

    // Tworzymy kolejkę priorytetową, a następnie
    // łączymy jej dwa pierwsze elementy w jeden,
    // po czym wstawiamy go do niej.
    // Powtarzamy, dopóki w kolejce nie pozostanie jeden element
    private void buildTree(Node[] fileData) {
        PriorityQueue priorityQueue = new PriorityQueue(fileData);
        Node leftPart;
        Node rightPart;
        Node concat;

        while (priorityQueue.size() > 1) {
            leftPart = priorityQueue.getMin();
            priorityQueue.delMin();

            rightPart = priorityQueue.getMin();
            priorityQueue.delMin();

            concat = new Node(leftPart.getWeight() + rightPart.getWeight());
            concat.setChildren(leftPart, rightPart);

            priorityQueue.insert(concat);
        }

        // Pozostały element jest korzeniem drzewa Huffmana
        root = priorityQueue.getMin();
    }

    public void printCodes() {
        if (root == null) {
            System.out.println();
            return;
        }

        if (root.isLeaf()) {
            System.out.println(root.getLetter() + " 0");
            return;
        }

        printCodes(root, "");
    }

    private void printCodes(Node node, String code) {
        if (node == null) {
            return;
        }

        // Jeśli jest liściem to wypisujemy literę i jej kod
        if (node.isLeaf()) {
            System.out.println(node.getLetter() + " " + code);
            return;
        }

        // Schodzimy dalej w lewo i w prawo
        printCodes(node.getLeft(), code + "0");
        printCodes(node.getRight(), code + "1");
    }

    public Map<Node, String> getCodes() {
        Map<Node, String> codes = new TreeMap<>();
        getCodes(root, "", codes);
        return codes;
    }

    private void getCodes(Node node, String code, Map<Node, String> codes) {
        if (node == null) {
            return;
        }

        // Jeśli jest liściem to wypisujemy literę i jej kod
        if (node.isLeaf()) {
            codes.put(node, code);
//            codes.add(new Pair(node, code));
            return;
        }

        // Schodzimy dalej w lewo i w prawo
        getCodes(node.getLeft(), code + "0", codes);
        getCodes(node.getRight(), code + "1", codes);
    }
}

record Pair(Node node, String code) implements Comparable<Pair> {
    @Override
    public int compareTo(Pair o) {
        return node.getLetter() - o.node.getLetter();
    }
}
