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

package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalAggregateFunctionBuilder implements
		IAggregateFunctionBuilder {

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add("AVG");
		names.add("SUM");
		names.add("COUNT");
		names.add("MIN");
		names.add("MAX");
		names.add("NEST");
		names.add("BEAN");
		names.add("SCRIPT");
	}
	
	public IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> createAggFunction(
			AggregateFunction key, int[] pos) {
		IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
		if ((key.getName().equalsIgnoreCase("AVG"))
				|| (key.getName().equalsIgnoreCase("SUM"))) {
			aggFunc = RelationalAvgSum.getInstance(pos[0],
					(key.getName().equalsIgnoreCase("AVG")) ? true : false);
		} else if (key.getName().equalsIgnoreCase("COUNT")) {
			aggFunc = RelationalCount.getInstance();
		} else if ((key.getName().equalsIgnoreCase("MIN"))
				|| (key.getName().equalsIgnoreCase("MAX"))) {
			aggFunc = RelationalMinMax.getInstance(pos[0],
					(key.getName().equalsIgnoreCase("MAX")) ? true : false);
		} else if ((key.getName().equalsIgnoreCase("NEST"))) {
			aggFunc = new RelationalNest(pos);
		} else if (key.getName().equalsIgnoreCase("BEAN")) {
			aggFunc = new AggregationBean(pos, key.getProperty("resource"));
		} else if (key.getName().equalsIgnoreCase("SCRIPT")) {
			aggFunc = new AggregationJSR223(pos, key.getProperty("resource"));
		} else {
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
