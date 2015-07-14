package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
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
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.table.StrategySelectionHelper;

public class BenchmarkButtonComposite extends AbstractBenchmarkComposite {

	private static final String CANCEL_BUTTON_TEXT = "Cancel";

	private ParallelizationBenchmarkerWindow window;
	private Button startButton;
	private BenchmarkDataHandler data;

	private Composite buttonComposite;
	
	
	public BenchmarkButtonComposite(Composite parent, int style,
			final ParallelizationBenchmarkerWindow window,
			ParalleizationBenchmarkerPage currentPage, UUID benchmarkProcessId, String parameter) {
		super(parent, style);
		this.window = window;

		if (benchmarkProcessId != null){
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

	private void createButtonsForStartPage(String parameter) {
		createCancelButton(CANCEL_BUTTON_TEXT);
	}

	private void createButtonsForConfigPage(String parameter) {
		createCancelButton(CANCEL_BUTTON_TEXT);

		startButton = new Button(buttonComposite, SWT.PUSH);
		startButton.setText("Start Analyse");
		startButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final BenchmarkConfigureComposite configureComposite = window.getConfigureComposite();
				StrategySelectionHelper strategySelectionHelper = configureComposite.getStrategySelectionTableViewer();
				if (strategySelectionHelper != null){
					
				}
				boolean successful = configureComposite.prepareParallelizationAnalysis();
				if (successful){
					window.startAnalysis();					
				}
			}
		});
	}

	private void createButtonsForAnalysePage(String parameter) {
		createCancelButton(CANCEL_BUTTON_TEXT);
	}

	private void createButtonsForAnalyseFinishedPage(final String parameter) {
		createCancelButton("Done");

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

		Button changeFileButton = new Button(buttonComposite, SWT.PUSH);
		changeFileButton.setText("Change file");
		changeFileButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		changeFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						StringBuilder builder = new StringBuilder();
						List<String> queryStringArray = data.getQueryStringArray();
						
						for (String queryString : queryStringArray) {
							builder.append(queryString);
							if (queryString.startsWith("#PARSER")){
								builder.append(parameter);
							}
						}
						ByteArrayInputStream in = new 
								ByteArrayInputStream(builder.toString().getBytes());
						
						IFile queryFile = data.getQueryFile();
						try {
							queryFile.setContents(in, true, true, null);
						} catch (CoreException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		});
	}

	private void createCancelButton(String label) {
		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(label);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// stop threads if benchmarker is closed
				if (window.getInitializeQueryThread() != null){
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
