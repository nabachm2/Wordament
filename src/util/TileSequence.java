package util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * @author Nicholas
 *
 * Contains a list of Tiles, basically functioning as
 * a StringBuffer.
 *
 */
public class TileSequence {

	private ArrayList<Tile> tileSequence;

	public TileSequence() {
		tileSequence = new ArrayList<Tile>();
	}
	
	public TileSequence(TileSequence other) {
		this();
		for (Tile t : other.tileSequence)
			tileSequence.add(t);
	}
	
	public void addTile(Tile tile) {
		tileSequence.add(tile);
	}
	
	public ArrayList<Tile> getTiles() {
		return tileSequence;
	}
	
	public void render(Graphics g) {
		for (Tile t : tileSequence)
			t.render(g);
		
		g.setColor(Color.RED);
		Tile t0 = tileSequence.get(0);
		g.fillOval(t0.getX() * 100, t0.getY() * 100, 30, 30);
		for (int i = 0;i < tileSequence.size();i ++) {
			Tile t1 = tileSequence.get(i);
			if (i + 1 < tileSequence.size()) {
				Tile t2 = tileSequence.get(i + 1);
				g.drawLine(t1.getX() * 100 + 50, t1.getY() * 100 + 50, 
						t2.getX() * 100 + 50, t2.getY() * 100 + 50);
			}
		}
	}
	
	public StringBuffer getStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		for (Tile t : tileSequence)
			buffer.append(t.toString());
		
		return buffer;
	}
	
	public String toString() {
		return getStringBuffer().toString();
	}
	
}
