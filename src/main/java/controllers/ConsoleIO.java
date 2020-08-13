package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleIO {

	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public static void printOutLn(String thingToPrint) {
		System.out.println(thingToPrint);
	}

	public static void printOut(String thingToPrint) {
		System.out.print(thingToPrint);
	}

	/**
	 * Generates a prompt that allows the user to enter any response and returns the String.
	 * This method does not accept null, empty, or whitespace-only inputs.
	 * @param prompt - the prompt to be displayed to the user.
	 * @return the input from the user as a String
	 */

	public static String promptForString(String prompt) {
		if(prompt == null || prompt.trim().isEmpty()) {
			throw new IllegalArgumentException("The prompt cannot be null, whitespace, nor empty.");
		}
		String input = null;
		boolean isInvalid = true;
		System.out.print(prompt);
		do {
			try {
				input = in.readLine();
				isInvalid = input == null || input.trim().isEmpty();
				if(isInvalid) {
					System.out.println("\nPlease enter something more than white space.");
				}
			} catch(IOException ioe) {
				System.out.println("\nThere was a technical issue. Please try again.");
			}
			System.out.println();
		} while(isInvalid);
		return input;
	}
	
	/**
	 * Generates a prompt that expects a numeric input representing an int value.
	 * This method loops until valid input is given.
	 * @param prompt - the prompt to be displayed to the user
	 * @param min - the inclusive minimum boundary
	 * @param max - the inclusive maximum boundary
	 * @return the int value
	 */
	public static int promptForInt(String prompt, int min, int max) {
		if(max < min) {
			throw new IllegalArgumentException("The min cannot be greater than max.");
		}
		int userNum = 0;
		boolean isInvalid = true;
		do {
			String input = promptForString(prompt);
			try {
				userNum = Integer.parseInt(input);
				isInvalid = userNum < min || userNum > max;
			} catch(NumberFormatException nfe) {
			}
			if(isInvalid) {
				System.out.println("Please enter a valid number: ");
			}
		} while(isInvalid);
		return userNum;
	}
	
	/**
	 * Generates a prompt that expects the user to enter one of two responses that will equate
	 * to a boolean value. The trueString represents the case insensitive response that will equate to true. 
	 * The falseString acts similarly, but for a false boolean value.
	 * 		Example: Assume this method is called with a trueString argument of "yes" and a falseString argument of
	 * 		"no". If the enters "YES", the method returns true. If the user enters "no", the method returns false.
	 * 		All other inputs are considered invalid, the user will be informed, and the prompt will repeat.
	 * @param prompt - the prompt to be displayed to the user
	 * @param trueString - the case insensitive value that will evaluate to true
	 * @param falseString - the case insensitive value that will evaluate to false
	 * @return the boolean value
	 */
	public static boolean promptForBoolean(String prompt, String trueString, String falseString){
		if(trueString == null || falseString == null || trueString.trim().isEmpty() || falseString.trim().isEmpty() ||
				trueString.trim().equalsIgnoreCase(falseString.trim())) {
			throw new IllegalArgumentException("The trueString and falseString must be non-null, non-whitespace-only, distinct values.");
		}
		trueString = trueString.trim();
		falseString = falseString.trim();

		boolean isInvalid;
		boolean answer;
		do {
			String input = promptForString(prompt).trim();
			isInvalid = !(input.equalsIgnoreCase(trueString) || input.equalsIgnoreCase(falseString));
			answer = input.equalsIgnoreCase(trueString);
			if(isInvalid) {
				System.out.println("You must enter either \"" + trueString + "\" or \"" + falseString + "\". Please try again.");
			}
		} while(isInvalid);
		return answer;
	}

	/**
	 * Generates a console-based menu using the Strings in options as the menu items.
	 * Reserves the number 0 for the "quit" option when withQuit is true.
	 * @param options - Strings representing the menu options
	 * @param withQuit - adds option 0 for "quit" when true
	 * @return the int of the selection made by the user
	 */
	public static int promptForMenuSelection(String prompt, String[] options, boolean withQuit){
		String menu = printOptions(prompt, options, withQuit);
		if(withQuit) {
			System.out.println("0) Exit");
		}
		return promptForInt(menu, withQuit ? 0 : 1, options.length);
	}

	public static int promptForMenuSelection(String prompt, String[] options, boolean withQuit, String customQuit){
		String menu = printOptions(prompt, options, withQuit);
		if(withQuit) {
			System.out.println("0) " + customQuit);
		}
		return promptForInt(menu, withQuit ? 0 : 1, options.length);
	}

	private static String printOptions(String prompt, String[] options, boolean withQuit) {
		if(options == null || (options.length == 0 && !withQuit)) {
			throw new IllegalArgumentException("There must be at least one option to choose");
		}
		StringBuilder sb = new StringBuilder(prompt);
		for (int i = 0; i < options.length; i++) {
			sb.append(i + 1).append(") ").append(options[i]).append("\n");
		}
		sb.append("\nEnter the number of your selection: ");
		return sb.toString();
	}
	
	public static int promptForMenuSelection(String prompt, List<String> options, boolean withQuit){
		String menu = printOptions(prompt, options, withQuit);
		if(withQuit) {
			System.out.println("0) Exit");
		}
		return promptForInt(menu, withQuit ? 0 : 1, options.size());
	}
	
	public static int promptForMenuSelection(String prompt, List<String> options, boolean withQuit, String customQuit){
		String menu = printOptions(prompt, options, withQuit);
		if(withQuit) {
			System.out.println("0) " + customQuit);
		}
		return promptForInt(menu, withQuit ? 0 : 1, options.size());
	}

	private static String printOptions(String prompt, List<String> options, boolean withQuit) {
		if(options == null || (options.size() == 0 && !withQuit)) {
			throw new IllegalArgumentException("There must be at least one option to choose");
		}
		StringBuilder sb = new StringBuilder(prompt).append("\n");
		for (int i = 0; i < options.size(); i++) {
			sb.append(i + 1).append(") ").append(options.get(i)).append("\n");
		}
		sb.append("\nEnter the number of your selection: ");
		return sb.toString();
	}


	public static boolean promptForYN(String prompt) {
		boolean isInvalid;
		boolean answer = false;
		do {
			String input = promptForString(prompt);
			String fLetter = input.substring(0, 1);
			isInvalid = !(fLetter.equalsIgnoreCase("y") || fLetter.equalsIgnoreCase("n"));
			if(isInvalid) {
				System.out.println("Please enter a yes or no answer.");
			}
			else if(fLetter.equalsIgnoreCase("y")) {
				answer = true;
			}
		} while(isInvalid);
		return (answer);
	}

}
