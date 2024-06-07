public class Node implements Comparable<Node> {
    private char letter;
    private final int weight;
    private Node left;
    private Node right;

    public Node(int weight) {
        this.weight = weight;
    }

    public Node(char letter, int weight) {
        this(weight);
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    public int getWeight() {
        return weight;
    }

    public void setChildren(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "weight=" + weight +
                ", letter=" + letter +
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return letter - o.letter;
    }
}