/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.logicaloperator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This class represents a link to another logical Operators
 * @author Marco Grawunder
 *
 */

public class LogicalSubscription extends Subscription<ILogicalOperator> implements Serializable{

	private static final long serialVersionUID = 678442733825703258L;
	
	/**
	 * Create a new logical Subscription
	 * @param target What is the link target (could be a source or a sink!)
	 * @param sinkInPort The input port of the sink that is affected
	 * @param sourceOutPort The output port of the source that is affected
	 * @param schema The data schema of the elements that should be processed
	 */
	public LogicalSubscription(ILogicalOperator target, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		super(target, sinkInPort, sourceOutPort, schema);
	}
		
		
}
