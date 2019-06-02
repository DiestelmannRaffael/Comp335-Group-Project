package datacontainers.dynamiccontainers;

import java.util.List;

public class DynamicServer {
    private String serverType;
    private int serverTypeId;
    private int serverState;
    private int availableTime;
    private int cpuCores;
    private int memory;
    private int disk;
    private int serverTypeValue;

    private List<DynamicJob> jobList;

    public DynamicServer(String serverType, int serverTypeId, int serverState, int availableTime, int cpuCores, int memory, int disk) {
        this.serverType = serverType;
        this.serverTypeId = serverTypeId;
        this.serverState = serverState;
        this.availableTime = availableTime;
        this.cpuCores = cpuCores;
        this.memory = memory;
        this.disk = disk;

        switch (serverType) {
            case "tiny": this.serverTypeValue = 0;
                break;
            case "small": this.serverTypeValue = 1;
                break;
            case "medium": this.serverTypeValue = 2;
                break;
            case "large": this.serverTypeValue = 3;
                break;
            case "xlarge": this.serverTypeValue = 4;
                break;
        }
    }
    public int getServerTypeValue() { return serverTypeValue; }

    public String getServerType() {
        return serverType;
    }

    public int getServerTypeId() {
        return serverTypeId;
    }

    public int getServerState() {
        return serverState;
    }

    public int getAvailableTime() {
        return availableTime;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public int getMemory() {
        return memory;
    }

    public int getDisk() {
        return disk;
    }

    public void setJobList(List<DynamicJob> jobList) {
        this.jobList = jobList;
    }
}
