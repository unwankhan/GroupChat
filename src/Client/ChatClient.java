package Client;

import Service.CommandProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    int port;
    String host;

    public ChatClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    private void createClient() throws IOException {
        try(Socket clientSocket = new Socket(host, port)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));


            Thread reader=new Thread(()->{

                 try{
                     String message;
                     while((message=in.readLine())!=null){
                         System.out.println(message);
                     }
                 }
                 catch(IOException e){
                     e.printStackTrace();
                 }
            });
            reader.start();


            Thread writer=new Thread(()->{

                try{
                    String toServer;
                    while((toServer= console.readLine())!=null){
                        out.println(toServer);
                        if(CommandProcessor.isExitCmd(toServer)){break;}
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            });
            writer.start();
            while(writer.isAlive()&&reader.isAlive()) {try{Thread.sleep(50);} catch(InterruptedException ignore){}}

            clientSocket.close();
            reader.join();
            writer.join();


        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient client=new ChatClient(8010,"localhost");
        client.createClient();
    }
}
