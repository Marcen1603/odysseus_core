/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.offis.chart.charts;

import de.offis.chart.charts.datatype.ProbViewSchema;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;

public abstract class AbstractProbabilityChart<T, M extends IMetaAttribute> extends AbstractChart<T, M>{

	public AbstractProbabilityChart() {
		// We need this
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initWithOperator(IPhysicalOperator observingOperator) {
		// create connection
		if (observingOperator instanceof DefaultStreamConnection<?>) {
			this.connection = (IStreamConnection<Object>) observingOperator;
		} else {
			this.connection = new DefaultStreamConnection(observingOperator);
		}

		// initConnection
		for (ISubscription<? extends ISource<?>> s : this.connection
				.getSubscriptions()) {
			this.viewSchema.put(s.getSinkInPort(),
					new ProbViewSchema<T>(s.getSchema(), s.getTarget()
							.getMetaAttributeSchema(), s.getSinkInPort()));
		}
		if (validate()) {
			this.connection.addStreamElementListener(this);
			this.connection.connect();
			chartSettingsChanged();
			
		}
	}
}
