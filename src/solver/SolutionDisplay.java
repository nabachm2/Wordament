package solver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.TileSequence;

/**
 * @author Nicholas
 *
 * This class provides an easy way to view the found
 * words within the grid, kinda hacked together
 *
 */
public class SolutionDisplay extends JFrame implements Runnable, KeyListener {

	private DisplayCanvas canvas;
	
	private HashSet<String> used;
	private ArrayList<TileSequence> words;
	private TileSequence current;
	
	public SolutionDisplay(ArrayList<TileSequence> words) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setSize(400, 400 + 15);
		this.words = words;
		
		used = new HashSet<String>();
		
		canvas = new DisplayCanvas();
		add(canvas);
		addKeyListener(this);
		current = words.remove(words.size() - 1);
		repaint();
		//new Thread(this).start();
	}

	@Override
	public void run() {
		while (!words.isEmpty()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			current = words.remove(words.size() - 1);
			repaint();
		}
	}
	
	private class DisplayCanvas extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(Color.BLACK);
			for (int i = 0;i < 4;i ++) {
				g.drawLine(0, i * 100, 400, i * 100);
				g.drawLine(i * 100, 0, i * 100, 400);
			}
			
			
			if (current != null)
				current.render(g);
		}
	
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		used.add(current.toString());
		
		do {
			current = words.remove(words.size() - 1);
			setTitle(current.toString());
		} while (used.contains(current.toString()));
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
