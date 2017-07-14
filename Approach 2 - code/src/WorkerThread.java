import java.util.ArrayList;
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
import java.sql.*;



class GMailAuthenticator extends Authenticator {
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

public class WorkerThread implements Runnable 
{     
    private int threadno;
    final private String from;
	final private String password;
	private Transport[] transport;
	private MimeMessage[] message;
	private static int donecount;
    
	public WorkerThread(int threadno,String from,String password,Transport[] transport,MimeMessage[] message)
	{
        this.threadno=threadno;
        this.from=from;
        this.password=password;
        this.message=message;
        this.transport=transport;
    }
 
    @Override
    public void run() 
	{
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+threadno);
        
		/*
		testing purpose
        String to1 = "executeiiitm@gmail.com";//change accordingly
        String to2="sachchitbansal@gmail.com";
        Sender's email ID needs to be mentioned
        String from = "riya.sharma8389@gmail.com";//change accordingly
        final String username = "riya.sharma8389@gmail.com";//change accordingly
        final String password = "sAchchit91";//change accordingly
		*/
		
        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

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
		boolean success=false;
		int tryno=0;
       
		while(success!=true)
		{
			try 
			{
				// Create a default MimeMessage object.
				message[threadno] = new MimeMessage(session);
				
				//Transport object from this session
				transport[threadno] = session.getTransport("smtp");
				transport[threadno].connect(host, from, password); //time taking step
			
				/*
				testing purpose
				System.out.println("*****************************SLEEP 1 Start****************************************************");
				processCommand();
				System.out.println("*****************************SLeep 1 End********************************************************");
				*/
			
				System.out.println("Established smtp for this from successfully....");
				success=true;	
				System.out.println(  donecount++);
			} 
			catch (MessagingException e) 
			{
				tryno++;
				processCommand(tryno);
				//throw new RuntimeException(e);
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
    private void processCommand2() 
	{
        try 
		{
            Thread.sleep(1);
        } 
		catch (InterruptedException e) 
		{
            e.printStackTrace();
        }
    }
 
    @Override
    public String toString()
	{
        return this.threadno+"";
    }
}