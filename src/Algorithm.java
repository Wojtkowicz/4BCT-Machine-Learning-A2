import java.util.ArrayList;
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
        if (currentDataset.get(0).attributes.size() <= 1 || depth >= 7) {
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
                ArrayList<Double> gain = MathUtils.calculateOptimalThreshold(doubleCol, currentDataset.get(currentDataset.size()-1));
                System.out.println("-----------------------------");
                System.out.println(doubleCol);
                System.out.println(currentDataset.get(currentDataset.size()-1));
                System.out.println(gain);
                System.out.println("-----------------------------");
                if (gain.get(1) >= maxGainRatio) {
                    maxGainRatio = gain.get(1);
                    attributeValueWithHighestGain = gain.get(0);
                    attributeWithHighestGain = currentDataset.get(i).attributes.get(0).getName();
                }
            } else {
                //discrete entropy calculation
            }
        }
        if(maxGainRatio == 0.0){
            System.out.println("Max Gain Ratio is 0");
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
        System.out.println("Ale: "+aleCount+" Stout: "+stoutCount+" Lager: "+lagerCount+"\n--------------------");
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
            leaf.setName("stout");
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
        System.out.println("ResultsData:: ");
        System.out.println(resultData);
        return resultData;
    }
}
