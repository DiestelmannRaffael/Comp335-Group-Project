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

        //parse the system.xml
        XmlReader xmlReader = new XmlReader();
        //set static server list to parsed list
        List<StaticServer> staticServers = xmlReader.getStaticServers();


        //create a new job list
        List<DynamicJob> dynamicJobList = new ArrayList<>();


        while((temp = client.receiveMessageFromServer()).contains("JOBN")) {
            String[] parts = temp.split(" ");

            //create new server list
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

            if(client.receiveMessageFromServer().contains("DATA")) {
                client.sendMessageToServer("OK");
            }

            //populate the dynamicServerList
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



                    //print the dynamicServerList
            System.out.println("DynamicServerList");
            for(DynamicServer current: dynamicServerList){
                System.out.println(current);
            }
            System.out.println("^^^");

            //get the first largest server index




            if(args[0].contains("-a") && args[1].contains("ff")){ // first fit

                System.out.println("ff");

            }else if (args[0].contains("-a") && args[1].contains("bf")){ // best fit

                System.out.println("bf");

            }else if (args[0].contains("-a") && args[1].contains("wf")) { // worst fit

                System.out.println("wf");

                System.out.println("StaticServers");
                for (StaticServer current : staticServers) {
                    System.out.println(current);
                }
                System.out.println("^^^");

                int worstFit = -1;
                int altFit = -1;
                int worstFitAct = -1;

                DynamicServer wf = null;
                DynamicServer af = null;
                DynamicServer wfact = null;

                for (StaticServer currentStatic : staticServers) {// for each server in staticServer list
                    for (DynamicServer currentDynamic : dynamicServerList) {//for each server in dynamicServer list
                        if (currentDynamic.getServerType().equals(currentStatic.getType())) {//

//                            System.out.println(currentDynamic);//print the current statistics

                            if(currentDynamic.getCpuCores() >= cpuCores && currentDynamic.getMemory() >= memory && currentDynamic.getDisk() >= disk){

                                System.out.println("current Dyanmic: "+currentDynamic);
                                System.out.println("sutible server ^");
                                int fitness = currentDynamic.getCpuCores() - cpuCores;

                                if(fitness > worstFit && (currentDynamic.getServerState() == 3 || currentDynamic.getServerState() == 2) ){
                                    worstFit = fitness;
                                    wf = currentDynamic;
                                }else if(fitness > altFit &&
                                        currentDynamic.getServerState() == 0){
                                    altFit = fitness;
                                    af = currentDynamic;
                                }
//
                            }

                            if(currentStatic.getCoreCount() >= cpuCores && currentStatic.getMemory() >= memory && currentStatic.getDiskSpace() >= disk){
                                System.out.println("current static initial: id: "+currentDynamic.getServerTypeId()+" Core: "+currentStatic.getCoreCount()+" memory:"+currentStatic.getMemory()+" disk:"+currentStatic.getDiskSpace());
                                int worstRunFitness = currentStatic.getCoreCount() - cpuCores;
                                if(worstRunFitness > worstFitAct && currentDynamic.getServerState() == 3){
                                    worstFitAct = worstRunFitness;
                                    wfact = currentDynamic;
                                }
                            }
//                            if(currentDynamic.getServerState() == 3){
//                                System.out.println("serverstate"+currentDynamic);
//                            }
//                            if(currentStatic.getCoreCount() >= cpuCores && currentStatic.getMemory() >= memory && currentStatic.getDiskSpace() >= disk && currentDynamic.getServerState() == 3){
//                                int fitness = currentStatic.getCoreCount() - cpuCores;
//                                if(fitness > worstFit && currentDynamic.getAvailableTime() >= submitTime ){
//                                    wfact = currentDynamic;
//                                }
//                            }
                        }
                    }
                }

                //Schedule the job

                String scheduleInfo = null;

                if(worstFit > -1){
                    System.out.println("wf: "+wf);
                    scheduleInfo = "SCHD "+jobId+" "+wf.getServerType()+" "+wf.getServerTypeId();
                }else if (altFit > -1){
                    System.out.println("af: "+af);
                    scheduleInfo = "SCHD "+jobId+" "+af.getServerType()+" "+af.getServerTypeId();
                }else{
                    System.out.println("wrf: "+wfact);
                    scheduleInfo = "SCHD "+jobId+" "+wfact.getServerType()+" "+wfact.getServerTypeId();

                }

                client.sendMessageToServer(scheduleInfo);
                //response from server "OK"
                if (client.receiveMessageFromServer().equals("OK")) {
                    //send back "REDY"
                    client.sendMessageToServer("REDY");
                } else if (client.receiveMessageFromServer().equals("ERR")) {
                    System.out.println("ERROR: <" + scheduleInfo + "> invalid message recieved");
                }

//                Schedule the job
//                String scheduleInfo = "SCHD 0 medium 0";
//                client.sendMessageToServer(scheduleInfo);
//
//                //response from server "OK"
//                if (client.receiveMessageFromServer().equals("OK")) {
//                    //send back "REDY"
//                    client.sendMessageToServer("REDY");
//                } else if (client.receiveMessageFromServer().equals("ERR")) {
//                    System.out.println("ERROR: <" + scheduleInfo + "> invalid message recieved");
//                }
            }

//            int serverIndex = 0;
//            int maxCoreCounter = 0;
//            for(int i = 0; i < dynamicServerList.size(); i++){
//                int hold;
//                if((hold = dynamicServerList.get(i).getCpuCores()) > maxCoreCounter){
//                    maxCoreCounter = hold;
//                    serverIndex = i;
//                }
//            }

//            //Schedule the job
//            String scheduleInfo = "SCHD " + jobId + " " +
//                    dynamicServerList.get(serverIndex).getServerType() + " " + dynamicServerList.get(serverIndex).getServerTypeId();
//            client.sendMessageToServer(scheduleInfo);
//
//            //response from server "OK"
//            if(client.receiveMessageFromServer().equals("OK")){
//                //send back "REDY"
//                client.sendMessageToServer("REDY");
//            }else if(client.receiveMessageFromServer().equals("ERR")){
//                System.out.println("ERROR: <"+scheduleInfo+"> invalid message recieved");
//            }
        }
        if(temp.equals("NONE")) {
            client.sendMessageToServer("QUIT");
            if(client.receiveMessageFromServer().equals("QUIT")) {
                socket.close();
            }
        }
        socket.close();
    }
}