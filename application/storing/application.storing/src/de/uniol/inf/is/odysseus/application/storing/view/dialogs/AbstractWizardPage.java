/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.application.storing.view.dialogs;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 * @author Dennis Geesen Created at: 29.11.2011
 */
public abstract class AbstractWizardPage extends WizardPage implements Listener {

	protected AbstractWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void handleEvent(Event event) {
		if (canFinish()) {
			((AbstractWizard) getWizard()).setFinishable(true);
		}
		getWizard().getContainer().updateButtons();

	}

	public boolean isActivePage() {
		return this.isCurrentPage();
	}

	abstract public boolean canFinish();

	public abstract void performFinish();

}
