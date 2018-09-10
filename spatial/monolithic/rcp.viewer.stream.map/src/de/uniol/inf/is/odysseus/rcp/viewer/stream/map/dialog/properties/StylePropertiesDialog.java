package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style.PersistentCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.ColorCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.IntegerCondition;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.expression.ShapeCondition;

/**
 * 
 * Dialog to edit style parameters of a layer
 * 
 * @author Stefan Bothe
 * 
 */
public class StylePropertiesDialog extends TitleAreaDialog {

	private static final String DEFAULT = "<?xml version=\"1.0\" standalone=\"no\"?>"
			+ "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\""
			+ "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">"
			+ "<svg width=\"16\" height=\"16\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">"
			+ "<circle cx=\"8\" cy=\"8\" r=\"7.5\" "
			+ "fill=\"$VAR$\" stroke=\"$VAR$\" stroke-width=\"$VAR$\"  />"
			+ "</svg>";;

	private static final Logger LOG = LoggerFactory
			.getLogger(TitleAreaDialog.class);

	private TreeViewer treeViewer;
	private PropertiesLabelProvider lblProvider = new PropertiesLabelProvider();
	HashMap<Integer, Style> hashStyles;

	private Label lblLinewidth, lblLinecolor, lblLinecolor_show, lblFillcolor,
			lblFillcolor_show, lblSaved, lblLineWidExpression,
			lblLineColExpression, lblFillColExpression;
	private Button btnChangeLine, btnChangeFill, btnSquare, btnCircle,
			btnTriangle, btnStar, btnExternalsvgFile, btnFile, btnDefaultValue,
			btnExpression;
	private Text txtLinewidth, txtFilepath, txtDefaultvalue,
			txtExpressionInput, txtLineWidExpression, txtLineColExpression,
			txtFillColExpression;

	private VectorLayer layer;
	private Style currentStyle = null;
	private CollectionStyle collStyle;
	private PointStyle pointStyle = null;
	private LineStyle lineStyle = null;
	private PolygonStyle polyStyle = null;

	private LayerConfiguration layerConfiguration = null;

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public StylePropertiesDialog(Shell parentShell,ILayer layer) {
		super(parentShell);
		this.setShellStyle(SWT.MAX | SWT.RESIZE);
		this.layer = (VectorLayer) layer;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		collStyle = new CollectionStyle();
		pointStyle = null;
		lineStyle = null;
		polyStyle = null;

		Style[] layerStyles = null;
		hashStyles = new HashMap<Integer, Style>();

		layerStyles = layer.getStyle().getSubstyles();

		for (Style style : layerStyles) {
			hashStyles.put(style.hashCode(), style);
		}

		setTitle("Style Properties: " + layer.getName());
		setMessage("Edit style parameters.", IMessageProvider.INFORMATION);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridData gd_container = new GridData(GridData.FILL, GridData.BEGINNING,
				false, false);
		gd_container.heightHint = 317;
		container.setLayoutData(gd_container);

		treeViewer = new TreeViewer(container, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.setContentProvider(new PropertiesContentProvider());
		treeViewer.setLabelProvider(lblProvider);
		treeViewer.setInput(new PropertiesModel(
				layer.getStyle().getSubstyles(), layer));
		treeViewer.refresh();
		treeViewer.expandAll();
		// first element selected
		treeViewer.getTree().setSelection(treeViewer.getTree().getItem(0));
		treeViewer.setSelection(treeViewer.getSelection(), true);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				lblSaved.setText("");
				if (event.getSelection().isEmpty()) {
					currentStyle = null;
					return;
				}
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();
					if (selection.getFirstElement() instanceof PointStyle) {
						currentStyle = (PointStyle) selection.getFirstElement();
						pointStyle = (PointStyle) hashStyles.get(currentStyle
								.hashCode());
						txtLinewidth.setText(pointStyle.getLineWidth()
								.getDefault().toString());
						txtLineWidExpression
								.setText((pointStyle.getLineWidth()
										.getSerializable().expression != null) ? pointStyle
										.getLineWidth().getSerializable().expression
										: "");
						txtLineColExpression
								.setText((pointStyle.getLineColor()
										.getSerializable().expression != null) ? pointStyle
										.getLineColor().getSerializable().expression
										: "");
						txtFillColExpression
								.setText((pointStyle.getFillColor()
										.getSerializable().expression != null) ? pointStyle
										.getFillColor().getSerializable().expression
										: "");
						Color lineColor = ColorManager.getInstance()
								.getColor(pointStyle.getLineColor()
										.getDefault().getRGB());
						lblLinecolor_show.setBackground(lineColor);
						txtFilepath.setText((String) pointStyle.getShape()
								.getSerializable().defaultValue);
						txtFilepath.setToolTipText((String) pointStyle
								.getShape().getSerializable().defaultValue);
						txtDefaultvalue.setText(pointStyle.getSize()
								.getSerializable().defaultValue.toString());
						Color fillColor = ColorManager.getInstance()
								.getColor(pointStyle.getFillColor()
										.getDefault().getRGB());
						lblFillcolor_show.setBackground(fillColor);
						if (pointStyle.getSize().getSerializable().expression != null) {
							txtExpressionInput.setText(pointStyle.getSize()
									.getSerializable().expression);
						}
						if (pointStyle.getShape().getSerializable().defaultValue != null) {
							btnExternalsvgFile.setSelection(true);
							btnSquare.setSelection(false);
							btnCircle.setSelection(false);
							btnTriangle.setSelection(false);
							btnStar.setSelection(false);
						}
						if (pointStyle.getSize().getSerializable().defaultValue != null) {
							btnDefaultValue.setSelection(true);
							btnExpression.setSelection(false);
							txtExpressionInput.setEnabled(false);
						}
					}
					if (selection.getFirstElement() instanceof LineStyle) {
						currentStyle = (LineStyle) selection.getFirstElement();
						lineStyle = (LineStyle) hashStyles.get(currentStyle
								.hashCode());
						txtLinewidth.setText(lineStyle.getLineWidth()
								.getDefault().toString());
						Color lineColor = ColorManager.getInstance().getColor(lineStyle
								.getLineColor().getDefault().getRGB());
						lblLinecolor_show.setBackground(lineColor);
						lblFillcolor_show.setBackground((lineStyle
								.getFillColor() != null) ? ColorManager.getInstance()
								.getColor(lineStyle.getFillColor().getDefault()
										.getRGB()) : null);
						txtLineWidExpression
								.setText((lineStyle.getLineWidth()
										.getSerializable().expression != null) ? lineStyle
										.getLineWidth().getSerializable().expression
										: "");
						txtLineColExpression
								.setText((lineStyle.getLineColor()
										.getSerializable().expression != null) ? lineStyle
										.getLineColor().getSerializable().expression
										: "");
					}
					if (selection.getFirstElement() instanceof PolygonStyle) {
						currentStyle = (PolygonStyle) selection
								.getFirstElement();
						polyStyle = (PolygonStyle) hashStyles.get(currentStyle
								.hashCode());
						txtLinewidth.setText(polyStyle.getLineWidth()
								.getDefault().toString());
						lblLinecolor_show.setBackground(ColorManager.getInstance()
								.getColor(polyStyle.getLineColor().getDefault()
										.getRGB()));
						lblFillcolor_show.setBackground((polyStyle
								.getFillColor() != null) ? ColorManager.getInstance()
								.getColor(polyStyle.getFillColor().getDefault()
										.getRGB()) : null);
						txtLineWidExpression
								.setText((polyStyle.getLineWidth()
										.getSerializable().expression != null) ? polyStyle
										.getLineWidth().getSerializable().expression
										: "");
						txtLineColExpression
								.setText((polyStyle.getLineColor()
										.getSerializable().expression != null) ? polyStyle
										.getLineColor().getSerializable().expression
										: "");
						txtFillColExpression
								.setText((polyStyle.getFillColor()
										.getSerializable().expression != null) ? polyStyle
										.getFillColor().getSerializable().expression
										: "");
					}
					if (selection.getFirstElement() instanceof PropertiesCategory) {
						currentStyle = null;
						btnSquare.setSelection(false);
						btnCircle.setSelection(false);
						btnTriangle.setSelection(false);
						btnStar.setSelection(false);
						btnExternalsvgFile.setSelection(false);
						btnDefaultValue.setSelection(false);
						btnExpression.setSelection(false);
					}
				}
				lblLinewidth.setEnabled(currentStyle != null);
				txtLinewidth.setEnabled(currentStyle != null);
				lblLinecolor.setEnabled(currentStyle != null);
				lblLinecolor_show.setEnabled(currentStyle != null);
				lblFillcolor.setEnabled(currentStyle != null);
				lblFillcolor_show.setEnabled(currentStyle != null);
				btnChangeLine.setEnabled(currentStyle != null);
				btnChangeFill.setEnabled(currentStyle != null);
				txtFillColExpression.setEnabled(currentStyle != null);
				txtLineColExpression.setEnabled(currentStyle != null);
				txtLineWidExpression.setEnabled(currentStyle != null);
				lblFillColExpression.setEnabled(currentStyle != null);
				lblLineColExpression.setEnabled(currentStyle != null);
				lblLineWidExpression.setEnabled(currentStyle != null);
				btnSquare.setEnabled(currentStyle instanceof PointStyle);
				btnCircle.setEnabled(currentStyle instanceof PointStyle);
				btnTriangle.setEnabled(currentStyle instanceof PointStyle);
				btnStar.setEnabled(currentStyle instanceof PointStyle);
				btnExternalsvgFile
						.setEnabled(currentStyle instanceof PointStyle);
				btnFile.setEnabled(currentStyle instanceof PointStyle);
				txtFilepath.setEnabled(currentStyle instanceof PointStyle);
				btnDefaultValue.setEnabled(currentStyle instanceof PointStyle);
				btnExpression.setEnabled(currentStyle instanceof PointStyle);
				txtDefaultvalue.setEnabled(currentStyle instanceof PointStyle);
				if (currentStyle == null) {
					txtLinewidth.setText("");
					lblLinecolor_show.setBackground(null);
					lblFillcolor_show.setBackground(null);
					txtFilepath.setText("");
					txtFilepath.setToolTipText("");
					txtDefaultvalue.setText("");
					txtExpressionInput.setText("");
					txtFillColExpression.setText("");
					txtLineColExpression.setText("");
					txtLineWidExpression.setText("");
				}
			}
		});

		Tree tree = treeViewer.getTree();
		tree.setBounds(0, 0, 224, 317);

		Group grpStyle = new Group(container, SWT.NONE);
		grpStyle.setText("Style");
		grpStyle.setBounds(230, 0, 528, 317);

		lblLinewidth = new Label(grpStyle, SWT.NONE);
		lblLinewidth.setBounds(10, 23, 55, 15);
		lblLinewidth.setText("Linewidth:");
		lblLinewidth.setEnabled(currentStyle != null);

		txtLinewidth = new Text(grpStyle, SWT.BORDER);
		txtLinewidth.setBounds(115, 20, 55, 21);
		if (currentStyle == null) {
			txtLinewidth.setText("");
		}
		txtLinewidth.setEnabled(currentStyle != null);

		lblLinecolor = new Label(grpStyle, SWT.NONE);
		lblLinecolor.setBounds(10, 52, 55, 15);
		lblLinecolor.setText("Linecolor:");
		lblLinecolor.setEnabled(currentStyle != null);

		lblLinecolor_show = new Label(grpStyle, SWT.NONE);
		lblLinecolor_show.setBounds(71, 52, 15, 15);
		lblLinecolor_show.setEnabled(currentStyle != null);

		btnChangeLine = new Button(grpStyle, SWT.NONE);
		btnChangeLine.setBounds(115, 47, 55, 25);
		btnChangeLine.setText("Change");
		btnChangeLine.setEnabled(currentStyle != null);
		btnChangeLine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog = new ColorDialog(Display.getCurrent()
						.getActiveShell());
				RGB color = lineStyle.getLineColor().getDefault().getRGB();
				colorDialog.setRGB(color);
				colorDialog.setText("LineColor");
				RGB selectedColor = colorDialog.open();
				if (selectedColor != null) {
					lblLinecolor_show.setBackground(ColorManager.getInstance()
							.getColor(selectedColor));
				}
			};
		});

		lblFillcolor = new Label(grpStyle, SWT.NONE);
		lblFillcolor.setText("Fillcolor:");
		lblFillcolor.setBounds(10, 83, 55, 15);
		lblFillcolor.setEnabled(currentStyle != null);

		lblFillcolor_show = new Label(grpStyle, SWT.NONE);
		lblFillcolor_show.setBounds(71, 83, 15, 15);
		lblFillcolor_show.setEnabled(currentStyle != null);

		btnChangeFill = new Button(grpStyle, SWT.NONE);
		btnChangeFill.setText("Change");
		btnChangeFill.setBounds(115, 78, 55, 25);
		btnChangeFill.setEnabled(currentStyle != null);
		btnChangeFill.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog = new ColorDialog(Display.getCurrent()
						.getActiveShell());
				RGB color = pointStyle.getLineColor().getDefault().getRGB();
				colorDialog.setRGB(color);
				colorDialog.setText("LineColor");
				RGB selectedColor = colorDialog.open();
				if (selectedColor != null) {
					lblFillcolor_show.setBackground(ColorManager.getInstance()
							.getColor(selectedColor));
				}
			};
		});

		Group grpShape = new Group(grpStyle, SWT.NONE);
		grpShape.setText("Shape (only Points)");
		grpShape.setBounds(10, 120, 247, 187);

		btnSquare = new Button(grpShape, SWT.RADIO);
		btnSquare.setBounds(10, 25, 56, 16);
		btnSquare.setText("Square");
		btnSquare.setEnabled(currentStyle != null);
		btnSquare.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtFilepath.setEnabled(false);
				btnFile.setEnabled(false);
			}
		});

		btnCircle = new Button(grpShape, SWT.RADIO);
		btnCircle.setBounds(10, 47, 56, 16);
		btnCircle.setText("Circle");
		btnCircle.setEnabled(currentStyle != null);
		btnCircle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtFilepath.setEnabled(false);
				btnFile.setEnabled(false);
			}
		});

		btnTriangle = new Button(grpShape, SWT.RADIO);
		btnTriangle.setBounds(72, 26, 64, 16);
		btnTriangle.setText("Triangle");
		btnTriangle.setEnabled(currentStyle != null);
		btnTriangle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtFilepath.setEnabled(false);
				btnFile.setEnabled(false);
			}
		});

		btnStar = new Button(grpShape, SWT.RADIO);
		btnStar.setBounds(72, 47, 64, 16);
		btnStar.setText("Star");
		btnStar.setEnabled(currentStyle != null);
		btnStar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtFilepath.setEnabled(false);
				btnFile.setEnabled(false);
			}
		});

		btnExternalsvgFile = new Button(grpShape, SWT.RADIO);
		btnExternalsvgFile.setBounds(10, 69, 126, 16);
		btnExternalsvgFile.setText("External .svg file");
		btnExternalsvgFile.setEnabled(currentStyle != null);
		btnExternalsvgFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtFilepath.setEnabled(true);
				btnFile.setEnabled(true);
			}
		});

		btnFile = new Button(grpShape, SWT.NONE);
		btnFile.setBounds(10, 152, 48, 25);
		btnFile.setText("File");
		btnFile.setEnabled(currentStyle != null);
		btnFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setText("Select File");
				fileDialog.setFilterExtensions(new String[] { "*.svg" });
				fileDialog
						.setFilterNames(new String[] { "Scalable Vector Graphics(*.svg)" });
				String filename = fileDialog.open();
				if (filename != null) {
					File file = new File(filename);

					try {
						txtFilepath.setText(file.toURI().toURL().toString());
						txtFilepath.setToolTipText(file.toURI().toURL()
								.toString());
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		txtFilepath = new Text(grpShape, SWT.BORDER | SWT.H_SCROLL);
		txtFilepath.setBounds(10, 101, 227, 42);
		txtFilepath.setEnabled(currentStyle != null);

		Group grpShapesize = new Group(grpStyle, SWT.NONE);
		grpShapesize.setText("Shapesize (only Points)");
		grpShapesize.setBounds(263, 120, 255, 156);

		btnDefaultValue = new Button(grpShapesize, SWT.RADIO);
		btnDefaultValue.setBounds(10, 25, 90, 16);
		btnDefaultValue.setText("Default value");
		btnDefaultValue.setEnabled(currentStyle != null);
		btnDefaultValue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtExpressionInput.setEnabled(false);
				txtDefaultvalue.setEnabled(true);
			}
		});

		txtDefaultvalue = new Text(grpShapesize, SWT.BORDER);
		txtDefaultvalue.setBounds(198, 23, 47, 21);
		txtDefaultvalue.setEnabled(currentStyle != null);

		btnExpression = new Button(grpShapesize, SWT.RADIO);
		btnExpression.setBounds(10, 57, 90, 16);
		btnExpression.setText("Expression");
		btnExpression.setEnabled(currentStyle != null);
		btnExpression.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtExpressionInput.setEnabled(true);
				txtDefaultvalue.setEnabled(false);
			}
		});

		txtExpressionInput = new Text(grpShapesize, SWT.MULTI | SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL);
		txtExpressionInput.setBounds(10, 79, 235, 67);
		txtExpressionInput.setEnabled(currentStyle != null);

		txtLineWidExpression = new Text(grpStyle, SWT.BORDER);
		txtLineWidExpression.setBounds(236, 20, 282, 21);
		txtLineWidExpression.setEnabled(currentStyle != null);

		txtLineColExpression = new Text(grpStyle, SWT.BORDER);
		txtLineColExpression.setBounds(236, 51, 282, 21);
		txtLineColExpression.setEnabled(currentStyle != null);

		txtFillColExpression = new Text(grpStyle, SWT.BORDER);
		txtFillColExpression.setBounds(236, 83, 282, 21);
		txtFillColExpression.setEnabled(currentStyle != null);

		lblLineWidExpression = new Label(grpStyle, SWT.NONE);
		lblLineWidExpression.setBounds(176, 23, 61, 15);
		lblLineWidExpression.setText("Expression:");
		lblLineWidExpression.setEnabled(currentStyle != null);

		lblLineColExpression = new Label(grpStyle, SWT.NONE);
		lblLineColExpression.setText("Expression:");
		lblLineColExpression.setBounds(176, 52, 61, 15);
		lblLineColExpression.setEnabled(currentStyle != null);

		lblFillColExpression = new Label(grpStyle, SWT.NONE);
		lblFillColExpression.setText("Expression:");
		lblFillColExpression.setBounds(176, 83, 61, 15);
		lblFillColExpression.setEnabled(currentStyle != null);

		lblSaved = new Label(grpStyle, SWT.NONE);
		lblSaved.setBounds(376, 287, 61, 15);

		Button btnSave = new Button(grpStyle, SWT.NONE);
		btnSave.setBounds(443, 282, 75, 25);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isValidInput()) {
					if (currentStyle instanceof PointStyle) {
						hashStyles.remove(currentStyle.hashCode());
						ShapeCondition shape = null;
						// TODO: Handle shape Expression
						shape = new ShapeCondition(
								new PersistentCondition(((btnExternalsvgFile
										.getSelection()) ? txtFilepath
										.getText() : DEFAULT), ""));
						IntegerCondition intConLineWid = new IntegerCondition(
								Integer.parseInt(txtLinewidth.getText()));
						if (!txtLineWidExpression.getText().isEmpty()) {
							intConLineWid = new IntegerCondition(
									new PersistentCondition(
											txtLineWidExpression.getText()));
						}

						ColorCondition colConLineCol = new ColorCondition(
								lblLinecolor_show.getBackground());
						if (!txtLineColExpression.getText().isEmpty()) {
							colConLineCol = new ColorCondition(
									new PersistentCondition(
											txtLineColExpression.getText()));
						}
						ColorCondition colConFillCol = new ColorCondition(
								lblFillcolor_show.getBackground());
						if (!txtFillColExpression.getText().isEmpty()) {
							colConFillCol = new ColorCondition(
									new PersistentCondition(
											txtFillColExpression.getText()));
						}

						Style newStyle = new PointStyle(shape,
								new IntegerCondition(new PersistentCondition(
										Integer.parseInt(txtDefaultvalue
												.getText()), txtExpressionInput
												.getText())), intConLineWid,
								colConLineCol, colConFillCol);

						hashStyles.put(currentStyle.hashCode(), newStyle);
						pointStyle = null;
						lblSaved.setText("Style saved!");
					}
					if (currentStyle instanceof LineStyle) {
						hashStyles.remove(currentStyle.hashCode());
						IntegerCondition intConLineWid = new IntegerCondition(
								Integer.parseInt(txtLinewidth.getText()));
						if (!txtLineWidExpression.getText().isEmpty()) {
							intConLineWid = new IntegerCondition(
									new PersistentCondition(
											txtLineWidExpression.getText()));
						}
						ColorCondition colConLineCol = new ColorCondition(
								lblLinecolor_show.getBackground());
						if (!txtLineColExpression.getText().isEmpty()) {
							colConLineCol = new ColorCondition(
									new PersistentCondition(
											txtLineColExpression.getText()));
						}

						Style newStyle = new LineStyle(intConLineWid,
								colConLineCol);
						hashStyles.put(currentStyle.hashCode(), newStyle);
						lineStyle = null;
						lblSaved.setText("Style saved!");
					}
					if (currentStyle instanceof PolygonStyle) {
						hashStyles.remove(currentStyle.hashCode());

						IntegerCondition intConLineWid = new IntegerCondition(
								Integer.parseInt(txtLinewidth.getText()));
						if (!txtLineWidExpression.getText().isEmpty()) {
							intConLineWid = new IntegerCondition(
									new PersistentCondition(
											txtLineWidExpression.getText()));
						}

						ColorCondition colConLineCol = new ColorCondition(
								lblLinecolor_show.getBackground());
						if (!txtLineColExpression.getText().isEmpty()) {
							colConLineCol = new ColorCondition(
									new PersistentCondition(
											txtLineColExpression.getText()));
						}
						ColorCondition colConFillCol = new ColorCondition(
								lblFillcolor_show.getBackground());
						if (!txtFillColExpression.getText().isEmpty()) {
							colConFillCol = new ColorCondition(
									new PersistentCondition(
											txtFillColExpression.getText()));
						}

						Style newStyle = new PolygonStyle(intConLineWid,
								colConLineCol, colConFillCol);
						hashStyles.put(currentStyle.hashCode(), newStyle);
						polyStyle = null;
						lblSaved.setText("Style saved!");
					}
					setErrorMessage(null);
				}
			}
		});

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createOkButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Iterator<Entry<Integer, Style>> it = hashStyles.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<Integer, Style> pairs = it.next();
					collStyle.addStyle(pairs.getValue());
					it.remove();

					layer.setStyle(collStyle);
					LOG.debug(layer.getName() + " Style changed");
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;

		if (txtLinewidth.getText().isEmpty()
				|| txtLinewidth.getText().contains(" ")) {
			super.setErrorMessage("Please enter a valid number for linewidth: No whitespaces");
			valid = false;
		}

		if (currentStyle instanceof PointStyle) {
			if (txtDefaultvalue.getText().isEmpty()
					|| txtDefaultvalue.getText().contains(" ")) {
				super.setErrorMessage("Please enter a valid number for default value: No whitespaces");
				valid = false;
			}
		}

		if (txtFilepath.getText().isEmpty()
				&& btnExternalsvgFile.getSelection()) {
			super.setErrorMessage("Please enter a valid filepath");
			valid = false;
		}

		try {
			Integer.parseInt(txtLinewidth.getText());
			if (currentStyle instanceof PointStyle) {
				Integer.parseInt(txtDefaultvalue.getText());
			}
		} catch (NumberFormatException e) {
			super.setErrorMessage("Please enter only valid numbers");
			valid = false;
		}
		return valid;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(784, 522);
	}
}
