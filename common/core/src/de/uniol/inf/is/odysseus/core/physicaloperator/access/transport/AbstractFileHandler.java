package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

abstract public class AbstractFileHandler extends AbstractTransportHandler {

	public static final String FILENAME = "filename";
	public static final String APPEND = "append";
	protected String filename;
	protected InputStream in;
	protected OutputStream out;
	protected boolean append;

	public AbstractFileHandler() {
		super();
	}

	public AbstractFileHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}
	
	@Override
	public void setOptionsMap(Map<String, String> options) {
		super.setOptionsMap(options);
		if (options.containsKey(FILENAME)){
			filename = options.get(FILENAME);
			filename = convertForOS(filename);
		}else{
			throw new IllegalArgumentException("No filename given!");			
		}
		
		append = (options.containsKey(APPEND)) ? Boolean
				.parseBoolean(options.get(APPEND)) : false;
	}

	protected String convertForOS(String filename) {
		char thisos = File.separatorChar;
		if(thisos=='/'){
			filename = filename.replace('\\', thisos);
		}else{
			filename = filename.replace('/', thisos);
		}
		return filename;
	}

	@Override
	public void send(byte[] message) throws IOException {
		out.write(message);
	}

	@Override
	public InputStream getInputStream() {
		return in;
	}

	@Override
	public OutputStream getOutputStream() {
		return out;
	}

	@Override
	public void processInClose() throws IOException {
		fireOnDisconnect();
		in.close();
	}

	@Override
	public void processOutClose() throws IOException {
		fireOnDisconnect();
		out.flush();
		out.close();
	}

}
