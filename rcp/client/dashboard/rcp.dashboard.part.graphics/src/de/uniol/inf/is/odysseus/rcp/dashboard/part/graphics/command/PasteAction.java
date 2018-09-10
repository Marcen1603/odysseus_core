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
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;

/**
 * @author DGeesen
 * 
 */
public class PasteAction extends WorkbenchPartAction {

	public static final String GRAPHICS_PASTE_ACTION = "GRAPHICS_PASTE_ACTION";

	/**
	 * @param part
	 */
	public PasteAction(IWorkbenchPart part) {
		super(part);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		Command cmd = createPasteCopyCommand();
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}

	@Override
	public void run() {
		execute(createPasteCopyCommand());
	}

	/**
	 * @param selectedObjects
	 * @return
	 */	
	private Command createPasteCopyCommand() {
		if(!(Clipboard.getDefault().getContents() instanceof AbstractPictogram)){
			return null;
		}		
		return new PictogramPasteCommand();		
	}

	@Override
	protected void init() {
		super.init();
		setText("&Paste");
		setToolTipText("Paste");
		setId(ActionFactory.PASTE.getId());
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(false);
	}
}
