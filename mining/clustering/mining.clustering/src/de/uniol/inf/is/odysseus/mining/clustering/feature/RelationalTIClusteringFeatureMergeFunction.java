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

package de.uniol.inf.is.odysseus.mining.clustering.feature;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.memory.ISnapshotMergeFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 06.09.2011
 */
public class RelationalTIClusteringFeatureMergeFunction<M extends ITimeInterval> implements ISnapshotMergeFunction<ClusteringFeature<Tuple<ITimeInterval>>> {

	@Override
	public ClusteringFeature<Tuple<ITimeInterval>> merge(ClusteringFeature<Tuple<ITimeInterval>> left, ClusteringFeature<Tuple<ITimeInterval>> right) {
		return left.merge(right);		
	}

}
