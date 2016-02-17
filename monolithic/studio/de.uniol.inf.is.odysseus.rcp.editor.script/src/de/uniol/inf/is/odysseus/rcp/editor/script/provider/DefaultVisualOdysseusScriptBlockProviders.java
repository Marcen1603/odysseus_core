package de.uniol.inf.is.odysseus.rcp.editor.script.provider;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProvider;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProviders;

public class DefaultVisualOdysseusScriptBlockProviders implements IVisualOdysseusScriptBlockProviders {

	@Override
	public Collection<IVisualOdysseusScriptBlockProvider> getProviders() {
		Collection<IVisualOdysseusScriptBlockProvider> provs = Lists.newArrayList();
		
		provs.add(new MetadataBlockProvider());
		
		return provs;
	}

}
