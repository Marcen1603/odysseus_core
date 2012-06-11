package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Map;

public class FileHandler extends AbstractTransportHandler {

	String filename;
	FileInputStream in;

	@Override
	public void process_open() throws UnknownHostException, IOException {
		final File file = new File(filename);
		in = new FileInputStream(file);
	}

	@Override
	public void process_close() throws IOException {
		in.close();
	}

	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public ITransportHandler createInstance(Map<String, String> options) {
		FileHandler fh = new FileHandler();
		fh.filename = options.get("filename");
		return fh;
	}

	@Override
	public InputStream getInputStream() {
		return in;
	}

	@Override
	public String getName() {
		return "File";
	}

}
