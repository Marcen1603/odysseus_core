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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.wrapper.ivef.IVEFVersion;

/**
 * @author msalous
 */

@LogicalOperator(name="IVEFNMEACONVERTER", minInputPorts=1, maxInputPorts=1, doc="This operator is used to convert Ivef messages into Nmea messages and vice versa.",category={LogicalOperatorCategory.PROCESSING})
public class IvefNmeaConverterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8015847502104587689L; 
	
	private ConversionType conversionType;
	private int positionToStaticRatio = 100;

	private IVEFVersion ivefVersion = IVEFVersion.V015;
		
	public IvefNmeaConverterAO(){
		super();
	}

	public IvefNmeaConverterAO(IvefNmeaConverterAO converterAO){ 
		super(converterAO);
		this.conversionType = converterAO.getConversionType();
		this.ivefVersion = converterAO.getIvefVersion();
		this.positionToStaticRatio = converterAO.getPositionToStaticRatio();
		
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new IvefNmeaConverterAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return super.getOutputSchemaIntern(0);
	}
	
	@Parameter(name="PositionToStaticRatio", type=IntegerParameter.class, isList=false, optional=true, doc="The number of position messages the operator should wait iteratively before generating a new Static&Voyage message.")
	public void setPositionToStaticRatio(int PositionToStaticRatio) {
		this.positionToStaticRatio = PositionToStaticRatio; 
	} 
	
	@Parameter(name="conversionType", type=EnumParameter.class, isList=false, optional=false, doc="The conversion type between Maritime messages: AIS_To_IVEF, IVEF_To_AIS, TTM_To_IVEF, IVEF_To_TTM")
	public void setConversionType(ConversionType conversionType) {
		this.conversionType = conversionType; 
	} 
	
	@Parameter(name="ivefVersion", type=EnumParameter.class, isList=false, optional=true, doc="The version of IVEF elements: v015 (0.1.5), v025 (0.2.5)")
	public void setIVEFVersion(IVEFVersion ivefVersion) {
		this.ivefVersion= ivefVersion; 
	} 
	
	public ConversionType getConversionType(){
		return this.conversionType;
	}
	
	public int getPositionToStaticRatio(){
		return this.positionToStaticRatio;
	}

	public IVEFVersion getIvefVersion() {
		return ivefVersion;
	}
}
