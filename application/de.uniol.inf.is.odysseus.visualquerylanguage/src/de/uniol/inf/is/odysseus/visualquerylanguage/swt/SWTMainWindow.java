package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.visualquerylanguage.ReflectionException;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.DefaultQueryController;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.IQueryController;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs.DefaultGraphArea;

public class SWTMainWindow {

	private final Logger log = LoggerFactory.getLogger(SWTMainWindow.class);

	private static Shell shell;

	private IAdvancedExecutor executor = null;

	IQueryController queryController = new DefaultQueryController();

	private CTabFolder tabFolder;
	CTabItem queryListTab;

	public SWTMainWindow(Display display, IAdvancedExecutor exec)
			throws IOException {

		shell = new Shell(display);
		shell.setText("Visuelle Anfragesprache von Nico Klein");
		shell.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2
				- shell.getSize().x / 2, Toolkit.getDefaultToolkit()
				.getScreenSize().height
				/ 2 - shell.getSize().y / 2);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		shell.setLayout(gridLayout);

		this.executor = exec;

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
		fileItem.setText("&Datei");
		MenuItem graphItem = new MenuItem(menu, SWT.CASCADE);
		graphItem.setText("&Graph");

		Menu fileSubMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileSubMenu);
		Menu graphSubMenu = new Menu(shell, SWT.DROP_DOWN);
		graphItem.setMenu(graphSubMenu);

		MenuItem exitMenuItem = new MenuItem(fileSubMenu, SWT.PUSH);
		exitMenuItem.setText("&Beenden");
		exitMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox msgBox = new MessageBox(shell, SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
				msgBox.setText("Warnung");
				msgBox.setMessage("Wirklich beenden?");
				if (msgBox.open() == SWT.YES) {
					shell.close();
					shell.dispose();
				}
			}
		});

		MenuItem removeNodeMenuItem = new MenuItem(graphSubMenu, SWT.PUSH);
		removeNodeMenuItem.setText("&Knoten Löschen\tEntf");
		removeNodeMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					((DefaultGraphArea) (tabFolder.getSelection().getControl()))
							.removeNodes();
				}
			}
		});
		removeNodeMenuItem.setAccelerator(SWT.DEL);

		MenuItem deleteConnectionItem = new MenuItem(graphSubMenu, SWT.PUSH);
		deleteConnectionItem.setText("&Verbindung löschen");

		deleteConnectionItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DefaultGraphArea area = null;
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					area = (DefaultGraphArea) tabFolder.getSelection()
							.getControl();
				}
				if (area != null) {
					if (deleteConnection()) {
						area.getStatusLine().setText("Verbindung gelöscht");
					} else {
						area
								.getStatusLine()
								.setErrorText(
										"Keine Verbindung zwischen den Knoten vorhanden.");
					}
				}
			}
		});

		MenuItem sortGraphItem = new MenuItem(graphSubMenu, SWT.PUSH);
		sortGraphItem.setText("&Graph automatisch anordnen");
		sortGraphItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					((DefaultGraphArea) (tabFolder.getSelection().getControl()))
							.getRenderer().resetPositions();
				}
			}
		});

		// Toolbar
		ToolBar tools = new ToolBar(shell, SWT.FLAT);

		ToolItem newQueryItem = new ToolItem(tools, SWT.PUSH);
		newQueryItem.setImage(SWTResourceManager.getInstance().getImage(
				"newQuery"));
		newQueryItem.setToolTipText("Neuen Anfrageplan erstellen");
		newQueryItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createNewGraphTab();
			}
		});

		ToolItem newSourceItem = new ToolItem(tools, SWT.PUSH);
		newSourceItem.setImage(SWTResourceManager.getInstance().getImage(
				"neueQuelle"));
		newSourceItem.setToolTipText("Neue Quelle hinzufügen");
		newSourceItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Collection<ISWTTreeChangedListener> listeners = new ArrayList<ISWTTreeChangedListener>();
				for (CTabItem item : tabFolder.getItems()) {
					if (item.getControl() instanceof DefaultGraphArea) {
						listeners.add((ISWTTreeChangedListener) item
								.getControl());
					}
				}
				@SuppressWarnings("unused")
				SWTSourceCreator newSource = new SWTSourceCreator(shell,
						executor, listeners);
			}
		});

		ToolItem startQueryItem = new ToolItem(tools, SWT.PUSH);
		startQueryItem.setImage(SWTResourceManager.getInstance().getImage(
				"start"));
		startQueryItem.setToolTipText("Anfrage ausführen");
		startQueryItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					try {
						queryController.launchQuery(tabFolder.getSelection()
								.getControl(), executor);
						tabFolder.getItem(0).setControl(getQueryTable());
						tabFolder.layout();
					} catch (ReflectionException e1) {
						((DefaultGraphArea) tabFolder.getSelection()
								.getControl())
								.getStatusLine()
								.setErrorText(
										"Es ist ein Fehler bei der Ausführung der Anfrage aufgetreten.");
						log.error("Error while executing query. Because of: ");
						e1.printStackTrace();
					}
				} else if (tabFolder.getSelection().getControl() instanceof Table) {
					if (((Table) (tabFolder.getSelection().getControl()))
							.getSelectionCount() != 0) {
						if (((Table) (tabFolder.getSelection().getControl()))
								.getSelection()[0].getData() instanceof IQuery) {
							try {
								executor
										.startQuery(((IQuery) ((Table) (tabFolder
												.getSelection().getControl()))
												.getSelection()[0].getData())
												.getID());
								tabFolder.getSelection().setControl(
										getQueryTable());
								tabFolder.layout();
							} catch (PlanManagementException e1) {
								log
										.error("Query could not be started. Because of: ");
								e1.printStackTrace();
							}
						}
					}
				}
			}

		});

		ToolItem stopQueryItem = new ToolItem(tools, SWT.PUSH);
		stopQueryItem.setImage(SWTResourceManager.getInstance().getImage(
				"stopQuery"));
		stopQueryItem.setToolTipText("Anfrage stoppen");
		stopQueryItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof Table) {
					if (((Table) (tabFolder.getSelection().getControl()))
							.getSelectionCount() != 0) {
						if (((Table) (tabFolder.getSelection().getControl()))
								.getSelection()[0].getData() instanceof IQuery) {
							if (((IQuery) ((Table) (tabFolder.getSelection()
									.getControl())).getSelection()[0].getData())
									.isStarted()) {
								try {
									executor
											.stopQuery(((IQuery) ((Table) (tabFolder
													.getSelection()
													.getControl()))
													.getSelection()[0]
													.getData()).getID());
									tabFolder.getSelection().setControl(
											getQueryTable());
									tabFolder.layout();
								} catch (PlanManagementException e1) {
									log
											.error("Query could not be stopped. Because of: ");
									e1.printStackTrace();
								}
							}
						}
					}
				} else if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					try {
						IQuery query = executor.getSealedPlan().getQuery(
								((DefaultGraphArea) tabFolder.getSelection()
										.getControl()).getQueryID());
						if (query != null && query.isStarted()) {
							executor.stopQuery(query.getID());
							((DefaultGraphArea) tabFolder.getSelection()
									.getControl()).setQueryID(-1);
							tabFolder.getItem(0).setControl(getQueryTable());
							tabFolder.layout();
						}

					} catch (PlanManagementException e1) {
						log.error("Query could not be stopped. Because of: ");
						e1.printStackTrace();
					}
				}
			}
		});

		ToolItem removeQueryItem = new ToolItem(tools, SWT.PUSH);
		removeQueryItem.setImage(SWTResourceManager.getInstance().getImage(
				"delete"));
		removeQueryItem.setToolTipText("Anfrage löschen");
		removeQueryItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof Table) {
					if (((Table) (tabFolder.getSelection().getControl()))
							.getSelectionCount() != 0) {
						if (((Table) (tabFolder.getSelection().getControl()))
								.getSelection()[0].getData() instanceof IQuery) {
							try {
								executor
										.removeQuery(((IQuery) ((Table) (tabFolder
												.getSelection().getControl()))
												.getSelection()[0].getData())
												.getID());
								tabFolder.getSelection().setControl(
										getQueryTable());
								tabFolder.layout();
							} catch (PlanManagementException e1) {
								log
										.error("Query could not be deleted. Because of: ");
								e1.printStackTrace();
							}
						}
					}
				} else if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					try {
						IQuery query = executor.getSealedPlan().getQuery(
								((DefaultGraphArea) tabFolder.getSelection()
										.getControl()).getQueryID());
						if (query != null) {
							executor.removeQuery(query.getID());
							((DefaultGraphArea) tabFolder.getSelection()
									.getControl()).setQueryID(-1);
							tabFolder.getItem(0).setControl(getQueryTable());
							tabFolder.layout();
						}

					} catch (PlanManagementException e1) {
						log.error("Query could not be deleted. Because of: ");
						e1.printStackTrace();
					}
				}
			}
		});

		ToolItem refreshQueryList = new ToolItem(tools, SWT.PUSH);
		refreshQueryList.setImage(SWTResourceManager.getInstance().getImage(
				"refresh"));
		refreshQueryList.setToolTipText("Anfrageliste aktualisiern");
		refreshQueryList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tabFolder.layout();
				tabFolder.setSelection(0);
			}
		});
		
		ToolItem deleteNode = new ToolItem(tools, SWT.PUSH);
		deleteNode.setImage(SWTResourceManager.getInstance().getImage(
				"deleteNode"));
		deleteNode.setToolTipText("Knoten löschen");
		deleteNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					((DefaultGraphArea) (tabFolder.getSelection().getControl()))
							.removeNodes();
				}
			}
		});
		
		ToolItem deleteConnection = new ToolItem(tools, SWT.PUSH);
		deleteConnection.setImage(SWTResourceManager.getInstance().getImage(
				"deleteConnection"));
		deleteConnection.setToolTipText("Verbindung löschen");
		deleteConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DefaultGraphArea area = null;
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					area = (DefaultGraphArea) tabFolder.getSelection()
							.getControl();
				}
				if (area != null) {
					if (deleteConnection()) {
						area.getStatusLine().setText("Verbindung gelöscht");
					} else {
						area
								.getStatusLine()
								.setErrorText(
										"Keine Verbindung zwischen den Knoten vorhanden.");
					}
				}
			}
		});

		tabFolder = new CTabFolder(shell, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		tabFolder.setLayoutData(gd);
		tabFolder.setLayout(new GridLayout());
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {

			@Override
			public void close(CTabFolderEvent event) {
				if (event.item.equals(queryListTab)) {
					event.doit = false;
				}
			}

		});

		queryListTab = new CTabItem(tabFolder, SWT.NULL);
		queryListTab.setText("Laufende Anfragen");

		getQueryTable();

		tabFolder.setSelection(queryListTab);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void createNewGraphTab() {
		DefaultGraphArea graphArea = new DefaultGraphArea(tabFolder, 0,
				executor);

		CTabItem item = new CTabItem(tabFolder, SWT.CLOSE);
		item.setText("Anfrage");
		item.setControl(graphArea);
		tabFolder.setSelection(item);
	}

	private synchronized Table getQueryTable() {
		Table table = null;
		try {
			if (!executor.getSealedPlan().getQueries().isEmpty()) {
				table = new Table(tabFolder, SWT.BORDER | SWT.SINGLE);
				TableColumn tc1 = new TableColumn(table, SWT.LEFT);
				TableColumn tc2 = new TableColumn(table, SWT.LEFT);
				tc1.setText("Query ID");
				tc1.setWidth(70);
				tc2.setText("Gestartet");
				tc2.setWidth(70);
				TableItem firstItem;
				for (IQuery query : executor.getSealedPlan().getQueries()) {
					firstItem = new TableItem(table, SWT.NONE);
					firstItem.setData(query);
					if (query.isStarted()) {
						firstItem.setText(new String[] {
								Integer.toString(query.getID()), "Ja" });
					} else {
						firstItem.setText(new String[] {
								Integer.toString(query.getID()), "Nein" });
					}
				}
				table.setHeaderVisible(true);
				queryListTab.setControl(table);
			}
		} catch (PlanManagementException e1) {
			log
					.error("Error while trying to get the Sealed Plan. Because of: ");
			e1.printStackTrace();
		}
		return table;
	}

	public static Shell getShell() {
		return shell;
	}

	@SuppressWarnings("unchecked")
	private boolean deleteConnection() {
		DefaultGraphArea area = null;
		if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
			area = (DefaultGraphArea) tabFolder.getSelection().getControl();
		}
		if (area != null) {
			if (area.getRenderer().getSelector().getSelected().size() == 2) {
				INodeModel<INodeContent> model1 = ((INodeView) area.getRenderer()
						.getSelector().getSelected().toArray()[0]).getModelNode();
				INodeModel<INodeContent> model2 = ((INodeView) area.getRenderer()
						.getSelector().getSelected().toArray()[1]).getModelNode();
				IConnectionModel<INodeContent> startConn = null;
				IConnectionModel<INodeContent> endConn = null;
				for (IConnectionModel<INodeContent> conn : model1
						.getConnectionsAsEndNode()) {
					for (IConnectionModel<INodeContent> conn1 : model2
							.getConnectionsAsStartNode()) {
						if (conn.equals(conn1)) {
							startConn = conn;
						}
					}
				}
				for (IConnectionModel<INodeContent> conn : model1
						.getConnectionsAsStartNode()) {
					for (IConnectionModel<INodeContent> conn1 : model2
							.getConnectionsAsEndNode()) {
						if (conn.equals(conn1)) {
							endConn = conn;
						}
					}
				}
				if (startConn != null || endConn != null) {
					area.removeSingelConnection(((INodeView) area.getRenderer()
							.getSelector().getSelected().toArray()[0]),
							((INodeView) area.getRenderer().getSelector()
									.getSelected().toArray()[1]), startConn, endConn);
					return true;
				}
			}
		}
		return false;
	}
}
