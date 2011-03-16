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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.BenchmarkStoreUtil;

/**
 * Diese Klasse zeichnet die Views für die Ergebnisse
 * 
 * @author Stefanie Witzke
 * 
 */
public class ResultEditorPart extends EditorPart {
	public static final String ID = "de.uniol.inf.is.odysseus.rcp.benchmarker.gui.editorResult";

	private int resultID;
	private String resultXml;
	private Benchmark benchmark;

	public ResultEditorPart() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public String getTitleToolTip() {
		return getPartName();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		this.resultID = ((ResultEditorInput) input).getResultID();
		this.resultXml = ((ResultEditorInput) input).getResultName();
		this.benchmark = ((ResultEditorInput) input).getBenchmark();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout(3, false);
		parent.setLayout(gridLayout);

		Label labelPage = new Label(parent, SWT.NULL);
		labelPage.setText(resultXml);
		new Label(parent, SWT.NULL);
		Button deleteButton = new Button(parent, SWT.PUSH);
		deleteButton.setText("Delete this result");

		// löscht das Ergebnis
		deleteButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				BenchmarkStoreUtil.deleteResult(benchmark, ((ResultEditorInput) getEditorInput()).getResult());
			}
		});
	}

	@Override
	public void setFocus() {
	}

	@Override
	protected void setPartName(String partName) {
	}

	@Override
	public String getPartName() {
		return "Result " + resultID;
	}
}
