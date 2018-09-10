package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.PropertyTitleDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties.MapPropertiesDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties.StylePropertiesDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.GroupLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.GroupLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public class MapLayerView extends AbstractStreamMapEditorViewPart {

	private static final Logger LOG = LoggerFactory.getLogger(MapLayerView.class);
	private TreeViewer treeViewer;
	private Action addItemAction, deleteItemAction, selectAllAction, editItemAction, upAction, downAction;

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

		treeViewer = new CheckboxTreeViewer(container);
		treeViewer.setContentProvider(new MapLayerViewTreeContentProvider());
		treeViewer.setLabelProvider(new MapLayerViewLabelProvider());
		((CheckboxTreeViewer)treeViewer).addCheckStateListener(new ICheckStateListener() {
		      @Override
			public void checkStateChanged(CheckStateChangedEvent event) {
	        	  if (event.getElement() instanceof ILayer){
//	        		  ((ILayer) event.getElement()).setActive(event.getChecked());
	        	      ((CheckboxTreeViewer)treeViewer).setSubtreeChecked(event.getElement(), event.getChecked());
	        	      getMapEditor().setActive(((ILayer) event.getElement()), event.getChecked());
	        	  }
		        }
		      });
		
		((CheckboxTreeViewer)treeViewer).setCheckStateProvider(new ICheckStateProvider() {
			
			@Override
			public boolean isGrayed(Object element) {
				if (element instanceof Style)
					return true;
				return false;
			}
			
			@Override
			public boolean isChecked(Object element) {
				if (element instanceof ILayer)
					return ((ILayer)element).isActive();
				return false;
			}
		});
		
		if (hasMapEditorModel() && getMapEditorModel() != null) {
			treeViewer.setInput(getMapEditorModel());
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
			@Override
			public void run() {
				LOG.debug("Edit: " + treeViewer.getSelection());
			}
		};

		addItemAction = new Action("Add") {
			@Override
			public void run() {

				if (hasMapEditor()) {
					Shell shell = editor.getScreenManager().getDisplay().getActiveShell();
//					NewLayerDialog nld = new NewLayerDialog(shell, model, null);
//					nld.create();
//					nld.open();
					PropertyTitleDialog dialog = new PropertyTitleDialog(shell, model.getLayers(), model.getConnectionCollection());
					dialog.create();
					dialog.open();
					if (dialog.getReturnCode() == Window.OK) {
						editor.addLayer(dialog.getLayerConfiguration());
					} else {
						// I guess user knows that this happend if he clicked "cancel"
//						MessageDialog.openInformation(shell, "Information", "No layer added to the map.");
					}

				}
			}
		};

		deleteItemAction = new Action("Remove") {
			@Override
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
			@Override
			public void run() {
				LOG.debug("Delete..");
			}
		};

		upAction = new Action("Up") {
			@Override
			public void run() {
				if (hasMapEditor()) {
					ILayer layer = (ILayer) ((TreeSelection) treeViewer.getSelection()).getFirstElement();
					editor.layerUp(layer);
				}
			}
		};

		downAction = new Action("Down") {
			@Override
			public void run() {
				if (hasMapEditor()) {
					if (hasMapEditor()) {
						ILayer layer = (ILayer) ((TreeSelection) treeViewer.getSelection()).getFirstElement();
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
			@Override
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
			new String("http://oatile2.mqcdn.com/tiles/1.0.0/sat/"), new String("http://otile2.mqcdn.com/tiles/1.0.0/osm/"),
			new String("http://tile.opencyclemap.org/cycle/"), new String("http://tile2.opencyclemap.org/transport/"),
			new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/1/256/"),
			new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/2/256/"),
			new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/3/256/"), new String("http://otile1.mqcdn.com/tiles/1.0.0/osm/"),
			new String("http://oatile1.mqcdn.com/naip/"), new String("http://oatile1.mqcdn.com/naip/"), new String("http://tile.openstreetmap.org/") };

	private void fillContextMenu(final IMenuManager mgr) {
		ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
		if (i.getFirstElement() instanceof ILayer) {
			mgr.add(new Action("Add To Group") {
				@Override
				public void run() {
					ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
					if (i.getFirstElement() instanceof ILayer) {
						ILayer element = (ILayer) i.getFirstElement();
						getGroupDialog(element);
						treeViewer.refresh(element, true);
					}
	
				}
			});
			mgr.add(new Action("Zoom to Layer") {
				@Override
				public void run() {
					ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
					if (i.getFirstElement() instanceof ILayer) {
						getMapEditor().getScreenManager().zoomToExtend((ILayer)i.getFirstElement());
					}
	
				}
			});
			mgr.add(new Action("Rename") {
				@Override
				public void run() {
					ITreeSelection i = (ITreeSelection) treeViewer
							.getSelection();
					if (i.getFirstElement() instanceof ILayer) {
						ILayer element = (ILayer) i.getFirstElement();
						IInputValidator validator = new IInputValidator() {

							@Override
							public String isValid(String newText) {
								if (newText.equals(""))
									return "Please type a name.";
								if (getMapEditorModel().containsGroup(newText)) {
									return "A layer or group with the name"
											+ newText + " does already exist.";
								}
								return null;
							}
						};
						InputDialog groupname = new InputDialog(Display
								.getCurrent().getActiveShell(), "Set Name",
								"Please enter a name:", element.getName(),
								validator);
						groupname.open();
						if (groupname.getReturnCode() == Window.OK) {
							String name = groupname.getValue();
							getMapEditor().renameLayer(element, name);
						}
						treeViewer.refresh(element, true);
					}

				}
			});
			mgr.add(new Action("Remove Layer") {
				@Override
				public void run() {
					ITreeSelection i = (ITreeSelection) treeViewer
							.getSelection();
					if (hasMapEditor()) {
						if (i.getFirstElement() instanceof ILayer) {
							ILayer element = (ILayer) i.getFirstElement();
							Shell shell = editor.getScreenManager()
									.getDisplay().getActiveShell();

							boolean feedback = MessageDialog.openQuestion(
									shell,
									"Remove Layer",
									"Would you really remove "
											+ element.getName());
							if (feedback) {
								editor.removeLayer(element);
							}
						} else {

						}
					}

				}
			});

			mgr.add(new Separator());

			if (i.getFirstElement() instanceof VectorLayer) {
				VectorLayer vLayer = (VectorLayer) i.getFirstElement();
				if (vLayer.getStyle().hasSubstyles()) {
					mgr.add(new Action("Style properties") {
						@Override
						public void run() {
							ITreeSelection i = (ITreeSelection) treeViewer
									.getSelection();
							if (hasMapEditor()) {
								if (i.getFirstElement() instanceof ILayer) {
									ILayer element = (ILayer) i
											.getFirstElement();
									Shell shell = editor.getScreenManager()
											.getDisplay().getActiveShell();

									StylePropertiesDialog dialog = new StylePropertiesDialog(
											shell,  element);
									dialog.create();
									dialog.open();
									if (dialog.getReturnCode() == Window.OK) {
										editor.setLayerChanged();
										MessageDialog.openInformation(shell,
												"Information",
												"Layer Properties Changed.");
									} else {
									}
								}
							}
						}
					});
				}
			}

			if (i.getFirstElement() instanceof ILayer) {
				mgr.add(new Action("Map properties") {
					@Override
					public void run() {
						ITreeSelection i = (ITreeSelection) treeViewer
								.getSelection();
						if (hasMapEditor()) {
							MapEditorModel map = getMapEditor()
									.getMapEditorModel();
							Shell shell = editor.getScreenManager()
									.getDisplay().getActiveShell();

							MapPropertiesDialog dialog = new MapPropertiesDialog(
									shell, map, editor);
							dialog.create();
							dialog.selectLayer((ILayer) i.getFirstElement());
							dialog.open();
							
							if (dialog.getReturnCode() == Window.OK) {
								editor.setLayerChanged();
								// It's confusing if this is shown always ...
//								MessageDialog.openInformation(shell,
//										"Information",
//										"Map Properties Changed.");
							} else {
							}
						}
					}
				});
			}

		} else if (i.getFirstElement() instanceof Style) {
			mgr.add(new Action("Linecolor") {
				@Override
				public void run() {
					ITreeSelection i = (ITreeSelection) treeViewer
							.getSelection();
					if (i.getFirstElement() instanceof CollectionStyle)
						return;
					if (i.getFirstElement() instanceof Style) {
						Style element = (Style) i.getFirstElement();
						ColorDialog colorDialog = new ColorDialog(Display
								.getCurrent().getActiveShell());
						RGB color = element.getLineColor().getDefault()
								.getRGB();
						colorDialog.setRGB(color);
						colorDialog.setText("ColorDialog");
						RGB selectedColor = colorDialog.open();
						if (!selectedColor.equals(color))
							element.setDefaultLineColor(ColorManager
									.getInstance().getColor(selectedColor));
						treeViewer.refresh(true);
					}
				}
			});
			if (!(i.getFirstElement() instanceof LineStyle))
				mgr.add(new Action("Fillcolor") {
					@Override
					public void run() {
						ITreeSelection i = (ITreeSelection) treeViewer
								.getSelection();
						if (i.getFirstElement() instanceof Style) {
							Style element = (Style) i.getFirstElement();
							ColorDialog colorDialog = new ColorDialog(Display
									.getCurrent().getActiveShell());
							Color c = element.getFillColor().getDefault();
							RGB color = null;
							if (c != null) {
								color = c.getRGB();
								colorDialog.setRGB(color);
							}
							colorDialog.setText("ColorDialog");
							RGB selectedColor = colorDialog.open();
							if (!selectedColor.equals(color)) {
								element.setDefaultFillColor(ColorManager
										.getInstance().getColor(selectedColor));
								treeViewer.refresh(true);
							}
						}

					}
				});
		}
	}

	public void getGroupDialog(final ILayer element) {
		ILabelProvider labelprovider = new ILabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return ((GroupLayer) element).getName();
			}

			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		ElementListSelectionDialog dlg = new ElementListSelectionDialog(Display.getCurrent().getActiveShell(), labelprovider);
		Collection<GroupLayer> groups = new ArrayList<GroupLayer>();
		GroupLayer empty = new GroupLayer(new GroupLayerConfiguration("New Group ... "));
		groups.add(empty);
		groups.addAll(this.model.getGroups());
		dlg.setElements(groups.toArray());
		dlg.setTitle("Add Layer to Group");
		dlg.setMultipleSelection(false);
		dlg.setFilter("*");
		dlg.open();

		if (dlg.getReturnCode() == Window.OK) {
			Object[] sel = dlg.getResult();
			for (Object object : sel) {
				if (object == empty) {
					IInputValidator validator = new IInputValidator() {

						@Override
						public String isValid(String newText) {
							if (newText.equals(""))
								return "Please type a name.";								
							if (getMapEditorModel().containsGroup(newText)) {
								return "A group with the name" + newText + " does already exist.";
							}
							return null;
						}
					};
					InputDialog groupname = new InputDialog(Display.getCurrent().getActiveShell(), "Set Groupname", "Please enter a name:", "Group "
							+ groups.size(), validator);
					groupname.open();
					if (groupname.getReturnCode() == Window.OK) {
						String name = groupname.getValue();
						element.getConfiguration().setGroup(name);
					}
				} else {
					element.getConfiguration().setGroup(((GroupLayer) object).getName());
				}
				if (editor == null)
					editor = getMapEditor();
				if (editor != null)
						editor.addToGroup(element);
				treeViewer.refresh();
			}
		}
	}
}
