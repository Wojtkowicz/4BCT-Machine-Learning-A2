public class Attribute {
    //Class used holding attribute values of rows

    private String name; //Name of the attribute column
    private String value; //Value of the attribute

    public Attribute(String name, String value){
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name+": "+this.value+"\n";
    }
}
