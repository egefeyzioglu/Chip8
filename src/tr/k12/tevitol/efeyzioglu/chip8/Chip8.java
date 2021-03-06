package tr.k12.tevitol.efeyzioglu.chip8;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

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
	private int width;
	/**
	 * Height of the display
	 */
	private int height;
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
	short I; //The I Register
	
	Random random;
	
	public Chip8(){
		random = new Random();
		i = 0x200;
		setWidth(64);
		setHeight(32);
		display = new short[getWidth()][getHeight()];
		memory = new char[0xFFF];
		registers = new byte[16];
		stack = new char[16];
		stackPointer = -1; //Initialised to -1 since it is 0-indexed
		I = 0;
		for(int temp = 0; temp < registers.length; temp++) {
			registers[temp] = 0;
		}
	}
	
	public char[] getMemory() {
		return memory;
	}
	
	public void loadFromStorage(String path) throws IOException {
		FileInputStream fis = new FileInputStream(path);
		int c;
		int index = 0x200;
		while((c = fis.read()) != -1) {
			memory[index++] = (char) c;
		}
		fis.close();
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Main.enableMemoryView) {
				Main.enableMemoryView = false;
				Main.setupMemoryView();
			}
			
			
			
			
			System.out.println("Current memory pointer is " + Integer.toHexString(i));
			System.out.println(Integer.toHexString(memory[i] & (0xF<<12)));
			switch(memory[i] & (0xF<<12)){
			case 0x0000:
				//0x0nnn means jump to machine code at address nnn but like most modern interpreters, we'll just ignore it.
				if(memory[i] == 0x00E0){ //Clear display
					display = new short[getWidth()][getHeight()];
					Main.getFrame().repaint();
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
			
			case 0x4000://4xkk: If Vx != kk, skip next instruction
				if((registers[memory[i] & 0x0F00] >> 8) != (memory[i] & 0x00FF))
					i+=2;
				else
					i++;
				break;
			
			case 0x5000://5xy0: If Vx == Vy, skip next instruction
				if(registers[(memory[i] & 0x0F00) >> 8] == registers[(memory[i] & 0x00F0) >> 4])
					i+=2;
				else
					i++;
				break;
				
			case 0x6000://6xkk: Load kk into Vx
				registers[(memory[i] & 0x0F00) >> 8] = (byte) (memory[i] & 0x00FF);
				System.out.println("debug lol");
				i++;
				break;
				
			case 0x7000://7xkk: Increment Vx by kk
				registers[(memory[i] & 0x0F00 >> 8)] += (memory[i] & 0x00FF);
				i++;
				break;
				
			case 0x8000:
				switch(memory[i] & 0x000F){
				case 1://8xy1: Set Vx to Vx OR Vy
					registers[(memory[i] & 0x0F00) >> 8] |= registers[(memory[i] & 0x00F0) >> 4];
					i++;
					break;
				case 2://8xy2: Set Vx to Vx AND Vy
					registers[(memory[i] & 0x0F00) >> 8] &= registers[(memory[i] & 0x00F0) >> 4];
					i++;
					break;
				case 3://8xy3: Set Vx to Vx XOR Vy
					registers[(memory[i] & 0x0F00) >> 8] ^= registers[(memory[i] & 0x00F0) >> 4];
					i++;
					break;
				case 4://8xy4: Increment Vx by Vy, if carry is generated set VF to 1, otherwise clear VF.
					byte sum = (byte) (registers[(memory[i] & 0x0F00) >> 8] + registers[(memory[i] & 0x00F0) >> 4]);
					if(sum > 0xFF) registers[0xF] = 1;
					else registers[0xF] = 0;
					registers[(memory[i] & 0x0F00) >> 8] = (byte) sum;
					i++;
					break;
				case 5://8xy5: Decrement Vx by Vy, if borrow, clear VF, otherwise set VF to 1
					byte diff = (byte) (registers[(memory[i] & 0x0F00) >> 8] - registers[(memory[i] & 0x00F0) >> 4]);
					registers[(memory[i] & 0x0F00) >> 8] = (byte) ((diff > 0) ? diff : -diff);
					registers[0xF] = (registers[(memory[i] & 0x0F00) >> 8] > registers[(memory[i] & 0x00F0) >> 4])? (byte) 0b11111111 : (byte) 0b0;
					break;
				default:
					System.err.println("Unsupported opcode, skipping");
					i++;
					break;
				}
				break;
				
			case 0x9000://9xy0: Skip next instruction if Vx != Vy
				if(registers[(memory[i] & 0x0F00)>>8] == registers[(memory[i] & 0x00F0) >> 4]) {
					i++;
				} else {
					i+=2;
				}
				break;
			case 0xA000://Annn: Set the I register to nnn
				I = (short) (memory[i] & 0x0FFF);
				i++;
				break;
			case 0xB000://Bnnn: Jump to V0 + nnn
				i = (memory[i] & 0x0FFF) + registers[0];
				break;
			case 0xC000://Cxkk: Set Vx = random byte AND kk
				registers[(memory[i] & 0x0F00)>>8] = (byte) ((byte) random.nextInt(0x100) & (memory[i] & 0x00FF));
				i++;
				break;
			default:
				System.err.println("Unsupported opcode, skipping");
				i++;
				break;
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
