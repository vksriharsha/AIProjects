package com.iastate.checkers.ui;

import java.util.ArrayList;

public class AlphaBetaSearch {
    private CheckersData board;

    // An instance of this class will be created in the Checkers.Board
    // It would be better to keep the default constructor.
    private static final int INFINITY = 99999999;
    private static final int NEG_INFINITY = -99999999;
    
    static final int
	EMPTY = 0,
	RED = 1,
	RED_KING = 2,
	BLACK = 3,
	BLACK_KING = 4;
    
    
    public void setCheckersData(CheckersData board) {
        this.board = board;
    }

    // Todo: You can implement your helper methods here
    
    
    int pieceAt(int row, int col, int[][] gameboard) {
		return gameboard[row][col];
	}
    
    boolean isJumpMove(CheckersMove m) {
    	return (m.fromRow - m.toRow == 2 || m.fromRow - m.toRow == -2);
    }
    
    
    public UtilityPair maxValue(int[][] gameboard, int alpha, int beta, int currentRecursionNum, int recursionNum) {
    	
    	if(currentRecursionNum == recursionNum) {
    		return getUtility(gameboard, BLACK);
    	}
    	
    	int v = NEG_INFINITY;
    	
    	UtilityPair pairToReturn = new UtilityPair(v,null);
    	
    	ArrayList<ArrayList<CheckersMove>> actions = getMovesFromGameBoard(BLACK, gameboard);
    	//System.out.println(actions);
    	
    	if(actions == null) {
    		return getUtility(gameboard, BLACK);
    	}
    	
    	for(ArrayList<CheckersMove> a : actions) {
    		
    		int[][] newgameboard = generateBoardAfterMove(a, gameboard);
    		UtilityPair minvals = minValue(newgameboard, alpha, beta, currentRecursionNum+1, recursionNum);
    		
    		if(minvals.getUtility() > v) {
    			v = minvals.getUtility();
    			pairToReturn.setUtility(v);
    			pairToReturn.setMove(a);
    			
    			if(v > alpha) {
    				alpha = v;
    			}
    			
    			if(v >= beta) {
    				return pairToReturn;
    			}
    		}
    	}
    	
    	return pairToReturn;
    	
    }
    
    
    public UtilityPair minValue(int[][] gameboard, int alpha, int beta, int currentRecursionNum, int recursionNum) {
    	
    	if(currentRecursionNum == recursionNum) {
    		return getUtility(gameboard, RED);
    	}
    	
    	int v = INFINITY;

    	UtilityPair pairToReturn = new UtilityPair(v,null);
    	
    	ArrayList<ArrayList<CheckersMove>> actions = getMovesFromGameBoard(RED, gameboard);
    	//System.out.println(actions);
    	
    	if(actions == null) {
    		return getUtility(gameboard, RED);
    	}
    	
    	for(ArrayList<CheckersMove> a : actions) {
    		
    		int[][] newgameboard = generateBoardAfterMove(a, gameboard);
    		
    		UtilityPair maxvals = maxValue(newgameboard, alpha, beta, currentRecursionNum+1, recursionNum);
    		
    		if(maxvals.getUtility() < v) {
    			v = maxvals.getUtility();
    			pairToReturn.setUtility(v);
    			pairToReturn.setMove(a);
    			
    			if(v < beta) {
    				beta = v;
    			}
    			
    			if(v <= alpha) {
    				return pairToReturn;
    			}
    		}
    		
    	}
    	
    	return pairToReturn;
    }
    
    
    public UtilityPair getUtility(int [][] gameboard, int player) {
    	
    	int heuristic_val = 0;
    	
    	for(int i=0; i<8; i++) {
    		for(int j=0; j<8; j++) {
    			if(pieceAt(i,j,gameboard) == RED_KING) {
    				heuristic_val -= 8;
    			}
    			else if(pieceAt(i,j,gameboard) == BLACK_KING) {
    				heuristic_val += 8;
    			}
    			else if(pieceAt(i,j,gameboard) == RED) {
    				if(i==1 || i==2) {
    					heuristic_val -= 4;
    				}
    				else if(i==3 || i==4) {
    					heuristic_val -= 3;
    				}
    				else if(i==5 || i==6 || i==7) {
    					heuristic_val -= 1;
    				}
    				
    			}
    			else if(pieceAt(i,j,gameboard) == BLACK) {
    				if(i==1 || i==2) {
    					heuristic_val += 4;
    				}
    				else if(i==3 || i==4) {
    					heuristic_val += 3;
    				}
    				else if(i==5 || i==6 || i==7) {
    					heuristic_val += 1;
    				}
    			}
    		}
    	}
    	
    	return new UtilityPair(heuristic_val, null);
    }
    
    
    
    public int[][] generateBoardAfterMove(ArrayList<CheckersMove> move, int[][] gameboard){
    	
    	int[][] newgameboard = new int[8][8];
    	
    	for(int i=0; i<8; i++) {
    		for(int j=0; j<8; j++) {
    			newgameboard[i][j] = gameboard[i][j];
    		}
    	}
    	
    	for(int i= move.size()-1; i >= 0; i--) {
    		CheckersMove cm = move.get(i);
    		
    		if(isJumpMove(cm)) {
    			int player = pieceAt(cm.fromRow, cm.fromCol, newgameboard);
    			newgameboard[cm.fromRow][cm.fromCol] = EMPTY;
    			newgameboard[cm.toRow][cm.toCol] = player;
    			
    			if(cm.fromRow - cm.toRow == -2 && cm.fromCol - cm.toCol == -2) {
    				newgameboard[cm.fromRow+1][cm.fromCol+1] = EMPTY;
    			}
    			else if(cm.fromRow - cm.toRow == 2 && cm.fromCol - cm.toCol == -2) {
    				newgameboard[cm.fromRow-1][cm.fromCol+1] = EMPTY;
    			}
    			else if(cm.fromRow - cm.toRow == -2 && cm.fromCol - cm.toCol == 2) {
    				newgameboard[cm.fromRow+1][cm.fromCol-1] = EMPTY;
    			}
    			else if(cm.fromRow - cm.toRow == 2 && cm.fromCol - cm.toCol == 2) {
    				newgameboard[cm.fromRow-1][cm.fromCol-1] = EMPTY;
    			}
    			
    			
    		}
    		else {
    			int player = pieceAt(cm.fromRow, cm.fromCol, newgameboard);
    			newgameboard[cm.fromRow][cm.fromCol] = EMPTY;
    			newgameboard[cm.toRow][cm.toCol] = player;
    		}
    		
    	}
    	
    	return newgameboard;
    }
    
    
    ArrayList<ArrayList<CheckersMove>> getMovesFromGameBoard(int player, int[][] gameboard) {
		// Todo: Implement your getLegalMoves here. 

    	ArrayList<ArrayList<CheckersMove>> checkersMovesList = new ArrayList<ArrayList<CheckersMove>>();
		ArrayList<ArrayList<CheckersMove>> checkersJumpsList = new ArrayList<ArrayList<CheckersMove>>();

		boolean jumpsPresent = false;

		if(player == BLACK) {

			for(int i=0 ; i<8; i++) {
				for(int j=0; j<8; j++) {
					if(gameboard[i][j] == BLACK) {

						ArrayList<ArrayList<CheckersMove>> jumpsPossible = getLegalJumpsFrom(BLACK, i, j, gameboard);

						if(jumpsPossible != null && jumpsPossible.size() > 0) {
							jumpsPresent = true;

							for(ArrayList<CheckersMove> jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i<7 && j < 7 && gameboard[i+1][j+1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i+1,j+1));
								checkersMovesList.add(rightMove);
							}
							if(i<7 && j>0 && gameboard[i+1][j-1] == EMPTY) {
								ArrayList<CheckersMove> leftMove = new ArrayList<CheckersMove>();
								leftMove.add(new CheckersMove(i,j,i+1,j-1));
								checkersMovesList.add(leftMove);
							}
						}
					}
					else if(gameboard[i][j] == BLACK_KING) {

						ArrayList<ArrayList<CheckersMove>> jumpsPossible = getLegalJumpsFrom(BLACK_KING, i, j, gameboard);

						if(jumpsPossible != null && jumpsPossible.size() > 0) {
							jumpsPresent = true;

							for(ArrayList<CheckersMove> jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i<7 && j < 7 && gameboard[i+1][j+1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i+1,j+1));
								checkersMovesList.add(rightMove);
							}
							if(i<7 && j>0 && gameboard[i+1][j-1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i+1,j-1));
								checkersMovesList.add(rightMove);
							}
							if(i>0 && j < 7 && gameboard[i-1][j+1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i-1,j+1));
								checkersMovesList.add(rightMove);
							}
							if(i>0 && j>0 && gameboard[i-1][j-1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i-1,j-1));
								checkersMovesList.add(rightMove);
							}
						}

					}

				}
			}
		}
		else if (player == RED) {

			for(int i=0 ; i<8; i++) {
				for(int j=0; j<8; j++) {
					if(gameboard[i][j] == RED) {

						ArrayList<ArrayList<CheckersMove>> jumpsPossible = getLegalJumpsFrom(RED, i, j, gameboard);

						if(jumpsPossible != null && jumpsPossible.size() > 0) {
							jumpsPresent = true;

							for(ArrayList<CheckersMove> jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i>0 && j>0 && gameboard[i-1][j-1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i-1,j-1));
								checkersMovesList.add(rightMove);
							}
							if(i>0 && j<7 && gameboard[i-1][j+1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i-1,j+1));
								checkersMovesList.add(rightMove);
							}
						}
					}
					else if(gameboard[i][j] == RED_KING) {

						ArrayList<ArrayList<CheckersMove>> jumpsPossible = getLegalJumpsFrom(RED_KING, i, j, gameboard);

						if(jumpsPossible != null && jumpsPossible.size() > 0) {
							jumpsPresent = true;

							for(ArrayList<CheckersMove> jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i>0 && j>0 && gameboard[i-1][j-1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i-1,j-1));
								checkersMovesList.add(rightMove);
							}
							if(i>0 && j<7 && gameboard[i-1][j+1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i-1,j+1));
								checkersMovesList.add(rightMove);
							}
							
							if(i<7 && j>0 && gameboard[i+1][j-1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i+1,j-1));
								checkersMovesList.add(rightMove);
							}
							if(i<7 && j<7 && gameboard[i+1][j+1] == EMPTY) {
								ArrayList<CheckersMove> rightMove = new ArrayList<CheckersMove>();
								rightMove.add(new CheckersMove(i,j,i+1,j+1));
								checkersMovesList.add(rightMove);
							}
						}
					}

				}
			}
		}


		if(jumpsPresent) {
			
			if(checkersJumpsList.size() > 0) {
				return checkersJumpsList;
			}
		}
		else {
			
			if(checkersMovesList.size() > 0) {
				return checkersMovesList;
			}
		}
		return null;
	}
    
    
    
    
    
	ArrayList<ArrayList<CheckersMove>> getLegalJumpsFrom(int player, int row, int col, int[][] gameboard) {
		// Todo: Implement your getLegalJumpsFrom here.

		ArrayList<ArrayList<CheckersMove>> finalLegalJumps = new ArrayList<ArrayList<CheckersMove>>();
		

		if(player == RED) {
			if(row > 1 && col < 6 && ( gameboard[row-1][col+1] == BLACK || gameboard[row-1][col+1] == BLACK_KING ) && gameboard[row-2][col+2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row-1][col+1] = EMPTY;
				gameboardAfterJump[row-2][col+2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row > 1 && col < 6) {
					recursiveJumpsList = getLegalJumpsFrom(player, row-2, col+2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row-2,col+2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row-2,col+2));
					finalLegalJumps.add(acm);
				}
				
			}
			if(row > 1 && col > 1 && ( gameboard[row-1][col-1] == BLACK || gameboard[row-1][col-1] == BLACK_KING ) && gameboard[row-2][col-2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row-1][col-1] = EMPTY;
				gameboardAfterJump[row-2][col-2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row > 1 && col > 1) {
				recursiveJumpsList = getLegalJumpsFrom(player, row-2, col-2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row-2,col-2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row-2,col-2));
					finalLegalJumps.add(acm);
				}
				
			}
			
			if(finalLegalJumps.size() > 0) {
				return finalLegalJumps;
			}
			return null;

		}
		else if(player == RED_KING) {
			if(row > 1 && col < 6 && (gameboard[row-1][col+1] == BLACK || gameboard[row-1][col+1] == BLACK_KING )&& gameboard[row-2][col+2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row-1][col+1] = EMPTY;
				gameboardAfterJump[row-2][col+2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row > 1 && col < 6) {
				recursiveJumpsList = getLegalJumpsFrom(player, row-2, col+2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row-2,col+2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row-2,col+2));
					finalLegalJumps.add(acm);
				}

			}
			if(row > 1 && col > 1 && (gameboard[row-1][col-1] == BLACK || gameboard[row-1][col-1] == BLACK_KING )&& gameboard[row-2][col-2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row-1][col-1] = EMPTY;
				gameboardAfterJump[row-2][col-2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row > 1 && col > 1) {
				recursiveJumpsList = getLegalJumpsFrom(player, row-2, col-2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row-2,col-2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row-2,col-2));
					finalLegalJumps.add(acm);
				}
				

			}
			if(row < 6 && col < 6 && (gameboard[row+1][col+1] == BLACK || gameboard[row+1][col+1] == BLACK_KING) && gameboard[row+2][col+2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row+1][col+1] = EMPTY;
				gameboardAfterJump[row+2][col+2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row < 6 && col < 6) {
				recursiveJumpsList = getLegalJumpsFrom(player, row+2, col+2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row+2,col+2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row+2,col+2));
					finalLegalJumps.add(acm);
				}


			}
			if(row < 6 && col > 1 && (gameboard[row+1][col-1] == BLACK || gameboard[row+1][col-1] == BLACK_KING )&& gameboard[row+2][col-2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row+1][col-1] = EMPTY;
				gameboardAfterJump[row+2][col-2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row < 6 && col > 1) {
				recursiveJumpsList = getLegalJumpsFrom(player, row+2, col-2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row+2,col-2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row+2,col-2));
					finalLegalJumps.add(acm);
				}

			}

			if(finalLegalJumps.size() > 0) {
				return finalLegalJumps;
			}
			return null;

		}
		else if (player == BLACK) {

			if(row < 6 && col < 6 && (gameboard[row+1][col+1] == RED || gameboard[row+1][col+1] == RED_KING) && gameboard[row+2][col+2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row+1][col+1] = EMPTY;
				gameboardAfterJump[row+2][col+2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row < 6 && col < 6 ) {
				recursiveJumpsList = getLegalJumpsFrom(player, row+2, col+2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row+2,col+2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row+2,col+2));
					finalLegalJumps.add(acm);
				}


			}
			if(row < 6 && col > 1 && (gameboard[row+1][col-1] == RED || gameboard[row+1][col-1] == RED_KING )&& gameboard[row+2][col-2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row+1][col-1] = EMPTY;
				gameboardAfterJump[row+2][col-2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row < 6 && col > 1 ) {
				recursiveJumpsList = getLegalJumpsFrom(player, row+2, col-2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row+2,col-2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row+2,col-2));
					finalLegalJumps.add(acm);
				}

			}


			if(finalLegalJumps.size() > 0) {
				return finalLegalJumps;
			}
			return null;

		}

		else if (player == BLACK_KING) {

			if(row < 6 && col < 6 && (gameboard[row+1][col+1] == RED || gameboard[row+1][col+1] == RED_KING)  && gameboard[row+2][col+2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row+1][col+1] = EMPTY;
				gameboardAfterJump[row+2][col+2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row < 6 && col < 6 ) {
				recursiveJumpsList = getLegalJumpsFrom(player, row+2, col+2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row+2,col+2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row+2,col+2));
					finalLegalJumps.add(acm);
				}

			}
			if(row < 6 && col > 1 && (gameboard[row+1][col-1] == RED || gameboard[row+1][col-1] == RED_KING) && gameboard[row+2][col-2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row+1][col-1] = EMPTY;
				gameboardAfterJump[row+2][col-2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row < 6 && col > 1 ) {
				recursiveJumpsList = getLegalJumpsFrom(player, row+2, col-2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row+2,col-2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row+2,col-2));
					finalLegalJumps.add(acm);
				}

			}
			if(row > 1 && col < 6 && ( gameboard[row-1][col+1] == RED || gameboard[row-1][col+1] == RED_KING ) && gameboard[row-2][col+2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row-1][col+1] = EMPTY;
				gameboardAfterJump[row-2][col+2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row > 1 && col < 6 ) {
				recursiveJumpsList = getLegalJumpsFrom(player, row-2, col+2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row-2,col+2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row-2,col+2));
					finalLegalJumps.add(acm);
				}

			}
			if(row > 1 && col > 1 && ( gameboard[row-1][col-1] == RED || gameboard[row-1][col-1] == RED_KING ) && gameboard[row-2][col-2] == EMPTY) {

				ArrayList<ArrayList<CheckersMove>> recursiveJumpsList = null;
				
				int[][] gameboardAfterJump = copyGameBoard(gameboard);
				
				gameboardAfterJump[row-1][col-1] = EMPTY;
				gameboardAfterJump[row-2][col-2] = player;
				gameboardAfterJump[row][col] = EMPTY;
				
				if(row > 1 && col > 1 ) {
				recursiveJumpsList = getLegalJumpsFrom(player, row-2, col-2, gameboardAfterJump);
				}
				if(recursiveJumpsList != null) {
				for(ArrayList<CheckersMove> acm : recursiveJumpsList) {
					acm.add(new CheckersMove(row,col,row-2,col-2));
					finalLegalJumps.add(acm);
				}
				}
				else {
					ArrayList<CheckersMove> acm = new ArrayList<CheckersMove>();
					acm.add(new CheckersMove(row,col,row-2,col-2));
					finalLegalJumps.add(acm);
				}

			}


			if(finalLegalJumps.size() > 0) {
				return finalLegalJumps;
			}
			return null;

		}
		return null;
	}
	
	
	
	public int[][] copyGameBoard(int[][] gameboard){
		
		int [][] newGameBoard = new int[8][8];
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				newGameBoard[i][j] = gameboard[i][j];
			}
		}
		
		return newGameBoard;
	}

    /**
     *  You need to implement the Alpha-Beta pruning algorithm here to
     * find the best move at current stage.
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        // The checker board state can be obtained from this.board,
        // which is a int 2D array. The numbers in the `board` are
        // defined as
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
        System.out.println(board);
        System.out.println();
        


        int alpha = NEG_INFINITY;
        int beta = INFINITY;
        
        UtilityPair output = maxValue(board.board, alpha, beta, 0, 8);
        
        System.out.println(output.getMove());
        
        ArrayList<CheckersMove> predictedMove = output.getMove();
        
        CheckersMove immediatePredictedMove = predictedMove.get(predictedMove.size()-1);
        
        for(CheckersMove cm: legalMoves) {
        	if(cm.fromRow == immediatePredictedMove.fromRow && cm.fromCol == immediatePredictedMove.fromCol && cm.toRow == immediatePredictedMove.toRow && cm.toCol == immediatePredictedMove.toCol) {
        		return cm;
        	}
        }
        
        // Here, we simply return the first legal move for demonstration.
        return legalMoves[0];
    }
    
    
    
   
}


class UtilityPair{
	
	int utility;
	ArrayList<CheckersMove> move;
	
	public UtilityPair(int utility, ArrayList<CheckersMove> move) {
		super();
		this.utility = utility;
		this.move = move;
	}

	public int getUtility() {
		return utility;
	}

	public void setUtility(int utility) {
		this.utility = utility;
	}

	public ArrayList<CheckersMove> getMove() {
		return move;
	}

	public void setMove(ArrayList<CheckersMove> move) {
		this.move = move;
	}
	
	
	
}
