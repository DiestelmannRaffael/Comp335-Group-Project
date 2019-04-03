package datacontainers.staticcontainers;

public class StaticJob {
    private static int nextId = 0;
    private final int jobId;
    private String type;
    private int minRunTime;
    private int maxRunTime;
    private int populationRate;

    public StaticJob(String type, int minRunTime, int maxRunTime, int populationRate){
        jobId = nextId++;

        this.type = type;
        this.minRunTime = minRunTime;
        this.maxRunTime = maxRunTime;
        this.populationRate = populationRate;
    }

    @Override
    public String toString(){
        return "< type: " + type + ", minRunTime: " + minRunTime +", maxRunTime: " + maxRunTime +", populationRate: " + populationRate + " >";
    }
}
