package de.uniol.inf.is.odysseus.net.rcp.views;

import java.util.Collection;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Preconditions;

public abstract class AbstractTableViewer<T> {

	private TableViewer tableViewer;
	private boolean refreshing = false;

	public AbstractTableViewer(Composite parent, Collection<T> dataContainer) {
		Preconditions.checkNotNull(parent, "parent must not be null!");
		Preconditions.checkNotNull(dataContainer, "dataContainer must not be null!");

		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		createColumns(tableViewer, tableColumnLayout);
		
		hideSelectionIfNeeded(tableViewer);

		tableViewer.setInput(dataContainer);

		tableViewer.refresh();
	}

	protected abstract void createColumns(TableViewer tableViewer, TableColumnLayout tableColumnLayout);

	public void dispose() {
		tableViewer.getTable().dispose();
	}

	private static void hideSelectionIfNeeded(final TableViewer tableViewer) {
		tableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (tableViewer.getTable().getItem(new Point(e.x, e.y)) == null) {
					tableViewer.setSelection(new StructuredSelection());
				}
			}
		});
	}

	public final TableViewer getTableViewer() {
		return tableViewer;
	}

	public final void refreshTableAsync() {
		if (refreshing) {
			return;
		}

		if (!tableViewer.getTable().isDisposed()) {

			refreshing = true;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!tableViewer.getTable().isDisposed()) {
						tableViewer.refresh();
					}
					refreshing = false;
				}
			});
		}
	}
}
