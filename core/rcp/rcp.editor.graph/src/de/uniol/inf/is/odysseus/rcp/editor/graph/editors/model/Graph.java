/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author DGeesen
 * 
 */
public class Graph extends Observable{

	private List<OperatorNode> nodes;

	public List<OperatorNode> getNodes() {
		if (nodes == null) {
			nodes = new ArrayList<>();
		}
		return nodes;
	}

	public void addNode(OperatorNode node) {
		getNodes().add(node);
		setChanged();
		notifyObservers();
	}

	public void removeNode(OperatorNode node) {
		getNodes().remove(node);
		setChanged();
		notifyObservers();
	}
}
