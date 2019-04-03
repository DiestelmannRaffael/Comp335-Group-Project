import datacontainers.DynamicJob;
import datacontainers.DynamicServer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
            String serverName = "127.0.0.1";
            int port = 8096;
            Socket socket = new Socket(serverName, port);
            JobSchedulerClient client = new JobSchedulerClient(socket);
            XmlReader xmlReader = new XmlReader();

            //send the helo
            client.sendMessageToServer("HELO");

            //server responds with ok
            if(client.receiveMessageFromServer().equals("OK")) {
                //sen the auth
                client.sendMessageToServer("AUTH comp335");
            }

            //server responds with ok
            if(client.receiveMessageFromServer().equals("OK")) {
                //send the ready response
                client.sendMessageToServer("REDY");
            }
            String temp ="";

            //read the system.xml

            List<DynamicJob> dynamicJobList = new ArrayList<>();
            List<DynamicServer> dynamicServerList = new ArrayList<>();


            while((temp = client.receiveMessageFromServer()).contains("JOBN")) {
                String[] parts = temp.split(" ");

                int submitTime = Integer.parseInt(parts[1]);
                int jobId = Integer.parseInt(parts[2]);
                int estRunTime = Integer.parseInt(parts[3]);
                int cpuCores = Integer.parseInt(parts[4]);
                int memory = Integer.parseInt(parts[5]);
                int disk = Integer.parseInt(parts[6]);

                System.out.println("job id: "+ jobId);

                DynamicJob dynamicJob = new DynamicJob(submitTime, jobId, estRunTime, cpuCores, memory, disk);
                dynamicJobList.add(dynamicJob);

                client.sendMessageToServer("RESC All");

                if(client.receiveMessageFromServer().contains("DATA")) {
                    client.sendMessageToServer("OK");
                }

                while(!(temp = client.receiveMessageFromServer()).equals(".")) {

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

                    client.sendMessageToServer("OK");
                }
//                System.out.println("out of . while loop");

                //Schedule the job
//                String scheduleInfo = "SCHD " + dynamicJobList.get(0).getJobId() + " " +
//                        dynamicServerList.get(10).getServerType() + " " + dynamicServerList.get(10).getServerTypeId();
//                client.sendMessageToServer(scheduleInfo);

                //Schedule the job
                String scheduleInfo = "SCHD " + jobId + " " +
                        dynamicServerList.get(10).getServerType() + " " + dynamicServerList.get(10).getServerTypeId();
                client.sendMessageToServer(scheduleInfo);

                //response from server "OK"
                if(client.receiveMessageFromServer().equals("OK")){
                    //send back "REDY"
                    client.sendMessageToServer("REDY");
                }
                //send back "REDY"
//                client.sendMessageToServer("REDY");

            }
//            if(temp.equals("NONE")) {
//                System.out.println("YAAAAASSSS");
//            }

            client.sendMessageToServer("QUIT");
            if(client.receiveMessageFromServer().equals("QUIT")) {
                socket.close();
            }





            socket.close();
    }

}
