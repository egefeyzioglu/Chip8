package tr.k12.tevitol.efeyzioglu.chip8.display;

import java.awt.Dimension;
import javax.swing.JFrame;

public class MemoryView extends JFrame {

	public MemoryView(){
		super("Chip8 - Memory View");
		this.setPreferredSize(new Dimension(400, 300));
		this.pack();
		this.setVisible(true);
	}
	
}
