package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.util.List;

import com.google.common.collect.ImmutableList;

public interface IOdysseusScriptTransformRule {

	public String getName();
	public int getPriority();
	
	public List<OdysseusScriptBlock> determineExecutableBlocks( ImmutableList<OdysseusScriptBlock> blocks);
	public IVisualOdysseusScriptTextBlock transform( List<OdysseusScriptBlock> blocksToTransform);
	
}
