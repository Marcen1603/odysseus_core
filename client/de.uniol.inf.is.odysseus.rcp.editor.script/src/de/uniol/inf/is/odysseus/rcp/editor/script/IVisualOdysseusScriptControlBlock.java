package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

public interface IVisualOdysseusScriptControlBlock {

	public Collection<String> getStartKeywords();
	public Collection<String> getInbetweenKeywords();
	public Collection<String> getEndKeywords();
	
	public void onStart( String startKeyword, String startKeywordParameters ) throws VisualOdysseusScriptException;
	public void onInbetween( String inbetweenKeyword, String inbetweenKeywordParameters ) throws VisualOdysseusScriptException;
	public void onEnd( String endKeyword, String endKeywordParameters ) throws VisualOdysseusScriptException;
	
	public List<Composite> createPartControl( Composite parent );
	public void dispose();
	
	public String generateOdysseusScriptStart() throws VisualOdysseusScriptException;
	public String generateOdysseusScriptInbetween() throws VisualOdysseusScriptException;
	public String generateOdysseusScriptEnd() throws VisualOdysseusScriptException;

}
