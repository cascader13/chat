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

    public  View(Client client){ this.client = client}

    
}
