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
package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.plangenerator.util.PlanGeneratorHelper;

/**
 * This class is for evaluation purposes only. It gets a plan consisting of
 * Source -> Map (expensive) -> Select (0.9 selectivity) and swaps the positions
 * of Map and Select.
 * 
 * @author Merlin Wasmann
 * 
 */
public class SwapOperatorsPlanGenerationMethod implements IPlanGenerationMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.plangenerator.generator.methods.
	 * IPlanGenerationMethod
	 * #generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator
	 * .ILogicalOperator,
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.optimization
	 * .configuration.PlanGenerationConfiguration,
	 * de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner) {
		List<ILogicalOperator> plans = new ArrayList<ILogicalOperator>();
		plans.add(plan);
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor(
				owner);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, copyVisitor);
		ILogicalOperator copy = copyVisitor.getResult();
		Pair<SelectAO, MapAO> pair = getSwapableOperators(copy);
		if(pair != null && pair.getE1() != null && pair.getE2() != null) {
			swapOperators(pair.getE2(), pair.getE1());
			PlanGeneratorHelper.setNewOwnerForPlan(copy, owner);
			plans.add(copy);
		}
		return plans;
	}

	private void swapOperators(MapAO map, SelectAO select) {
		RestructHelper.simpleOperatorSwitch(select, map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Pair<SelectAO, MapAO> getSwapableOperators(ILogicalOperator plan) {
		CollectOperatorLogicalGraphVisitor<SelectAO> selectVisitor = new CollectOperatorLogicalGraphVisitor<SelectAO>(SelectAO.class);
		CollectOperatorLogicalGraphVisitor<MapAO> mapVisitor = new CollectOperatorLogicalGraphVisitor<MapAO>(MapAO.class);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, selectVisitor);
		walker.clearVisited();
		walker.prefixWalk(plan, mapVisitor);

		Set<SelectAO> selects = selectVisitor.getResult();
		Set<MapAO> maps = mapVisitor.getResult();
		
		if(selects.isEmpty() || maps.isEmpty() || selects.size() > 1 || maps.size() > 1) {
			return null;
		}
		
		SelectAO select = null;
		MapAO map = null;
		
		Iterator<SelectAO> selectIterator = selects.iterator();
		if(selectIterator.hasNext()) {
			select = selectIterator.next();
		}
		Iterator<MapAO> mapIterator = maps.iterator();
		if(mapIterator.hasNext()) {
			map = mapIterator.next();
		}
		return new Pair<SelectAO, MapAO>(select, map);
	}

}
