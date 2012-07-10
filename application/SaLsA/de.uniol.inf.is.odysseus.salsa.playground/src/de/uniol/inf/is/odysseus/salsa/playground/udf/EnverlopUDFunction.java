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
package de.uniol.inf.is.odysseus.salsa.playground.udf;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

@UserDefinedFunction(name = "ENVERLOP")
public class EnverlopUDFunction implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	String init = null;

	@Override
	public void init(String initString) {
		init = initString;
	}

	@Override
	@SuppressWarnings("all")
	public Tuple<? extends IMetaAttribute> process(
			Tuple<? extends IMetaAttribute> in, int port) {
		Tuple<IMetaAttribute> intuple = (Tuple<IMetaAttribute>) in;
		
		for (int i = 0; i < ((List<Tuple<IMetaAttribute>>) intuple.getAttribute(0)).size(); i++) {
			Geometry geometry = (Geometry) ((Tuple) ((List<Tuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i)).getAttribute(0);
			//geometry = geometry.convexHull();
			((List<Tuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i).setAttribute(0, geometry.getEnvelope());
		
		}
		
		//intuple.setAttribute(0, mergetupleList);
		return intuple;
	}



	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
