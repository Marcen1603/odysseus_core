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
package de.uniol.inf.is.odysseus.fusion.provider;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.fusion.function.classification.ShapeClassify;
import de.uniol.inf.is.odysseus.fusion.function.extrapolation.SmoothPolygon;
import de.uniol.inf.is.odysseus.fusion.function.tracking.LPPrediction;
import de.uniol.inf.is.odysseus.fusion.function.tracking.LPTracking;

public class FunctionProvider implements IFunctionProvider{

	@Override
	public List<IFunction<?>> getFunctions() {
		
		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		functions.add(new ShapeClassify());
		functions.add(new SmoothPolygon());
		functions.add(new LPPrediction());
		functions.add(new LPTracking());
		return functions;
	}

}
