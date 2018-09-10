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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.util.Collection;
import java.util.HashMap;
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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.billingmodel.BillingHelper;
import de.uniol.inf.is.odysseus.billingmodel.DatabaseBillingManager;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class SLACostMonitoringDashboardPart extends AbstractDashboardPart implements PaintListener {

	private TableViewer tableViewer;
	private TableColumnLayout tableColumnLayout;
	private Composite tableComposite;
	private Font titleFont;
	private final List<RowHelper> data = Lists.newArrayList();
	private long updateFrequency;
	private long lastUpdate;
	private boolean refreshing = false;
	private Map<Integer, String> costTypeDescriptions = new HashMap<>();

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {

		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		topComposite.setLayout(new GridLayout(1, false));
		topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		createLabel(topComposite);

		tableComposite = new Composite(topComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		parent.layout();

		updateFrequency = 30 * 1000;
		costTypeDescriptions = ((DatabaseBillingManager)BillingHelper.getBillingManager()).getDescriptionOfCostTypes();
	}

	private void createLabel(Composite topComposite) {
		Label label = new Label(topComposite, SWT.BOLD);
		label.setText("SLA Cost Monitoring");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setAlignment(SWT.CENTER);
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		titleFont = createBoldFont(label.getFont());
		label.setFont(titleFont);
	}

	private static Font createBoldFont(Font baseFont) {
		FontData[] fontData = baseFont.getFontData();
		fontData[0].setStyle(SWT.BOLD);
		return new Font(Display.getCurrent(), fontData[0]);
	}

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		lastUpdate = System.currentTimeMillis();

		TableViewerColumn userColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		userColumn.getColumn().setText("User");
		tableColumnLayout.setColumnData(userColumn.getColumn(), new ColumnWeightData(5, 25, true));
		userColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final RowHelper row = (RowHelper) cell.getElement();
				cell.setText(row.getUserID());
			}
		});

		TableViewerColumn queryColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		queryColumn.getColumn().setText("Query");
		tableColumnLayout.setColumnData(queryColumn.getColumn(), new ColumnWeightData(5, 25, true));
		queryColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final RowHelper row = (RowHelper) cell.getElement();
				cell.setText(String.valueOf(row.getQueryID()));
			}
		});

		TableViewerColumn costTypeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		costTypeColumn.getColumn().setText("Cost type");
		tableColumnLayout.setColumnData(costTypeColumn.getColumn(), new ColumnWeightData(5, 25, true));
		costTypeColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final RowHelper row = (RowHelper) cell.getElement();
				cell.setText(row.getCostType());
			}
		});

		TableViewerColumn amountColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		amountColumn.getColumn().setText("Total amount");
		tableColumnLayout.setColumnData(amountColumn.getColumn(), new ColumnWeightData(5, 25, true));
		amountColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final RowHelper row = (RowHelper) cell.getElement();
				cell.setText(String.valueOf(row.getAmount()));
			}
		});

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(data);
		tableViewer.refresh();
		tableViewer.getTable().redraw();

		tableComposite.layout();
	}
	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdate > updateFrequency) {
			lastUpdate = currentTime;

			data.clear();
			addPaymentsToList();
			addPaymentSanctionToList();
			addRevenueSanctionToList();
			addRevenuesToList();

			if( !refreshing && tableViewer.getInput() != null) {
				refreshing = true;
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						synchronized( data ) {
							if (!tableViewer.getTable().isDisposed()) {
								tableViewer.refresh();
							}
							refreshing = false;
						}
					}
				});
			}
		}
	}

	private void addPaymentsToList() {
		for (Map.Entry<String, Map<Integer, Long>> entry : BillingHelper.getBillingManager().getUnsavedPayments().entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> paymentsMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : paymentsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000;

				data.add(new RowHelper(userID, queryID, costTypeDescriptions.get(1), amount));
			}
		}
	}

	private void addPaymentSanctionToList() {
		for (Map.Entry<String, Map<Integer, Long>> entry : BillingHelper.getBillingManager().getUnsavedPaymentSanctions().entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> paymentsMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : paymentsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000;

				data.add(new RowHelper(userID, queryID, costTypeDescriptions.get(2), amount));
			}
		}
	}

	private void addRevenueSanctionToList() {
		for (Map.Entry<String, Map<Integer, Long>> entry : BillingHelper.getBillingManager().getUnsavedRevenueSanctions().entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> paymentsMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : paymentsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000;

				data.add(new RowHelper(userID, queryID, costTypeDescriptions.get(3), amount));
			}
		}
	}

	private void addRevenuesToList() {
		for (Map.Entry<String, Map<Integer, Long>> entry : BillingHelper.getBillingManager().getUnsavedRevenues().entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> paymentsMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : paymentsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000;

				data.add(new RowHelper(userID, queryID, costTypeDescriptions.get(4), amount));
			}
		}
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
	}

	@Override
	public void paintControl(PaintEvent e) {

	}

	private class RowHelper {
		private String userID;
		private int queryID;
		private String costType;
		private double amount;

		public RowHelper(String u, int q, String t, double a) {
			userID = u;
			queryID = q;
			costType = t;
			amount = a;
		}

		public String getUserID() {
			return userID;
		}

		public int getQueryID() {
			return queryID;
		}

		public String getCostType() {
			return costType;
		}

		public double getAmount() {
			return amount;
		}
	}
}