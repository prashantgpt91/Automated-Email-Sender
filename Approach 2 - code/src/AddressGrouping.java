import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ArrayList;

public class AddressGrouping 
{
	private String fromAddress[];
	private String password[];
	private String toAddress[];
	private String subject[];
	private String body[];
	private int id[];
	public Connection cn;
	private HashMap<String, ArrayList<Integer> > hm;
	private int records;
	
	public AddressGrouping(int id[],String fromAddress[],String password[],String toAddress[],String subject[],String body[],
			HashMap<String,ArrayList<Integer> > hm,Connection cn)
	{
		this.fromAddress=fromAddress;
		this.password=password;
		this.toAddress=toAddress;
		this.subject=subject;
		this.body=body;
		this.id=id;
		this.hm=hm;
		this.cn=cn;
	}
	
	//returns the total number of records in the database
	public int getrecords()
	{
		return records;
	}	
	
	public void extractdata ()
	{
		records = 0;
		try
		{
			//take result set from database
			Statement st = cn.createStatement();
			ResultSet rs=st.executeQuery("select * from emailqueue");
			System.out.println("selected");
			
			//Setting the result set line by line into the class variables
			int i=0;
			while(rs.next())
			{
			
				id[i]=rs.getInt(1);
				fromAddress[i]=rs.getString(2);
				password[i]=rs.getString(3);
				toAddress[i]=rs.getString(4);
				subject[i]=rs.getString(5);
				body[i]=rs.getString(6);
				i++;	
			}
			System.out.println("all records selected and inserted in arrays");
			records=i;
			rs.close();
		}
		
		catch(SQLException ex)
		{
			System.out.println("invalid sql:"+ex.getMessage());
		}
		catch(Exception ex)
		{
			System.out.println("error:"+ex.getMessage());
		}
		
		//Use of HashMap to find unique from addresses inorder to determine number of smtp connections 
		for(int i=0;i<records;i++)
		{			
			ArrayList<Integer> arr= hm.get(fromAddress[i]);
			if(arr==null) {arr=new ArrayList<Integer>();hm.put(fromAddress[i], arr);}
	        arr.add(i);
		}
		 
		System.out.println("HashMap built");
	}
}