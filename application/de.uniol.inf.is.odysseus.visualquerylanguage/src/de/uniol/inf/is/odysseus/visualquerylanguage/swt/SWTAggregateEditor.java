package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AggregateAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class SWTAggregateEditor {

	private Shell shell;

	private Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations = new HashMap<SDFAttribute, Map<AggregateFunction, SDFAttribute>>();

	private SDFAttributeList inputList = new SDFAttributeList();

	public SWTAggregateEditor(Shell baseWindow,
			Collection<ILogicalOperator> opList, final AggregateAO op) {
		
		Collection<SDFAttributeList> inputs = new ArrayList<SDFAttributeList>();
		for (ILogicalOperator operator : opList) {
				inputs.add(operator.getOutputSchema());
		}
		
		shell = new Shell(baseWindow, SWT.RESIZE | SWT.CLOSE
				| SWT.APPLICATION_MODAL);
		shell.setText("Parametereditor");
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		shell.setLayout(gl);

		Composite comp = new org.eclipse.swt.widgets.Composite(shell,
				SWT.BORDER);
		GridLayout compLayout = new GridLayout();
		compLayout.numColumns = 5;
		compLayout.makeColumnsEqualWidth = true;
		comp.setLayout(compLayout);
		GridData compData = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(compData);

		for (SDFAttributeList list : inputs) {
			for (SDFAttribute att : list) {
				inputList.add(att);
			}
		}

		for (final SDFAttribute sdfAttribute : inputList) {
			final Map<AggregateFunction, SDFAttribute> outputs = new HashMap<AggregateFunction, SDFAttribute>();
			final Text inputText = new Text(comp, SWT.SINGLE);
			inputText.setText(sdfAttribute.toPointString());
			inputText.setData(sdfAttribute);
			inputText.setEnabled(false);
			Color c = new Color(Display.getCurrent(), 255, 255, 255);
			inputText.setBackground(c);
			final Button functionButton = new Button(comp, SWT.PUSH);
			functionButton.setText("+");
			final Button removeFunctionButton = new Button(comp, SWT.PUSH);
			removeFunctionButton.setText("-");
			final Combo functionCombo = new Combo(comp, SWT.DROP_DOWN
					| SWT.READ_ONLY);
			for (AggregateFunction function : AggregateFunction.values()) {
				functionCombo.add(function.toString());
			}
			if (functionCombo.getItemCount() > 0) {
				functionCombo.select(0);
			}
			final Text functionText = new Text(comp, SWT.SINGLE);
			functionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			functionText.setEditable(false);
			functionText.setBackground(c);
			functionButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					AggregateFunction func = null;
					for (AggregateFunction function : AggregateFunction
							.values()) {
						if (function.toString().equals(
								functionCombo.getItem(functionCombo
										.getSelectionIndex()))) {
							func = function;
						}
					}
					if (functionText.getText().isEmpty()) {
						functionText.setText(functionCombo
								.getItem(functionCombo.getSelectionIndex()));
					} else {
						functionText.setText(functionText.getText()
								+ ", "
								+ functionCombo.getItem(functionCombo
										.getSelectionIndex()));
					}
					SDFAttribute att = new SDFAttribute(functionCombo
							.getItem(functionCombo.getSelectionIndex())
							+ "(" + inputText.getText() + ")");
					functionText.setData(att);
					if (func != null) {
						outputs.put(func, att);
					}
				}

			});
			removeFunctionButton.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					AggregateFunction func = null;
					for (AggregateFunction function : AggregateFunction
							.values()) {
						if (function.toString().equals(
								functionCombo.getItem(functionCombo
										.getSelectionIndex()))) {
							func = function;
						}
					}
					if(func != null) {
						if(functionText.getText().contains(", " + func.toString())) {
							functionText.setText(functionText.getText().replace(", " + func.toString(), ""));
						}
						if(functionText.getText().contains(func.toString())) {
							functionText.setText(functionText.getText().replace(func.toString(), ""));
						}
						outputs.remove(func);
					}
				}
			});
				aggregations.put(sdfAttribute, outputs);
		}

		Button applyButton = new Button(shell, SWT.PUSH);
		applyButton.setText("Aggregationen setzen");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Collection<SDFAttribute> mapList = new ArrayList<SDFAttribute>();
				for ( SDFAttribute key: aggregations.keySet()) {
					if(aggregations.get(key).isEmpty()) {
						mapList.add(key);
					}
				}
				for (SDFAttribute att : mapList) {
					aggregations.remove(att);
				}
				op.addAggregations(aggregations);
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

}
