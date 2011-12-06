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

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.access.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.SetDataHandler;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleDataHandler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Kai Pancratz, Marco Grawunder
 * 
 */

public class FileAccessPO<T extends IMetaAttributeContainer<? extends IClone>>
		extends AbstractIterableSource<T> {

	Logger logger = LoggerFactory.getLogger(FileAccessPO.class);

	// Definition for the location and the type of file
	private String path;
	private String fileType;
	
	private boolean isDone = false;
	private BufferedReader bf;

	private IAtomicDataHandler[] dataHandlers;

	public FileAccessPO(String path, String fileType) {
		this.path = path;
		this.fileType = fileType;
	}

	public FileAccessPO(String path, String fileType, long delay) {
		this.path = path;
		this.fileType = fileType;
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
					String[] splittedLine = line.split(";");
					Object[] tuple = new Object[splittedLine.length];

					for (int i = 0; i < this.getOutputSchema().size(); i++) {

						tuple[i] = dataHandlers[i].readData(splittedLine[i]);

					}
					transfer((T) (new RelationalTuple<IMetaAttribute>(tuple)));
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

	// TODO: Der folgende Code taucht mehrfach auf .. siehe auch RelationalTupleDataHandler
	private void createDataReader() {
		SDFAttributeList schema = this.getOutputSchema();
		this.dataHandlers = new IAtomicDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {

			SDFDatatype type = attribute.getDatatype();

			if (type.isBase() || type.isBean()) {
				SDFDatatype datatype = attribute.getDatatype();
				String uri = datatype.getURI(false);
				IAtomicDataHandler handler = DataHandlerRegistry
						.getDataHandler(uri);

				if (handler == null) {
					throw new RuntimeException("illegal datatype " + uri);
				}

				this.dataHandlers[i++] = handler;
			} else if (type.isTuple()) {
				RelationalTupleDataHandler handler = new RelationalTupleDataHandler(
						type.getSubSchema());
				this.dataHandlers[i++] = handler;
			} else if (type.isMultiValue()) {
				SetDataHandler handler = new SetDataHandler(type.getSubType());
				this.dataHandlers[i++] = handler;
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
			createDataReader();
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
