import java.util.ArrayList;

public class Row {
    public ArrayList<Attribute> attributes;
    private int ID;

    public Row(int ID){
        attributes = new ArrayList<>();
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void addAttribute(Attribute input){
        attributes.add(input);
    }

    public void removeAttribute(int attributeIndex){
        attributes.remove(attributeIndex);
    }

    public String toStringAllAttributes(){
        String output = "";
        for( Attribute a : attributes){
            output += a.toString();
        }
        return output;
    }
    @Override
    public String toString() {
        return "Row ID:"+this.ID+" contains attributes:\n";
    }
}
