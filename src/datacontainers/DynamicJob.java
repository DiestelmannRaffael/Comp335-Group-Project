package datacontainers;
// JOBN submit_time (int) job_ID (int)
//estimated_runtime (int) #CPU_cores (int)
//memory (int) disk (int)
public class DynamicJob {
    private int submitTime;
    private int jobId;
    private int estRunTime;
    private int cpuCores;
    private int memory;
    private int disk;

    public int getSubmitTime() {
        return submitTime;
    }

    public int getJobId() {
        return jobId;
    }

    public int getEstRunTime() {
        return estRunTime;
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

    public DynamicJob(int submitTime, int jobId, int estRunTime, int cpuCores, int memory, int disk) {
        this.submitTime = submitTime;
        this.jobId = jobId;
        this.estRunTime = estRunTime;
        this.cpuCores = cpuCores;
        this.memory = memory;
        this.disk = disk;
    }
}
