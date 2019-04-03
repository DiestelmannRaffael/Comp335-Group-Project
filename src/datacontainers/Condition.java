package datacontainers;

public class Condition {
    private String type;
    private int value;

    public Condition( String type, int value){
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString(){
        return "< type: " + type + ", value: " + value + " >";
    }
}
