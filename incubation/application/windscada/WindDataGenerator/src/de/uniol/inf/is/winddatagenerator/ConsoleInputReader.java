package de.uniol.inf.is.winddatagenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

/**
 * Reads input from standard input. Valid commands are: 'on','off'. Sets the
 * state accordingly. Observers can register to this class to get notifications
 * when the state changes
 * 
 * @author Dennis Nowak
 * 
 */
public class ConsoleInputReader extends Observable implements Runnable {

	private static ConsoleInputReader INSTANCE;
	private boolean isRunning;

	public enum State {
		ON, OFF
	};

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
					if (this.state != State.ON) {
						this.state = State.ON;
						System.out.println("Generators on");
						this.setChanged();
					}
					break;
				case "off":
					if (this.state != State.OFF) {
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

	/**
	 * Returns the current state
	 * 
	 * @return the current state
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * stops the ConsoleInputReader runnable
	 */
	public void stopIt() {
		this.isRunning = false;
	}

	/**
	 * Returns a single ConsoleInputReader instance
	 * 
	 * @return an instance of the ConsoleInputReader
	 */
	public static ConsoleInputReader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ConsoleInputReader();
		}
		return INSTANCE;
	}

}
