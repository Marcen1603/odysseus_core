package de.uniol.inf.is.odysseus.rcp.editor.script.provider;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProvider;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.DefinesVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.DefinitionsList;

public class DefinitionsBlockProvider implements IVisualOdysseusScriptBlockProvider {

	@Override
	public String getName() {
		return "Definitions";
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public IVisualOdysseusScriptBlock create() {
		DefinitionsList list = new DefinitionsList();
		
		list.addNewDefinition();
		DefinesVisualOdysseusScriptBlock block = new DefinesVisualOdysseusScriptBlock(list);
		
		return block;
	}

}
