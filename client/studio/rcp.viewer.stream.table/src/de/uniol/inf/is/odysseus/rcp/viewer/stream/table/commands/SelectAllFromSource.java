package de.uniol.inf.is.odysseus.rcp.viewer.stream.table.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.table.StreamTable50Editor;

public class SelectAllFromSource extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(SelectAllFromSource.class);

	private static final String STREAM_EDITOR_TYPE = "de.uniol.inf.is.odysseus.rcp.viewer.stream.Table20";

	//@SuppressWarnings("cast")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			List<ViewInformation> selectedSourceEntries = getSelection();

			ISession activeUser = OdysseusRCPPlugIn.getActiveSession();

			if (!selectedSourceEntries.isEmpty()) {
				if (selectedSourceEntries.get(0) instanceof ViewInformation) {
					for (ViewInformation sourceEntry : selectedSourceEntries) {
						Resource sourceName = sourceEntry.getName();

						Collection<Integer> queryIDs = createQueryToSelectAllDataFromSource(sourceName, activeUser);
						openEditor(queryIDs, event);
						startQueries(queryIDs, activeUser);
					}
				} else {
					throw new IllegalArgumentException("Called Select all data with " + selectedSourceEntries);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return null;
	}

	private static Collection<Integer> createQueryToSelectAllDataFromSource(Resource sourceName, ISession caller) {
		return OdysseusRCPPlugIn.getExecutor().addQuery(sourceName + "_out = "+ sourceName , "PQL", caller, Context.empty());
	}

	private static void startQueries(Collection<Integer> queryIDs, ISession caller) {
		for (Integer queryID : queryIDs) {
			try {
				OdysseusRCPPlugIn.getExecutor().startQuery(queryID, caller);
			} catch (Throwable t) {
				LOG.error("Could not start query for viewing source data", t);
			}
		}
	}

	private static void openEditor(Collection<Integer> queryIDs, ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		for (Integer queryID : queryIDs) {
			IPhysicalOperator physicalOperator = getFirstPhysialRoot(queryID);

			if (!activateEditorIfNeeded(page, physicalOperator)) {

				IStreamEditorType editor = new StreamTable50Editor();
				StreamEditorInput input = new StreamEditorInput(physicalOperator, editor, STREAM_EDITOR_TYPE, "Table", queryID);

				try {
					page.openEditor(input, "de.uniol.inf.is.odysseus.rcp.editors.StreamEditor");
				} catch (PartInitException ex) {
					LOG.error("Could not open editor", ex);
				}
			}
		}
	}

	private static boolean activateEditorIfNeeded(IWorkbenchPage page, IPhysicalOperator physicalOperator) {
		for (IEditorReference editorRef : page.getEditorReferences()) {
			try {
				IEditorInput i = editorRef.getEditorInput();
				if (i instanceof StreamEditorInput) {
					StreamEditorInput gInput = (StreamEditorInput) i;
					if (gInput.getPhysicalOperator() == physicalOperator && gInput.getEditorTypeID().equals(STREAM_EDITOR_TYPE)) {
						page.activate(editorRef.getPart(false));
						return true;
					}
				}
			} catch (PartInitException ex) {
				LOG.error("Could not see, if an editor for stream data is open", ex);
			}
		}

		return false;
	}

	private static IPhysicalOperator getFirstPhysialRoot(Integer queryID) {
		return OdysseusRCPPlugIn.getExecutor().getPhysicalRoots(queryID, OdysseusRCPPlugIn.getActiveSession()).iterator().next();
	}

	// private static String getRealSourcename(Resource sourceName) {
	// int pos = sourceName.indexOf(".");
	// if( pos != -1 ) {
	// return sourceName.substring(pos + 1);
	// }
	//
	// return sourceName;
	// }

	private static <T> List<T> getSelection() {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
		try {
			if (selection instanceof IStructuredSelection) {
				List<T> items = new ArrayList<T>();
				IStructuredSelection structSelection = (IStructuredSelection) selection;
				Iterator<?> iter = structSelection.iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					T item = (T) iter.next();
					items.add(item);
				}
				return items;
			}
		} catch (Throwable t) {
			LOG.error("Could not determine selection", t);
		}
		return new ArrayList<T>();
	}
}
