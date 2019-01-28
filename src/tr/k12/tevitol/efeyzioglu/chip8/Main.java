package tr.k12.tevitol.efeyzioglu.chip8;

import java.io.IOException;

import tr.k12.tevitol.efeyzioglu.chip8.display.ChipFrame;

public class Main {
	
	public static String title = "Chip 8";
	private static ChipFrame frame;
	
	public static void main(String[] args){
		Chip8 chip8 = new Chip8();
		setFrame(new ChipFrame(title, chip8));
		try {
			chip8.loadFromStorage("D:\\pong2.c8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chip8.run();
	}

	public static ChipFrame getFrame() {
		return frame;
	}

	public static void setFrame(ChipFrame frame) {
		Main.frame = frame;
	}
}
