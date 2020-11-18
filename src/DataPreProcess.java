import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataPreProcess {
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
}
