package Client;

import Connection.*;
import java.io.IOException;
import java.net.Socket;
public class Client {

    private Connection connection;

    private static Model model;

    private static View gui;

    private volatile boolean isConnect = false;

    public boolean isConnect() {return  isConnect;}
    public void setConnect(boolean connect){isConnect = connect;}

    public static void main(String[] args) {
        Client client = new Client();
        model = new Model();
        gui = new View(client);
        gui.initFrame();
        while (true){
            if (client.isConnect){
                client.nameUserRegistration();
                client.receiveMessageFromServer();
                client.setConnect(false);
            }
        }
    }

    protected void connectToServer(){
        if(!isConnect) {
            while (true){
                try {
                    String adress = gui.getServerAddressFromOptionPane();
                    int port = gui.getPortServerFromOptionPane();

                    Socket socket = new Socket(adress, port);
                    connection = new Connection(socket);
                    isConnect = true;
                    gui.addMessage("Сервисное сообщение: Вы подключились к серверу.\n");
                    break;
                }catch (Exception e) {
                    gui.errorDialogWindow("Произошла Ошибка! Возможно вы ввели неверный фдрес сервера или порт");
                    break;
                }
            }
        }
    }

    protected void nameUserRegistration(){
        while (true){
            try {
                Message message = connection.receive();
                if (message.getType() == MessageType.REQUEST_NAME_USER) {
                    String nameUser = gui.getNameUser();
                    connection.send(new Message(MessageType.USER_NAME, nameUser));
                }

                if (message.getType() == MessageType.NAME_USED) {
                    gui.errorDialogWindow("Данное имя уже используется, введите другое");
                    String nameUser = gui.getNameUser();
                    connection.send(new Message(MessageType.USER_NAME, nameUser));
                }
                if (message.getType() == MessageType.NAME_ACCEPTED) {
                    gui.addMessage("Сервисное сообщение: ваше имя принято!\n");
                    model.setUsers(message.getListUsers());
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                gui.errorDialogWindow("Произошла ошибка при регистрации имени. Попробуйте переподключиться");
                try{
                    connection.close();
                    isConnect = false;
                    break;
                } catch (Exception g){
                    gui.errorDialogWindow("Ошибка при закрытии соединения");
                }
            }
        }
    }

    protected void sendMessageOnServer(String text) {
        try {
            connection.send(new Message(MessageType.TEXT_MESSAGE, text));
        } catch (Exception e) {
            gui.errorDialogWindow("Ошибка при отправки сообщения");
        }
    }

    protected void receiveMessageFromServer() {
        while (isConnect) {
            try {
                Message message = connection.receive();
                //если тип TEXT_MESSAGE, то добавляем текст сообщения в окно переписки
                if (message.getType() == MessageType.TEXT_MESSAGE) {
                    gui.addMessage(message.getTextMessage());
                }
                //если сообщение с типо USER_ADDED добавляем сообщение в окно переписки о новом пользователе
                if (message.getType() == MessageType.USER_ADDED) {
                    model.addUser(message.getTextMessage());
                    gui.refreshUsersList(model.getUsers());
                    gui.addMessage(String.format("Сервисное сообщение: пользователь %s присоединился к чату.\n", message.getTextMessage()));
                }
                //аналогично для отключения других пользователей
                if (message.getType() == MessageType.REMOVED_USER) {
                    model.removeUser(message.getTextMessage());
                    gui.refreshUsersList(model.getUsers());
                    gui.addMessage(String.format("Сервисное сообщение: пользователь %s покинул чат.\n", message.getTextMessage()));
                }
            } catch (Exception e) {
                gui.errorDialogWindow("Ошибка при приеме сообщения от сервера.");
                setConnect(false);
                gui.refreshUsersList(model.getUsers());
                break;
            }
        }
    }

    //метод реализующий отключение нашего клиента от чата
    protected void disableClient() {
        try {
            if (isConnect) {
                connection.send(new Message(MessageType.DISABLE_USER));
                model.getUsers().clear();
                isConnect = false;
                gui.refreshUsersList(model.getUsers());
            } else gui.errorDialogWindow("Вы уже отключены.");
        } catch (Exception e) {
            gui.errorDialogWindow("Сервисное сообщение: произошла ошибка при отключении.");
        }
    }

}
