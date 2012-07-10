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

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fusion.aggregate.function.spatialfusion.RelationalPolygonAggregation;
import de.uniol.inf.is.odysseus.fusion.aggregate.function.tracking.TrackingAggregation;

public class AggregateFunctionProvider implements IAggregateFunctionBuilder {

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("L1Fusion");
		names.add("Tracker");
	}
	
	@Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(AggregateFunction key, int[] pos) {
		
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		
		if(key.getName().equalsIgnoreCase("L1Fusion")) {
			aggFunc = new RelationalPolygonAggregation<Tuple<?>, Tuple<?>>(pos);
		}  
		if(key.getName().equalsIgnoreCase("Tracker")) {
			aggFunc = new TrackingAggregation<Tuple<?>, Tuple<?>>(pos);
		}  
		
		
		if(aggFunc == null) {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		
		return aggFunc;
	}

	@Override
	public String getDatamodel() {
		return "relational";
	}

	@Override
	public Collection<String> getFunctionNames() {
		return names;
	}

}
