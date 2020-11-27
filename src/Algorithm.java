import java.util.ArrayList;
import java.util.Iterator;

public class Algorithm {

    private Column style;//Column of class attribute (Style)

    public Node startTreeBuilding(int classIndex, ArrayList<Column> dataset) {
        style = dataset.get(classIndex); //Put class into separate column
        dataset.remove(classIndex); //Remove class index from dataset
        return subTreeBuild(dataset, 0, null); //start building tree
    }

    private Node subTreeBuild(ArrayList<Column> dataSet, int depth, String value) {
        //Copy current dataset to use in this iteration
        ArrayList<Column> currentDataset = dataSet;
        //Exit case for recursion
        if (currentDataset.get(0).attributes.size() <= 1 || depth >= 5) {
            return createleafNode(currentDataset, value);
        }
        //Calculate entropy of entire dataset
        double globalEntropy = MathUtils.calculateEntropy(MathUtils.countAttribute(style, "ale"), MathUtils.countAttribute(style, "lager"), MathUtils.countAttribute(style, "stout"));
        //Prepare variables for gain calculation
        String attributeWithHighestGain = "";
        String attributeValueWithHighestGain = "";
        double maxGainRatio = 0.0;
        //Calculate conditional entropy for each attribute
        for (int i = 0; i < currentDataset.size(); i++) {
            //if true => continuous, false => discrete
            if (DataPreProcess.containsNumber(currentDataset.get(i).attributes.get(0).getValue())) {
                ArrayList<Double> doubleCol = new ArrayList<>();
                for (int j = 0; j < currentDataset.get(i).attributes.size(); j++) {
                    doubleCol.add(Double.parseDouble(currentDataset.get(i).attributes.get(j).getValue()));
                }
                //0 -> origin , 1-> gain
                ArrayList<Double> gain = MathUtils.calculateOptimalThreshold(doubleCol, style);
                if(gain.get(1) >= maxGainRatio){
                    maxGainRatio = gain.get(1);
                    attributeValueWithHighestGain = gain.get(0).toString();
                    attributeWithHighestGain = currentDataset.get(i).attributes.get(0).getName();
                }
            } else {
                //discrete entropy calculation
            }
        }

        return new Node();
    }

    private Node createleafNode(ArrayList<Column> dataSet, String value) {
        return new Node();
    }

    private ArrayList<Column> addBasedOnPartition(ArrayList<Column> dataSet, String splitValue, String highestGainAttribute){
        ArrayList<Column> data = dataSet;
        int size = dataSet.size();
        for(int i =0; i < dataSet.size(); i++){
            for(int j = 0; j < dataSet.get(0).attributes.size(); j++){

            }
        }

    }
}
