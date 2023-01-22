import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
//esta clase crea un objeto Connection y regresa el Statement
public class Conexion
{
    private String server, namebd, userbd, password;
    private Statement stmt;
    private String error;
    public Conexion(
        String server,
        String name,
        String user,
        String password)
    {
        this.server = server;
        namebd = name;
        userbd = user;
        this.password = password;
        try
        {
            conecta();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private void conecta() throws ClassNotFoundException
    {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try
        {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://" + server + ":1433;encrypt=true;databaseName=" + namebd + ";TrustServerCertificate=true;", userbd, password);
            stmt = con.createStatement();
            System.out.println("conectado correctamente");
        }
        catch (SQLException e)
        {
            error = e.getMessage();
        }
    }
    public Statement getStmt()
    {
        return stmt;
    }
    public String getError()
    {
        return error;
    }
}
