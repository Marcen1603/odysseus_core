package de.uniol.inf.is.odysseus.physicaloperator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class FileSink extends AbstractSink<Object> {
	
	private String filename;
	BufferedWriter out;
	
	public FileSink(String filename){
		this.filename = filename;
	}

	public FileSink(FileSink fileSink) {
		this.filename = fileSink.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
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
			out.write(""+object);
			out.newLine();
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
	
	@Override
	public FileSink clone() {
		return new FileSink(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}
	
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof FileSink)) {
			return false;
		}
		FileSink fs = (FileSink) ipo;
		if(this.getSubscribedToSource().equals(fs.getSubscribedToSource()) && this.filename.equals(fs.getFilename())) {
			return true;
		}
		return false;
	}

}
