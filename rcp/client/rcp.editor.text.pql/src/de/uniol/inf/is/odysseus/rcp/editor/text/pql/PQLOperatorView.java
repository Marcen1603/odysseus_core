package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.actions.OpenWikiAction;

public class PQLOperatorView extends ViewPart {
    private static final Logger LOG = LoggerFactory.getLogger(PQLOperatorView.class);

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

        // Contextmenu
        final MenuManager menuManager = new MenuManager();
        final Menu contextMenu = menuManager.createContextMenu(treeViewer.getControl());

        menuManager.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                if (treeViewer.getSelection().isEmpty()) {
                    return;
                }

                if (treeViewer.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                    Object object = selection.getFirstElement();

                    if (object instanceof LogicalOperatorInformation) {
                        Action openWikiAction = new OpenWikiAction((LogicalOperatorInformation) object);
                        openWikiAction.setText("Documentation");
                        openWikiAction.setToolTipText("Opens the documentation in the Odysseus wiki");
                        manager.add(openWikiAction);
                    }
                }
            }
        });
        menuManager.setRemoveAllWhenShown(true);

        // Set the MenuManager
        treeViewer.getControl().setMenu(contextMenu);
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {

                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
                        try {
                            Object value = iterator.next();
                            if (value instanceof LogicalOperatorInformation) {
                                insertSelection((LogicalOperatorInformation) value);
                            }
                            else if (value instanceof LogicalParameterInformation) {
                                insertSelection((LogicalParameterInformation) value);
                            }
                        }
                        catch (BadLocationException e) {
                            LOG.error(e.getMessage(), e);
                        }

                    }
                }

            }

        });
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

    private void insertSelection(LogicalOperatorInformation element) throws BadLocationException {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof ITextEditor) {
            ISelectionProvider selectionProvider = ((ITextEditor) editor).getSelectionProvider();
            ISelection selection = selectionProvider.getSelection();
            final IDocumentProvider documentProvider = ((ITextEditor) editor).getDocumentProvider();
            final IDocument document = documentProvider.getDocument(editor.getEditorInput());
            if (selection instanceof ITextSelection) {
                ITextSelection textSelection = (ITextSelection) selection;
                document.replace(textSelection.getOffset(), 0, element.getOperatorName());

            }
        }
    }

    private void insertSelection(LogicalParameterInformation element) throws BadLocationException {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof ITextEditor) {
            ISelectionProvider selectionProvider = ((ITextEditor) editor).getSelectionProvider();
            ISelection selection = selectionProvider.getSelection();
            final IDocumentProvider documentProvider = ((ITextEditor) editor).getDocumentProvider();
            final IDocument document = documentProvider.getDocument(editor.getEditorInput());
            if (selection instanceof ITextSelection) {
                ITextSelection textSelection = (ITextSelection) selection;
                document.replace(textSelection.getOffset(), 0, element.getName());

            }
        }
    }
}
