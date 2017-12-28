package tr.k12.tevitol.efeyzioglu;

public class Chip8 implements Runnable{
	/**
	 * Program counter
	 */
	int i;
	/**
	 * Array for the monochrome display<br>
	 * Left to right, top to bottom
	 */
	short[][] display;
	/**
	 * Memory<br>
	 * (Character array because all the instructions are two bits wide)
	 */
	char[] memory;
	/**
	 * Buzzer active whenever not equal to zero
	 */
	int ST;
	/**
	 * Delay timer
	 */
	int DT;
	byte[] registers;
	/**
	 * The stack is a 16-deep array of 16 bit values<br/>
	 *(that's why this is an char array and not a byte array)
	 */
	char[] stack;
	public Chip8(){
		i = 200;
		display = new short[64][32];
		memory = new char[0xFFF];
		registers = new byte[16];
		stack = new char[16];
	}
	
	public void run() {
		char ins = (char) (memory[i] << 8 | memory[i+1]);
		while(!Thread.currentThread().isInterrupted()){
			
		}
	}

}
