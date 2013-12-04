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
package de.uniol.inf.is.odysseus.scheduler.slascheduler;

/**
 * Interface for starvation freedom functions
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface IStarvationFreedom {

	/**
	 * returns a cost value to avoid starvation freedom
	 * 
	 * @param decay
	 *            the decay factor of the starvation-freedom-function. This
	 *            param is needed for fine-tuning of the function and should be
	 *            given by the system
	 * @return a cost value representing the costs that would be caused by
	 *         starvation of a query. this value does not represent effective
	 *         costs, but should be relative to the values of the cost functions
	 *         oc() and mg().
	 */
	public double sf(double decay);

}
