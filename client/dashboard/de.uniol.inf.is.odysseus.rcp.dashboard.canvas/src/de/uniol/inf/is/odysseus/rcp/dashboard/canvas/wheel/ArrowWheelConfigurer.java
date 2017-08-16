/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.wheel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: ArrowWheelConfigurer.java | Fri Apr 10 23:28:57 2015 +0000 |
 *          ckuka $
 *
 */
public class ArrowWheelConfigurer extends AbstractWheelDashboardPartConfigurer<ArrowWheelDashboardPart> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createPartControl(final ArrowWheelDashboardPart dashboardPart, final FormToolkit toolkit, final ScrolledForm form) {
        final Composite composite = form.getBody();

        {// Arrow Settings
            final Section section = toolkit.createSection(composite, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
            section.setText("Arrow Settings");
            section.addExpansionListener(new ExpansionAdapter() {
                @Override
                public void expansionStateChanged(final ExpansionEvent e) {
                    form.reflow(true);
                }
            });

            final Composite group = toolkit.createComposite(section);
            final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
            group.setLayoutData(gridData);
            group.setLayout(new GridLayout(3, false));
            {// Gauge color
                toolkit.createLabel(group, "Color");
                final RGB arrowColor = this.getDashboardPart().getArrowColor();
                //@SuppressWarnings("boxing")
                final Text gaugeColorText = toolkit.createText(group, String.format("%s,%s,%s", (int) arrowColor.R, (int) arrowColor.G, (int) arrowColor.B));
                gaugeColorText.setBackground(new Color(group.getShell().getDisplay(), new org.eclipse.swt.graphics.RGB((int) arrowColor.R, (int) arrowColor.G, (int) arrowColor.B)));
                gaugeColorText.setEditable(false);
                final Button gaugeColorButton = toolkit.createButton(group, "..", SWT.PUSH | SWT.BORDER);
                gaugeColorButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final ColorDialog dialog = new ColorDialog(group.getShell());
                        dialog.setText("Select Color");
                        dialog.setRGB(new org.eclipse.swt.graphics.RGB(255, 255, 255));
                        final org.eclipse.swt.graphics.RGB selectedColor = dialog.open();
                        if (selectedColor == null) {
                            return;
                        }
                        gaugeColorText.setText(selectedColor.red + "," + selectedColor.green + "," + selectedColor.blue);
                        gaugeColorText.setBackground(new Color(group.getShell().getDisplay(), selectedColor));
                        ArrowWheelConfigurer.this.getDashboardPart().setArrowColor(new RGB(selectedColor.red, selectedColor.green, selectedColor.blue));
                        fireListener();
                    }
                });
            }
            section.setClient(group);
        }

    }

}
