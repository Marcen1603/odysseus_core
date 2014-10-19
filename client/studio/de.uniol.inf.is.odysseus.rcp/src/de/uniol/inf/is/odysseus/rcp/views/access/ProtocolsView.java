/**
 * 
 */
package de.uniol.inf.is.odysseus.rcp.views.access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProtocolsView extends ViewPart {

    TableViewer tableViewer;

    @Override
    public void createPartControl(final Composite parent) {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);

        final Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        final TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);

        this.tableViewer = new TableViewer(tableComposite);
        this.tableViewer.getTable().setHeaderVisible(true);
        this.tableViewer.getTable().setLinesVisible(true);
        this.tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        this.tableViewer.setComparator(new ViewerComparator() {
            @Override
            public int compare(final Viewer viewer, final Object e1, final Object e2) {
                return ProtocolsView.this.compareElements(e1, e2);
            }
        });
        this.createColumns(this.tableViewer, tableColumnLayout);
        refresh();
    }

    @Override
    public void setFocus() {
        this.tableViewer.getTable().setFocus();
    }

    public void refresh() {
        if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    final List<String> protocols = new ArrayList<>(ProtocolHandlerRegistry.getHandlerNames());
                    Collections.sort(protocols);
                    ProtocolsView.this.tableViewer.setInput(protocols);
                    ProtocolsView.this.tableViewer.refresh();
                }
            });
        }
    }

    private void createColumns(final TableViewer tableViewer, final TableColumnLayout tableColumnLayout) {
        final TableViewerColumn symbolColumn = ProtocolsView.createColumn(tableViewer, tableColumnLayout, OdysseusNLS.Symbol, new CellLabelProvider() {
            @Override
            public void update(final ViewerCell cell) {
                cell.setText(((String) cell.getElement()));
            }
        }, 5);
        this.addColumnSelectionListener(symbolColumn.getColumn());
    }

    private void addColumnSelectionListener(final TableColumn column) {
        column.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                ProtocolsView.this.tableColumnClicked((TableColumn) e.widget);
            }
        });
    }

    private static TableViewerColumn createColumn(final TableViewer tableViewer, final TableColumnLayout tableColumnLayout, final String title, final CellLabelProvider labelProvider, final int weight) {
        final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
        column.getColumn().setText(title);
        column.setLabelProvider(labelProvider);
        tableColumnLayout.setColumnData(column.getColumn(), new ColumnWeightData(weight, 25, true));
        return column;
    }

    private void tableColumnClicked(final TableColumn column) {
        final Table table = column.getParent();
        if (column.equals(table.getSortColumn())) {
            table.setSortDirection(table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP);
        }
        else {
            table.setSortColumn(column);
            table.setSortDirection(SWT.UP);
        }
        this.tableViewer.refresh();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private int compareElements(final Object e1, final Object e2) {
        final Table table = this.tableViewer.getTable();
        final int index = Arrays.asList(table.getColumns()).indexOf(table.getSortColumn());
        int result = 0;

        if (index != -1) {
            final Comparable c1 = (Comparable) e1;
            final Comparable c2 = (Comparable) e2;
            ;
            result = c1.compareTo(c2);
        }
        return table.getSortDirection() == SWT.UP ? result : -result;
    }
}
