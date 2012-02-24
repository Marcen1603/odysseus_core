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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "FILESINK", minInputPorts = 1, maxInputPorts = Integer.MAX_VALUE)
public class FileSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5468128562016704956L;
	String filename;
	
	/**
	 * CSV = write data in csv format to file
	 * nothing = write data in raw format to file
	 */
	String sinkType;
	
	/**
	 * describes how many elements
	 * are cached before they are
	 * written to file. Writing
	 * each element to file will
	 * decrease odysseus' performance
	 * very much.
	 */
	long writeAfterElements = -1;
	
	private boolean printMetadata;

	public FileSinkAO() {
	}

	public FileSinkAO(String filename, String sinkType, long writeAfterElements, boolean printMetadata) {
		this.filename = filename;
		this.sinkType = sinkType;
		this.printMetadata = printMetadata;
		this.writeAfterElements = writeAfterElements;
	}

	public FileSinkAO(FileSinkAO fileSinkAO) {
		super(fileSinkAO);
		this.filename = fileSinkAO.filename;
		this.sinkType = fileSinkAO.sinkType;
		this.printMetadata = fileSinkAO.printMetadata;
		this.writeAfterElements = fileSinkAO.writeAfterElements;
	}

	@Parameter(name = "FILE", type = StringParameter.class)
	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Parameter(name = "FILETYPE", type = StringParameter.class, optional = true)
	public void setSinkType(String sinkType) {
		this.sinkType = sinkType;
	}

	@Parameter(type = LongParameter.class, optional = true)
	public void setCacheSize(long value) {
		this.writeAfterElements = value;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return null;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FileSinkAO(this);
	}

	public String getFilename() {
		return filename;
	}

	public String getSinkType() {
		return sinkType;
	}

	public long getWriteAfterElements() {
		return writeAfterElements;
	}
	
	public boolean getPrintMetadata(){
		return this.printMetadata;
	}

}
