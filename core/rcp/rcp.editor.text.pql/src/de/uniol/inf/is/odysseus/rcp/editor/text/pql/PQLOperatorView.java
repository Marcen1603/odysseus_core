package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;

public class PQLOperatorView extends ViewPart {

	private TreeViewer treeViewer;
	private boolean showOptionalParameters = true;
	private boolean alphaSort = false;

	private PQLOperatorsContentProvider contentProvider;

	@Override
	public void createPartControl(Composite parent) {
		contentProvider = new PQLOperatorsContentProvider();

		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new PQLOperatorsLabelProvider());

		treeViewer.setInput(determineInput());
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

	private List<IOperatorBuilder> determineInput() {
		IOperatorBuilderFactory factory = PQLEditorTextPlugIn.getOperatorBuilderFactory();
		return factory != null ? Lists.newArrayList(factory.getOperatorBuilder()) : Lists.<IOperatorBuilder>newArrayList();
	}

	private static List<IOperatorBuilder> sortInput(List<IOperatorBuilder> unsortedList) {
		Collections.sort(unsortedList, new Comparator<IOperatorBuilder>() {
			@Override
			public int compare(IOperatorBuilder o1, IOperatorBuilder o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return unsortedList;
	}
}
