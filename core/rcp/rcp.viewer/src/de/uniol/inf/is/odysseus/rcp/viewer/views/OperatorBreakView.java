package de.uniol.inf.is.odysseus.rcp.viewer.views;

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.IOperatorBreakListener;
import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.IOperatorBreakManagerListener;
import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.OperatorBreak;
import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.OperatorBreakManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class OperatorBreakView extends ViewPart implements IOperatorBreakManagerListener, IOperatorBreakListener, ISelectionListener {

	private TableViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {

		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		// OPERATOR-NAME
		TableViewerColumn operatorColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		operatorColumn.getColumn().setText("Operator");
		operatorColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				OperatorBreak ob = (OperatorBreak) cell.getElement();
				cell.setText(ob.getOperator().getName());
			}
		});
		tableColumnLayout.setColumnData(operatorColumn.getColumn(), new ColumnWeightData(5, 25, true));

		// LÄUFT?
		TableViewerColumn breakedColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		breakedColumn.getColumn().setText("Breaked");
		breakedColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				OperatorBreak ob = (OperatorBreak) cell.getElement();
				cell.setText(String.valueOf(ob.isBreaked()));
			}
		});
		tableColumnLayout.setColumnData(breakedColumn.getColumn(), new ColumnWeightData(1, 25, true));

		// VORGEHALTENE DATEN
		TableViewerColumn elementColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		elementColumn.getColumn().setText("#Data");
		elementColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				OperatorBreak ob = (OperatorBreak) cell.getElement();
				if (ob.getBuffer() != null)
					cell.setText(String.valueOf(ob.getBuffer().size()));
			}
		});
		tableColumnLayout.setColumnData(elementColumn.getColumn(), new ColumnWeightData(1, 25, true));

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(OperatorBreakManager.getInstance().getAll());
		getSite().setSelectionProvider(tableViewer);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(tableViewer.getTable());
		// Set the MenuManager
		tableViewer.getTable().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, tableViewer);

		OperatorBreakManager.getInstance().addListener(this);
	}

	@Override
	public void dispose() {
		OperatorBreakManager.getInstance().removeListener(this);
		for (OperatorBreak ob : OperatorBreakManager.getInstance().getAll()) {
			ob.removeListener(this);
		}

		super.dispose();
	}

	protected void refreshTable() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewer.refresh();
			}
		});
	}
	
	protected void selectInTable( final OperatorBreak ob ) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if( ob != null ) 
					tableViewer.setSelection(new StructuredSelection(ob));
				else
					tableViewer.setSelection(new StructuredSelection());
			}
		});
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	@Override
	public void operatorBreakAdded(OperatorBreakManager manager, OperatorBreak ob) {
		ob.addListener(this);
		refreshTable();
	}

	@Override
	public void operatorBreakRemoved(OperatorBreakManager manager, OperatorBreak ob) {
		ob.removeListener(this);
		refreshTable();
	}

	@Override
	public void breakStarted(OperatorBreak ob) {
		refreshTable();
	}

	@Override
	public void breakStopped(OperatorBreak ob) {
		refreshTable();
	}

	@Override
	public void sizeChanged(OperatorBreak ob) {
		refreshTable();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part != this) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structSelection = (IStructuredSelection) selection;
				List<?> selectedObjects = structSelection.toList();
				if (selectedObjects.size() == 1) {
					Object selObject = structSelection.getFirstElement();

					if (selObject instanceof IOdysseusNodeView) {
						IOdysseusNodeView node = (IOdysseusNodeView) selObject;

						if (node.getModelNode() != null && node.getModelNode().getContent() != null) {
							IPhysicalOperator op = node.getModelNode().getContent();
							
							for( OperatorBreak ob : OperatorBreakManager.getInstance().getAll() ) {
								if( ob.getOperator() == op ) {
									selectInTable(ob);
									return;
								}
							}
						}
					}

				}
				selectInTable(null);

			}
		}
	}

}
