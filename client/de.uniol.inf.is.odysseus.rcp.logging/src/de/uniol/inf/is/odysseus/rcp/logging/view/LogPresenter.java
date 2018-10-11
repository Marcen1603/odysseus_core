package de.uniol.inf.is.odysseus.rcp.logging.view;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.ikoffice.widgets.SplitButton;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.logging.ILogSaver;
import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;
import de.uniol.inf.is.odysseus.rcp.logging.activator.RCPLoggingPlugIn;
import de.uniol.inf.is.odysseus.rcp.logging.save.LogSaverRegistry;
import de.uniol.inf.is.odysseus.rcp.logging.view.filter.LevelAndTextRCPLogFilter;
import de.uniol.inf.is.odysseus.rcp.logging.view.filter.LevelRCPLogFilter;
import de.uniol.inf.is.odysseus.rcp.logging.view.util.SaveMenuItem;

public class LogPresenter {

	private static final String[] LEVELS = { "ALL", "FATAL", "ERROR", "WARN", "INFO", "DEBUG" };

	private final LogTable logTableViewer;
	private final TabItem tab;

	private Text filterText;
	private Combo levelCombo;

	public LogPresenter(Composite parent, TabItem tab, boolean withFilters) {
		// Preconditions.checkNotNull(parent, "Parent must not be null!");
		// Preconditions.checkNotNull(tab, "TabItem must not be null!");

		Composite viewComposite = new Composite(parent, SWT.NONE);
		viewComposite.setLayout(new GridLayout());

		if (withFilters) {
			createSettingsArea(viewComposite);
		}

		logTableViewer = new LogTable(viewComposite, RCPLoggingPlugIn.getLogContainer());
		createButtons(viewComposite);

		this.tab = tab;
	}

	private void createButtons(Composite viewComposite) {
		Composite buttonBarComposite = new Composite(viewComposite, SWT.NONE);
		buttonBarComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonBarComposite.setLayout(new GridLayout(3, false));

		Composite buttonLeftComposite = new Composite(buttonBarComposite, SWT.NONE);
		buttonLeftComposite.setLayout(new GridLayout(3, true));

		Button clearTableButton = new Button(buttonLeftComposite, SWT.PUSH);
		clearTableButton.setText("Clear");
		clearTableButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		clearTableButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logTableViewer.clear();
			}
		});

		Button restoreTableButton = new Button(buttonLeftComposite, SWT.PUSH);
		restoreTableButton.setText("Restore");
		restoreTableButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		restoreTableButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logTableViewer.restore();
			}
		});

		SplitButton saveAsButton = new SplitButton(buttonLeftComposite, SWT.NONE);
		saveAsButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		saveAsButton.setText("Save");
		createSaveMenu(saveAsButton.getMenu());

		Composite buttonCenterComposite = new Composite(buttonBarComposite, SWT.NONE);
		buttonCenterComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Composite buttonRightComposite = new Composite(buttonBarComposite, SWT.NONE);
		buttonRightComposite.setLayout(new GridLayout(2, true));
		
		final Button showExceptionButton = new Button(buttonRightComposite, SWT.PUSH);
		showExceptionButton.setText("Show Exception");
		showExceptionButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		showExceptionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<RCPLogEntry> selectedEntries = logTableViewer.getSelectedLogEntries();
				
				for( RCPLogEntry selectedEntry : selectedEntries ) {
					if( selectedEntry.hasThrowable() ) {
						new ExceptionWindow(selectedEntry.getThrowable());
						return;
					}
				}
			}
		});
		showExceptionButton.setEnabled(hasOneThrowable(logTableViewer.getSelectedLogEntries()));
		
		Button showDetailsButton = new Button( buttonRightComposite, SWT.PUSH);
		showDetailsButton.setText("Details...");
		showDetailsButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		showDetailsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<RCPLogEntry> selectedEntries = logTableViewer.getSelectedLogEntries();
				
				for( RCPLogEntry selectedEntry : selectedEntries ) {
					LogEntryDetailsWindow.show(selectedEntry);
					// show only the first one
					return;
				}
			}
		});
		
		logTableViewer.addListener(new ILogTableListener() {
			@Override
			public void selectionChanged(LogTable table, List<RCPLogEntry> selection) {
				showExceptionButton.setEnabled(hasOneThrowable(selection));
			}
		});
	}
	
	private static boolean hasOneThrowable(List<RCPLogEntry> selection) {
		for( RCPLogEntry selectedEntry : selection ) {
			if( selectedEntry.hasThrowable() ) {
				return true;
			}
		}
		
		return false;
	}
	
	private void createSaveMenu(Menu menu) {
		LogSaverRegistry registry = RCPLoggingPlugIn.getLogSaverRegistry();
		Collection<String> saverNames = registry.getRegisteredSaverNames();

		if (saverNames.isEmpty()) {
			MenuItem item = new MenuItem(menu, SWT.PUSH);
			item.setText("<none available>");
			return;
		}

		for (String saverName : saverNames) {
			Optional<? extends ILogSaver> optSaver = registry.createLogSaverInstance(saverName);
			
			if (optSaver.isPresent()) {
				final ILogSaver saver = optSaver.get();

				new SaveMenuItem(menu, saverName, saver.getFilenameExtension()) {
					@Override
					protected void doSave(String file) {
						List<RCPLogEntry> entriesToSave = determineLogEntriesToSave();
						if( !entriesToSave.isEmpty() ) {
							try {
								saver.save(file, entriesToSave);
							} catch( IOException e ) {
								new ExceptionWindow("Could not save the log to the file '" + file + "'", e);
							}
						}
					}

					@Override
					protected String getFileName() {
						return determineFilename();
					}
				};
			}
		}
	}

	private List<RCPLogEntry> determineLogEntriesToSave() {
		return logTableViewer.getShownLogEntries();
	}
	
	private String determineFilename() {
		if (filterText != null) {
			String filterString = filterText.getText();
			String levelString = levelCombo.getText();
			if (!Strings.isNullOrEmpty(filterString)) {
				return "LOG_" + filterString.trim() + "_" + levelString;
			}
			return "LOG_" + levelString;
		}

		return "AllLogs";
	}

	private void createSettingsArea(Composite viewComposite) {
		Composite settingsComposite = new Composite(viewComposite, SWT.NONE);
		settingsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		settingsComposite.setLayout(new GridLayout(2, true));

		createLevelArea(settingsComposite);
		createFilterArea(settingsComposite);
	}

	private void createLevelArea(Composite settingsComposite) {
		Composite levelChoiceComposite = new Composite(settingsComposite, SWT.NONE);
		levelChoiceComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		levelChoiceComposite.setLayout(new GridLayout(2, false));

		Label levelLabel = new Label(levelChoiceComposite, SWT.NONE);
		levelLabel.setText("Level");

		levelCombo = new Combo(levelChoiceComposite, SWT.BORDER | SWT.READ_ONLY);
		levelCombo.setItems(LEVELS);
		levelCombo.select(0);
		levelCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		levelCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				updateFilter();
			}
		});
	}

	private void createFilterArea(Composite settingsComposite) {
		Composite filterComposite = new Composite(settingsComposite, SWT.NONE);
		filterComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		filterComposite.setLayout(new GridLayout(3, false));

		Label filterLabel = new Label(filterComposite, SWT.NONE);
		filterLabel.setText("Filter");

		filterText = new Text(filterComposite, SWT.BORDER | SWT.SINGLE);
		filterText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				updateFilter();
			}
		});
		filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button clearFilterButton = new Button(filterComposite, SWT.PUSH);
		clearFilterButton.setText("Clear"); // TODO: replace with image
		clearFilterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				filterText.setText("");
			}
		});
	}

	private void updateFilter() {
		if (!tab.isDisposed()) {
			String filterString = filterText.getText();
			String levelString = levelCombo.getText();

			if (!Strings.isNullOrEmpty(filterString)) {
				logTableViewer.setFilter(new LevelAndTextRCPLogFilter(levelString, filterString));
			} else {
				logTableViewer.setFilter(new LevelRCPLogFilter(levelString));
			}

			tab.setText("[" + levelString + "] " + filterString);
		}
	}

	public void setFocus() {
		logTableViewer.setFocus();
	}

	public void dispose() {
		logTableViewer.dispose();
	}

	public void resetFilters() {
		if (!tab.isDisposed()) {
			levelCombo.select(0);
			filterText.setText("");
		}
	}
}
