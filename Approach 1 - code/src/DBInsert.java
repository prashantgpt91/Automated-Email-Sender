import java.sql.*;
import java.util.Scanner;

public class DBInsert 
{
	private static String fromAddress[];
	private static String password[];
	private static String toAddress[];
	private static String subject[];
	private static String body[];
	
	public static void main2 (String []s)
	{
		
		fromAddress=new String[7];
		password=new String[7];
		toAddress=new String[7];
		subject=new String[7];
		body=new String[7];
		
		//values
		fromAddress[0]="riya.sharma8389@gmail.com";
		password[0]="sAchchit91";
		toAddress[0]="sachchit.bansal1@gmail.com";
		subject[0]="Hello";
		body[0]="How are you ???";
		
		fromAddress[1]="sachchit.bansal1@gmail.com";
		password[1]="sAchchit91";
		toAddress[1]="sachchit.bansal1@gmail.com";
		subject[1]="Hello";
		body[1]="How are you ???";

		fromAddress[2]="sachchit.bansal2@gmail.com";
		password[2]="sAchchit91";
		toAddress[2]="sachchit.bansal1@gmail.com";
		subject[2]="Hello";
		body[2]="How are you ???";
		
		fromAddress[3]="sachchit.bansal3@gmail.com";
		password[3]="sAchchit91";
		toAddress[3]="sachchit.bansal1@gmail.com";
		subject[3]="Hello";
		body[3]="How are you ???";
		
		fromAddress[4]="divesh21kumar@gmail.com";
		password[4]="7879009450";
		toAddress[4]="sachchit.bansal1@gmail.com";
		subject[4]="Hello";
		body[4]="How are you ???";
		
		fromAddress[5]="arsh.brar072@gmail.com";
		password[5]="deep#1990";
		toAddress[5]="sachchit.bansal1@gmail.com";
		subject[5]="Hello";
		body[5]="How are you ???";
		
		fromAddress[6]="cutelittle1992@gmail.com";
		password[6]="sAchchit91";
		toAddress[6]="sachchit.bansal1@gmail.com";
		subject[6]="Hello";
		body[6]="How are you ???";
		
		Scanner sc= new Scanner(System.in);
		try
		{
			int x;
			
			//database connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection cn= DriverManager.getConnection("jdbc:mysql://localhost:3306/coupondunia","root","");
			Statement st= cn.createStatement();
			
			for(int i=0;i<300;i++)
			{		
				int j=i%7;
				x= st.executeUpdate("insert into emailqueue(from_email_address,password,to_email_address,subject,body) values('"
					+fromAddress[j]+"','"+password[j]+"','"+toAddress[j]+"','"+subject[j]+"','"+body[j]+"')");
					 
				if(x>0 && i%20==0)
					 System.out.println("record inserted");
			}
			cn.close();
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