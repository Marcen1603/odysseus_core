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
package de.uniol.inf.is.odysseus.rcp.editor.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;

public class OperatorFigure extends Figure implements IFigure {

	private Label label; 
	
	public OperatorFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		layout.setSpacing(10);
		setLayoutManager(layout);
		
		label = new Label();
		label.setBorder(new LineBorder(2));
		add(label);
	}
	
	public void setText( String text ) {
		label.setText(text);
		layout();
	}

	public void markError() {
		label.setForegroundColor(ColorConstants.red);
	}
	
	public void unmarkError() {
		label.setForegroundColor(ColorConstants.black);
	}
}
