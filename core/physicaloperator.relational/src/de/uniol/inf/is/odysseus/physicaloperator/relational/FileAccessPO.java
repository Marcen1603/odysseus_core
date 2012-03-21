/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

/**
 * @author Kai Pancratz, Marco Grawunder
 * 
 */

public class FileAccessPO<T extends IMetaAttributeContainer<? extends IClone>>
		extends AbstractIterableSource<T> {

	private static final Logger LOG = LoggerFactory
			.getLogger(FileAccessPO.class);

	// Definition for the location and the type of file
	final private String path;
	final private String fileType;

	private boolean isDone = false;
	private BufferedReader bf;

	final private IDataHandler<?> dataHandler;

	final private String separator;

	public FileAccessPO(String path, String fileType, String separator,
			IDataHandler<?> dataHandler) {
		this.path = path;
		this.fileType = fileType;
		this.separator = separator;
		this.dataHandler = dataHandler;
	}

	@Override
	public synchronized boolean hasNext() {
		if (isDone || !isOpen()) {
			return false;
		}
		try {
			if (bf.ready()) {
				return true;
			}
		} catch (IOException e) {
			LOG.error("Exception during checking, if file " + path
					+ " has data left", e);
		}

		tryPropagateDone();
		return false;
	}

	private void tryPropagateDone() {
		try {
			propagateDone();
		} catch (Throwable throwable) {
			LOG.error("Exception during propagating done", throwable);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void transferNext() {
		if (isOpen() && !isDone()) {
			String line = "";
			try {
				line = bf.readLine();
				if (line != null && !(line).isEmpty()) {
					String[] splittedLine;
					if ("csv".equalsIgnoreCase(fileType)) {
						splittedLine = line.split(separator);
					} else {
						splittedLine = new String[1];
						splittedLine[0] = line;
					}
					transfer((T) dataHandler.readData(splittedLine));

				} else {
					isDone = true;
					propagateDone();
				}
			} catch (Exception e) {
				LOG.error("Exception during transfering next line", e);
				isDone = true;
				propagateDone();
			}
		}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {

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
	protected synchronized void process_close() {
		if (isOpen()) {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public AbstractSource<T> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof FileAccessPO)) {
			return false;
		}
		FileAccessPO fapo = (FileAccessPO) ipo;
		if (this.path.equals(fapo.path) && this.fileType.equals(fapo.fileType)) {
			return true;
		}
		return false;
	}

}
