import java.util.ArrayList;
import java.util.List;

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

    public static double calculateInformationGain(final double value, final ArrayList<Double> column, final ArrayList<String> resultsColumn) {
        int aleCount = 0;
        int lagerCount = 0;
        int stoutCount = 0;
        double entropyLessThanValueCount = 0;
        double entropyMoreThanValueCount = 0;
        int columnSize = column.size();

        // Count amount of ale, lager and stout results when column value <= value
        for (int i = 0; i < columnSize;  i++) {
            if (column.get(i) <= value) {
                entropyLessThanValueCount++;
                switch (resultsColumn.get(i)) {
                    case "Ale":
                        aleCount++;
                        break;
                    case "Lager":
                        lagerCount++;
                        break;
                    case "Stout":
                        stoutCount++;
                        break;
                }
            }
        }

        // Calculate entropy of values less than value
        double entropyLessThanValue = calculateEntropy(aleCount, lagerCount, stoutCount);

        // Reset count
        aleCount = 0;
        lagerCount = 0;
        stoutCount = 0;

        // Count amount of ale, lager and stout results when column value > value
        for (int i = 0; i < columnSize; i++) {
            if (column.get(i) > value) {
                entropyMoreThanValueCount++;
                switch (resultsColumn.get(i)) {
                    case "Ale":
                        aleCount++;
                        break;
                    case "Lager":
                        lagerCount++;
                        break;
                    case "Stout":
                        stoutCount++;
                        break;
                }
            }
        }

        // Calculate entropy of values less than value
        double entropyMoreThanValue = calculateEntropy(aleCount, lagerCount, stoutCount);

        // Reset count
        aleCount = 0;
        lagerCount = 0;
        stoutCount = 0;

        // Count amount of ale, lager and stout results for all column values
        for (int i = 0; i < columnSize; i++) {
            switch (resultsColumn.get(i)) {
                case "Ale":
                    aleCount++;
                    break;
                case "Lager":
                    lagerCount++;
                    break;
                case "Stout":
                    stoutCount++;
                    break;
            }
        }

        // Calculate entropy of all values
        double globalEntropy = calculateEntropy(aleCount, lagerCount, stoutCount);

        // Calculate and return information gain
        return globalEntropy - (entropyLessThanValueCount / columnSize) * (entropyLessThanValue) - (entropyMoreThanValueCount / columnSize) * (entropyMoreThanValue);
    }

    // Goes through each value and performs information gain calculation on it then returns the highest vaalue
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
}
