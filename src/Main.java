
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    // global variable to store data headings
    public static final ArrayList<String> headings = new ArrayList<>(Arrays.asList("calorific_value", "nitrogen", "turbidity", "style", "alcohol", "sugars", "bitterness", "beer_id", "colour", "degree_of_fermentation"));
    // global variable to store the imported Data set
    public static ArrayList<Row> dataSet = new ArrayList<>();
    //global variable to store dataset to be used for training
    public static ArrayList<Row> trainingDataset = new ArrayList<>();
    //global variable to store dataset to be used for testing
    public static ArrayList<Row> testingDataset = new ArrayList<>();

    public static void main(String[] args){
        // Create mock file
        File testFile = new File("src/testFile.txt");

        // Read in the user input file
        DataPreProcess.ReadInData(testFile);

        // Remove ID column
        DataPreProcess.removeColumnFromDataSet("beer_id");

        // Print out data
        System.out.println(dataSet);

        //Divide data into thirds, one third is testing, two thirds is training.
        DataPreProcess.datasetDivision();
        System.out.println(testingDataset.size());
        System.out.println(trainingDataset.size());
        System.out.println(dataSet.size());

        // Test data to find threshold
        ArrayList<String> resultsData = new ArrayList<>(Arrays.asList("Ale", "Ale", "Stout", "Ale", "Lager", "Stout"));
        ArrayList<Double> data = new ArrayList<>(Arrays.asList(1d, 5d, 2d, 5d, 8d, 4d));
        System.out.println("threshold: " + MathUtils.calculateOptimalThreshold(data, resultsData));

        //Algorithm
        Algorithm c45 = new Algorithm();
        //Convert to columns
        ArrayList<Column> columns = new ArrayList<>();
        for(int i =0; i < trainingDataset.get(0).attributes.size(); i++){
            Column col = new Column(i, DataPreProcess.rowIntoAttributeCol(trainingDataset, i));
            columns.add(col);
        }
        c45.startTreeBuilding(3, columns);
    }

}

