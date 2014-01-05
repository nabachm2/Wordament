package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * @author Nicholas
 *
 * Utility class that holds the data of a given
 * wordament tile, mainly its x,y position and the
 * text on the tile
 *
 */
public class Tile {
	
	private int x;
	private int y;
	private String tileName;
	
	public Tile(String tn, int x, int y) { 
		tileName = tn;
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics g) {
		Font font = new Font("Times", Font.PLAIN, 30);
		g.setFont(font);
		
		int xOff = g.getFontMetrics().stringWidth(tileName);
		int yOff = g.getFontMetrics().getAscent();
		g.setColor(Color.BLACK);
		g.fillRect(x * 100, y * 100, 100, 100);
		g.setColor(Color.WHITE);
		g.drawString(tileName, x * 100 + (100 - xOff) / 2, y * 100 + (100 + yOff) / 2);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return tileName;
	}
	
}