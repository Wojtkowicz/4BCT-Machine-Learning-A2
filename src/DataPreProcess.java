import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DataPreProcess {
    //Author = Jakub Wojtkowicz
    // Takes in a text file and populates the DataSet Arraylist with Row objects
    public static void ReadInData(final File file){
        int rowNumber = 1;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            // Line by line convert the line to a row and add to the data set
            while (line != null){
                // Add Row to data set
                Main.dataSet.add(lineToRow(line, rowNumber));
                line = bufferedReader.readLine();
                rowNumber++;
            }
            bufferedReader.close();
        }
        catch (IOException ex){
            ex.printStackTrace();

        }
    }
    //Author = Jakub Wojtkowicz
    // Takes in a line of text and creates a Row object
    private static Row lineToRow(final String line, final int ID){
        int index = 0;
        int headingIndex = 0;
        char currentChar;
        String currentValue = "";
        ArrayList<Attribute> rowAttributes = new ArrayList<>();

        // While the index of the line is less than the length of the line
        while(index < line.length()) {
            currentChar = line.charAt(index);
            // If you have reached the end or the current char is a tab
            if (currentChar == '\t' || index == line.length()-1) {
                // Create a new attribute with current value and the appropriate heading then
                // add it to the row list and reset current value
                rowAttributes.add(new Attribute(Main.headings.get(headingIndex), currentValue));
                headingIndex++;
                currentValue = "";
            }
            // Iterate through each char and add to to current value
            else {
                   String temp = String.valueOf(currentChar);
                   currentValue = currentValue + temp;
            }
            index++;
        }
        return new Row(ID, new ArrayList<>(rowAttributes));
    }
    //Author = Jakub Wojtkowicz
    public static void removeColumnFromDataSet(final String columnName){
        Integer columnIndex = null;
        for(int i=0; i<Main.headings.size(); i++){
            if(Main.headings.get(i).equals(columnName)){
                columnIndex = i;
                Main.headings.remove(i);
                break;
            }
        }
        int finalColumnIndex = columnIndex;
        Main.dataSet.forEach(row -> row.removeAttribute(finalColumnIndex));
    }
    //Author = Jaroslav Kucera
    public static void datasetDivision(){
        //Get size of total dataset and get one third of it
        int numColumns = Main.dataSet.size();
        int divider = Math.round(numColumns/3);
        //Create a list of integers(divider size) of random integers between 0 and max size - 1
        List<Integer> randomIndex = ThreadLocalRandom.current().ints(0, numColumns-1).boxed().distinct().limit(divider).collect(Collectors.toList());
        //Loop through dataset and divide values into training or testing datasets
        for(int i = 0; i < Main.dataSet.size(); i++){
            if(randomIndex.contains(i)){
                Main.testingDataset.add(Main.dataSet.get(i));
            }else{
                Main.trainingDataset.add(Main.dataSet.get(i));
            }
        }
    }
    //Author = Jaroslav Kucera
    //Returns a column arraylist of a specific row arraylist attribute index (Continuous)
    public static ArrayList<Attribute> rowIntoAttributeCol(ArrayList<Row> row, int index){
        ArrayList<Attribute> col = new ArrayList<>();
        for(int i = 0; i < row.size(); i++){
            col.add((row.get(i).attributes.get(index)));
        }
        return col;
    }
    //Author = Jaroslav Kucera
    //Checks if a string contains a number
    public static boolean containsNumber(String s){
        char[] characters = s.toCharArray();
        StringBuilder builder = new StringBuilder();
        for(char c: characters){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }
}
