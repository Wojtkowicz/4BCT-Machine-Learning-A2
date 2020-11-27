public class Node {

    private String name;
    private String value;
    private Node leftChild;
    private Node rightChild;

    public Node(String name, String value, Node leftChild, Node rightChild){
        this.name = name;
        this.value = value;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public Node(String name, String value){
        this.name = name;
        this.value = value;
        this.leftChild = null;
        this.rightChild = null;
    }

    public Node(){
        this.name = null;
        this.value = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }
}
