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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.CopyAction;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.PictogramCopyCommand;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.PictogramDeleteCommand;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.GraphicsLayer;

/**
 * @author DGeesen
 * 
 */
public class PictogramComponentEditPolicy extends ComponentEditPolicy {

	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	@Override
	public Command getCommand(Request request) {
		if(request.getType().equals(CopyAction.GRAPHICS_COPY_ACTION)){
			return createCopyCommand();
		}
		return super.getCommand(request);
	}	
	
	private Command createCopyCommand() {
		PictogramCopyCommand copyCommand = new PictogramCopyCommand();		
		copyCommand.setPictogram((AbstractPictogram) getHost().getModel());
		return copyCommand;
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		PictogramDeleteCommand deleteCommand = new PictogramDeleteCommand();
		deleteCommand.setPictogramGroup((GraphicsLayer) getHost().getParent().getModel());
		deleteCommand.setPictogram((AbstractPictogram) getHost().getModel());
		return deleteCommand;
	}
	
}
