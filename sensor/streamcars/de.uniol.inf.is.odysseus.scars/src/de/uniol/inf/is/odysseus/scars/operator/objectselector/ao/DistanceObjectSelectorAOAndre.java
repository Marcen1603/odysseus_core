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
package de.uniol.inf.is.odysseus.scars.operator.objectselector.ao;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

@SuppressWarnings("rawtypes")
public class DistanceObjectSelectorAOAndre extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private IAttributeResolver attrRes;
	private String trackedObjectList;
	private HashMap<IPredicate, SDFExpression> solutions;

	public DistanceObjectSelectorAOAndre(IAttributeResolver attrRes) {
		this.attrRes = attrRes;
	}

	public DistanceObjectSelectorAOAndre(DistanceObjectSelectorAOAndre distanceObjectSelectorAO) {
		super(distanceObjectSelectorAO);
		this.trackedObjectList = distanceObjectSelectorAO.trackedObjectList;
		this.attrRes = distanceObjectSelectorAO.attrRes;
		this.solutions = distanceObjectSelectorAO.solutions;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public DistanceObjectSelectorAOAndre clone() {
		return new DistanceObjectSelectorAOAndre(this);
	}

	public String getTrackedObjectList() {
		return trackedObjectList;
	}

	public void setTrackedObjectList(String trackedObjectList) {
		this.trackedObjectList = trackedObjectList;
	}
	
	public void setSolutions(HashMap<IPredicate, SDFExpression> sols){
		this.solutions = sols;
	}
	
	public HashMap<IPredicate, SDFExpression> getSolutions(){
		return this.solutions;
	}

}
