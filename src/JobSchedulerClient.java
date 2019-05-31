import java.io.*;
import java.net.Socket;


class JobSchedulerClient {
    private DataOutputStream out;
    private DataInputStream in;

    JobSchedulerClient(Socket socket) throws IOException {
        OutputStream outToServer = socket.getOutputStream();
        out = new DataOutputStream(outToServer);
        InputStream inFromServer = socket.getInputStream();
        in = new DataInputStream(inFromServer);
    }

    void sendMessageToServer(String output) throws IOException {
        String message = output+"\n";
        byte[] temp = message.getBytes();
        out.write(temp);
        //System.out.println("sendMessageToServer: " + output);
    }

    String receiveMessageFromServer() throws IOException {
        int tempinput;
        StringBuilder response = new StringBuilder();
        while((tempinput = in.read()) != '\n'){ // 10 is ASCII code for newLine
            response.append((char) tempinput);
        }
        //System.out.println("receiveMessageFromServer: " + response);
        return response.toString();
    }
}
