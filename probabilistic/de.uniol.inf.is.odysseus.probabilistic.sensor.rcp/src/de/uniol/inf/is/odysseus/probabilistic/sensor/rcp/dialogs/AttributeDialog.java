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
package de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AttributeDialog extends Dialog {
    private Text txtName;
    private Combo cmbDatatype;
    private SDFAttribute attribute;

    /**
     * Class constructor.
     * 
     * @param parentShell
     */
    public AttributeDialog(Shell parent) {
        super(parent);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);

        Monitor monitor = parent.getMonitor();
        int maxWidth = monitor.getBounds().width * 2 / 3;

        Label lblName = new Label(container, SWT.WRAP);
        lblName.setText(OdysseusNLS.Attribute);
        GridData gd = new GridData();
        int attributeWidth = lblName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(attributeWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblName.setLayoutData(gd);

        txtName = new Text(container, SWT.BORDER);
        gd = new GridData();
        attributeWidth = txtName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (attributeWidth > maxWidth)
            gd.widthHint = maxWidth;
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtName.setLayoutData(gd);

        Label lblDatatype = new Label(container, SWT.WRAP);
        lblDatatype.setText(OdysseusNLS.Datatype);
        gd = new GridData();
        int datatypeWidth = lblDatatype.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(datatypeWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblDatatype.setLayoutData(gd);

        cmbDatatype = new Combo(container, SWT.BORDER);
        cmbDatatype.add(SDFDatatype.BOOLEAN.getQualName());
        cmbDatatype.add(SDFDatatype.BYTE.getQualName());
        cmbDatatype.add(SDFDatatype.SHORT.getQualName());
        cmbDatatype.add(SDFDatatype.INTEGER.getQualName());
        cmbDatatype.add(SDFDatatype.LONG.getQualName());
        cmbDatatype.add(SDFDatatype.FLOAT.getQualName());
        cmbDatatype.add(SDFDatatype.DOUBLE.getQualName());
        
        cmbDatatype.setData(SDFDatatype.BOOLEAN.getQualName(), SDFDatatype.BOOLEAN);
        cmbDatatype.setData(SDFDatatype.BYTE.getQualName(), SDFDatatype.BYTE);
        cmbDatatype.setData(SDFDatatype.SHORT.getQualName(), SDFDatatype.SHORT);
        cmbDatatype.setData(SDFDatatype.INTEGER.getQualName(), SDFDatatype.INTEGER);
        cmbDatatype.setData(SDFDatatype.LONG.getQualName(), SDFDatatype.LONG);
        cmbDatatype.setData(SDFDatatype.FLOAT.getQualName(), SDFDatatype.FLOAT);
        cmbDatatype.setData(SDFDatatype.DOUBLE.getQualName(), SDFDatatype.DOUBLE);

        cmbDatatype.select(0);

        gd = new GridData();
        datatypeWidth = cmbDatatype.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (datatypeWidth > maxWidth)
            gd.widthHint = maxWidth;
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        cmbDatatype.setLayoutData(gd);

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    private void saveInput() {
        String name = txtName.getText();
        String item = cmbDatatype.getItem(cmbDatatype.getSelectionIndex());
        SDFDatatype datatype = (SDFDatatype) cmbDatatype.getData(item);
        this.attribute = new SDFAttribute("", name, datatype);
    }

    @Override
    protected void okPressed() {
        saveInput();
        super.okPressed();
    }

    public SDFAttribute getAttribute() {
        return attribute;
    }

}
