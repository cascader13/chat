package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class conn {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public static void Conn(int port) throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + String.valueOf(port) +".s3db");

        System.out.println("База Подключена!");
    }

    // --------Создание таблицы--------
    public static void CreateDB() throws ClassNotFoundException, SQLException
    {
        statmt = te
        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'IP' text);");
        statmt.execute("CREATE TABLE if not exists 'users_messages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'sender' int, 'receiver' int, 'message' text, 'data' text);");
        statmt.execute("CREATE TABLE if not exists 'chat_messages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'sender' int, 'message' text, 'data' text);");
        ResultSet res = statmt.executeQuery("SELECT ID from users WHERE name = 'Admin'");

        System.out.println("Таблица создана или уже существует.");
    }

    // --------Заполнение таблицы--------
    public static void new_user(String name, String IP) throws SQLException
    {
        statmt.execute("INSERT INTO 'users' ('name', 'IP') VALUES (?, ?)");
        System.out.println("Новый пользователь добавлен");
    }

//    public static void new_message(String name, String message, String data){
//        statmt.execute("INSERT INTO" )
//    }

    // -------- Вывод таблицы--------
    public static void ReadDB() throws ClassNotFoundException, SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM users");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String  name = resSet.getString("name");
            String  phone = resSet.getString("phone");
            System.out.println( "ID = " + id );
            System.out.println( "name = " + name );
            System.out.println( "phone = " + phone );
            System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    // --------Закрытие--------
    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }

}
