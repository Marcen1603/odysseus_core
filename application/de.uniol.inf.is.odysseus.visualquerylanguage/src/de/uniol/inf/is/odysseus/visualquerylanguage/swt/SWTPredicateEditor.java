package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;

public class SWTPredicateEditor {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(SWTPredicateEditor.class);

	private Shell shell;

	private Collection<ISWTParameterListener> listeners = new ArrayList<ISWTParameterListener>();

	public SWTPredicateEditor(Shell baseWindow, final INodeContent content,
			Collection<SDFAttributeList> inputSchemas, Composite actualComp) {

		shell = new Shell(baseWindow, SWT.RESIZE | SWT.CLOSE
				| SWT.APPLICATION_MODAL);
		shell.setText("Parametereditor");
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		shell.setLayout(gl);

		final StyledText textArea = new StyledText(shell, SWT.BORDER
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		textArea.setLayoutData(gd);
		for (Control c : actualComp.getChildren()) {
			if(c instanceof Text) {
				textArea.setText(((Text)c).getText());
			}
		}

		Composite comp = new org.eclipse.swt.widgets.Composite(shell,
				SWT.BORDER);
		GridLayout compLayout = new GridLayout();
		compLayout.numColumns = 5;
		compLayout.makeColumnsEqualWidth = true;
		comp.setLayout(compLayout);
		GridData compData = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(compData);

		Button button;
		
		for (SDFAttributeList sdfAttributeList : inputSchemas) {
			if (sdfAttributeList != null) {
				for (SDFAttribute sdfAttribute : sdfAttributeList) {
					if (sdfAttribute != null) {
						button = new Button(comp, SWT.PUSH);
						button.setLayoutData(new GridData(
								GridData.FILL_HORIZONTAL));
						button.setText(sdfAttribute.toString());
						button.setData(sdfAttribute);
						button.addSelectionListener(new SelectionAdapter() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								if (e.getSource() instanceof Button) {
									textArea.setText(textArea.getText()
											+ ((Button) e.getSource())
													.getText());
								}
							}

						});
					}
				}
			}
		}

		Collection<String> additionalFuncs = new ArrayList<String>(Arrays
				.asList("and", "or", "avg", "min", "max", "count", "+", "-",
						"/", "*", "=", "<>", "<", ">", "<=", ">=", "(", ")"));

		for (String func : additionalFuncs) {
			button = new Button(comp, SWT.PUSH);
			button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			button.setText(func);
			button.setData(func);
			button.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (e.getSource() instanceof Button) {
						textArea.setText(textArea.getText()
								+ ((Button) e.getSource()).getText());
					}
				}

			});
		}

		for (String func : SDFExpression.getFunctions()) {
			button = new Button(comp, SWT.PUSH);
			button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			button.setText(func);
			button.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (e.getSource() instanceof Button) {
						textArea.setText(textArea.getText()
								+ ((Button) e.getSource()).getText());
					}
				}
			});
		}

		Button applyButton = new Button(shell, SWT.PUSH);
		applyButton.setText("Parameter hinzufügen");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (ISWTParameterListener listener : listeners) {
					listener.setValue(textArea.getText());
				}
				shell.dispose();
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

	public void addSWTParameterListener(ISWTParameterListener listener) {
		this.listeners.add(listener);
	}

}
