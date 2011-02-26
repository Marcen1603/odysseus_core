package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.StringUtils;

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

		dropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER);
		for (BenchmarkGroup benchmarkGroup : directoryList) {
			dropDown.add(benchmarkGroup.getName());
			System.out.println(benchmarkGroup);
		}

		// TODO sonderzeichen verbieten!!! Weil Ordner ist ;)
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
				// TODO Auto-generated method stub

			}
		});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		dropDown.setLayoutData(gridData);
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	public String getDropDown() {
		return dropDown.getText();
	}

	private List<BenchmarkGroup> getDirectories() {
		return BenchmarkHolder.INSTANCE.getBenchmarkGroups();
	}
}