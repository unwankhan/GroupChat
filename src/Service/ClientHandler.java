package Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private UserRegistry userRegistry;
    private MessageRouter messageRouter;
    private String username;
    public ClientHandler(Socket clientSocket,UserRegistry userRegistry,MessageRouter messageRouter) throws IOException {
        this.clientSocket = clientSocket;
        this.userRegistry = userRegistry;
        this.messageRouter = messageRouter;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            promptUser(in,out);
            userRegistry.addUser(this.username,out);
            messageRouter.broadCast("[Server]: ",username+" join the chat",this.username);
            messageLoop(in);
            messageRouter.broadCast("[Server]: ",username+" left the chat",this.username);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void promptUser(BufferedReader in, PrintWriter out) throws IOException {
        while(this.username==null){
            out.println("Please enter your username:");
            String newUsername = in.readLine().trim();
            if(newUsername.isEmpty()){
                out.println("Name cannot be empty. Please try again.");
            }
            else if(userRegistry.checkUser(newUsername)){
                out.println("Username is already in use. Please try again.");
            }
            else{
                this.username = newUsername;
            }
        }
    }


    private void messageLoop(BufferedReader in) throws IOException {
            String msg;
            while((msg=in.readLine())!=null){
                String[] parse = CommandProcessor.parseMessage(msg);
                try {
                    if (CommandProcessor.isPrivate(parse[0])) {
                        messageRouter.sendPrivateMessage("["+this.username+"]: ", parse[1], parse[2],this.username);
                    }
                    else if (CommandProcessor.isListCmd(parse[0])) {
                        ConsoleUtils.writeLine(userRegistry.getUsersList());
                    }
                    else if (CommandProcessor.isHelpCmd(parse[0])) {
                        ConsoleUtils.writeLine(CommandProcessor.helpText());
                    }
                    else if (CommandProcessor.isExitCmd(parse[0])) {
                        this.clientSocket.close();
                        break;
                    }
                    else if(CommandProcessor.isBroadCast(parse[0])){
                        messageRouter.broadCast("["+this.username+"]: ",parse[2],this.username);
                    }
                    else{
                        System.out.println("Unknown command,try again");
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Invalid command");
                }
            }

    }


}
