import java.io.*;
import java.net.*;

public class ClientModel extends ServerModel 
{
	String host = "localhost";
    int port = 9999;
    
    public ClientModel()
    {
    	//TODO custom host and port
	     
	    try {
	    	System.out.println("Client will attempt connecting to server at host=" + host + " port=" + port + ".");
	    	skt = new Socket(host,port);
	    	
			// ok, got a connection.  
	    	myInput = new ObjectInputStream(skt.getInputStream());
	    	myOutput = new ObjectOutputStream(skt.getOutputStream());
			
			
			//skt.close();
		} catch (IOException  ex) {
			ex.printStackTrace();
		}
    }    
}

