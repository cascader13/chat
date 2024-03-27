package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
public class View {
    private final Client client;

    private JFrame frame = new JFrame("Чат");

    private JTextArea messages = new JTextArea(30, 20);

    private JTextArea users = new JTextArea(30, 15);

    private JPanel panel = new JPanel();

    private JTextField textField = new JTextField(40);

    private JButton buttonConnectDisconnect = new JButton("Подключиться");

    public  View(Client client){ this.client = client;}

    protected void initFrame(){
        messages.setEditable(false);
        users.setEditable(false);
        frame.add(new JScrollPane(messages), BorderLayout.CENTER);
        frame.add(new JScrollPane(users), BorderLayout.WEST);

        panel.add(textField);
        panel.add(buttonConnectDisconnect);
        frame.add(panel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                if (client.isConnect()){
                    client.disableClient();
                }
                System.exit(0);
            }
        });
        frame.setVisible(true);

        buttonConnectDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client.isConnect()){
                    client.disableClient();
                    buttonConnectDisconnect.setText("Подключиться");
                }
                else{
                    client.connectToServer();
                    buttonConnectDisconnect.setText("Отключиться");
                }
            }
        });

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessageOnServer(textField.getText());
                textField.setText("");
            }
        });
    }

    protected void addMessage(String text) {messages.append(text);}

    protected  void refreshUsersList(Set<String> UsersList) {
        users.setText("");
        if (client.isConnect()){
            StringBuilder text = new StringBuilder("Недавние чаты:\n");
            for (String user : UsersList) {
                text.append(user + "\n");
            }
            users.append(text.toString());
        }
    }

    protected String getServerAddressFromOptionPane() {
        while (true) {
            String addressServer = JOptionPane.showInputDialog(
                    frame, "Введите адрес сервера:",
                    "Ввод адреса сервера",
                    JOptionPane.QUESTION_MESSAGE
            );
            return addressServer.trim();
        }
    }

    protected int getPortServerFromOptionPane() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame, "Введите порт сервера:",
                    "Ввод порта сервера",
                    JOptionPane.QUESTION_MESSAGE
            );
            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame, "Введен неккоректный порт сервера. Попробуйте еще раз.",
                        "Ошибка ввода порта сервера", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    protected String getNameUser() {
        return JOptionPane.showInputDialog(
                frame, "Введите имя пользователя:",
                "Ввод имени пользователя",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    protected void errorDialogWindow(String text) {
        JOptionPane.showMessageDialog(
                frame, text,
                "Ошибка", JOptionPane.ERROR_MESSAGE
        );
    }
}

