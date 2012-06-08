package de.uniol.inf.is.odysseus.rcp.viewer.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.server.RCPViewerServerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak.OperatorBreak;
import de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak.OperatorBreakManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class BreakOperatorCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = window.getActivePage().getSelection();

		if (selection == null)
			return null;

		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;

			OperatorBreak ob = null;

			Object selObject = structSelection.getFirstElement();
			if (selObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView node = (IOdysseusNodeView) selObject;

				IPhysicalOperator operator = node.getModelNode().getContent();

				// Schauen, ob schon darauf ein Break ist
				for (OperatorBreak o : OperatorBreakManager.getInstance().getAll()) {
					if (o.getOperator() == operator) {
						ob = o;
						break;
					}
				}

				if (ob == null) {
					// Es können nur hinter Quellen Breaks gesetzt werden
					if (operator instanceof ISource) {
						final ISource<?> src = (ISource<?>) operator;
						ob = new OperatorBreak(src);
						OperatorBreakManager.getInstance().add(ob);
						
						// View anzeigen
						try {
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RCPViewerServerPlugIn.OPERATOR_BREAK_VIEW_ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						
					} else {
						MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
						box.setMessage("Only sources can be breaked");
						box.setText("Error");
						box.open();
					}

				}
			} else if (selObject instanceof OperatorBreak)
				ob = (OperatorBreak) selObject;

			if (ob != null) {
				if (!ob.isBreaked()) {
					if (ob.startBreak())
						return true;
                    System.out.println("Could not start breaking");
                    OperatorBreakManager.getInstance().remove(ob);
				} else
					System.out.println("Already breaking");

			} else
				System.out.println("No OperatorBreak found");

		}

		return false;
	}

}
