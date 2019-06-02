import datacontainers.dynamiccontainers.DynamicJob;
import datacontainers.dynamiccontainers.DynamicServer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static int cpuCores;
    private static int disk;
    private static int memory;
    private static int jobId;
    private static JobSchedulerClient client;
    private static String scheduleInfo = null;

    public static void main(String[] args) throws IOException {
        String serverName = "127.0.0.1";
        int port = 8096;
        Socket socket = new Socket(serverName, port);
        client = new JobSchedulerClient(socket);

        client.sendMessageToServer("HELO");

        if (client.receiveMessageFromServer().equals("OK")) {
            client.sendMessageToServer("AUTH " + System.getProperty("user.name"));
        }

        if (client.receiveMessageFromServer().equals("OK")) {
            client.sendMessageToServer("REDY");
        }

        String temp;

        List<DynamicJob> dynamicJobList = new ArrayList<>();
        List<DynamicServer> initialServerList = new ArrayList<>();

        while ((temp = client.receiveMessageFromServer()).contains("JOBN")) {
            List<DynamicServer> dynamicServerList = new ArrayList<>();

            String[] parts = temp.split(" ");

            int submitTime = Integer.parseInt(parts[1]);
            jobId = Integer.parseInt(parts[2]);
            int estRunTime = Integer.parseInt(parts[3]);
            cpuCores = Integer.parseInt(parts[4]);
            memory = Integer.parseInt(parts[5]);
            disk = Integer.parseInt(parts[6]);

            DynamicJob dynamicJob = new DynamicJob(submitTime, jobId, estRunTime, cpuCores, memory, disk);
            dynamicJobList.add(dynamicJob);
            

            //first fit algorithm
            if (args[0].equals("-a") && args[1].equals("ff")) {
                firstFit(dynamicServerList, initialServerList);

            }

            if (args[0].equals("-a") && args[1].equals("new")) {
                optimizeTurnaround(dynamicServerList, initialServerList);
            }

            if (client.receiveMessageFromServer().equals("OK")) {
                client.sendMessageToServer("REDY");
            } else if (client.receiveMessageFromServer().equals("ERR")) {
                System.out.println("ERROR: <" + scheduleInfo + "> invalid message recieved");
            }
        }
        if (temp.equals("NONE")) {
            client.sendMessageToServer("QUIT");
            if (client.receiveMessageFromServer().equals("QUIT")) {
                socket.close();
            }
        }
        socket.close();
    }

    private static void optimizeTurnaround(List<DynamicServer> dynamicServerList, List<DynamicServer> initialServerList) throws IOException {
        rescAll(dynamicServerList, initialServerList);
        Collections.sort(dynamicServerList, new ServerSorter());

                int minRuntime = Integer.MAX_VALUE;
                DynamicServer scheduleCandidate = null;
                for (DynamicServer ds : dynamicServerList) {
                    if(ds.getCpuCores() >= cpuCores) {
                        List<DynamicJob> jobList;
                        ds.setJobList(jobList = LSTJ(ds.getServerType(), ds.getServerTypeId()));
                        int runtime = getJobRuntime(jobList);
                        if (runtime < minRuntime) {
                            minRuntime = runtime;
                            scheduleCandidate = ds;
                        }
                    }
                }

                scheduleInfo = "SCHD " + jobId + " " + scheduleCandidate.getServerType() + " " + scheduleCandidate.getServerTypeId();
                client.sendMessageToServer(scheduleInfo);

                // submitToLowestRuntime
            //}
        //}
    }

    private static int getJobRuntime(List<DynamicJob> jobList) {
        int runtime = 0;
        for(DynamicJob job : jobList) {
            runtime += job.getSubmitTime() + job.getEstRunTime();
        }
        return runtime;
    }

    private static void firstFit(List<DynamicServer> dynamicServerList, List<DynamicServer> initialServerList) throws IOException {
        rescAll(dynamicServerList, initialServerList);
        Collections.sort(dynamicServerList, new ServerSorter());
        Collections.sort(initialServerList, new ServerSorter());

        int i = 0;
        boolean lastElement = false;
        for (DynamicServer ds : dynamicServerList) {
            if(i++ == dynamicServerList.size() - 1)
                lastElement = true;
            if (ds.getCpuCores() >= cpuCores && ds.getDisk() >= disk && ds.getMemory() >= memory) {
                scheduleInfo = "SCHD " + jobId + " " + ds.getServerType() + " " + ds.getServerTypeId();
                client.sendMessageToServer(scheduleInfo);
                break;
                // no available server found
            } else if (lastElement) {
                for (DynamicServer is : initialServerList) {
                    if (is.getCpuCores() >= cpuCores && is.getDisk() >= disk && is.getMemory() >= memory) {
                        scheduleInfo = "SCHD " + jobId + " " + is.getServerType() + " " + is.getServerTypeId();
                        client.sendMessageToServer(scheduleInfo);
                        break;
                    }
                }
            }
        }
    }

    private static void rescAll(List<DynamicServer> dynamicServerList, List<DynamicServer> initialServerList) throws IOException {
        client.sendMessageToServer("RESC All");

        if (client.receiveMessageFromServer().contains("DATA")) {
            client.sendMessageToServer("OK");
        }
        String temp;
        String[] parts;
        boolean initialRun = false;

        while (!(temp = client.receiveMessageFromServer()).equals(".")) {

            parts = temp.split(" ");
            String serverType = parts[0];
            int serverTypeId = Integer.parseInt(parts[1]);
            int serverState = Integer.parseInt(parts[2]);
            int availableTime = Integer.parseInt(parts[3]);
            int serverCpuCores = Integer.parseInt(parts[4]);
            int serverMemory = Integer.parseInt(parts[5]);
            int serverDisk = Integer.parseInt(parts[6]);

            dynamicServerList.add(new DynamicServer(serverType, serverTypeId, serverState, availableTime, serverCpuCores,
                    serverMemory, serverDisk));
            if(initialRun) {
                initialServerList.add(new DynamicServer(serverType, serverTypeId, serverState, availableTime, serverCpuCores,
                        serverMemory, serverDisk));
            }

            client.sendMessageToServer("OK");

        }
    }

    private static List<DynamicJob> LSTJ(String serverType, int serverId) throws IOException {
        client.sendMessageToServer("LSTJ " + serverType + " " + serverId);
        String response = client.receiveMessageFromServer();
        if (response.contains("DATA")) {
            client.sendMessageToServer("OK");
        }

        String temp = null;
        String[] parts;
        ArrayList<DynamicJob> jobList = new ArrayList<>();

        while (!(temp = client.receiveMessageFromServer()).equals(".")) {

            parts = temp.split(" ");
            //job_id job_state job_start_time job_estimated_runtime #cores_required memeory_required disk_required
            int jobId = Integer.parseInt(parts[0]);
            int jobState = Integer.parseInt(parts[1]);
            int submitTime = Integer.parseInt(parts[2]);
            int estRunTime = Integer.parseInt(parts[3]);
            int cores = Integer.parseInt(parts[4]);
            int memory = Integer.parseInt(parts[5]);
            int disk = Integer.parseInt(parts[6]);

            DynamicJob dynamicJob = new DynamicJob(jobId, jobState, submitTime, estRunTime, cores, memory, disk);

            jobList.add(dynamicJob);
            client.sendMessageToServer("OK");
        }
        return jobList;
    }
}
