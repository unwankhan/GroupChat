package Service;

import java.io.PrintWriter;
import java.util.Collection;

public class MessageRouter {
    UserRegistry userRegistry ;
    public MessageRouter(UserRegistry userRegistry){
        this.userRegistry = userRegistry;
    }
    public void broadCast(String from, String msg,String username){
        Collection<PrintWriter> allUsers = userRegistry.allWriters();
        PrintWriter curr=userRegistry.getWriter(username);
        for (PrintWriter pw : allUsers){
            if(curr!=pw)
            pw.println(from+msg);
        }
    }

    public void sendPrivateMessage(String from, String to, String msg,String username){
        PrintWriter pw=userRegistry.getWriter(to);
        try {
            if(username.equalsIgnoreCase(to)){
               pw.println("You can't send a private message to yourself");
               return;
            }
            else
                pw.println("Received private message from " + from + msg);
        }
        catch (NullPointerException npe) {
            userRegistry.getWriter(to).println("User " + to + " not found.");
        }
    }
}
