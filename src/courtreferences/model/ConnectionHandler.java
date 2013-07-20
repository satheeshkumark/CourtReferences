package courtreferences.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ConnectionHandler {
	private static Connection conn = null;
	private static final String dbconfigfile = new String("src/courtreferences/resources/dbconfig.txt");
	
	public ConnectionHandler(){
	}
	
	public Connection getConnection()
	{
		if(ConnectionHandler.conn == null){
			Scanner sc = null;
			try{			
				sc = new Scanner(new FileReader(ConnectionHandler.dbconfigfile));	
				String hostname = sc.nextLine();
				String port 	= sc.nextLine();
				String dbname 	= sc.nextLine();
				String username = sc.nextLine();
				String password = sc.nextLine();
			
				String connstring = "jdbc:mysql" + "://" + hostname + ":" + port + "/" + dbname + 
						"?user=" + username + "&password=" + password;
			
				System.out.println(connstring);					
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				this.setConn(DriverManager.getConnection(connstring));		
				System.out.println(getConn() + " " +"connection opened");
			}	
			catch(IOException e){
				String error_msg = "DB config file doesnt exist";
				JOptionPane.showMessageDialog(null, "Check config file",error_msg, JOptionPane.ERROR_MESSAGE); 
				System.out.println("IO Exception " + e.getMessage());
			}
			catch(SQLException e){
				String error_msg = "Invalid DB settings";
				JOptionPane.showMessageDialog(null,error_msg,"Check DB Settings", JOptionPane.ERROR_MESSAGE);
				System.out.println("SQL Exception: " + e.getMessage());
			}
			catch(Exception e){
				System.out.println("Exception: " + e.getMessage());
			}
			finally{			
			}
			
		}
		return this.getConn();
	}
	
	public void closeConnection(){
		try	{
			if(this.getConn() == null)
				return;
			ConnectionHandler.conn.close();
			this.setConn(null);
		}   
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		ConnectionHandler.conn = conn;
	}
}