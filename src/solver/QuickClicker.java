package solver;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import util.Tile;
import util.TileSequence;

import com.asprise.util.ocr.OCR;

/**
 * @author Nicholas
 *
 * This class provides ways to read the board and 
 * click the buttons very fast. However this is very
 * specific to wordament, ie it uses the color of the board
 * to calculate the positions of the tiles, and requires the whole
 * board to be visible. As such its a little hacked together
 * and I dont care enough to really comment/clean it up
 *
 */
public class QuickClicker {

	private final static Color BOARD_COLOR = new Color(26, 141, 226);
	
	private Point boardStart;
	private int tileSize;
	private int spacing;
	
	public QuickClicker() {
		boardStart = new Point();
		calculateBoardDimensions();
	}
	
	private boolean colorsClose(Color color1, Color color2) {
		return Math.abs(color1.getRed() - color2.getRed()) < 3 &&
				Math.abs(color1.getGreen() - color2.getGreen()) < 3 && 
				Math.abs(color1.getBlue() - color2.getBlue()) < 3;
	}
	
	private void calculateBoardDimensions() {
		try {
			Robot r = new Robot();
			Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
			BufferedImage image = r.createScreenCapture(new Rectangle(0, 0, ss.width, ss.height));
			for (int x = 0;x < ss.width;x ++) {
				for (int y = 0;y < ss.height;y ++) {
					Color color = new Color(image.getRGB(x, y));
					if (colorsClose(color, BOARD_COLOR)) {
						boardStart.x = x;
						boardStart.y = y;
						x = ss.width;
						break;
					}
				}
			}
			
			int x = boardStart.x;
			while (colorsClose(r.getPixelColor(x, boardStart.y), BOARD_COLOR)) x ++;
			tileSize = x - boardStart.x + 1;
			
			while (!colorsClose(r.getPixelColor(x, boardStart.y), BOARD_COLOR)) x ++;
			spacing = x - (boardStart.x + tileSize) + 1;
			System.out.println(boardStart + " " + tileSize + " " + spacing);
			
			image = null;
		} catch (AWTException e) {
			e.printStackTrace();
		} 
	}
	
	public String[][] getBoard() {
		try { //Board reader requires text to be black and white
			Robot r = new Robot();
			BufferedImage board = r.createScreenCapture(
					new Rectangle(boardStart, new Dimension(
							tileSize * 4 + spacing * 3 - 1, tileSize * 4 + spacing * 3 - 1)));
			for (int x = 0;x < board.getWidth();x ++) {
				for (int y = 0;y < board.getHeight();y ++) {	
					if (!colorsClose(new Color(board.getRGB(x, y)), Color.WHITE)) //lets are white, so make them black
						board.setRGB(x, y, Color.WHITE.getRGB());
					else //everything else should be black
						board.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
			
			//White out numbers (point values)
			for (int x = 0;x < 4;x ++) {
				for (int y = 0;y < 4;y ++) {
					for (int xs = 0;xs < tileSize / 3;xs ++) {
						for (int ys = 0;ys < tileSize / 4;ys ++) {
							int off = tileSize + spacing; 
							board.setRGB(x * off + xs, y * off + ys, Color.WHITE.getRGB());
						}
					}
				}
			}
			
			int i = 0;
			String[][] tiles = new String[4][4];
			OCR ocr = new OCR();
			String b = ocr.recognizeCharacters(board);
			for (String line : b.split("\\r?\\n")) { //Get red of any special characters
				if (line.length() > 0 && Character.isLetter(line.charAt(0))) {
					for (String l : line.split(" ")) {
						if (l.length() > 0 && Character.isLetter(l.charAt(0))) {
							System.out.println("|" + l + "|");
							System.out.println((i / 4) + " " + (i % 4));
							tiles[i % 4][i / 4] = l;
							i ++;
						}
					}
				}
			}
			for ( i = 0;i < 4;i ++ ) {
				for (int k = 0;k < 4;k ++) { //fix l's to i's
					if (tiles[i][k].equals("l")) tiles[i][k] = "i";
					else tiles[i][k] = tiles[i][k].toLowerCase();
				}
			}
			
			return tiles;
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void clickWord(TileSequence word) {
		System.out.println(word.toString());
		try {
			Robot r = new Robot();
			Tile st = word.getTiles().get(0);
			int off = spacing + tileSize;
			int sx = st.getX() * off + boardStart.x + off / 2;
			int sy = st.getY() * off + boardStart.y + off / 2;
			r.mouseMove(sx - 5, sy - 5);
			
			int HANDICAP = 10;
			Thread.sleep(50);
			r.mousePress(InputEvent.BUTTON1_MASK);
			
			
			for (Tile t : word.getTiles()) {
				System.out.println(t.getX() + " " + t.getY());
				int x = t.getX() * off + boardStart.x + off / 2;
				int y = t.getY() * off + boardStart.y + off / 2;
				r.mouseMove(x, y);
				Thread.sleep(50 * HANDICAP);
			}	
			r.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
