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
package de.uniol.inf.is.odysseus.rcp.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class OdysseusProjectWizard extends BasicNewProjectResourceWizard {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusProjectWizard.class);
	
	@Override
	public boolean performFinish() {
		boolean result = super.performFinish();
		result &= configureProject(getNewProject());
		return result;
	}

	private static boolean configureProject(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = OdysseusRCPPlugIn.ODYSSEUS_PROJECT_NATURE_ID;
			description.setNatureIds(newNatures);
			
			if( !project.isSynchronized(IResource.DEPTH_ZERO) ) {
				project.refreshLocal(IResource.DEPTH_ZERO, null);
			}
			
			project.setDescription(description, null);
		} catch (CoreException e) {
			LOG.error("Could not configure project {} project.", e);
			return false;
		}
		return true;
	}

}
