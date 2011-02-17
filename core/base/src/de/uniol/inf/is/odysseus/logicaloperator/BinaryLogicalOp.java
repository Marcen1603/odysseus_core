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



public abstract class BinaryLogicalOp extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1558576598140153762L;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public BinaryLogicalOp(AbstractLogicalOperator po) {
		super(po);
	}

	public BinaryLogicalOp() {
		super();
	}
	
	public ILogicalOperator getLeftInput(){
		return getSubscribedToSource(LEFT).getTarget();
	}
	
	public ILogicalOperator getRightInput(){
		return getSubscribedToSource(RIGHT).getTarget();
	}

//	public void setLeftInput(ILogicalOperator source, SDFAttributeList inputSchema) {
//		subscribeTo(source, LEFT, 0, inputSchema);
//	}
//
//	public void setRightInput(ILogicalOperator source, SDFAttributeList inputSchema) {
//		subscribeTo(source, RIGHT, 0, inputSchema);
//	}	
	
//	private ISource<?> getLeftPhysInput(){
//		return getPhysSubscriptionTo(LEFT)==null?null:getPhysSubscriptionTo(LEFT).getTarget();
//	}
//
//	private ISource<?> getRightPhysInput(){
//		return getPhysSubscriptionTo(RIGHT)==null?null:getPhysSubscriptionTo(RIGHT).getTarget();
//	}
}
