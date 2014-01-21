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

import java.util.List;

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
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

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
    public AttributeDialog(final Shell parent) {
        super(parent);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);

        final Monitor monitor = parent.getMonitor();
        final int maxWidth = (monitor.getBounds().width * 2) / 3;

        final Label lblName = new Label(container, SWT.WRAP);
        lblName.setText(OdysseusNLS.Attribute);
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

        final Label lblDatatype = new Label(container, SWT.WRAP);
        lblDatatype.setText(OdysseusNLS.Datatype);
        gd = new GridData();
        int datatypeWidth = lblDatatype.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(datatypeWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblDatatype.setLayoutData(gd);

        this.cmbDatatype = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        List<SDFDatatype> types = SDFProbabilisticDatatype.getTypes();
        for (SDFDatatype type : types) {
            this.cmbDatatype.add(type.getQualName());
            this.cmbDatatype.setData(type.getQualName(), type);
        }

        this.cmbDatatype.select(0);

        gd = new GridData();
        datatypeWidth = this.cmbDatatype.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (datatypeWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.cmbDatatype.setLayoutData(gd);

        return container;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    private void saveInput() {
        final String name = this.txtName.getText();
        final String item = this.cmbDatatype.getItem(this.cmbDatatype.getSelectionIndex());
        final SDFDatatype datatype = (SDFDatatype) this.cmbDatatype.getData(item);
        this.attribute = new SDFAttribute("", name, datatype, null, null, null);
    }

    @Override
    protected void okPressed() {
        this.saveInput();
        super.okPressed();
    }

    public SDFAttribute getAttribute() {
        return this.attribute;
    }

}
