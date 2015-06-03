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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewSchema;

public abstract class AbstractChart<T, M extends IMetaAttribute> extends ViewPart implements IAttributesChangeable<T>, IStreamElementListener<IStreamObject<?>> {

	Logger logger = LoggerFactory.getLogger(AbstractChart.class);

	protected Map<Integer, ViewSchema<T>> viewSchema = new HashMap<Integer, ViewSchema<T>>();
	protected int[] groupByList = null;
	protected IStreamConnection<IStreamObject<?>> connection;

	public AbstractChart() {
		// We need this
	}

	public void initWithOperator(IPhysicalOperator observingOperator) {
		this.connection = createConnection(observingOperator);
		initConnection(connection);
	}
	
	protected abstract void initConnection(IStreamConnection<IStreamObject<?>> connection);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static IStreamConnection<IStreamObject<?>> createConnection(IPhysicalOperator operator) {
		if (operator instanceof DefaultStreamConnection<?>) {
			return (IStreamConnection<IStreamObject<?>>) operator;
		}
		return new DefaultStreamConnection(operator);
	}

	protected void reloadChartImpl() {
		try {
			reloadChart();
		} catch( Throwable t ) {
			// expected here
		}
	}

	protected abstract void reloadChart();
	
    public abstract void saveImage(File path) throws IOException;
    
	protected boolean validate() {
		for (Entry<Integer, ViewSchema<T>> e : viewSchema.entrySet()) {
			if (e.getValue().getChoosenAttributes().size() > 0) {
				return true;
			}
		}
		logger.error("Chart View not validated, because there has to be at least one valid attribute");
		return false;
	}

	protected void init() {

	}

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: Stream visualization is only for relational tuple!");
			return;
		}

		@SuppressWarnings("unchecked")
		final Tuple<M> tuple = (Tuple<M>) element;
		try {
			List<T> values = this.viewSchema.get(port).convertToChoosenFormat(this.viewSchema.get(port).convertToViewableFormat(tuple));
			processElement(values, tuple.getMetadata(), port);
		} catch (SWTException swtex) {
			System.out.println("WARN: SWT Exception " + swtex.getMessage());
		}

	}

	protected abstract void processElement(List<T> tuple, M metadata, int port);

	@Override
	public void dispose() {
		if (this.connection != null) {
			if (this.connection.isConnected()) {
				this.connection.disconnect();
			}
		}
	}

	@Override
	public List<IViewableAttribute> getViewableAttributes(int port) {
        if (!this.viewSchema.containsKey(port)) {
            return null;
        }
		return this.viewSchema.get(port).getViewableAttributes();
	}

	@Override
	public List<IViewableAttribute> getChoosenAttributes(int port) {
        if (!this.viewSchema.containsKey(port)) {
            return null;
        }
		return this.viewSchema.get(port).getChoosenAttributes();
	}
	
	@Override
	public List<IViewableAttribute> getGroupByAttributes(int port) {
        if (!this.viewSchema.containsKey(port)) {
            return null;
        }
		return this.viewSchema.get(port).getGroupByAttributes();
	}

	@Override
	public void setChoosenAttributes(int port, List<IViewableAttribute> choosenAttributes) {
		this.viewSchema.get(port).setChoosenAttributes(choosenAttributes);
		reloadChartImpl();
	}
	
	@Override
	public void setGroupByAttributes(int port,
			List<IViewableAttribute> groupByAttributes) {
		this.viewSchema.get(port).setGroupByAttributes(groupByAttributes);
	}

	@Override
	public Set<Integer> getPorts() {
		return viewSchema.keySet();
	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {

	}

	@Override
	public void securityPunctuationElementReceived(IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
	}

	@Override
	public String getTitle() {
		return "";
	}

	public abstract String getViewID();

	protected String checkAtLeastOneSelectedAttribute(Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		for (Entry<Integer, Set<IViewableAttribute>> e : selectAttributes.entrySet()) {
			if (e.getValue().size() > 0) {
				return null;
			}
		}
		return "The number of choosen attributes should be at least one!";
	}

}
