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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FileAccessAO extends AbstractLogicalOperator implements
		OutputSchemaSettable {

	private static final long serialVersionUID = 3955519214402695311L;

	private SDFSource source = null;
	private SDFAttributeList outputSchema;

	private String path;
	private String fileType;
	private long delay;

	// This variable will be used to generate an ID for every new input tuple.
	private static long ID = 1;

	// This variable will be used, if a wildcard is necessary for an id
	private static Long wildcard = Long.valueOf(-1);

	public FileAccessAO() {
		super();
	}

	public FileAccessAO(AbstractLogicalOperator po) {
		super(po);
	}

	public FileAccessAO(SDFSource source) {
		this.source = source;
	}

	public FileAccessAO(FileAccessAO po) {
		super(po);
		this.source = po.source;
		this.path = po.path;
		this.fileType = po.fileType;
		this.delay = po.delay;
		this.outputSchema = po.outputSchema.clone();
	}

	public SDFSource getSource() {
		return source;
	}

	public void setSource(SDFSource source) {
		this.source = source;
	}

	@Override
	@Parameter(name = "ATTRIBUTES", type = CreateSDFAttributeParameter.class, isList = true)
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	public FileAccessAO clone() {
		return new FileAccessAO(this);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public String getSourceType() {
		return this.source.getSourceType();
	}

	private static long genID() {
		return ++ID;
	}

	public static List<Long> nextID() {
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(Long.valueOf(genID()));
		return idList;
	}

	public static Long getWildcard() {
		return wildcard;
	}

	@Override
	public String toString() {
		return getName() + " (" + this.getSource().getURI() + " | "
				+ this.getSourceType() + ")";
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		return true;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		if (port == 0) {
			setOutputSchema(outputSchema);
		} else {
			throw new IllegalArgumentException("no such port: " + port);
		}

	}
}
