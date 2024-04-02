package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;


public class View {
    private JFrame frame = new JFrame("Запуск сервера");
    private JTextArea dialogWindow = new JTextArea(10, 40);
    private JButton buttonStartStopServer = new JButton("Запустить сервер");
    private JPanel panelButtons = new JPanel();

    private JTextField Input = new JTextField(40);
    private final Server server;

    public View(Server server) {
        this.server = server;
    }

    //метод инициализации графического интерфейса приложения сервера
    protected void initFrameServer() {
        dialogWindow.setEditable(false);
        dialogWindow.setLineWrap(true);  //автоматический перенос строки в JTextArea
        frame.add(new JScrollPane(dialogWindow), BorderLayout.CENTER);
        panelButtons.add(Input);
        panelButtons.add(buttonStartStopServer);
        frame.add(panelButtons, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null); // при запуске отображает окно по центру экрана
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //класс обработки события при закрытии окна приложения Сервера
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.stop();
                System.exit(0);
            }
        });
        frame.setVisible(true);

        buttonStartStopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(buttonStartStopServer.getText(), "Запустить сервер")) {
                    int port = getPortFromOptionPane();
                    server.start(port);
                    buttonStartStopServer.setText("Остановить сервер");
                } else {
                    server.stop();
                    buttonStartStopServer.setText("Запустить сервер");
                }
            }
        });
        Input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comm = Input.getText().split(" ")[0];

                switch (comm){
                    case ("/help"):
                        server.console(Console_command.HElP, "");
                        break;
                    case ("/send"):
                        if (Input.getText().split(" ").length != 1){
                        server.console(Console_command.SEND, Input.getText().substring(5));
                        break;
                }
                }
            }
            });
        }


    //метод который добавляет в текстовое окно новое сообщение
    public void refreshDialogWindowServer(String serviceMessage) {
        dialogWindow.append(serviceMessage);
    }

    //метод вызывающий диалоговое окно для ввода порта сервера
    protected int getPortFromOptionPane() {
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
}