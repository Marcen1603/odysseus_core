package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.base.OpenFailedException;

public class FileSink extends AbstractSink<Object> {
	
	private String filename;
	BufferedWriter out;
	
	public FileSink(String filename){
		this.filename = filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		try {
			out = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			OpenFailedException ex = new OpenFailedException(e);
			ex.fillInStackTrace();
			throw ex;
		}
		
	}
	
	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		try {
			out.write(""+object+"\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void process_close() {
		super.process_close();
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
