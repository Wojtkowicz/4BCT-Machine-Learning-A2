public class Attribute {
    //Class used holding attribute values of rows

    private String name; //Name of the attribute column
    private double value; //Value of the attribute

    public Attribute(double value){
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name+": "+this.value+"\n";
    }
}
