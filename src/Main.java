
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    // global variable to store data headings
    public static final ArrayList<String> headings = new ArrayList<>(Arrays.asList("calorific_value", "nitrogen", "turbidity", "style", "alcohol", "sugars", "bitterness", "beer_id", "colour", "degree_of_fermentation"));
    // global variable to store the imported Data set
    public static ArrayList<Row> DataSet = new ArrayList<>();

    public static void main(String[] args){
        // Create mock file
        File testFile = new File("src/testFile.txt");

        // Read in the user input file
        DataPreProcess.ReadInData(testFile);

        // Print out data
        System.out.println(DataSet);
    }

}

