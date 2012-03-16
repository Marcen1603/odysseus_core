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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.MinMax;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class RelationalMinMax extends MinMax<Tuple<?>,Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 571119114462961967L;

	int[] attrList = new int[1];

	static Map<Boolean, Map<Integer, RelationalMinMax>> instances = new HashMap<Boolean, Map<Integer,RelationalMinMax>>();

	static public RelationalMinMax getInstance(int pos, boolean isMax){
		Map<Integer, RelationalMinMax> in = instances.get(isMax);
		RelationalMinMax ret;
		if (in==null){
			in = new HashMap<Integer, RelationalMinMax>();
			instances.put(isMax, in);
			ret = new RelationalMinMax(pos, isMax);
			in.put(pos, ret);
		}else{
			ret = in.get(pos);
			if (ret == null){
				ret = new RelationalMinMax(pos, isMax);
				in.put(pos,ret);
			}
		}
		return ret;
	}


	private RelationalMinMax(int pos, boolean isMax) {
		super(isMax);
		attrList[0] = pos;
	}
	
	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		return super.init(in.restrict(attrList, true));
	}
	
	@Override
	public IPartialAggregate<Tuple<?>> merge(
			IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge,
			boolean createNew) {
		return super.merge(p, toMerge.restrict(attrList, true), createNew);
	}
	
}
