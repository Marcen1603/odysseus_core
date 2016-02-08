package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

public interface IVisualOdysseusScriptTextBlock {

	public Collection<String> getStartKeywords();
	
	public void init( String startKeyword, String odysseusScriptText, List<IVisualOdysseusScriptTextBlock> previousBlocks ) throws VisualOdysseusScriptException;
	
	public void createPartControl( Composite parent, IVisualOdysseusScriptContainer container );
	public void dispose();
	
	public String generateOdysseusScript() throws VisualOdysseusScriptException;

	
}
