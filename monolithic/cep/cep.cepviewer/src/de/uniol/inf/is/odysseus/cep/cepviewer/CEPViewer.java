/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class defines the cep perspective.
 * 
 * @author Christian
 */
public class CEPViewer implements IPerspectiveFactory {

	/**
	 * This methods creates the layout of the perspective and adds the views.
	 * 
	 * @param myLayout
	 *            is the layout of the perspective
	 */
	@Override
    public void createInitialLayout(IPageLayout myLayout) {
		// deactived the editor area
		myLayout.setEditorAreaVisible(false);
	}
}
