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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleDataHandler;

/**
 * @author Kai Pancratz, Marco Grawunder
 * 
 */

public class FileAccessPO<T extends IMetaAttributeContainer<? extends IClone>>
		extends AbstractIterableSource<T> {

	Logger logger = LoggerFactory.getLogger(FileAccessPO.class);

	// Definition for the location and the type of file
	final private String path;
	final private String fileType;
	
	private boolean isDone = false;
	private BufferedReader bf;

	private RelationalTupleDataHandler dataHandler;

	final private String separator;

	public FileAccessPO(String path, String fileType, String separator) {
		this.path = path;
		this.fileType = fileType;
		this.separator = separator;
	}

	@Override
	public synchronized boolean hasNext() {
		try {
			if (bf.ready())
				return true;
			else {
				propagateDone();
				return false;
			}
		} catch (Exception e) {
			propagateDone();
			e.printStackTrace();
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void transferNext() {
		if (isOpen()) {
			String line = "";
			try {

				if (!(line = bf.readLine()).isEmpty()) {
					String[] splittedLine = line.split(separator);
					transfer((T) dataHandler.readData(splittedLine));					
				} else {
					isDone = true;
					propagateDone();
				}
			} catch (Exception e) {
				isDone = true;
				propagateDone();
				e.printStackTrace();
			}
		}
	}



	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	protected void process_open() throws OpenFailedException {

		try {
			this.dataHandler = new RelationalTupleDataHandler(this.getOutputSchema());

			// logger.debug(fileType);
			if (fileType.equalsIgnoreCase("csv")) {
				File file = new File(path);
				bf = new BufferedReader(new FileReader(file));
				// logger.debug(path);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		} else {
			return false;
		}
	}

}
