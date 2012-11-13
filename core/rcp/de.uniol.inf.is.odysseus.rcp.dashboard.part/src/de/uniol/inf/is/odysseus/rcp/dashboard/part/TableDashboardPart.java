/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;

public class TableDashboardPart extends AbstractDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(TableDashboardPart.class);
	
	private IPhysicalOperator operator;

	private TableViewer tableViewer;
	
	private String[] attributes;
	private int[] positions;
	
	private List<Tuple<?>> data = Lists.newArrayList();
	private int maxData = 10;
	
	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		
		String attributeList = getConfiguration().get("Attributes");
		if (Strings.isNullOrEmpty(attributeList)) {
			new Label(parent, SWT.NONE).setText("Attribute List is invalid!");
			return;
		}

		attributes = attributeList.trim().split(",");
		for(int i = 0; i < attributes.length; i++) {
			attributes[i] = attributes[i].trim();
		}
		
		maxData = determineMaxData(getConfiguration());

		int colCount = attributes.length;
		
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		for( int i = 0; i < colCount; i++ ) {
			
			final int finalI = i;
			
			TableViewerColumn fileNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			fileNameColumn.getColumn().setText(attributes[i]);
			tableColumnLayout.setColumnData(fileNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
			fileNameColumn.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					Tuple<?> tuple = (Tuple<?>)cell.getElement();
					Object attrValue = tuple.getAttributes()[positions[finalI]];
					cell.setText(attrValue != null ? attrValue.toString() : "null");
				}
			});
		}
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(data);
		
	}

	private static int determineMaxData(Configuration config) {
		int maxData = 10;
		try {
			maxData = config.get("MaxData");
			if(maxData < 0 ) {
				throw new Exception("Negative numbers for maximum data sizes are not allowed!");
			}
			
		} catch( Throwable t ) {
			LOG.error("Could not determine maximum data size for table-contents. Using 10 as default.", t);
			maxData = 10;
		}
		
		return maxData;
	}
	
	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		if (physicalRoots.size() > 1) {
			throw new Exception("Table DashboardPart only supports one query!");
		}

		operator = physicalRoots.get(0);
		positions = determinePositions(operator.getOutputSchema(), attributes);
	}

	@Override
	public void streamElementRecieved(final IStreamObject<?> element, int port) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				data.add((Tuple<?>)element);
				if( data.size() > maxData ) {
					data.remove(0);
				}
				
				if( !tableViewer.getTable().isDisposed()) {
					tableViewer.refresh();
				}
				
			}
		});
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}
	
	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {

	}

	private static int[] determinePositions(SDFSchema outputSchema, String[] attributes2) {
		int[] positions = new int[attributes2.length];
		
		for( int i = 0 ; i < attributes2.length; i++ ) {
			for( int j = 0; j < outputSchema.size(); j++ ) {
				if( outputSchema.get(j).getAttributeName().equals(attributes2[i])) {
					positions[i] = j;
					break;
				}
			}
		}
		
		return positions;
	}

	@Override
	public Map<String, String> onSave() {
		return null;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		
	}

}
