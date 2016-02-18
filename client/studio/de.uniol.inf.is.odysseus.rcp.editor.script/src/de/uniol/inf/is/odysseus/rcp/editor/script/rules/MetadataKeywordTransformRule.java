package de.uniol.inf.is.odysseus.rcp.editor.script.rules;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRule;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.OdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.MetadataVisualOdysseusScriptBlock;

public class MetadataKeywordTransformRule implements IOdysseusScriptTransformRule {

	@Override
	public String getName() {
		return "Metadata";
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public List<OdysseusScriptBlock> determineTransformableBlocks(ImmutableList<OdysseusScriptBlock> blocks) {
		List<OdysseusScriptBlock> selectedBlocks = Lists.newArrayList();

		boolean beginFound = false;
		for (OdysseusScriptBlock block : blocks) {
			if (block.getKeyword().equals("#METADATA")) {
				if (!beginFound) {
					beginFound = true;
				}

				selectedBlocks.add(block);
			} else {
				if (beginFound) {
					return selectedBlocks;
				}
			}
		}

		return selectedBlocks;
	}

	@Override
	public IVisualOdysseusScriptBlock transform(List<OdysseusScriptBlock> allBlocks, List<OdysseusScriptBlock> selectedBlocks) throws VisualOdysseusScriptException {
		List<String> metadataNames = Lists.newArrayList();

		for (OdysseusScriptBlock selectedBlock : selectedBlocks) {
			allBlocks.remove(selectedBlock);
			metadataNames.add(selectedBlock.getText().trim());
		}

		return new MetadataVisualOdysseusScriptBlock(metadataNames);
	}

}
