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
package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import de.uniol.inf.is.odysseus.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class PlanMonitor extends AbstractPlanMonitor<Double> {

	@SuppressWarnings("rawtypes")
	public PlanMonitor(IQuery target, boolean onlyRoots, boolean onlyBuffer,
			String monitoringType, long monitoringPeriod) {
		super(target, onlyRoots, onlyBuffer, monitoringType);
		for (IPhysicalOperator p : monitoredOps) {
			if (monitoringPeriod <= 0) {
				if (!p.providesMonitoringData(monitoringType)) {
					p.addMonitoringData(monitoringType, MonitoringDataTypes
							.createMetadata(monitoringType, p));
				}
			} else {
				if (!p.providesMonitoringData(monitoringType, monitoringPeriod)) {
					p.getMonitoringData(
							(IPeriodicalMonitoringData) MonitoringDataTypes
									.createMetadata(monitoringType, p),
							monitoringPeriod);
				}
			}
		}
	}

	public PlanMonitor(PlanMonitor monitor) {
		super(monitor);
	}

	@Override
	public Double getValue() {
		return null;
	}

	@Override
	public double getDoubleValue() {
		return 0;
	}

	@Override
	public void reset() {
		reset(getType());
	}

	@Override
	public PlanMonitor clone() {
		return new PlanMonitor(this);
	}

	@Override
	public Double getValue(IPhysicalOperator operator) {
		return getValue(operator, getType());
	}

	protected void reset(String type) {
		for (IPhysicalOperator p : monitoredOps) {
			p.getMonitoringData(type).reset();
		}
	}

}
