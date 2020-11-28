import java.util.ArrayList;

public abstract class DataStructure {

    public ArrayList<Attribute> attributes;
    private int ID;

    public DataStructure(int ID, ArrayList<Attribute> attributes) {
        this.attributes = attributes;
        this.ID = ID;
    }
    public DataStructure(int ID){
        this.ID = ID;
        attributes = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void addAttribute(Attribute input) {
        attributes.add(input);
    }

    public void removeAttribute(int attributeIndex) {
        attributes.remove(attributeIndex);
    }

    public String toStringAllAttributes() {
        String output = "";
        for (Attribute a : attributes) {
            output += a.toString();
        }
        return output;
    }

    @Override
    public String toString() {
        return "\nID:" + this.ID + " contains attributes:\n" + this.attributes;
    }
}
