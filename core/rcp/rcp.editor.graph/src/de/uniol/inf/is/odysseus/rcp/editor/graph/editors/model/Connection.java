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

/**
 * @author DGeesen
 * 
 */
public class Connection {

	private OperatorNode source;
	private OperatorNode target;

	public OperatorNode getSource() {
		return source;
	}

	public void setSource(OperatorNode source) {
		if (source == this.source) return;
		if (this.source != null) {
			this.source.removeSourceConnection(this);
		}
		this.source = source;
		if (source != null) {
			source.addSourceConnection(this);
		}
	}
	
	public OperatorNode getTarget() {
		return target;
	}
	
	public void setTarget(OperatorNode target) {
		if (target == this.target) return;
		if (this.target != null) {
			this.target.removeTargetConnection(this);
		}
		this.target = target;
		if (target != null) {
			target.addTargetConnection(this);
		}
	}

}
