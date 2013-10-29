/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.ui.actions.ActionBarContributor;
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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

/**
 * @author DGeesen
 * 
 */
public class DashboardPartEditorActionBarContributor extends ActionBarContributor {

	@Override
	public void setActiveEditor(IEditorPart editor) {
		super.setActiveEditor(editor);
		rootEditor = editor;
		rootEditorRegistry = (ActionRegistry) editor.getAdapter(ActionRegistry.class);
		connectActions();
	}

	private ActionRegistry registry = new ActionRegistry();

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
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());

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
			RetargetAction action = (RetargetAction) retargetActions.get(i);
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

	public void init(IActionBars bars) {
		buildActions();
		declareGlobalActionKeys();
		super.init(bars);
	}

	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));

	}

	public void contributeToMenu(IMenuManager menubar) {
		super.contributeToMenu(menubar);
		MenuManager viewMenu = new MenuManager("View");
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
		if (rootEditorRegistry != null) {
			return rootEditorRegistry.getAction(key);
		}
		return null;
	}

}
