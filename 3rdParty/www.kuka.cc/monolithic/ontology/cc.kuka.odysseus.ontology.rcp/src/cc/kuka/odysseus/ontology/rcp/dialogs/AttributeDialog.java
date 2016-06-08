/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.rcp.dialogs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class AttributeDialog extends Dialog {
    private Text txtName;
    private Combo cmbDatatype;
    private Combo cmbProperty;
    private SDFAttribute attribute;
    private Property property;

    /**
     *
     * Class constructor.
     *
     * @param parent
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

        final Label lblProperty = new Label(container, SWT.WRAP);
        lblProperty.setText(OdysseusNLS.Property);
        gd = new GridData();
        int propertyWidth = lblProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(propertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblProperty.setLayoutData(gd);

        this.cmbProperty = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        fillPropertyCombo(this.cmbProperty);
        this.cmbProperty.select(0);
        gd = new GridData();
        propertyWidth = this.cmbProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (propertyWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.cmbProperty.setLayoutData(gd);

        final Label lblDatatype = new Label(container, SWT.WRAP);
        lblDatatype.setText(OdysseusNLS.Datatype);
        gd = new GridData();
        int datatypeWidth = lblDatatype.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(datatypeWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblDatatype.setLayoutData(gd);

        this.cmbDatatype = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        final List<SDFDatatype> types = SDFDatatype.getTypes();
        for (final SDFDatatype type : types) {
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
        final String datatypeItem = this.cmbDatatype.getItem(this.cmbDatatype.getSelectionIndex());
        final String propertyItem = this.cmbProperty.getItem(this.cmbProperty.getSelectionIndex());

        final SDFDatatype datatype = (SDFDatatype) this.cmbDatatype.getData(datatypeItem);
        this.attribute = new SDFAttribute("", name, datatype, null, null, null);
        this.property = (Property) this.cmbProperty.getData(propertyItem);
    }

    @Override
    protected void okPressed() {
        this.saveInput();
        super.okPressed();
    }

    /**
     * @return the attribute
     */
    public SDFAttribute getAttribute() {
        return this.attribute;
    }

    /**
     * @return the property
     */
    public Property getProperty() {
        return this.property;
    }

    private static void fillPropertyCombo(final Combo combo) {
        final SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
        combo.removeAll();
        final Set<Property> properties = new HashSet<>(ontology.allProperties());

        for (final Property property : properties) {
            combo.add(property.name());
            combo.setData(property.name(), property);
        }
        combo.select(0);
    }
}
