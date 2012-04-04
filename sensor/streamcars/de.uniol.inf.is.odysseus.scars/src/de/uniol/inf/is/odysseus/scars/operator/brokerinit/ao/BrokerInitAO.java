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
package de.uniol.inf.is.odysseus.scars.operator.brokerinit.ao;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

public class BrokerInitAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private int size = 1;
	
	public BrokerInitAO() {
		
	}
	
	public BrokerInitAO( BrokerInitAO other ) {
		setSize( other.getSize() );
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new BrokerInitAO(this);
	}

	public void setSize( int size ) {
		if( size < 1 ) throw new IllegalArgumentException("size of BrokerInit must be positive");
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
}
