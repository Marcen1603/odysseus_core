package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

abstract public class AbstractFileHandler extends AbstractTransportHandler {

	public static final String FILENAME = "filename";
	public static final String APPEND = "append";
	public static final String WRITEDELAYSIZE = "writedelaysize";
	protected String filename;
	protected InputStream in;
	protected OutputStream out;
	protected boolean append;
	final private int writeDelaySize;
	final private ByteBuffer writeBuffer;

	public AbstractFileHandler() {
		super();
		writeDelaySize = 0;
		writeBuffer = null;
	}

	public AbstractFileHandler(IProtocolHandler<?> protocolHandler,
			OptionMap options) {
		super(protocolHandler, options);
		options.checkRequiredException(FILENAME);
		if (options.containsKey(FILENAME)) {
			filename = options.get(FILENAME);
			filename = convertForOS(filename);
		} else {
			throw new IllegalArgumentException("No filename given!");
		}

		writeDelaySize = options.getInt(WRITEDELAYSIZE, 0);
		if (writeDelaySize > 0) {
			this.writeBuffer = ByteBuffer.allocate(writeDelaySize);
		} else {
			this.writeBuffer = null;
		}

		append = (options.containsKey(APPEND)) ? Boolean.parseBoolean(options
				.get(APPEND)) : false;
	}

	protected String convertForOS(String filename) {
		char thisos = File.separatorChar;
		if (thisos == '/') {
			filename = filename.replace('\\', thisos);
		} else {
			filename = filename.replace('/', thisos);
		}
		return filename;
	}

	@Override
	public synchronized void send(byte[] message) throws IOException {
		if (writeDelaySize > 0) {
			int msgL = message.length;
			// Would message plus current buffer content exceed buffers
			// capacity?
			if (msgL + writeBuffer.position() > writeDelaySize) {

				dumpBuffer();
				// To avoid double writes, write content only if message
				// does not
				// fit into buffer
				if (msgL > writeDelaySize) {
					out.write(message);
				} else {
					writeBuffer.put(message);
				}
			} else {
				writeBuffer.put(message);
			}
		} else {
			out.write(message);
		}
	}

	private void dumpBuffer() throws IOException {
		byte[] copy = new byte[writeBuffer.position()];
		writeBuffer.flip();
		writeBuffer.get(copy, 0, writeBuffer.limit());
		out.write(copy);
		writeBuffer.clear();
	}

	@Override
	public InputStream getInputStream() {
		return in;
	}

	@Override
	public boolean isDone() {
		try {
			return in.available() == 0;
		} catch (IOException e) {
			return true;
		}
	}
	
	@Override
	public OutputStream getOutputStream() {
		return out;
	}

	@Override
	public void processInStart() {

	}

	@Override
	public void processInClose() throws IOException {
		fireOnDisconnect();
		in.close();
	}

	@Override
	public synchronized void processOutClose() throws IOException {
		fireOnDisconnect();

		if (writeDelaySize > 0)
			dumpBuffer();

		out.flush();
		out.close();
		out = null;
	}

}
