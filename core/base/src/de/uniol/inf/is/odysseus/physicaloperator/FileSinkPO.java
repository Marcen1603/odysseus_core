package de.uniol.inf.is.odysseus.physicaloperator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.ICSVToString;
import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class FileSinkPO extends AbstractSink<Object> {

	final private String filename;
	final private boolean csvSink;
	final private long writeAfterElements;
	private long elementsWritten;
	transient private StringBuffer writeCache;
	transient BufferedWriter out;

	public FileSinkPO(String filename, String sinkType, long writeAfterElements) {
		this.filename = filename;
		if ("CSV".equalsIgnoreCase(sinkType)) {
			csvSink = true;
		} else {
			csvSink = false;
		}
		this.writeAfterElements = writeAfterElements;
	}

	public FileSinkPO(FileSinkPO fileSink) {
		this.filename = fileSink.filename;
		this.csvSink = fileSink.csvSink;
		this.writeAfterElements = fileSink.writeAfterElements;
	}

	public String getFilename() {
		return filename;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			writeCache = new StringBuffer();
			elementsWritten = 0;
			out = new BufferedWriter(new FileWriter(
					OdysseusDefaults.openOrCreateFile(filename)));
		} catch (IOException e) {
			OpenFailedException ex = new OpenFailedException(e);
			ex.fillInStackTrace();
			throw ex;
		}

	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		try {
			String toWrite = null;
			if (csvSink) {
				toWrite = ((ICSVToString) object).csvToString();
			} else {
				toWrite = "" + object;
			}
			if (writeAfterElements > 0) {
				writeCache.append(toWrite).append("\n");
				elementsWritten++;
				if (writeAfterElements >= elementsWritten) {
					writeToFile(writeCache.toString());
					writeCache = new StringBuffer();
					elementsWritten = 0;
				}
			} else {
				writeToFile(toWrite);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void writeToFile(String elem) throws IOException {
		out.write(elem);
		out.flush();
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
		if (this.getSubscribedToSource().get(0)
				.equals(fs.getSubscribedToSource().get(0))
				&& this.filename.equals(fs.getFilename())
				&& this.csvSink == fs.csvSink) {
			return true;
		}
		return false;
	}

}
