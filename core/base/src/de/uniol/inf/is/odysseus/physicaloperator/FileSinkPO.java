package de.uniol.inf.is.odysseus.physicaloperator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.CSVToString;
import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class FileSinkPO extends AbstractSink<Object> {

	final private String filename;
	final private boolean csvSink;
	transient BufferedWriter out;

	public FileSinkPO(String filename, String sinkType) {
		this.filename = filename;
		if ("CSV".equalsIgnoreCase(sinkType)) {
			csvSink = true;
		} else {
			csvSink = false;
		}
	}

	public FileSinkPO(FileSinkPO fileSink) {
		this.filename = fileSink.filename;
		this.csvSink = fileSink.csvSink;
	}

	public String getFilename() {
		return filename;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			out = new BufferedWriter(new FileWriter(OdysseusDefaults.openOrCreateFile(filename)));
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
	public FileSinkPO clone() {
		return new FileSinkPO(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof FileSinkPO)) {
			return false;
		}
		FileSinkPO fs = (FileSinkPO) ipo;
		if (this.getSubscribedToSource().get(0).equals(fs.getSubscribedToSource().get(0))
				&& this.filename.equals(fs.getFilename()) && this.csvSink == fs.csvSink) {
			return true;
		}
		return false;
	}

}
