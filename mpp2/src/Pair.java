public class Pair<K, V> {
    private K first;
    private V last;

    public Pair(K first, V last) {
        this.first = first;
        this.last = last;
    }

    public K getFirst() {
        return first;
    }

    public V getLast() {
        return last;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public void setLast(V last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", last=" + last +
                '}';
    }
}
