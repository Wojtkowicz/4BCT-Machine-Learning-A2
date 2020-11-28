
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
        //System.out.println(dataSet);

        //Divide data into thirds, one third is testing, two thirds is training.
        DataPreProcess.datasetDivision();
        //System.out.println(testingDataset.size());
        //System.out.println(trainingDataset.size());
        //System.out.println(dataSet.size());

        // Test data to find threshold
        ArrayList<Attribute> resultsData = new ArrayList<>(Arrays.asList(
                new Attribute("Style","ale"),
                new Attribute("Style","ale"),
                new Attribute("Style","stout"),
                new Attribute("Style","ale"),
                new Attribute("Style","lager"),
                new Attribute("Style","stout")));

        Column column = new Column(1, resultsData);
        //System.out.println("Col: "+column);
        ArrayList<Double> data = new ArrayList<>(Arrays.asList(1d, 5d, 2d, 5d, 8d, 4d));
        //System.out.println(data);
        //System.out.println("threshold: " + MathUtils.calculateOptimalThreshold(data, column));

        //Algorithm
        Algorithm c45 = new Algorithm();
        //Convert to columns
        ArrayList<Column> columns = new ArrayList<>();

        for(int i =0; i < dataSet.get(0).attributes.size(); i++){
            Column col = new Column(i, DataPreProcess.rowIntoAttributeCol(dataSet, i));
            columns.add(col);
        }

        Node root = c45.startTreeBuilding(3, columns);
        System.out.println(c45.traverseTreeForTesting(root, trainingDataset.get(0)));
        System.out.println("--------------------------------------------------------------");

        // Print tree
            System.out.println("                         " + root.getName());
            printNode(root, 1);




        //System.out.println(c45.addBasedOnPartition(columns, 9, "colour", "Left"));
        //System.out.println(c45.addBasedOnPartition(columns, 9, "colour", "Right"));
    }

    private static void printNode(final Node node, int depth){
        if(node.getLeftChild() != null) {
            System.out.println("                          /   \\  \n                         /     \\");
            System.out.println("                    " + node.getLeftChild().getName() + "   " + node.getRightChild().getName());
        }

        if(node.getLeftChild() != null)
        printNode(node.getLeftChild(), depth+1);
    }

}

