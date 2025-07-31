package Server;

import Service.CommandProcessor;
import Service.ConsoleUtils;
import Service.UserRegistry;
import Service.MessageRouter;
import Service.ClientHandler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
       int port;
       UserRegistry userRegistry;
       MessageRouter messageRouter;
       private final BufferedReader console=new BufferedReader(new InputStreamReader(System.in));
       private static volatile boolean running=true;


    public ChatServer(int port){
        this.port=port;
        this.userRegistry=new UserRegistry();
        this.messageRouter=new MessageRouter(this.userRegistry);
    }


    private void start() throws IOException {

      try{
          ServerSocket serverSocket = new ServerSocket(port);
          System.out.println("Server Listening on port "+port);
          new Thread(()-> {
              try {
                  while (running) {adminConsoleLoop();}
              } catch (IOException e) {
                  throw new RuntimeException(e);
              }
          }).start();


          while (running) {
              try {
                  Socket client = serverSocket.accept();
                  new Thread(new ClientHandler(client,userRegistry,messageRouter)).start();
              }
              catch (IOException e) {
                  e.printStackTrace();
                  throw new RuntimeException(e);
              }
          }

      } catch (Exception e) {
          throw new RuntimeException(e);
      }
    }


    private void adminConsoleLoop() throws IOException {
        String command= ConsoleUtils.readNonEmpty(console, """
                \n
                Enter your command
                
                /broadcast all <msg>
                /msg <username> <msg>
                /list
                /help
                /exit""");
        //parse: /broadcast, /msg, /list, /help, /exit
        String[] parse=CommandProcessor.parseMessage(command.trim());
        try {
          if (CommandProcessor.isPrivate(parse[0])) {
                messageRouter.sendPrivateMessage("[Server]: ", parse[1], parse[2],"server");
            }
          else if (CommandProcessor.isListCmd(parse[0])) {
              ConsoleUtils.writeLine(userRegistry.getUsersList());
            }
          else if (CommandProcessor.isHelpCmd(parse[0])) {
              ConsoleUtils.writeLine(CommandProcessor.helpText());
            }
          else if(CommandProcessor.isExitCmd(parse[0])){
              running=false;
              messageRouter.broadCast("[Server]: ","I'm Going to Shutting Down","server");
              System.exit(0);
          }
          else if(CommandProcessor.isBroadCast(parse[0])){
              messageRouter.broadCast("[Server]: ",parse[2],"server");
          }
          else{
              System.out.println("Unknown command,try again");
          }
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Invalid command");
        }
    }



    public static void main(String[] args) throws IOException {
         ChatServer server=new ChatServer(8010);
         server.start();

    }
}