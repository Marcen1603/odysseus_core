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
package de.uniol.inf.is.odysseus.rcp.views.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class QueryTableViewer extends TableViewer {

	private static final Map<String, Integer> STATUS_MAP = ImmutableMap.<String, Integer>builder()
			.put(OdysseusNLS.Opened.toLowerCase(), 3)
			.put(OdysseusNLS.Active.toLowerCase(), 2)
			.put(OdysseusNLS.Inactive.toLowerCase(), 1)
			.build();
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS yyyy-MM-dd");

	public QueryTableViewer(Composite parent, int style) {
		super(parent, style);

		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(ArrayContentProvider.getInstance());

		createColumns((TableColumnLayout) parent.getLayout());
	}

	private void createColumns(TableColumnLayout tableColumnLayout) {
		/************* ID ****************/
		TableViewerColumn idColumn = new TableViewerColumn(this, SWT.NONE);
		idColumn.getColumn().setText(OdysseusNLS.ID);
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQueryViewData) cell.getElement()).getId()));
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(5, 25, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(this, idColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData id1 = (IQueryViewData) e1;
				IQueryViewData id2 = (IQueryViewData) e2;
				if (id1.getId() > id2.getId())
					return 1;
				else if (id1.getId() < id2.getId())
					return -1;
				else
					return 0;
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* QueryName ****************/
		TableViewerColumn queryNameColumn = new TableViewerColumn(this, SWT.NONE);
		queryNameColumn.getColumn().setText(OdysseusNLS.Name);
		queryNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQueryViewData) cell.getElement()).getName()));
			}
		});
		tableColumnLayout.setColumnData(queryNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(this, queryNameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData id1 = (IQueryViewData) e1;
				IQueryViewData id2 = (IQueryViewData) e2;
				return id1.getName().compareTo(id2.getName());
			}
		};

		/************* Status ****************/
		TableViewerColumn statusColumn = new TableViewerColumn(this, SWT.NONE);
		statusColumn.getColumn().setText(OdysseusNLS.Status);
		// statusColumn.getColumn().setWidth(100);
		statusColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				String text = ((IQueryViewData) cell.getElement()).getStatus();
				cell.setText(text);
			}
		});
		tableColumnLayout.setColumnData(statusColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(this, statusColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData id1 = (IQueryViewData) e1;
				IQueryViewData id2 = (IQueryViewData) e2;
				String s1 = id1.getStatus();
				String s2 = id2.getStatus();

				return compareStatus(s1, s2);
			}
		};

		/************* Priority ****************/
		TableViewerColumn priorityColumn = new TableViewerColumn(this, SWT.NONE);
		priorityColumn.getColumn().setText(OdysseusNLS.Priority);
		priorityColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQueryViewData) cell.getElement()).getPriority()));
			}
		});
		tableColumnLayout.setColumnData(priorityColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(this, priorityColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData id1 = (IQueryViewData) e1;
				IQueryViewData id2 = (IQueryViewData) e2;
				if (id1.getPriority() > id2.getPriority())
					return 1;
				else if (id1.getPriority() < id2.getPriority())
					return -1;
				else
					return 0;
			}
		};

		/************* Parser ID ****************/
		TableViewerColumn parserIdColumn = new TableViewerColumn(this, SWT.NONE);
		parserIdColumn.getColumn().setText(OdysseusNLS.Parser);
		// parserIdColumn.getColumn().setWidth(100);
		parserIdColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IQueryViewData query = (IQueryViewData) cell.getElement();
				cell.setText(query.getParserId());
			}
		});
		tableColumnLayout.setColumnData(parserIdColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(this, parserIdColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData q1 = (IQueryViewData) e1;
				IQueryViewData q2 = (IQueryViewData) e2;
				String id1 = q1.getParserId();
				String id2 = q2.getParserId();
				return id1.compareToIgnoreCase(id2);
			}
		};

		/************* User ****************/
		TableViewerColumn userColumn = new TableViewerColumn(this, SWT.NONE);
		userColumn.getColumn().setText(OdysseusNLS.User);
		// userColumn.getColumn().setWidth(400);
		userColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IQueryViewData query = (IQueryViewData) cell.getElement();
				cell.setText(query.getUserName());
			}
		});
		tableColumnLayout.setColumnData(userColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(this, userColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData id1 = (IQueryViewData) e1;
				IQueryViewData id2 = (IQueryViewData) e2;
				return id1.getUserName().compareToIgnoreCase(id2.getUserName());
			}
		};

		/************* Query Start ***************/
		TableViewerColumn queryStartColumn = new TableViewerColumn(this, SWT.NONE);
		queryStartColumn.getColumn().setText(OdysseusNLS.QueryStart);
		// queryTextColumn.getColumn().setWidth(400);
		queryStartColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IQueryViewData query = (IQueryViewData) cell.getElement();
				long start = query.getStartTS();
				final String text;
				if (start > 0){
					text = dateFormat.format(new Date(start));
				}else{
					text = "--";
				}
				cell.setText(text);
			}
		});
		tableColumnLayout.setColumnData(queryStartColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(this, queryStartColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData q1 = (IQueryViewData) e1;
				IQueryViewData q2 = (IQueryViewData) e2;
				Long s1 = q1.getStartTS();
				Long s2 = q2.getStartTS();

				return s1.compareTo(s2);
			}
		};
		
		/************* Query Text ****************/
		TableViewerColumn queryTextColumn = new TableViewerColumn(this, SWT.NONE);
		queryTextColumn.getColumn().setText(OdysseusNLS.QueryText);
		// queryTextColumn.getColumn().setWidth(400);
		queryTextColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IQueryViewData query = (IQueryViewData) cell.getElement();
				String text = query.getQueryText();
				if (text == null) {
					cell.setText("[No Text]");
					return;
				}
				text = text.replace('\n', ' ');
				text = text.replace('\r', ' ');
				text = text.replace('\t', ' ');
				cell.setText(text);
			}
		});
		tableColumnLayout.setColumnData(queryTextColumn.getColumn(), new ColumnWeightData(50, 200, true));
		new ColumnViewerSorter(this, queryTextColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQueryViewData q1 = (IQueryViewData) e1;
				IQueryViewData q2 = (IQueryViewData) e2;
				String text1 = q1.getQueryText();
				String text2 = q2.getQueryText();

				return text1.compareToIgnoreCase(text2);
			}
		};
	}

	@Override
	protected List<?> getSelectionFromWidget() {

		List<Integer> queryIds = new ArrayList<Integer>();
		for (Object item : super.getSelectionFromWidget()) {
			queryIds.add(((IQueryViewData) item).getId());
		}

		return queryIds;
	}
	
	private static int compareStatus(String s1, String s2) {
		Integer s1Value = Optional.fromNullable(STATUS_MAP.get(s1.toLowerCase())).or(0);
		Integer s2Value = Optional.fromNullable(STATUS_MAP.get(s2.toLowerCase())).or(0);
		return s1Value.compareTo(s2Value);
	}
}
