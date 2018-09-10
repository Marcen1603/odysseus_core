package de.uniol.inf.is.odysseus.rcp.editor.script;

import org.eclipse.core.resources.IFile;

public interface IVisualOdysseusScriptContainer {

	public void layoutAll();
	public void setDirty(boolean dirty);
	public void setTitleText(String title);
	
	public IFile getFile();
}
