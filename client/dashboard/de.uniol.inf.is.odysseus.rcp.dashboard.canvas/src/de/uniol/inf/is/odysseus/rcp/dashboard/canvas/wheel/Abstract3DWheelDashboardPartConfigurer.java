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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: Abstract3DWheelDashboardPartConfigurer.java | Fri Apr 10
 *          23:28:57 2015 +0000 | ckuka $
 *
 */
public abstract class Abstract3DWheelDashboardPartConfigurer<T extends Abstract3DWheelDashboardPart> extends AbstractDashboardPartConfigurer<T> {
    T dashboardPart;
    private SDFSchema[] schemas;

    @Override
    public void init(final T dashboardPart, final Collection<IPhysicalOperator> roots) {
        this.dashboardPart = dashboardPart;
        this.schemas = new SDFSchema[roots.size()];
        final Iterator<IPhysicalOperator> iter = roots.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            final IPhysicalOperator sink = iter.next();
            this.schemas[i] = sink.getOutputSchema();
        }
    }

    /**
     * @return the dashboardPart
     */
    public T getDashboardPart() {
        return this.dashboardPart;
    }

    @Override
    public void createPartControl(final Composite parent) {
        final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        final ScrolledForm form = toolkit.createScrolledForm(parent);
        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        form.getBody().setLayout(layout);
        final Composite composite = form.getBody();

        {// Attribute positions
            final Section section = toolkit.createSection(composite, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
            section.setText("Attributes");
            section.addExpansionListener(new ExpansionAdapter() {
                @Override
                public void expansionStateChanged(final ExpansionEvent e) {
                    form.reflow(true);
                }
            });

            final Composite group = toolkit.createComposite(section);

            final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
            group.setLayoutData(gridData);
            group.setLayout(new GridLayout(2, false));
            {// X
                toolkit.createLabel(group, "X");
                final Combo xAttribute = DashboardPartUtil.createAttributeDropDown(group, this.schemas[0]);
                xAttribute.select(this.getDashboardPart().getXPos());
                xAttribute.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final Combo combo = (Combo) e.widget;
                        Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setXPos(combo.getSelectionIndex());
                        fireListener();
                    }
                });
            }
            {// Y
                toolkit.createLabel(group, "Y");
                final Combo yAttribute = DashboardPartUtil.createAttributeDropDown(group, this.schemas[0]);
                yAttribute.select(this.getDashboardPart().getYPos());
                yAttribute.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final Combo combo = (Combo) e.widget;
                        Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setYPos(combo.getSelectionIndex());
                        fireListener();

                    }
                });
            }
            {// Z
                toolkit.createLabel(group, "Z");
                final Combo zAttribute = DashboardPartUtil.createAttributeDropDown(group, this.schemas[0]);
                zAttribute.select(this.getDashboardPart().getZPos());
                zAttribute.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final Combo combo = (Combo) e.widget;
                        Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setZPos(combo.getSelectionIndex());
                        fireListener();

                    }
                });
            }
            {// Max Elements
                toolkit.createLabel(group, "Elements");
                final Text maxElementsText = toolkit.createText(group, "" + this.getDashboardPart().getMaxElements());
                maxElementsText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = maxElementsText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setMaxElements(Integer.parseInt(text));
                            fireListener();
                        }
                    }
                });
            }
            section.setClient(group);
        }

        {// Adjustment
            final Section section = toolkit.createSection(composite, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
            section.setText("Adjustment");
            section.addExpansionListener(new ExpansionAdapter() {
                @Override
                public void expansionStateChanged(final ExpansionEvent e) {
                    form.reflow(true);
                }
            });

            final Composite group = toolkit.createComposite(section);
            final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
            group.setLayoutData(gridData);
            group.setLayout(new GridLayout(4, false));

            {// Autoadjust
                final Composite autoadjustComposite = new Composite(group, SWT.NONE);
                final GridData autoadjustCompositeGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
                autoadjustCompositeGridData.horizontalSpan = 4;
                autoadjustComposite.setLayoutData(autoadjustCompositeGridData);
                autoadjustComposite.setLayout(new GridLayout(2, false));

                toolkit.createLabel(autoadjustComposite, "Autoadjust");
                final Button autoadjustButton = toolkit.createButton(autoadjustComposite, "", SWT.CHECK);
                autoadjustButton.setSelection(this.dashboardPart.isAutoadjust());
                autoadjustButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final Button b = (Button) e.widget;
                        Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setAutoadjust(b.getSelection());
                        fireListener();
                    }
                });

            }
            {// Min X
                toolkit.createLabel(group, "Min X");
                final Text minXText = toolkit.createText(group, "" + this.getDashboardPart().getMinX());
                minXText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = minXText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setMinX(Double.parseDouble(text));
                            fireListener();
                        }
                    }
                });
            }
            {// Max X
                toolkit.createLabel(group, "Max X");
                final Text maxXText = toolkit.createText(group, "" + this.getDashboardPart().getMaxX());
                maxXText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = maxXText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setMaxX(Double.parseDouble(text));
                            fireListener();
                        }
                    }
                });
            }
            {// Min Y
                toolkit.createLabel(group, "Min Y");
                final Text minYText = toolkit.createText(group, "" + this.getDashboardPart().getMinY());
                minYText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = minYText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setMinY(Double.parseDouble(text));
                            fireListener();
                        }
                    }
                });
            }
            {// Max Y
                toolkit.createLabel(group, "Max Y");
                final Text maxYText = toolkit.createText(group, "" + this.getDashboardPart().getMaxY());
                maxYText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = maxYText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setMaxY(Double.parseDouble(text));
                            fireListener();
                        }
                    }
                });
            }
            {// Min Z
                toolkit.createLabel(group, "Min Z");
                final Text minZText = toolkit.createText(group, "" + this.getDashboardPart().getMinZ());
                minZText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = minZText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setMinZ(Double.parseDouble(text));
                            fireListener();
                        }
                    }
                });
            }
            {// Max Z
                toolkit.createLabel(group, "Max Z");
                final Text maxZText = toolkit.createText(group, "" + this.getDashboardPart().getMaxZ());
                maxZText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = maxZText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setMaxZ(Double.parseDouble(text));
                            fireListener();
                        }
                    }
                });
            }
            section.setClient(group);
        }
        {// Colors
            final Section section = toolkit.createSection(composite, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
            section.setText("Size and Color");
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
            {// Radius
                toolkit.createLabel(group, "Radius");
                final Text radiusText = toolkit.createText(group, "10");
                radiusText.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final String text = radiusText.getText();
                        if (!"".equals(text)) {
                            Abstract3DWheelDashboardPartConfigurer.this.dashboardPart.setRadius(Integer.parseInt(text));
                            fireListener();
                        }
                    }
                });
                DashboardPartUtil.createLabel(group, "");
            }
            {// Backgroundcolor
                toolkit.createLabel(group, "Background");
                final RGB backgroundColor = this.getDashboardPart().getBackgroundColor();
               // //@SuppressWarnings("boxing")
                final Text backgroundColorText = toolkit.createText(group, String.format("%s,%s,%s", (int) backgroundColor.R, (int) backgroundColor.G, (int) backgroundColor.B));
                backgroundColorText
                        .setBackground(new Color(group.getShell().getDisplay(), new org.eclipse.swt.graphics.RGB((int) backgroundColor.R, (int) backgroundColor.G, (int) backgroundColor.B)));
                backgroundColorText.setEditable(false);
                final Button backgroundColorButton = toolkit.createButton(group, "..", SWT.PUSH | SWT.BORDER);
                backgroundColorButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final ColorDialog dialog = new ColorDialog(group.getShell());
                        dialog.setText("Select Color");
                        dialog.setRGB(new org.eclipse.swt.graphics.RGB(255, 255, 255));
                        final org.eclipse.swt.graphics.RGB selectedColor = dialog.open();
                        if (selectedColor == null) {
                            return;
                        }
                        backgroundColorText.setText(selectedColor.red + "," + selectedColor.green + "," + selectedColor.blue);
                        backgroundColorText.setBackground(new Color(group.getShell().getDisplay(), selectedColor));
                        Abstract3DWheelDashboardPartConfigurer.this.getDashboardPart().setBackgroundColor(new RGB(selectedColor.red, selectedColor.green, selectedColor.blue));
                        fireListener();
                    }
                });
            }
            {// Foregroundcolor
                toolkit.createLabel(group, "Foreground");

                final RGB foregroundColor = this.getDashboardPart().getForegroundColor();
                //@SuppressWarnings("boxing")
                final Text foregroundColorText = toolkit.createText(group, String.format("%s,%s,%s", (int) foregroundColor.R, (int) foregroundColor.G, (int) foregroundColor.B));
                foregroundColorText
                        .setBackground(new Color(group.getShell().getDisplay(), new org.eclipse.swt.graphics.RGB((int) foregroundColor.R, (int) foregroundColor.G, (int) foregroundColor.B)));
                foregroundColorText.setEditable(false);
                final Button foregroundColorButton = toolkit.createButton(group, "..", SWT.PUSH | SWT.BORDER);
                foregroundColorButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        final ColorDialog dialog = new ColorDialog(group.getShell());
                        dialog.setText("Select Color");
                        dialog.setRGB(new org.eclipse.swt.graphics.RGB(255, 255, 255));
                        final org.eclipse.swt.graphics.RGB selectedColor = dialog.open();
                        if (selectedColor == null) {
                            return;
                        }
                        foregroundColorText.setText(selectedColor.red + "," + selectedColor.green + "," + selectedColor.blue);
                        foregroundColorText.setBackground(new Color(group.getShell().getDisplay(), selectedColor));
                        Abstract3DWheelDashboardPartConfigurer.this.getDashboardPart().setForegroundColor(new RGB(selectedColor.red, selectedColor.green, selectedColor.blue));
                        fireListener();
                    }
                });
            }
            section.setClient(group);
        }

        this.createPartControl(this.dashboardPart, toolkit, form);
        composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    }

    /**
     * @param dashboardPart
     * @param composite
     */
    protected abstract void createPartControl(final T dashboardPart, final FormToolkit toolkit, final ScrolledForm form);

    @Override
    public void dispose() {
        // Empty block
    }
}
