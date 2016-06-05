import java.awt.event.*;

public class CheckersController
{
	private CheckersView theView;
	private CheckersModel theModel;

	
	public CheckersController(CheckersView view, CheckersModel model)
	{
		theModel = model;
		theView = view;
		theView.addPanelListener(new MouseHandler());
		theView.addNewGameListener(new NewGameListener());
		theView.addDontJumpListener(new DontJumpListener());
	}
	
	class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			int [] tab =  theView.checkClick(event.getX(), event.getY());
			if( tab != null)
				theModel.setCurrentValues(tab);
		
			if(theModel.getTurn() ==  CheckersModel.PLAYER)
				theView.setMessageText("PLAYER turn");
			else theView.setMessageText("OPPONENT turn");
			
			if ( theModel.getMultipleJumpFlag() )
				theView.dontJumpON();
			else theView.dontJumpOFF();
			
			theView.paintCheckers(theModel.getTableBoard());
			theView.highlightCurrent(theModel.getCurrent());
		}
	}
	
	class NewGameListener implements ActionListener
	{
		public void actionPerformed(ActionEvent  event)
		{
			theModel.newGame(CheckersModel.PLAYER);
			
			if(theModel.getTurn() ==  CheckersModel.PLAYER)
				theView.setMessageText("PLAYER starts");
			else theView.setMessageText("OPPONENT starts");
			
			theView.paintCheckers(theModel.getTableBoard());
		}
	}
	
	class DontJumpListener implements ActionListener
	{
		public void actionPerformed(ActionEvent  event)
		{
			if ( theModel.getMultipleJumpFlag() )
			{
				theModel.resetMultipleJumpFlag();
				
				theView.dontJumpOFF(); ///
				if(theModel.getTurn() ==  CheckersModel.PLAYER)
					theView.setMessageText("PLAYER turn");
				else theView.setMessageText("OPPONENT turn");
				
				theView.paintCheckers(theModel.getTableBoard());
			}
		}
	}
	
	class NetworkListener implements ActionListener
	{
		public void actionPerformed(ActionEvent  event)
		{
			//TODO
			NetworkController nc = new NetworkController();
		}
	}
	
}

