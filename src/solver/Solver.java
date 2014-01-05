package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import util.Tile;
import util.TileSequence;

/**
 * @author Nicholas
 *
 * Solver for a wordament board, see the method solve() for 
 * implementation details.
 */
public class Solver {

	//Our dictionary object which contains uses a TrieTree
	private Dictionary dictionary;
	
	//Our Grid
	private Tile[][] tiles;
	
	public Solver(String[][] tiles) {
		this.tiles = new Tile[4][4];
		for (int x = 0;x < 4;x ++)
			for (int y = 0;y < 4;y ++)
				this.tiles[x][y] = new Tile(tiles[x][y], x, y);
		
		
		try {
			dictionary = new Dictionary();
		} catch (IOException ex) { ex.printStackTrace(); } 
	}
	
	/**
	 * The way in which this program solves the board is
	 * by recursing through all possible combinations/path
	 * of words within the 4x4 grid. the process is shortened
	 * by using a TrieTree: (http://en.wikipedia.org/wiki/Trie) to 
	 * with all possible words from a given dictionary, and 
	 * culling paths that are not part of a word. 
	 * 
	 * @param cWord the current path
	 * @param used a grid of booleans of which spots have been used
	 * @param x location within grid
	 * @param y location within grid
	 * @param words the final list of words
	 */
	private void _solve(TileSequence cWord, boolean[][] used, int x, int y, ArrayList<TileSequence> words) {		
		if (x < 0 || y < 0 || x > 3 || y > 3 || used[x][y]) //we are out of bounds...
			return;
				
		used[x][y] = true; //we are using this spot now
		cWord.addTile(tiles[x][y]); //add the tile location
		
		int result = dictionary.find(cWord.getStringBuffer()); //see if this word is part of our dictionary
		if (cWord.toString().length() > 2 && result == Dictionary.COMPLETE_WORD) { //its actually a word...
			words.add(cWord); //add it
		} else if (result == Dictionary.NOT_PART_WORD) { //this path isn't part of a word, cull it
			used[x][y] = false;
			return;
		}
		
		//recurse through all directions
		_solve(new TileSequence(cWord), used, x, y + 1, words);
		_solve(new TileSequence(cWord), used, x, y - 1, words);
		_solve(new TileSequence(cWord), used, x - 1, y, words);
		_solve(new TileSequence(cWord), used, x + 1, y, words);
		_solve(new TileSequence(cWord), used, x + 1, y + 1, words);
		_solve(new TileSequence(cWord), used, x + 1, y - 1, words);
		_solve(new TileSequence(cWord), used, x - 1, y + 1, words);
		_solve(new TileSequence(cWord), used, x - 1, y - 1, words);
		//mark this spot as unused now
		used[x][y] = false;
	}
	
	/**
	 * The method, given a sort style, will find words in
	 * the board, and then sort them accordingly. See 
	 * the _solve() for implementation details
	 * 
	 * @param sort_style A comparator of the sorting style
	 * @return
	 */
	public ArrayList<TileSequence> solve(Comparator<TileSequence> sort_style) {
		ArrayList<TileSequence> words = new ArrayList<TileSequence>();
		for (int x = 0;x < 4;x ++)
			for (int y = 0;y < 4;y ++)
				_solve(new TileSequence(), new boolean[4][4], x, y, words);
		
		Collections.sort(words, sort_style);
		return words;
	}
	
	/**
	 * 
	 * The following classes represent the ways to sort the 
	 * solved words, ie by point value, length of words, or just
	 * randomly
	 * 
	 */
	private class RandomComparator implements Comparator<TileSequence> {
		
		@Override
		public int compare(TileSequence a0, TileSequence a1) {
			return (int) Math.signum(Math.random() - 0.5);
		}
		
	}
	
	private class StringLengthComparator implements Comparator<TileSequence> {

		@Override
		public int compare(TileSequence a0, TileSequence a1) {
			return a0.toString().length() - a1.toString().length();
		}
		
	}
	
	private class PointComparator implements Comparator<TileSequence> {

		@Override
		public int compare(TileSequence a0, TileSequence a1) {
			return (int) -Math.signum((dictionary.getPointValue(a1.toString()) - 
							dictionary.getPointValue(a0.toString())));
		}
		
	}
	
	public static void main(String[] args) {
		String[][] tiles = new String[4][4]; //reads the board from terminal input
		Scanner r = new Scanner(System.in); //it is entered like reading a book (left to right, top to bottom)
		for (int i = 0;i < 4;i ++) {
			for (int k = 0;k < 4;k ++) {
				System.out.print("[" + k + "][" + i + "]:");
				tiles[k][i] = r.next();
			}
		}			
				
		/*
		 * The commented code provides a way to read the board
		 * by parsing a screenshot of the computer. It will search
		 * for a wordament board and try to use an OCR library to
		 * read the text. This is untested on other machines
		 */
		//QuickClicker q = new QuickClicker();
		//Solver s = new Solver(q.getBoard());
		Solver s = new Solver(tiles);
		ArrayList<TileSequence> words = s.solve(s.new PointComparator());
	
		/*
		The following code will click the board if the program
		can recognize a wordament board on the screen, this is
		untested on other machines
		*/
		/*
		int t = 0;
		HashSet<String> used = new HashSet<String>();
		for (TileSequence w : words) {
			
			if (used.contains(w.toString()))
				continue;
			else
				used.add(w.toString());
			
			q.clickWord(w);
		}
		*/
		
		//display the board
		SolutionDisplay display = new SolutionDisplay(words);
	}

	
}
