import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.sql.SQLException;
import java.sql.Statement;

//This is a custom authenticator function to set username and password used in the creating the session variable
class GMailAuthenticator extends Authenticator 
{
    String user;
    String pw;
    public GMailAuthenticator (String username, String password)
    {
       super();
       this.user = username;
       this.pw = password;
    }
	
	public PasswordAuthentication getPasswordAuthentication()
	{
	    return new PasswordAuthentication(user, pw);
	}
}

//This is the thread used in the thread pool that schedules all the threads in the pool on a timely basis
public class WorkerThread implements Runnable 
{     
    private String command;
    private int id;
	final private String from;
	final private String password;
	final private String to;	
	final private String subject;
	final private String body;
	private static int donecount;
	final private Statement st;
	
	//Constructor to initialize the class variables 
    public WorkerThread(int s,int id,String from,String password,String to,String subject,String body,Statement st){
        this.command=s+"";
        this.id=id;
        
        this.from=from;this.password=password;    
        this.to=to;
        this.subject=subject;
        this.body=body;
        this.st=st;
    }
 
    //this is the main method that is called when a thread is started by the thread pool
    @Override
    public void run() 
	{
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        
        /*testing purpose 
			String to1 = "executeiiitm@gmail.com";//change accordingly
			String to2="sachchitbansal@gmail.com";
			Sender's email ID needs to be mentioned
			String from = "riya.sharma8389@gmail.com";//change accordingly
			final String username = "riya.sharma8389@gmail.com";//change accordingly
			final String password = "sAchchit91";//change accordingly
		*/
        
        // Assuming you are sending email through gmail server
        String host = "smtp.gmail.com";

        //This sets the various features of the smtp connection that is established before sending the message (local machine only)
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");

        // Get the Session object.
        //repository for various properties and defaults. It doesn't actively send messages, 
        //it is simply read by the actual mail sending method. Therefore there is no "pile up".
        Session session = Session.getInstance(props, new GMailAuthenticator(from, password));
       
        //used to ensure that the message is sent to the smtp server and retry is made in case of denied connection after a proper delay
        boolean success=false;
        int tryno=0;
        
        while(success!=true)
        {
        	try 
			{
        		//success=true;
                 //setting the message headers
                 // Create a default MimeMessage object.
                 MimeMessage message = new MimeMessage(session);
                 // Set From and To: header field of the header.
                 Address fromAddress=new InternetAddress(from);
                 Address[] toAddress=new Address[1];
                 toAddress[0]=new InternetAddress(to);
                 //toAddress[1]=new InternetAddress("executeiiitm@gmail.com");
                 message.setFrom(fromAddress);
                 message.setRecipients(Message.RecipientType.TO, toAddress);
                 // Set Subject: header field
                 message.setSubject(subject);
                 // Now set the actual message
                 //Transport
                 Transport transport = session.getTransport("smtp");
                 transport.connect(host, from, password); //time taking
                 
                //System.out.println("**********************************************************************************************");

				//processCommand();
              
				//System.out.println("**************************************************************************************");
              
				// Send message
               	message.setText(body);
               	message.saveChanges();   
               	transport.sendMessage(message, toAddress);
                
               	//terminate the smtp connection in order to allow other threads to initialize theie connection to the smtp server since the 
               	//number of connections that can be made simultaneously are limited   
                transport.close();
                
                
                
                
				success=true;	
                System.out.println(  donecount++);
                
                //set sent_flag in database
                int rs=st.executeUpdate("update emailqueue set sent_flag=1 where id="+id);
                
                if(rs>0) System.out.println("record updated");
                else System.out.println("record failed");
                System.out.println(rs);
                

			} 
			catch (MessagingException e) 
            {
				//e.printStackTrace();
            	
				tryno++;
              	processCommand(tryno);
            }
        	catch(SQLException ex)
        	{
        		ex.printStackTrace();
        	}
        }
        
        System.out.println(Thread.currentThread().getName()+" End.");
    }
 
    private void processCommand(int tryno) 
	{
        try 
		{
            Thread.sleep(1000);
        } 
		catch (InterruptedException e) 
		{
            e.printStackTrace();
        }
    }
 
    @Override
    public String toString()
	{
        return this.command;
    }
}