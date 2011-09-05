/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.memory.relational;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.memory.ISnapshot;
import de.uniol.inf.is.odysseus.mining.memory.ISnapshotMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 05.09.2011
 */
public class RelationalTISnapshotMergeFunction implements ISnapshotMergeFunction<RelationalTuple<ITimeInterval>>{

	private RelationalMergeFunction<ITimeInterval> mergeFunction;
	
	public RelationalTISnapshotMergeFunction(int schemaSize){
		mergeFunction = new RelationalMergeFunction<ITimeInterval>(schemaSize);
	}
	
	
	@Override
	public ISnapshot<RelationalTuple<ITimeInterval>> merge(ISnapshot<RelationalTuple<ITimeInterval>> left, ISnapshot<RelationalTuple<ITimeInterval>> right) {
		RelationalTuple<ITimeInterval> merged = mergeFunction.merge(left.getValue(), right.getValue());
		RelationalTISnapshot newOne = new RelationalTISnapshot(merged);		
		return newOne;
	}

}
