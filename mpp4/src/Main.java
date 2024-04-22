import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter k: ");
        int k = sc.nextInt();
        KMeans kMeans = new KMeans(k);
        try {
            List<Observation> observations = DataManager.readFile("mpp4/data/iris_training.txt", ',');
            kMeans.groupData(observations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}