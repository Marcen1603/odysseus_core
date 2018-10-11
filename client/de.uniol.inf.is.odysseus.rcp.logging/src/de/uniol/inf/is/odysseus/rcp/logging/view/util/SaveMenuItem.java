package de.uniol.inf.is.odysseus.rcp.logging.view.util;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public abstract class SaveMenuItem {

	private MenuItem menuItem;

	public SaveMenuItem(Menu menu, String title, final String fileExtension) {
		// Preconditions.checkNotNull(menu, "Menu must not be null!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(title), "Title must not be null or empty!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(fileExtension), "fileExtension must not be null or empty!");

		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText(title);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				FileDialog dlg = new FileDialog(shell, SWT.SAVE);

				dlg.setFilterExtensions(new String[] { "*." + fileExtension });

				String fileName = getFileName();
				if (!Strings.isNullOrEmpty(fileName)) {
					dlg.setFileName(fileName + "." + fileExtension);
				}

				String file = dlg.open();
				if (!Strings.isNullOrEmpty(file)) {
					File f = new File(file);
					if (f.exists() && !MessageDialog.openConfirm(shell, "Overwrite file?", "The file '" + file + "' already exists!\nAre you sure you want to overwrite this file?")) {
						return;
					}

					doSave(file);
				}
			}

		});
	}

	protected abstract String getFileName();

	protected abstract void doSave(String file);
}
