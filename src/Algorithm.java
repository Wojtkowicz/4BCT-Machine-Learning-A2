import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Algorithm {

    public Node startTreeBuilding(int classIndex, ArrayList<Column> dataset) {
        Column style = dataset.get(classIndex); //Put class into separate column
        dataset.remove(classIndex); //Remove class index from dataset
        dataset.add(style); //add style to the end
        return subTreeBuild(dataset, 0); //start building tree
    }

    private Node subTreeBuild(ArrayList<Column> dataSet, int depth) {
        //Copy current dataset to use in this iteration
        ArrayList<Column> currentDataset = dataSet;
        //Exit case for recursion
        if (currentDataset.get(0).attributes.size() <= 1 || depth >= 5) {
            System.out.println("Exit Condition 1:" + currentDataset.get(0).attributes.size() + " "+depth);
            return createLeafNode(currentDataset);
        }
        //Prepare variables for gain calculation
        String attributeWithHighestGain = "";
        double attributeValueWithHighestGain = 0.0;
        double maxGainRatio = 0.0;
        double splitValue = 0;
        //Calculate conditional entropy for each attribute
        for (int i = 0; i < currentDataset.size()-1; i++) {
            //if true => continuous, false => discrete
            if (DataPreProcess.containsNumber(currentDataset.get(i).attributes.get(0).getValue())) {
                ArrayList<Double> doubleCol = new ArrayList<>();
                for (int j = 0; j < currentDataset.get(i).attributes.size(); j++) {
                    doubleCol.add(Double.parseDouble(currentDataset.get(i).attributes.get(j).getValue()));
                }
                //0 -> origin , 1-> gain
                ArrayList<Double> gain = MathUtils.calculateInformationGainRatio(doubleCol, currentDataset.get(currentDataset.size()-1));
                if (gain.get(1) > maxGainRatio) {
                    maxGainRatio = gain.get(1);
                    attributeValueWithHighestGain = gain.get(0);
                    attributeWithHighestGain = currentDataset.get(i).attributes.get(0).getName();
                }
            } else {
                //discrete entropy calculation
                System.out.println("Algorithm does not support discrete values");
                return new Node();
            }
        }
        if(maxGainRatio == 0.0){
            return createLeafNode(currentDataset);
        }
        //Create datasets for children
        ArrayList<Column> leftDataSet = addBasedOnPartition(currentDataset, attributeValueWithHighestGain, attributeWithHighestGain, "Left");
        ArrayList<Column> rightDataSet = addBasedOnPartition(currentDataset, attributeValueWithHighestGain, attributeWithHighestGain, "Right");

        Node node = new Node(attributeWithHighestGain, String.valueOf(attributeValueWithHighestGain), subTreeBuild(leftDataSet, depth+1), subTreeBuild(rightDataSet, depth+1));

        return node;
    }

    private Node createLeafNode(ArrayList<Column> dataSet) {
        int aleCount = 0;
        int stoutCount = 0;
        int lagerCount = 0;
        Node leaf = new Node();

        for(int i = 0; i < dataSet.get(0).attributes.size(); i++){
            if(dataSet.get(dataSet.size()-1).attributes.get(i).getValue().equals("ale")){
                aleCount++;
            }else if(dataSet.get(dataSet.size()-1).attributes.get(i).getValue().equals("lager")){
                lagerCount++;
            }else if(dataSet.get(dataSet.size()-1).attributes.get(i).getValue().equals("stout")){
                stoutCount++;
            }else{
                System.out.println("Error in Create Leaf Node Function");
            }


        }
        if(aleCount >= stoutCount && aleCount >= lagerCount){
            leaf.setName("ale");
            leaf.setValue(null);
            leaf.setLeftChild(null);
            leaf.setRightChild(null);
        }else if(stoutCount >= aleCount && stoutCount >= lagerCount){
            leaf.setName("stout");
            leaf.setValue(null);
            leaf.setLeftChild(null);
            leaf.setRightChild(null);
        }else if(lagerCount >= aleCount && lagerCount >= stoutCount){
            leaf.setName("lager");
            leaf.setValue(null);
            leaf.setLeftChild(null);
            leaf.setRightChild(null);
        }else{
            System.out.println("Error in counting of create leaf node");
        }
        return leaf;
    }

    public ArrayList<Column> addBasedOnPartition(ArrayList<Column> dataSet, double splitValue, String highestGainAttribute, String Child) {
        ArrayList<Column> resultData = new ArrayList<>();
        int columnSize = dataSet.get(0).attributes.size();
        int columnNumber = dataSet.size();
        ArrayList<Integer> rowPositionsToAdd = new ArrayList<>();

        // filter only rows with certain value
        for (int i = 0; i < columnNumber; i++) {
            if (dataSet.get(i).attributes.get(0).getName().equals(highestGainAttribute)) {

                if (Child.equals("Left")) {
                    for (int j = 0; j < columnSize; j++) {
                        if (Double.parseDouble(dataSet.get(i).attributes.get(j).getValue()) <= splitValue) {
                            rowPositionsToAdd.add(j);
                        }
                    }
                } else {
                    for (int j = 0; j < columnSize; j++) {
                        if (Double.parseDouble(dataSet.get(i).attributes.get(j).getValue()) > splitValue) {
                            rowPositionsToAdd.add(j);
                        }
                    }
                }
                break;
            }
        }

        for (int i = 0; i < columnNumber; i++) {
            Column temp = new Column(i);
            for (int j = 0; j < columnSize; j++) {
                if (rowPositionsToAdd.contains(j)) {
                    temp.addAttribute(dataSet.get(i).attributes.get(j));
                }
            }
            resultData.add(temp);

        }
        return resultData;
    }

    public ArrayList<String> traverseTreeForTesting(Node root, Row rowToTest){
        Node testNode = root;
        while(testNode.getValue()!=null){
            String attribute = testNode.getName();
            for(int i = 0; i < rowToTest.attributes.size(); i++){
                if(rowToTest.attributes.get(i).getName().equals(attribute)){
                    if(Double.parseDouble(rowToTest.attributes.get(i).getValue()) > Double.parseDouble(testNode.getValue())){
                        if(testNode.getRightChild() != null){
                            testNode = testNode.getRightChild();
                        }else{
                            break;
                        }
                    }else if(Double.parseDouble(rowToTest.attributes.get(i).getValue()) <= Double.parseDouble(testNode.getValue())){
                        if(testNode.getLeftChild() != null){
                            testNode = testNode.getLeftChild();
                        }else{
                            break;
                        }
                    }
                }
            }
        }
        //At leaf node
        return new ArrayList(Arrays.asList(testNode.getName(), rowToTest.attributes.get(3).getValue()));

    }

    public int[][] confusionMatrix(ArrayList<ArrayList> input){
        int [][] confusionMatrix = new int[3][3];
        //Set up confusion Matrix
        for(int i = 0; i < input.size(); i++){
            if(input.get(i).get(1).equals("ale")){
                if(input.get(i).get(0).equals("ale")){
                    confusionMatrix[0][0] += 1;
                }else if(input.get(i).get(0).equals("stout")){
                    confusionMatrix[1][0] += 1;
                }else if(input.get(i).get(0).equals("lager")){
                    confusionMatrix[1][0] += 1;
                }
            }else if(input.get(i).get(1).equals("stout")) {
                if (input.get(i).get(0).equals("ale")) {
                    confusionMatrix[0][1] += 1;
                } else if (input.get(i).get(0).equals("stout")) {
                    confusionMatrix[1][1] += 1;
                } else if (input.get(i).get(0).equals("lager")) {
                    confusionMatrix[1][1] += 1;
                }
            }else if(input.get(i).get(1).equals("lager")) {
                if (input.get(i).get(0).equals("ale")) {
                    confusionMatrix[0][2] += 1;
                } else if (input.get(i).get(0).equals("stout")) {
                    confusionMatrix[1][2] += 1;
                } else if (input.get(i).get(0).equals("lager")) {
                    confusionMatrix[2][2] += 1;
                }
            }

        }
        //Print confusion matrix
        System.out.println("      [ale, stout, lager]");
        System.out.println("[ale]   "+Arrays.toString(confusionMatrix[0]));
        System.out.println("[stout] " +Arrays.toString(confusionMatrix[1]));
        System.out.println("[lager] " +Arrays.toString(confusionMatrix[2]));

        return confusionMatrix;
    }
    public double errorRate(int[][] confusionMatrix){
        int error = confusionMatrix[1][0] + confusionMatrix[2][0] + confusionMatrix[0][1] + confusionMatrix[2][1] + confusionMatrix[0][2] + confusionMatrix[1][2];
        int total = 0;
        for(int[] matrix: confusionMatrix){
            for(int i: matrix){
                total+=i;
            }
        }
        double errorRate = (double) error / total;
        return errorRate;
    }
}
