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
package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * This class defines the view which shows the MatchingTrace of the current
 * selected Instance.
 * 
 * @author Christian
 */
public class CEPTraceView extends ViewPart {

	// the ID of this view
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.matchingtraceview";
	// the widget which holds the informations
	private Text text;

	/**
	 * This is the constructor.
	 */
	public CEPTraceView() {
		super();
	}

	/**
	 * This method clears the view.
	 */
	public void clearView() {
		this.text.setText("");
	}

	/**
	 * This method creates the CEPTraceView by creating the Text widget.
	 * 
	 * @param parent
	 *            is the widget which contains the query view.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		this.text = new Text(parent, SWT.MULTI | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		this.text.setEditable(false);
		this.text.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
	}

	/**
	 * This method displays the information of the given instance in the view.
	 * 
	 * @param instance
	 *            is a CEPInstanz holding the StateMachine
	 */
	public void setContent(CEPInstance instance) {
		String output = "";
		for (State state : instance.getStateMachine().getStates()) {
			output += state.getId() + StringConst.TITLE_BREAK;
			for (int i = 0; i < instance.getInstance().getMatchingTrace()
					.getStateBufferSize(state); i++) {
				output = output.concat(
						instance.getInstance().getMatchingTrace()
								.getEvent(state, i).toString()).concat(
						StringConst.BREAK);
			}
		}
		this.text.setText(output);
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
		// do nothing
	}

}
