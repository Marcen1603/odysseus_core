package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.commands;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;


public class AddSourceCommand extends AbstractHandler implements IHandler {

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		
		StreamMapEditorPart editor = (StreamMapEditorPart) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getActiveEditor();
		
//		Shell shell = editor.getScreenManager().getDisplay().getActiveShell(); 
		
//		ISession user = OdysseusRCPPlugIn.getActiveSession();
		IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		IServerExecutor serverExecutor = null;
		
		if (executor instanceof IServerExecutor) {
			serverExecutor = (IServerExecutor) executor;
			Collection<IPhysicalQuery> queries = serverExecutor.getExecutionPlan().getQueries();

//			for (IPhysicalQuery iPhysicalQuery : queries) {
//				List<IPhysicalOperator> ops = serverExecutor.getPhysicalRoots(iPhysicalQuery.getID());
//				ops.isEmpty();
//			}

			IStructuredContentProvider contentprovider = new IStructuredContentProvider() {
				Collection<IPhysicalQuery> operator;

				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					this.operator = (Collection<IPhysicalQuery>) newInput;

				}

				@Override
				public void dispose() {
					// TODO Auto-generated method stub

				}

				@Override
				public Object[] getElements(Object inputElement) {
					// TODO Auto-generated method stub
					return this.operator.toArray();
				}
			};
			
			ILabelProvider labelprovider = new ILabelProvider() {

				@Override
				public void removeListener(ILabelProviderListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean isLabelProperty(Object element, String property) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void dispose() {
					// TODO Auto-generated method stub

				}

				@Override
				public void addListener(ILabelProviderListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public String getText(Object element) {
					// TODO Auto-generated method stub
					return ((IPhysicalQuery) element).getLogicalQuery().getQueryText();
				}

				@Override
				public Image getImage(Object element) {
					// TODO Auto-generated method stub
					return null;
				}
			};
			ListSelectionDialog dlg = new ListSelectionDialog(Display.getCurrent().getActiveShell(), queries, contentprovider, labelprovider, "Select the resources to save:");

			// dlg..setInitialSelections(dirtyEditors);
			dlg.setTitle("Select Map Stream Connections");
			dlg.open();
			Object[] sel = dlg.getResult();
			for (Object object : sel) {
				IPhysicalQuery op = (IPhysicalQuery) object;
				editor.addConnection(op);

			}
		}

		return null;
    }

}
