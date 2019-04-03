import java.io.*;
import java.net.Socket;

public class JobSchedulerClient {
    private DataOutputStream out;
    private DataInputStream in;

    public JobSchedulerClient(Socket socket) throws IOException {
        OutputStream outToServer = socket.getOutputStream();
        out = new DataOutputStream(outToServer);
        InputStream inFromServer = socket.getInputStream();
        in = new DataInputStream(inFromServer);
    }

    public void sendMessageToServer(String output) throws IOException {
        String message = output+"\n";
        byte[] temp = message.getBytes();
        out.write(temp);
        System.out.println("sendMessageToServer: " + output);
    }

    public String receiveMessageFromServer() throws IOException {
        int tempinput;
        String response = "";
        while((tempinput = in.read()) != '\n'){ // 10 is ASCII code for newLine
            response = response+(char)tempinput;
        }
        System.out.println("receiveMessageFromServer: " + response);
        return response;
    }

//    public static void main(String[] args) {
//        String serverName = "127.0.0.1";
//        int port = 8096;
//
//        try{
//            System.out.println("Connecting to " + serverName + " on port " + port);
//            Socket client = new Socket(serverName, port);
//
//            System.out.println("Just connected to " + client.getRemoteSocketAddress());
//            OutputStream outToServer = client.getOutputStream();
//            DataOutputStream out = new DataOutputStream(outToServer);
//            InputStream inFromServer = client.getInputStream();
//            DataInputStream in = new DataInputStream(inFromServer);
//
//            sendMessageToServer
//
//
//
////            String newLine = "\n";
////            String message = "HELO"+newLine;
////            byte[] temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message+newLine);
////
////
////
////            if(inFromServer != null){
////                System.out.println("inFromServer is not null");
////            }
////
////
////            String response = "";
////
////
////
////            int tempinput;
////            while((tempinput = in.read()) != 10){
////                response = response+(char)tempinput;
////            }
////            System.out.printf(response);
////            response = "";
////
////
////            //send the auth response
////            message = "AUTH xxx"+newLine;
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message+newLine);
//
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the auth response
////            message = "AUTH xxx";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 83 0 1566 1 200 300
////            for(int i = 0; i < 24; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////
////            //job 0
////            message = "SCHD 0 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 290 1 7 1 400 1200
////            for(int i = 0; i < 23; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            System.out.printf("before job 1");
////            //job 1
////            message = "SCHD 1 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            System.out.printf("before ok");
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            System.out.printf("before ready");
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            System.out.printf("before recieve");
////            //revice job info   JOBN 336 2 96 2 2100 2800
////            for(int i = 0; i < 25; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            System.out.printf("before job 2");
////            //job 2
////            message = "SCHD 2 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 443 3 282 2 500 1100
////            for(int i = 0; i < 25; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //job 3
////            message = "SCHD 3 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 728 4 1431 8 8000 15400
////            for(int i = 0; i < 28; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //job 4
////            message = "SCHD 4 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 867 5 133 1 800 1400
////            for(int i = 0; i < 25; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //job 5
////            message = "SCHD 5 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 870 6 65 1 900 200
////            for(int i = 0; i < 23; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //job 6
////            message = "SCHD 6 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 954 7 741 1 1000 900
////            for(int i = 0; i < 25; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //job 7
////            message = "SCHD 7 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 1042 8 477 1 800 1700
////            for(int i = 0; i < 26; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //job 8
////            message = "SCHD 8 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //revice job info   JOBN 1064 9 47267 8 6300 4100
////            for(int i = 0; i < 29; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //job 9
////            message = "SCHD 9 large 0";
////            temp = message.getBytes();
////            out.write(temp);
////
////            //recieve the ok response
////            for(int i = 0; i < 2; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //send the redy response
////            message = "REDY";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            //recieve the none response
////            for(int i = 0; i < 4; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
////
////            //quit
////            message = "QUIT";
////            temp = message.getBytes();
////            out.write(temp);
////            System.out.println("message: "+message);
////
////            for(int i = 0; i < 4; i++){
////                response = response + (char)in.read();
////            }
////            System.out.println("response: "+response);
////            response = "";
//
//
//            client.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
