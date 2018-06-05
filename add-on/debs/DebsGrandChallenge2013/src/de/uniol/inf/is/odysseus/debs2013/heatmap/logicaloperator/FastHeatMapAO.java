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
/*
* Created on 07.12.2004
*
*/
package de.uniol.inf.is.odysseus.debs2013.heatmap.logicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
* @author Andreas Harre, Philipp Rudolph, Jan Sören Schwarz
* 
*/
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="FASTHEATMAP", doc="Optimized heat map operator for grand challenge 2013.", category={LogicalOperatorCategory.ADVANCED})
public class FastHeatMapAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 3215936185841514846L;
	private int x;
	private int y;
	private int xLength;
	private int yLength;
	private String xAttribute;
	private String yAttribute;
	private String valueAttribute;

	public FastHeatMapAO() {
		super();
		this.initOutputSchema();
	}

	public FastHeatMapAO(FastHeatMapAO po) {
		super(po);
		this.x = po.x;
		this.y = po.y;
		this.xLength = po.xLength;
		this.yLength = po.yLength;
		this.xAttribute = po.xAttribute;
		this.yAttribute = po.yAttribute;
		this.valueAttribute = po.valueAttribute;
		this.initOutputSchema();
	}

	@Parameter(type = IntegerParameter.class, name = "x", optional=false)
	public void setX(int x){
		this.x = x;
	}

	@Parameter(type = IntegerParameter.class, name = "y", optional=false)
	public void setY(int y){
		this.y = y;
	}

	@Parameter(type = IntegerParameter.class, name = "xLength", optional=false)
	public void setXLength(int xLength){
		this.xLength = xLength;
	}

	@Parameter(type = IntegerParameter.class, name = "yLength", optional=false)
	public void setYLength(int yLength){
		this.yLength = yLength;
	}

	@Parameter(type = StringParameter.class, name = "xAttribute", optional=false)
	public void setXAttribute(String xAttribute){
		this.xAttribute = xAttribute;
	}

	@Parameter(type = StringParameter.class, name = "yAttribute", optional=false)
	public void setYAttribute(String yAttribute){
		this.yAttribute = yAttribute;
	}

	@Parameter(type = StringParameter.class, name = "valueAttribute", optional=false)
	public void setValueAttribute(String valueAttribute){
		this.valueAttribute = valueAttribute;
	}
	
	@Override
	public FastHeatMapAO clone() {
		return new FastHeatMapAO(this);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getXLength() {
		return xLength;
	}

	public int getYLength() {
		return yLength;
	}
	
	public String getXAttribute() {
		return xAttribute;
	}

	public String getYAttribute() {
		return yAttribute;
	}

	public String getValueAttribute() {
		return valueAttribute;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
		if (recalcOutputSchemata || getOutputSchema() == null) {
			initOutputSchema();
		}
		return getOutputSchema();
	}

	
	@SuppressWarnings("unchecked")
	public void initOutputSchema() {
		
		@SuppressWarnings("rawtypes")
		ArrayList attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("", "ts", new SDFDatatype("Long"), null, null, null));
		attributes.add(new SDFAttribute("", "player_id", new SDFDatatype("Integer"), null, null, null));
		String name = "";
		for(int i = 0; i < this.x; i++) {
			for(int j = 0; j < this.y; j++) {
				name = "cell_" + this.x + "x" + this.y + "_x1";
				attributes.add(new SDFAttribute("", name, new SDFDatatype("Integer"), null, null, null));
				name = "cell_" + this.x + "x" + this.y + "_y1";
				attributes.add(new SDFAttribute("", name, new SDFDatatype("Integer"), null, null, null));
				name = "cell_" + this.x + "x" + this.y + "_x2";
				attributes.add(new SDFAttribute("", name, new SDFDatatype("Integer"), null, null, null));
				name = "cell_" + this.x + "x" + this.y + "_y2";
				attributes.add(new SDFAttribute("", name, new SDFDatatype("Integer"), null, null, null));
				name = "time_" + this.x + "x" + this.y;
				attributes.add(new SDFAttribute("", name, new SDFDatatype("Double"), null, null, null));
			}
		}
		SDFSchema grandChallengeSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema());
//		SDFSchema grandChallengeSchema = new SDFSchema(schemaURI, 
//				new SDFAttribute("", "ts", new SDFDatatype("Long")), 
//				new SDFAttribute("", "player_id", new SDFDatatype("Integer")),
//				new SDFAttribute("", "cell_x1", new SDFDatatype("Integer")),
//				new SDFAttribute("", "cell_y1", new SDFDatatype("Integer")),
//				new SDFAttribute("", "cell_x2", new SDFDatatype("Integer")),
//				new SDFAttribute("", "cell_y2", new SDFDatatype("Integer")),
//				new SDFAttribute("", "time", new SDFDatatype("Double")));
		this.setOutputSchema(grandChallengeSchema);
	}
}