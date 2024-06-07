import java.io.IOException;
import java.util.*;

public class Main {
    public static int cap;

    public static void main(String[] args) throws IOException {
        var res = DataManager.readDataFrom("mpp6/data/plecak.txt");
//        res.forEach((k, v) -> System.out.println(k + ": " + v));
        int setNum = (int) (Math.random() * res.size()) + 1;
        System.out.println("Using set num: " + setNum);
        List<Item> set = res.get(setNum);
        System.out.println(set + "\n");

        System.out.println("Calculating brute force...");
        long startTime = System.currentTimeMillis();
        int maxVal = Integer.MIN_VALUE;
        List<Item> optimalSet = null;
        for (long i = 0; i < Math.pow(2, set.size()); i++) {
            List<Item> subset = getSubsetOf(set, i);

            if (i % 1_000_000 == 0) {
                System.out.println(i + " checked!");
            }

            int sSize = 0;
            int sVal = 0;
            for (Item item : subset) {
                sSize += item.size();
                sVal += item.value();
            }

            if (sSize <= cap && sVal > maxVal) {
                maxVal = sVal;
                optimalSet = subset;
            }
        }
        System.out.println("Brute force results:");

        System.out.println("\tPlecak: " + optimalSet);
        System.out.println("\tRozmiar: " + optimalSet.stream().map(Item::size).reduce(Integer::sum).get());
        System.out.println("\tWartość: " + maxVal);
        System.out.println("\tUpłynęło: " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
        System.out.println("\tSprawdzone: " + Math.pow(2, set.size()) + "\n");

        long startHTime = System.currentTimeMillis();
        List<Item> optimalHSet = new ArrayList<>();
        int optimalHSetSize = 0;
        int optimalHSetVal = 0;

        PriorityQueue<Item> itemsPQ = new PriorityQueue<>(set);
        while (!itemsPQ.isEmpty()) {
            Item item = itemsPQ.poll();
            if (optimalHSetSize + item.size() <= cap) {
                optimalHSet.add(item);
                optimalHSetSize += item.size();
                optimalHSetVal += item.value();
            }
        }

        System.out.println("Heuristic results: ");
        System.out.println("\tPlecak: " + optimalHSet);
        System.out.println("\tRozmiar: " + optimalHSetSize);
        System.out.println("\tWartość: " + optimalHSetVal);
        System.out.println("\tUpłynęło: " + (double) (System.currentTimeMillis() - startHTime) + "ms");
    }

    private static List<Item> getSubsetOf(List<Item> baseSet, long num) {
//        boolean[] cArr = new boolean[size];
//
//        for (int i = 0; i < size; i++) {
//            cArr[size - 1 - i] = (num & (1 << i)) != 0;
//        }
//
//        return cArr;
        List<Item> subset = new ArrayList<>();
        for (int i = 0; i < baseSet.size(); i++) {
            if ((num & (1L << i)) != 0) {
                subset.add(baseSet.get(baseSet.size() - 1 - i));
            }
        }
        return subset;
    }
}