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
	public List<OdysseusScriptBlock> determineTransformableBlocks(ImmutableList<OdysseusScriptBlock> blocks) {
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
	public IVisualOdysseusScriptBlock transform(List<OdysseusScriptBlock> allBlocks, List<OdysseusScriptBlock> selectedBlocks) throws VisualOdysseusScriptException {
		
		Map<String, String> keyValuePairs = Maps.newHashMap();
		for( OdysseusScriptBlock block : selectedBlocks ) {
			String line = block.getText();
			String[] parts = line.split(" |\t", 2);
			
			String key = null;
			String value = null;
			if( parts.length == 2 ) {
				key = parts[0];
				value = parts[1];
				
				keyValuePairs.put(key, value);
			} else if( parts.length == 1 ) {
				keyValuePairs.put(parts[0], "");
			} else {
				throw new VisualOdysseusScriptException("DEFINE with line '" + line + "' is invalid");
			}
		}
		
		for( OdysseusScriptBlock selectedBlock : selectedBlocks ) {
			allBlocks.remove(selectedBlock);
		}
			
		return new DefinesVisualOdysseusScriptBlock( keyValuePairs );
	}

}
