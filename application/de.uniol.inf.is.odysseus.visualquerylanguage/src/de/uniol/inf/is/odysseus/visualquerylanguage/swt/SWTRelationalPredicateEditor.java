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

import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;

public class SWTRelationalPredicateEditor {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory
			.getLogger(SWTRelationalPredicateEditor.class);

	private Shell shell;
	private SDFAttributeList schema = new SDFAttributeList();

	private String left = "";
	private String right = "";

	private ComplexPredicate<RelationalTuple<ITimeInterval>> leftPred = null;
	private ComplexPredicate<RelationalTuple<ITimeInterval>> rightPred = null;

	private Collection<ISWTParameterListener> listeners = new ArrayList<ISWTParameterListener>();

	public SWTRelationalPredicateEditor(Shell baseWindow, final INodeContent content,
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
			if (c instanceof Text) {
				textArea.setText(((Text) c).getText());
			}
		}

		Composite comp = new Composite(shell, SWT.BORDER);
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
					schema.add(sdfAttribute);
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
				DirectAttributeResolver resolver = new DirectAttributeResolver(
						schema);
				ArrayList<String> ors = new ArrayList<String>();
				String[] orValues = null;
				if (textArea.getText().contains(" or ")) {
					orValues = textArea.getText().split(" or ");
				}
				for (String string : orValues) {
					if (ors.contains("=")) {
						string = string.replace("=", "==");
					}
					ors.add(string);
				}
				ArrayList<String[]> andValues = new ArrayList<String[]>();
				ArrayList<String> orsToRemove = new ArrayList<String>();
				if (!ors.isEmpty()) {
					for (String string : ors) {
						if (string.contains(" and ")) {
							andValues.add(string.split(" and "));
							orsToRemove.add(string);
						}
					}
				}
				for (String string : orsToRemove) {
					ors.remove(string);
				}
				AndPredicate<RelationalTuple<ITimeInterval>> complexPredicate = null;
				ArrayList<ComplexPredicate<RelationalTuple<ITimeInterval>>> complexPreds = new ArrayList<ComplexPredicate<RelationalTuple<ITimeInterval>>>();
				for (String[] strings : andValues) {
					for (int i = 0; i < strings.length; i++) {
						if (left.isEmpty()) {
							left = strings[i].replace("=", "==");
						} else if (right.isEmpty()) {
							right = strings[i].replace("=", "==");
						}
						if (!left.isEmpty() && !right.isEmpty()) {
							complexPredicate = new AndPredicate<RelationalTuple<ITimeInterval>>(
									new RelationalPredicate(new SDFExpression(
											"", left, resolver)),
									new RelationalPredicate(new SDFExpression(
											"", right, resolver)));
							left = "";
							right = "";
						} else if (!left.isEmpty() && right.isEmpty()
								&& complexPredicate != null) {
							complexPredicate = new AndPredicate<RelationalTuple<ITimeInterval>>(
									new RelationalPredicate(new SDFExpression(
											"", left, resolver)),
									complexPredicate);
							left = "";
						}
					}
					if (complexPredicate != null) {
						complexPreds.add(complexPredicate);
						complexPredicate = null;
					}
				}
				OrPredicate<RelationalTuple<ITimeInterval>> orPred = null;
				for (String string : ors) {
					if (left.isEmpty()) {
						left = string.replace("=", "==");
					} else if (right.isEmpty()) {
						right = string.replace("=", "==");
					}
					if (!left.isEmpty() && !right.isEmpty()) {
						orPred = new OrPredicate<RelationalTuple<ITimeInterval>>(
								new RelationalPredicate(new SDFExpression("",
										left, resolver)),
								new RelationalPredicate(new SDFExpression("",
										right, resolver)));
						left = "";
						right = "";
					} else if (!left.isEmpty() && right.isEmpty()
							&& orPred != null) {
						orPred = new OrPredicate<RelationalTuple<ITimeInterval>>(
								new RelationalPredicate(new SDFExpression("",
										left, resolver)), orPred);
						left = "";
					}
				}
				for (ComplexPredicate<RelationalTuple<ITimeInterval>> pred : complexPreds) {
					if (leftPred == null) {
						leftPred = pred;
					} else if (rightPred == null) {
						rightPred = pred;
					}
					
					if(!left.isEmpty() && leftPred != null) {
						orPred = new OrPredicate<RelationalTuple<ITimeInterval>>(
								new RelationalPredicate(new SDFExpression("",
										left, resolver)), leftPred);
						left = "";
						leftPred = null;
					}else if (leftPred != null && rightPred != null) {
						orPred = new OrPredicate<RelationalTuple<ITimeInterval>>(
								leftPred, rightPred);
						leftPred = null;
						rightPred = null;
					} else if (leftPred != null && rightPred == null
							&& orPred != null) {
						orPred = new OrPredicate<RelationalTuple<ITimeInterval>>(
								leftPred, orPred);
						leftPred = null;
					}
				}
				if (orPred != null) {
					for (ISWTParameterListener listener : listeners) {
						listener.setValue(orPred);
					}
				} else {
					RelationalPredicate pred = new RelationalPredicate(
							new SDFExpression("", textArea.getText().replace("=", "=="), resolver));
					for (ISWTParameterListener listener : listeners) {
						listener.setValue(pred);
					}
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
