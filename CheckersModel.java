import java.util.*;
import java.awt.*;

public class CheckersModel 
{
	private int[][] board;
	private int currentCol;
	private int currentRow;
	private int currentTurn;
	private Point[] possibleMoves;
	private Point[] possibleJumps;
	private boolean multipleJumpFlag;
	public static final int EMPTY = 0;
	public static final int PLAYER = 1;
	public static final int PLAYER_KING = 2;
	public static final int OPPONENT = 3;
	public static final int OPPONENT_KING = 4;
	
	public CheckersModel()
	{
		currentCol = 0;
		currentRow = 0;
		multipleJumpFlag = false;
		board = new int[8][8];
		//initialize with EMPTY
		for(int i = 0; i < 8; ++i)
			for(int j = 0; j < 8; ++j)
				board[i][j] = EMPTY;
	}
	
	public boolean getMultipleJumpFlag()
	{
		return multipleJumpFlag;
	}
	public void resetMultipleJumpFlag()
	{
		//System.out.println("RESET");
		multipleJumpFlag = false;
		currentCol = -1;
		currentRow = -1;
		changeTurn();
	}
	public void newGame(int player)
	{
        for (int row = 0; row < 8; row++) 
        {
            for (int col = 0; col < 8; col++) 
            {
               if ( row % 2 == col % 2 ) 
               {
                  if (row < 3)
                     board[col][row] = PLAYER;
                  else if (row > 4)
                     board[col][row] = OPPONENT;
                  else
                     board[col][row] = EMPTY;
               }
               else 
               {
                  board[col][row] = EMPTY;
               }
            }
        }
        currentTurn = player;
	}
	
	public void setCurrentValues(int[] tab)
	{
		//System.out.println("before: "+currentCol+currentRow+tab[0] + tab[1]);
		if(multipleJumpFlag)
		{
			possibleJumps = getPossibleJumps(currentTurn,currentCol,currentRow);
			Point destination = new Point(tab[0],tab[1]);
			if(possibleJumps != null)
			{//System.out.println("possibleJumps");
				for(Point p :possibleJumps)
				{
					if(destination.equals(p))
					{
						multipleJumpFlag = makeJump(currentCol,currentRow,(int)destination.getX(),(int)destination.getY()); 
						
					}
				}
			}
			if(!multipleJumpFlag)
			{
				currentCol = -1;//(int)destination.getX();
				currentRow = -1;//(int)destination.getY();
			}
			else 
			{
				currentCol = tab[0];//(int)destination.getX();
				currentRow = tab[1];
			}
			return;
		}
		
		//currentTurn+1 == King
		if (board[tab[0]][tab[1]] == currentTurn || board[tab[0]][tab[1]] == currentTurn + 1)
		{
			currentCol = tab[0];
			currentRow = tab[1];
			possibleMoves = getPossibleMoves(currentTurn);
			possibleJumps = getPossibleJumps(currentTurn,currentCol,currentRow);
		}
		else
		{
			if(currentCol != -1)
			{
				int skipMovesFlag = 0; //when jump is done don't check possible moves
				Point destination = new Point(tab[0],tab[1]);
				if(possibleJumps != null)
				{//System.out.println("possibleJumps");
					for(Point p :possibleJumps)
					{
						if(destination.equals(p))
						{
							multipleJumpFlag = makeJump(currentCol,currentRow,(int)destination.getX(),(int)destination.getY()); 
								
							skipMovesFlag = 1;
						}
					}
				}
				if(possibleMoves != null && skipMovesFlag == 0)
				{//System.out.println("possibleMoves");
					for(Point p :possibleMoves)
					{
						if(destination.equals(p))
							makeMove(currentCol,currentRow,(int)destination.getX(),(int)destination.getY());
					}
				}
			}
			
			if(!multipleJumpFlag)
			{
				currentCol = -1;//(int)destination.getX();
				currentRow = -1;//(int)destination.getY();
			}
			else 
			{
				currentCol = tab[0];//(int)destination.getX();
				currentRow = tab[1];
			}
		}
		
		
	}
	
	public int[][] getTableBoard()
	{
		return board;
	}
	
	public int getTurn()
	{
		return currentTurn;
	}
	
	public int[] getCurrent()
	{
		int[] current = new int[2];
		current[0] = currentCol;
		current[1] = currentRow;
		return current;
	}
	
//------PRIVATE-------------------------------------------------------------------------------------------------------
	
    private void makeMove(int fromCol, int fromRow, int toCol, int toRow) {
        board[toCol][toRow] = board[fromCol][fromRow];
        board[fromCol][fromRow] = EMPTY;
        // jump here
        if (toRow == 7 && board[toCol][toRow] == PLAYER)
           board[toCol][toRow] = PLAYER_KING;
        if (toRow == 0 && board[toCol][toRow] == OPPONENT)
           board[toCol][toRow] = OPPONENT_KING;
        
        changeTurn();
        
     }
    
    //returns true if multiple jump is possible
    private boolean makeJump(int fromCol, int fromRow, int toCol, int toRow)
    {
    	board[toCol][toRow] = board[fromCol][fromRow];
        board[fromCol][fromRow] = EMPTY;
    	// The move is a jump.  Remove the jumped piece from the board.
        int jumpRow = (fromRow + toRow) / 2;  // Row of the jumped piece.
        int jumpCol = (fromCol + toCol) / 2;  // Column of the jumped piece.
        board[jumpCol][jumpRow] = EMPTY;
        
        if (toRow == 7 && board[toCol][toRow] == PLAYER)
            board[toCol][toRow] = PLAYER_KING;
        if (toRow == 0 && board[toCol][toRow] == OPPONENT)
            board[toCol][toRow] = OPPONENT_KING;
        
        Point[] temp =  getPossibleJumps(currentTurn, toCol, toRow);
        if (temp != null)
        {
        	System.out.println("BIJ JESZzCzE");
        	return true;
        }
        else
        {
        	changeTurn();
        	return false;
       	}
    }
    
    private void changeTurn()
    {
    	if(currentTurn == PLAYER)
        	currentTurn = OPPONENT;
        else 
        	currentTurn = PLAYER;
    }
    
    private Point[] getPossibleMoves(int player) 
    {
        
        ArrayList<Point> moves = new ArrayList<Point>();  // Moves will be stoPLAYER in this list.
        
        int row = currentRow;
        int col = currentCol;
        if (canMove(player,row,col,row+1,col+1))
            moves.add(new Point(col+1, row+1));
        if (canMove(player,row,col,row-1,col+1))
            moves.add(new Point(col+1, row-1));
        if (canMove(player,row,col,row+1,col-1))
            moves.add(new Point(col-1, row+1));
        if (canMove(player,row,col,row-1,col-1))
            moves.add(new Point(col-1, row-1));
         
        
        if (moves.size() == 0)
           return null;
        else {
           Point[] moveArray = new Point[moves.size()];
           for (int i = 0; i < moves.size(); i++)
              moveArray[i] = moves.get(i);
           return moveArray;
        }
        
     }  
     
     
     /**
      * Return a list of the legal jumps that the specified player can
      * make starting from the specified row and column.  If no such
      * jumps are possible, null is returned.  The logic is similar
      * to the logic of the getLegalMoves() method.
      */
    private Point[] getPossibleJumps(int player,int col, int row) 
    {
    	ArrayList<Point> moves = new ArrayList<Point>();  // The legal jumps will be stoPLAYER in this list.

    	if (canJump(player, row, col, row+1, col+1, row+2, col+2))
    		moves.add(new Point( col+2, row+2));
    	if (canJump(player, row, col, row-1, col+1, row-2, col+2))
    		moves.add(new Point( col+2, row-2));
    	if (canJump(player, row, col, row+1, col-1, row+2, col-2))
    		moves.add(new Point( col-2, row+2));
    	if (canJump(player, row, col, row-1, col-1, row-2, col-2))
    		moves.add(new Point( col-2,row-2));
    
        
    	if (moves.size() == 0)
    		return null;
    	else 
    	{
    		Point[] moveArray = new Point[moves.size()];
    		for (int i = 0; i < moves.size(); i++)
    			moveArray[i] = moves.get(i);
    		return moveArray;
    	}
    }  
     
     
     /**
      * This is called by the two previous methods to check whether the
      * player can legally jump from (r1,c1) to (r3,c3).  It is assumed
      * that the player has a piece at (r1,c1), that (r3,c3) is a position
      * that is 2 rows and 2 columns distant from (r1,c1) and that 
      * (r2,c2) is the square between (r1,c1) and (r3,c3).
      */
     private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) 
     {
        
    	 if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
    		 return false;  // (r3,c3) is off the board.
        
    	 if (board[c3][r3] != EMPTY)
    		 return false;  // (r3,c3) already contains a piece.
        
    	 if (player == PLAYER) 
    	 {
    		 if (board[c1][r1] == PLAYER && r3 < r1)	//
    			 return false;  // Regular PLAYER piece can only move up.
    		 if (board[c2][r2] != OPPONENT && board[c2][r2] != OPPONENT_KING)
    			 return false;  // There is no OPPONENT piece to jump.
    		 return true;  // The jump is legal.
    	 }
    	 else if(player == OPPONENT)
    	 {
    		 if (board[c1][r1] == OPPONENT && r3 > r1)	//
    			 return false;  // Regular OPPONENT piece can only move down.
    		 if (board[c2][r2] != PLAYER && board[c2][r2] != PLAYER_KING)
    			 return false;  // There is no PLAYER piece to jump.
    		 return true;  // The jump is legal.
    	 }
    	 else if(player == PLAYER_KING)
    	 {
    		 if (board[c2][r2] != OPPONENT && board[c2][r2] != OPPONENT_KING)
    			 return false;  // There is no OPPONENT piece to jump.
    		 return true;  // The jump is legal.
    	 }
    	 else //if(player == OPPONENT_KING)
    	 {
    		 if (board[c2][r2] != PLAYER && board[c2][r2] != PLAYER_KING)
    			 return false;  // There is no PLAYER piece to jump.
    		 return true;  // The jump is legal.
    	 }
     }  
     
     
     /**
      * This is called by the getLegalMoves() method to determine whether
      * the player can legally move from (r1,c1) to (r2,c2).  It is
      * assumed that (r1,r2) contains one of the player's pieces and
      * that (r2,c2) is a neighboring square.
      */
     private boolean canMove(int player, int r1, int c1, int r2, int c2) 
     {
        
    	 if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
    		 return false;  // (r2,c2) is off the board.
        
    	 if (board[c2][r2] != EMPTY)
    		 return false;  // (r2,c2) already contains a piece.
        
    	 if (player == PLAYER) 
    	 {
    		 if (board[c1][r1] == PLAYER && r2 < r1)
    			 return false;  // Regular PLAYER piece can only move up.
    		 return true;  // The move is legal.
    	 }
    	 else if(player == OPPONENT)
    	 {
        	 if (board[c1][r1] == OPPONENT && r2 > r1)
        		  return false;  // Regular OPPONENT piece can only move down.
        	 return true;  // The move is legal.
    	 }
    	 else 
    		 return true; //KING can move everywhere  
     }  
}
