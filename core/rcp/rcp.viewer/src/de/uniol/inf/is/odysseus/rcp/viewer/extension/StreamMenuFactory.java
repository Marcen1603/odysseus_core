/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.extension;

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

import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;

public class StreamMenuFactory extends ExtensionContributionFactory {

	private final List<StreamExtensionDefinition> definitions;

	public StreamMenuFactory() {
		StreamEditorRegistry registry = StreamEditorRegistry.getInstance();
		definitions = registry.getStreamExtensionDefinitions();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
		MenuManager manager = new MenuManager("Stream");
		
		for( StreamExtensionDefinition def : definitions ) {
			CommandContributionItemParameter p = new CommandContributionItemParameter(serviceLocator, "", OdysseusRCPViewerPlugIn.SHOW_STREAM_COMMAND_ID, SWT.PUSH);
			p.label = def.getLabel();
			p.parameters = new HashMap<Object, Object>();
			p.parameters.put(OdysseusRCPViewerPlugIn.STREAM_EDITOR_TYPE_PARAMETER_ID, def.getID());
			
			IContributionItem item = new CommandContributionItem(p);
			item.setVisible(true);
			manager.add(item);
		}
		
		additions.addContributionItem(manager, null);
	}

}
