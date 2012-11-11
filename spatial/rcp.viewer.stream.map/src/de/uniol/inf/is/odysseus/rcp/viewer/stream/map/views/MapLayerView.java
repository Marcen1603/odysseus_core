package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.PropertyTitleDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorOutlineLabelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorOutlineTreeContentProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth.ChoroplethLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth.EditChoroplethStyleDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.diagram.DiagramLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.diagram.EditDiagramStyleSettings;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location.EditLocationStyleDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location.LocationLayer;

public class MapLayerView extends AbstractStreamMapEditorViewPart {

	private static final Logger LOG = LoggerFactory.getLogger(MapLayerView.class);
	private TreeViewer treeViewer;
	private Action addItemAction, deleteItemAction, selectAllAction,
	        editItemAction, upAction, downAction;

	@Override
	public void setFocus() {

	}

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		createActions();
	}

	@Override
	protected void updatePartControl(Composite parent) {
		// LOG.debug("Update Layer");

		treeViewer = new TreeViewer(container);
		treeViewer.setContentProvider(new StreamMapEditorOutlineTreeContentProvider());
		treeViewer.setLabelProvider(new StreamMapEditorOutlineLabelProvider());
		if (hasMapEditorModel()) {
			treeViewer.setInput(getMapEditorModel().getLayers().toArray());
		}
		createContextMenu();
		treeViewer.refresh();
	}

	@Override
	protected void createMenu() {
		IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
		mgr.add(selectAllAction);
	}

	@Override
	protected void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(addItemAction);
		mgr.add(deleteItemAction);
		mgr.add(editItemAction);
		mgr.add(upAction);
		mgr.add(downAction);
	}

	@Override
	protected void createActions() {
		editItemAction = new Action("Edit") {
			public void run() {
				LOG.debug("Edit: " + treeViewer.getSelection());
			}
		};

		addItemAction = new Action("Add") {
			public void run() {

				if (hasMapEditor()) {
					Shell shell = editor.getScreenManager().getDisplay().getActiveShell();
					PropertyTitleDialog dialog = new PropertyTitleDialog(shell, model.getLayers(), model.getConnectionCollection());
					dialog.create();
					dialog.open();

					if (dialog.getReturnCode() == MessageDialog.OK) {
						editor.addLayer(dialog.getLayerConfiguration());
					} else {
						MessageDialog.openInformation(shell, "Information", "No layer added to the map.");
					}

				}
			}
		};

		deleteItemAction = new Action("Remove") {
			public void run() {
				TreeSelection selection = ((TreeSelection) treeViewer.getSelection());
				if (hasMapEditor()) {
					if (selection.getFirstElement() instanceof ILayer) {
						Shell shell = editor.getScreenManager().getDisplay().getActiveShell();
						ILayer layer = (ILayer) selection.getFirstElement();

						boolean feedback = MessageDialog.openQuestion(shell, "Remove Layer", "Would you really remove " + layer.getName());
						if (feedback) {
							editor.removeLayer(layer);
						}
					} else {

					}
				}
			}
		};

		selectAllAction = new Action("Select All") {
			public void run() {
				LOG.debug("Delete..");
			}
		};


		upAction = new Action("Up") {
			public void run() {
				if (hasMapEditor()) {
						ILayer layer = (ILayer) ((TreeSelection)treeViewer.getSelection()).getFirstElement();
						editor.layerUp(layer);
				}
			}
		};

		downAction = new Action("Down") {
			public void run() {
				if (hasMapEditor()) {
					if (hasMapEditor()) {
						ILayer layer = (ILayer) ((TreeSelection)treeViewer.getSelection()).getFirstElement();
						editor.layerDown(layer);
				}
				}
			}
		};

	}

	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu("", menuMgr, new ISelectionProvider() {

			@Override
			public void setSelection(ISelection selection) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeSelectionChangedListener(ISelectionChangedListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public ISelection getSelection() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addSelectionChangedListener(ISelectionChangedListener listener) {
				LOG.debug("Select: " + listener.toString());
			}
		});
	}

	int counter = 0;

	public String[] TILESERVERS = {
	        // new String("http://tah.openstreetmap.org/Tiles/tile/"),
	new String("http://oatile2.mqcdn.com/tiles/1.0.0/sat/"), new String("http://otile2.mqcdn.com/tiles/1.0.0/osm/"), new String("http://tile.opencyclemap.org/cycle/"), new String("http://tile2.opencyclemap.org/transport/"), new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/1/256/"), new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/2/256/"), new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/3/256/"), new String("http://otile1.mqcdn.com/tiles/1.0.0/osm/"), new String("http://oatile1.mqcdn.com/naip/"), new String("http://oatile1.mqcdn.com/naip/"), new String("http://tile.openstreetmap.org/") };

	private void fillContextMenu(final IMenuManager mgr) {
		final ITreeSelection i = (ITreeSelection) treeViewer.getSelection();

//		mgr.add(new Action("Change Server") {
//			public void run() {
//				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
//				if (i.getFirstElement() instanceof RasterLayer) {
//					RasterLayer mapLayer = (RasterLayer) i.getFirstElement();
//					if (TILESERVERS.length == counter) {
//						counter = 0;
//					}
//					mapLayer.setTileServer(TILESERVERS[counter++]);
//				}
//				treeViewer.refresh(true);
//				editor.getScreenManager().getCanvas().redraw();
//			}
//		});

//		mgr.add(new Separator());
		
		if(i.getFirstElement() instanceof ChoroplethLayer){
			mgr.add(new Action("Edit Settings") {
				public void run(){
					ChoroplethLayer layer = (ChoroplethLayer)i.getFirstElement();
					EditChoroplethStyleDialog dialog = new EditChoroplethStyleDialog(Display.getCurrent().getActiveShell(), layer);
					dialog.open();
				}
			});
		}else if(i.getFirstElement() instanceof LocationLayer){
			mgr.add(new Action("Edit Settings") {
				public void run(){
					LocationLayer layer = (LocationLayer)i.getFirstElement();
					EditLocationStyleDialog dialog = new EditLocationStyleDialog(Display.getCurrent().getActiveShell(), layer);
					dialog.open();
				}
			});
		}else if(i.getFirstElement() instanceof DiagramLayer){
			mgr.add(new Action("Edit Settings") {
				public void run(){
					DiagramLayer layer = (DiagramLayer)i.getFirstElement();
					EditDiagramStyleSettings dialog = new EditDiagramStyleSettings(Display.getCurrent().getActiveShell(), layer);
					dialog.open();
				}
			});
		}else{
			mgr.add(new Action("Linecolor") {
				public void run() {
					ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
					if (i.getFirstElement() instanceof CollectionStyle)
						return;
					if (i.getFirstElement() instanceof Style) {
						Style element = (Style) i.getFirstElement();
						ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
						RGB color = element.getLineColor().getRGB();
						colorDialog.setRGB(color);
						colorDialog.setText("ColorDialog");
						RGB selectedColor = colorDialog.open();
						if (!selectedColor.equals(color))
							element.setLineColor(ColorManager.getInstance().getColor(selectedColor));
						treeViewer.refresh(element, true);
					}

				}
			});

			mgr.add(new Action("Fillcolor") {
				public void run() {
					ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
					if (i.getFirstElement() instanceof Style) {
						Style element = (Style) i.getFirstElement();
						ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
						RGB color = element.getFillColor().getRGB();
						colorDialog.setRGB(color);
						colorDialog.setText("ColorDialog");
						RGB selectedColor = colorDialog.open();
						if (!selectedColor.equals(color))
							element.setFillColor(ColorManager.getInstance().getColor(selectedColor));
						treeViewer.refresh(element, true);
					}
				}
			});
		}
		
		
	}
	
}
