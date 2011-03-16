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

import static de.uniol.inf.is.odysseus.rcp.benchmarker.utils.StringUtils.strContains;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;

/**
 * Diese Klasse filtert die Parameter nach dem eingegebenen Suchwort
 * 
 * @author Stefanie Witzke
 * 
 */
public class FilterTextTreeViewer extends ControlContribution implements ModifyListener {

	private String filterString;
	private TreeViewer viewer;

	protected FilterTextTreeViewer(String id, TreeViewer viewer) {
		super(id);
		this.viewer = viewer;
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		Label label = new Label(composite, SWT.NONE);
		label.setText("Search:");
		Text text = new Text(composite, SWT.BORDER);
		text.addModifyListener(this);
		return composite;
	}

	@Override
	public void modifyText(ModifyEvent event) {
		if (event.widget instanceof Text) {
			Text text = (Text) event.widget;
			filterString = text.getText(); // filterString = searchText
			viewer.setFilters(new ViewerFilter[] { viewerFilter });
		}
	}

	ViewerFilter viewerFilter = new ViewerFilter() {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof Benchmark) {
				return showBenchmarkNote((Benchmark) element);
			}
			return true;
		}
	};

	private boolean showBenchmarkNote(Benchmark benchmark) {
		// wonach gesucht/gefiltert werden kann:
		return strContains(benchmark.getId(), filterString)
				|| strContains(benchmark.getParam().getName(), filterString)
				|| strContains(benchmark.getParam().getScheduler(), filterString)
				|| strContains(benchmark.getParam().getSchedulingstrategy(), filterString)
				|| strContains(benchmark.getParam().getBufferplacement(), filterString)
				|| strContains(benchmark.getParam().getDataType(), filterString)
				|| strContains(benchmark.getParam().getQueryLanguage(), filterString)
				|| strContains(benchmark.getParam().getQuery(), filterString)
				|| strContains(benchmark.getParam().getMaxResult(), filterString)
				|| strContains(benchmark.getParam().getWaitConfig(), filterString)
				|| strContains(benchmark.getParam().getInputFile(), filterString);
	}
}