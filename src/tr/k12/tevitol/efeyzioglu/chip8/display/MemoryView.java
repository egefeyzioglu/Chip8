package tr.k12.tevitol.efeyzioglu.chip8.display;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import tr.k12.tevitol.efeyzioglu.chip8.Chip8;

public class MemoryView extends JFrame implements Runnable{
	Chip8 chip8;
	JLabel memoryText;
	
	public MemoryView(Chip8 chip8){
		super("Chip8 - Memory View");
		
		this.chip8 = chip8;
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		memoryText = new JLabel();
		memoryText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		this.add(memoryText);
		
		this.setPreferredSize(new Dimension(1920, 1080));
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void run() {
		while(!Thread.interrupted()) {
			int i = 0;
			String memoryTextString = "<html>";
			for(char c: chip8.getMemory()) {
				memoryTextString += Integer.toHexString(0x100 | c).substring(1) + "  " + (i + 1 % 0x4F == 0? "<br/>" : "");
				i++;
			}
			memoryTextString += "</html>";
			memoryText.setText(memoryTextString);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}
	
}
