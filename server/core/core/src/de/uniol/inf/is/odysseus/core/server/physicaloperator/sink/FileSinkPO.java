/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.locks.ReentrantLock;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ICSVToString;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.util.FileUtils;

public class FileSinkPO extends AbstractSink<IStreamObject<?>> {

	final private String filename;
	final private boolean csvSink;
	final private boolean xmlSink;
	final private long writeAfterElements;
	final private boolean append;
	private long elementsInCache;
	private boolean printMetadata;
	transient private StringBuffer writeCache;
	transient BufferedWriter out;
	Serializer serializer;

	private final ReentrantLock lock = new ReentrantLock();
	private DecimalFormat floatingFormatter;
	private DecimalFormat numberFormatter;
	private Character textSeperator = null;
	private char delimiter = ';';
	private boolean linenumbering = false;
	private long linenumber = 0;

	static Logger LOG = LoggerFactory.getLogger(FileSinkPO.class);

	public FileSinkPO(String filename, String sinkType, long writeAfterElements, boolean printMetadata, boolean append) {
		this.filename = filename;
		if ("CSV".equalsIgnoreCase(sinkType)) {
			csvSink = true;
			xmlSink = false;
		} else if ("XML".equalsIgnoreCase(sinkType)) {
			xmlSink = true;
			csvSink = false;
		} else {
			xmlSink = false;
			csvSink = false;
		}
		this.writeAfterElements = writeAfterElements;
		this.printMetadata = printMetadata;
		this.append = append;
	}

	public FileSinkPO(FileSinkPO fileSink) {
		this.filename = fileSink.filename;
		this.csvSink = fileSink.csvSink;
		this.xmlSink = fileSink.xmlSink;
		this.printMetadata = fileSink.printMetadata;
		this.writeAfterElements = fileSink.writeAfterElements;
		this.append = fileSink.append;
		this.linenumbering = fileSink.linenumbering;
	}

	public String getFilename() {
		return filename;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			lock.lock();
			writeCache = new StringBuffer();
			elementsInCache = 0;
			linenumber = 0;
			if (xmlSink) {
				serializer = new Persister();
			}
			LOG.debug("Trying to open "+filename);
			out = new BufferedWriter(new FileWriter(FileUtils.openOrCreateFile(filename), this.append));
			LOG.debug("Open "+filename+" success.");
		} catch (IOException e) {
			OpenFailedException ex = new OpenFailedException(e);
			ex.fillInStackTrace();
			throw ex;
		} finally{
			lock.unlock();
		}

	}

	@Override
	protected void process_next(IStreamObject<?> object, int port) {
		if (isOpen()) {
			try {
				linenumber++;
				if (xmlSink) {
					serializer.write(object, out);
					out.flush();
				} else {

					String toWrite = null;
					if (csvSink) {
						toWrite = ((ICSVToString) object).csvToString(delimiter,textSeperator,floatingFormatter, numberFormatter, printMetadata) + "\n";
						if(linenumbering){
							toWrite = Long.toString(linenumber)+delimiter+toWrite;
						}
					} else {
						toWrite = "" + object + "\n";
					}
					
					if (writeAfterElements > 0) {
						writeCache.append(toWrite);// .append("\n");
						elementsInCache++;
						if (writeAfterElements >= elementsInCache) {
							writeToFile(writeCache.toString());
							writeCache = new StringBuffer();
							elementsInCache = 0;
						}
					} else {
						writeToFile(toWrite);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void writeToFile(String elem) throws IOException {
		lock.lock();
		if (out != null) {
			out.write(elem);
			out.flush();

		}
		lock.unlock();
	}

	@Override
	protected void process_close() {

		if (isOpen()) {
			lock.lock();
			process_done(0);
			lock.unlock();

		}
	}

	@Override
	public FileSinkPO clone() {
		return new FileSinkPO(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof FileSinkPO)) {
			return false;
		}
		FileSinkPO fs = (FileSinkPO) ipo;
		if (this.getSubscribedToSource().get(0).equals(fs.getSubscribedToSource().get(0)) && this.filename.equals(fs.getFilename()) && this.csvSink == fs.csvSink) {
			return true;
		}
		return false;
	}

	@Override
	public void process_done(int port) {		
		if (isOpen()) {
			if (out != null) {
				LOG.debug("FileSinkPO finishing...");
				try {
					writeToFile(writeCache.toString());
					lock.lock();
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					lock.unlock();
				}
				LOG.debug("FileSinkPO.done");
			}
		}
	}

	public void setFormatter(DecimalFormat floatingFormatter,
			DecimalFormat numberFormatter) {
		if (floatingFormatter != null){
			this.floatingFormatter = floatingFormatter;
		}else{
			this.floatingFormatter = new DecimalFormat();
		}
		if (numberFormatter != null){
			this.numberFormatter = numberFormatter;
		}else{
			this.numberFormatter = new DecimalFormat();
		}
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}
	
	public void setTextSeperator(Character textSeperator) {
		this.textSeperator = textSeperator;
	}

	public void setLineNumbering(boolean lineNumbering) {
		this.linenumbering  = lineNumbering;		
	}
}
