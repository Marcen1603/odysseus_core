package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class LineFileInputHandler extends AbstractInputHandler<String> {

	private String path;
	private BufferedReader bf;

	public LineFileInputHandler() {
		// needed for declarative service
	}
	
	public LineFileInputHandler(String filename) {
		this.path = filename;
	}
	
	@Override
	public IInputHandler<String> getInstance(Map<String, String> options) {
		return new LineFileInputHandler(options.get("filename"));
	}

	@Override
	public void init() {
		try {
			// logger.debug(fileType);
			File file = new File(path);
			bf = new BufferedReader(new FileReader(file));
			// logger.debug(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized boolean hasNext() {
		try {
			if (bf.ready()) {
				return true;
			}
		} catch (IOException e) {
			// TODO: Use another exception
			throw new RuntimeException(e);
		}
		return false;
	}

	@Override
	public String getNext() {
		try {
			String ret = bf.readLine();
			if (ret != null && !ret.isEmpty()) {
				return ret;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void terminate() {

	}

	@Override
	public String getName() {
		return "LineFile";
	}
	
}
