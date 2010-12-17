package de.uniol.inf.is.odysseus.physicaloperator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.CSVToString;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class FileSink extends AbstractSink<Object> {

	final private String filename;
	final private boolean csvSink;
	transient BufferedWriter out;

	public FileSink(String filename, String sinkType) {
		this.filename = filename;
		if ("CSV".equalsIgnoreCase(sinkType)) {
			csvSink = true;
		} else {
			csvSink = false;
		}
	}

	public FileSink(FileSink fileSink) {
		this.filename = fileSink.filename;
		this.csvSink = fileSink.csvSink;
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
			if (csvSink) {
				out.write(""+((CSVToString)object).csvToString()); // Check if correct Type to expensive!
			} else {
				out.write("" + object);
			}
			out.newLine();
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process_close() {
		super.process_close();
		try {
			out.close();
		} catch (IOException e) {
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

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof FileSink)) {
			return false;
		}
		FileSink fs = (FileSink) ipo;
		if (this.getSubscribedToSource().equals(fs.getSubscribedToSource())
				&& this.filename.equals(fs.getFilename())) {
			return true;
		}
		return false;
	}

}
