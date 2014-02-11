/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.ontology.rcp.dialogs;

import java.net.URI;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.ontology.common.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.common.model.Property;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class PropertyDialog extends Dialog {
    private Text txtName;
    private Text txtURI;
    private Property property;

    /**
     * Class constructor.
     * 
     * @param parentShell
     */
    public PropertyDialog(final Shell parent) {
        super(parent);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);

        final Monitor monitor = parent.getMonitor();
        final int maxWidth = (monitor.getBounds().width * 2) / 3;

        final Label lblName = new Label(container, SWT.WRAP);
        lblName.setText(OdysseusNLS.Property);
        GridData gd = new GridData();
        int attributeWidth = lblName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(attributeWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblName.setLayoutData(gd);

        this.txtName = new Text(container, SWT.BORDER);
        gd = new GridData();
        attributeWidth = this.txtName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (attributeWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.txtName.setLayoutData(gd);
        this.txtName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                final int fragmentPos = PropertyDialog.this.txtURI.getText().indexOf("#");
                if (fragmentPos > 0) {
                    PropertyDialog.this.txtURI.setText(PropertyDialog.this.txtURI.getText().substring(0, fragmentPos + 1) + PropertyDialog.this.txtName.getText());
                }
                else {
                    PropertyDialog.this.txtURI.setText(ODYSSEUS.NS + PropertyDialog.this.txtName.getText());
                }
            }

        });

        final Label lblURI = new Label(container, SWT.WRAP);
        lblURI.setText(OdysseusNLS.URI);
        gd = new GridData();
        int uriWidth = lblURI.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(uriWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblURI.setLayoutData(gd);

        this.txtURI = new Text(container, SWT.BORDER);
        this.txtURI.setText(ODYSSEUS.NS);
        gd = new GridData();
        uriWidth = this.txtURI.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (uriWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.txtURI.setLayoutData(gd);

        return container;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    private void saveInput() {
        final String name = this.txtName.getText();
        final URI uri = URI.create(this.txtURI.getText());
        this.property = new Property(uri, name);
    }

    @Override
    protected void okPressed() {
        this.saveInput();
        super.okPressed();
    }

    public Property getProperty() {
        return this.property;
    }

}
