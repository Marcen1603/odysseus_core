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

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Dennis Geesen Created at: 12.08.2011
 */
@XmlRootElement
public class SimpleGraph {

	private ArrayList<GraphNode> rootNodes = new ArrayList<GraphNode>();

	public GraphNode[] getRootNodes() {
		return rootNodes.toArray(new GraphNode[0]);
	}

	public void addRootNode(GraphNode rootNode) {
		this.rootNodes.add(rootNode);
	}

	public void removeRootNode(GraphNode rootNode) {
		this.rootNodes.remove(rootNode);
	}

	public void setRootNodes(GraphNode[] newRootNodes) {
		for (GraphNode n : newRootNodes) {
			this.rootNodes.add(n);
		}
	}

}
