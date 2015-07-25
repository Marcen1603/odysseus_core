/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * benchmarker composite for initializing current query
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkStartComposite extends AbstractBenchmarkComposite {

	private ProgressBar progressInitializeQuery;

	public BenchmarkStartComposite(Composite parent, int style, int windowWidth) {
		super(parent, style);

		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		GridLayout gridLayout = new GridLayout();
		this.setLayout(gridLayout);

		createContent();

		parent.pack();
		parent.setVisible(true);
	}

	/**
	 * create content for this page (progress bar and a hint)
	 */
	private void createContent() {
		createLabelWithSeperator(this, "Initialize current query");

		progressInitializeQuery = new ProgressBar(this, SWT.SMOOTH);
		progressInitializeQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressInitializeQuery.setMinimum(0);
		progressInitializeQuery.setMaximum(100);

		createLabel(this,
				"Hint: Benchmarking is only possible if operators have an unique id.");
	}

	public void updateProgress(int selectionValue) {
		progressInitializeQuery.setSelection(selectionValue);
	}
}
