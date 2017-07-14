import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.Properties;

public class MessageWorkerThread implements Runnable 
{     
    private String threadno;
	final private String from;
	final private String to;
	final private int id;
	final private String subject;
	final private String body;
	final private Statement st;
	private static int donecount;
	final private Transport transport;
    final private MimeMessage message; 
	
    public MessageWorkerThread(int threadno,int id,String from,String to,String subject,String body,
    							Connection cn,Transport transport,MimeMessage message,Statement st)
	{
        this.threadno=threadno+"";
        this.from=from;
		this.st=st;
        this.transport=transport;
        this.id=id;
        this.to=to;
        this.subject=subject;
        this.body=body;
        this.message=message;
    }
 
    @Override
    public void run() 
	{
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+threadno);
        
        /*  testing purpose
		
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.user", from);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "true");
        */
       

        try 
		{
            //setting the message headers and sending message
			//setting the common from address
			Address fromAddress=new InternetAddress(from);
			message.setFrom(fromAddress);
			//Set recipient
			Address[] toAddress=new Address[1];	
			toAddress[0]=new InternetAddress(to);
			message.setRecipient(Message.RecipientType.TO, toAddress[0]);
         
         	// Set Subject
         	message.setSubject(subject);
         	
         	// Now set the actual message
          	 message.setText(body);
          	   
			// Send message
          	message.saveChanges();
            transport.sendMessage(message, toAddress);//TIME TAKING
   
            System.out.println(donecount++);
                  
            //set sent_flag in database
             int rs=st.executeUpdate("update emailqueue set sent_flag=1 where id="+id);
               
            if(rs>0) System.out.println("record updated");
            else System.out.println("record failed");
            System.out.println(rs);
               
           //testing purpose : System.out.println("Sent all messages from this from successfully....");
          
		}            
        catch (MessagingException e) 
		{
             throw new RuntimeException(e);
        }

        catch(SQLException ex)
    	{
    		ex.printStackTrace();
    	}
        
        System.out.println(Thread.currentThread().getName()+" End.");
    }
 
    private void processCommand() 
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
        return this.threadno;
    }
}