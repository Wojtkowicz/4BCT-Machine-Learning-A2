
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    // global variable to store data headings
    public static ArrayList<String> headings = new ArrayList<>(Arrays.asList("calorific_value", "nitrogen", "turbidity", "style", "alcohol", "sugars", "bitterness", "beer_id", "colour", "degree_of_fermentation"));
    // global variable to store the imported Data set
    public static ArrayList<Row> dataSet = new ArrayList<>();
    //global variable to store dataset to be used for training
    public static ArrayList<Row> trainingDataset = new ArrayList<>();
    //global variable to store dataset to be used for testing
    public static ArrayList<Row> testingDataset = new ArrayList<>();
    // Algorithm Results
    public static String testingResults = "";
    // Output String
    public static String output = "";

    //Author = Jakub Wojtkowicz
    public static void main(String[] args) {
        // Creating GUI fram
        JFrame frame = new JFrame("C4.5 Machine Learning Algorithm Implementation");
        frame.setSize(600,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        // Creating a text box for results
        JTextField textBox = new JTextField();
        textBox.setSize(400,200);
        textBox.setLocation(100,300);
        frame.add(textBox);
        // Adding a button to run the algorithm
        JButton button = new JButton("Run Algorithm");
        int buttonWidth = 200;
        button.setBounds((frame.getBounds().width/2 - buttonWidth/2) , 30,  200, 30);
        frame.add(button);
        // Making Radio Buttons to select a file
        JRadioButton r1 = new JRadioButton("beer.txt");
        r1.setBounds(50,100, 150,20);
        frame.add(r1);
        JRadioButton r2 = new JRadioButton("beer_training.txt");
        r2.setBounds(50,150, 150,20);
        frame.add(r2);
        JRadioButton r3 = new JRadioButton("beer_test.txt");
        r3.setBounds(50,200, 150,20);
        frame.add(r3);

        ButtonGroup trainingFileRadioButtons = new ButtonGroup();
        trainingFileRadioButtons.add(r1);
        trainingFileRadioButtons.add(r2);
        trainingFileRadioButtons.add(r3);

        // Making all frame components visible
        frame.setVisible(true);
        // Adding listener for the button that runs the algorithm
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                trainModelAndTest();
                textBox.setText("Classification success rate: " + testingResults);
            }
        });

    }

    //Author = Jaroslav Kucera
    public static void trainModelAndTest(){
        // Create mock file
        File testFile = new File("src/beer.txt");

        // Read in the user input file
        DataPreProcess.ReadInData(testFile);

        // Remove ID column
        DataPreProcess.removeColumnFromDataSet("beer_id");
        //Case with best accuracy
        double bestAccuracy = 0.0;
        output = new String();
        //Loop 10 times as per assignment requirements
        for (int i = 0; i < 10; i++) {
            output += "Starting dataset division for dataset Split number :" + i+"\n";
            System.out.println("Starting dataset division for dataset Split number :" + i);
            //Divide data into thirds, one third is testing, two thirds is training.
            DataPreProcess.datasetDivision();
            //Algorithm instance creation
            Algorithm c45 = new Algorithm();
            //Convert to columns
            output += "Converting rows into columns\n";
            System.out.println("Converting rows into columns");
            ArrayList<Column> columns = new ArrayList<>();
            for (int colID = 0; colID < dataSet.get(0).attributes.size(); colID++) {
                Column col = new Column(i, DataPreProcess.rowIntoAttributeCol(dataSet, colID));
                columns.add(col);
            }
            output += "Starting tree building algorithm\n";
            System.out.println("Starting tree building algorithm");
            //Get root node from algorithm
            Node root = c45.startTreeBuilding(3, columns);
            //Create arraylist for confusionMatrix data
            ArrayList<ArrayList> confusionMatrixData = new ArrayList<>();
            //Test with testing data
            output += "Testing tree model with training dataset\n";
            System.out.println("Testing tree model with training dataset");
            for (int cf = 0; cf < testingDataset.size(); cf++) {
                confusionMatrixData.add(c45.traverseTreeForTesting(root, testingDataset.get(cf)));
            }
            output += "Presenting Confusion Matrix of dataset: " + i + "\n\n";
            System.out.println("Presenting Confusion Matrix of dataset: " + i + "\n");
            int[][] errorRateData = c45.confusionMatrix(confusionMatrixData);
            double errorRate = c45.errorRate(errorRateData);
            output += "\n" + "Training Dataset " + i + " has an error rate of " + (errorRate*100) + "%" + "\n\n";
            System.out.println("\n" + "Training Dataset " + i + " has an error rate of " + (errorRate*100) + "%" + "\n");
            if (bestAccuracy < ((double) 100 - (errorRate*100))) {
                bestAccuracy = ((double) 100 - (errorRate*100));
            }
            //Print tree
            output += "Created model visualisation:\n";
            System.out.println("Created model visualisation:");
            printTree(root);
        }
        output+="\n" + "Model with best accuracy was with: " + bestAccuracy + "%\n";
        System.out.println("\n" + "Model with best accuracy was with: " + bestAccuracy + "%");
        testingResults = String.valueOf(bestAccuracy);
        try(PrintWriter outputFile = new PrintWriter("Results.txt")){
            outputFile.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        resetAlgorithm();
    }

    //Author = Jakub Wojtkowicz
    // Resets global variables for the algorithm
    private static void resetAlgorithm(){
        dataSet = new ArrayList<>();
        trainingDataset = new ArrayList<>();
        testingDataset = new ArrayList<>();
        headings = new ArrayList<>(Arrays.asList("calorific_value", "nitrogen", "turbidity", "style", "alcohol", "sugars", "bitterness", "beer_id", "colour", "degree_of_fermentation"));
    }

    //Author = Jakub Wojtkowicz
    //Prints a tree visual given its root node
    private static void printTree(final Node root) {
        List<String> treeNodes = new ArrayList<>();
        traverseTree(root, treeNodes);
        List<List<String>> lineLists = new ArrayList<>();
        ArrayList<String> tempPosition = new ArrayList<>();
        // for each saved node, add to temporary list or save temporary list if new line is reached
        // indicated by an X. This re-saves the nodes as individual lists for each line.
        for (String treeNode : treeNodes) {
            if (treeNode.equals("X")) {
                lineLists.add(tempPosition);
                tempPosition = new ArrayList<>();
            } else {
                tempPosition.add(treeNode);
            }
        }

        // For all lines
        for (int j = 0; j < lineLists.size(); j++) {
            int currentCount = 0;
            int parentIndex = -1;
            // Set dynamic string for indent
            String lineToPrint = " ";
            boolean childWritten = false;
            // For all values in a line
            for (int k = 0; k < (int) Math.pow(2, j); k++) {
                childWritten = false;
                // Until the current value is saved keep checking if the current spot is blank
                while (!childWritten) {
                    // Change parent every 2 values
                    if (currentCount % 2 == 0) {
                        parentIndex++;
                        lineToPrint = lineToPrint + " ".repeat(5);
                    }
                    // Get the current child and parent
                    String child = "";
                    String parent = "";
                    if (j > 0) {
                        // try to set the parent, if no parent exists due to an upstream leaf, set the child to
                        // an empty string placeholder
                        try {
                            parent = lineLists.get(j - 1).get(parentIndex);
                        } catch (IndexOutOfBoundsException ex) {
                            child = "  ";
                        }
                    }
                    // If the current position iterator is less than the total size of level nodes,
                    // set the child
                    if (k < lineLists.get(j).size()) {
                        child = lineLists.get(j).get(k);
                    }
                    // Add 1 to the current count which maintains current line position
                    currentCount++;
                    // If current position has a leaf as a parent do not print the child else print child
                    if (parent.equals("ale") || parent.equals("stout") || parent.equals("lager")) {
                        lineToPrint = lineToPrint + "  ";
                    } else {
                        lineToPrint = lineToPrint + child + "  ";
                        childWritten = true;
                    }
                }
            }
            int lineLength = lineToPrint.length();
            lineToPrint = " ".repeat(85 - (lineLength / 2) + 40) + lineToPrint;
            output+=lineToPrint+"\n\n\n";
            System.out.println(lineToPrint);
            System.out.println("\n\n");
        }
    }
    //Author = Jakub Wojtkowicz
    // Traverses a tree via level order traversal given the root node and an empty
    // list that is written to when a new node is traversed. It writes an X to the list
    // as a placeholder to signify a new level of a tree when it is reached
    private static void traverseTree(final Node root, List<String> treeNodes) {
        int treeDepth = getTreeDepth(root);
        for (int i = 1; i <= treeDepth; i++) {
            recordTreeLevelNodes(root, i, treeNodes);
            treeNodes.add("X");
        }
    }
    //Author = Jakub Wojtkowicz
    // Recursive function that traverses the tree and writes to the tree nodes list all new nodes traversed
    private static void recordTreeLevelNodes(final Node node, int depth, List<String> treeNodes) {
        if (node == null)
            return;
        if (depth == 1) {
            treeNodes.add(node.getName());
        } else if (depth > 1) {
            recordTreeLevelNodes(node.getLeftChild(), depth - 1, treeNodes);
            recordTreeLevelNodes(node.getRightChild(), depth - 1, treeNodes);
        }
    }
    //Author = Jakub Wojtkowicz
    // calculates the max depth of a tree recursively given the tree root
    private static int getTreeDepth(final Node root) {
        if (root == null)
            return 0;
        else {
            int depthLeft = getTreeDepth(root.getLeftChild());
            int depthRight = getTreeDepth(root.getRightChild());

            if (depthLeft > depthRight)
                return (depthLeft + 1);
            else
                return (depthRight + 1);
        }
    }
}

