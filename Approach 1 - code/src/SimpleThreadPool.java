import java.sql.Connection;
import java.sql.SQLException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import java.sql.Statement;

public class SimpleThreadPool 
{
	private static int id[];
	private static String fromAddress[];
	private static String password[];
	private static String toAddress[];
	private static String subject[];
	private static String body[];	
	private static Connection cn;
	private static int records;
	private static Statement st;

    public static void main(String[] args) 
	{
		id=new int[300];
		fromAddress=new String[300];
		password=new String[300];
		toAddress=new String[300];
		subject=new String[300];
		body=new String[300];
	      
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
		AddressGrouping group=new AddressGrouping(id,fromAddress,password,toAddress,subject,body,cn);
		group.extractdata();
		records=group.getrecords();
		
		//A pool of threads is created for parallel execution wherein each threead is an instance of the worker thread class
        ExecutorService executor = Executors.newFixedThreadPool(records);
        for (int i = 0; i < records; i++) 
		{
        	//passing the class variables to the worker thread
        	//each worker thread is responsible for the delivery of a single message via a separate smtp connection
        	
            Runnable worker = new WorkerThread(i,id[i],fromAddress[i],password[i],toAddress[i],subject[i],body[i],st);
			executor.execute(worker);
        }
        
		executor.shutdown();
        while (!executor.isTerminated()) 
		{
        }
        System.out.println("Finished all threads");
    }
}