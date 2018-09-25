package de.uniol.inf.is.odysseus.rcp.editor.script.provider;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProvider;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.DefaultOdysseusScriptBlock;

public class GenericBlockProvider implements IVisualOdysseusScriptBlockProvider {

	@Override
	public String getName() {
		return "Generic Text";
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public IVisualOdysseusScriptBlock create() {
		return new DefaultOdysseusScriptBlock();
	}

}
