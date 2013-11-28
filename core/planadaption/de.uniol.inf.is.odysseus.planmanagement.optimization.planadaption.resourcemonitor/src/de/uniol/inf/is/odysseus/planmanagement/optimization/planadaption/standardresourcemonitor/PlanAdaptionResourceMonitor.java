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

package de.uniol.inf.is.odysseus.planmanagement.optimization.planadaption.standardresourcemonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionResourceMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.IPlanMigrationCostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * 
 * @author Merlin Wasmann
 *
 */
public class PlanAdaptionResourceMonitor implements
		IPlanAdaptionResourceMonitor {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlanAdaptionResourceMonitor.class);
	
	private IAdmissionControl ac;
	private Map<String, IPlanMigrationCostModel> costModels = new HashMap<String, IPlanMigrationCostModel>();
	private IPlanMigrationCostModel selectedCostModel = null;

	@Override
	public ICost<PlanMigration> getPlanMigrationCost(PlanMigration migration) {
		List<PlanMigration> migrations = new ArrayList<PlanMigration>();
		migrations.add(migration);
		return getSelectedCostModelInstance().estimateCost(migrations, true);
	}

	@Override
	public ICost<IPhysicalOperator> getPlanExecutionCost(IPhysicalQuery query) {
		return this.ac.getCost(query);
	}

	public void bindAdmissionControl(IAdmissionControl ac) {
		this.ac = ac;
		LOG.debug("AdmissionControl bound");
	}
	
	public void unbindAdmissionControl(IAdmissionControl ac) {
		if(ac.equals(this.ac)) {
			this.ac = null;
			LOG.debug("AdmissionControl unbound");
		}
	}
	
	public void bindCostModel(IPlanMigrationCostModel model) {
		this.costModels.put(model.getClass().getSimpleName(), model);
		LOG.debug("CostModel: " + model.getClass().getSimpleName() + " bound");
		if(getSelectedCostModel() == null) {
			selectCostModel(model.getClass().getSimpleName());
		}
	}
	
	public void unbindCostModel(IPlanMigrationCostModel model) {
		this.costModels.remove(model.getClass().getSimpleName());
		LOG.debug("CostModel: " + model.getClass().getSimpleName() + " unbound");
	}
	
	String getSelectedCostModel() {
		if(this.selectedCostModel == null) {
			return null;
		}
		return this.selectedCostModel.getClass().getSimpleName();
	}
	
	ICostModel<PlanMigration> getSelectedCostModelInstance() {
		return this.selectedCostModel;
	}
	
	public void selectCostModel(String name) {
		if(!this.costModels.containsKey(name)) {
			throw new RuntimeException("CostModel " + name + " not found.");
		}
		this.selectedCostModel = this.costModels.get(name);
		LOG.debug("CostModel: " + name + " selected");
	}
}
