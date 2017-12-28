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
	 * Width of the display
	 */
	int width;
	/**
	 * Height of the display
	 */
	int height;
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
	/**
	 * Also referred to as V
	 */
	byte[] registers;
	/**
	 * The stack is a 16-deep array of 16 bit values<br/>
	 *(that's why this is an char array and not a byte array)
	 */
	char[] stack;
	int stackPointer;
	public Chip8(){
		i = 200;
		width = 64;
		height = 32;
		display = new short[width][height];
		memory = new char[0xFFF];
		registers = new byte[16];
		stack = new char[16];
		stackPointer = -1; //Initialised to -1 since it is 0-indexed
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			switch(memory[i] & (0xF<<12)){
			case 0x0000:
				//0x0nnn means jump to machine code at address nnn but like most modern interpreters, we'll just ignore it.
				if(memory[i] == 0x00E0){ //Clear display
					display = new short[width][height];
					//Display.redraw();//Disabled for now since we do not have a display
					i++;
				} else if(memory[i] == 0x00EE){//Return from subroutine
					try{
						i = stack[stackPointer--];
					} catch(ArrayIndexOutOfBoundsException aioobe){
						System.err.println("Pogram tried to return from non-existent subroutine, skipping.");
						i++;
					}
				}
				break;
			
			case 0x1000://1nnn: Jump to address nnn
				i = memory[i] & 0x0FFF;
				break;
			
			case 0x2000://2nnn: Call subroutine at address nnn
				stack[++stackPointer] = (char) i;
				i = memory[i] & 0x0FFF;
				break;
			
			case 0x3000://3xkk: If Vx == kk, skip next instruction
				if((registers[memory[i] & 0x0F00] >> 8) == (memory[i] & 0x00FF))
					i+=2;
				else
					i++;
				break;
			
			default:
				System.err.println("Unsupported opcode, skipping");
				i++;
				break;
			}
		}
	}

}
