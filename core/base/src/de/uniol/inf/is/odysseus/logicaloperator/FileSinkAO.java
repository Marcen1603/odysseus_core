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
package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FileSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5468128562016704956L;
	final String filename;
	final String sinkType;
	final long writeAfterElements;
	
	public FileSinkAO(String filename, String sinkType, long writeAfterElements) {
		this.filename = filename;
		this.sinkType = sinkType;
		this.writeAfterElements = writeAfterElements;
	}

	public FileSinkAO(FileSinkAO fileSinkAO) {
		super(fileSinkAO);
		this.filename = fileSinkAO.filename;
		this.sinkType = fileSinkAO.sinkType;
		this.writeAfterElements = fileSinkAO.writeAfterElements;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
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


}
