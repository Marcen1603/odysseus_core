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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;

/**
 * @author DGeesen
 *
 */
public class PictogramCopyCommand extends Command{
	
	private AbstractPictogram pictogram;	
	private Object oldContents;
	
	@Override
	public void execute() {
		this.oldContents = Clipboard.getDefault().getContents();
		Clipboard.getDefault().setContents(pictogram);		
	}
	@Override
	public void undo() {	
		Clipboard.getDefault().setContents(oldContents);
	}	

	public AbstractPictogram getPictogram() {
		return pictogram;
	}

	public void setPictogram(AbstractPictogram pictogram) {
		this.pictogram = pictogram;
	}	

}
