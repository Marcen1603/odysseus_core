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
package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharingoptimizer;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class Vertice {
	private IPhysicalOperator po;
	private String label;
	private boolean locked = false;
	
	protected Vertice(IPhysicalOperator po, String label) {
		this.po = po;
		this.label = label;
	}
	
	public IPhysicalOperator getPo() {
		return po;
	}

	public void setPo(IPhysicalOperator po) {
		this.po = po;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean equals(Vertice v) {
		if(this.label.equals(v.getLabel()) && this.po == v.po) {
			return true;
		}
		return false;
	}

	public boolean implies(Vertice v) {
		// TODO: Feststellen, ob this-->v im Sinne des IE gilt
		// Auswertung der Labels, d.h. Vergleich der Expressions und ob die POs die gleichen inputs haben
		
		return false;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void lock(boolean locked) {
		this.locked = locked;
	}
	
}
