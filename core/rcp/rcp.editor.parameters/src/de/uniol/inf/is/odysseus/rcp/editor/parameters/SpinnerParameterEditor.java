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
package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;

public class SpinnerParameterEditor extends SimpleParameterEditor<Long> {

	@Override
	public Long convertFromString(String txt) {
		return Long.valueOf(txt);
	}

	@Override
	public String convertToString(Long object) {
		if( object == null ) 
			return "";
		return object.toString();
	}
	
	@Override
	protected Control createInputControl(Composite parent) {
		final Spinner spinner = new Spinner(parent, SWT.BORDER);

		spinner.setSelection(0);
		spinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					setValue(spinner.getSelection());
				} catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
			
		});
		
		return spinner;
	}

}
