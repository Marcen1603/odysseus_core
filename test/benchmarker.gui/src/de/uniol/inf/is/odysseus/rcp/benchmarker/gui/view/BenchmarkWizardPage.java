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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.StringUtils;

/**
 * Diese Klasse zeichnet die Seite für den Wizard, in dem eine Gruppe ausgewählt
 * werden kann
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkWizardPage extends WizardPage {
	private Composite container;
	private Combo dropDown;
	private List<BenchmarkGroup> directoryList;

	public BenchmarkWizardPage() {
		super("Benchmark");
		setTitle("New Benchmark");
		setDescription("Select the group for the new Benchmarksettings or create a new group.");
		directoryList = getDirectories();
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		Label label1 = new Label(container, SWT.NULL);
		label1.setText("Group: ");

		// Vorhandene Gruppe auswählen
		dropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER);
		for (BenchmarkGroup benchmarkGroup : directoryList) {
			dropDown.add(benchmarkGroup.getName());
			System.out.println(benchmarkGroup);
		}

		dropDown.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!dropDown.getText().isEmpty()) {
					setPageComplete(true);
				}
			}
		});

		dropDown.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (StringUtils.isNotBlank(dropDown.getText())) {
					setPageComplete(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		dropDown.setLayoutData(gridData);

		// Wird benötigt, um einen Fehler im System zu vermeiden
		setControl(container);
		setPageComplete(false);
	}

	public String getDropDown() {
		return dropDown.getText();
	}

	private static List<BenchmarkGroup> getDirectories() {
		return BenchmarkHolder.INSTANCE.getBenchmarkGroups();
	}
}