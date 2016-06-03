/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants { 
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog(); //Returns the dialog used for user interaction
		nPlayers = dialog.readInt("Enter number of players"); 
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i); 
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames); 
		playGame();                                              
	}

	private void playGame() {
		//to avoid re-adding score to already added cell
		initializeScore();
		
		//number of rounds - one for each category 
		for(int i = 0; i < N_SCORING_CATEGORIES; i++) {
			// one turn per round, for each player
			for (int j = 1; j <= nPlayers; j++) {
				turn(j);
			}
		}
		//calculate and print the total score
		for (int k = 1; k <= nPlayers; k++) {
			totalScore(k);
		}
		display.printMessage("Congratulations, " + playerNames[winner()] + ", you are the winner with a total score of " + score[TOTAL - 1][winner()] + "!");
	}
	
	// Each player gets a turn in each single round. A single turn consists of roll, second roll,
	//third roll and choosing a category.
	private void turn(int player){
		firstRoll(player);
		reroll();
		reroll();
		selectCategory(player);
	}
	
	// First roll resets all dice values and stores random number in dice array
	private void firstRoll(int player) {
		display.printMessage(playerNames[player - 1] + "'s turn! Click \"Roll Dice\" button to roll the dice.");
		display.waitForPlayerToClickRoll(player);
			for(int i = 0; i < N_DICE; i++) {
				dice[i] = rgen.nextInt(1, 6);
			}
		display.displayDice(dice); //Draws the pictures of the dice on the screen
	}
	
	/* Allows the player to select which dice to reroll by clicking on the dice with the mouse,
	 * and then store dice values in appropriate dice[] location
	 */
	private void reroll() {
		display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\".");
		display.waitForPlayerToSelectDice();
			for(int i = 0; i < N_DICE; i++) {
				if (display.isDieSelected(i)) {
					dice[i] = rgen.nextInt(1, 6);

				}
			}
		display.displayDice(dice);
	}
	
	/* 
	 * the method waits for the player to click on one of the categories and returns the index of the category, 
	 * which will be one of the constants defined in YahtzeeConstants.
	 */
	private void selectCategory(int player) {
		display.printMessage("Select a category for this roll.");
		int category = display.waitForPlayerToSelectCategory();	
		while (score[category - 1][player - 1] != -1) {
			display.printMessage("Choose a different category");
			category = display.waitForPlayerToSelectCategory();
		}
		if (YahtzeeMagicStub.checkCategory(dice, category)) { // tests to see whether an array of dice values matches a particular category
				switch (category) {
					case ONES: //ones
						display.updateScorecard(category, player, firstSixCategoryScore(category)); 
						score[category - 1][player - 1] = firstSixCategoryScore(category);
						break;
					case TWOS: //twos
						display.updateScorecard(category, player, firstSixCategoryScore(category)); 
						score[category - 1][player - 1] = firstSixCategoryScore(category);
						break;
					case THREES: //threes
						display.updateScorecard(category, player, firstSixCategoryScore(category)); 
						score[category - 1][player - 1] = firstSixCategoryScore(category);
						break;
					case FOURS: //fours
						display.updateScorecard(category, player, firstSixCategoryScore(category)); 
						score[category - 1][player - 1] = firstSixCategoryScore(category);
						break;
					case FIVES: //fives
						display.updateScorecard(category, player, firstSixCategoryScore(category)); 
						score[category - 1][player - 1] = firstSixCategoryScore(category);
						break;
					case SIXES: //sixes
						display.updateScorecard(category, player, firstSixCategoryScore(category)); 
						score[category - 1][player - 1] = firstSixCategoryScore(category);
						break;
					case THREE_OF_A_KIND://three of a Kind
						display.updateScorecard(category, player, sumOfAllDices());
						score[category - 1][player - 1] = sumOfAllDices();
						break;
					case FOUR_OF_A_KIND://four of a Kind
						display.updateScorecard(category, player, sumOfAllDices());
						score[category - 1][player - 1] = sumOfAllDices();
						break;
					case CHANCE://chance
						display.updateScorecard(category, player, sumOfAllDices());
						score[category - 1][player - 1] = sumOfAllDices();
						break;
					case FULL_HOUSE ://Full House
						display.updateScorecard(category, player, FULL_HOUSE_SCORE);
						score[category - 1][player - 1] = FULL_HOUSE_SCORE;
						break; 	
					case SMALL_STRAIGHT://Small Straight
						display.updateScorecard(category, player, SMALL_STRAIGHT_SCORE);
						score[category - 1][player - 1] = SMALL_STRAIGHT_SCORE;
						break;
					case LARGE_STRAIGHT://Large Straight
						display.updateScorecard(category, player, LARGE_STRAIGHT_SCORE);
						score[category - 1][player - 1] = LARGE_STRAIGHT_SCORE;
						break;
					case YAHTZEE://Yahtzee
						display.updateScorecard(category, player, YAHTZEE_SCORE);
						score[category - 1][player - 1] = YAHTZEE_SCORE;
						break;
				}
		} else {
				display.updateScorecard(category, player, WRONG_CATEGORY_SCORE);
				score[category - 1][player - 1] = WRONG_CATEGORY_SCORE;
				}
		//Update total score
		display.updateScorecard(TOTAL, player, categoryScore(player));
	}
	
	//Counts all instances of a category, for category 1-6

	private int firstSixCategoryScore(int category) {
		int x = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == category) x += category;		
		}
		return(x);
	}
	
	//this method counts all values in dice array
	private int sumOfAllDices() {
		int total = 0;
		for (int i = 0; i < N_DICE; i++) {
			total = total + dice[i];
		}
		return(total);
	}
	
	//Total score including upper, lower and bonus
	private void totalScore(int player) {
		int bonus = 0;
		if (upperScore(player) >= LIMIT_FOR_UPPERSCORE_SCORE) bonus = BONUS_SCORE;
		int total = upperScore(player) + lowerScore(player) + bonus;
		score[TOTAL -1][player - 1] = total;
		display.updateScorecard(UPPER_SCORE, player, upperScore(player)); 	// upper score
		display.updateScorecard(UPPER_BONUS, player, bonus);			// upper bonus
		display.updateScorecard(LOWER_SCORE, player, lowerScore(player));	// lower score
		display.updateScorecard(TOTAL, player, total);				// total score
	}
	
	//Total sum of all category scores
	private int categoryScore(int player) {
		int total = 0;
		for(int i = 0; i < N_CATEGORIES; i++) {
			if(score[i][player - 1] != -1) total += score[i][player - 1];
		}
		return(total);
	}
	
	//Sum of categories between One and Six 
	private int upperScore(int player) {
		int total = 0;
			for(int i = ONES - 1; i < SIXES; i++) {
				total += score[i][player - 1];
			}
			return(total);
	}
	
	//Sum of categories between Three of a kind and Chance 
	private int lowerScore(int player) {
		int total = 0;
			for(int i = THREE_OF_A_KIND - 1; i < CHANCE; i++) {
				total += score[i][player - 1];
			}
			return(total);
	}
	
	//Find the player with max score and skip if single player
	private int winner() {
		int max = 0;
		int winner = 0;
		if(nPlayers != 1) {
			for(int i = 0; i < MAX_PLAYERS; i++) {
				if (score[TOTAL - 1][i] > max) {
					max = score[TOTAL -1][i];
					winner = i;
				}
			}
		}
		return(winner);
	}
	
	/* In order to make sure user can't enter score in already filled cells and
	 * since int gets automatically initilized with 0, in this case we chose -1, as a type of sentinel value
	 */
	private void initializeScore() {
		for(int i = 0; i < N_CATEGORIES; i++) {
			for(int j = 0; j < MAX_PLAYERS; j++)
				score[i][j] = -1;
		}
	}
	

		
/* Private instance variables */

	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	
	private int[][] score = new int[N_CATEGORIES][MAX_PLAYERS]; //we keep track of this array because we can put the score only one time
	                                                            //at first values are -1
	private int[] dice = new int[N_DICE];
}
