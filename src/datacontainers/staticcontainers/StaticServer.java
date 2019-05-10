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

    public String getType() {
        return type;
    }

    public int getLimit() {
        return limit;
    }

    public int getBootupTime() {
        return bootupTime;
    }

    public float getHourlyRate() {
        return hourlyRate;
    }

    public int getCoreCount() {
        return coreCount;
    }

    public int getMemory() {
        return memory;
    }

    public int getDiskSpace() {
        return diskSpace;
    }
}
