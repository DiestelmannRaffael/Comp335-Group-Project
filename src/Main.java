import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
            String serverName = "127.0.0.1";
            int port = 8096;
            Socket socket = new Socket(serverName, port);
            JobSchedulerClient client = new JobSchedulerClient(socket);
            client.sendMessageToServer("HELO");

            if(client.receiveMessageFromServer().equals("OK")) {
                client.sendMessageToServer("AUTH comp335");
            }

            if(client.receiveMessageFromServer().equals("OK")) {
                client.sendMessageToServer("REDY");
            }
            String temp ="";

            int jobID = 0;
            String serverType = "large";
            int serverID = 0;

            while((temp = client.receiveMessageFromServer()).contains("JOBN")) {
                String scheduleInfo = "SCHD " + jobID + " " + serverType + " " + serverID;
                client.sendMessageToServer(scheduleInfo);
                if(client.receiveMessageFromServer().equals("OK")) {
                    client.sendMessageToServer("REDY");
                }
                jobID++;
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
