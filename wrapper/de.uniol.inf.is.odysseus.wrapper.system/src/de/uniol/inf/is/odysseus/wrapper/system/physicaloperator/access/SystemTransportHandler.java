/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.wrapper.system.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SystemTransportHandler extends AbstractTransportHandler {
	/** Logger */
	private Logger LOG = LoggerFactory.getLogger(SystemTransportHandler.class);
	private String command;
	private String[] env;
	/** In and output for data transfer */
	private InputStream input;
	private OutputStream output;

	/**
 * 
 */
	public SystemTransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public SystemTransportHandler(final IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new RuntimeException("Not supported yet");
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		final SystemTransportHandler handler = new SystemTransportHandler(
				protocolHandler);
		handler.init(options);
		return handler;
	}

	protected void init(Map<String, String> options) {
		if (options.get("command") != null) {
			setCommand(options.get("command"));
		}
		if (options.get("env") != null) {
			setEnv(options.get("env"));
		} else {
			setEnv("");
		}
	}

	@Override
	public InputStream getInputStream() {
		return this.input;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.output;
	}

	@Override
	public String getName() {
		return "System";
	}

	@Override
	public void processInOpen() throws IOException {
		this.input = new SystemInputStream(this.command, this.env);
	}

	@Override
	public void processOutOpen() throws IOException {
		this.output = new SystemOutputStream(this.command, this.env);
	}

	@Override
	public void processInClose() throws IOException {
		this.input = null;
		this.fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
		this.output = null;
		this.fireOnDisconnect();
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}

	public void setEnv(String[] env) {
		this.env = env;
	}

	public void setEnv(String env) {
		this.env = env.split(",");
	}

	public String[] getEnv() {
		return this.env;
	}

	private class SystemInputStream extends InputStream {
		private final String command;
		private final String[] env;
		private InputStream stream;

		public SystemInputStream(final String command, final String[] env) {
			this.command = command;
			this.env = env;
		}

		@Override
		public int read() throws IOException {
			if (this.isStreamEmpty()) {
				this.call();
			}
			return this.stream.read();
		}

		@Override
		public int available() throws IOException {
			if (this.isStreamEmpty()) {
				this.call();
			}
			return this.stream.available();
		}

		private boolean isStreamEmpty() throws IOException {
			return (this.stream == null) || (this.stream.available() == 0);
		}

		private void call() throws IOException {
			if (this.stream != null) {
				this.stream.close();
			}
			Process process = Runtime.getRuntime().exec(command, env);
			try {
				process.waitFor();
				this.stream = process.getInputStream();
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	private class SystemOutputStream extends OutputStream {
		private ByteBuffer buffer = ByteBuffer.allocate(1024);
		private final String command;
		private final String[] env;

		public SystemOutputStream(final String command, final String[] env) {
			this.command = command;
			this.env = env;
		}

		@Override
		public void write(final int b) throws IOException {
			if ((1 + this.buffer.position()) >= this.buffer.capacity()) {
				final ByteBuffer newBuffer = ByteBuffer
						.allocate((1 + this.buffer.position()) * 2);
				final int pos = this.buffer.position();
				this.buffer.flip();
				newBuffer.put(this.buffer);
				this.buffer = newBuffer;
				this.buffer.position(pos);
				SystemTransportHandler.this.LOG.debug("Extending buffer to "
						+ this.buffer.capacity());
			}
			this.buffer.put((byte) b);
		}

		@Override
		public void flush() throws IOException {
			this.buffer.flip();
			Process process = Runtime.getRuntime().exec(
					String.format(command, this.buffer.asCharBuffer()
							.toString()), env);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			} finally {
				process.destroy();
			}
		}
	}

}
