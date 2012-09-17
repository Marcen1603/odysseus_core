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

package de.uniol.inf.is.odysseus.rcp.viewer.manage.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;

public class PhysicalOperatorFilter {

	private static final ImmutableList<Class<? extends IPhysicalOperator>> FILTERED_OPERATORS = ImmutableList.<Class<? extends IPhysicalOperator>> builder()
			.add(DefaultStreamConnection.class)
			.build();

	public static boolean isFiltered(IPhysicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator must not be null!");
		return FILTERED_OPERATORS.contains(operator.getClass());
	}
}
