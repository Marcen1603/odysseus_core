package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorgrid;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

public class ColorGridConfigurer extends
		AbstractDashboardPartConfigurer<ColorGridDashboadPart> implements
		ChangeListener {

	ColorGridDashboadPart dashboardPart;
	private SDFSchema[] schemas;
	Text zoomText;
	private Text offsetX;
	private Text offsetY;
	Text repaintRate;
	private boolean reactOnModify = true;

	@Override
	public void init(ColorGridDashboadPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {
		if (roots.size() == 0) {
			throw new IllegalArgumentException(
					"Insifficient physical operators " + roots.size());
		}
		this.dashboardPart = dashboardPartToConfigure;
		this.schemas = new SDFSchema[roots.size()];
		final Iterator<IPhysicalOperator> iter = roots.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			final IPhysicalOperator sink = iter.next();
			this.schemas[i] = sink.getOutputSchema();
		}
		this.dashboardPart.addChangeListner(this);
	}

	public ColorGridDashboadPart getDashboardPart() {
		return dashboardPart;
	}

	@Override
	public void createPartControl(Composite parent) {
		final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);
		final TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
		final Composite composite = form.getBody();
		{// Attribute positions
			final Section section = toolkit.createSection(composite,
					Section.DESCRIPTION | ExpandableComposite.TITLE_BAR
							| ExpandableComposite.TWISTIE
							| ExpandableComposite.EXPANDED);
			section.setText("Attributes");
			section.addExpansionListener(new ExpansionAdapter() {
				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					form.reflow(true);
				}
			});

			final Composite group = toolkit.createComposite(section);

			final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true,
					false);
			group.setLayoutData(gridData);
			group.setLayout(new GridLayout(2, false));
			{// X
				toolkit.createLabel(group, "X");
				final Combo xAttribute = DashboardPartUtil
						.createAttributeDropDown(group, this.schemas[0]);
				xAttribute.select(dashboardPart.getXpos());
				xAttribute.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						final Combo combo = (Combo) e.widget;
						ColorGridConfigurer.this.dashboardPart.setXpos(combo
								.getSelectionIndex());
						fireListener();
					}
				});
			}
			{// Y
				toolkit.createLabel(group, "Y");
				final Combo yAttribute = DashboardPartUtil
						.createAttributeDropDown(group, this.schemas[0]);
				yAttribute.select(this.getDashboardPart().getYpos());
				yAttribute.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						final Combo combo = (Combo) e.widget;
						ColorGridConfigurer.this.dashboardPart.setYpos(combo
								.getSelectionIndex());
						fireListener();

					}
				});
			}
			{// Z
				toolkit.createLabel(group, "value");
				final Combo zAttribute = DashboardPartUtil
						.createAttributeDropDown(group, this.schemas[0]);
				zAttribute.select(this.getDashboardPart().getValue_pos());
				zAttribute.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						final Combo combo = (Combo) e.widget;
						ColorGridConfigurer.this.dashboardPart
								.setValue_pos(combo.getSelectionIndex());
						fireListener();

					}
				});
			}
			{// Witdh
				toolkit.createLabel(group, "Width per cell");
				final Text boxWidth = toolkit.createText(group, ""
						+ this.getDashboardPart().getBoxWidth());
				boxWidth.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = boxWidth.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setBoxWidth(Integer.parseInt(text));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{// Height
				toolkit.createLabel(group, "Height per cell");
				final Text boxHeight = toolkit.createText(group, ""
						+ this.getDashboardPart().getBoxHeight());
				boxHeight.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = boxHeight.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setBoxHeight(Integer.parseInt(text));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{// Height
				toolkit.createLabel(group, "Number Cells x");
				final Text numberCellsX = toolkit.createText(group, ""
						+ this.getDashboardPart().getWidth());
				numberCellsX.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = numberCellsX.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setWidth(Integer.parseInt(text));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{// Number cells y
				toolkit.createLabel(group, "Number Cells y");
				final Text numberCellsY = toolkit.createText(group, ""
						+ this.getDashboardPart().getHeight());
				numberCellsY.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = numberCellsY.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setHeight(Integer.parseInt(text));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{// Max Duration
				toolkit.createLabel(group, "Max Duration (ms)");
				final Text maxElementsText = toolkit.createText(group, String
						.format("%15d%n", this.getDashboardPart()
								.getMaxDuration()));
				maxElementsText.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = maxElementsText.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setMaxDuration(Integer.parseInt(text));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{// UpdateThread
				toolkit.createLabel(group, "Repaint Delay (msec)");
				repaintRate = toolkit.createText(group, String.format("%15d%n",
						this.getDashboardPart().getRepaintDelay()));
				repaintRate.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = repaintRate.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setRepaintDelay(Integer.parseInt(text));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			section.setClient(group);
		}

		{// Colors
			final Section section = toolkit.createSection(composite,
					Section.DESCRIPTION | ExpandableComposite.TITLE_BAR
							| ExpandableComposite.TWISTIE
							| ExpandableComposite.EXPANDED);
			section.setText("Appearance");
			section.addExpansionListener(new ExpansionAdapter() {
				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					form.reflow(true);
				}
			});

			final Composite group = toolkit.createComposite(section);
			final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true,
					false);
			group.setLayoutData(gridData);
			group.setLayout(new GridLayout(3, false));
			{// Backgroundcolor
				toolkit.createLabel(group, "Background");
				final RGB backgroundColor = this.getDashboardPart()
						.getBackgroundColor();
				//@SuppressWarnings("boxing")
				final Text backgroundColorText = toolkit.createText(group,
						String.format("%s,%s,%s", (int) backgroundColor.R,
								(int) backgroundColor.G,
								(int) backgroundColor.B));
				backgroundColorText.setBackground(new Color(group.getShell()
						.getDisplay(), new org.eclipse.swt.graphics.RGB(
						(int) backgroundColor.R, (int) backgroundColor.G,
						(int) backgroundColor.B)));
				backgroundColorText.setEditable(false);
				final Button backgroundColorButton = toolkit.createButton(
						group, "..", SWT.PUSH | SWT.BORDER);
				backgroundColorButton
						.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(final SelectionEvent e) {
								final ColorDialog dialog = new ColorDialog(
										group.getShell());
								dialog.setText("Select Color");
								dialog.setRGB(new org.eclipse.swt.graphics.RGB(
										255, 255, 255));
								final org.eclipse.swt.graphics.RGB selectedColor = dialog
										.open();
								if (selectedColor == null) {
									return;
								}
								backgroundColorText.setText(selectedColor.red
										+ "," + selectedColor.green + ","
										+ selectedColor.blue);
								backgroundColorText.setBackground(new Color(
										group.getShell().getDisplay(),
										selectedColor));
								ColorGridConfigurer.this.getDashboardPart()
										.setBackgroundColor(
												new RGB(selectedColor.red,
														selectedColor.green,
														selectedColor.blue));
								fireListener();
							}
						});
			}
			{// Alpha
				toolkit.createLabel(group, "Alpha");
				final int alpha = this.getDashboardPart().getBackgroundAlpha();
				//@SuppressWarnings("boxing")
				final Slider alphaSlide = new Slider(group, SWT.NONE);
				alphaSlide.setMinimum(0);
				alphaSlide.setMaximum(255);
				alphaSlide.setIncrement(1);
				alphaSlide.setSelection(alpha);
				alphaSlide.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						ColorGridConfigurer.this.getDashboardPart()
								.setBackgroundAlpha(alphaSlide.getSelection());
						fireListener();
					}
				});
				toolkit.createLabel(group, "");
			}
			{// Background image
				toolkit.createLabel(group, "Background Image");
				final String image = this.getDashboardPart().getImagePath();
				//@SuppressWarnings("boxing")
				final Text imageText = toolkit.createText(group, image);
				imageText.setEditable(true);
				final Button imageButton = toolkit.createButton(group, "..",
						SWT.PUSH | SWT.BORDER);
				imageButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
								getDashboardPart().getCanvas().getParent()
										.getShell(),
								new WorkbenchLabelProvider(),
								new WorkbenchContentProvider());
						dialog.setInput(getDashboardPart().getProject());

						dialog.addFilter(new ViewerFilter() {
							@Override
							public boolean select(Viewer viewer,
									Object parentElement, Object element) {
								if (element instanceof IFile) {
									IFile res = (IFile) element;
									if (validFileResouce(res)) {
										return true;
									}
									return false;
								}
								return true;
							}
						});
						dialog.setValidator(new ISelectionStatusValidator() {

							@Override
							public IStatus validate(Object[] selection) {
								dialog.getOkButton().setEnabled(false);
								if (selection.length <= 0) {
									return new Status(IStatus.ERROR,
											PlatformUI.PLUGIN_ID,
											"You have to choose at least one image");
								}
								Object sel = selection[0];
								if (sel instanceof IFile) {
									IFile file = (IFile) sel;
									if (validFileResouce(file)) {
										dialog.getOkButton().setEnabled(true);
										return Status.OK_STATUS;
									}
								}
								return new Status(IStatus.ERROR,
										PlatformUI.PLUGIN_ID,
										"You can only choose images");
							}
						});

						dialog.setTitle("Choose one or more images");

						if (dialog.open() == Window.OK) {
							for (Object o : dialog.getResult()) {
								IResource resource = (IResource) o;
								String filename = resource
										.getProjectRelativePath().toOSString();
								imageText.setText(filename);
								ColorGridConfigurer.this.getDashboardPart()
										.setImagePath(filename);

							}
						}

						fireListener();
					}
				});
			}
			{// Scale Image
				toolkit.createLabel(group, "Scale Image");
				final Text minXText = toolkit.createText(group, "  "
						+ this.getDashboardPart().getImageScale());
				toolkit.createLabel(group, "");

				minXText.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = minXText.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setImageScale(Double.parseDouble(text
												.trim()));
								fireListener();
							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{
				toolkit.createLabel(group, "Background Image Offset x");
				final Text numberCellsX = toolkit.createText(group, String
						.format("%10d%n", this.getDashboardPart()
								.getBackgroundImageOffsetX()));
				toolkit.createLabel(group, "");
				numberCellsX.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = numberCellsX.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setBackgroundImageOffsetX(Integer
												.parseInt(text.trim()));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{
				toolkit.createLabel(group, "Background Image Offset y");
				final Text numberCellsY = toolkit.createText(group, String
						.format("%10d%n", +this.getDashboardPart()
								.getBackgroundImageOffsetY()));
				toolkit.createLabel(group, "");
				numberCellsY.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						final String text = numberCellsY.getText().trim();
						if (!"".equals(text)) {
							try {
								ColorGridConfigurer.this.dashboardPart
										.setBackgroundImageOffsetY(Integer
												.parseInt(text.trim()));
								fireListener();

							} catch (final NumberFormatException ex) {
								// Empty block
							}
						}
					}
				});
			}
			{// Scale Image
				toolkit.createLabel(group, "Zoom");
				zoomText = toolkit.createText(group, "  "
						+ this.getDashboardPart().getZoom());
				toolkit.createLabel(group, "");

				zoomText.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						if (reactOnModify) {
							final String text = zoomText.getText().trim();
							if (!"".equals(text)) {
								try {
									ColorGridConfigurer.this.dashboardPart
											.setZoom(Double.parseDouble(text
													.trim()));
									fireListener();
								} catch (final NumberFormatException ex) {
									// Empty block
								}
							}
						}
					}
				});
			}
			{
				toolkit.createLabel(group, "Offset x");
				offsetX = toolkit.createText(group, String.format("%10d%n",
						this.getDashboardPart().getOffsetX()));
				toolkit.createLabel(group, "");
				offsetX.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						if (reactOnModify) {
							final String text = offsetX.getText().trim();
							if (!"".equals(text)) {
								try {
									ColorGridConfigurer.this.dashboardPart
											.setOffsetX(Integer.parseInt(text
													.trim()));
									fireListener();

								} catch (final NumberFormatException ex) {
									// Empty block
								}
							}
						}
					}
				});
			}
			{
				toolkit.createLabel(group, "Offset y");
				offsetY = toolkit.createText(group, String.format("%10d%n",
						+this.getDashboardPart().getOffsetY()));
				toolkit.createLabel(group, "");
				offsetY.addModifyListener(new ModifyListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void modifyText(final ModifyEvent e) {
						if (reactOnModify) {
							final String text = offsetY.getText().trim();
							if (!"".equals(text)) {
								try {
									ColorGridConfigurer.this.dashboardPart
											.setOffsetY(Integer.parseInt(text
													.trim()));
									fireListener();

								} catch (final NumberFormatException ex) {
									// Empty block
								}
							}
						}
					}
				});
			}

			{// Foregroundcolor
				toolkit.createLabel(group, "Base Color");

				final RGB color = this.getDashboardPart().getColor();
				//@SuppressWarnings("boxing")
				final Text colorText = toolkit.createText(group, String
						.format("%s,%s,%s", (int) color.R, (int) color.G,
								(int) color.B));
				colorText.setBackground(new Color(
						group.getShell().getDisplay(),
						new org.eclipse.swt.graphics.RGB((int) color.R,
								(int) color.G, (int) color.B)));
				colorText.setEditable(false);
				final Button foregroundColorButton = toolkit.createButton(
						group, "..", SWT.PUSH | SWT.BORDER);
				foregroundColorButton
						.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(final SelectionEvent e) {
								final ColorDialog dialog = new ColorDialog(
										group.getShell());
								dialog.setText("Select Color");
								dialog.setRGB(new org.eclipse.swt.graphics.RGB(
										255, 255, 255));
								final org.eclipse.swt.graphics.RGB selectedColor = dialog
										.open();
								if (selectedColor == null) {
									return;
								}
								colorText.setText(selectedColor.red + ","
										+ selectedColor.green + ","
										+ selectedColor.blue);
								colorText
										.setBackground(new Color(group
												.getShell().getDisplay(),
												selectedColor));
								ColorGridConfigurer.this.getDashboardPart()
										.setColor(
												new RGB(selectedColor.red,
														selectedColor.green,
														selectedColor.blue));
								fireListener();
							}
						});
			}
			section.setClient(group);

		}

		composite.setBackground(composite.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
	}

	private boolean validFileResouce(IFile file) {
		String[] extensions = { "png", "gif", "jpg", "jpeg" };
		for (String ext : extensions) {
			if (ext.equalsIgnoreCase(file.getFileExtension())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void dispose() {
		getDashboardPart().removeChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		reactOnModify = false;
		zoomText.setText(String.valueOf(getDashboardPart().getZoom()));
		offsetX.setText(String.valueOf(getDashboardPart().getOffsetX()));
		offsetY.setText(String.valueOf(getDashboardPart().getOffsetY()));
		reactOnModify = true;
		fireListener();
	}
}
