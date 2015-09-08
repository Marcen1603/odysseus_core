package de.uniol.inf.is.odysseus.iql.basic.ui.labeling;

import com.google.inject.Inject;

public class BasicIQLLabelProvider extends AbstractIQLLabelProvider {

	@Inject
	public BasicIQLLabelProvider(org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}
}
