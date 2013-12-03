/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

package de.uniol.inf.is.odysseus.rcp.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * 
 * @author Dennis Geesen Created at: 29.11.2011
 */
public abstract class AbstractWizard extends Wizard implements INewWizard {

	private boolean finishable = false;
	private IContainer selectedContainer = null;

	public boolean isFinishable() {
		return finishable;
	}

	public void setFinishable(boolean finishable) {
		this.finishable = finishable;
	}

	@Override
	public boolean canFinish() {
		return finishable;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
		this.selectedContainer = getFolder((IResource)selection.getFirstElement());
		System.out.println(this.selectedContainer.getFullPath());
	}

	private static IContainer getFolder(IResource resource) {
		if (resource instanceof IContainer)
			return (IContainer) resource;
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			return file.getParent();
		}
		throw new IllegalArgumentException("unknown resource-type:" + resource.getClass().getName());

	}

	public IContainer getSelectedContainer() {
		return selectedContainer;
	}

	public void setSelectedContainer(IContainer selectedContainer) {
		this.selectedContainer = selectedContainer;
	}
	
	public String getFullPath(){
		if(this.selectedContainer!=null){
			return this.selectedContainer.getLocation().toString();
		}
		
		return System.getProperty("user.home");
	}

}
