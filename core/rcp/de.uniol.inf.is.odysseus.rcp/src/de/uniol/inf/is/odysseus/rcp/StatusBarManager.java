/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;

@SuppressWarnings("restriction")
public class StatusBarManager implements IWindowListener {

	private static StatusBarManager instance = null;

	public static final String EXECUTOR_ID = "executorStatus";
	public static final String SCHEDULER_ID = "schedulerStatus";
	public static final String USER_ID = "userStatus";

	private IStatusLineManager manager;

	private Map<String, String> cache = new HashMap<String, String>();
	private String msgCache;

	private StatusBarManager() {
	}

	public static StatusBarManager getInstance() {
		if (instance == null) {
			instance = new StatusBarManager();
		}
		return instance;
	}

	private void tryToRegisterListener() {
		if (PlatformUI.isWorkbenchRunning()) {
			PlatformUI.getWorkbench().addWindowListener(this);
		}
	}

	private void initManager() {
		StatusLineContributionItem item = new StatusLineContributionItem(EXECUTOR_ID, 30);
		StatusLineContributionItem item2 = new StatusLineContributionItem(SCHEDULER_ID, 60);
		StatusLineContributionItem item3 = new StatusLineContributionItem(USER_ID, 40);

		manager.add(item);
		manager.add(item2);
		manager.add(item3);

		setStandardMessages();
		applyCache();

	}

	private void setStandardMessages() {
		setMessage(StatusBarManager.EXECUTOR_ID, "No executor found");
		setMessage(StatusBarManager.SCHEDULER_ID, "No scheduler found");
		setMessage(StatusBarManager.USER_ID, "anonymous");
	}

	private void applyCache() {
		if (msgCache != null)
			setMessage(msgCache);

		for (String key : cache.keySet())
			setMessage(key, cache.get(key));
		cache.clear();
	}

	public void setMessage(final String itemID, final String message) {		
		tryToRegisterListener();
		if (manager == null) {
			cache.put(itemID, message);
			return;
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IContributionItem[] items = manager.getItems();
				for (IContributionItem i : items) {
					if (i.getId().equals(itemID)) {
						((StatusLineContributionItem) i).setText(message);
					}
				}
			}

		});
	}

	public void setMessage(final String message) {
		tryToRegisterListener();
		if (manager == null) {
			msgCache = message;
			return;
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {

				manager.setMessage(message);
			}
		});
	}

	@Override
	public void windowActivated(IWorkbenchWindow window) {

	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {

	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {

	}

	
	@Override
	public void windowOpened(IWorkbenchWindow window) {		
		WorkbenchPage wp = (WorkbenchPage) window.getActivePage();				
		IActionBars ab = wp.getActionBars();
		this.manager = ab.getStatusLineManager();		
		initManager();

	}

}
