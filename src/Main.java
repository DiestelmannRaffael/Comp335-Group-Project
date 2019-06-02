import datacontainers.dynamiccontainers.DynamicJob;
import datacontainers.dynamiccontainers.DynamicServer;
import datacontainers.xmlparsing.XmlReader;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
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
        //XmlReader xmlReader = new XmlReader();

        //send the helo
        client.sendMessageToServer("HELO");

        //server responds with ok
        if (client.receiveMessageFromServer().equals("OK")) {
            //send the auth
            client.sendMessageToServer("AUTH " + System.getProperty("user.name"));
        }

        //server responds with ok
        if (client.receiveMessageFromServer().equals("OK")) {
            //send the ready response
            client.sendMessageToServer("REDY");
        }

        String temp;

        //read the system.xml = this is the ds-config1.xml

        List<DynamicJob> dynamicJobList = new ArrayList<>();
        List<DynamicServer> initialServerList = new ArrayList<>();

        boolean initialRun = true;

        while ((temp = client.receiveMessageFromServer()).contains("JOBN")) {
            List<DynamicServer> dynamicServerList = new ArrayList<>();

            String[] parts = temp.split(" ");

            int submitTime = Integer.parseInt(parts[1]);
            jobId = Integer.parseInt(parts[2]);
            //System.out.println("JOB ID: " + jobId);
            int estRunTime = Integer.parseInt(parts[3]);
             cpuCores = Integer.parseInt(parts[4]);
             memory = Integer.parseInt(parts[5]);
             disk = Integer.parseInt(parts[6]);

            DynamicJob dynamicJob = new DynamicJob(submitTime, jobId, estRunTime, cpuCores, memory, disk);
            dynamicJobList.add(dynamicJob);

//            client.sendMessageToServer("RESC Avail " + cpuCores + " " + memory + " " + disk);
            client.sendMessageToServer("RESC All");

            if (client.receiveMessageFromServer().contains("DATA")) {
                client.sendMessageToServer("OK");
            }

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

                Collections.sort(dynamicServerList, new ServerSorter());
                Collections.sort(initialServerList, new ServerSorter());
                client.sendMessageToServer("OK");

            }
            initialRun = false;

            

            //first fit algorithm
            if (args[0].equals("-a") && args[1].equals("ff")) {
                firstFit(dynamicServerList, initialServerList);
            }

            if (args[0].equals("-a") && args[1].equals("new")) {
                for (DynamicServer ds : dynamicServerList) {
                    if(ds.getServerState() == 2 && ds.getCpuCores() > cpuCores) { // schedule to idle server first
                        scheduleInfo = "SCHD" + jobId + " " + ds.getServerType() + " " + ds.getServerTypeId();
                    } else if (ds.getServerState() == 0 && ds.getCpuCores() > cpuCores) { // bootup inactive servers
                        scheduleInfo = "SCHD" + jobId + " " + ds.getServerType() + " " + ds.getServerTypeId();
                    } else if (ds.getServerState() == 1 && ds.getCpuCores() > cpuCores) { // bootup inactive servers
                        scheduleInfo = "SCHD" + jobId + " " + ds.getServerType() + " " + ds.getServerTypeId();
                    } else if (ds.getServerState() == 3 && ds.getCpuCores() > cpuCores) { // bootup inactive servers
                        scheduleInfo = "SCHD" + jobId + " " + ds.getServerType() + " " + ds.getServerTypeId();
                    } else if (ds.getServerState() == 4 && ds.getCpuCores() > cpuCores) { // bootup inactive servers
                        scheduleInfo = "SCHD" + jobId + " " + ds.getServerType() + " " + ds.getServerTypeId();
                    } else {
                        firstFit(dynamicServerList, initialServerList);
                    }
                }
            }

            //response from server "OK"
            if (client.receiveMessageFromServer().equals("OK")) {
                //send back "REDY"
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

    private static void firstFit(List<DynamicServer> dynamicServerList, List<DynamicServer> initialServerList) throws IOException {
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
}
