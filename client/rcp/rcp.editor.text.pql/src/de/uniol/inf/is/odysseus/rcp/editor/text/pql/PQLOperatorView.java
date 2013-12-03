package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class PQLOperatorView extends ViewPart {

	private TreeViewer treeViewer;
	private boolean showOptionalParameters = true;
	private boolean alphaSort = true;

	private PQLOperatorsContentProvider contentProvider;

	@Override
	public void createPartControl(Composite parent) {
		contentProvider = new PQLOperatorsContentProvider();

		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(contentProvider);
		ColumnViewerToolTipSupport.enableFor(treeViewer);
		treeViewer.setLabelProvider(new PQLOperatorsLabelProvider());

		treeViewer.setInput(sortInput(determineInput()));
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.refresh();
			}
		});
	}

	public void expandAll() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.expandAll();
			}
		});

	}

	public void collapseAll() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.collapseAll();
			}

		});
	}

	public void toggleShowOptionalParameters() {
		showOptionalParameters = !showOptionalParameters;
		contentProvider.showOptionalParameters(showOptionalParameters);
		refresh();
	}

	public void toogleAlphaOrder() {
		alphaSort = !alphaSort;
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				Object input = alphaSort ? sortInput(determineInput()) : determineInput();
				treeViewer.setInput(input);
			}
		});
		refresh();
	}

	private List<LogicalOperatorInformation> determineInput() {
		return OdysseusRCPPlugIn.getExecutor().getOperatorInformations(OdysseusRCPPlugIn.getActiveSession());		
	}

	private static List<LogicalOperatorInformation> sortInput(List<LogicalOperatorInformation> unsortedList) {
		Collections.sort(unsortedList, new Comparator<LogicalOperatorInformation>() {
			@Override
			public int compare(LogicalOperatorInformation o1, LogicalOperatorInformation o2) {
				return o1.getOperatorName().compareTo(o2.getOperatorName());
			}
		});
		return unsortedList;
	}
}
