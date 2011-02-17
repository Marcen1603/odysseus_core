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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Ist nur eine Hilfsklasse um den obersten Knoten eines Plans eindeutig
 * bestimmen zu koennen.
 * 
 * @author Marco Grawunder
 * 
 */
public class TopAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6533111765567598018L;
	transient private ISink<?> physicalInput;

	public TopAO(TopAO po) {
		super(po);
		this.physicalInput = po.physicalInput;
	}

	public TopAO() {
		super();
	}

	public @Override
	TopAO clone() {
		return new TopAO(this);
	}

	public void setPhysicalInputPO(ISink<?> physical) {
		this.physicalInput = physical;
	}

	public IPhysicalOperator getPhysicalInput() {
		if (physicalInput != null) {
			return physicalInput;
		}
		IPhysicalOperator ret = null;
		Iterator<Subscription<ISource<?>>> iter = getPhysSubscriptionsTo().iterator();
		if (iter.hasNext()){
			Subscription<ISource<?>> s = iter.next();
			if (s!=null){
				ret = s.getTarget();
			}
		}
		return ret;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

}
