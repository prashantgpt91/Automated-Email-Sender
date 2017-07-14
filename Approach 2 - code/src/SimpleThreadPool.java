import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
 
public class SimpleThreadPool 
{ 
	private static int id[];
	private static String fromAddress[];
	private static String password[];
	private static String toAddress[];
	private static String subject[];
	private static String body[];
	private static HashMap<String, ArrayList<Integer> > hm;
	private static Connection cn;
	private static Statement st;
	private static Transport transport[];
	private static MimeMessage message[];
	private static int records;
	
	public static void main(String[] args) 
	{
		id=new int[300];
		fromAddress=new String[300];
		password=new String[300];
		toAddress=new String[300];
		subject=new String[300];
		body=new String[300];
	    hm=new HashMap<String, ArrayList<Integer> >();  
		
		//from database connectivity  
		DBConnection.extractdata();
		cn=DBConnection.cn;
		try{st=cn.createStatement();} catch(SQLException ex){}
		
		/*  testing purpose
			System.out.printf("\n");
			for(int i=0;i<7;i++)
			System.out.println(fromAddress[i]+"\t"+password[i]+"\t"+toAddress[i]+"\t"+subject[i]+"\t"+body[i]);
		*/
		
		//Address Grouping
		AddressGrouping group=new AddressGrouping(id,fromAddress,password,toAddress,subject,body,hm,cn);
		group.extractdata();
		records=group.getrecords();
		
		//Connections are made first in parallel
		int size=hm.size(); 
		//A pool of threads is created for parallel execution wherein each threead is an instance of the worker thread class
		ExecutorService executor = Executors.newFixedThreadPool(size);
		transport =new Transport[size];
		message=new MimeMessage[size];
		
		Iterator<Map.Entry<String, ArrayList<Integer>>> iterator = hm.entrySet().iterator() ;
		
        for(int i=0;iterator.hasNext();i++)
		{
            Map.Entry<String, ArrayList<Integer>> entry = iterator.next();
            ArrayList<Integer> arr=entry.getValue();
            String from=entry.getKey();
            String pass=password[arr.get(0)];
            
			//Here the worker threads establish connections with the smtp server first before sending the messages
            Runnable worker = new WorkerThread(i,from,pass,transport,message);
        	//Runnable worker = new WorkerThread(i,"riya.sharma8389@gmail.com","sAchchit91","sachchit.bansal1@gmail.com","sdadsa","sdasdasd");
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) 
		{
        }
        System.out.println("Finished all threads for making connections");
      
        //Message are sent through the established connections
    	//A pool of threads is created for parallel execution wherein each threead is an instance of the message worker thread class
        ExecutorService executor2 = Executors.newFixedThreadPool(records);
        
        int arrsize;
        int threadno=0;
        
        Iterator<Map.Entry<String, ArrayList<Integer>>> iterator2 = hm.entrySet().iterator() ;
      
	  for(int i=0;iterator2.hasNext();i++)
	  {
            Map.Entry<String, ArrayList<Integer>> entry = iterator2.next();
            ArrayList<Integer> arr=entry.getValue();
            String from=entry.getKey();
            arrsize=arr.size();
            for(int j=0;j<arrsize;j++)
            {
            	int index=arr.get(j);
            	
            	//These message worker threads use the connections already made by the worker threads to send messages to the smtp server
                Runnable worker = new MessageWorkerThread(threadno++,id[index],from,toAddress[index],subject[index],body[index],
									cn,transport[i],message[i],st);
              	//Runnable worker = new WorkerThread(i,"riya.sharma8389@gmail.com","sAchchit91","sachchit.bansal1@gmail.com","sdadsa","sdasdasd");
              	
				executor2.execute(worker);
            }
       }

        executor2.shutdown();
        while (!executor2.isTerminated()) 
		{
        }
        System.out.println("Finished all threads for sending messages");
       
		try{cn.close();} catch(SQLException ex){}
    }
}