
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
        //Case with best accuracy
        double bestAccuracy = 0.0;
        //Loop 10 times as per assignment requirements
        for(int i = 0; i < 10; i++){
            System.out.println("Starting dataset division for dataset Split number :"+i);
            //Divide data into thirds, one third is testing, two thirds is training.
            DataPreProcess.datasetDivision();
            //Algorithm instance creation
            Algorithm c45 = new Algorithm();
            //Convert to columns
            System.out.println("Converting rows into columns");
            ArrayList<Column> columns = new ArrayList<>();
            for(int colID =0; colID < dataSet.get(0).attributes.size(); colID++){
                Column col = new Column(i, DataPreProcess.rowIntoAttributeCol(dataSet, colID));
                columns.add(col);
            }
            System.out.println("Starting tree building algorithm");
            //Get root node from algorithm
            Node root = c45.startTreeBuilding(3, columns);
            //Create arraylist for confusionMatrix data
            ArrayList<ArrayList> confusionMatrixData = new ArrayList<>();
            //Test with testing data
            System.out.println("Testing tree model with training dataset");
            for(int cf = 0; cf < testingDataset.size(); cf++){
                confusionMatrixData.add(c45.traverseTreeForTesting(root, testingDataset.get(cf)));
            }
            System.out.println("Presenting Confusion Matrix of dataset: "+i+"\n");
            int[][] errorRateData = c45.confusionMatrix(confusionMatrixData);
            double errorRate = c45.errorRate(errorRateData);
            System.out.println("\n"+"Training Dataset "+i+" has an error rate of "+errorRate+"%"+"\n");
            if(bestAccuracy < ((double)100-errorRate)){
                bestAccuracy = ((double)100-errorRate);
            }
        }
        System.out.println("\n"+"Model with best accuracy was with: "+bestAccuracy+"%");











        System.out.println("--------------------------------------------------------------");

        // Print tree
            //System.out.println("                         " + root.getName());
            //printNode(root, 1);




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

