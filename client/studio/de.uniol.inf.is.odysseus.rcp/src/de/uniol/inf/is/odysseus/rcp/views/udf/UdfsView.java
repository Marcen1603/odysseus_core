/**
 * 
 */
package de.uniol.inf.is.odysseus.rcp.views.udf;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class UdfsView extends ViewPart {

    private TableViewer tableViewer;

    @Override
    public void createPartControl(Composite parent) {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);

        Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);

        tableViewer = new TableViewer(tableComposite);
        tableViewer.getTable().setHeaderVisible(true);
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());

        createColumns(tableViewer, tableColumnLayout);
        refresh();
    }

    @Override
    public void setFocus() {
        tableViewer.getTable().setFocus();
    }

    public void refresh() {
        if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    tableViewer.setInput(OdysseusRCPPlugIn.getExecutor().getUdfs());
                    tableViewer.refresh();
                }
            });
        }
    }

    private static void createColumns(TableViewer tableViewer, TableColumnLayout tableColumnLayout) {
        createColumn(tableViewer, tableColumnLayout, OdysseusNLS.Symbol, new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                cell.setText(((String) cell.getElement()));
            }
        }, 5);
    }

    private static TableViewerColumn createColumn(TableViewer tableViewer, TableColumnLayout tableColumnLayout, String title, CellLabelProvider labelProvider, int weight) {
        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
        column.getColumn().setText(title);
        column.setLabelProvider(labelProvider);
        tableColumnLayout.setColumnData(column.getColumn(), new ColumnWeightData(weight, 25, true));
        return column;
    }

}
