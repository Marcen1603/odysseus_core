/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.views;

import java.util.ArrayList;
import java.util.List;

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

import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDeviceTableViewer extends TableViewer {

    public SensingDeviceTableViewer(Composite parent, int style) {
        super(parent, style);

        getTable().setHeaderVisible(true);
        getTable().setLinesVisible(true);
        setContentProvider(ArrayContentProvider.getInstance());

        createColumns((TableColumnLayout) parent.getLayout());
    }

    private void createColumns(TableColumnLayout tableColumnLayout) {
        /************* URL ****************/
        TableViewerColumn idColumn = new TableViewerColumn(this, SWT.NONE);
        idColumn.getColumn().setText(OdysseusNLS.URL);
        idColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                cell.setText(String.valueOf(((ISensingDeviceViewData) cell.getElement()).getURL()));
            }
        });
        tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(5, 25, true));
        ColumnViewerSorter sorter = new ColumnViewerSorter(this, idColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                ISensingDeviceViewData id1 = (ISensingDeviceViewData) e1;
                ISensingDeviceViewData id2 = (ISensingDeviceViewData) e2;
                return id1.getURL().compareTo(id2.getURL());
            }
        };
        sorter.setSorter(sorter, ColumnViewerSorter.NONE);

        /************* Name ****************/
        TableViewerColumn queryNameColumn = new TableViewerColumn(this, SWT.NONE);
        queryNameColumn.getColumn().setText("Name");
        queryNameColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                cell.setText(String.valueOf(((ISensingDeviceViewData) cell.getElement()).getName()));
            }
        });
        tableColumnLayout.setColumnData(queryNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
        new ColumnViewerSorter(this, queryNameColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                ISensingDeviceViewData id1 = (ISensingDeviceViewData) e1;
                ISensingDeviceViewData id2 = (ISensingDeviceViewData) e2;
                return id1.getName().compareTo(id2.getName());
            }
        };

    }

    @Override
    protected List<?> getSelectionFromWidget() {

        List<String> sensingDeviceURLs = new ArrayList<String>();
        for (Object item : super.getSelectionFromWidget()) {
            sensingDeviceURLs.add(((ISensingDeviceViewData) item).getURL());
        }

        return sensingDeviceURLs;
    }

}
