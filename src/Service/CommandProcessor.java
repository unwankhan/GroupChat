package Service;

public class CommandProcessor {


    public static boolean isPrivate(String command){

        return command.startsWith("/msg")||command.equalsIgnoreCase("/msg");
    }


    public static boolean isBroadCast(String command){
        return command.startsWith("/broadcast")||command.equalsIgnoreCase("/broadcast");
    }

    public static String[] parseMessage(String command){
        return command.split("\\s+", 3);
    }

    public static boolean isListCmd(String command){
        return command.equalsIgnoreCase("/list");//INCOM
    }

    public static boolean isHelpCmd(String command){
        return command.equalsIgnoreCase("/help");//incom
    }

    public static boolean isExitCmd(String command){
        return command.equalsIgnoreCase("/exit");
    }

    public static  String helpText() {
        return "/msg <user> <text>\n/list\n/help\n/exit";
    }
}
