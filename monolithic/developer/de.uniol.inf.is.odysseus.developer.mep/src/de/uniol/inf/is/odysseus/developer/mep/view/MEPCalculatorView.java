/*******************************************************************************
 * Copyright 2016 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.developer.mep.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.optimizer.BooleanExpressionOptimizer;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$ *
 *
 */
public class MEPCalculatorView extends ViewPart {
    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MEPCalculatorView.class);
    Text txtExpression;
    Text txtReturnType;
    Text txtOptimizedForm;
    Text txtConjunctiveNormalForm;
    Text txtDisjunctiveNormalForm;

    private MEP mep;

    /**
     * 
     */
    public MEPCalculatorView() {
        super();
        this.mep = MEP.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(Composite parent) {
        final Composite container = new Composite(parent, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        final GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        final Label lbtExpression = new Label(container, SWT.NONE);
        lbtExpression.setText("Expression:"); //$NON-NLS-1$

        final GridData dataExpression = new GridData();
        dataExpression.grabExcessHorizontalSpace = true;
        dataExpression.horizontalAlignment = GridData.FILL;

        this.txtExpression = new Text(container, SWT.BORDER);
        this.txtExpression.setLayoutData(dataExpression);
        this.txtExpression.setText(""); //$NON-NLS-1$

        final Label lbtReturnType = new Label(container, SWT.NONE);
        lbtReturnType.setText("Return Type:"); //$NON-NLS-1$

        final GridData dataReturnType = new GridData();
        dataReturnType.grabExcessHorizontalSpace = true;
        dataReturnType.horizontalAlignment = GridData.FILL;

        this.txtReturnType = new Text(container, SWT.BORDER);
        this.txtReturnType.setLayoutData(dataReturnType);
        this.txtReturnType.setEditable(false);
        this.txtReturnType.setText(""); //$NON-NLS-1$

        final Label lbtOptimizedForm = new Label(container, SWT.NONE);
        lbtOptimizedForm.setText("Optimized:"); //$NON-NLS-1$

        final GridData dataOptimizedForm = new GridData();
        dataOptimizedForm.grabExcessHorizontalSpace = true;
        dataOptimizedForm.horizontalAlignment = GridData.FILL;

        this.txtOptimizedForm = new Text(container, SWT.BORDER);
        this.txtOptimizedForm.setLayoutData(dataOptimizedForm);
        this.txtOptimizedForm.setEditable(false);
        this.txtOptimizedForm.setText(""); //$NON-NLS-1$

        final Label lbtConjunctiveNormalForm = new Label(container, SWT.NONE);
        lbtConjunctiveNormalForm.setText("CNF:"); //$NON-NLS-1$

        final GridData dataConjunctiveNormalForm = new GridData();
        dataConjunctiveNormalForm.grabExcessHorizontalSpace = true;
        dataConjunctiveNormalForm.horizontalAlignment = GridData.FILL;

        this.txtConjunctiveNormalForm = new Text(container, SWT.BORDER);
        this.txtConjunctiveNormalForm.setLayoutData(dataConjunctiveNormalForm);
        this.txtConjunctiveNormalForm.setEditable(false);
        this.txtConjunctiveNormalForm.setText(""); //$NON-NLS-1$

        final Label lbtDisjunctiveNormalForm = new Label(container, SWT.NONE);
        lbtDisjunctiveNormalForm.setText("DNF:"); //$NON-NLS-1$

        final GridData dataDisjunctiveNormalForm = new GridData();
        dataDisjunctiveNormalForm.grabExcessHorizontalSpace = true;
        dataDisjunctiveNormalForm.horizontalAlignment = GridData.FILL;

        this.txtDisjunctiveNormalForm = new Text(container, SWT.BORDER);
        this.txtDisjunctiveNormalForm.setLayoutData(dataDisjunctiveNormalForm);
        this.txtDisjunctiveNormalForm.setEditable(false);
        this.txtDisjunctiveNormalForm.setText(""); //$NON-NLS-1$

        this.txtExpression.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(final KeyEvent e) {
                // Empty block
            }

            @Override
            public void keyReleased(final KeyEvent event) {
                String text = MEPCalculatorView.this.txtExpression.getText();
                MEPCalculatorView.this.txtExpression.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
                if (!Strings.isNullOrEmpty(text)) {
                    try {
                        IMepExpression<?> parsedExpression = MEPCalculatorView.this.mep.parse(text);
                        MEPCalculatorView.this.txtReturnType.setText(parsedExpression.getReturnType().getQualName());
                        MEPCalculatorView.this.txtOptimizedForm.setText(BooleanExpressionOptimizer.optimize(parsedExpression).toString());
                        MEPCalculatorView.this.txtConjunctiveNormalForm.setText(BooleanExpressionOptimizer.toConjunctiveNormalForm(parsedExpression).toString());
                        MEPCalculatorView.this.txtDisjunctiveNormalForm.setText(BooleanExpressionOptimizer.toDisjunctiveNormalForm(parsedExpression).toString());
                    }
                    catch (Throwable e) {
                        MEPCalculatorView.this.txtExpression.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                        MEPCalculatorView.this.txtReturnType.setText("");
                        MEPCalculatorView.this.txtOptimizedForm.setText("");
                        MEPCalculatorView.this.txtConjunctiveNormalForm.setText("");
                        MEPCalculatorView.this.txtDisjunctiveNormalForm.setText("");
                    }
                }
                else {
                    MEPCalculatorView.this.txtReturnType.setText("");
                    MEPCalculatorView.this.txtOptimizedForm.setText("");
                    MEPCalculatorView.this.txtConjunctiveNormalForm.setText("");
                    MEPCalculatorView.this.txtDisjunctiveNormalForm.setText("");
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus() {
        this.txtExpression.setFocus();
    }

}
