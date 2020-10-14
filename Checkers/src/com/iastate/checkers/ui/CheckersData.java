package com.iastate.checkers.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class CheckersData {

	/*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

	static final int
	EMPTY = 0,
	RED = 1,
	RED_KING = 2,
	BLACK = 3,
	BLACK_KING = 4;


	int[][] board;  // board[r][c] is the contents of row r, column c.

	HashMap<String,Integer> capturedPieces;

	/**
	 * Constructor.  Create the board and set it up for a new game.
	 */
	CheckersData() {
		board = new int[8][8];
		capturedPieces = new HashMap<String,Integer>();
		capturedPieces.put("RED", 0);
		capturedPieces.put("RED_KING", 0);
		capturedPieces.put("BLACK", 0);
		capturedPieces.put("BLACK_KING", 0);

		setUpGame();
	}

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < board.length; i++) {
			int[] row = board[i];
			sb.append(8 - i).append(" ");
			for (int n : row) {
				if (n == 0) {
					sb.append(" ");
				} else if (n == 1) {
					sb.append(ANSI_RED + "R" + ANSI_RESET);
				} else if (n == 2) {
					sb.append(ANSI_RED + "K" + ANSI_RESET);
				} else if (n == 3) {
					sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
				} else if (n == 4) {
					sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
				}
				sb.append(" ");
			}
			sb.append(System.lineSeparator());
		}
		sb.append("  a b c d e f g h");

		return sb.toString();
	}

	/**
	 * Set up the board with checkers in position for the beginning
	 * of a game.  Note that checkers can only be found in squares
	 * that satisfy  row % 2 == col % 2.  At the start of the game,
	 * all such squares in the first three rows contain black squares
	 * and all such squares in the last three rows contain red squares.
	 */
	void setUpGame() {
		// Todo: setup the board with pieces BLACK, RED, and EMPTY

		for(int i = 0; i < 3; i++ ) {
			for(int j = 0; j < 8 ; j++) {
				if(i%2 == j%2) {
					board[i][j] = BLACK;
				}
			}
		}

		for(int i = 7; i > 4; i-- ) {
			for(int j = 0; j < 8 ; j++) {
				if(i%2 == j%2) {
					board[i][j] = RED;
				}
			}
		}
	}


	/**
	 * Return the contents of the square in the specified row and column.
	 */
	int pieceAt(int row, int col) {
		return board[row][col];
	}


	/**
	 * Make the specified move.  It is assumed that move
	 * is non-null and that the move it represents is legal.
	 * @return  true if the piece becomes a king, otherwise false
	 */
	boolean makeMove(CheckersMove move) {
		return makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
	}


	/**
	 * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
	 * assumed that this move is legal.  If the move is a jump, the
	 * jumped piece is removed from the board.  If a piece moves to
	 * the last row on the opponent's side of the board, the
	 * piece becomes a king.
	 *
	 * @param fromRow row index of the from square
	 * @param fromCol column index of the from square
	 * @param toRow   row index of the to square
	 * @param toCol   column index of the to square
	 * @return        true if the piece becomes a king, otherwise false
	 */
	boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
		// Todo: update the board for the given move.
		// You need to take care of the following situations:
		// 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
		// 2. if this move is a jump, remove the captured piece
		// 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king

		if(pieceAt(fromRow,fromCol) == RED || pieceAt(fromRow,fromCol) == RED_KING) {
			if(pieceAt(fromRow,fromCol) == RED_KING) {
				board[toRow][toCol] = RED_KING;
			}
			else {
				board[toRow][toCol] = RED;
			}
			board[fromRow][fromCol] = EMPTY;

			if(fromRow - toRow == 2 || fromRow - toRow == -2) {
				int [] capturedPiece = getCapturedPiece(fromRow, fromCol, toRow, toCol);
				board[capturedPiece[0]][capturedPiece[1]] = EMPTY;

				if(pieceAt(capturedPiece[0], capturedPiece[1] )== BLACK) {
					int blkCap = (int)capturedPieces.get("BLACK");
					capturedPieces.put("BLACK", blkCap++);
				}
				if(pieceAt(capturedPiece[0], capturedPiece[1] )== BLACK_KING) {
					int blkCap = (int)capturedPieces.get("BLACK_KING");
					capturedPieces.put("BLACK_KING", blkCap++);
				}

			}

			if(toRow == 0) {
				board[toRow][toCol] = RED_KING;
			}

		}
		else if(pieceAt(fromRow,fromCol) == BLACK || pieceAt(fromRow,fromCol) == BLACK_KING) {

			if(pieceAt(fromRow,fromCol) == BLACK_KING) {
				board[toRow][toCol] = BLACK_KING;
			}
			else {
				board[toRow][toCol] = BLACK;
			}
			board[fromRow][fromCol] = EMPTY;

			if(fromRow - toRow == 2 || fromRow - toRow == -2) {

				int [] capturedPiece = getCapturedPiece(fromRow, fromCol, toRow, toCol);
				board[capturedPiece[0]][capturedPiece[1]] = EMPTY;
				if(pieceAt(capturedPiece[0], capturedPiece[1] )== RED) {
					int blkCap = (int)capturedPieces.get("RED");
					capturedPieces.put("RED", blkCap++);

				}
				if(pieceAt(capturedPiece[0], capturedPiece[1] )== RED_KING) {
					int blkCap = (int)capturedPieces.get("RED_KING");
					capturedPieces.put("RED_KING", blkCap++);

				}

			}

			if(toRow == 7) {
				board[toRow][toCol] = BLACK_KING;
			}

		}


		return false;
	}

	/**
	 * Return an array containing all the legal CheckersMoves
	 * for the specified player on the current board.  If the player
	 * has no legal moves, null is returned.  The value of player
	 * should be one of the constants RED or BLACK; if not, null
	 * is returned.  If the returned value is non-null, it consists
	 * entirely of jump moves or entirely of regular moves, since
	 * if the player can jump, only jumps are legal moves.
	 *
	 * @param player color of the player, RED or BLACK
	 */
	CheckersMove[] getLegalMoves(int player) {
		// Todo: Implement your getLegalMoves here. 

		ArrayList<CheckersMove> checkersMovesList = new ArrayList<CheckersMove>();
		ArrayList<CheckersMove> checkersJumpsList = new ArrayList<CheckersMove>();

		boolean jumpsPresent = false;

		if(player == BLACK) {

			for(int i=0 ; i<8; i++) {
				for(int j=0; j<8; j++) {
					if(pieceAt(i,j) == BLACK) {

						CheckersMove[] jumpsPossible = getLegalJumpsFrom(BLACK, i, j);

						if(jumpsPossible != null && jumpsPossible.length > 0) {
							jumpsPresent = true;

							for(CheckersMove jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i<7 && j < 7 && pieceAt(i+1,j+1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i+1,j+1));
							}
							if(i<7 && j>0 && pieceAt(i+1,j-1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i+1,j-1));
							}
						}
					}
					else if(pieceAt(i,j) == BLACK_KING) {

						CheckersMove[] jumpsPossible = getLegalJumpsFrom(BLACK_KING, i, j);

						if(jumpsPossible != null && jumpsPossible.length > 0) {
							jumpsPresent = true;

							for(CheckersMove jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i<7 && j < 7 && pieceAt(i+1,j+1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i+1,j+1));
							}
							if(i<7 && j>0 && pieceAt(i+1,j-1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i+1,j-1));
							}
							if(i>0 && j < 7 && pieceAt(i-1,j+1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i-1,j+1));
							}
							if(i>0 && j>0 && pieceAt(i-1,j-1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i-1,j-1));
							}
						}

					}

				}
			}
		}
		else if (player == RED) {

			for(int i=0 ; i<8; i++) {
				for(int j=0; j<8; j++) {
					if(pieceAt(i,j) == RED) {

						CheckersMove[] jumpsPossible = getLegalJumpsFrom(RED, i, j);

						if(jumpsPossible != null && jumpsPossible.length > 0) {
							jumpsPresent = true;

							for(CheckersMove jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i>0 && j>0 && pieceAt(i-1,j-1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i-1,j-1));
							}
							if(i>0 && j<7 && pieceAt(i-1,j+1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i-1,j+1));
							}
						}
					}
					else if(pieceAt(i,j) == RED_KING) {

						CheckersMove[] jumpsPossible = getLegalJumpsFrom(RED_KING, i, j);

						if(jumpsPossible != null && jumpsPossible.length > 0) {
							jumpsPresent = true;

							for(CheckersMove jm : jumpsPossible) {
								checkersJumpsList.add(jm);
							}

						}

						if(!jumpsPresent){
							if(i>0 && j>0 && pieceAt(i-1,j-1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i-1,j-1));
							}
							if(i>0 && j<7 && pieceAt(i-1,j+1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i-1,j+1));
							}
							if(i<7 && j>0 && pieceAt(i+1,j-1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i+1,j-1));
							}
							if(i<7 && j<7 && pieceAt(i+1,j+1) == EMPTY) {
								checkersMovesList.add(new CheckersMove(i,j,i+1,j+1));
							}
						}
					}

				}
			}
		}


		if(jumpsPresent) {
			CheckersMove[] legalCheckersJumps = new CheckersMove[checkersJumpsList.size()];
			legalCheckersJumps = checkersJumpsList.toArray(legalCheckersJumps);

			if(legalCheckersJumps.length > 0) {
				return legalCheckersJumps;
			}
		}
		else {
			CheckersMove[] legalCheckersMoves = new CheckersMove[checkersMovesList.size()];
			legalCheckersMoves = checkersMovesList.toArray(legalCheckersMoves);

			if(legalCheckersMoves.length > 0) {
				return legalCheckersMoves;
			}
		}
		return null;
	}


	/**
	 * Return a list of the legal jumps that the specified player can
	 * make starting from the specified row and column.  If no such
	 * jumps are possible, null is returned.  The logic is similar
	 * to the logic of the getLegalMoves() method.
	 *
	 * @param player The player of the current jump, either RED or BLACK.
	 * @param row    row index of the start square.
	 * @param col    col index of the start square.
	 */
	CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
		// Todo: Implement your getLegalJumpsFrom here.

		ArrayList<CheckersMove> finalLegalJumps = new ArrayList<CheckersMove>();

		if(player == RED) {
			if(row > 1 && col < 6 && ( pieceAt(row-1, col+1) == BLACK || pieceAt(row-1, col+1) == BLACK_KING ) && pieceAt(row-2, col+2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row-2,col+2));

			}
			if(row > 1 && col > 1 && ( pieceAt(row-1, col-1) == BLACK || pieceAt(row-1, col-1) == BLACK_KING ) && pieceAt(row-2, col-2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row-2,col-2));

			}

			CheckersMove[] legalCheckersJumps = new CheckersMove[finalLegalJumps.size()];
			legalCheckersJumps = finalLegalJumps.toArray(legalCheckersJumps);

			if(legalCheckersJumps.length > 0) {
				return legalCheckersJumps;
			}
			return null;

		}
		else if(player == RED_KING) {
			if(row > 1 && col < 6 && (pieceAt(row-1, col+1) == BLACK || pieceAt(row-1, col+1) == BLACK_KING )&& pieceAt(row-2, col+2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row-2,col+2));

			}
			if(row > 1 && col > 1 && (pieceAt(row-1, col-1) == BLACK || pieceAt(row-1, col-1) == BLACK_KING )&& pieceAt(row-2, col-2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row-2,col-2));

			}
			if(row < 6 && col < 6 && (pieceAt(row+1, col+1) == BLACK || pieceAt(row+1, col+1) == BLACK_KING) && pieceAt(row+2, col+2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row+2,col+2));


			}
			if(row < 6 && col > 1 && (pieceAt(row+1, col-1) == BLACK || pieceAt(row+1, col-1) == BLACK_KING )&& pieceAt(row+2, col-2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row+2,col-2));

			}

			CheckersMove[] legalCheckersJumps = new CheckersMove[finalLegalJumps.size()];
			legalCheckersJumps = finalLegalJumps.toArray(legalCheckersJumps);

			if(legalCheckersJumps.length > 0) {
				return legalCheckersJumps;
			}
			return null;

		}
		else if (player == BLACK) {

			if(row < 6 && col < 6 && (pieceAt(row+1, col+1) == RED || pieceAt(row+1, col+1) == RED_KING) && pieceAt(row+2, col+2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row+2,col+2));


			}
			if(row < 6 && col > 1 && (pieceAt(row+1, col-1) == RED || pieceAt(row+1, col-1) == RED_KING )&& pieceAt(row+2, col-2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row+2,col-2));

			}


			CheckersMove[] legalCheckersJumps = new CheckersMove[finalLegalJumps.size()];
			legalCheckersJumps = finalLegalJumps.toArray(legalCheckersJumps);

			if(legalCheckersJumps.length > 0) {
				return legalCheckersJumps;
			}
			return null;

		}

		else if (player == BLACK_KING) {

			if(row < 6 && col < 6 && (pieceAt(row+1, col+1) == RED || pieceAt(row+1, col+1) == RED_KING)  && pieceAt(row+2, col+2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row+2,col+2));


			}
			if(row < 6 && col > 1 && (pieceAt(row+1, col-1) == RED || pieceAt(row+1, col-1) == RED_KING) && pieceAt(row+2, col-2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row+2,col-2));

			}
			if(row > 1 && col < 6 && ( pieceAt(row-1, col+1) == RED || pieceAt(row-1, col+1) == RED_KING ) && pieceAt(row-2, col+2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row-2,col+2));

			}
			if(row > 1 && col > 1 && ( pieceAt(row-1, col-1) == RED || pieceAt(row-1, col-1) == RED_KING ) && pieceAt(row-2, col-2) == EMPTY) {

				finalLegalJumps.add(new CheckersMove(row,col,row-2,col-2));

			}


			CheckersMove[] legalCheckersJumps = new CheckersMove[finalLegalJumps.size()];
			legalCheckersJumps = finalLegalJumps.toArray(legalCheckersJumps);

			if(legalCheckersJumps.length > 0) {
				return legalCheckersJumps;
			}
			return null;

		}
		return null;
	}



	public int[] getCapturedPiece(int fromRow, int fromCol, int toRow, int toCol) {

		if(toRow == fromRow+2 && toCol == fromCol+2) {
			return new int[] {fromRow+1,fromCol+1};
		}
		else if(toRow == fromRow+2 && toCol == fromCol-2) {
			return new int[] {fromRow+1,fromCol-1};
		}
		else if(toRow == fromRow-2 && toCol == fromCol+2) {
			return new int[] {fromRow-1,fromCol+1};
		}
		else if(toRow == fromRow-2 && toCol == fromCol-2) {
			return new int[] {fromRow-1,fromCol-1};
		}

		return new int[] {fromRow+1,fromCol+1};
	}

}