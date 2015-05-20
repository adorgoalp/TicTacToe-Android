package com.adorgolap.utils;


import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	private int utility = Integer.MIN_VALUE;

	private static final int SIZE = 3;
	private char[][] holder = new char[SIZE][SIZE];

	public Board() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.getHolder()[i][j] = 'E';
			}
		}
	}
	
	public Board(char[][] holder) {
		this.setHolder(holder);
	}

	public Board(Board b) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.getHolder()[i][j] = b.getHolder()[i][j];
			}
		}
	}

	public void setAIMove(int i, int j) {
		if (this.getHolder()[i][j] == 'E') {
			this.getHolder()[i][j] = 'x';
		} else {
//			System.out.println("Error!!!!");
		}
	}

	public boolean isTerminal() {
		if (isGoalFor('x')) {
			return true;
		}
		if (isGoalFor('o')) {
			return true;
		}
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (this.getHolder()[i][j] == 'E') {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String r = "";
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				r += getHolder()[i][j] + " ";
			}
			r += "\n";
		}
		return r;
	}

	private int getutilityValue() {
		if (isGoalFor('x')) {
			return 1;
		} else if (isGoalFor('o')) {
			return -1;
		}
		return 0;
	}

	public void setUtility(int utility) {
		this.utility = utility;
	}

	public int getUtility() {
		return utility;
	}


	private ArrayList<Board> getSuccessors(char c) {
		ArrayList<Board> t = new ArrayList<Board>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (this.getHolder()[i][j] == 'E') {
					char[][] holder = new char[SIZE][];
					for (int k = 0; k < SIZE; k++)
						holder[k] = Arrays.copyOf(this.getHolder()[k], SIZE);
					
					holder[i][j] = c;
					t.add(new Board(holder));
				}
			}
		}
		return t;
	}

	public void giveThatAlphaBetaPruningMove() {
		Board v = getMaxValue(Integer.MIN_VALUE, Integer.MAX_VALUE);
		this.giveMove(v);
	}

	public void giveMove(Board board) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.getHolder()[i][j] = board.getHolder()[i][j];
			}
		}
	}

	private Board getMaxValue(int alpha, int beta) {
//		System.out.println("IN MAX max called for");
		int maxUtility = Integer.MIN_VALUE;
		Board maxUtilityBoard = null;
		ArrayList<Board> childrens = getSuccessors('x');
		
//		assert(!childrens.isEmpty());
		
//		System.out.println("IN MAX c of max");
//		System.out.println(childrens);
		for(Board c : childrens)
		{
//			System.out.println("IN MAX lopping for");
			if(c.isTerminal())
			{
//				assert(c.getutilityValue() >= -1 && c.getutilityValue() <= 1);
//				System.out.println("IN MAX this is terminal");
				if(c.getutilityValue() > maxUtility)
				{
					maxUtility = c.getutilityValue();
					maxUtilityBoard = c;
//					System.out.println("IN MAX updated max utility " + maxUtility);
//					System.out.println("IN MAX Updated max  utility board ");
//					System.out.println(maxUtilityBoard);
				}else
				{
//					System.out.println("IN MAX utility value not updated");
				}
			}
			else
			{
//				System.out.println("IN MAX c is not terminal. calling min for this c");
				//maxUtility = Math.max(maxUtility, c.getMinValue(alpha, beta).utility);
				Board x = c.getMinValue(alpha, beta);
//				assert(x != null);
				
//				System.out.println("IN MAX min of board ");
//				System.out.println(c + " is");
//				System.out.println(x);
				int t = x.utility;
				
				if(maxUtility < t)
				{
					maxUtility = t;
					maxUtilityBoard = c;
				}
				if(maxUtility>beta)
				{
					maxUtilityBoard = c;
					maxUtilityBoard.utility = maxUtility;
					return maxUtilityBoard;
				}
				alpha = Math.max(alpha, maxUtility);
			}
//			System.out.println(this);
//			System.out.println("-->");
//			System.out.println(c);
		}
//		assert maxUtilityBoard != null : "Max utility is " + maxUtility;
		maxUtilityBoard.utility = maxUtility;
		return maxUtilityBoard;
	}

	private Board getMinValue(int alpha, int beta) {
//		System.out.println("IN MIN min called for");
//		System.out.println(this);
		int minUtility = Integer.MAX_VALUE;
		Board minUtilityBoard = null;
		ArrayList<Board> childrens = getSuccessors('o');
//		assert(!childrens.isEmpty());
//		System.out.println("IN MIN c of min");
//		System.out.println(childrens);
		for(Board c : childrens)
		{
//			System.out.println("IN MIN looping for");
//			System.out.println(c);
			if(c.isTerminal())
			{
//				assert(c.getutilityValue() >= -1 && c.getutilityValue() <= 1);
//				System.out.println("IN MIN this is terminal");
				if(c.getutilityValue() < minUtility)
				{
					minUtility = c.getutilityValue();
					minUtilityBoard = c;
//					System.out.println("IN MIN updated min utility " + minUtility);
//					System.out.println("IN MIN Updated min utility board ");
//					System.out.println(minUtilityBoard);
				}else
				{
//					System.out.println("IN MIN  utulity not upadted");
				}
			}
			else
			{
//				System.out.println("IN MIN it is not terminal. calling for max");
				//minUtility = Math.min(minUtility, c.getMaxValue(alpha, beta).utility);
				Board x = c.getMaxValue(alpha, beta);
				
//				System.out.println("IN MIN max of  board");
//				System.out.println(c +" is");
//				System.out.println(x);
				
				int t = x.utility;
				if(minUtility > t)
				{
					minUtility = t;
					minUtilityBoard = c;
				}
				if(minUtility < alpha)
				{
					minUtilityBoard = c;
					minUtilityBoard.utility = minUtility;
					return minUtilityBoard;
				}
				beta = Math.min(beta, minUtility);
			}
		}
		assert(minUtilityBoard != null);
		
		minUtilityBoard.utility = minUtility;
		return minUtilityBoard;
	}

	public boolean isGoalFor(char c) {
		for (int i = 0; i < SIZE; i++) {
			if (this.getHolder()[i][0] == this.getHolder()[i][1]
					&& this.getHolder()[i][1] == this.getHolder()[i][2]
					&& this.getHolder()[i][0] == c) {
				return true;
			}

		}
		for (int i = 0; i < SIZE; i++) {
			if (this.getHolder()[0][i] == this.getHolder()[1][i]
					&& this.getHolder()[1][i] == this.getHolder()[2][i]
					&& this.getHolder()[0][i] == c) {
				return true;
			}
		}
		if (this.getHolder()[0][0] == this.getHolder()[1][1]
				&& this.getHolder()[1][1] == this.getHolder()[2][2]
				&& this.getHolder()[1][1] == c) {

			return true;
		}
		if (this.getHolder()[0][2] == this.getHolder()[1][1]
				&& this.getHolder()[1][1] == this.getHolder()[2][0]
				&& this.getHolder()[1][1] == c) {

			return true;
		}
		return false;
	}

	public char[][] getHolder() {
		return holder;
	}

	public void setHolder(char[][] holder) {
		this.holder = holder;
	}

	public void giveUserMove(int position) {
		this.holder[position/SIZE][position%SIZE] = 'o';
	}

}
