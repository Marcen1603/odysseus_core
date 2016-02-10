package de.uniol.inf.is.odysseus.rcp.editor.script.rules;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRule;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.OdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.DefinesVisualOdysseusScriptBlock;

public class DefineKeywordTransformRule implements IOdysseusScriptTransformRule {

	@Override
	public String getName() {
		return "DEFINE";
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public List<OdysseusScriptBlock> determineExecutableBlocks(ImmutableList<OdysseusScriptBlock> blocks) {
		List<OdysseusScriptBlock> selectedBlocks = Lists.newArrayList();
		
		boolean beginFound = false;
		for( OdysseusScriptBlock block : blocks ) {
			if(block.getKeyword().equals("#DEFINE")) {
				if( !beginFound ) {
					beginFound = true;
				}
				
				selectedBlocks.add(block);
			} else {
				if( beginFound ) {
					return selectedBlocks;
				}
			}
		}
		
		return selectedBlocks;
	}

	@Override
	public IVisualOdysseusScriptBlock transform(List<OdysseusScriptBlock> blocksToTransform) throws VisualOdysseusScriptException {
		
		Map<String, String> keyValuePairs = Maps.newHashMap();
		for( OdysseusScriptBlock block : blocksToTransform ) {
			String line = block.getText();
			String[] parts = line.split(" |\t", 2);
			
			if( parts.length != 2 ) {
				throw new VisualOdysseusScriptException("DEFINE with line '" + line + "' is invalid");
			}
			
			String key = parts[0];
			String value = parts[1];
			
			keyValuePairs.put(key, value);
		}
			
		return new DefinesVisualOdysseusScriptBlock( keyValuePairs );
	}

}
