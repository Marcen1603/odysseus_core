/**********************************************************************************
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.exception;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class ExceptionErrorDialog extends ErrorDialog {
	private static final int BUGREPORT_ID = IDialogConstants.CLIENT_ID + 1;
	private final Throwable exception;

	public static void open(final IStatus status, final Throwable exception) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				final Shell shell;
				if (PlatformUI.getWorkbench() != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
						&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null) {
					shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				} else {
					shell = new Shell();
				}

				final ErrorDialog dialog = new ExceptionErrorDialog(shell, status, exception);
				dialog.open();

			}

		});
	}

	/**
	 * Class constructor.
	 *
	 * @param parentShell
	 * @param status
	 */
	public ExceptionErrorDialog(final Shell parentShell, final IStatus status, final Throwable exception) {
		this(parentShell, status, 0xFFFF, exception);
	}

	/**
	 * Class constructor.
	 *
	 * @param parentShell
	 * @param status
	 * @param displayMask
	 */
	public ExceptionErrorDialog(final Shell parentShell, final IStatus status, final int displayMask,
			final Throwable exception) {
		super(parentShell, null, null, status, displayMask);
		this.exception = exception;
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		super.createButtonsForButtonBar(parent);
		this.createSendBugReportButton(parent);
	}

	protected void createSendBugReportButton(final Composite parent) {
		this.createButton(parent, IDialogConstants.CLIENT_ID + 1, OdysseusNLS.SendBugReport, false);
	}

	@Override
	protected void buttonPressed(final int id) {
		if (id == ExceptionErrorDialog.BUGREPORT_ID) {
			final BugReport report = new BugReport(this.exception);
			report.openEditor();
		} else {
			super.buttonPressed(id);
		}
	}
}
