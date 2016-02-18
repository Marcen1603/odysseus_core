package de.uniol.inf.is.odysseus.rcp.editor.script.rules;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRule;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.OdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.QueryVisualOdysseusScriptBlock;

public class QueryTransformRule implements IOdysseusScriptTransformRule {

	private OdysseusScriptBlock lastSeenParser;
	private OdysseusScriptBlock lastSeenQName;
	private boolean runningQuery;
	
	@Override
	public String getName() {
		return "QUERY";
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public List<OdysseusScriptBlock> determineTransformableBlocks(ImmutableList<OdysseusScriptBlock> blocks) {
		lastSeenParser = null;
		lastSeenQName = null;
		runningQuery = false;
		
		for (OdysseusScriptBlock block : blocks) {
			String keyword = block.getKeyword();
			
			if( keyword.equals("#PARSER")) {
				lastSeenParser = block;
			} else if( keyword.equals("#QNAME")) {
				lastSeenQName = block;
			} else if (keyword.equals("#ADDQUERY")) {
				runningQuery = false;
				return Lists.newArrayList(block);
			} else if (keyword.equals("#RUNQUERY")) {
				runningQuery = true;
				return Lists.newArrayList(block);
			} 
		}

		return Lists.newArrayList();
	}

	@Override
	public IVisualOdysseusScriptBlock transform(List<OdysseusScriptBlock> allBlocks, List<OdysseusScriptBlock> blocksToTransform) throws VisualOdysseusScriptException {
		OdysseusScriptBlock block = blocksToTransform.get(0);
		
		String parser = lastSeenParser != null ? lastSeenParser.getText() : "PQL";
		String qname = lastSeenQName != null ? lastSeenQName.getText() : null;
		QueryVisualOdysseusScriptBlock visualBlock = new QueryVisualOdysseusScriptBlock(block.getText(), parser, qname, runningQuery);
		allBlocks.remove(block);
			
		// determine if we can remove #PARSER and #QNAME odysseus script blocks
		// this happens when there are no #ADDQUERY and #RUNQUERY left
		if( !hasQueryScriptBlock(allBlocks)) {
			
			for( OdysseusScriptBlock curBlock : allBlocks.toArray(new OdysseusScriptBlock[0]) ) {
				if( curBlock.getKeyword().equals("#PARSER")) {
					allBlocks.remove(curBlock);
				} else if( curBlock.getKeyword().equals("#QNAME")) {
					allBlocks.remove(curBlock);
				}  
			}
		}
		
		return visualBlock;
	}

	private static boolean hasQueryScriptBlock(List<OdysseusScriptBlock> allBlocks) {
		for (OdysseusScriptBlock block : allBlocks) {
			String keyword = block.getKeyword();
			
			if (keyword.equals("#ADDQUERY")) {
				return true;
			} else if (keyword.equals("#RUNQUERY")) {
				return true;
			} 
		}
		
		return false;
	}

}
