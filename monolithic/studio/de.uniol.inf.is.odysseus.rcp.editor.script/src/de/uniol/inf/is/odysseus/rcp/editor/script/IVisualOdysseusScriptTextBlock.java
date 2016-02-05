package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;

public interface IVisualOdysseusScriptTextBlock {

	public Collection<String> getStartKeywords();
	
	public void init( String startKeyword, String odysseusScriptText ) throws VisualOdysseusScriptException;
	
	public void createPartControl( Composite parent );
	public void dispose();
	
	public String generateOdysseusScript() throws VisualOdysseusScriptException;
	
}
