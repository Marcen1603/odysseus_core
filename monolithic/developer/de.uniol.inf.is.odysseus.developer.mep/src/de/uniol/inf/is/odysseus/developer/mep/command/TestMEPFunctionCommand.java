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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.mep.MEP;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class TestMEPFunctionCommand extends AbstractHandler {
	static final Logger LOG = LoggerFactory.getLogger(TestMEPFunctionCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final List<FunctionSignature> functionSignatures = new ArrayList<>(MEP.getFunctions());
		Collections.sort(functionSignatures, new Comparator<FunctionSignature>() {

			@Override
			public int compare(final FunctionSignature o1, final FunctionSignature o2) {
				return o1.getSymbol().compareTo(o2.getSymbol());
			}

		});
		final Map<String, IMepFunction<?>> functions = new TreeMap<>();
		for (final FunctionSignature functionSignature : functionSignatures) {
			final IMepFunction<?> function = MEP.getFunction(functionSignature);
			final String className = function.getClass().getName();
			functions.put(className, function);
		}

		TestMEPFunctionDialog dialog = new TestMEPFunctionDialog(Display.getDefault().getActiveShell(), functions);
		dialog.create();
		if (dialog.open() == Window.OK) {
			final Path root = Paths.get(dialog.getPath());
			final IMepFunction<?> function = dialog.getFunction();

			Job job = new Job("Test MEP function") { //$NON-NLS-1$
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						testMEPFunction(root, function, monitor); // $NON-NLS-1$
					} catch (final Throwable e) {
						e.printStackTrace();
						EstimateTimeAndSpaceComplexityCommand.LOG.error(e.getMessage(), e);
					}
					syncWithUi(root);
					return Status.OK_STATUS;
				}

			};
			job.setUser(true);
			job.schedule();

		}
		return null;
	}

	void syncWithUi(Path root) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "MEP Function tested", //$NON-NLS-1$
						"See report for results."); //$NON-NLS-1$
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(root.toUri());
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

				try {
					IDE.openEditorOnFileStore(page, fileStore);
				} catch (PartInitException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		});
	}

	static void testMEPFunction(final Path root, final IMepFunction<?> function, IProgressMonitor monitor)
			throws IOException, JSONException {
		final Object[][] values = ParameterGenerator.getFunctionValues(function);

		SubMonitor subMonitor = SubMonitor.convert(monitor, values.length);
		subMonitor.setTaskName("Testing: " + function.getSymbol() + "()"); //$NON-NLS-1$ //$NON-NLS-2$
		List<Throwable> exceptions = new ArrayList<>();

		root.getParent().toFile().mkdirs();
		final File output = root.toFile(); // $NON-NLS-1$
		output.createNewFile();
		try (BufferedWriter out = new BufferedWriter(new FileWriter(output))) {

			JSONObject report = new JSONObject();
			report.append("symbol", function.getSymbol()); //$NON-NLS-1$
			report.append("class", function.getClass()); //$NON-NLS-1$
			report.append("type", function.getReturnType()); //$NON-NLS-1$
			JSONArray acceptedTypes = new JSONArray();
			for (int i = 0; i < function.getArity(); i++) {
				acceptedTypes.put(new JSONArray(function.getAcceptedTypes(i)));
			}
			report.append("accpetedTypes", acceptedTypes); //$NON-NLS-1$

			JSONArray tests = new JSONArray();
			int work = 0;
			for (final Object[] value : values) {
				JSONObject test = new JSONObject();
				Throwable exception = generateFunctionResult(function, value);

				JSONArray parameters = new JSONArray();
				for (int i = 0; i < function.getArity(); i++) {
					JSONObject parameter = new JSONObject();
					parameter.put("value", value[i] != null ? value[i] : "null"); //$NON-NLS-1$ //$NON-NLS-2$
					parameter.put("type", value[i] != null ? value[i].getClass().getName() : "NULL"); //$NON-NLS-1$ //$NON-NLS-2$
					parameters.put(parameter);
				}
				test.put("parameters", parameters); //$NON-NLS-1$
				String argument = Arrays.toString(value);
				test.put("call", //$NON-NLS-1$
						String.format("%s(%s)", function.getSymbol(), argument.substring(1, argument.length() - 1))); //$NON-NLS-1$

				if (exception != null) {
					StringWriter stacktrace = new StringWriter();
					exception.printStackTrace(new PrintWriter(stacktrace));
					test.put("status", 1); //$NON-NLS-1$
					test.put("exception", exception.getMessage()); //$NON-NLS-1$
					test.put("stacktrace", stacktrace.toString()); //$NON-NLS-1$
				} else {
					test.put("status", 0); //$NON-NLS-1$
				}
				tests.put(test);
				exceptions.add(exception);
				if (subMonitor.isCanceled()) {
					return;
				}
				work++;
				subMonitor.worked(work);
			}
			report.put("tests", tests); //$NON-NLS-1$
			out.write(report.toString(4));
		}
	}

	private static Throwable generateFunctionResult(final IMepFunction<?> function, final Object[] value) {
		LOG.info("Generated cases for {}", function.getSymbol()); //$NON-NLS-1$
		final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
		try {

			@SuppressWarnings("rawtypes")
			final IMepExpression[] arguments = new IMepExpression[function.getArity()];
			for (int i = 0; i < function.getArity(); i++) {
				arguments[i] = MEP.createConstant(value[i], function.getAcceptedTypes(i)[0]);
			}
			function.setArguments(arguments);
			final Future<Throwable> future = executor.submit(new Callable<Throwable>() {
				/**
				 * {@inheritDoc}
				 */
				@Override
				public Throwable call() throws Exception {
					Throwable exception = null;
					try {
						function.getValue();
					} catch (final Throwable e) {
						exception = e;
					}

					if (exception != null) {
						EstimateTimeAndSpaceComplexityCommand.LOG.debug("Function {} with input {}", //$NON-NLS-1$
								function.getSymbol(), Arrays.toString(value));
						EstimateTimeAndSpaceComplexityCommand.LOG.error(exception.getMessage(), exception);
					}
					return exception;
				}
			});

			Throwable result;
			try {
				result = future.get(50, TimeUnit.MILLISECONDS);
			} catch (final Throwable e) {
				EstimateTimeAndSpaceComplexityCommand.LOG.debug("Timeout for function {}", //$NON-NLS-1$
						function.getSymbol());
				EstimateTimeAndSpaceComplexityCommand.LOG.error(e.getMessage(), e);
				result = e;
			}

			return result;
		} finally {
			executor.shutdownNow();
		}
	}

	class TestMEPFunctionDialog extends TitleAreaDialog {
		Map<String, IMepFunction<?>> functions;
		Text txtPath;

		Combo cbClass;
		String path;
		IMepFunction<?> function;
		String cls;

		/**
		 * @param parent
		 * @param functions
		 */
		public TestMEPFunctionDialog(Shell parent, Map<String, IMepFunction<?>> functions) {
			super(parent);
			this.functions = functions;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void create() {
			super.create();
			setTitle("MEP Function Test"); //$NON-NLS-1$
			setMessage("Select MEP function", IMessageProvider.INFORMATION); //$NON-NLS-1$
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
			this.txtPath.setText(root.resolve("report.json").toString()); //$NON-NLS-1$

			Label lbtClass = new Label(container, SWT.NONE);
			lbtClass.setText("Class:"); //$NON-NLS-1$

			GridData dataClass = new GridData();
			dataClass.grabExcessHorizontalSpace = true;
			dataClass.horizontalAlignment = GridData.FILL;

			this.cbClass = new Combo(container, SWT.BORDER);
			this.cbClass.setLayoutData(dataClass);
			for (String className : this.functions.keySet()) {
				this.cbClass.add(className);
				this.cbClass.setData(className, this.functions.get(className));
			}
			this.cbClass.select(0);
			return area;
		}

		@Override
		protected boolean isResizable() {
			return true;
		}

		private void saveInput() {
			this.path = this.txtPath.getText();
			this.cls = this.cbClass.getItem(this.cbClass.getSelectionIndex());
			this.function = (IMepFunction<?>) this.cbClass.getData(this.cls);
		}

		@Override
		protected void okPressed() {
			saveInput();
			super.okPressed();
		}

		public String getClassName() {
			return this.cls;
		}

		public String getPath() {
			return this.path;
		}

		public IMepFunction<?> getFunction() {
			return this.function;
		}
	}
}
