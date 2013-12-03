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

package de.uniol.inf.is.odysseus.costmodel.planmigration;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * 
 * @author Merlin Wasmann
 * 
 */
public class PlanMigrationCost implements ICost<PlanMigration> {

	private PlanMigration migration = null;
	private PlanMigrationEstimation estimation = null;
	private double memCost;
	private double cpuCost;
	private long duration;

	public PlanMigrationCost(PlanMigration migration, PlanMigrationEstimation estimation,
			double memCost, double cpuCost, long duration) {
		this.migration = migration;
		this.estimation = estimation;
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.duration = duration;
	}

	/**
	 * internal constructor.
	 * 
	 * @param memCost
	 * @param cpuCost
	 * @param duration
	 */
	PlanMigrationCost(double memCost, double cpuCost, long duration) {
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.duration = duration;
	}

	@Override
	public int compareTo(ICost<PlanMigration> arg0) {
		PlanMigrationCost cost = (PlanMigrationCost) arg0;
		if(this.memCost > cost.memCost || this.cpuCost > cost.cpuCost || this.duration > cost.duration) {
			return 1;
		}
		if(this.memCost < cost.memCost && this.cpuCost < cost.cpuCost && this.duration < cost.duration) {
			return -1;
		}
		return 0;
	}

	@SuppressWarnings("serial")
	@Override
	public Collection<PlanMigration> getOperators() {
		return new ArrayList<PlanMigration>() {
			{
				add(migration);
			}
		};
	}

	@Override
	public ICost<PlanMigration> getCostOfOperator(PlanMigration operator) {
		if (!operator.equals(this.migration)) {
			return new PlanMigrationCost(0, 0, 0);
		}
		return new PlanMigrationCost(this.estimation.getDetailCost()
				.getMemoryCost(), this.estimation.getDetailCost()
				.getProcessorCost(), this.estimation.getDetailCost()
				.getDuration());
	}

	@Override
	public ICost<PlanMigration> merge(ICost<PlanMigration> otherCost) {
		if(otherCost == null) {
			return new PlanMigrationCost(this.memCost, this.cpuCost, this.duration);
		}
		PlanMigrationCost cost = (PlanMigrationCost) otherCost;
		return new PlanMigrationCost(this.memCost + cost.memCost, this.cpuCost + cost.cpuCost, this.duration + cost.duration);
	}

	@Override
	public ICost<PlanMigration> substract(ICost<PlanMigration> otherCost) {

		if(otherCost == null) {
			return new PlanMigrationCost(this.memCost, this.cpuCost, this.duration);
		}
		PlanMigrationCost cost = (PlanMigrationCost) otherCost;
		return new PlanMigrationCost(this.memCost - cost.memCost, this.cpuCost - cost.cpuCost, this.duration - cost.duration);
	}

	@Override
	public ICost<PlanMigration> fraction(double factor) {
		return new PlanMigrationCost(this.memCost * factor, this.cpuCost * factor, (long) (this.duration * factor));
	}

	public PlanMigrationEstimation getEstimation() {
		return this.estimation;
	}

	public double getMemCost() {
		return this.memCost;
	}


	public double getCpuCost() {
		return this.cpuCost;
	}

	public long getDuration() {
		return this.duration;
	}


	
}
