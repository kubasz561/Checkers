
public class MVCmain {
    
	public static void main(String[] args)
	{
		CheckersModel theModel = new CheckersModel();
		
		CheckersView theView = new CheckersView();
	         
		CheckersController theController = new CheckersController(theView,theModel);
	}
}
