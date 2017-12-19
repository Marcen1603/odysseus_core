package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

public class GroupSplitFileWriter<R extends IStreamObject<?>> extends
		AbstractSink<R> {

	Map<Object, OutputStream> filemap = new HashMap<>();
	final String path;
	final IGroupProcessor<R, R> groupProcessor;
	final IStreamObjectDataHandler<R> dataHandler;
	final WriteOptions writeOptions;

	public GroupSplitFileWriter(String path,
			IGroupProcessor<R, R> groupProcessor, IStreamObjectDataHandler<R> dataHandler) {
		this.path = path;
		this.groupProcessor = groupProcessor;
		this.dataHandler = dataHandler;
		writeOptions = WriteOptions.defaultOptions2;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		groupProcessor.init();

	}

	@Override
	protected void process_close() {
		for (OutputStream os : filemap.values()) {
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void process_next(R object, int port) {
		Object id = groupProcessor.getGroupID(object);
		OutputStream outputStream = filemap.get(id);
		if (outputStream == null) {
			try {
				outputStream = new FileOutputStream(new File(path
						+ groupProcessor.toGroupString(object) + ".csv"));
				filemap.put(id, outputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		StringBuilder handler = new StringBuilder();

		dataHandler.writeCSVData(handler, object, writeOptions);

		if (outputStream != null) {
			try {
				outputStream.write(handler.toString().getBytes());
				outputStream.write("\n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// ignore
	}

	@Override
	public AbstractSink<R> clone() {
		// Ignore
		return null;
	}

}
