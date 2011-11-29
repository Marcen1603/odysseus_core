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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * 
 * @author Dennis Geesen
 * Created at: 29.11.2011
 */
public abstract class AbstractWizard extends Wizard {

	@Override
	public boolean performFinish() {
		for(IWizardPage page :super.getPages()){
			if(page instanceof AbstractWizardPage){
			AbstractWizardPage p = (AbstractWizardPage)page;
				if(p.isActivePage()){
					p.performFinish();
					return true;
				}
			}
		}
		return false;
	}
	
	abstract public void setFinishable(boolean value);	

}
