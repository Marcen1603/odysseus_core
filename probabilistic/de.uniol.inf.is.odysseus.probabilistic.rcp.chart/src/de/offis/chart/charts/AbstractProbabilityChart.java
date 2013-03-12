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
