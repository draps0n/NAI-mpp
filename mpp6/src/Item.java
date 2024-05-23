public record Item(int id, int size, int value) implements Comparable<Item> {
    @Override
    public int compareTo(Item o) {
        double res = ((double) size / value) - ((double) o.size / o.value);
        return Double.compare(res, 0.);
    }
}
