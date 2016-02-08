package de.uniol.inf.is.odysseus.rcp.viewer.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class RenameOperatorCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RenameOperatorCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<IPhysicalOperator> optSelectedOperator = getSelectedOperator(event);
		if( optSelectedOperator.isPresent() ) {
			final IPhysicalOperator selectedOperator = optSelectedOperator.get();
			final InputDialog inputDlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Rename " + selectedOperator.getClass().getSimpleName(), 
					"New name", 
					selectedOperator.getName(),
					new IInputValidator() {

						@Override
						public String isValid(String newText) {
							if(!Strings.isNullOrEmpty(newText) && !newText.trim().isEmpty()) {
								return null;
							}
							return "Name must not be empty";
						}
					});
		
			if( inputDlg.open() == Window.OK ) {
				final String newName = inputDlg.getValue();
				selectedOperator.setName(newName);
				
				executeCommand(OdysseusRCPViewerPlugIn.REFRESH_GRAPH_EDITOR_COMMAND_ID);
				executeCommand(OdysseusRCPViewerPlugIn.REFRESH_GRAPH_EDITOR_OUTLINE_COMMAND_ID);
			}
		}
		
		return null;
	}

	private static void executeCommand(String commandID) {
		IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(commandID, null);
		} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			LOG.error("Could not execute command {}", commandID, e);
		}
	}

	private static Optional<IPhysicalOperator> getSelectedOperator(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = window.getActivePage().getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structSelection = (IStructuredSelection) selection;
			
			Object selectedObject = structSelection.getFirstElement();
			if( selectedObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView nodeView = (IOdysseusNodeView) selectedObject;
				if( nodeView.getModelNode() != null ) {
					IOdysseusNodeModel nodeModel = nodeView.getModelNode();
					if( nodeModel.getContent() != null ) {
						return Optional.of(nodeModel.getContent());
					}
				}
			}
		}
		
		return Optional.absent();
	}

}
