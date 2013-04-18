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

package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

/**
 * Interface providing functionality for policy rules for the plan adaption.
 * Such as when do we want to adapt and how long should we wait after an
 * adaption.
 * 
 * @author Merlin Wasmann
 * 
 */
public interface IPlanAdaptionPolicyRuleEngine {

	/**
	 * @return duration of a migration/adaption block
	 */
	public long getBlockedTime();

	public void fireAdaptionStartEvent();

	/**
	 * Stops the rule engine from sending adaption events.
	 * 
	 * @param startAfterBlockedTime
	 *            policyEngine waits blockedTime before firing new events.
	 */
	public void start(boolean startAfterBlockedTime);

	/**
	 * Resumes sending adaption events after the blocked time.
	 */
	public void stop();

	public void register(IPlanAdaptionPolicyListener listener);

	public void unregister(IPlanAdaptionPolicyListener listener);
}
