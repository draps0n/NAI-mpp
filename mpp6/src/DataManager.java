import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DataManager {
    public static Map<Integer, List<Item>> readDataFrom(String filePath) throws IOException {
        Map<Integer, List<Item>> data = new HashMap<>();
        List<String> lines = Files.readAllLines(Path.of(filePath));
        int counter = 1;
        String[] parts = lines.getFirst().split(" ");
        Main.cap = Integer.parseInt(parts[parts.length - 1].trim());

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.matches("dataset \\d+:")) {
                List<Integer> sizes = Arrays.stream(
                                lines.get(i + 1).split("\\{")[1]
                                        .split("}")[0]
                                        .split(",")
                        ).map(s -> Integer.parseInt(s.trim()))
                        .toList();
                List<Integer> values = Arrays.stream(
                                lines.get(i + 2).split("\\{")[1]
                                        .split("}")[0]
                                        .split(",")
                        ).map(s -> Integer.parseInt(s.trim()))
                        .toList();

                List<Item> items = new ArrayList<>();
                for (int j = 0; j < values.size(); j++) {
                    items.add(new Item(j, sizes.get(j), values.get(j)));
                }
                data.put(counter++, items);
                i += 2;
            }
        }

        return data;
    }
}
