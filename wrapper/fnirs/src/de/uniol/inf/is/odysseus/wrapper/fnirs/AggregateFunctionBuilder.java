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

package de.uniol.inf.is.odysseus.wrapper.fnirs;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.wrapper.fnirs.physicaloperator.RelationalTest;

public class AggregateFunctionBuilder implements
		IAggregateFunctionBuilder {
	
    private final static String TEST = "TEST";
    
	private static Collection<String> names = new LinkedList<String>();
	{
		names.add(TEST);
	}
	
	@Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(AggregateFunction key, SDFSchema schema, int[] pos, boolean partialAggregateInput, String datatype) 
	{
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		
		if (key.getName().equalsIgnoreCase(TEST)) 
		{
			aggFunc = RelationalTest.getInstance(pos[0],partialAggregateInput);
		} 
		else  
		{
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		return aggFunc;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends IStreamObject> getDatamodel() {
		return Tuple.class;
	}

	@Override
	public Collection<String> getFunctionNames() {
		return names;
	}
	
}
