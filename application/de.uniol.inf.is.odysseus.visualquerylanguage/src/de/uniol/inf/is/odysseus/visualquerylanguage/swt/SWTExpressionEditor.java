package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class SWTExpressionEditor implements ISWTParameterListener, SelectionListener{
	
	private Shell shell;
	
	private List<SDFExpression> expressions = new ArrayList<SDFExpression>();
	
	private Collection<ILogicalOperator> opList = new ArrayList<ILogicalOperator>();
	
	private StyledText textArea;
	
	public SWTExpressionEditor(final MapAO op, final Collection<ILogicalOperator> operators) {
		opList = operators;
		shell = new Shell(SWTMainWindow.getShell(), SWT.RESIZE | SWT.CLOSE
				| SWT.APPLICATION_MODAL);
		shell.setText("Expressioneditor");
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		shell.setLayout(gl);
		
		textArea = new StyledText(shell, SWT.BORDER
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		textArea.setLayoutData(gd);
		
		Composite comp = new Composite(shell,
				SWT.BORDER);
		GridLayout compLayout = new GridLayout();
		compLayout.numColumns = 4;
		comp.setLayout(compLayout);
		GridData compData = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(compData);
		
		Button addExpressionButton = new Button(comp, SWT.PUSH);
		addExpressionButton.setText("Neue Berechnung hinzufügen");
		addExpressionButton.addSelectionListener(this);
		
		Button clearExpressionList = new Button(comp, SWT.PUSH);
		clearExpressionList.setText("Lösche Expressions");
		clearExpressionList.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				expressions = new ArrayList<SDFExpression>();
				textArea.setText("");
			}
			
		});
		
		Button addListButton = new Button(comp, SWT.PUSH);
		addListButton.setText("Berechnungen speichern");
		addListButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				op.setExpressions(expressions);
				shell.close();
				shell.dispose();
			}
			
		});
		
		
		
		Button cancelButton = new Button(comp, SWT.PUSH);
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

	@Override
	public void setValue(Object value) {
		if(value instanceof SDFExpression) {
			this.expressions.add((SDFExpression)value);
			textArea.setText(expressions.toString());
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// Diese Methode wird nicht benötigt
		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		SWTCalcEditor editor = new SWTCalcEditor(SWTMainWindow.getShell(), opList);
		editor.addSWTParameterListener(this);
	}
}
