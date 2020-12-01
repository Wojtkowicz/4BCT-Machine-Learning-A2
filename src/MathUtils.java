import java.util.ArrayList;
import java.util.Arrays;

public class MathUtils {
    //Author = Jakub Wojtkowicz
    // Calculates Entropy of the three possible results (ale, lager stout)
    public static double calculateEntropy(final double aleCount, final double lagerCount, final double stoutCount) {
        double totalCount = aleCount + lagerCount + stoutCount;
        double pAle = aleCount / totalCount;
        double pLager = lagerCount / totalCount;
        double pStout = stoutCount / totalCount;
        double log2pAle = log2(pAle);
        double log2pLager = log2(pLager);
        double log2pStout = log2(pStout);

        double answer = -pAle * log2pAle - pLager * log2pLager - pStout * log2pStout;

        if (Double.isNaN(answer)) {
            return 0d;
        } else {
            return answer;
        }
    }
    //Author = Jakub Wojtkowicz
    // Calculates Entropy of the two possible results (no, yes)
    public static double calculateSingularEntropy(final double noCount, final double yesCount) {
        double totalCount = noCount + yesCount;
        double pNo = noCount / totalCount;
        double pYes = yesCount / totalCount;
        double log2pNo = log2(pNo);
        double log2pYes = log2(pYes);

        double answer = (- pNo * log2pNo - pYes * log2pYes);

        if (Double.isNaN(answer)) {
            return 0d;
        } else {
            return answer;
        }
    }


    //Author = Jakub Wojtkowicz
    public static double calculateInformationGain(final double value, final ArrayList<Double> column, final ArrayList<String> resultsColumn) {
        int aleCountLessThan = 0;
        int lagerCountLessThan = 0;
        int stoutCountLessThan = 0;
        int aleCountMoreThan = 0;
        int lagerCountMoreThan = 0;
        int stoutCountMoreThan = 0;

        double entropyLessThanValueCount = 0;
        double entropyMoreThanValueCount = 0;
        int columnSize = column.size();

        // For each value in the column
        for (int i = 0; i < columnSize;  i++) {
            // Count amount of ale, lager and stout results when column value <= value
            if (column.get(i) <= value) {
                entropyLessThanValueCount++;
                switch (resultsColumn.get(i)) {
                    case "ale":
                        aleCountLessThan++;
                        break;
                    case "lager":
                        lagerCountLessThan++;
                        break;
                    case "stout":
                        stoutCountLessThan++;
                        break;
                }
            }
            // Count amount of ale, lager and stout results when column value > value
            else if(column.get(i) > value){
                entropyMoreThanValueCount++;
                switch (resultsColumn.get(i)) {
                    case "ale":
                        aleCountMoreThan++;
                        break;
                    case "lager":
                        lagerCountMoreThan++;
                        break;
                    case "stout":
                        stoutCountMoreThan++;
                        break;
                }
            }
        }
        int totalAleCount = aleCountLessThan + aleCountMoreThan;
        int totalLagerCount = lagerCountLessThan + lagerCountMoreThan;
        int totalStoutCount = stoutCountLessThan + stoutCountMoreThan;
        double entropyLessThanValue = 0;
        double entropyMoreThanValue = 0;
        double globalEntropy = 0;

        if(totalAleCount == 0){
            // Calculate entropy of values less than value
            entropyLessThanValue = calculateSingularEntropy(lagerCountLessThan, stoutCountLessThan);
            // Calculate entropy of values more than value
            entropyMoreThanValue = calculateSingularEntropy(lagerCountMoreThan, stoutCountMoreThan);
            // Calculate entropy of all values
            globalEntropy = calculateSingularEntropy(totalLagerCount, totalStoutCount);

        }
        else if(totalLagerCount == 0){
            // Calculate entropy of values less than value
            entropyLessThanValue = calculateSingularEntropy(aleCountLessThan, stoutCountLessThan);
            // Calculate entropy of values more than value
            entropyMoreThanValue = calculateSingularEntropy(aleCountMoreThan, stoutCountMoreThan);
            // Calculate entropy of all values
            globalEntropy = calculateSingularEntropy(totalLagerCount, totalStoutCount);
        }
        else if(totalStoutCount == 0){
            // Calculate entropy of values less than value
            entropyLessThanValue = calculateSingularEntropy(aleCountLessThan, stoutCountLessThan);
            // Calculate entropy of values more than value
            entropyMoreThanValue = calculateSingularEntropy(aleCountMoreThan, stoutCountMoreThan);
            // Calculate entropy of all values
            globalEntropy = calculateSingularEntropy(totalLagerCount, totalStoutCount);
        }
        else{
            // Calculate entropy of values less than value
            entropyLessThanValue = calculateEntropy(aleCountLessThan, lagerCountLessThan, stoutCountLessThan);

            // Calculate entropy of values more than value
            entropyMoreThanValue = calculateEntropy(aleCountMoreThan, lagerCountMoreThan, stoutCountMoreThan);

            // Calculate entropy of all values
            globalEntropy = calculateEntropy((aleCountLessThan + aleCountMoreThan), (lagerCountLessThan + lagerCountMoreThan), (stoutCountLessThan + stoutCountMoreThan));
        }

        // Calculate and return information gain
        return (globalEntropy - (entropyLessThanValueCount / columnSize) * (entropyLessThanValue) - (entropyMoreThanValueCount / columnSize) * (entropyMoreThanValue));
    }
    //Author = Jakub Wojtkowicz
    // Goes through each value and performs information gain calculation on it then returns the highest value
    public static ArrayList<Double> calculateOptimalThreshold(final ArrayList<Double> data, final Column column) {
        double highestInformationGain = 0;
        double highestInformationGainOrigin = 0;
        double informationGain = 0;
        ArrayList<String> resultData = new ArrayList<>();
        column.attributes.forEach(a -> resultData.add(a.getValue()));
        // For each value
        for(int i=0; i<data.size(); i++){
            // Calculate the gain
            informationGain = calculateInformationGain(data.get(i), data, resultData);
            // If new gain is bigger than highest gain, set highest gain to new gain
            if(informationGain > highestInformationGain){
                highestInformationGain = informationGain;
                highestInformationGainOrigin = data.get(i);
            }
        }
        // Return highest gain / threshold
        return new ArrayList(Arrays.asList(highestInformationGainOrigin, highestInformationGain));
    }
    //Author = Jakub Wojtkowicz
    // Calculates log base 2
    public static double log2(double x) {
        return (Math.log(x) / Math.log(2));
    }
    //Author = Jaroslav Kucera
    public static int countAttribute(Column data, String attributeToCount){
        int numAttribute = 0;
        for(int i =0; i < data.attributes.size(); i++){
            if(data.attributes.get(i).getValue().equals(attributeToCount)){
                numAttribute++;
            }
        }
        return numAttribute;
    }
    //Author = Jaroslav Kucera
    //Calculate the information gain ratio based on arraylist of a column of attributes and column of class attributes
    public static ArrayList<Double> calculateInformationGainRatio(final ArrayList<Double> data, final Column column){

        double highestInformationGainRatio = 0;
        double highestInformationGainRatioOrigin = 0;
        double informationGain = 0;
        double splitInfo = 0;
        double informationGainRatio = 0;
        ArrayList<String> resultData = new ArrayList<>();
        column.attributes.forEach(a -> resultData.add(a.getValue()));
        // For each value
        for(int i=0; i<data.size(); i++){
            // Calculate the gain
            informationGain = calculateInformationGain(data.get(i), data, resultData);

            //Calculate split info
            splitInfo = calculateSplitInformation(data, data.get(i));
            //calculate information gain ratio formula = gain / split info
            informationGainRatio = informationGain/splitInfo;
            // If new gain is bigger than highest gain, set highest gain to new gain
            if(informationGainRatio > highestInformationGainRatio){
                highestInformationGainRatio = informationGain;
                highestInformationGainRatioOrigin = data.get(i);
            }
        }
        return new ArrayList(Arrays.asList(highestInformationGainRatioOrigin, highestInformationGainRatio));
    }
    //Author = Jaroslav Kucera
    //Method for calculating split information based on column of double attributes and threshold value
    public static double calculateSplitInformation(final ArrayList<Double> column, final Double threshold){

        int totalNumAttributes = column.size();
        int numAttributesMoreThan = 0; //number of attributes more than threshold
        int numAttributesLessThan = 0; //number of attributes less than threshold
        double splitInfo = 0d; //split information to return

        //Get attribute numbers in relation to threshold
        for(int i = 0; i < totalNumAttributes; i++){
            if(column.get(i) > threshold){
                numAttributesMoreThan++;
            }else if(column.get(i) <= threshold){
                numAttributesLessThan++;
            }else{
                System.out.println("Error in Calculation of Split Value Info with value of: "+column.get(i));
            }
        }
        double firstLog = log2(numAttributesLessThan);
        double secondLog = log2(numAttributesMoreThan);
        double totalLog = log2(totalNumAttributes);

        double calculationLog1 = firstLog - totalLog;
        double calculationLog2 = secondLog - totalLog;

        //Calculate splitInfo where formula = -(less than threshold/total)*log2(less than threshold/total)-(more than threshold/total)*log2(more than threshold/total)
        splitInfo = -(((double)numAttributesLessThan/totalNumAttributes))*calculationLog1-(((double)numAttributesMoreThan/totalNumAttributes))*calculationLog2;
        return splitInfo;
    }
}
