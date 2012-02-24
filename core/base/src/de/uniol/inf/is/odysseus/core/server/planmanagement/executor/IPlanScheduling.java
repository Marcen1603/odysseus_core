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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.IPlanExecutionHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;

/**
 * IPlanScheduling beschreibt die Möglichkeit das Scheduling innerhalb von
 * Odysseus zu beeinflussen. Und Informationenen darüber abzurufen. Dies ist
 * neben IPlanManager eine der beiden Grundlagen für die Ausführungsumgebung.
 * 
 * @author wolf
 * 
 */
public interface IPlanScheduling extends IPlanExecutionHandler {

	/**
	 * liefert den aktuellen physischen Ausführungsplan.
	 * @return den aktuellen physischen Ausführungsplan
	 */
	public IExecutionPlan getExecutionPlan();

	/**
	 * startExecution startet die Ausführung von Odysseus.
	 * @throws PlanManagementException
	 */
	public void startExecution() throws PlanManagementException;

	/**
	 * stopExecution stopped die Ausführung von Odysseus.
	 * @throws PlanManagementException
	 */
	public void stopExecution() throws PlanManagementException;

	/**
	 * isRunning gibt an, ob die Ausführung innerhalb von Odysseus läuft.
	 * @return true: Ausführung läuft. false: Ausführung läuft nicht.
	 * @throws PlanManagementException
	 */
	public boolean isRunning() throws PlanManagementException;
}
