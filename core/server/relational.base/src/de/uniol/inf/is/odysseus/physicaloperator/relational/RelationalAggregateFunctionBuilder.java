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
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AbstractAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalAttributeCount;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalAvgSum;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalCorr;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalCount;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalCov;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalDistinctNest;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalFirst;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalGreenwaldKhannaMedian;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalLast;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalMedian;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalMinMax;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalNest;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalNth;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalStdDev;
import de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate.RelationalVar;

public class RelationalAggregateFunctionBuilder extends AbstractAggregateFunctionBuilder {
	
    private final static String AVG = "AVG";
    private final static String SUM = "SUM";
    private final static String COUNT = "COUNT";
    private final static String MIN = "MIN";
    private final static String MAX = "MAX";
    private final static String NEST = "NEST";
    private final static String DISTINCTNEST = "DISTINCTNEST";
    private final static String STDDEV = "STDDEV";
    private final static String CORR = "CORR";
    private final static String COV = "COV";
    private final static String VAR = "VAR";
    private final static String LAST = "LAST";
    private final static String FIRST = "FIRST";
    private final static String NTH = "NTH";
    private final static String RATE = "RATE";
    private final static String COMPLETENESS = "COMPLETENESS";
    private final static String MEDIAN = "MEDIAN";
    private final static String AMEDIAN = "AMEDIAN";
    
    private final static int[] dummy = {-1};
    
	private static Collection<String> names = new LinkedList<String>();
	{
		names.add(AVG);
		names.add(SUM);
		names.add(COUNT);
		names.add(MIN);
		names.add(MAX);
		names.add(NEST);
		names.add(DISTINCTNEST);
		names.add(STDDEV);
        names.add(CORR);
        names.add(COV);
        names.add(VAR);
        names.add(LAST);
        names.add(FIRST);
        names.add(NTH);
        names.add(RATE);
        names.add(COMPLETENESS);
        names.add(MEDIAN);
        names.add(AMEDIAN);
	}
	
	@Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(
			AggregateFunction key, SDFSchema schema, int[] pos, boolean partialAggregateInput, String datatype) {
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		if ((key.getName().equalsIgnoreCase(AVG))
				|| (key.getName().equalsIgnoreCase(SUM))) {
			aggFunc = RelationalAvgSum.getInstance(pos[0],
					(key.getName().equalsIgnoreCase(AVG)) ? true : false, partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(COUNT)) {
			if (pos.length == 1){
				aggFunc = RelationalAttributeCount.getInstance(pos[0],partialAggregateInput);
			}else{
				aggFunc = RelationalCount.getInstance(partialAggregateInput);
			}
		} else if ((key.getName().equalsIgnoreCase(MIN))
				|| (key.getName().equalsIgnoreCase(MAX))) {
			aggFunc = RelationalMinMax.getInstance(pos[0],
					(key.getName().equalsIgnoreCase(MAX)) ? true : false, partialAggregateInput, datatype);
		}else if ((key.getName().equalsIgnoreCase(STDDEV))){
			aggFunc = RelationalStdDev.getInstance(pos[0], partialAggregateInput);
        } else if ((key.getName().equalsIgnoreCase(CORR))) {
            aggFunc = RelationalCorr.getInstance(pos[0], pos[1], partialAggregateInput);
        } else if ((key.getName().equalsIgnoreCase(COV))) {
            aggFunc = RelationalCov.getInstance(pos[0], pos[1], partialAggregateInput);
        } else if ((key.getName().equalsIgnoreCase(VAR))) {
            aggFunc = RelationalVar.getInstance(pos[0], partialAggregateInput);		
        } else if ((key.getName().equalsIgnoreCase(NEST))) {
			aggFunc = new RelationalNest(pos, partialAggregateInput);
        } else if ((key.getName().equalsIgnoreCase(DISTINCTNEST))) {
        	aggFunc = new RelationalDistinctNest(pos, partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(LAST)) {
			aggFunc = RelationalLast.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(FIRST)) {
			aggFunc = RelationalFirst.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(RATE)) {
			aggFunc = RelationalRate.getInstance(partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(NTH)) {
			aggFunc = RelationalNth.getInstance(Integer.parseInt(key
					.getProperty("nth")), partialAggregateInput);
        } else if (key.getName().equalsIgnoreCase(COMPLETENESS)) {
            aggFunc = RelationalCompleteness.getInstance(pos[0], partialAggregateInput, datatype);
        }else if (key.getName().equalsIgnoreCase(MEDIAN)) {
            aggFunc = RelationalMedian.getInstance(pos[0], partialAggregateInput);
        }else if (key.getName().equalsIgnoreCase(AMEDIAN)) {
            aggFunc = RelationalGreenwaldKhannaMedian.getInstance(pos[0], partialAggregateInput);
        } else {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		return aggFunc;
	}
	
	@Override
	public SDFDatatype getDatatype(String functionName, List<SDFAttribute> input) {
		// TODO: This is terrible! Two places where to compare ...
		// Maybe the aggregatefunctions should be created similar to the mep functions!
		if (functionName.equalsIgnoreCase(NEST)){
			RelationalNest n = new RelationalNest(dummy,false);
			return n.getReturnType(input);
		}
		if (functionName.equalsIgnoreCase(DISTINCTNEST)){
			RelationalDistinctNest n = new RelationalDistinctNest(dummy,false);
			return n.getReturnType(input);
		}

		if (functionName.equalsIgnoreCase(MIN) || functionName.equalsIgnoreCase(MAX)){
			return RelationalMinMax.getInstance(0, false, false, null).getReturnType(input);
		}
		if (functionName.equalsIgnoreCase(COUNT)){
			return RelationalCount.getInstance(false).getReturnType(input);
		}
		return null;
		
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
