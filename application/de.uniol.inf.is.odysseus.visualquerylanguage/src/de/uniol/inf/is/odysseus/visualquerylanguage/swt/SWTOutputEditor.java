package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;

public class SWTOutputEditor {

	private Shell shell;

	private SDFAttributeList outputList = new SDFAttributeList();
	private SDFAttributeList inputList = new SDFAttributeList();
	
	private Collection<Text> textList = new ArrayList<Text>();

	private Collection<ISWTParameterListener> listeners = new ArrayList<ISWTParameterListener>();

	public SWTOutputEditor(Shell baseWindow,
			Collection<ILogicalOperator> opList, final INodeContent content) {
		shell = new Shell(baseWindow, SWT.RESIZE | SWT.CLOSE
				| SWT.APPLICATION_MODAL);
		shell.setText("Parametereditor");
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		shell.setLayout(gl);

		Composite comp = new Composite(shell,
				SWT.BORDER);
		GridLayout compLayout = new GridLayout();
		compLayout.numColumns = 4;
		compLayout.makeColumnsEqualWidth = true;
		comp.setLayout(compLayout);
		GridData compData = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(compData);

		for (ILogicalOperator iLogicalOperator : opList) {
			for (SDFAttribute att : iLogicalOperator.getOutputSchema()) {
				inputList.add(att);
			}
		}

		for (SDFAttribute sdfAttribute : inputList) {
			final Button button = new Button(comp, SWT.PUSH);
			button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			button.setText(sdfAttribute.toString());
			button.setData(sdfAttribute);
			final Text text = new Text(comp, SWT.SINGLE);
			textList.add(text);
			text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			if (EditorChecker.getInstance().hasProjectEditor(
					content.getEditor())) {
				button.setText(sdfAttribute.toString());
				button.setData(sdfAttribute);
				text.setEditable(false);
				Color c = new Color(Display.getCurrent(), 255, 255, 255);
				text.setBackground(c);
				button.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						if (text.getText().isEmpty()) {
							text.setText(button.getText());
							outputList.add((SDFAttribute) button.getData());
						} else {
							text.setText("");
							outputList
									.remove(((SDFAttribute) button.getData()));
						}
					}

				});
			}else if (EditorChecker.getInstance().hasRenameEditor(content.getEditor())) {
				button.setEnabled(false);
				text.setEditable(true);
			}
		}

		Button applyButton = new Button(shell, SWT.PUSH);
		applyButton.setText("Neues Outputschema setzen");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (EditorChecker.getInstance().hasProjectEditor(
						content.getEditor())
						&& !outputList.isEmpty()) {
					for (ISWTParameterListener listener : listeners) {
						listener.setValue(outputList);
					}
					shell.dispose();
				} else if (EditorChecker.getInstance().hasRenameEditor(
						content.getEditor())) {
					for (Text text : textList) {
						if(!text.getText().isEmpty()) {
							outputList.add(new SDFAttribute(text.getText()));
						}
					}
					if (outputList.size() == inputList.size()) {
						for (ISWTParameterListener listener : listeners) {
							listener.setValue(outputList);
						}
						shell.dispose();
					}else {
						outputList.clear();
					}
				}
			}
		});

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Abbrechen");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
			}
		});

		cancelButton.setBounds(SWT.DEFAULT, SWT.DEFAULT, 50, 25);

		shell.pack();
		shell.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2
				- shell.getSize().x / 2, Toolkit.getDefaultToolkit()
				.getScreenSize().height
				/ 2 - shell.getSize().y / 2);
		shell.open();

	}
	
	public void addParameterListener(ISWTParameterListener listener) {
		this.listeners.add(listener);
	}

}
