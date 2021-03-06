package de.uniol.inf.is.odysseus.rcp.editor.script.provider;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProvider;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.QueryVisualOdysseusScriptBlock;

public class CQLQueryBlockProvider implements IVisualOdysseusScriptBlockProvider {

	@Override
	public String getName() {
		return "CQL-Query";
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public IVisualOdysseusScriptBlock create() {
		QueryVisualOdysseusScriptBlock block = new QueryVisualOdysseusScriptBlock("/// CQL-Query", "CQL", "", false);
		return block;
	}

}
