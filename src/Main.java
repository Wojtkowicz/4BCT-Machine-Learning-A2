
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    // Algorithm matrix for printing
    public static  int[][] matrixResult = null;
    // Output String
    public static String output = "";
    // GUI frame
    public static JFrame frame;

    //Author = Jakub Wojtkowicz
    public static void main(String[] args) {
        // Creating GUI frame
        frame = new JFrame("C4.5 Machine Learning Algorithm Implementation");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        // Creating a text box for result info
        JTextArea resultInfo = new JTextArea("The algorithm runs 10 times and below is the best result");
        resultInfo.setSize(300, 20);
        resultInfo.setLocation(150, 300);
        frame.add(resultInfo);

        // Creating a text box for saved results information
        JTextArea savedInfo = new JTextArea("*A file is created in the program files containing full training and testing data*");
        savedInfo.setSize(410, 20);
        savedInfo.setLocation(105, 500);
        frame.add(savedInfo);

        // Creating a text box for classification results
        JTextArea classificationDisplay = new JTextArea();
        classificationDisplay.setSize(270, 20);
        classificationDisplay.setLocation(170, 350);
        frame.add(classificationDisplay);

        // Creating a text box for matrix results
        JTextArea matrixDisplay = new JTextArea();
        matrixDisplay.setSize(110, 80);
        matrixDisplay.setLocation(250, 400);
        frame.add(matrixDisplay);

        // Creating a text box for input of training file source
        JTextField textBoxTrainingFileInput = new JTextField();
        textBoxTrainingFileInput.setSize(150, 25);
        textBoxTrainingFileInput.setLocation(110, 260);
        textBoxTrainingFileInput.setText("C:\\Users\\jakub\\machine_learning_pair_programming\\src\\beer.txt");
        frame.add(textBoxTrainingFileInput);

        // Creating a text box for input of testing file source
        JTextField textBoxTestFileInput = new JTextField();
        textBoxTestFileInput.setSize(150, 25);
        textBoxTestFileInput.setLocation(390, 260);
        textBoxTestFileInput.setText("C:\\Users\\jakub\\machine_learning_pair_programming\\src\\beer.txt");
        frame.add(textBoxTestFileInput);

        // Text for single file training and testing
        JTextArea singleFileInfo = new JTextArea(
                " If only one file is selected the\n" +
                        " program will randomly split your\n " +
                        "file into thirds and use two of\n" +
                        " them for training and one for tests");
        singleFileInfo.setBounds(10, 10, 180, 70);
        singleFileInfo.setEditable(false);
        frame.add(singleFileInfo);

        // Adding a button to run the algorithm
        JButton button = new JButton("Run Algorithm");
        int buttonWidth = 200;
        button.setBounds((frame.getBounds().width / 2 - buttonWidth / 2), 10, 200, 30);
        frame.add(button);

        // Label for training radio buttons
        Label trainingSetLabel = new Label("Training/Testing Set");
        trainingSetLabel.setBounds(20, 90, 200, 20);
        frame.add(trainingSetLabel);

        // Making Radio Buttons to select a training file
        JRadioButton r1 = new JRadioButton("beer.txt");
        r1.setBounds(20, 110, 150, 20);
        frame.add(r1);
        JRadioButton r2 = new JRadioButton("beer_training.txt");
        r2.setBounds(20, 160, 150, 20);
        frame.add(r2);
        JRadioButton r3 = new JRadioButton("beer_test.txt");
        r3.setBounds(20, 210, 150, 20);
        frame.add(r3);
        JRadioButton r4 = new JRadioButton("Import File");
        r4.setBounds(20, 260, 90, 20);
        frame.add(r4);
        ButtonGroup trainingFileRadioButtons = new ButtonGroup();
        trainingFileRadioButtons.add(r1);
        trainingFileRadioButtons.add(r2);
        trainingFileRadioButtons.add(r3);
        trainingFileRadioButtons.add(r4);
        r1.setSelected(true);

        // Label for training radio buttons
        Label testingSetLabel = new Label("Testing Set");
        testingSetLabel.setBounds(300, 90, 200, 20);
        frame.add(testingSetLabel);

        // Making Radio Buttons to select a testing file
        JRadioButton r5 = new JRadioButton("beer.txt");
        r5.setBounds(300, 110, 150, 20);
        r5.setEnabled(false);
        frame.add(r5);
        JRadioButton r6 = new JRadioButton("beer_training.txt");
        r6.setBounds(300, 160, 150, 20);
        r6.setEnabled(false);
        frame.add(r6);
        JRadioButton r7 = new JRadioButton("beer_test.txt");
        r7.setBounds(300, 210, 150, 20);
        frame.add(r7);
        r7.setEnabled(false);
        JRadioButton r8 = new JRadioButton("Import File");
        r8.setBounds(300, 260, 90, 20);
        r8.setEnabled(false);
        frame.add(r8);
        ButtonGroup testingFileRadioButtons = new ButtonGroup();
        testingFileRadioButtons.add(r5);
        testingFileRadioButtons.add(r6);
        testingFileRadioButtons.add(r7);
        testingFileRadioButtons.add(r8);
        r1.setSelected(true);

        // Making a toggle button to switch between one and 2 files
        JToggleButton twoFileSwitch = new JToggleButton("Enable 2 Files");
        twoFileSwitch.setBounds((frame.getBounds().width / 2 - buttonWidth / 2), 50, 200, 30);
        twoFileSwitch.setSelected(false);
        frame.add(twoFileSwitch);
        twoFileSwitch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Disable the test file buttons if the dual file option isn't selected
                r5.setEnabled(!r5.isEnabled());
                r6.setEnabled(!r6.isEnabled());
                r7.setEnabled(!r7.isEnabled());
                r8.setEnabled(!r8.isEnabled());

                if (twoFileSwitch.isSelected()) {
                    twoFileSwitch.setText("Disable 2 Files");
                } else {
                    twoFileSwitch.setText("Enable 2 Files");
                }
            }
        });

        // Making all frame components visible
        frame.setVisible(true);
        // Adding listener for the button that runs the algorithm
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String trainingFile = "";
                String testFile = "";
                boolean dualFileOption = twoFileSwitch.isSelected();

                // Find training file selected
                if (r4.isSelected()) {
                    String location = textBoxTestFileInput.getText();
                    // Check to see if the file exists
                    try {
                        new FileReader(location);
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid File Location");
                        resetAlgorithm();
                        return;
                    }
                    trainingFile = textBoxTestFileInput.getText();
                } else {
                    // Find training file selection
                    if (r1.isSelected()) {
                        trainingFile = "src/beer.txt";
                    } else if (r2.isSelected()) {
                        trainingFile = "src/beer_training.txt";
                    } else if (r3.isSelected()) {
                        trainingFile = "src/beer_test.txt";
                    }
                }

                // If the user passes in two files
                if (dualFileOption) {
                    // Find Test File selected
                    if (r8.isSelected()) {
                        String location = textBoxTestFileInput.getText();
                        // Check to see if the file exists
                        try {
                            new FileReader(location);
                        } catch (FileNotFoundException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid File Location");
                            resetAlgorithm();
                            return;
                        }
                        testFile = textBoxTestFileInput.getText();
                    } else {
                        // Find training file selection
                        if (r5.isSelected()) {
                            testFile = "src/beer.txt";
                        } else if (r6.isSelected()) {
                            testFile = "src/beer_training.txt";
                        } else if (r7.isSelected()) {
                            testFile = "src/beer_test.txt";
                        }
                    }
                }
                trainModelAndTest(trainingFile, testFile, dualFileOption);
                classificationDisplay.setText("Classification success rate: " + testingResults);
                String matrix = "      [ale, stout, lager]"+"\n[ale]   "+Arrays.toString(matrixResult[0])+ "\n[stout] " +Arrays.toString(matrixResult[1]) + "\n[lager] " +Arrays.toString(matrixResult[2]);
                matrixDisplay.setText(matrix);
            }
        });

    }

    //Author = Jaroslav Kucera
    public static void trainModelAndTest(final String trainingFileName, final String testFileName, final boolean dualFileOption) {
        // If dual file option selected se the training and dta sets
        if (dualFileOption) {
            // Create training file
            File trainingFile = new File(trainingFileName);
            // Read in the user input file
            DataPreProcess.ReadInData(trainingFile);
            // Remove ID column
            DataPreProcess.removeColumnFromDataSet("beer_id");
            // Set trainingDataSet to DataSet
            trainingDataset = dataSet;
            // Reset DataSet
            headings = new ArrayList<>(Arrays.asList("calorific_value", "nitrogen", "turbidity", "style", "alcohol", "sugars", "bitterness", "beer_id", "colour", "degree_of_fermentation"));
            dataSet = new ArrayList<>();
            // Create test file
            File testFile = new File(testFileName);
            // Read in the user input file
            DataPreProcess.ReadInData(testFile);
            // Remove ID column
            DataPreProcess.removeColumnFromDataSet("beer_id");
            // Set testDataSet to DataSet
            testingDataset = dataSet;
        } else {
            // Create training file
            File trainingFile = new File(trainingFileName);
            // Read in the user input file
            DataPreProcess.ReadInData(trainingFile);
            // Remove ID column
            DataPreProcess.removeColumnFromDataSet("beer_id");
        }

        //Case with best accuracy
        double bestAccuracy = 0.0;
        output = "";
        int [][] bestMatrix = null;
        //Loop 10 times as per assignment requirements
        for (int i = 0; i < 10; i++) {
            output += "Starting dataset division for dataset Split number :" + i + "\n";
            System.out.println("Starting dataset division for dataset Split number :" + i);
            if (!dualFileOption) {
                // Check for invalid file contents
                try {
                    //Divide data into thirds, one third is testing, two thirds is training.
                    DataPreProcess.datasetDivision();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid File Contents");
                    resetAlgorithm();
                    return;
                }
            }
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
            output += "\n" + "Training Dataset " + i + " has an error rate of " + (errorRate * 100) + "%" + "\n\n";
            System.out.println("\n" + "Training Dataset " + i + " has an error rate of " + (errorRate * 100) + "%" + "\n");
            if (bestAccuracy < ((double) 100 - (errorRate * 100))) {
                bestAccuracy = ((double) 100 - (errorRate * 100));
                bestMatrix = errorRateData;
            }
            //Print tree
            output += "Created model visualisation:\n";
            System.out.println("Created model visualisation:");
            printTree(root);
        }
        matrixResult = bestMatrix;
        output += "\n" + "Model with best accuracy was with: " + bestAccuracy + "%\n";
        System.out.println("\n" + "Model with best accuracy was with: " + bestAccuracy + "%");
        testingResults = String.valueOf(bestAccuracy);
        try (PrintWriter outputFile = new PrintWriter("Results.txt")) {
            outputFile.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        resetAlgorithm();
    }

    //Author = Jakub Wojtkowicz
    // Resets global variables for the algorithm
    private static void resetAlgorithm() {
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
            output += lineToPrint + "\n\n\n";
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

