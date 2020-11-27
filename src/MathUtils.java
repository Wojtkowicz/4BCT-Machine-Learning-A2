import java.util.ArrayList;

public class MathUtils {

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
                    case "Ale":
                        aleCountLessThan++;
                        break;
                    case "Lager":
                        lagerCountLessThan++;
                        break;
                    case "Stout":
                        stoutCountLessThan++;
                        break;
                }
            }
            // Count amount of ale, lager and stout results when column value > value
            else if(column.get(i) > value){
                entropyMoreThanValueCount++;
                switch (resultsColumn.get(i)) {
                    case "Ale":
                        aleCountMoreThan++;
                        break;
                    case "Lager":
                        lagerCountMoreThan++;
                        break;
                    case "Stout":
                        stoutCountMoreThan++;
                        break;
                }
            }
        }

        // Calculate entropy of values less than value
        double entropyLessThanValue = calculateEntropy(aleCountLessThan, lagerCountLessThan, stoutCountLessThan);

        // Calculate entropy of values more than value
        double entropyMoreThanValue = calculateEntropy(aleCountMoreThan, lagerCountMoreThan, stoutCountMoreThan);

        // Calculate entropy of all values
        double globalEntropy = calculateEntropy((aleCountLessThan + aleCountMoreThan), (lagerCountLessThan + lagerCountMoreThan), (stoutCountLessThan + stoutCountMoreThan));

        // Calculate and return information gain
        return (globalEntropy - (entropyLessThanValueCount / columnSize) * (entropyLessThanValue) - (entropyMoreThanValueCount / columnSize) * (entropyMoreThanValue));
    }

    // Goes through each value and performs information gain calculation on it then returns the highest value
    public static double calculateOptimalThreshold(final ArrayList<Double> data, final ArrayList<String> resultsData) {
        double highestInformationGain = 0;
        double informationGain = 0;
        // For each value
        for(int i=0; i<data.size(); i++){
            // Calculate the gain
            informationGain = calculateInformationGain(data.get(i), data, resultsData);
            // If new gain is bigger than highest gain, set highest gain to new gain
            if(informationGain > highestInformationGain){
                highestInformationGain = informationGain;
            }
        }
        // Return highest gain / threshold
        return highestInformationGain;
    }

    // Calculates log base 2
    public static double log2(double x) {
        return (Math.log(x) / Math.log(2));
    }

    public static int countAttribute(Row data, String attributeToCount){
        int numAttribute = 0;
        for(int i =0; i < data.attributes.size(); i++){
            if(data.attributes.get(i).getValue().equals(attributeToCount)){
                numAttribute++;
            }
        }
        return numAttribute;
    }
}
