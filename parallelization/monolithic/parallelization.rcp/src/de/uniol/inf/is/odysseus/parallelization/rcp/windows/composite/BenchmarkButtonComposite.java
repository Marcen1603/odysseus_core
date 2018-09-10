/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow.ParalleizationBenchmarkerPage;

/**
 * composite for the buttons on the bottom of the benchmarker. The buttons and
 * the functions differ based on the current page
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkButtonComposite extends AbstractBenchmarkComposite {

	private static final String CANCEL_BUTTON_TEXT = "Cancel";

	private ParallelizationBenchmarkerWindow window;
	private Button startButton;
	private BenchmarkDataHandler data;

	private Composite buttonComposite;

	public BenchmarkButtonComposite(Composite parent, int style,
			final ParallelizationBenchmarkerWindow window,
			ParalleizationBenchmarkerPage currentPage, UUID benchmarkProcessId,
			String parameter) {
		super(parent, style);
		this.window = window;

		if (benchmarkProcessId != null) {
			data = BenchmarkDataHandler.getExistingInstance(benchmarkProcessId);
		}

		this.setLayout(new GridLayout(1, false));
		this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create a horizontal separator
		Label separator = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.BEGINNING));
		buttonComposite.setLayout(new GridLayout(3, false));

		createButtonsBasedOnPage(currentPage, parameter);

		parent.pack();
		parent.setVisible(true);
	}

	/**
	 * selectes the method for creating buttons based on the current page
	 * 
	 * @param currentPage
	 * @param parameter
	 */
	private void createButtonsBasedOnPage(
			ParalleizationBenchmarkerPage currentPage, String parameter) {
		switch (currentPage) {
		case START:
			createButtonsForStartPage(parameter);
			break;
		case CONFIG:
			createButtonsForConfigPage(parameter);
			break;
		case ANALYSE:
			createButtonsForAnalysePage(parameter);
			break;
		case ANALYSE_FINISHED:
			createButtonsForAnalyseFinishedPage(parameter);
			break;
		default:
			break;
		}
	}

	/**
	 * start page has only cancel button
	 * 
	 * @param parameter
	 */
	private void createButtonsForStartPage(String parameter) {
		createCancelButton(CANCEL_BUTTON_TEXT);
	}

	/**
	 * config page has cancel and start button
	 * 
	 * @param parameter
	 */
	private void createButtonsForConfigPage(String parameter) {
		createCancelButton(CANCEL_BUTTON_TEXT);

		startButton = new Button(buttonComposite, SWT.PUSH);
		startButton.setText("Start Analyse");
		startButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final BenchmarkConfigureComposite configureComposite = window
						.getConfigureComposite();
				// validate and prepare values from UI
				boolean successful = configureComposite
						.prepareParallelizationAnalysis();
				// if validation is successful, do analysis
				if (successful) {
					window.startAnalysis();
				}
			}
		});
	}

	/**
	 * analysis page has while analysis is running only cancel button
	 * 
	 * @param parameter
	 */
	private void createButtonsForAnalysePage(String parameter) {
		createCancelButton(CANCEL_BUTTON_TEXT);
	}

	/**
	 * if analysis is done , we have an done button and a button for copy to
	 * clipboard
	 * 
	 * @param parameter
	 */
	private void createButtonsForAnalyseFinishedPage(final String parameter) {
		// cancel button is now named done
		createCancelButton("Done");

		// copy to clipboard
		final Clipboard cb = new Clipboard(window.getWindow().getDisplay());
		Button copyClipboardButton = new Button(buttonComposite, SWT.PUSH);
		copyClipboardButton.setText("Copy to Clipboard");
		copyClipboardButton
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		copyClipboardButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] { parameter },
						new Transfer[] { textTransfer });
			}
		});

		// change file button. the button works not as it is
		// expected (-> invisible)
		Button changeFileButton = new Button(buttonComposite, SWT.PUSH);
		changeFileButton.setVisible(false);
		changeFileButton.setText("Change file");
		changeFileButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		changeFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						StringBuilder builder = new StringBuilder();
						List<String> queryStringArray = data
								.getBenchmarkInitializationResult()
								.getQueryStringArray();

						for (String queryString : queryStringArray) {
							builder.append(queryString);
							if (queryString.startsWith("#PARSER")) {
								builder.append(parameter);
							}
						}
						ByteArrayInputStream in = new ByteArrayInputStream(
								builder.toString().getBytes());

						IFile queryFile = data
								.getBenchmarkInitializationResult()
								.getQueryFile();
						try {
							queryFile.setContents(in, true, true, null);
							if (!queryFile.isSynchronized(IResource.DEPTH_ZERO)) {
								queryFile.refreshLocal(IResource.DEPTH_ZERO,
										null);
							}
						} catch (CoreException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		});
	}

	/**
	 * creates a cancel button (stops all running threads and disposes
	 * benchmarker window)
	 * 
	 * @param label
	 */
	private void createCancelButton(String label) {
		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(label);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// stop threads if benchmarker is closed
				if (window.getInitializeQueryThread() != null) {
					window.getInitializeQueryThread().interrupt();
				}
				if (window.getBenchmarkMainThread() != null) {
					window.getBenchmarkMainThread().interrupt();
				}
				if (!window.getWindow().isDisposed()) {
					window.getWindow().dispose();
				}
			}
		});
	}
}
