package Database;
import java.sql.SQLException;

public class Database {
    public Database(int port) throws SQLException, ClassNotFoundException {
        conn.Conn(port);
        conn.CreateDB();
    }
}