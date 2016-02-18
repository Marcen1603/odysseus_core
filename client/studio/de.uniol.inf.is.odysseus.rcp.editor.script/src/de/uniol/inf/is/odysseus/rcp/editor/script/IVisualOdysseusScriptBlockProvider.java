package de.uniol.inf.is.odysseus.rcp.editor.script;

import org.eclipse.swt.graphics.Image;

public interface IVisualOdysseusScriptBlockProvider {

	public String getName();
	public Image getImage();
	
	public IVisualOdysseusScriptBlock create();
	
}
