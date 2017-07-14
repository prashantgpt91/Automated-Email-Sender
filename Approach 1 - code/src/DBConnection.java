/*
Class to make the connection to the MYSQL database 
*/
import java.sql.*;

public class DBConnection 
{
	public static Connection cn;
	public static void extractdata ()
	{	
		try
		{
			//database name---->coupondunia    
			//username----->root
			//password---->""
			//driver used------>Connector/J available as a jar file
			Class.forName("com.mysql.jdbc.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coupondunia","root","");
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println("invalid driver");
		}
		catch(SQLException ex)
		{
			System.out.println("invalid sql:"+ex.getMessage());
		}
		catch(Exception ex)
		{
			System.out.println("error:"+ex.getMessage());
		}
	}
}