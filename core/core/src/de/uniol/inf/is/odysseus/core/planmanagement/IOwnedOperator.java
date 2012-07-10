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
package de.uniol.inf.is.odysseus.core.planmanagement;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;


/**
 * Describes an operator which is owned by an other object (e. g. a query can
 * own physical and logical operator). An operator could be owned by more then
 * one object (e. g. a global source which distributes data elements to more
 * then one query).
 * 
 * @author Wolf Bauer
 * 
 */
public interface IOwnedOperator {
	/**
	 * Adds an owner to this operator.
	 * 
	 * @param owner Owner which should be added.
	 */
	public void addOwner(IOperatorOwner owner);

	/**
	 * Removes an owner from this operator.
	 * 
	 * @param owner Owner which should be removed.
	 */
	public void removeOwner(IOperatorOwner owner);

	/**
	 * Removes all owner from this operator.
	 * 
	 */
	void removeAllOwners();
	
	/**
	 * Checks if this operator is owned by a specific owner.
	 * 
	 * @param owner Owner which should be checked.
	 * @return TRUE: This operator is owned by the owner. FALSE: else
	 */
	public boolean isOwnedBy(IOperatorOwner owner);

	/**
	 * Indicatives if this operator has at least one owner.
	 * 
	 * @return TRUE: This operator has at least one owner. FALSE: else
	 */
	public boolean hasOwner();

	/**
	 * Returns all registered owner of this operator.
	 * 
	 * @return All registered owner of this operator.
	 */
	public List<IOperatorOwner> getOwner();
	
	/**
	 * Returns a printable String with all Owners
	 */
	public String getOwnerIDs();

	
}
