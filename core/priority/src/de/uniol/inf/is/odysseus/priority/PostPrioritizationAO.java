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
package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

public class PostPrioritizationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4238316182706318260L;

	private boolean isActive = false;

	public PostPrioritizationAO() {
	}

	public PostPrioritizationAO(PostPrioritizationAO postPriorisationAO) {
		this.isActive = postPriorisationAO.isActive;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result;
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		PostPrioritizationAO other = (PostPrioritizationAO) obj;
//		if (isActive != other.isActive) {
//			return false;
//		}
//		return true;
//	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	private byte defaultPriority;

	public byte getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(byte defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	@Override
	public PostPrioritizationAO clone() {
		return new PostPrioritizationAO(this);
	}

}
