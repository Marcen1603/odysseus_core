package de.uniol.inf.is.odysseus.rcp.logging.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class LoggingView extends ViewPart {

	private static LoggingView instance;

	private TabFolder tabs;
	private Map<TabItem, LogPresenter> presenterMap = Maps.newHashMap();

	@Override
	public void createPartControl(Composite parent) {
		tabs = new TabFolder(parent, SWT.BORDER);

		// create all-log tab
		createNewTab("All logs", false);

		parent.layout();
		instance = this;
	}

	private void createNewTab(String title, boolean filtered) {
		TabItem tabItem = new TabItem(tabs, SWT.NONE);
		tabItem.setText(title);

		Composite tabComposite = new Composite(tabs, SWT.NONE);
		tabComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		LogPresenter presenter = new LogPresenter(tabComposite, tabItem, filtered);

		tabItem.setControl(tabComposite);

		presenterMap.put(tabItem, presenter);
	}

	@Override
	public void setFocus() {
		LogPresenter presenter = determineSelectedLogPresenter();
		presenter.setFocus();
	}

	@Override
	public void dispose() {
		super.dispose();

		instance = null;
	}

	public static Optional<LoggingView> getInstance() {
		return Optional.fromNullable(instance);
	}

	public boolean isCurrentTabFilterable() {
		return tabs.getSelectionIndex() != 0;
	}

	public void addNewFilterTab() {
		createNewTab("Logs", true);

		tabs.setSelection(tabs.getItemCount() - 1);

		tabs.layout();
	}

	public void removeCurrentTab() {
		if (!tabs.isDisposed()) {
			int selectedTabIndex = tabs.getSelectionIndex();
			if (selectedTabIndex > 0) { // do not remove first one!
				LogPresenter presenter = determineSelectedLogPresenter();
				presenter.dispose();

				TabItem tabItem = determineSelectedTabItem();
				tabItem.dispose();
			}
		}
	}

	private LogPresenter determineSelectedLogPresenter() {
		TabItem tabItem = determineSelectedTabItem();
		return presenterMap.get(tabItem);
	}

	private TabItem determineSelectedTabItem() {
		return tabs.getItem(tabs.getSelectionIndex());
	}
}
