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
package de.uniol.inf.is.odysseus.wrapper.ivef.conversion.logicaloperator;

//import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
//import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IvefMsgParameter;
//import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NmeaMsgParameter;
//import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.wrapper.ivef.IIvefElement;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.Sentence;

/**
 * @author msalous
 */

@LogicalOperator(name="IVEFNMEACONVERTER", minInputPorts=1, maxInputPorts=1, doc="This operator is used to convert Ivef messages into Nmea messages and vice versa.",category={LogicalOperatorCategory.PROCESSING})
public class IvefNmeaConverterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8015847502104587689L; 
	
	private IIvefElement ivef;
	private boolean directionIvefToNmea;
	private Sentence nmea;
		
	public IvefNmeaConverterAO(){
		super();
	}

	public IvefNmeaConverterAO(IvefNmeaConverterAO converterAO){ 
		super(converterAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new IvefNmeaConverterAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return super.getOutputSchemaIntern(0);
	}
	
//	@Parameter(name="ivef", type=IvefMsgParameter.class, isList=false, optional=true, doc="The Ivef message to be converted into nmea")
//	public void setIvef(IIvefElement ivef) {
//		this.ivef = ivef;
//	}
//	
//	@Parameter(name="nmea", type=NmeaMsgParameter.class, isList=false, optional=true, doc="The nmea message to be converted into ivef")
//	public void setNmea(Sentence nmea) {
//		this.nmea = nmea;
//	}
//	
//	@Parameter(name="ivefToNmea", type=BooleanParameter.class, isList=false, optional=false, doc="this parameter determines the direction of the cenvesion, from ivef to nmea or from nmea to ivef")
//	public void setIvefToNmea(boolean ivefToNmea) { 
//		this.ivefToNmea = ivefToNmea;
//	}
	
	public IIvefElement getIvef() {
		return this.ivef;
	}
	
	public Sentence getNmea() {
		return this.nmea;
	}
	
	public boolean isDirectionIvefToNmea() {
		return this.directionIvefToNmea;
	}
}
