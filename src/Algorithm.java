import java.util.ArrayList;

public class Algorithm {

    private Row style;//Column of class attribute (Style)

    public Node startTreeBuilding(int classIndex, ArrayList<Row> dataset){
        style = dataset.get(classIndex); //Put class into separate column
        dataset.remove(classIndex); //Remove class index from dataset
        return subTreeBuild(dataset, 0, null); //start building tree
    }

    private Node subTreeBuild(ArrayList<Row> dataSet, int depth, String value){
        //Copy current dataset to use in this iteration
        ArrayList<Row> currentDataset = dataSet;
        //Exit case for recursion
        if(currentDataset.get(0).attributes.size() <= 1 || depth >= 5){
            return createleafNode(currentDataset, value);
        }
        //Calculate entropy of entire dataset
        double globalEntropy = MathUtils.calculateEntropy(MathUtils.countAttribute(style, "ale"), MathUtils.countAttribute(style, "lager"), MathUtils.countAttribute(style, "stout"));
        //Prepare variables for gain calculation
        String attributeWithHighestGain = "";
        String attributeValueWithHighestGain = "";
        double maxGainRatio = 0.0;
        //Calculate conditional entropy for each attribute
        for(int i= 0; i < currentDataset.size(); i++){
            double conditionalEntropy;
            double variableEntropy;
            double gainRatio;
            String bestVar = "";
            //if true => continuous, false => discrete
            if(DataPreProcess.containsNumber(currentDataset.get(i).attributes.get(0).getValue())){
                ArrayList<Double> doubleCol = new ArrayList<>();
                    for(int j = 0; j < currentDataset.get(i).attributes.size(); j++){
                        doubleCol.add(Double.parseDouble(currentDataset.get(i).attributes.get(j).getValue()));
                    }
                    gainRatio = MathUtils.calculateOptimalThreshold(doubleCol, style);
            }else{
                //discrete entropy calculation
            }
        }
        return new Node();
    }

    private Node createleafNode(ArrayList<Row> dataSet, String value){
        return new Node();
    }
}
