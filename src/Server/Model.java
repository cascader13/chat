package Server;

import Connection.Connection;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<String, Connection> allUsersMultiChat = new HashMap<>();


    protected Map<String, Connection> getAllUsersMultiChat() {
        return allUsersMultiChat;
    }

    protected void addUser(String nameUser, Connection connection) {
        allUsersMultiChat.put(nameUser, connection);
    }

    protected void removeUser(String nameUser) {
        allUsersMultiChat.remove(nameUser);
    }

}