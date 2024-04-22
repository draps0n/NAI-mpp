import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            List<Observation> observations = DataManager.readFile("mpp4/data/iris_training.txt", ',');
            Scanner sc = new Scanner(System.in);
            int k = 0;
            while (k <= 0 || k > observations.size()) {
                System.out.println("Enter k: ");
                k = sc.nextInt();
            }
            KMeans kMeans = new KMeans(k);
            kMeans.groupData(observations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}