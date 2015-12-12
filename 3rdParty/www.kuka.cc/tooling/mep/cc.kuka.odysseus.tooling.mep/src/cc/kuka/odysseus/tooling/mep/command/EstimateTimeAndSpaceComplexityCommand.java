/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.tooling.mep.command;

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

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
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
						e.printStackTrace();
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

		final Map<String, List<IFunction<?>>> functions = new HashMap<>();
		SubMonitor subMonitor = SubMonitor.convert(monitor, functionSignatures.size());
		subMonitor.setTaskName("Collecting functions"); //$NON-NLS-1$

		for (final FunctionSignature functionSignature : functionSignatures) {
			final IFunction<?> function = MEP.getFunction(functionSignature);
			final String packageName = function.getClass().getPackage().getName();
			if (!functions.containsKey(packageName)) {
				functions.put(packageName, new ArrayList<IFunction<?>>());
			}
			functions.get(packageName).add(function);
		}
		final List<String> packages = new ArrayList<>(functions.keySet());
		Collections.sort(packages);

		Map<IFunction<?>, double[]> timeAndSpace = new HashMap<>();
		int work = 0;
		for (final String packageName : packages) {
			if (packageName.startsWith(filter)) {
				for (final IFunction<?> function : functions.get(packageName)) {
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
			for (Entry<IFunction<?>, double[]> entry : timeAndSpace.entrySet()) {
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
		if (p <= 0.2) {
			return (int) (p * 10);
		} else if (p <= 0.5) {
			return (int) (Math.log(p * Math.exp(5.0) + 1.0));
		} else {
			return (int) (Math.log(p * Math.exp(9.0) + 1.0));
		}
	}

	private static double[] generateFunctionResults(final IFunction<?> function, final Object[][] values) {
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
				final IExpression[] arguments = new IExpression[function.getArity()];
				for (int i = 0; i < function.getArity(); i++) {
					arguments[i] = new Constant<>(value[i], function.getAcceptedTypes(i)[0]);
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
						try {
							for (int i = 0; i < runs; i++) {
								@SuppressWarnings("unused")
								Object result = function.getValue();
							}
						} catch (final Throwable e) {
							EstimateTimeAndSpaceComplexityCommand.LOG.debug("Function {} with input {}", //$NON-NLS-1$
									function.getSymbol(), Arrays.toString(value));
							EstimateTimeAndSpaceComplexityCommand.LOG.error(e.getMessage(), e);
							return null;
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
			this.txtPath.setText(root.resolve("mep_functions.csv").toString()); //$NON-NLS-1$

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