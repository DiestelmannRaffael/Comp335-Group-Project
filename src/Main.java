/*
 * Stage 2: Stage 2: baseline algorithms
 * Group 8: Raffael Andreas Diestelmann (45569037),
 *          Connor O’Grady (45117322),
 *          Sang Woung Yoon (44298196)

 * BEST FIT Algorithm
 *
 * Git: (https://github.com/DiestelmannRaffael/Comp335-Group-Project/tree/stage2_Sang_BF)
 * */
import datacontainers.dynamiccontainers.DynamicJob;
import datacontainers.dynamiccontainers.DynamicServer;
import datacontainers.staticcontainers.StaticServer;
import datacontainers.xmlparsing.XmlReader;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    enum SchedlingType {
        FIRST_FIT, BEST_FIT, WORST_FIT
    }

    /* Get best fit server for job */
    public static DynamicServer getBestFitServer(
            DynamicJob job, List<StaticServer> staticServers,
            List<DynamicServer> dynamicServers) {
        int bestFit = Integer.MAX_VALUE;
        int minAvail = Integer.MAX_VALUE;
        int minBestFitActive = Integer.MAX_VALUE;

        DynamicServer bestFitServer = null;
        DynamicServer bestFitActiveServer = null;

        for (StaticServer currentStatic : staticServers) {
            for (DynamicServer dServer : dynamicServers) {
                if (!dServer.getServerType().equals(currentStatic.getType())) {
                    continue;
                }

                // Best fit server
                if (dServer.getCpuCores() >= job.getCpuCores()
                        && dServer.getMemory() >= job.getMemory()
                        && dServer.getDisk() >= job.getDisk()) {
                    int fitness = dServer.getCpuCores() - job.getCpuCores();
                    if (fitness < bestFit
                            || (fitness == bestFit && dServer.getAvailableTime() < minAvail)) {
                        bestFit = fitness;
                        bestFitServer = dServer;
                    }
                }

                // Best fit active server based on initial resource capacity
                if (currentStatic.getCoreCount() >= job.getCpuCores()
                        && currentStatic.getMemory() >= job.getMemory()
                        && currentStatic.getDiskSpace() >= job.getDisk()) {
                    int bestRunFitness = currentStatic.getCoreCount() - job.getCpuCores();
                    if (bestRunFitness < minBestFitActive && dServer.getServerState() == 3){
                        minBestFitActive = bestRunFitness;
                        bestFitActiveServer = dServer;
                    }
                }
            }
        }

        if (bestFitServer != null) {
            return bestFitServer;
        } else {
            return bestFitActiveServer;
        }
    }

    public static String scheduleJob(
            JobSchedulerClient client, DynamicJob job, DynamicServer dynamicServer)
            throws IOException {
        // Schedule the job
        String scheduleInfo = String.format(
                "SHCD %s %s %s", job.getJobId(), dynamicServer.getServerType(),
                dynamicServer.getServerTypeId());
        client.sendMessageToServer(scheduleInfo);
        return scheduleInfo;
    }

    public static void main(String[] args) throws IOException {
        String serverName = "127.0.0.1";
        int port = 8096;
        Socket socket = new Socket(serverName, port);
        JobSchedulerClient client = new JobSchedulerClient(socket);
        //XmlReader xmlReader = new XmlReader();

        //send the helo
        client.sendMessageToServer("HELO");

        //server responds with ok
        if (client.receiveMessageFromServer().equals("OK")) {
            //send the auth
            client.sendMessageToServer(
                    "AUTH " + System.getProperty("user.name"));
        }

        //server responds with ok
        if (client.receiveMessageFromServer().equals("OK")) {
            //send the ready response
            client.sendMessageToServer("REDY");
        }

        String jobString, serverString;

        //read the system.xml = this is the ds-config1.xml
        //parse the system.xml
        XmlReader xmlReader = new XmlReader();
        //set static server list to parsed list
        List<StaticServer> staticServers = xmlReader.getStaticServers();

        List<DynamicJob> dynamicJobList = new ArrayList<>();
        List<DynamicServer> dynamicServerList = new ArrayList<>();


        while ((jobString = client.receiveMessageFromServer())
                .contains("JOBN")) {
            // Get Job INFO
            DynamicJob newJob = parseJobString(jobString);
            dynamicJobList.add(newJob);
            client.sendMessageToServer("RESC All");
            if (client.receiveMessageFromServer().contains("DATA")) {
                client.sendMessageToServer("OK");
            }
            // Get all servers info
            dynamicServerList.clear();
            while (!(serverString = client.receiveMessageFromServer()).equals(".")) {
                dynamicServerList.add(parseServerString(serverString));
                client.sendMessageToServer("OK");
            }

            // Schedule job
            if (getSchedulingType(args) == SchedlingType.BEST_FIT) {
                DynamicServer jobServer =
                        getBestFitServer(newJob, staticServers, dynamicServerList);
                String scheduleString = scheduleJob(client, newJob, jobServer);
                // Get ready for next job
                if (client.receiveMessageFromServer().equals("OK")) {
                    client.sendMessageToServer("REDY");
                } else if (client.receiveMessageFromServer().equals("ERR")) {
                    System.out.println(
                            "ERROR: <" + scheduleString
                                    + "> invalid message recieved");
                }
            } else if (getSchedulingType(args) == SchedlingType.FIRST_FIT) {
                System.out.println("FIRST FIT, implemented separately");
            } else if (getSchedulingType(args) == SchedlingType.WORST_FIT) {
                System.out.println("WORST FIT, implemented separately");
            }
        }
        if (jobString.equals("NONE")) {
            client.sendMessageToServer("QUIT");
            if (client.receiveMessageFromServer().equals("QUIT")) {
                socket.close();
            }
        }
        socket.close();
    }

    /* Parse args to get scheduling type */
    private static SchedlingType getSchedulingType(String[] args) {
        if (!args[0].contains("-a")) {
            // Default is FIRST_FIT
            return SchedlingType.FIRST_FIT;
        }
        switch (args[1]) {
            case "ff":
                return SchedlingType.FIRST_FIT;
            case "bf":
                return SchedlingType.BEST_FIT;
            case "wf":
                return SchedlingType.WORST_FIT;
            default:
                throw new IllegalArgumentException("Invalid fit type: " + args[1]);
        }
    }

    /* Parse job string to get new job */
    private static DynamicJob parseJobString(String jobString) {
        String[] parts = jobString.split(" ");
        int submitTime = Integer.parseInt(parts[1]);
        int jobId = Integer.parseInt(parts[2]);
        int estRunTime = Integer.parseInt(parts[3]);
        int cpuCores = Integer.parseInt(parts[4]);
        int memory = Integer.parseInt(parts[5]);
        int disk = Integer.parseInt(parts[6]);

        return new DynamicJob(
                submitTime, jobId, estRunTime, cpuCores, memory, disk);
    }

    private static DynamicServer parseServerString(String serverString) {
        String[] parts = serverString.split(" ");
        String serverType = parts[0];
        int serverTypeId = Integer.parseInt(parts[1]);
        int serverState = Integer.parseInt(parts[2]);
        int availableTime = Integer.parseInt(parts[3]);
        int serverCpuCores = Integer.parseInt(parts[4]);
        int serverMemory = Integer.parseInt(parts[5]);
        int serverDisk = Integer.parseInt(parts[6]);

        return new DynamicServer(
                serverType, serverTypeId, serverState, availableTime,
                serverCpuCores, serverMemory, serverDisk);
    }
}
