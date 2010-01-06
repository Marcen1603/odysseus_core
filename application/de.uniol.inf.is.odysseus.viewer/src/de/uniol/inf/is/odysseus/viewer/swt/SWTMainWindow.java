package de.uniol.inf.is.odysseus.viewer.swt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.viewer.Activator;
import de.uniol.inf.is.odysseus.viewer.ctrl.IController;
import de.uniol.inf.is.odysseus.viewer.model.create.IModelManagerListener;
import de.uniol.inf.is.odysseus.viewer.model.create.ModelManager;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.viewer.swt.render.SWTRenderManager;
import de.uniol.inf.is.odysseus.viewer.swt.resource.IResourceConfiguration;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.viewer.swt.resource.XMLResourceConfiguration;
import de.uniol.inf.is.odysseus.viewer.swt.select.GraphSelector;
import de.uniol.inf.is.odysseus.viewer.swt.select.ISelectListener;
import de.uniol.inf.is.odysseus.viewer.swt.select.ISelector;
import de.uniol.inf.is.odysseus.viewer.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.viewer.view.manage.AbstractView;
import de.uniol.inf.is.odysseus.viewer.view.manage.IGraphViewFactory;
import de.uniol.inf.is.odysseus.viewer.view.manage.OdysseusGraphViewFactory;
import de.uniol.inf.is.odysseus.viewer.view.position.SugiyamaPositioner;
import de.uniol.inf.is.odysseus.viewer.view.render.IRenderManager;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.viewer.view.symbol.XMLSymbolConfiguration;

public class SWTMainWindow extends AbstractView<IPhysicalOperator> implements SWTInfoPanel.ISWTInfoPanelListener, ISelectListener<INodeView<IPhysicalOperator>>,
		IModelManagerListener<IPhysicalOperator>, MouseWheelListener {

	private static final Logger logger = LoggerFactory.getLogger(SWTMainWindow.class);

	private final IGraphViewFactory<IPhysicalOperator> DEFAULT_GRAPH_FACTORY = new OdysseusGraphViewFactory();
	private final ISymbolElementFactory<IPhysicalOperator> DEFAULT_SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();

	private ISymbolConfiguration symbolConfiguration;
	private IResourceConfiguration resourceConfiguration;

	private static final float ZOOM_STEP = 0.1f;
	private static final int UPDATEINTERVAL_STEP = 500;
	private static final int UPDATEINTERVAL_STD = 1000;

	private final SWTStatusLine statusText;
	private final TabFolder tabFolder;
	private final List<SWTGraphTab> tabs = new ArrayList<SWTGraphTab>();
	private TabItem selectedTab;
	private final Label zoomText;

	private final Map<INodeView<IPhysicalOperator>, SWTInfoPanel> showInfoPanels = new HashMap<INodeView<IPhysicalOperator>, SWTInfoPanel>();

	private final Shell shell;
	private final boolean useOGL;

	public SWTMainWindow(Shell comp, IController<IPhysicalOperator> ctrl, boolean useOGL) {
		super(ctrl);

		this.useOGL = useOGL;
		ctrl.getModelManager().addListener(this);

		// Parameter prüfen
		if (comp == null)
			throw new IllegalArgumentException("composite is null!");

		// Parameter übernehmen
		shell = comp;

		// Ressourcen laden
		try {
			resourceConfiguration = new XMLResourceConfiguration(Activator.getContext().getBundle().getEntry(Activator.RESOURCES_FILE), Activator.getContext().getBundle().getEntry(
					Activator.XSD_RESOURCES_FILE));
			SWTResourceManager.getInstance().freeAllResources();
			SWTResourceManager.getInstance().load(shell.getDisplay(), resourceConfiguration);
		} catch (Exception ex) {
			new SWTExceptionWindow(shell, ex);
		}

		// *******************************
		// Menü
		// *******************************
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
		fileItem.setText("&Datei");
		MenuItem editItem = new MenuItem(menu, SWT.CASCADE);
		editItem.setText("&Bearbeiten");
		MenuItem viewItem = new MenuItem(menu, SWT.CASCADE);
		viewItem.setText("&Ansicht");
		MenuItem nodeItem = new MenuItem(menu, SWT.CASCADE);
		nodeItem.setText("K&noten");
//		MenuItem configItem = new MenuItem(menu, SWT.CASCADE);
//		configItem.setText("&Optionen");

		Menu fileSubMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileSubMenu);
		Menu editSubMenu = new Menu(shell, SWT.DROP_DOWN);
		editItem.setMenu(editSubMenu);
		Menu viewSubMenu = new Menu(shell, SWT.DROP_DOWN);
		viewItem.setMenu(viewSubMenu);
		Menu nodeSubMenu = new Menu(shell, SWT.DROP_DOWN);
		nodeItem.setMenu(nodeSubMenu);
//		Menu configSubMenu = new Menu(shell, SWT.DROP_DOWN);
//		configItem.setMenu(configSubMenu);

		// DATEI
		MenuItem exitMenuItem = new MenuItem(fileSubMenu, SWT.PUSH);
		exitMenuItem.setText("&Beenden");
		exitMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox msgBox = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				msgBox.setText("Warnung");
				msgBox.setMessage("Wirklich beenden?");
				if (msgBox.open() == SWT.YES) {
					shell.close();
					shell.dispose();
				}
			}
		});

		// BEARBEITEN
		MenuItem selectAllMenuItem = new MenuItem(editSubMenu, SWT.PUSH);
		selectAllMenuItem.setText("&Alle auswählen\tStrg+A");
		selectAllMenuItem.setAccelerator(SWT.MOD1 + 'A');
		selectAllMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null && getSelectedRenderManager() != null) {
					getSelectedRenderManager().getSelector().unselectAll();
					getSelectedRenderManager().getSelector().select(getSelectedGraphView().getViewedNodes());
				}
			}
		});

		new MenuItem(editSubMenu, SWT.SEPARATOR);

		MenuItem unselectMenuItem = new MenuItem(editSubMenu, SWT.PUSH);
		unselectMenuItem.setText("A&uswahl aufheben");
		unselectMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() != null)
					getSelectedRenderManager().getSelector().unselectAll();
			}
		});
		MenuItem reverseSelectMenuItem = new MenuItem(editSubMenu, SWT.PUSH);
		reverseSelectMenuItem.setText("Auswahl umkeh&ren\tStrg+R");
		reverseSelectMenuItem.setAccelerator(SWT.MOD1 + 'R');
		reverseSelectMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null && getSelectedRenderManager() != null) {
					Collection<INodeView<IPhysicalOperator>> selectedNodes = getSelectedRenderManager().getSelector().getSelected();

					Collection<INodeView<IPhysicalOperator>> nodesToSelect = new ArrayList<INodeView<IPhysicalOperator>>();
					for (INodeView<IPhysicalOperator> node : getSelectedGraphView().getViewedNodes()) {
						if (!selectedNodes.contains(node)) {
							nodesToSelect.add(node);
						}
					}
					getSelectedRenderManager().getSelector().unselectAll();
					getSelectedRenderManager().getSelector().select(nodesToSelect);
				}
			}
		});
		MenuItem selectStartMenuItem = new MenuItem(editSubMenu, SWT.PUSH);
		selectStartMenuItem.setText("&Vorgänger auswählen\tStrg+PfeilUnten");
		selectStartMenuItem.setAccelerator(SWT.MOD1 + SWT.ARROW_DOWN);
		selectStartMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;
				Collection<INodeView<IPhysicalOperator>> selectedNodes = getSelectedRenderManager().getSelector().getSelected();

				Collection<INodeView<IPhysicalOperator>> nodesToSelect = new ArrayList<INodeView<IPhysicalOperator>>();
				for (INodeView<IPhysicalOperator> selectedNode : selectedNodes) {
					Collection<IConnectionView<IPhysicalOperator>> connections = selectedNode.getConnectionsAsEnd();
					for (IConnectionView<IPhysicalOperator> conn : connections) {
						nodesToSelect.add(conn.getViewedStartNode());
					}
				}

				getSelectedRenderManager().getSelector().unselectAll();
				getSelectedRenderManager().getSelector().select(nodesToSelect);
			}
		});
		MenuItem selectEndMenuItem = new MenuItem(editSubMenu, SWT.PUSH);
		selectEndMenuItem.setText("&Nachfolger auswählen\tStrg+PfeilOben");
		selectEndMenuItem.setAccelerator(SWT.MOD1 + SWT.ARROW_UP);
		selectEndMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				Collection<INodeView<IPhysicalOperator>> selectedNodes = getSelectedRenderManager().getSelector().getSelected();

				Collection<INodeView<IPhysicalOperator>> nodesToSelect = new ArrayList<INodeView<IPhysicalOperator>>();
				for (INodeView<IPhysicalOperator> selectedNode : selectedNodes) {
					Collection<IConnectionView<IPhysicalOperator>> connections = selectedNode.getConnectionsAsStart();
					for (IConnectionView<IPhysicalOperator> conn : connections) {
						nodesToSelect.add(conn.getViewedEndNode());
					}
				}

				getSelectedRenderManager().getSelector().unselectAll();
				getSelectedRenderManager().getSelector().select(nodesToSelect);
			}
		});

		new MenuItem(editSubMenu, SWT.SEPARATOR);

		MenuItem selectPathMenuItem = new MenuItem(editSubMenu, SWT.PUSH);
		selectPathMenuItem.setText("&Pfad auswählen\tStrg+P");
		selectPathMenuItem.setAccelerator(SWT.MOD1 + 'P');
		selectPathMenuItem.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				if (getSelectedRenderManager().getSelector().getSelected().size() != 2)
					return;
				INodeView<IPhysicalOperator>[] selectedNodes = getSelectedRenderManager().getSelector().getSelected().toArray(new INodeView[0]);
				getSelectedRenderManager().getSelector().unselectAll();
				if (!((GraphSelector<IPhysicalOperator>) getSelectedRenderManager().getSelector()).selectPath(selectedNodes[0], selectedNodes[1])) {
					statusText.setErrorText("Konnte keinen Pfad finden");
				}
			}
		});

		// ANSICHT
		MenuItem resetViewMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
		resetViewMenuItem.setText("&Graphen ausrichten\tStrg+G");
		resetViewMenuItem.setAccelerator(SWT.MOD1 + 'G');
		resetViewMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;
				getSelectedRenderManager().resetZoom();
				getSelectedRenderManager().resetGraphOffset();
				getSelectedRenderManager().resetPositions();
				getSelectedRenderManager().refreshView();
			}
		});

		new MenuItem(viewSubMenu, SWT.SEPARATOR);

		MenuItem zoomInMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
		zoomInMenuItem.setText("&Heranzoomen\tStrg+Plus");
		zoomInMenuItem.setAccelerator(SWT.MOD1 + '+');
		zoomInMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				getSelectedRenderManager().zoom(ZOOM_STEP);
			}
		});
		MenuItem zoomOutMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
		zoomOutMenuItem.setText("&Rauszoomen\tStrg+Minus");
		zoomOutMenuItem.setAccelerator(SWT.MOD1 + '-');
		zoomOutMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;
				getSelectedRenderManager().zoom(-ZOOM_STEP);
			}
		});
		MenuItem stdZoomMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
		stdZoomMenuItem.setText("&Normalzoom\tStrg+Z");
		stdZoomMenuItem.setAccelerator(SWT.MOD1 + 'Z');
		stdZoomMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				getSelectedRenderManager().zoom(1.0f - getSelectedRenderManager().getZoomFactor());
			}
		});

		if (!useOGL) {
			new MenuItem(viewSubMenu, SWT.SEPARATOR);

			MenuItem miniMapMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
			miniMapMenuItem.setText("Übersicht\tStrg+M");
			miniMapMenuItem.setAccelerator(SWT.MOD1 + 'M');
			miniMapMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					showMiniMap();
				}
			});
		}

		// KNOTEN
		MenuItem allVisibleMenuItem = new MenuItem(nodeSubMenu, SWT.PUSH);
		allVisibleMenuItem.setText("Alle &sichtbar");
		allVisibleMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null && getSelectedRenderManager() != null) {
					setVisible(getSelectedGraphView().getViewedNodes(), true);
					updateStatusText();
					getSelectedRenderManager().refreshView();
				}
			}
		});

		MenuItem hideMenuItem = new MenuItem(nodeSubMenu, SWT.PUSH);
		hideMenuItem.setText("Kn&oten verstecken\tStrg+H");
		hideMenuItem.setAccelerator(SWT.MOD1 + 'H');
		hideMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				setVisible(getSelectedRenderManager().getSelector().getSelected(), false);
				getSelectedRenderManager().getSelector().unselectAll();
				updateStatusText();
				getSelectedRenderManager().refreshView();
			}
		});

		new MenuItem(nodeSubMenu, SWT.SEPARATOR);

		MenuItem allDisableMenuItem = new MenuItem(nodeSubMenu, SWT.PUSH);
		allDisableMenuItem.setText("Alle deaktivieren");
		allDisableMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null)
					setEnabled(getSelectedGraphView().getViewedNodes(), false);
			}
		});

		MenuItem disableMenuItem = new MenuItem(nodeSubMenu, SWT.PUSH);
		disableMenuItem.setText("Auswahl deaktivieren\tStrg+D");
		disableMenuItem.setAccelerator(SWT.MOD1 + 'D');
		disableMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;
				setEnabled(getSelectedRenderManager().getSelector().getSelected(), false);
			}
		});
		MenuItem allEnableMenuItem = new MenuItem(nodeSubMenu, SWT.PUSH);
		allEnableMenuItem.setText("Alle aktivieren");
		allEnableMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null)
					setEnabled(getSelectedGraphView().getViewedNodes(), true);
			}
		});

		MenuItem enableMenuItem = new MenuItem(nodeSubMenu, SWT.PUSH);
		enableMenuItem.setText("Auswahl aktivieren\tStrg+E");
		enableMenuItem.setAccelerator(SWT.MOD1 + 'E');
		enableMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				setEnabled(getSelectedRenderManager().getSelector().getSelected(), true);
			}
		});

//		new MenuItem(configSubMenu, SWT.SEPARATOR);

		// *******************************
		// Layout
		// *******************************
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		shell.setLayout(gridLayout);

		// *******************************
		// Toolbar
		// *******************************
		ToolBar toolbar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		ToolItem positionizeToolItem = new ToolItem(toolbar, SWT.PUSH);
		positionizeToolItem.setImage(SWTResourceManager.getInstance().getImage("position"));
		positionizeToolItem.setToolTipText("Graphen ausrichten");
		positionizeToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				getSelectedRenderManager().resetZoom();
				getSelectedRenderManager().resetGraphOffset();
				getSelectedRenderManager().resetPositions();
				getSelectedRenderManager().refreshView();
			}
		});

		ToolItem zoomInItem = new ToolItem(toolbar, SWT.PUSH);
		zoomInItem.setImage(SWTResourceManager.getInstance().getImage("zoomIn"));
		zoomInItem.setToolTipText("Heranzoomen");
		zoomInItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;
				getSelectedRenderManager().zoom(ZOOM_STEP);
			}
		});

		ToolItem zoomOutItem = new ToolItem(toolbar, SWT.PUSH);
		zoomOutItem.setImage(SWTResourceManager.getInstance().getImage("zoomOut"));
		zoomOutItem.setToolTipText("Rauszoomen");
		zoomOutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;
				getSelectedRenderManager().zoom(-ZOOM_STEP);
			}
		});

		ToolItem deactivateItem = new ToolItem(toolbar, SWT.PUSH);
		deactivateItem.setImage(SWTResourceManager.getInstance().getImage("pause"));
		deactivateItem.setToolTipText("Deaktivieren");
		deactivateItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null && getSelectedRenderManager() != null) {
					Collection<INodeView<IPhysicalOperator>> selectedNodes = getSelectedRenderManager().getSelector().getSelected();
					if (selectedNodes.isEmpty())
						setEnabled(getSelectedGraphView().getViewedNodes(), false);
					else
						setEnabled(selectedNodes, false);
				}
			}
		});

		ToolItem activateItem = new ToolItem(toolbar, SWT.PUSH);
		activateItem.setImage(SWTResourceManager.getInstance().getImage("play"));
		activateItem.setToolTipText("Aktivieren");
		activateItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null && getSelectedRenderManager() != null) {
					Collection<INodeView<IPhysicalOperator>> selectedNodes = getSelectedRenderManager().getSelector().getSelected();
					if (selectedNodes.isEmpty())
						setEnabled(getSelectedGraphView().getViewedNodes(), true);
					else
						setEnabled(selectedNodes, true);
				}
			}
		});

		ToolItem hideNodeItem = new ToolItem(toolbar, SWT.PUSH);
		hideNodeItem.setImage(SWTResourceManager.getInstance().getImage("invisibleNode"));
		hideNodeItem.setToolTipText("Knoten Verstecken");
		hideNodeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedRenderManager() == null)
					return;

				setVisible(getSelectedRenderManager().getSelector().getSelected(), false);
				getSelectedRenderManager().getSelector().unselectAll();
				updateStatusText();
				getSelectedRenderManager().refreshView();
			}
		});

		ToolItem showAllItem = new ToolItem(toolbar, SWT.PUSH);
		showAllItem.setImage(SWTResourceManager.getInstance().getImage("visible"));
		showAllItem.setToolTipText("Alle Knoten anzeigen");
		showAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getSelectedGraphView() != null && getSelectedRenderManager() != null) {
					setVisible(getSelectedGraphView().getViewedNodes(), true);
					updateStatusText();
					getSelectedRenderManager().refreshView();
				}
			}
		});

		// ************************
		// Steuerelemente
		// ************************
		Composite upperRightComp = new Composite(shell, 0);
		GridLayout updaterLayout = new GridLayout();
		updaterLayout.numColumns = 3;
		updaterLayout.verticalSpacing = 0;
		updaterLayout.marginHeight = 0;
		updaterLayout.marginWidth = 0;
		upperRightComp.setLayout(updaterLayout);

		tabFolder = new TabFolder(shell, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setBackground(SWTResourceManager.getInstance().getColor(180, 180, 180));
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tabChanged(selectedTab, (TabItem)e.item);
			}
		});

		// Statuszeile
		Composite statusLine = new Composite(shell, 0);
		statusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		statusLine.setBackground(shell.getBackground());
		statusLine.setForeground(shell.getForeground());
		statusLine.setFont(shell.getFont());
		GridLayout statusLineLayout = new GridLayout();
		statusLineLayout.numColumns = 3;
		statusLine.setLayout(statusLineLayout);

		// Statustext
		statusText = new SWTStatusLine(statusLine);
		statusText.setText("Viewer bereit");

		// Rechter bereich der Statuszeile
		Composite statusRight = new Composite(statusLine, 0);
		statusRight.setLayout(new FillLayout());
		GridData d2 = new GridData();
		d2.horizontalAlignment = GridData.END;
		d2.widthHint = 300;
		statusRight.setLayoutData(d2);

		zoomText = new Label(statusRight, SWT.SHADOW_IN);
		zoomText.setText("100 %");

		// if( useOGL ) {
		// defaultSymbolFactory = new
		// JOGLSymbolElementFactory<IPhysicalOperator>();
		// renderManager = new JOGLRenderManager<IPhysicalOperator>(
		// canvasComposite, statusText, new
		// SugiyamaPositioner(defaultSymbolFactory) );
		// renderManager.getSelector().addSelectListener( this );
		// } else {
		// renderManager = new
		// SWTRenderManager<IPhysicalOperator>(canvasComposite, statusText, new
		// SugiyamaPositioner(defaultSymbolFactory) );
		// renderManager.getSelector().addSelectListener( this );
		// }

		// Wenn SWT zum Zeichnen genutzt wird, dann muss ein
		// Updateregler plaziert werden, sowie geeignete Menüs
		// if( renderManager instanceof SWTRenderManager<?> ) {

		MenuItem increaseUpdateIntervalMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
		increaseUpdateIntervalMenuItem.setText("&Intervall erhöhen\tStrg+Alt+Plus");
		increaseUpdateIntervalMenuItem.setAccelerator(SWT.MOD1 + SWT.MOD3 + '+');

		MenuItem decreaseUpdateIntervalMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
		decreaseUpdateIntervalMenuItem.setText("&Intervall verringern\tStrg+Alt+Minus");
		decreaseUpdateIntervalMenuItem.setAccelerator(SWT.MOD1 + SWT.MOD3 + '-');

		MenuItem stdUpdateIntervalMenuItem = new MenuItem(viewSubMenu, SWT.PUSH);
		stdUpdateIntervalMenuItem.setText("&Intervall auf 1 Sekunde\tStrg+Alt+I");
		stdUpdateIntervalMenuItem.setAccelerator(SWT.MOD1 + SWT.MOD3 + 'I');

		new MenuItem(viewSubMenu, SWT.SEPARATOR);

		Label label = new Label(upperRightComp, SWT.SHADOW_IN);
		label.setText("Intervall:");

		final Spinner updateIntervalSpinner = new Spinner(upperRightComp, SWT.HORIZONTAL | SWT.BORDER);
		updateIntervalSpinner.setMaximum(60000);
		updateIntervalSpinner.setMinimum(100);
		updateIntervalSpinner.setIncrement(200);
		updateIntervalSpinner.setPageIncrement(1000);
		updateIntervalSpinner.setSelection(1000);
		updateIntervalSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final SWTGraphTab tab = getSelectedTab();
				if (tab == null)
					return;

				final SWTRenderManager<IPhysicalOperator> rm = tab.renderManager;
				rm.setUpdateInterval(updateIntervalSpinner.getSelection());
			}
		});
		increaseUpdateIntervalMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateIntervalSpinner.setSelection(updateIntervalSpinner.getSelection() + UPDATEINTERVAL_STEP);
				final SWTGraphTab tab = getSelectedTab();
				if (tab == null)
					return;

				final SWTRenderManager<IPhysicalOperator> rm = tab.renderManager;
				rm.setUpdateInterval(updateIntervalSpinner.getSelection());
			}
		});
		decreaseUpdateIntervalMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateIntervalSpinner.setSelection(updateIntervalSpinner.getSelection() - UPDATEINTERVAL_STEP);
				final SWTGraphTab tab = getSelectedTab();
				if (tab == null)
					return;

				final SWTRenderManager<IPhysicalOperator> rm = tab.renderManager;
				rm.setUpdateInterval(updateIntervalSpinner.getSelection());
			}
		});
		stdUpdateIntervalMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateIntervalSpinner.setSelection(UPDATEINTERVAL_STD);
				final SWTGraphTab tab = getSelectedTab();
				if (tab == null)
					return;

				final SWTRenderManager<IPhysicalOperator> rm = tab.renderManager;
				rm.setUpdateInterval(updateIntervalSpinner.getSelection());
			}
		});

		shell.layout();
	}

	private SWTGraphTab getSelectedTab() {

		final int selectedTabIndex = tabFolder.getSelectionIndex();
		if (selectedTabIndex > -1)
			return tabs.get(selectedTabIndex);

		return null;
	}

	private IRenderManager<IPhysicalOperator> getSelectedRenderManager() {
		SWTGraphTab tab = getSelectedTab();
		if (tab != null)
			return tab.renderManager;
		return null;
	}

	private IGraphView<IPhysicalOperator> getSelectedGraphView() {
		SWTGraphTab tab = getSelectedTab();
		if (tab != null)
			return tab.graphView;
		return null;
	}

	private SWTGraphTab createTab(IGraphView<IPhysicalOperator> graphView) {

		// Graphenbereich
		Composite graphArea = new Composite(tabFolder, 0);
		graphArea.setLayout(new FormLayout());
		GridData graphAreaData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		graphAreaData.horizontalIndent = 0;
		graphAreaData.horizontalSpan = 2;
		graphArea.setLayoutData(graphAreaData);

		// Trenner innerhalb des Graphenbereichs
		final Sash sash = new Sash(graphArea, SWT.VERTICAL);
		FormData sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 0);
		sashFormData.bottom = new FormAttachment(100, 0);
		sashFormData.left = new FormAttachment(60, 0);
		sash.setLayoutData(sashFormData);
		sash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// damit es auch mit dem Resizen des Fensters klappt
				final int val = (int) (((float) event.x / shell.getSize().x) * 100) + 1;
				((FormData) sash.getLayoutData()).left = new FormAttachment(val, 0);
				sash.getParent().layout();
			}
		});

		// Zeichenbereich für den Graphen
		Composite canvasComposite = new Composite(graphArea, SWT.BORDER);
		canvasComposite.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormData canvasFormData = new FormData();
		canvasFormData.top = new FormAttachment(0, 0);
		canvasFormData.bottom = new FormAttachment(100, 0);
		canvasFormData.left = new FormAttachment(0, 0);
		canvasFormData.right = new FormAttachment(sash, 0);
		canvasComposite.setLayoutData(canvasFormData);

		SWTRenderManager<IPhysicalOperator> renderManager = new SWTRenderManager<IPhysicalOperator>(canvasComposite, statusText, new SugiyamaPositioner(DEFAULT_SYMBOL_FACTORY));
		renderManager.getSelector().addSelectListener(this);
		renderManager.setDisplayedGraph(graphView);

		// Informationsbereich
		ScrolledComposite infoScroll = new ScrolledComposite(graphArea, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		FormData infoFormData = new FormData();
		infoFormData.top = new FormAttachment(0, 0);
		infoFormData.bottom = new FormAttachment(100, 0);
		infoFormData.left = new FormAttachment(sash, 0);
		infoFormData.right = new FormAttachment(100, 0);
		infoScroll.setLayoutData(infoFormData);

		Composite infoArea = new Composite(infoScroll, 0);
		GridData gd = new GridData(GridData.FILL_BOTH);
		infoArea.setLayoutData(gd);
		GridLayout infoLayout = new GridLayout();
		infoLayout.numColumns = 1;
		infoLayout.verticalSpacing = 10;
		infoArea.setLayout(infoLayout);
		infoScroll.setContent(infoArea);
		infoScroll.setMinSize(200, 200);
		infoScroll.setExpandHorizontal(true);
		infoScroll.setExpandVertical(true);


		SWTGraphTab tab = new SWTGraphTab();
		tab.canvasComposite = canvasComposite;
		tab.graphArea = graphArea;
		tab.infoArea = infoArea;
		tab.infoScroll = infoScroll;
		tab.renderManager = renderManager;
		tab.sash = sash;
		tab.graphView = graphView;
		
		tabs.add(tab);
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Model " + (tabFolder.getItemCount()));
		tabItem.setControl(graphArea);
		tab.tabItem = tabItem;
		
		return tab;
	}

	public void dispose() {
		logger.debug("Disposing resources");

		for (SWTGraphTab t : tabs) {
			disconnectWithModel(t.graphView);
		}

		SWTResourceManager.getInstance().freeAllResources();
		shell.dispose();

		logger.debug("Resources disposed");
	}

	private void connectWithModel(IGraphView<IPhysicalOperator> graphView) {
		if (graphView != null) {
			for (INodeView<IPhysicalOperator> node : graphView.getViewedNodes()) {
				if (node instanceof IOdysseusNodeView)
					((IOdysseusNodeView) node).connect();
			}
		}
	}

	private void disconnectWithModel(IGraphView<IPhysicalOperator> graphView) {
		if (graphView != null) {
			for (INodeView<IPhysicalOperator> node : graphView.getViewedNodes()) {
				if (node instanceof IOdysseusNodeView)
					((IOdysseusNodeView) node).disconnect();
			}
		}
	}

	private void loadConfigurations() {
		try {
			ISymbolConfiguration symConfig = new XMLSymbolConfiguration(Activator.getContext().getBundle().getEntry(Activator.SYMBOL_CONFIG_FILE), Activator.getContext().getBundle().getEntry(
					Activator.XSD_SYMBOL_SCHEMA_FILE));
			symbolConfiguration = symConfig;

			resourceConfiguration = new XMLResourceConfiguration(Activator.getContext().getBundle().getEntry(Activator.RESOURCES_FILE), Activator.getContext().getBundle().getEntry(
					Activator.XSD_RESOURCES_FILE));
			SWTResourceManager.getInstance().freeImageResources();
			SWTResourceManager.getInstance().load(shell.getDisplay(), resourceConfiguration);
		} catch (IOException ex) {
			ex.printStackTrace();
			statusText.setErrorText("Konnte Konfiguration nicht laden!");
		}
	}

	private void updateStatusText() {
		if (getSelectedRenderManager() == null)
			return;
		Collection<INodeView<IPhysicalOperator>> selectedNodes = getSelectedRenderManager().getSelector().getSelected();

		if (selectedNodes.isEmpty())
			statusText.setText("");
		else if (selectedNodes.size() == 1) {
			for (INodeView<IPhysicalOperator> n : selectedNodes) {
				if (n.getModelNode() != null)
					statusText.setText(n.getModelNode().getName() + " ausgewählt");
				else
					statusText.setText("");
			}
		} else {
			statusText.setText(selectedNodes.size() + " Knoten ausgewählt");
		}

		if (getSelectedGraphView() != null) {
			int count = 0;
			for (INodeView<IPhysicalOperator> display : getSelectedGraphView().getViewedNodes()) {
				if (!display.isVisible())
					count++;
			}
			if (count > 0)
				statusText.setText(statusText.getText() + "(" + count + " Knoten versteckt)");
		}
	}

	private void showMiniMap() {
		if (!useOGL) {
			((SWTRenderManager<IPhysicalOperator>) getSelectedRenderManager()).showMinimap();
		}
	}

	private void addInfoPanel(INodeView<IPhysicalOperator> node) {
		if (!showInfoPanels.containsKey(node)) {

			SWTInfoPanel p = new SWTInfoPanel(getSelectedTab().infoArea, (IOdysseusNodeView) node);
			p.addInfoPanelListener(this);
			showInfoPanels.put(node, p);
			logger.debug("NodeInfoPanel for " + node + " created");
		}
	}

	private void removeInfoPanel(INodeView<IPhysicalOperator> unselected) {
		if (showInfoPanels.containsKey(unselected)) {
			SWTInfoPanel panel = showInfoPanels.get(unselected);
			if (panel.isPinned())
				return;

			panel.removeInfoPanelListener(this);
			panel.dispose();
			showInfoPanels.remove(unselected);

			logger.debug("NodeInfoPanel for " + unselected + " destroyed");
		}
	}

	private void updateZoomText() {
		if (getSelectedRenderManager() != null)
			zoomText.setText(String.valueOf((int) (100.0f * getSelectedRenderManager().getZoomFactor())) + " %");
		else
			zoomText.setText("");
	}

	/********************** EREIGNISSE *********************************************/

	@Override
	public void pinChanged(SWTInfoPanel sender) {
		if (!sender.isPinned()) {
			IOdysseusNodeView node = sender.getNodeDisplay();
			if (showInfoPanels.containsKey(node)) {
				if (!getSelectedRenderManager().getSelector().getSelected().contains(node)) {
					removeInfoPanel(node);
					getSelectedTab().infoArea.layout();
				}
			}
		}
	}

	@Override
	public void showDataStream(SWTInfoPanel sender) {
		new SWTDiagramSelectWindow(shell, sender.getNodeDisplay());
	}

	@Override
	public void selectNode(SWTInfoPanel sender) {
		if (getSelectedRenderManager().getSelector().getSelected().size() == 1 && getSelectedRenderManager().getSelector().getSelected().contains(sender.getNodeDisplay()))
			return;

		getSelectedRenderManager().getSelector().unselectAll();
		getSelectedRenderManager().getSelector().select(sender.getNodeDisplay());
		getSelectedRenderManager().refreshView();
	}

	@Override
	public void selectObject(ISelector<INodeView<IPhysicalOperator>> sender, Collection<? extends INodeView<IPhysicalOperator>> selected) {
		logger.debug("Select:" + selected);
		for (INodeView<IPhysicalOperator> node : selected) {
			if (node.getModelNode() != null)
				addInfoPanel(node);
		}
		logger.debug("Select finished!");
		getSelectedTab().infoScroll.setMinSize(getSelectedTab().infoArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		getSelectedTab().infoArea.layout();
		getSelectedTab().infoScroll.layout();

		updateStatusText();
		logger.debug("layout finished!");
	}

	@Override
	public void unselectObject(ISelector<INodeView<IPhysicalOperator>> sender, Collection<? extends INodeView<IPhysicalOperator>> unselected) {
		logger.debug("Unselect:" + unselected);
		for (INodeView<IPhysicalOperator> display : unselected) {
			removeInfoPanel(display);
		}

		logger.debug("Unselect finished!");
		getSelectedTab().infoScroll.setMinSize(getSelectedTab().infoArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		getSelectedTab().infoArea.layout();
		getSelectedTab().infoScroll.layout();

		updateStatusText();
		logger.debug("layout finished!");
	}

	@Override
	public void modelAdded(ModelManager<IPhysicalOperator> sender, final IGraphModel<IPhysicalOperator> model) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (symbolConfiguration == null) {
					loadConfigurations();
				}

				try {
					// View erzeugen
					IGraphView<IPhysicalOperator> graphView = DEFAULT_GRAPH_FACTORY.createGraphDisplay(model, symbolConfiguration, DEFAULT_SYMBOL_FACTORY);

					// Neues Tab...
					SWTGraphTab tab = createTab(graphView);
					tab.renderManager.resetPositions();
					tab.renderManager.resetZoom();
					tab.renderManager.resetGraphOffset();
					tab.renderManager.refreshView();
					
					tabFolder.setSelection(tab.tabItem);

					statusText.setText("Neues Modell geladen");

				} catch (Exception ex) {
					logger.error("Could not load model: " + ex.getMessage());
					ex.printStackTrace();
					statusText.setErrorText("Konnte Modell nicht laden!");

				}
			}
		});
	}

	@Override
	public void modelRemoved(ModelManager<IPhysicalOperator> sender, IGraphModel<IPhysicalOperator> model) {

	}
	
	private SWTGraphTab getGraphTab( TabItem tab ) {
		for( SWTGraphTab t : tabs ) {
			if( t.tabItem == tab ) 
				return t;
		}
		return null;
	}

	private void tabChanged(TabItem oldTab, TabItem selectedTab) {
		
		SWTGraphTab oldGraphTab = getGraphTab(oldTab);
		SWTGraphTab selectedGraphTab = getGraphTab( selectedTab );
		
		if (oldGraphTab != null) {
			oldGraphTab.canvasComposite.removeMouseWheelListener(this);
			disconnectWithModel(oldGraphTab.graphView);
		}

		if( selectedGraphTab != null ) {
			selectedGraphTab.canvasComposite.addMouseWheelListener(this);
			connectWithModel(selectedGraphTab.graphView);
		}
		
		this.selectedTab = selectedTab;
		updateZoomText();
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		updateZoomText();
	}
}
