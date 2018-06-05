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
package de.uniol.inf.is.odysseus.probabilistic.rcp.chart;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.probabilistic.rcp.chart.datatype.ProbabilisticViewSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 * @param <M>
 */
public abstract class AbstractProbabilityChart<T, M extends IMetaAttribute> extends AbstractChart<T, M> {
    /**
     * Default constructor.
     */
    public AbstractProbabilityChart() {

    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#
     * initWithOperator
     * (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void initWithOperator(final IPhysicalOperator observingOperator) {
        // create connection
        if (observingOperator instanceof DefaultStreamConnection<?>) {
            this.connection = (IStreamConnection<IStreamObject<?>>) observingOperator;
        }
        else {
            this.connection = new DefaultStreamConnection(observingOperator);
        }

        // initConnection
        for (final ISubscription<? extends ISource<?>,?> s : this.connection.getSubscriptions()) {
            this.viewSchema.put(s.getSinkInPort(), new ProbabilisticViewSchema<T>(s.getSchema(), (s.getSource()).getMetaAttributeSchema(), s.getSinkInPort()));
        }
        if (this.validate()) {
            this.connection.addStreamElementListener(this);
            this.connection.connect();
            this.chartSettingsChanged();

        }
    }
}
