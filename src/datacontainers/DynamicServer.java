package datacontainers;

public class DynamicServer {
    private String serverType;
    private int serverTypeId;
    private int serverState;
    private int availableTime;
    private int cpuCores;
    private int memory;
    private int disk;

    public DynamicServer(String serverType, int serverTypeId, int serverState, int availableTime, int cpuCores, int memory, int disk) {
        this.serverType = serverType;
        this.serverTypeId = serverTypeId;
        this.serverState = serverState;
        this.availableTime = availableTime;
        this.cpuCores = cpuCores;
        this.memory = memory;
        this.disk = disk;
    }

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
}
