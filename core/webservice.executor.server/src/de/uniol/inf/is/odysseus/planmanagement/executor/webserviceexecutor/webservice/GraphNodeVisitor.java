/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;
import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response.GraphNode;

/**
 * 
 * @author Dennis Geesen
 * Created at: 12.08.2011
 */
public class GraphNodeVisitor<T extends IPhysicalOperator> implements IGraphNodeVisitor<T,GraphNode> {

	private Map<T, GraphNode> mappings = new HashMap<T, GraphNode>();
	private GraphNode rootNode;
	private int idCounter = 0;
	
	
	public int getIdCounter() {
		return idCounter;
	}

	public void setIdCounter(int idCounter) {
		this.idCounter = idCounter;
	}

	@Override
	public void nodeAction(T node) {		
		if(!this.mappings.containsKey(node)){
			GraphNode gn = new GraphNode(node.getName()); 
			gn.setId(idCounter);
			idCounter++;
			this.mappings.put(node, gn);
			if(this.rootNode==null){
				this.rootNode = gn;
			}
		}
		
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		
	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		this.mappings.get(sink).addChild(this.mappings.get(source));		
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {		
		
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {		
		
	}

	@Override
	public GraphNode getResult() {		
		return this.rootNode;
	}

}
