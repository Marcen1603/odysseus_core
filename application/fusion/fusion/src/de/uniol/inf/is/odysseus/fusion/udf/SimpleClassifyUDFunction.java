/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.fusion.udf;

import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

@UserDefinedFunction(name="Classify")
public class SimpleClassifyUDFunction implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	String init = null;
	
	@Override
	public void init(String initString) {
		init = initString;
	}

	@Override
	public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
		Polygon polygon = (Polygon)in.getAttribute(0); 
		
		Tuple<? extends IMetaAttribute> tuple = new Tuple<IMetaAttribute>(in.size()+1, false);
		
		
		tuple.setAttribute(0,polygon);
		tuple.setAttribute(in.size(), "DuNo!");
		
		double area = polygon.getArea();
		
		if(area > 260 && area < 2000){
			tuple.setAttribute(1, "Human");
		}
		
		if(area > 3000){
			tuple.setAttribute(1, "Scooter");
		}
		
		return tuple;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
