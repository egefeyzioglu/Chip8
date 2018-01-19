package tr.k12.tevitol.efeyzioglu.chip8.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tr.k12.tevitol.efeyzioglu.chip8.Chip8;

public class ChipFrame extends JFrame{

	/**
	 * Apparently Eclipse has a use for this. I certainly don't.
	 */
	private static final long serialVersionUID = 7375531713906526789L;
	
	int pixelSize = 20;
	/**
	 * Display JPanels, [x][y]
	 */
	JPanel[][] display;
	
	public ChipFrame(String title, Chip8 c8){
		super(title);
		this.setLayout(new GridLayout(c8.getHeight(), c8.getWidth()));
		this.setPreferredSize(new Dimension(c8.getWidth() * pixelSize, c8.getHeight() * pixelSize));
		this.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//                                    I <3
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);// Stackowerflow
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		display = new JPanel[c8.getWidth()][c8.getHeight()];
		for(int x = 0; x < display.length; x++){
			for(int y = 0; y < display[x].length; y++){
				display[x][y] = new JPanel();
				display[x][y].setBackground(Color.BLACK);
				this.add(display[x][y]);
			}
		}
		this.setVisible(true);
	}
	
	public void setPixel(int x, int y, boolean on){
		display[x][y].setBackground(on ? Color.WHITE : Color.BLACK);
	}
	
}
