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

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSum;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

@SuppressWarnings({"unchecked","rawtypes"})
public class RelationalAvgSum extends AvgSum<Tuple<?>, Tuple<?>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7768784425424062403L;

	private int pos;

	static Map<Boolean, Map<Integer, RelationalAvgSum>> instances = new HashMap<Boolean, Map<Integer,RelationalAvgSum>>();

	static public RelationalAvgSum getInstance(int pos, boolean isAvg){
		Map<Integer, RelationalAvgSum> in = instances.get(isAvg);
		RelationalAvgSum ret;
		if (in==null){
			in = new HashMap<Integer, RelationalAvgSum>();
			instances.put(isAvg, in);
			ret = new RelationalAvgSum(pos, isAvg);
			in.put(pos, ret);
		}else{
			ret = in.get(pos);
			if (ret == null){
				ret = new RelationalAvgSum(pos, isAvg);
				in.put(pos,ret);
			}
		}
		return ret;
	}
	
	private RelationalAvgSum(int pos, boolean isAvg){
		super(isAvg);
		this.pos = pos;
	}
	

	
	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple in) {
		AvgSumPartialAggregate<Tuple<?>> pa = 
			new AvgSumPartialAggregate<Tuple<?>>(((Number)in.getAttribute(pos)).doubleValue(),1);
		return pa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate p, Tuple toMerge, boolean createNew) {
		AvgSumPartialAggregate<Tuple> pa = null;
		if (createNew){
			AvgSumPartialAggregate<Tuple> h = (AvgSumPartialAggregate<Tuple>) p;			
			pa = new AvgSumPartialAggregate<Tuple>(h.getAggValue(), h.getCount());
			
		}else{
			pa = (AvgSumPartialAggregate<Tuple>) p;
		}
		return merge(pa, toMerge);
	}
	
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate p, Tuple toMerge) {
		AvgSumPartialAggregate pa = (AvgSumPartialAggregate) p;
		Double newAggValue = pa.getAggValue().doubleValue() + ((Number)toMerge.getAttribute(pos)).doubleValue(); 
		pa.setAggValue(newAggValue, pa.getCount()+1);
		return pa;
	}
	
	@Override
	public Tuple evaluate(IPartialAggregate p) {
		AvgSumPartialAggregate pa = (AvgSumPartialAggregate) p;
		Tuple r = new Tuple(1);
		if (isAvg()){
			r.setAttribute(0, new Double(pa.getAggValue().doubleValue()/pa.getCount()));
		}else{
			r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
		}
		return r;
	}
	
}
