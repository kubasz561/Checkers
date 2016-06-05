import java.io.*;
import java.net.*;

public class ServerModel 
{
	BoardToObject sendObj;
	BoardToObject recieveObj;
	Socket skt;
	ObjectInputStream myInput;
	ObjectOutputStream myOutput;
	
	public ServerModel()
	{
		try {
			// First we create a server socket and bind it to port 9999.
			ServerSocket myServerSocket = new ServerSocket(9999);
	
			// wait for an incoming connection... 
			System.out.println("Server is waiting for an incoming connection on host=" 
					+ InetAddress.getLocalHost().getCanonicalHostName() 
					+ " port=" + myServerSocket.getLocalPort());
			skt = myServerSocket.accept();
				
			// ok, got a connection.	
			myInput = new ObjectInputStream(skt.getInputStream());
			myOutput = new ObjectOutputStream(skt.getOutputStream());
			
			//skt.close();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void sendBoard(int[][] board)
	{
		sendObj = new BoardToObject(board);
		try
		{
			myOutput.writeObject(sendObj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveBoard()
	{
		try
		{
			recieveObj = (BoardToObject)myInput.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int[][] getBoard()
	{
		return recieveObj.getBoard();
	}
}
class BoardToObject implements Serializable
{
	private int[][] sendingArray;
	
	public BoardToObject(int[][] board)
	{
		sendingArray = board;
	}
	public void print()
	{
		System.out.println(sendingArray.toString());
	}
	public int[][] getBoard()
	{
		return sendingArray;
	}
}
