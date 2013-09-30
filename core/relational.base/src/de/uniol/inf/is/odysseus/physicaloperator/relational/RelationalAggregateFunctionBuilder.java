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

package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

public class RelationalAggregateFunctionBuilder implements
		IAggregateFunctionBuilder {
	
    private final static String LAST = "LAST";
    private final static String FIRST = "FIRST";
    private final static String NTH = "NTH";
    private final static String RATE = "RATE";
    
	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("AVG");
		names.add("SUM");
		names.add("COUNT");
		names.add("MIN");
		names.add("MAX");
		names.add("NEST");
		names.add("STDDEV");
        names.add(LAST);
        names.add(FIRST);
        names.add(NTH);
        names.add(RATE);
	}
	
	@Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(
			AggregateFunction key, int[] pos, boolean partialAggregateInput, String datatype) {
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		if ((key.getName().equalsIgnoreCase("AVG"))
				|| (key.getName().equalsIgnoreCase("SUM"))) {
			aggFunc = RelationalAvgSum.getInstance(pos[0],
					(key.getName().equalsIgnoreCase("AVG")) ? true : false, partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase("COUNT")) {
			aggFunc = RelationalCount.getInstance(pos[0],partialAggregateInput);
		} else if ((key.getName().equalsIgnoreCase("MIN"))
				|| (key.getName().equalsIgnoreCase("MAX"))) {
			aggFunc = RelationalMinMax.getInstance(pos[0],
					(key.getName().equalsIgnoreCase("MAX")) ? true : false, partialAggregateInput, datatype);
		}else if ((key.getName().equalsIgnoreCase("STDDEV"))){
			aggFunc = new RelationalStdDev(pos, partialAggregateInput);
		} else if ((key.getName().equalsIgnoreCase("NEST"))) {
			aggFunc = new RelationalNest(pos, partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(LAST)) {
			aggFunc = RelationalLast.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(FIRST)) {
			aggFunc = RelationalFirst.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(RATE)) {
			aggFunc = RelationalRate.getInstance(partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(NTH)) {
			aggFunc = RelationalNth.getInstance(Integer.parseInt(key
					.getProperty("nth")), partialAggregateInput);
		} else {
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
