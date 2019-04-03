package datacontainers;

public class Workload {
    private String type;
    private int minLoad;
    private int maxload;

    public Workload( String type, int minLoad, int maxload){
        this.type = type;
        this.minLoad = minLoad;
        this.maxload = maxload;
    }

    @Override
    public String toString(){
        return "< type: " + type + ", minLoad: " + minLoad +", maxLoad: " + maxload + " >";
    }
}
