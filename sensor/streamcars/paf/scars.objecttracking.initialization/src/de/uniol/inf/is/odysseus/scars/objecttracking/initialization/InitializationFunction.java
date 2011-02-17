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
/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.initialization;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

/**
 * @author dtwumasi
 * @param <M>
 * 
 */
public class InitializationFunction<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer> extends AbstractInitializationFunction<M> {

	public InitializationFunction(AbstractInitializationFunction<M> copy) {
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));
	}

	public InitializationFunction() {

	}

	@Override
	public MVRelationalTuple<M> compute(MVRelationalTuple<M> object, SchemaIndexPath newObjectListPath, SchemaIndexPath oldObjectListPath) {

		TupleIndexPath newTupleIndexPath = newObjectListPath.toTupleIndexPath(object);
		TupleIndexPath oldTupleIndexPath = oldObjectListPath.toTupleIndexPath(object);
		TupleHelper tHelper = new TupleHelper(object);

		MVRelationalTuple<M> newList = (MVRelationalTuple<M>) tHelper.getObject(newObjectListPath);
		MVRelationalTuple<M> copy = newList.clone();

		TupleHelper tHelper2 = new TupleHelper(copy);

		ConnectionList newObjConList = new ConnectionList();
		
		for (int i = 0; i <= newList.getAttributeCount() - 1; i++) {

			TupleIndexPath newConPath = newTupleIndexPath.appendClone(i);
			TupleIndexPath oldConPath = oldTupleIndexPath.appendClone(i);
			Connection con = new Connection(newConPath, oldConPath, 5.0);
			newObjConList.add(con);
			copy.setAttribute(i, this.getParameters().get(Parameters.InitializationTuple));

		}

		object.getMetadata().setConnectionList(newObjConList);
		oldTupleIndexPath.setTupleObject(copy);

		return object;
	}

	@Override
	public AbstractInitializationFunction clone() {
		return new InitializationFunction(this);
	}

}
