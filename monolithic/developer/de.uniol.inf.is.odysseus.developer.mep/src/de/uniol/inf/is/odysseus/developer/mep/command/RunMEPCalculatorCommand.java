/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.developer.mep.command;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.optimizer.ExpressionOptimizer;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$ *
 *
 */
public class RunMEPCalculatorCommand extends AbstractHandler {
    static final Logger LOG = LoggerFactory.getLogger(RunMEPCalculatorCommand.class);

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final MEPCalculatorDialog dialog = new MEPCalculatorDialog(Display.getDefault().getActiveShell());
        dialog.create();
        if (dialog.open() == Window.OK) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(dialog.getExpression());
            clipboard.setContents(stringSelection, null);
        }
        return null;
    }

    class MEPCalculatorDialog extends TitleAreaDialog {
        Text txtExpression;
        Text txtReturnType;
        Text txtOptimizedForm;
        Text txtConjunctiveNormalForm;
        Text txtDisjunctiveNormalForm;

        String expression;
        MEP mep;

        public MEPCalculatorDialog(final Shell parent) {
            super(parent);
            this.mep = MEP.getInstance();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void create() {
            super.create();
            this.setTitle("MEP Calculator"); //$NON-NLS-1$
            this.setMessage("MEP Calculator", IMessageProvider.INFORMATION); //$NON-NLS-1$
        }

        @Override
        protected Control createDialogArea(final Composite parent) {
            final Composite area = (Composite) super.createDialogArea(parent);
            final Composite container = new Composite(area, SWT.NONE);
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
            this.txtReturnType.setText(""); //$NON-NLS-1$

            final Label lbtOptimizedForm = new Label(container, SWT.NONE);
            lbtOptimizedForm.setText("Optimized:"); //$NON-NLS-1$

            final GridData dataOptimizedForm = new GridData();
            dataOptimizedForm.grabExcessHorizontalSpace = true;
            dataOptimizedForm.horizontalAlignment = GridData.FILL;

            this.txtOptimizedForm = new Text(container, SWT.BORDER);
            this.txtOptimizedForm.setLayoutData(dataOptimizedForm);
            this.txtOptimizedForm.setText(""); //$NON-NLS-1$

            final Label lbtConjunctiveNormalForm = new Label(container, SWT.NONE);
            lbtConjunctiveNormalForm.setText("CNF:"); //$NON-NLS-1$

            final GridData dataConjunctiveNormalForm = new GridData();
            dataConjunctiveNormalForm.grabExcessHorizontalSpace = true;
            dataConjunctiveNormalForm.horizontalAlignment = GridData.FILL;

            this.txtConjunctiveNormalForm = new Text(container, SWT.BORDER);
            this.txtConjunctiveNormalForm.setLayoutData(dataConjunctiveNormalForm);
            this.txtConjunctiveNormalForm.setText(""); //$NON-NLS-1$

            final Label lbtDisjunctiveNormalForm = new Label(container, SWT.NONE);
            lbtDisjunctiveNormalForm.setText("DNF:"); //$NON-NLS-1$

            final GridData dataDisjunctiveNormalForm = new GridData();
            dataDisjunctiveNormalForm.grabExcessHorizontalSpace = true;
            dataDisjunctiveNormalForm.horizontalAlignment = GridData.FILL;

            this.txtDisjunctiveNormalForm = new Text(container, SWT.BORDER);
            this.txtDisjunctiveNormalForm.setLayoutData(dataDisjunctiveNormalForm);
            this.txtDisjunctiveNormalForm.setText(""); //$NON-NLS-1$

            this.txtExpression.addKeyListener(new KeyListener() {

                @Override
                public void keyPressed(final KeyEvent e) {

                }

                @Override
                public void keyReleased(final KeyEvent event) {
                    String text = txtExpression.getText();
                    if ((text != null) && (!"".equals(text))) {
                        try {
                            IExpression<?> parsedExpression = mep.parse(text);
                            txtReturnType.setText(parsedExpression.getReturnType().getQualName());
                            txtOptimizedForm.setText(ExpressionOptimizer.optimize(parsedExpression).toString());
                            txtConjunctiveNormalForm.setText(ExpressionOptimizer.toConjunctiveNormalForm(parsedExpression).toString());
                            txtDisjunctiveNormalForm.setText(ExpressionOptimizer.toDisjunctiveNormalForm(parsedExpression).toString());
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            return area;
        }

        @Override
        protected boolean isResizable() {
            return true;
        }

        private void saveInput() {
            this.expression = this.txtExpression.getText();
        }

        @Override
        protected void okPressed() {
            this.saveInput();
            super.okPressed();
        }

        /**
         * @return the expression
         */
        public String getExpression() {
            return this.expression;
        }
    }
}
