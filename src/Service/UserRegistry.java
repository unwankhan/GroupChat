package Service;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserRegistry {

    private final static ConcurrentHashMap<String, PrintWriter> userList = new ConcurrentHashMap<>();

    public void addUser(String username, PrintWriter writer) {
        userList.put(username, writer);
    }

    public boolean checkUser(String username) {
        return userList.containsKey(username);
    }
    public void removeUser(String username) {
        userList.remove(username);
    }

    public Set<String> getUsersList() {
        return userList.keySet();
    }

    public PrintWriter getWriter(String username) {
        return userList.get(username);
    }

    public Collection<PrintWriter> allWriters() {
        return userList.values();
    }
}
