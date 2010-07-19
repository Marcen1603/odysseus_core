package de.uniol.inf.is.odysseus.rcp.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class MyContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry registry;
	
	public MyContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		this.registry = registry;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		// Add standard action groups to the menu
		GEFActionConstants.addStandardActionGroups(menu);
		
		menu.appendToGroup(
				GEFActionConstants.GROUP_UNDO, // target group id
				getAction(ActionFactory.UNDO.getId())); // action to add
		menu.appendToGroup(
				GEFActionConstants.GROUP_UNDO, 
				getAction(ActionFactory.REDO.getId()));
		menu.appendToGroup(
				GEFActionConstants.GROUP_EDIT,
				getAction(ActionFactory.DELETE.getId()));
//		
//		EditPart plan = getViewer().getContents();
//		List<EditPart> children = plan.getChildren();
//		for( EditPart p : children ) {
//			OperatorEditPart part = (OperatorEditPart)p;
//			if( part.getSelected() == EditPart.SELECTED_PRIMARY ) {
//				
//				System.out.println( ((Operator)part.getModel()).getOperatorExtensionDescriptor().getLabel());
//			}
//		}
	}
	
	private IAction getAction(String actionId) {
		return registry.getAction(actionId);
	}
}
