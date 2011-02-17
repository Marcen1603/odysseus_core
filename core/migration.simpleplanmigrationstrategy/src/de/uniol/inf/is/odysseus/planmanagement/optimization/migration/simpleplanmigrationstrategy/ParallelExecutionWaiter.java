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
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

/**
 * 
 * Helper class, that informs the {@link SimplePlanMigrationStrategy}, when a
 * specified period has ended.
 * 
 * @author Tobias Witt
 * 
 */
class ParallelExecutionWaiter implements Runnable {
	
	private SimplePlanMigrationStrategy sender;
	private StrategyContext context;
	
	public ParallelExecutionWaiter(SimplePlanMigrationStrategy sender, StrategyContext context) {
		this.sender = sender;
		this.context = context;
	}

	// TODO: Fehler! So ist das nicht erlaubt!! Zeitfortschritt kann nur im Strom gemessen werden!
	@Override
	public void run() {
		try {
			// plus 1 second safety for scheduling
			Thread.sleep(this.context.getwMax().getWindowSize() + 1000L);
		} catch (InterruptedException e) {}
		this.sender.finishedParallelExecution(context);
	}

}
