package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class OperatorGraphEditorContributor extends MultiPageEditorActionBarContributor {

	private ActionRegistry registry = new ActionRegistry();

	private ActionRegistry activePageRegistry;

	private ActionRegistry rootEditorRegistry;

	protected IEditorPart rootEditor;

	private List<RetargetAction> retargetActions = new ArrayList<RetargetAction>();

	private Set<String> globalActionKeys = new HashSet<String>();

	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}

	public void addGlobalActionKey(String key) {
		globalActionKeys.add(key);
	}

	public void addRetargetAction(RetargetAction action) {
		addAction(action);
		retargetActions.add(action);
		getPage().addPartListener(action);
		addGlobalActionKey(action.getId());
	}

	protected void buildActions() {
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
		
		RetargetAction cra = new RetargetAction(ActionFactory.COPY.getId(), "&Copy");
		cra.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		cra.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));		
		addRetargetAction(cra);
		
		addRetargetAction(new RetargetAction(ActionFactory.PASTE.getId(), "&Paste"));
		RetargetAction pra = new RetargetAction(ActionFactory.PASTE.getId(), "&Paste");
		pra.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		pra.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));		
		addRetargetAction(pra);

	}

	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.PRINT.getId());
		addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
		addGlobalActionKey(ActionFactory.COPY.getId());
		addGlobalActionKey(ActionFactory.PASTE.getId());
		addGlobalActionKey(ActionFactory.DELETE.getId());
	}

	@Override
	public void dispose() {
		for (int i = 0; i < retargetActions.size(); i++) {
			RetargetAction action = retargetActions.get(i);
			getPage().removePartListener(action);
			action.dispose();
		}
		registry.dispose();
		retargetActions = null;
		registry = null;
	}

	protected IAction getAction(String id) {
		return registry.getAction(id);
	}

	protected ActionRegistry getActionRegistry() {
		return registry;
	}

	@Override
	public void init(IActionBars bars) {
		buildActions();
		declareGlobalActionKeys();
		super.init(bars);
	}

	@Override
	public void setActivePage(IEditorPart editor) {
		if (editor != null) {
			activePageRegistry = editor.getAdapter(ActionRegistry.class);
			connectActions();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveEditor(IEditorPart editor) {
		super.setActiveEditor(editor);
		rootEditor = editor;
		rootEditorRegistry = editor.getAdapter(ActionRegistry.class);
		connectActions();
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.COPY.getId()));
		toolBarManager.add(getAction(ActionFactory.PASTE.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));		
		toolBarManager.add(new Separator());
		
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));			

	}

	@Override
	public void contributeToMenu(IMenuManager menubar) {
		super.contributeToMenu(menubar);
		MenuManager viewMenu = new MenuManager("View", "view");
		viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
		menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
	}

	protected void connectActions() {
		IActionBars bars = getActionBars();
		Iterator<String> iter = globalActionKeys.iterator();
		while (iter.hasNext()) {
			String id = iter.next();
			bars.setGlobalActionHandler(id, getEditorAction(id));
			bars.updateActionBars();
		}
	}

	protected IAction getEditorAction(String key) {
		IAction action = null;

		if (activePageRegistry != null) {
			action = activePageRegistry.getAction(key);
		}
		if (action == null && rootEditorRegistry != null) {
			action = rootEditorRegistry.getAction(key);
		}
		return action;
	}

}
