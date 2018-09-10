/**
 * 
 */
package de.uniol.inf.is.odysseus.rcp.views.access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TransportsView extends ViewPart {
    private static final Logger LOG = LoggerFactory.getLogger(TransportsView.class);

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
                return TransportsView.this.compareElements(e1, e2);
            }
        });
        this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {

                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
                        try {
                            insertSelection((String) iterator.next());
                        }
                        catch (BadLocationException e) {
                            LOG.error(e.getMessage(), e);
                        }

                    }
                }

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
                    final List<String> transports = new ArrayList<>(TransportHandlerRegistry.instance.getHandlerNames());
                    Collections.sort(transports);
                    TransportsView.this.tableViewer.setInput(transports);
                    TransportsView.this.tableViewer.refresh();
                }
            });
        }
    }


    private void createColumns(final TableViewer tableViewer, final TableColumnLayout tableColumnLayout) {
        final TableViewerColumn symbolColumn = TransportsView.createColumn(tableViewer, tableColumnLayout, OdysseusNLS.Symbol, new CellLabelProvider() {
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
                TransportsView.this.tableColumnClicked((TableColumn) e.widget);
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
            
            result = c1.compareTo(c2);
        }
        return table.getSortDirection() == SWT.UP ? result : -result;
    }
    
    private void insertSelection(String element) throws BadLocationException {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof ITextEditor) {
            ISelectionProvider selectionProvider = ((ITextEditor) editor).getSelectionProvider();
            ISelection selection = selectionProvider.getSelection();
            final IDocumentProvider documentProvider = ((ITextEditor) editor).getDocumentProvider();
            final IDocument document = documentProvider.getDocument(editor.getEditorInput());
            if (selection instanceof ITextSelection) {
                ITextSelection textSelection = (ITextSelection) selection;
                document.replace(textSelection.getOffset(), 0, element);

            }
        }
    }
}
