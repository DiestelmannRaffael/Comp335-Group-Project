/*
* Stage 1: 'vanilla' client-side simulator with a simple job dispatcher
* Group 8: Raffael Andreas Diestelmann (45569037),
*          Connor O’Grady (45117322),
*          Sang Woung Yoon (44298196)
* Git: (https://github.com/DiestelmannRaffael/Comp335-Group-Project/tree/stage1)
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
    public static void main(String[] args) throws IOException {
        String serverName = "127.0.0.1";
        int port = 8096;
        Socket socket = new Socket(serverName, port);
        JobSchedulerClient client = new JobSchedulerClient(socket);

//        for(String i : args){
//            System.out.println(i);
//        }

        //send the helo
        client.sendMessageToServer("HELO");

        //server responds with ok
        if(client.receiveMessageFromServer().equals("OK")) {
            //send the auth
            client.sendMessageToServer("AUTH "+System.getProperty("user.name"));
        }

        //server responds with ok
        if(client.receiveMessageFromServer().equals("OK")) {
            //send the ready response
            client.sendMessageToServer("REDY");
        }

        String temp;

        //read the system.xml = this is the ds-config1.xml
        XmlReader xmlReader = new XmlReader();

        List<StaticServer> staticServers = xmlReader.getStaticServers();

//        for(StaticServer i : a){
//            System.out.println(i.getType());
//        }


        List<DynamicJob> dynamicJobList = new ArrayList<>();

        while((temp = client.receiveMessageFromServer()).contains("JOBN")) {
            String[] parts = temp.split(" ");


            List<DynamicServer> dynamicServerList = new ArrayList<>();

            int submitTime = Integer.parseInt(parts[1]);
            int jobId = Integer.parseInt(parts[2]);
            int estRunTime = Integer.parseInt(parts[3]);
            int cpuCores = Integer.parseInt(parts[4]);
            int memory = Integer.parseInt(parts[5]);
            int disk = Integer.parseInt(parts[6]);

            DynamicJob dynamicJob = new DynamicJob(submitTime, jobId, estRunTime, cpuCores, memory, disk);
            dynamicJobList.add(dynamicJob);

            client.sendMessageToServer("RESC All");
//            client.sendMessageToServer("RESC Avail "+cpuCores+" "+memory+" "+disk);


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

//            System.out.println("before message");
            if(args[0].contains("-a") && args[1].contains("ff")){
                // first fit
//                System.out.println("ff");
            }else if (args[0].contains("-a") && args[1].contains("bf")){
                //best fit
//                System.out.println("bf");

            }else if (args[0].contains("-a") && args[1].contains("wf")){
                //worst fit
//                System.out.println("===== wf ====== ");
                int worstFit = cpuCores;
                int altFit = worstFit;

                //
                for(StaticServer server : staticServers){//
                    System.out.println();
                    System.out.println(server.getType());
                    //check the server type in order of the system.xml
                    for(DynamicServer dynamicServer: dynamicServerList){
                        if(dynamicServer.getServerType().equals(server.getType())){
//                            if(dynamicServer.getCpuCores())
                            System.out.println(dynamicServer.toString());
                        }
                    }


                }

                //testing=======
                socket.close();
                //testing=======

            }else{

//                System.out.println("else");
                //get the first largest server index

//                int serverIndex = 0;
//                int maxCoreCounter = 0;
//                for(int i = 0; i < dynamicServerList.size(); i++){
//                    int hold;
//                    if((hold = dynamicServerList.get(i).getCpuCores()) > maxCoreCounter){
//                        maxCoreCounter = hold;
//                        serverIndex = i;
//
//
//                    }
//                }
//                //Schedule the job
//                String scheduleInfo = "SCHD " + jobId + " " +
//                        dynamicServerList.get(serverIndex).getServerType() + " " + dynamicServerList.get(serverIndex).getServerTypeId();
//                client.sendMessageToServer(scheduleInfo);
//
//                //response from server "OK"
//                if(client.receiveMessageFromServer().equals("OK")){
//                    //send back "REDY"
//                    client.sendMessageToServer("REDY");
//                }else if(client.receiveMessageFromServer().equals("ERR")){
//                    System.out.println("ERROR: <"+scheduleInfo+"> invalid message recieved");
//                }
            }


        }
//        //go through dynamic job list print cores
//        for(DynamicJob i : dynamicJobList){
//            System.out.println(i.getCpuCores());
//        }

        if(temp.equals("NONE")) {
            client.sendMessageToServer("QUIT");
            if(client.receiveMessageFromServer().equals("QUIT")) {
                socket.close();
            }
        }
        socket.close();
    }
}
