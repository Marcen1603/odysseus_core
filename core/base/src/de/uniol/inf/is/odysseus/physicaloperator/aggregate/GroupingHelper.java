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
package de.uniol.inf.is.odysseus.physicaloperator.aggregate;

import de.uniol.inf.is.odysseus.collection.FESortedPair;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class GroupingHelper<R> {

    abstract public Integer getGroupID(R elem);

    abstract public void init();

    abstract public R createOutputElement(Integer groupID,
            PairMap<SDFAttributeList, AggregateFunction, R, ?> r);

    abstract public IInitializer<R> getInitAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p);

    abstract public IMerger<R> getMergerAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p);

    abstract public IEvaluator<R> getEvaluatorAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p);

}
