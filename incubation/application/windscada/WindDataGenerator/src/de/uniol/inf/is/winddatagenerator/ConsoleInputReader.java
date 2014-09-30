package de.uniol.inf.is.winddatagenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

public class ConsoleInputReader extends Observable implements Runnable{

	private static ConsoleInputReader INSTANCE;
	private boolean isRunning;
	public enum State { ON, OFF};
	
	private State state = State.ON;

	private ConsoleInputReader() {
		
	}
	@Override
	public void run() {
		this.isRunning = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (isRunning) {
			try {
				String content = br.readLine();
				switch (content) {
				case "on":
					if(this.state!= State.ON) {
						this.state = State.ON;
						System.out.println("Generators on");
						this.setChanged();
					}
					break;
				case "off":
					if(this.state!= State.OFF) {
						this.state = State.OFF;
						System.out.println("Generators produce 0");
						this.setChanged();
					}
					break;
				default:
					System.out.println("Command unknown. Type 'on' or 'off'.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.notifyObservers();
		}
	}
	
	public State getState() {
		return this.state;
	}

	public void stopIt() {
		this.isRunning = false;
	}
	
	public static ConsoleInputReader getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ConsoleInputReader();
		}
		return INSTANCE;
	}
	
	

}
