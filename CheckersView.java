import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class CheckersView {
	JFrame frame;
	CheckersPanel panel;
	ButtonPanel buttonPanel;
	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 600;
	public static final int X_OFFSET = 10;
	public static final int Y_OFFSET = 5;
	
	public CheckersView()
	{
		frame = new JFrame();
		frame.setTitle("Checkers");
		frame.setLayout(new BorderLayout());
		
		panel = new CheckersPanel();
		buttonPanel = new ButtonPanel();
		frame.add(panel, BorderLayout.NORTH);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo( null );	//window on the center of the screen
		frame.setVisible(true);
	}
	
	
	public void addPanelListener(MouseAdapter listener)
	{
		panel.addMouseListener(listener);
	}
	public void addDontJumpListener(ActionListener e)
	{
		buttonPanel.addDontJumpListener(e);
	}
	public void addNewGameListener(ActionListener listener)
	{
		buttonPanel.addNewGameListener(listener);
	}
	public void addNetworkListener(ActionListener listener)
	{
		buttonPanel.addNetworkListener(listener);
	}
	public void setMessageText(String s)
	{
		buttonPanel.setMessageText(s);
	}
	public void dontJumpON()
	{
		buttonPanel.dontJumpON();
	}
	public void dontJumpOFF()
	{
		buttonPanel.dontJumpOFF();
	}
	
	public int[] checkClick(int x, int y)
	{
		if(x > X_OFFSET  && y > Y_OFFSET )
		{
			int col = (x - X_OFFSET ) / (DEFAULT_WIDTH/8);
	    	int row = (y - Y_OFFSET ) / (DEFAULT_HEIGHT/8);
	    	row = 7 - row;
	    	if (col >= 0 && col < 8 && row >= 0 && row < 8)
	    	{
	    		int[] tab = {col,row}; 
	    		return tab; 
	    	}	
		}
		return null;
	}
	
	public void paintCheckers(int[][] tab)
	{
		 for (int row = 0; row < 8; row++) 
	     {
	            for (int col = 0; col < 8; col++) 
	            {
	            	if (col % 2 == row % 2)
	            	panel.paintComponent(col,row,tab[col][row]);
	            }
	     }      
	}
	
	public void highlightCurrent(int[] current)
	{
		panel.paintHighlight(current);
	}
}


class CheckersPanel extends JPanel
{
	private JPanel chessBoard;
	private Image playerImg, opponentImg, playerKingImg, opponentKingImg;
	public CheckersPanel()
	{
		loadImages();
		Dimension boardSize = new Dimension(CheckersView.DEFAULT_WIDTH, CheckersView.DEFAULT_HEIGHT);
		 
		chessBoard = new JPanel();
		chessBoard.setLayout( new GridLayout(8, 8) );
		chessBoard.setPreferredSize( boardSize );
		chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);
		 
		for (int i = 0; i < 64; i++) 
		{
			//JPanel square = new JPanel( new BorderLayout() );
			ImagePanel square = new ImagePanel();
			chessBoard.add( square );
		 
			int row = (i / 8) % 2;
			if (row == 0)
				square.setBackground( i % 2 == 0 ? Color.DARK_GRAY : Color.WHITE );
			else
				square.setBackground( i % 2 == 0 ? Color.WHITE : Color.DARK_GRAY );
		}
		add(chessBoard);//, BorderLayout.CENTER);
   }
	
	public void loadImages()
	{
		try 
    	{                
	          playerImg = ImageIO.read(new File("red.gif"));
	          playerKingImg = ImageIO.read(new File("redKing.gif"));
	          opponentImg = ImageIO.read(new File("black.gif"));
	          opponentKingImg = ImageIO.read(new File("blackKing.gif"));
	    } 
    	catch (IOException ex) 
    	{
	            return;// handle exception...
	    }
	}
	
	public void paintComponent(int col, int row, int type) // only white   %2==%2
	{
		int n = (7-row)*8 + col;//(left down,x,y) to (left top, n) conversion
		ImagePanel squareToPaint = (ImagePanel)chessBoard.getComponent(n);
		
		//cleans image
			squareToPaint.setImage(null); 
		//cleans highlight
			squareToPaint.setBackground(Color.WHITE);
			
		if(type == 1)
		{
			squareToPaint.setImage(playerImg);
		}
		else if(type == 2)
		{
			squareToPaint.setImage(playerKingImg);
		}
		else if(type == 3)
		{
			squareToPaint.setImage(opponentImg);
		}
		else if(type == 4)
		{
			squareToPaint.setImage(opponentKingImg);
		}
		
	}
	
	
	public void paintHighlight(int[] current)
	{
		if (current[0] == -1) 
			return;
		int n = (7-current[1])*8 + current[0];//(left down,x,y) to (left top, n) conversion
		JPanel squareToPaint = (JPanel)chessBoard.getComponent(n);
		squareToPaint.setBackground(Color.YELLOW);
	}
	
	
	
		class ImagePanel extends JPanel
		{
	
		    private Image image;
	
		    public ImagePanel() { }
		    
		    public void setImage(Image img)
		    {
		    	if (img == null)
		    	{
		    		image = null;
		    	}
		    	else
		    	{
			    	image = img;
		    	}
		    	repaint();
		    }
		    
		    protected void paintComponent(Graphics g) 
		    {
		        super.paintComponent(g);
		        if(image != null)
		        	g.drawImage(image, 15, 15, null);            
		    }
		}
}

class ButtonPanel extends JPanel
{
	private JPanel buttonPanel;
	private JButton newGameButton;
	private JLabel message;
	private JButton dontJumpButton;

	private JButton networkButton;
	
	public ButtonPanel()
	{
		buttonPanel = new JPanel(new GridLayout(1,4));
		//buttonPanel.setPreferredSize(new Dimension(600,100));
		//buttonPanel.setBounds(0, 620, 600, 100);
		newGameButton = new JButton("New Game");
		message = new JLabel("Welcome!");
		networkButton = new JButton("Network");
		dontJumpButton = new JButton("Don't Jump");
		dontJumpOFF();
		
		buttonPanel.add(newGameButton);//, BorderLayout.WEST);
		buttonPanel.add(message);//, BorderLayout.EAST);
		buttonPanel.add(networkButton);
		buttonPanel.add(dontJumpButton);//, BorderLayout.CENTER);
		add(buttonPanel);
	}
	
	public void addNewGameListener(ActionListener e )
	{
		newGameButton.addActionListener(e);
	}
	
	public void setMessageText(String s)
	{
		message.setText(s);
	}
	
	public void addDontJumpListener(ActionListener e)
	{
		dontJumpButton.addActionListener(e);
	}
	public void addNetworkListener(ActionListener e)
	{
		networkButton.addActionListener(e);
	}
	public void dontJumpON()
	{
		dontJumpButton.setEnabled(true);
	}
	public void dontJumpOFF()
	{
		dontJumpButton.setEnabled(false);
	}
}