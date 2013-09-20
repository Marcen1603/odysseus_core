package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class OperatorGraphEditorContributor extends MultiPageEditorActionBarContributor {

	private List<String> globalActionKeys = new ArrayList<String>();
	private List<RetargetAction> retargetActions = new ArrayList<RetargetAction>();
	private ActionRegistry registry = new ActionRegistry();

	/**
	 * Initialization
	 */
	public void init(IActionBars bars) {
		buildActions();
		declareGlobalActionKeys();
		super.init(bars);
	}

	/**
	 * Builds the actions.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
	}

	/**
	 * Adds the retarded actions.
	 * 
	 * @param action
	 *            The action to add
	 */
	protected void addRetargetAction(RetargetAction action) {
		addAction(action);
		retargetActions.add(action);
		getPage().addPartListener(action);
		addGlobalActionKey(action.getId());
	}

	/**
	 * Adds global action key.
	 * 
	 * @param key
	 *            The key to add
	 */
	protected void addGlobalActionKey(String key) {
		globalActionKeys.add(key);
	}

	/**
	 * Adds to action registry an action.
	 * 
	 * @param action
	 *            The action to add
	 */
	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}

	/**
	 * Gets the registry.
	 * 
	 * @return ActionRegistry The registry
	 */
	protected ActionRegistry getActionRegistry() {
		return registry;
	}

	/**
	 * Declares the global action keys.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.UNDO.getId());
		addGlobalActionKey(ActionFactory.REDO.getId());
		addGlobalActionKey(ActionFactory.COPY.getId());
		addGlobalActionKey(ActionFactory.PASTE.getId());
		addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
		addGlobalActionKey(ActionFactory.DELETE.getId());
	}

	protected IAction getAction(String id) {
		return getActionRegistry().getAction(id);
	}

	/**
	 * Adds the undo and redo items to the toolbar.
	 * 
	 * @param tbm
	 *            The IToolBarManager
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager tbm) {
		tbm.add(getAction(ActionFactory.UNDO.getId()));
		tbm.add(getAction(ActionFactory.REDO.getId()));
		tbm.add(getAction(ActionFactory.DELETE.getId()));

	}

	/**
	 * Sets the page to active status.
	 * 
	 * @param activeEditor
	 *            The active editor
	 */
	public void setActivePage(IEditorPart activeEditor) {
		if (activeEditor != null) {
			ActionRegistry registry = (ActionRegistry) activeEditor.getAdapter(ActionRegistry.class);
			if (registry != null) {
				IActionBars bars = getActionBars();
				for (int i = 0; i < globalActionKeys.size(); i++) {
					String id = (String) globalActionKeys.get(i);
					bars.setGlobalActionHandler(id, registry.getAction(id));
				}
				getActionBars().updateActionBars();
			}
		}
	}

}
