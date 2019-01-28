package tr.k12.tevitol.efeyzioglu.chip8;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;

import tr.k12.tevitol.efeyzioglu.chip8.display.ChipFrame;
import tr.k12.tevitol.efeyzioglu.chip8.display.MemoryView;

public class Main {
	
	public static String title = "Chip 8";
	private static ChipFrame frame;
	static MemoryView memoryView;
	static Chip8 chip8;
	
	public static boolean enableMemoryView = false;
	
	/**
	 * Sets up and displays the memory view
	 */
	public static void setupMemoryView() {
		memoryView = new MemoryView(chip8);
		memoryView.run();
	}
	
	public static void main(String[] args){
		chip8 = new Chip8();
		setFrame(new ChipFrame(title, chip8));
		try {
			chip8.loadFromStorage("D:\\pong2.c8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		doKeyboardStuff();
		
		//memoryView = new MemoryView(chip8);
		//memoryView.run();
		
		chip8.run();
	}

	public static ChipFrame getFrame() {
		return frame;
	}

	public static void setFrame(ChipFrame frame) {
		Main.frame = frame;
	}
	
	private static void doKeyboardStuff() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

	        @Override
	        public boolean dispatchKeyEvent(KeyEvent ke) {
	            synchronized (Main.class) {
	                switch (ke.getID()) {
	                case KeyEvent.KEY_PRESSED:
	                    if (ke.getKeyCode() == KeyEvent.VK_M && ke.isControlDown()) {
	                    	//memoryView = new MemoryView(chip8);
	                    	//memoryView.run();
	                    	enableMemoryView = true;
	                    }
	                    break;

	                case KeyEvent.KEY_RELEASED:
	                    break;
	                }
	                return false;
	            }
	        }
	    });
	}
}
