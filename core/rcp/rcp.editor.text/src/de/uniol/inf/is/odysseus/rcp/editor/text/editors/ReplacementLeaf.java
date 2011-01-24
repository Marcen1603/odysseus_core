package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReplacementLeaf {

	private List<String> replace = new LinkedList<String>(); 
	
	public ReplacementLeaf( Map<String, String> replacements ) {
		for( String key : replacements.keySet()) {
			replace.add( key + " = " + replacements.get(key));
		}
	}
	
	public List<String> getReplacements() {
		return replace;
	}
}
