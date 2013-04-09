package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.VectorLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style.PersistentStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public class StyleComposite extends Composite {

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public StyleComposite(Composite parent, int style, final LayerConfiguration config) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		Label lblStyle = new Label(this, SWT.NONE);
		lblStyle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStyle.setText("Style:");

		final TreeViewer treeViewer = new TreeViewer(this, SWT.BORDER);
		setStyleContent(treeViewer);
		if (config instanceof VectorLayerConfiguration) {
			((VectorLayerConfiguration) config).setStyle(new PersistentStyle());
			treeViewer.setInput(config);
		}
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Menu menu = new Menu(tree);
		tree.setMenu(menu);

		MenuItem mntmNewStyle = new MenuItem(menu, SWT.NONE);
		mntmNewStyle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				if (selection.getFirstElement() == null) {
					if (config instanceof VectorLayerConfiguration) {
						((VectorLayerConfiguration) config).setStyle(new PersistentStyle());
						treeViewer.setInput(config);
					}
				} else {
					((StyleElement) selection.getFirstElement()).addChild(new PersistentStyle());
				}
				// treeViewer.getC
			}
		});
		mntmNewStyle.setText("New Style");

	}

	private int addTypeColumn(final TreeViewer treeViewer, final int numColumn) {
		final TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnType = treeViewerColumn.getColumn();
		trclmnType.setWidth(150);
		trclmnType.setText("Type");
		treeViewerColumn.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				if (cell.getColumnIndex() == numColumn)
					cell.setText("Test");

			}
		});
		treeViewerColumn.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				String type = types[(Integer) value];
				((StyleElement) element).self.setType(type);
				if (type.equals("Point"))
					// ((StyleElement)
					// element).self.setShape(SHAPE.CIRCLE.name());
					treeViewer.refresh(element);
			}

			@Override
			protected Object getValue(Object element) {
				String type = ((StyleElement) element).self.getType();
				for (int i = 0; i < types.length; i++) {
					if (types[i].equals(type))
						return i;
				}
				return 0;
			}

			@Override
			protected org.eclipse.jface.viewers.CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor editor = new ComboBoxCellEditor(treeViewer.getTree(), types);
				// editor.setValue(((PersistentStyle)element).getType());
				return editor;
				// TextCellEditor e = new
				// TextCellEditor(((TreeViewer)getViewer()).getTree());
				// return e;
			}

			@Override
			protected boolean canEdit(Object element) {
				return (element instanceof StyleElement);
			}
		});
		return numColumn;
	}

	private int addFillColorColumn(final TreeViewer treeViewer, final int numColumn) {
		final TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnType = treeViewerColumn.getColumn();
		trclmnType.setWidth(20);
		trclmnType.setText("FillColor");
		treeViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() instanceof Style) {
					Style style = (Style) cell.getElement();
					@SuppressWarnings("unused")
					int i = cell.getColumnIndex();
					if (cell.getColumnIndex() == numColumn)
						cell.setBackground(style.getFillColor().getDefault());
					super.update(cell);
				}
			}
		});
		treeViewerColumn.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				((StyleElement) element).self.setFillColor((RGB) value, null);
				treeViewer.refresh(element);
			}

			@Override
			protected Object getValue(Object element) {
				RGB item = ((StyleElement) element).self.getFillColor().getDefault().getRGB();
				return item;
			}

			@Override
			protected org.eclipse.jface.viewers.CellEditor getCellEditor(Object element) {
				ColorCellEditor editor = new ColorCellEditor(treeViewer.getTree());
				editor.setValue(getValue(element));
				// editor.setValue(((PersistentStyle)element).getType());
				return editor;
				// TextCellEditor e = new
				// TextCellEditor(((TreeViewer)getViewer()).getTree());
				// return e;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (((StyleElement) element).self.getType() != null) {
					if (((StyleElement) element).self.getType().equals("Collection"))
						return false;
					return true;
				} 
				return false;
			}
		});
		return numColumn;

	}

	private int addLineColorColumn(final TreeViewer treeViewer, final int numColumn) {
		final TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnType = treeViewerColumn.getColumn();
		trclmnType.setWidth(20);
		trclmnType.setText("LineColor");
		treeViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() instanceof Style) {
					Style style = (Style) cell.getElement();
					@SuppressWarnings("unused")
					int i = cell.getColumnIndex();
					if (cell.getColumnIndex() == numColumn)
						cell.setBackground(style.getLineColor().getDefault());
					super.update(cell);
				}
			}
		});
		treeViewerColumn.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				((StyleElement) element).self.setLineColor((RGB) value, null);
				treeViewer.refresh(element);
			}

			@Override
			protected Object getValue(Object element) {
				RGB item = ((StyleElement) element).self.getLineColor().getDefault().getRGB();
				return item;
			}

			@Override
			protected org.eclipse.jface.viewers.CellEditor getCellEditor(Object element) {
				ColorCellEditor editor = new ColorCellEditor(treeViewer.getTree());
				editor.setValue(getValue(element));
				// editor.setValue(((PersistentStyle)element).getType());
				return editor;
				// TextCellEditor e = new
				// TextCellEditor(((TreeViewer)getViewer()).getTree());
				// return e;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (((StyleElement) element).self.getType() != null) {
					if (((StyleElement) element).self.getType().equals("Collection"))
						return false;
					return true;
				}
				
				return false;
			}
		});
		return numColumn;

	}

	private int addShapeColumn(final TreeViewer treeViewer, final int numColumn) {
		final TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnType = treeViewerColumn.getColumn();
		trclmnType.setWidth(150);
		trclmnType.setText("Shape");
		treeViewerColumn.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				if (cell.getColumnIndex() == numColumn)
					cell.setText("Test");

			}
		});
		treeViewerColumn.setEditingSupport(new EditingSupport(treeViewer) {
			@SuppressWarnings("unused")
			String[] shapes = null;
			{
				// SHAPE[] shape = SHAPE.values();
				// shapes = new String[shape.length];
				// for (int i = 0; i < shape.length; i++) {
				// shapes[i] = shape[i].name();
				// }
			}

			@Override
			protected void setValue(Object element, Object value) {
				// ((StyleElement) element).self.setShape(shapes[(Integer)
				// value]);
				treeViewer.refresh(element);
			}

			@Override
			protected Object getValue(Object element) {
				// SHAPE shape = ((StyleElement) element).self.getShape();
				// if (shape != null)
				// return shape.ordinal();
				return 0;
			}

			@Override
			protected org.eclipse.jface.viewers.CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor editor = new ComboBoxCellEditor(treeViewer.getTree(), types);
				// editor.setValue(((PersistentStyle)element).getType());
				return editor;
				// TextCellEditor e = new
				// TextCellEditor(((TreeViewer)getViewer()).getTree());
				// return e;
			}

			@Override
			protected boolean canEdit(Object element) {
				return false;
//				if (((StyleElement) element).self.getType() != null) {
//					if (((StyleElement) element).self.getType().equals("Collection"))
//						return false;
//					return true;
//				} else
//					return false;

			}
		});
		return numColumn;

	}

	private int addShapeSizeColumn(final TreeViewer treeViewer, final int numColumn) {
		final TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnType = treeViewerColumn.getColumn();
		trclmnType.setWidth(150);
		trclmnType.setText("Size");
		treeViewerColumn.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				if (cell.getColumnIndex() == numColumn)
					cell.setText("Test");

			}
		});
		treeViewerColumn.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				((StyleElement) element).self.setShapeSize(Integer.parseInt(((String) value)), null);
				treeViewer.refresh(element);
			}

			@Override
			protected Object getValue(Object element) {
				return ((StyleElement) element).self.getShapeSize();
			}

			@Override
			protected org.eclipse.jface.viewers.CellEditor getCellEditor(Object element) {
				TextCellEditor e = new TextCellEditor(((TreeViewer) getViewer()).getTree());
				// e.setValue((String) getValue());
				return e;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (((StyleElement) element).self.getType() != null) {
					if (((StyleElement) element).self.getType().equals("Collection"))
						return false;
					return true;
				} 
				
				return false;
			}
		});
		return numColumn;

	}

	private int addLineWidthColumn(final TreeViewer treeViewer, final int numColumn) {
		final TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnType = treeViewerColumn.getColumn();
		trclmnType.setWidth(150);
		trclmnType.setText("LineWidth");
		treeViewerColumn.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				if (cell.getColumnIndex() == numColumn)
					cell.setText("Test");

			}
		});
		treeViewerColumn.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				((StyleElement) element).self.setLineWidth(Integer.parseInt(("0" + (String) value)), null);
				treeViewer.refresh(element);
			}

			@Override
			protected Object getValue(Object element) {
				return ((StyleElement) element).self.getLineWidth();
			}

			@Override
			protected org.eclipse.jface.viewers.CellEditor getCellEditor(Object element) {
				TextCellEditor e = new TextCellEditor(((TreeViewer) getViewer()).getTree());
				return e;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (((StyleElement) element).self.getType() != null) {
					if (((StyleElement) element).self.getType().equals("Collection"))
						return false;
					return true;
				}
				
				return false;
			}
		});
		return numColumn;
	}

	private void setStyleContent(final TreeViewer treeViewer) {
		int numColumn = 0;
		final int typeColumn = addTypeColumn(treeViewer, numColumn++);
		final int shapeColumn = addLineColorColumn(treeViewer, numColumn++);
		final int shapeSizeColumn = addFillColorColumn(treeViewer, numColumn++);
		final int fillColorColumn = addLineWidthColumn(treeViewer, numColumn++);
		final int lineColorColumn = addShapeColumn(treeViewer, numColumn++);
		final int lineWidthColumn = addShapeSizeColumn(treeViewer, numColumn++);

		treeViewer.setContentProvider(new ITreeContentProvider() {
			@SuppressWarnings("unused")
			LayerConfiguration input;
			StyleElement root;

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				this.input = (LayerConfiguration) newInput;
				if (newInput instanceof VectorLayerConfiguration)
					this.root = new StyleElement(((VectorLayerConfiguration) newInput).getStyle());

			}

			@Override
			public Object[] getElements(Object inputElement) {
				return this.getChildren(inputElement);
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof VectorLayerConfiguration)
					return new Object[] { this.root };
				return ((StyleElement) parentElement).children.toArray();
			}

			@Override
			public Object getParent(Object element) {
				return ((StyleElement) element).parent;
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof VectorLayerConfiguration)
					return root.children.isEmpty();
				return !((StyleElement) element).children.isEmpty();
			}

		});
		treeViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				StyleElement style = ((StyleElement) element);
				if (columnIndex == fillColorColumn)
					return style.getFillColorImage();
				else if (columnIndex == lineColorColumn)
					return style.getLineColorImage();

				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				PersistentStyle style = ((StyleElement) element).self;

				if (style.getType() == null)
					return (columnIndex == typeColumn ? "none" : "");
				else if (style.getType().equals("Collection"))
					return (columnIndex == typeColumn ? "Collection" : "");
				else if (style.getType().equals("Point")) {
					if (columnIndex == shapeColumn) {
						return style.getShape().getDefault().toString();
					} else if (columnIndex == shapeSizeColumn) {
						return Integer.toString(style.getLineWidth().getDefault());
					}
				} else if (style.getType().equals("Line")) {
					if (columnIndex == fillColorColumn)
						return "";
				}
				if (columnIndex == typeColumn) {
					return style.getType();
				} else if (columnIndex == fillColorColumn) {
					RGB rgb = style.getFillColor().getDefault().getRGB();
					return (rgb != null ? "(" + rgb.red + "," + rgb.green + "," + rgb.blue + ")" : "");
				} else if (columnIndex == lineColorColumn) {
					RGB rgb = style.getFillColor().getDefault().getRGB();
					return (rgb != null ? "(" + rgb.red + "," + rgb.green + "," + rgb.blue + ")" : "");
				} else if (columnIndex == lineWidthColumn) {
					return Integer.toString(style.getLineWidth().getDefault());
				}
				return "";
			}

		});

		// CellEditor[] editors = {(CellEditor) new TextCellEditor(this), null};
		//
		// treeViewerColumn.setCellEditors(editors);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	static String[] types = { "Collection", "Point", "Line", "Polygon" };

	class StyleElement {

		StyleElement parent;
		PersistentStyle self;
		ArrayList<StyleElement> children;

		public StyleElement(PersistentStyle style) {
			this(style, null);

		}

		public StyleElement(PersistentStyle style, StyleElement parent) {
			this.parent = parent;
			this.self = style;
			LinkedList<PersistentStyle> styles = style.getSubstyles();
			this.children = new ArrayList<StyleComposite.StyleElement>(0);
			if (styles != null)
				for (PersistentStyle c : styles) {
					this.children.add(new StyleElement(c, this));
				}
		}

		public void addChild(PersistentStyle persistentStyle) {
			this.children.add(new StyleElement(persistentStyle, this));

		}

		private Image fillColor;
		private Image lineColor;

		public Image getFillColorImage() {
			if (this.self.getFillColor() == null)
				return null;
			if (fillColor != null)
				fillColor.dispose();
			fillColor = new Image(Display.getCurrent(), 16, 16);
			GC gc = new GC(fillColor);
			gc.setBackground(this.self.getFillColor().getDefault());
			gc.fillRectangle(0, 0, 15, 15);
			gc.setForeground(ColorManager.getInstance().getColor(new RGB(0, 0, 0)));
			gc.drawRectangle(0, 0, 15, 15);
			gc.dispose();
			return fillColor;
		}

		public Image getLineColorImage() {
			if (this.self.getLineColor() == null)
				return null;
			if (lineColor != null)
				lineColor.dispose();
			lineColor = new Image(Display.getCurrent(), 16, 16);
			GC gc = new GC(lineColor);
			gc.setBackground(this.self.getLineColor().getDefault());
			gc.fillRectangle(0, 0, 15, 15);
			gc.setForeground(ColorManager.getInstance().getColor(new RGB(0, 0, 0)));
			gc.drawRectangle(0, 0, 15, 15);
			gc.dispose();
			return lineColor;
		}

	}
}
