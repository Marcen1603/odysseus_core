package de.uniol.inf.is.odysseus.wrapper.fs20.physicaloperator.access;

import java.nio.ByteBuffer;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class FS20Parser {
	@SuppressWarnings("unused")
	private final static byte checksum = 0x06;

	@SuppressWarnings("unused")
	public void parse(ByteBuffer message) {
		byte houseCode1Part = message.get();
		byte houseCode2Part = message.get();
		byte addressPart = message.get();
		byte commandPart = message.get();
		byte extendedPart = message.get();
		byte checksumPart = message.get();
		Command command = Command.valueOf(commandPart);
	}

	private enum Command {
		OFF((byte) 0x0);

		byte value;

		Command(byte b) {
			value = b;
		}

		byte getByte() {
			return value;
		}

		public static Command valueOf(byte b) {
			for (Command command : Command.values()) {
				if (b == command.getByte()) {
					return command;
				}
			}
			throw new RuntimeException("Unknown command  " + b);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
