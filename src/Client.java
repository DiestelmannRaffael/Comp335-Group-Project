import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverName = "127.0.0.1";
        int port = 8096;

        try{
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            String message = "HELO";
            byte[] temp = message.getBytes();
            out.write(temp);
            //out.writeUTF("HELO");

            InputStream inFromServer = client.getInputStream();

            if(inFromServer != null){
                System.out.println("inFromServer is not null");
            }

            DataInputStream in = new DataInputStream(inFromServer);
            //byte[] ok = new byte[1024];
            //System.out.println("Server says " + in.read(ok));
            StringBuilder ok = new StringBuilder();
            do{
                ok.append(Integer.toString(in.read()));
                System.out.println(ok);
            } while(in.read()!= -1)

            System.out.println("Server says " + ok);

            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
