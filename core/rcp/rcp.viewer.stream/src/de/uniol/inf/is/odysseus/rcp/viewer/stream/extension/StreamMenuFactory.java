package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.IStreamConstants;

public class StreamMenuFactory extends ExtensionContributionFactory {

	private final List<StreamExtensionDefinition> definitions;

	public StreamMenuFactory() {
		StreamEditorRegistry registry = StreamEditorRegistry.getInstance();
		definitions = registry.getStreamExtensionDefinitions();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
		MenuManager manager = new MenuManager("Show Stream");
		
		for( StreamExtensionDefinition def : definitions ) {
			CommandContributionItemParameter p = new CommandContributionItemParameter(serviceLocator, "", IStreamConstants.SHOW_STREAM_COMMAND, SWT.PUSH);
			p.label = def.getLabel();
			p.parameters = new HashMap<Object, Object>();
			p.parameters.put(IStreamConstants.STREAM_EDITOR_TYPE_PARAMETER_ID, def.getID());
			
			IContributionItem item = new CommandContributionItem(p);
			item.setVisible(true);
			manager.add(item);
		}
		
		additions.addContributionItem(manager, null);
	}

}
