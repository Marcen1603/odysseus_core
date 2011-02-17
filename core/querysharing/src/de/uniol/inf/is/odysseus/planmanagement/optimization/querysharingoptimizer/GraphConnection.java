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
package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

public class GraphConnection {
	private Vertice source;
	private Vertice target;
	
	protected GraphConnection(Vertice source, Vertice target) {
		this.source = source;
		this.target  = target;
	}

	public Vertice getSource() {
		return source;
	}

	public void setSource(Vertice source) {
		this.source = source;
	}

	public Vertice getTarget() {
		return target;
	}

	public void setTarget(Vertice target) {
		this.target = target;
	}

	public boolean equals(GraphConnection gc) {
		if(this.source.equals(gc.getSource()) && this.target.equals(gc.getTarget())) {
			return true;
		}
		return false;
	}	
}
