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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;

/**
 * An iterator interface for plain sources or buffers.
 * While {@link #hasNext()} may block until data is available
 * for plain sources,
 * it is not allowed to block for buffers.
 * @author Jonas Jacobi
 */
public interface IIterableSource<T> extends ISource<T> {
	/**
	 * Get whether a call to transferNext() will be successful.
	 * @return true, if an element can be transfered. 
	 */
	public boolean hasNext();
	/**
	 * Call {@link #transfer(Object)} with the next element. May only be called
	 * if a call to hasNext() returns true.
	 */
	public void transferNext();
	
	/**
	 * Returns true, if ISource has all Input processed
	 */
	public boolean isDone();
	
//	/**
//	 * Activates this operator if it could be done.
//	 * 
//	 * @param operatorControl
//	 *            Control which wants to activate this operator.
//	 */
//	public void activateRequest(IOperatorOwner operatorControl);
//
//	/**
//	 * Deactivates this operator if it could be done (e. g. no other query needs
//	 * this one).
//	 * 
//	 * @param operatorControl
//	 *            Control which wants to activate this operator.
//	 */
//	public void deactivateRequest(IOperatorOwner operatorControl);
//
//	/**
//	 * Checks if an control currently requests a deactivation of this operator.
//	 * 
//	 * @param operatorControl
//	 *            Control for the check.
//	 * @return TRUE: Control currently requests a deactivation of this operator.
//	 *         FALSE: else
//	 */
//	public boolean deactivateRequestedBy(IOperatorOwner operatorControl);
//
//	/**
//	 * Indicates if this operator is currently active.
//	 * 
//	 * @return TRUE: operator is currently active. FALSE: else
//	 */
//	public boolean isActive();
	
	/**
	 * Indicates if the operator is currently blocked (i.e. does not produce elements)
	 * @return
	 */
	@Override
	public boolean isBlocked();

	/**
	 * Block operator
	 */
	@Override
	public void block();
	
	/**
	 * unblock operator
	 */
	@Override
	public void unblock();
}

