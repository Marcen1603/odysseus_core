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
package de.uniol.inf.is.odysseus.rcp.editor.parameters.activator;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec( new Runnable() {
			@Override
			public void run() {
				Activator.getDefault().getImageRegistry().put("addIcon", Activator.getImageDescriptor("icons/add.gif"));
				Activator.getDefault().getImageRegistry().put("removeIcon", Activator.getImageDescriptor("icons/remove.gif"));
				Activator.getDefault().getImageRegistry().put("upIcon", Activator.getImageDescriptor("icons/arrow-up.jpg"));
				Activator.getDefault().getImageRegistry().put("downIcon", Activator.getImageDescriptor("icons/arrow-down.jpg"));
			}
		});
	}

}
