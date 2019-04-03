package datacontainers.staticcontainers;

public class StaticServer {
    private String type;
    private int limit;
    private int bootupTime;
    private float hourlyRate;
    private int coreCount;
    private int memory;
    private int diskSpace;

    public StaticServer(String type, int limit, int bootupTime, float hourlyRate, int coreCount, int memory, int diskSpace){
        this.type = type;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
        this.coreCount = coreCount;
        this.memory = memory;
        this.diskSpace = diskSpace;
    }

    @Override
    public String toString(){
        return "< type: " + type + ", limit: " + limit + ", bootupTime: " + bootupTime + ", hourlyRate: " + hourlyRate
                + ", coreCount: " + coreCount + ", memory: " + memory + ", diskSpace: " + diskSpace +  " >";
    }
}
