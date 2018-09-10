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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.GraphicsLayer;

/**
 * @author DGeesen
 *
 */
public class PictogramPasteCommand extends Command{
	
	private AbstractPictogram newPictogram;		
	private GraphicsLayer graphicsLayer;
	
	@Override
	public void execute() {
		Object contents = Clipboard.getDefault().getContents();
		if(contents instanceof AbstractPictogram){
			AbstractPictogram oldOne = (AbstractPictogram) contents;			
			AbstractPictogram newOne = oldOne.clone();			
			Rectangle cons = newOne.getConstraint().getCopy();
			cons.x = cons.x + 50;
			cons.y = cons.y + 50;
			newOne.setConstraint(cons);
			graphicsLayer = oldOne.getGraphicsLayer();
			graphicsLayer.addPictogram(newOne);
			newPictogram = newOne;			
		}
			
	}
	
	@Override
	public void undo() {			
		graphicsLayer.removePictogram(newPictogram);
	}		
}
