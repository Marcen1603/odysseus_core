/*******************************************************************************
 * Copyright 2016 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.developer.mep.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.mep.MEP;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class EstimateTimeAndSpaceComplexityCommand extends AbstractHandler {
	static final Logger LOG = LoggerFactory.getLogger(EstimateTimeAndSpaceComplexityCommand.class);
	private static final Runtime RUNTIME = Runtime.getRuntime();

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		FilterInputDialog dialog = new FilterInputDialog(Display.getDefault().getActiveShell());
		dialog.create();
		if (dialog.open() == Window.OK) {
			final Path root = Paths.get(dialog.getPath());
			final String filter = dialog.getFilter();
			Job job = new Job("Estimate Time and Space Complexity") { //$NON-NLS-1$
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						buildMEPFunctions(root, filter, monitor); // $NON-NLS-1$
					} catch (final Throwable e) {
						EstimateTimeAndSpaceComplexityCommand.LOG.error(e.getMessage(), e);
					}
					syncWithUi();
					return Status.OK_STATUS;
				}

			};
			job.setUser(true);
			job.schedule();
		}
		return null;
	}

	void syncWithUi() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(),
						"Time and Space Complexity estimated", //$NON-NLS-1$
						"All available functions have been measured."); //$NON-NLS-1$
			}
		});
	}

	static void buildMEPFunctions(final Path root, final String filter, IProgressMonitor monitor) throws IOException {

		final List<FunctionSignature> functionSignatures = new ArrayList<>(MEP.getFunctions());
		Collections.sort(functionSignatures, new Comparator<FunctionSignature>() {

			@Override
			public int compare(final FunctionSignature o1, final FunctionSignature o2) {
				return o1.getSymbol().compareTo(o2.getSymbol());
			}

		});

		final Map<String, List<IMepFunction<?>>> functions = new HashMap<>();
		SubMonitor subMonitor = SubMonitor.convert(monitor, functionSignatures.size());
		subMonitor.setTaskName("Collecting functions"); //$NON-NLS-1$

		for (final FunctionSignature functionSignature : functionSignatures) {
			final IMepFunction<?> function = MEP.getFunction(functionSignature);
			final String packageName = function.getClass().getPackage().getName();
			if (!functions.containsKey(packageName)) {
				functions.put(packageName, new ArrayList<IMepFunction<?>>());
			}
			functions.get(packageName).add(function);
		}
		final List<String> packages = new ArrayList<>(functions.keySet());
		Collections.sort(packages);

		Map<IMepFunction<?>, double[]> timeAndSpace = new HashMap<>();
		int work = 0;
		for (final String packageName : packages) {
			if (packageName.startsWith(filter)) {
				for (final IMepFunction<?> function : functions.get(packageName)) {
					subMonitor.setTaskName("Function: " + function.getSymbol() + "()"); //$NON-NLS-1$ //$NON-NLS-2$
					final Object[][] testValues = ParameterGenerator.getFunctionValues(function);
					timeAndSpace.put(function,
							EstimateTimeAndSpaceComplexityCommand.generateFunctionResults(function, testValues));
					if (subMonitor.isCanceled()) {
						return;
					}
				}
			}
			work++;
			subMonitor.worked(work);
		}

		subMonitor.setTaskName("Estimating Time and Space scores"); //$NON-NLS-1$

		double minTime = Double.MAX_VALUE;
		double maxTime = Double.MIN_VALUE;
		double minSpace = Double.MAX_VALUE;
		double maxSpace = Double.MIN_VALUE;

		for (double[] value : timeAndSpace.values()) {
			double time = value[0];
			double space = value[1];
			if (!Double.isNaN(time)) {
				minTime = Math.min(minTime, Math.max(0, time));
				maxTime = Math.max(maxTime, Math.max(Double.MIN_NORMAL, time));
			}
			if (!Double.isNaN(space)) {
				minSpace = Math.min(minSpace, Math.max(0, space));
				maxSpace = Math.max(maxSpace, Math.max(Double.MIN_NORMAL, space));
			}
		}

		root.getParent().toFile().mkdirs();
		final File output = root.toFile(); // $NON-NLS-1$
		output.createNewFile();
		try (BufferedWriter out = new BufferedWriter(new FileWriter(output))) {
			out.write("Symbol, Class, Time Score, Space Score, Time (ns), Space (byte)\n"); //$NON-NLS-1$
			for (Entry<IMepFunction<?>, double[]> entry : timeAndSpace.entrySet()) {
				try {
					int time;
					if (!Double.isNaN(entry.getValue()[0])) {
						time = (normalize(entry.getValue()[0], minTime, maxTime));
					} else {
						time = 9;
					}
					int space;
					if (!Double.isNaN(entry.getValue()[1])) {
						space = (normalize(entry.getValue()[1], minSpace, maxSpace));
					} else {
						space = 9;
					}

					out.write(
							entry.getKey().getSymbol() + ", " + entry.getKey().getClass().getName() + " ," + time + " ," //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									+ space + ", " + entry.getValue()[0] + ", " + entry.getValue()[1] + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				} catch (Throwable t) {
					LOG.error(t.getMessage(), t);
				}
			}
		}
	}

	private static int normalize(double value, double min, double max) {
		double p = (value - min) / (max - min);
		double b = 1.0;
		for (int i = 9; i >= 0; i--) {
			b = 2.0 / 3.0 * b;
			if (p >= b) {
				return i;
			}
		}
		return 0;
	}

	private static double[] generateFunctionResults(final IMepFunction<?> function, final Object[][] values) {
		EstimateTimeAndSpaceComplexityCommand.LOG.info("Generated cases for {}", function.getSymbol()); //$NON-NLS-1$
		// RUNTIME.gc();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			EstimateTimeAndSpaceComplexityCommand.LOG.error(e.getMessage(), e);
		}
		final ExecutorService executor = Executors.newFixedThreadPool(1);
		try {
			final List<Future<Long>> futures = new ArrayList<>();
			long free = RUNTIME.freeMemory();
			for (final Object[] value : values) {
				@SuppressWarnings("rawtypes")
				final IMepExpression[] arguments = new IMepExpression[function.getArity()];
				for (int i = 0; i < function.getArity(); i++) {
					arguments[i] = MEP.createConstant(value[i], function.getAcceptedTypes(i)[0]);
				}
				function.setArguments(arguments);
				futures.add(executor.submit(new Callable<Long>() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public Long call() throws Exception {
						int runs = 1000;
						long time = System.nanoTime();
						Throwable exception = null;
						for (int i = 0; i < runs; i++) {
							try {
								@SuppressWarnings("unused")
								Object result = function.getValue();
							} catch (final Throwable e) {
								exception = e;
							}
						}
						if (exception != null) {
							EstimateTimeAndSpaceComplexityCommand.LOG.debug("Function {} with input {}", //$NON-NLS-1$
									function.getSymbol(), Arrays.toString(value));
							EstimateTimeAndSpaceComplexityCommand.LOG.error(exception.getMessage(), exception);
						}
						return Long.valueOf((System.nanoTime() - time) / runs);
					}
				}));
			}
			double space = free - RUNTIME.freeMemory();
			if (space < 0.0) {
				free = (long) (free - space);
				space = 0.0;
			}
			double time = 0.0;
			for (final Future<Long> future : futures) {
				try {
					Thread.sleep(0, 100);
				} catch (InterruptedException e) {
					EstimateTimeAndSpaceComplexityCommand.LOG.error(e.getMessage(), e);
				}
				try {
					if (!Double.isNaN(space)) {
						space = Math.max(space, free - RUNTIME.freeMemory());
					}
					Long result = future.get(50, TimeUnit.MILLISECONDS);
					if (result != null) {
						if (!Double.isNaN(time)) {
							time += result.doubleValue();
						}
					} else {
						space = Double.NaN;
						time = Double.NaN;
					}
				} catch (final Throwable e) {
					EstimateTimeAndSpaceComplexityCommand.LOG.debug("Timeout for function {}", function.getSymbol()); //$NON-NLS-1$
					EstimateTimeAndSpaceComplexityCommand.LOG.error(e.getMessage(), e);
					time = Double.NaN;
				}
			}
			if (!Double.isNaN(time)) {
				time = time / values.length;
			}

			return new double[] { time, space };
		} finally {
			executor.shutdownNow();
		}
	}

	class FilterInputDialog extends TitleAreaDialog {
		Text txtFilter;
		Text txtPath;
		String filter;
		String path;

		/**
		 * @param parent
		 */
		public FilterInputDialog(Shell parent) {
			super(parent);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void create() {
			super.create();
			setTitle("Package filter"); //$NON-NLS-1$
			setMessage("Set the package filter", IMessageProvider.INFORMATION); //$NON-NLS-1$
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite area = (Composite) super.createDialogArea(parent);
			Composite container = new Composite(area, SWT.NONE);
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			GridLayout layout = new GridLayout(2, false);
			container.setLayout(layout);

			Label lbtPath = new Label(container, SWT.NONE);
			lbtPath.setText("Path:"); //$NON-NLS-1$

			GridData dataPath = new GridData();
			dataPath.grabExcessHorizontalSpace = true;
			dataPath.horizontalAlignment = GridData.FILL;

			this.txtPath = new Text(container, SWT.BORDER);
			this.txtPath.setLayoutData(dataPath);
			final Path root = Paths.get(System.getProperty("user.home")); //$NON-NLS-1$
			this.txtPath.setText(root.resolve("report.csv").toString()); //$NON-NLS-1$

			Label lbtFilter = new Label(container, SWT.NONE);
			lbtFilter.setText("Filter:"); //$NON-NLS-1$

			GridData dataFilter = new GridData();
			dataFilter.grabExcessHorizontalSpace = true;
			dataFilter.horizontalAlignment = GridData.FILL;

			this.txtFilter = new Text(container, SWT.BORDER);
			this.txtFilter.setLayoutData(dataFilter);

			return area;
		}

		@Override
		protected boolean isResizable() {
			return true;
		}

		private void saveInput() {
			this.path = this.txtPath.getText();
			this.filter = this.txtFilter.getText();
		}

		@Override
		protected void okPressed() {
			saveInput();
			super.okPressed();
		}

		public String getFilter() {
			return this.filter;
		}

		public String getPath() {
			return this.path;
		}
	}

}