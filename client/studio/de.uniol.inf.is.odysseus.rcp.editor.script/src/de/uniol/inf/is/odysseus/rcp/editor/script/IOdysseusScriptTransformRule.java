package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.util.List;

import com.google.common.collect.ImmutableList;

public interface IOdysseusScriptTransformRule {

	public String getName();
	public int getPriority();
	
	public  List<OdysseusScriptBlock> determineTransformableBlocks( ImmutableList<OdysseusScriptBlock> blocks);
	public IVisualOdysseusScriptBlock transform( List<OdysseusScriptBlock> allBlocks, List<OdysseusScriptBlock> selectedBlocks) throws VisualOdysseusScriptException;
	
}
