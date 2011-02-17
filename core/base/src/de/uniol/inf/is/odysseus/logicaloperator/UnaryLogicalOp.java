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

import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */

public abstract class UnaryLogicalOp extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	private static final int PORTNUMBER = 0;

	public UnaryLogicalOp(AbstractLogicalOperator po) {
		super(po);
	}

	public UnaryLogicalOp() {
		super();
	}

	public SDFAttributeList getInputSchema() {
		return getInputSchema(PORTNUMBER);
	}

//	public void setInputSchema(SDFAttributeList schema) {
//		setInputSchema(PORTNUMBER, schema);
//	}

	public ILogicalOperator getInputAO(){
		return getSubscribedToSource(PORTNUMBER)==null?null:getSubscribedToSource(PORTNUMBER).getTarget();
	}
	
	public Subscription<ISource<?>> getPhysSubscriptionTo() {
		return getPhysSubscriptionTo(PORTNUMBER);
	}
	
	public void subscribeTo(ILogicalOperator source, SDFAttributeList inputSchema){
		subscribeToSource(source, 0, 0, inputSchema);
	}
		
	@Override
	public void subscribeToSource(ILogicalOperator source, int sinkInPort,
			int sourceOutPort, SDFAttributeList inputSchema) {
		if (sinkInPort != 0) {
			throw new IllegalArgumentException("illegal sink port for subscription in unary operatore: " + sinkInPort);
		}
		super.subscribeToSource(source, sinkInPort, sourceOutPort, inputSchema);
	}
	
	public LogicalSubscription getSubscription() {
		return getSubscriptions().iterator().next();
	}
}
