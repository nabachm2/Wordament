package solver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author Nicholas
 *
 * This class represents an English 
 * dictionary to check for words in our board.
 * The dictionary loads all its words from the file
 * Dictionary.txt
 *
 * This class functions as a TrieTree which
 * allows the solver to check to see if a possible
 * word is part of a another word.
 */
public class Dictionary {

	public final static int NOT_PART_WORD = 0;
	public final static int COMPLETE_WORD = 1;
	public final static int PART_WORD = 2;

	//root of tree
	private TrieNode trieTreeRoot;
	//point values specific to wordament
	private HashMap<Character, Integer> pointValues;

	public Dictionary() throws IOException {
		trieTreeRoot = new TrieNode('\0', false);
		InputStream dictFile = Dictionary.class.getResourceAsStream("Dictionary.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(dictFile));

		//load all our words
		String line;
		while ((line = reader.readLine()) != null) 
			insertWord(line);
		
		reader.close();

		//load wordament specific point values
		loadPointValues();
	}
	
	private void loadPointValues() {
		pointValues = new HashMap<Character, Integer>();
		pointValues.put('a', 2);
		pointValues.put('b', 7);
		pointValues.put('c', 3);
		pointValues.put('d', 3);
		pointValues.put('e', 1);
		pointValues.put('f', 5);
		pointValues.put('g', 4);
		pointValues.put('h', 4);
		pointValues.put('i', 2);
		pointValues.put('j', 6);
		pointValues.put('k', 6);
		pointValues.put('l', 3);
		pointValues.put('m', 4);
		pointValues.put('n', 2);
		pointValues.put('o', 2);
		pointValues.put('p', 4);
		pointValues.put('q', 8);
		pointValues.put('r', 2);
		pointValues.put('s', 3);
		pointValues.put('t', 2);
		pointValues.put('u', 4);
		pointValues.put('v', 6);
		pointValues.put('w', 6);
		pointValues.put('x', 9);
		pointValues.put('y', 5);
		pointValues.put('z', 8);
	}

	/**
	 * Inserts a word into the trie tree, by
	 * recursing through all the nodes and building
	 * the spots as necessary.
	 * 
	 * @param word the word to add
	 */
	public void insertWord(String word) {
		char[] letters = word.toCharArray();
		TrieNode curNode = trieTreeRoot;

		for (int i = 0; i < word.length(); i++) { //build the nodes as we need
			if (curNode.links[letters[i] - 'a'] == null)
				curNode.links[letters[i] - 'a'] = new TrieNode(letters[i],
						false);

			curNode = curNode.links[letters[i] - 'a'];
		}
		curNode.fullWord = true; //mark it as a word
	}

	/**
	 * Given a possible word this method recurses through the tree
	 * to see if this word matches or is part of any other word.
	 * 
	 * @param word the word to check
	 * @return PART_WORD if this word is part of a word in the dictionary
	 * 			COMPLETE_WORD if this word is a word in the dictionary
	 * 			NOT_PART_WORD if this word is not a part of any word in the dictionary
	 */
	public int find(StringBuffer word) {
		TrieNode curNode = trieTreeRoot;
		for (int i = 0; i < word.length();i ++) {
			curNode = curNode.links[word.charAt(i) - 'a'];
			if (curNode == null)
				return NOT_PART_WORD;
		}

		if (curNode.fullWord)
			return COMPLETE_WORD;

		return PART_WORD;
	}

	/**
	 * Calculates the point value of the given
	 * word by iterating over the letters and
	 * using their know point values to find the
	 * total value of the word
	 * 
	 * @param word
	 * @return the point value of the word
	 */
	public double getPointValue(String word) {
		double value = 0;
		for (char c : word.toCharArray())
			value += pointValues.get(c);

		if (word.toString().length() >= 8)
			value *= 3;
		else if (word.toString().length() >= 6)
			value *= 2;
		else if (word.toString().length() >= 4)
			value *= 1.5;

		return value;
	}

	private class TrieNode {

		private char letter;
		private TrieNode[] links;

		private boolean fullWord;

		public TrieNode(char letter, boolean fullWord) {
			this.letter = letter;
			links = new TrieNode[26];
			this.fullWord = fullWord;
		}
	}

}
