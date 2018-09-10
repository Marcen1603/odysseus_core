package de.uniol.inf.is.odysseus.rcp.editor.script.provider;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProvider;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.QueryVisualOdysseusScriptBlock;

public class PQLQueryBlockProvider implements IVisualOdysseusScriptBlockProvider {

	@Override
	public String getName() {
		return "PQL-Query";
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public IVisualOdysseusScriptBlock create() {
		QueryVisualOdysseusScriptBlock block = new QueryVisualOdysseusScriptBlock("///PQL-Query", "PQL", "", false);
		return block;
	}

}
