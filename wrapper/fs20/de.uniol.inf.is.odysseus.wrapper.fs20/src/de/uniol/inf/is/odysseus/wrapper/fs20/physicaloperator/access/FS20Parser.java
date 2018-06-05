/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
