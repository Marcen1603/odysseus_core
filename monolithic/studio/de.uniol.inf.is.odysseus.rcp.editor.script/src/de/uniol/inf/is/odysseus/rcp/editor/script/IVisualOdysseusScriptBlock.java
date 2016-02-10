package de.uniol.inf.is.odysseus.rcp.editor.script;

import org.eclipse.swt.widgets.Composite;

public interface IVisualOdysseusScriptBlock {

	public void createPartControl( Composite parent, IVisualOdysseusScriptContainer container );
	public void dispose();
	
	public String generateOdysseusScript() throws VisualOdysseusScriptException;

	
}
