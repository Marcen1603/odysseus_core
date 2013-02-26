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
package de.uniol.inf.is.odysseus.costmodel.opcount;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;

/**
 * Repräsentiert das Kostenmodell nach Operatorzahl. Die Kosten Ergeben
 * sich aus der Zahl der Operatoren der zu untersuchenden Anfrage.
 * 
 * @author Timo Michelsen
 *
 */
public class OpCountCostModel<T> implements ICostModel<T> {

	@Override
	public ICost<T> getMaximumCost() {
		return new OpCountCost<T>(5);
	}

	@Override
	public ICost<T> estimateCost(List<T> operators, boolean useQuerySharing) {
		return new OpCountCost<T>(operators);
	}

	@Override
	public ICost<T> getZeroCost() {
		return new OpCountCost<T>(0);
	}

	@Override
	public ICost<T> getOverallCost() {
		return new OpCountCost<T>(0);
	}

}
