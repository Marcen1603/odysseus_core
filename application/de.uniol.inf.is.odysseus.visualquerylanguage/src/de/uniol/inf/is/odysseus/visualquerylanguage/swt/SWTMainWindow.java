package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.visualquerylanguage.ISWTTreeChangedListener;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.DefaultQueryController;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.IQueryController;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.query.DefaultQuery;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs.DefaultGraphArea;

public class SWTMainWindow {

	private final Logger log = LoggerFactory.getLogger(SWTMainWindow.class);

	private final Shell shell;

	private IAdvancedExecutor executor = null;

	IQueryController<DefaultQuery> queryController = new DefaultQueryController();

	private CTabFolder tabFolder;
	CTabItem queryListTab;
	
	private HashMap<Integer, DefaultQuery> queryMap = new HashMap<Integer, DefaultQuery>();

	public SWTMainWindow(Display display, IAdvancedExecutor exec)
			throws IOException {

		shell = new Shell(display);
		shell.setText("Visuelle Anfragesprache von Nico Klein");
		shell.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - shell.getSize().x/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 - shell.getSize().y/2);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		shell.setLayout(gridLayout);

		this.executor = exec;

		// Menu von Timo
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
		MenuItem configItem = new MenuItem(menu, SWT.CASCADE);
		configItem.setText("&Optionen");

		// Submenus von Timo
		Menu fileSubMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileSubMenu);
		Menu editSubMenu = new Menu(shell, SWT.DROP_DOWN);
		editItem.setMenu(editSubMenu);
		Menu viewSubMenu = new Menu(shell, SWT.DROP_DOWN);
		viewItem.setMenu(viewSubMenu);
		Menu nodeSubMenu = new Menu(shell, SWT.DROP_DOWN);
		nodeItem.setMenu(nodeSubMenu);
		Menu configSubMenu = new Menu(shell, SWT.DROP_DOWN);
		configItem.setMenu(configSubMenu);

		// DATEI von Timo
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

		MenuItem removeNodeMenuItem = new MenuItem(nodeSubMenu, SWT.PUSH);
		removeNodeMenuItem.setText("&Knoten Löschen");
		removeNodeMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					((DefaultGraphArea) (tabFolder.getSelection().getControl()))
							.removeNodes();
				}
			}
		});

		MenuItem sortGraphItem = new MenuItem(nodeSubMenu, SWT.PUSH);
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
				"newQuery"));
		newSourceItem.setToolTipText("Neue Quelle hinzufügen");
		newSourceItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Collection<ISWTTreeChangedListener> listeners = new ArrayList<ISWTTreeChangedListener>();
				for (CTabItem item : tabFolder.getItems()) {
					if(item.getControl() instanceof DefaultGraphArea) {
						listeners.add((ISWTTreeChangedListener)item.getControl());
					}
				}
				@SuppressWarnings("unused")
				SWTSourceCreator newSource = new SWTSourceCreator(shell, executor, listeners);
			}
		});

		ToolItem startQueryItem = new ToolItem(tools, SWT.PUSH);
		startQueryItem.setImage(SWTResourceManager.getInstance().getImage(
				"newQuery"));
		startQueryItem.setToolTipText("Anfrage ausführen");
		startQueryItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelection().getControl() instanceof DefaultGraphArea) {
					try {
						queryController.launchQuery(tabFolder.getSelection()
								.getControl(), executor);
					} catch (SecurityException e1) {
						((DefaultGraphArea)tabFolder.getSelection().getControl()).getStatusLine().setErrorText("Es ist ein Fehler bei der Ausführung der Anfrage aufgetreten.");
						log.error("Error while executing query.");
					} catch (IllegalArgumentException e1) {
						((DefaultGraphArea)tabFolder.getSelection().getControl()).getStatusLine().setErrorText("Es ist ein Fehler bei der Ausführung der Anfrage aufgetreten.");
						log.error("Error while executing query.");
					} catch (ClassNotFoundException e1) {
						((DefaultGraphArea)tabFolder.getSelection().getControl()).getStatusLine().setErrorText("Es ist ein Fehler beim dynamischen Laden der Klassen aufgetreten.");
						log.error("Error while using reflection for dynamic classloading.");
					} catch (NoSuchMethodException e1) {
						((DefaultGraphArea)tabFolder.getSelection().getControl()).getStatusLine().setErrorText("Es ist ein Fehler beim dynamischen Laden von Methoden aufgetreten.");
						log.error("Error while using reflection for dynamic methodloading.");
					} catch (InstantiationException e1) {
						((DefaultGraphArea)tabFolder.getSelection().getControl()).getStatusLine().setErrorText("Es ist ein Fehler beim dynamischen Instanziieren einer Klasse aufgetreten.");
						log.error("Error while creating an instance of a class.");
					} catch (IllegalAccessException e1) {
						((DefaultGraphArea)tabFolder.getSelection().getControl()).getStatusLine().setErrorText("Es ist ein Fehler bei der Ausführung der Anfrage aufgetreten.");
						log.error("Error while executing query");
					} catch (InvocationTargetException e1) {
						((DefaultGraphArea)tabFolder.getSelection().getControl()).getStatusLine().setErrorText("Es ist ein Fehler bei der Ausführung der Anfrage aufgetreten.");
						log.error("Error while executing query");
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

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void createNewGraphTab() {
		DefaultQuery query = new DefaultQuery("Query");
		DefaultGraphArea graphArea = new DefaultGraphArea(tabFolder, query, 0,
				executor);

		CTabItem item = new CTabItem(tabFolder, SWT.CLOSE);
		item.setText("Anfrage");
		item.setControl(graphArea);
		// queryMap.put(queryCounter, query);
		tabFolder.setSelection(item);
	}
}
