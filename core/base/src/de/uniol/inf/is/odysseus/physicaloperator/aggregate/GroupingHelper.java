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

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class GroupingHelper<R, W extends IClone> {

    abstract public Integer getGroupID(R elem);

    abstract public void init();

    abstract public W createOutputElement(Integer groupID,
            PairMap<SDFAttributeList, AggregateFunction, W, ?> r);

    abstract public IInitializer<R> getInitAggFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p);

    abstract public IMerger<R> getMergerAggFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p);

    abstract public IEvaluator<R,W> getEvaluatorAggFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p);

}
