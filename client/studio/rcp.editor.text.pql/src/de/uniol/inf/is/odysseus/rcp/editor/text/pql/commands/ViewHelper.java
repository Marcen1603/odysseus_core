/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.editor.text.pql.commands;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLOperatorView;


class ViewHelper {
	
	private ViewHelper() {
	}
	
	public static PQLOperatorView getPQLOperatorView() {
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(PQLEditorTextPlugIn.PQL_OPERATOR_VIEW_ID);
		return (PQLOperatorView) part;
	}
	
}
