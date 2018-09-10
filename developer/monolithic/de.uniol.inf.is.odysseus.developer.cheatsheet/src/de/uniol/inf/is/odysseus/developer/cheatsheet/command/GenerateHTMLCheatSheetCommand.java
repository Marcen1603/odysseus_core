/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.developer.cheatsheet.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.developer.cheatsheet.Activator;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.ReplacementProviderManager;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 * @version $Id: GenerateCheatSheetCommand.java | Sat Nov 29 17:11:29 2014 +0800
 *          | Christian Kuka $
 *
 */
public class GenerateHTMLCheatSheetCommand extends AbstractHandler {
	static final Logger LOG = LoggerFactory.getLogger(GenerateHTMLCheatSheetCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			//@SuppressWarnings("nls")
			@Override
			public void run() {
				final Shell shell;
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null) {
					shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				} else {
					shell = new Shell();
				}
				try {
					final FileDialog dialog = new FileDialog(shell, SWT.SAVE);

					dialog.setFilterNames(new String[] { "HTML Files (*.html)" });
					dialog.setFilterExtensions(new String[] { "*.html" });
					dialog.setText("Select file..");
					final String file = dialog.open();
					if (file != null) {
						final StringBuilder builder = new StringBuilder();
						GenerateHTMLCheatSheetCommand.build(builder);
						final Path path = new File(file).toPath();
						final Path directory = path.getParent();
						if (!Files.exists(directory)) {
							Files.createDirectory(directory);
						}
						try (FileOutputStream out = new FileOutputStream(file)) {
							try (PrintStream print = new PrintStream(out)) {
								print.println(builder.toString());
							}
						}
						final IFileStore fileStore = EFS.getLocalFileSystem().getStore(path.toUri());
						final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage();

						try {
							IDE.openEditorOnFileStore(page, fileStore);
						} catch (final PartInitException e) {
							GenerateHTMLCheatSheetCommand.LOG.error(e.getMessage(), e);
						}
					}
				} catch (final Exception e) {
					GenerateHTMLCheatSheetCommand.LOG.error(e.getMessage(), e);
				}
			}
		});
		return null;
	}

	//@SuppressWarnings("nls")
	static void build(final StringBuilder builder) {
		builder.append("<!DOCTYPE html>\n");
		builder.append("<html xmlns='http://www.w3.org/1999/xhtml'>\n\n");
		builder.append("<head>\n");
		builder.append("<meta http-equiv='content-type' content='text/html; charset=utf-8' />\n");
		builder.append("<title>Odysseus Cheat Sheet</title>\n\n");

		builder.append("<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n");
		builder.append("<meta name='viewport' content='width=device-width, initial-scale=1'>\n");
		builder.append(
				"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'>\n");
		builder.append(
				"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css' integrity='sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r' crossorigin='anonymous'>\n");
		builder.append(
				"<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js' integrity='sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS' crossorigin='anonymous'></script>\n");

		builder.append("</head>\n");
		builder.append("<body>\n");

		builder.append("<div class='container'>\n");
		builder.append("<header><h1>Odysseus Cheat Sheet</h1></header>\n");
		builder.append("<article>\n");
		GenerateHTMLCheatSheetCommand.buildPQLGrammar(builder);
		GenerateHTMLCheatSheetCommand.buildPQLOperators(builder);
		GenerateHTMLCheatSheetCommand.buildAggregationFunctions(builder);
		GenerateHTMLCheatSheetCommand.buildMEPFunctions(builder);
		GenerateHTMLCheatSheetCommand.buildMetadatas(builder);
		GenerateHTMLCheatSheetCommand.buildDatatypes(builder);
		GenerateHTMLCheatSheetCommand.buildHandlers(builder);
		GenerateHTMLCheatSheetCommand.buildScheduling(builder);
		GenerateHTMLCheatSheetCommand.buildBufferPlacementStrategies(builder);
		GenerateHTMLCheatSheetCommand.buildOdysseusScript(builder);

		GenerateHTMLCheatSheetCommand.buildSample(builder);
		builder.append("</article>\n");
		builder.append("</div>\n");

		builder.append("<footer class='footer'>\n");
		builder.append("<div class='container'>\n");
		builder.append("<p>Copyright &copy; ").append(Calendar.getInstance().get(Calendar.YEAR))
				.append(" ODYSSEUS Team</p>\n");
		builder.append(
				"<p><a href='http://odysseus.informatik.uni-oldenburg.de'>http://odysseus.informatik.uni-oldenburg.de</a><br/>\n");
		builder.append(
				"Wiki: <a href='http://wiki.odysseus.informatik.uni-oldenburg.de'>http://wiki.odysseus.informatik.uni-oldenburg.de</a><br/>\n");
		builder.append(
				"Forum: <a href='http://forum.odysseus.informatik.uni-oldenburg.de'>http://forum.odysseus.informatik.uni-oldenburg.de</a></p>\n");
		builder.append("</div>\n");
		builder.append("</footer>\n");
		builder.append("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js'></script>\n");

		builder.append("</body>\n");
		builder.append("</html>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildDatatypes(final StringBuilder builder) {
		builder.append("<div class='row'>");
		builder.append("<h2>Datatypes</h2>\n");
		builder.append("<ul class='list-inline'>\n");
		if (Activator.getExecutor() != null) {
			final List<SDFDatatype> datatypes = new ArrayList<>(
					Activator.getExecutor().getRegisteredDatatypes(Activator.getSession()));
			Collections.sort(datatypes, new Comparator<SDFDatatype>() {

				@Override
				public int compare(SDFDatatype o1, SDFDatatype o2) {
					return o1.getQualName().compareTo(o2.getQualName());
				}
			});
			for (final SDFDatatype datatype : datatypes) {
				builder.append("<li>")
						.append(GenerateHTMLCheatSheetCommand.sanitize(datatype.getQualName().toUpperCase()))
						.append("</li>\n");
			}
		}
		builder.append("</ul>\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildMetadatas(final StringBuilder builder) {
		builder.append("<div class='row'>");
		builder.append("<h2>Metadata</h2>\n");
		builder.append("<ul class='list-inline'>\n");
		if (Activator.getExecutor() != null) {
			final List<String> metadatas = new ArrayList<>(MetadataRegistry.getNames());
			Collections.sort(metadatas);
			for (final String metadata : metadatas) {
				builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(metadata.toUpperCase()))
						.append("</li>\n");
			}
		}
		builder.append("</ul>\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildScheduling(final StringBuilder builder) {
		builder.append("<div class='row'>");
		builder.append("<h2>Scheduling</h2>\n");
		GenerateHTMLCheatSheetCommand.buildSchedulers(builder);
		GenerateHTMLCheatSheetCommand.buildSchedulingStrategies(builder);
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildSchedulers(final StringBuilder builder) {
		builder.append("<h3>Schedulers</h3>\n");
		builder.append("<ul class='list-inline'>\n");
		if (Activator.getExecutor() != null) {
			final List<String> schedulers = new ArrayList<>(
					Activator.getExecutor().getRegisteredSchedulers(Activator.getSession()));
			Collections.sort(schedulers);
			for (final String scheduler : schedulers) {
				builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(scheduler.toUpperCase()))
						.append("</li>\n");
			}
		}
		builder.append("</ul>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildSchedulingStrategies(final StringBuilder builder) {
		builder.append("<h3>Scheduling Strategies</h3>\n");
		builder.append("<ul class='list-inline'>\n");
		if (Activator.getExecutor() != null) {
			final List<String> schedulingStrategies = new ArrayList<>(
					Activator.getExecutor().getRegisteredSchedulingStrategies(Activator.getSession()));
			Collections.sort(schedulingStrategies);
			for (final String schedulingStrategy : schedulingStrategies) {
				builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(schedulingStrategy.toUpperCase()))
						.append("</li>\n");
			}
		}
		builder.append("</ul>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildBufferPlacementStrategies(final StringBuilder builder) {
		builder.append("<div class='row'>");
		builder.append("<h2>Buffer Placement Strategies</h2>\n");
		builder.append("<ul class='list-inline'>\n");
		if (Activator.getExecutor() != null) {
			final List<String> bufferPlacementStrategies = new ArrayList<>(
					Activator.getExecutor().getRegisteredBufferPlacementStrategiesIDs(Activator.getSession()));
			Collections.sort(bufferPlacementStrategies);
			for (final String bufferPlacementStrategy : bufferPlacementStrategies) {
				builder.append("<li>")
						.append(GenerateHTMLCheatSheetCommand.sanitize(bufferPlacementStrategy.toUpperCase()))
						.append("</li>\n");
			}
		}
		builder.append("</ul>\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildPQLOperators(final StringBuilder builder) {
		if (Activator.getExecutor() != null) {
			final List<LogicalOperatorInformation> operators = Activator.getExecutor()
					.getOperatorInformations(Activator.getSession());

			final Map<String, List<LogicalOperatorInformation>> categories = new HashMap<>();
			for (final LogicalOperatorInformation operator : operators) {
				if (!operator.isHidden()) {
					for (final String category : operator.getCategories()) {
						if (!categories.containsKey(category)) {
							categories.put(category, new ArrayList<LogicalOperatorInformation>());
						}
						categories.get(category).add(operator);
					}
				}
			}
			final List<String> sortedCategories = new ArrayList<>(categories.keySet());
			Collections.sort(sortedCategories);
			for (final String category : sortedCategories) {
				builder.append("<div class='row'>");
				builder.append("<h2>").append(category.substring(0, 1).toUpperCase())
						.append(category.substring(1).toLowerCase()).append(" Operators</h2>\n");

				Collections.sort(categories.get(category), new Comparator<LogicalOperatorInformation>() {

					@Override
					public int compare(final LogicalOperatorInformation o1, final LogicalOperatorInformation o2) {
						return o1.getOperatorName().compareTo(o2.getOperatorName());
					}

				});
				for (final LogicalOperatorInformation operator : categories.get(category)) {
					builder.append("<h3>")
							.append(GenerateHTMLCheatSheetCommand.sanitize(operator.getOperatorName().toUpperCase()));
					if (operator.isDeprecated()) {
						builder.append(" <span class='label label-danger'>Deprecated</span>");
					}
					builder.append("</h3>\n");
					builder.append("<p>").append(GenerateHTMLCheatSheetCommand.sanitize(operator.getDoc()))
							.append("</p>\n");
					String len = "";
					for (final LogicalParameterInformation parameter : operator.getParameters()) {
						if (parameter.getName().length() > len.length()) {
							len = GenerateHTMLCheatSheetCommand.sanitize(parameter.getName());
						}
					}

					builder.append("<table class='table'>\n");
					builder.append("<tbody>\n");
					for (final LogicalParameterInformation parameter : operator.getParameters()) {
						if (!parameter.getName().equalsIgnoreCase("NAME") && !parameter.getName().equalsIgnoreCase("ID")
								&& !parameter.getName().equalsIgnoreCase("DESTINATION")) {
							if (!parameter.getDoc().equalsIgnoreCase("No description")) {
								builder.append("<tr><td>")
										.append(GenerateHTMLCheatSheetCommand.sanitize(parameter.getName()))
										.append("</td><td>")
										.append(GenerateHTMLCheatSheetCommand.sanitize(parameter.getDoc()))
										.append("</td></tr>\n");
							} else {
								builder.append("<tr><td>")
										.append(GenerateHTMLCheatSheetCommand.sanitize(parameter.getName()))
										.append("</td><td></td></tr>\n");
							}
						}
					}

					builder.append("</tbody>\n");
					builder.append("</table>\n");
				}
				builder.append("</div>");
			}
		}
	}

	//@SuppressWarnings("nls")
	private static void buildAggregationFunctions(final StringBuilder builder) {
		builder.append("<div class='row'>\n");
		builder.append("<h2>Aggregates</h2>\n");
		builder.append("<h3>Example</h3>\n");
		builder.append("<pre class='brush: pql'>\n");
		builder.append("aggregate = AGGREGATE({',\n");
		builder.append("            group_by = ['y'],\n");
		builder.append("            aggregations=[\n");
		builder.append("              ['SUM', 'x', 'sum_x', 'double']\n");
		builder.append("             ]\n");
		builder.append("            }, input)\n");
		builder.append("</pre>\n\n");

		builder.append("<h3>Functions</h3>\n");

		final List<String> functions = new ArrayList<>(AggregateFunctionBuilderRegistry.getFunctionNames(Tuple.class));
		Collections.sort(functions);
		builder.append("<table class='table  table-striped'>\n");
		for (final String function : functions) {
			builder.append("<tr><td>").append(GenerateHTMLCheatSheetCommand.sanitize(function.toUpperCase()))
					.append("</td></tr>\n");
		}
		builder.append("</table>\n\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildMEPFunctions(final StringBuilder builder) {
		builder.append("<div class='row'>\n");
		builder.append("<h2>Functions</h2>\n");
		final List<FunctionSignature> functionSignatures = new ArrayList<>(MEP.getFunctions());
		Collections.sort(functionSignatures, new Comparator<FunctionSignature>() {

			@Override
			public int compare(final FunctionSignature o1, final FunctionSignature o2) {
				return o1.getSymbol().compareTo(o2.getSymbol());
			}

		});

		final List<IMepFunction<?>> symbols = new ArrayList<>();
		final Map<String, List<IMepFunction<?>>> functions = new HashMap<>();

		for (final FunctionSignature functionSignature : functionSignatures) {
			final IMepFunction<?> function = MEP.getFunction(functionSignature);
			if (function.getSymbol().charAt(0) >= 'A' && function.getSymbol().charAt(0) <= 'Z'
					|| function.getSymbol().charAt(0) >= 'a' && function.getSymbol().charAt(0) <= 'z') {
				String packageName = function.getClass().getPackage().getName();
				final int index = packageName.lastIndexOf(".");
				packageName = packageName.substring(index + 1, index + 2).toUpperCase()
						+ packageName.substring(index + 2);
				if (!functions.containsKey(packageName)) {
					functions.put(packageName, new ArrayList<IMepFunction<?>>());
				}
				functions.get(packageName).add(function);
			} else {
				if (function.getSymbol().charAt(0) != '_') {
					symbols.add(function);
				}
			}
		}
		final List<String> packages = new ArrayList<>(functions.keySet());
		Collections.sort(packages);
		for (final String packageName : packages) {
			builder.append("<h3>").append(packageName).append("</h3>\n");
			String len = "";
			for (final IMepFunction<?> function : functions.get(packageName)) {
				if (function.getSymbol().length() > len.length()) {
					len = GenerateHTMLCheatSheetCommand.sanitize(function.getSymbol());
				}
			}
			builder.append("<table class='table table-striped'>\n");
			builder.append("<tbody>\n");
			for (final IMepFunction<?> function : functions.get(packageName)) {
				final String name = function.getSymbol();
				final String returnType = function.getReturnType().getQualName();
				final StringBuilder sb = new StringBuilder();

				for (int i = 0; i < function.getArity(); i++) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(GenerateHTMLCheatSheetCommand.concatDatatypes(function.getAcceptedTypes(i)));
				}
				final Deprecated annotation = function.getClass().getAnnotation(Deprecated.class);
				if (annotation != null) {
					builder.append("<tr><td>").append(GenerateHTMLCheatSheetCommand.sanitize(name))
							.append(" <span class='label label-danger'>Deprecated</span></td><td>")
							.append(sb.toString()).append("</td>");
				} else {
					builder.append("<tr><td>").append(GenerateHTMLCheatSheetCommand.sanitize(name)).append("</td><td>")
							.append(sb.toString()).append("</td>");
				}
				builder.append("<td>").append(GenerateHTMLCheatSheetCommand.sanitize(returnType))
						.append("</td></tr>\n");
			}
			builder.append("</tbody>\n");
			builder.append("</table>\n");
		}
		builder.append("</div>\n");
		builder.append("<div class='row'>\n");
		builder.append("<h2>Symbols</h2>\n");
		builder.append("<table class='table table-striped'>\n");
		builder.append("<tbody>\n");
		for (final IMepFunction<?> symbol : symbols) {
			final String name = symbol.getSymbol();
			final String returnType = symbol.getReturnType().getQualName();
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < symbol.getArity(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(GenerateHTMLCheatSheetCommand.concatDatatypes(symbol.getAcceptedTypes(i)));
			}

			builder.append("<tr><td>").append(GenerateHTMLCheatSheetCommand.sanitize(name)).append("</td><td>")
					.append(sb.toString()).append("</td>");
			builder.append("<td>").append(GenerateHTMLCheatSheetCommand.sanitize(returnType)).append("</td></tr>\n");
		}
		builder.append("</tbody>\n");
		builder.append("</table>\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildHandlers(final StringBuilder builder) {
		builder.append("<div class='row'>\n");
		builder.append("<h2>Handlers</h2>\n");
		GenerateHTMLCheatSheetCommand.buildDataHandlers(builder);
		GenerateHTMLCheatSheetCommand.buildProtocolHandlers(builder);
		GenerateHTMLCheatSheetCommand.buildTransportHandlers(builder);
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildDataHandlers(final StringBuilder builder) {
		builder.append("<h3>Data Handlers</h3>\n");
		final List<String> datas = new ArrayList<>(DataHandlerRegistry.instance.getHandlerNames());
		Collections.sort(datas);
		builder.append("<ul class='list-inline'>\n");
		for (final String data : datas) {
			builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(data.toUpperCase())).append("</li>\n");
		}
		builder.append("</ul>\n\n");
	}

	//@SuppressWarnings("nls")
	private static void buildProtocolHandlers(final StringBuilder builder) {
		builder.append("<h3>Protocol Handlers</h3>\n");
		final List<String> protocols = new ArrayList<>(ProtocolHandlerRegistry.instance.getHandlerNames());
		Collections.sort(protocols);
		builder.append("<ul class='list-inline'>\n");
		for (final String protocol : protocols) {
			builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(protocol.toUpperCase()))
					.append("</li>\n");
		}
		builder.append("</ul>\n\n");
	}

	//@SuppressWarnings("nls")
	private static void buildTransportHandlers(final StringBuilder builder) {
		builder.append("<h3>Transport Handlers</h3>\n");
		final List<String> transports = new ArrayList<>(TransportHandlerRegistry.instance.getHandlerNames());
		Collections.sort(transports);
		builder.append("<ul class='list-inline'>\n");
		for (final String transport : transports) {
			builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(transport.toUpperCase()))
					.append("</li>\n");
		}
		builder.append("</ul>\n\n");
	}

	//@SuppressWarnings("nls")
	private static void buildOdysseusScript(final StringBuilder builder) {
		builder.append("<div class='row'>\n");
		builder.append("<h2>Odysseus Script</h2>\n");
		builder.append("<h3>Commands</h3>\n");
		final OdysseusScriptParser parser = new OdysseusScriptParser();
		final List<String> commands = new ArrayList<>(parser.getKeywordNames());
		Collections.sort(commands);
		builder.append("<ul class='list-inline'>\n");
		for (final String command : commands) {
			builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(command.toUpperCase()))
					.append("</li>\n");
		}
		builder.append("</ul>\n\n");

		builder.append("<h3>Constants</h3>\n");
		final List<String> constants = new ArrayList<>();
		for (final String key : ReplacementProviderManager.generateProviderMap().keySet()) {
			constants.add(key.substring(1));
		}
		Collections.sort(constants);
		builder.append("<ul class='list-inline'>\n");
		for (final String constant : constants) {
			builder.append("<li>").append(GenerateHTMLCheatSheetCommand.sanitize(constant.toUpperCase()))
					.append("</li>\n");
		}
		builder.append("</ul>\n\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildSample(final StringBuilder builder) {
		builder.append("<div class='row'>\n");
		builder.append("<h2>Sample Odysseus query</h2>\n");
		builder.append("<pre class='brush: pql'>\n");
		builder.append("#PARSER PQL\n");
		builder.append("#ADDQUERY\n\n");
		builder.append("input = ACCESS({source='source',\n");
		builder.append("        wrapper='GenericPull',\n");
		builder.append("        transport='File',\n");
		builder.append("        protocol='CSV',\n");
		builder.append("        dataHandler='Tuple',\n");
		builder.append("        metaattribute=['TimeInterval'],\n");
		builder.append("        options=[['filename','example.csv']],\n");
		builder.append("        schema=[['value','Double']]\n");
		builder.append("})\n");
		builder.append("output = MAP({expressions = ['value + 3']}, input)\n");
		builder.append("</pre>\n\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static void buildPQLGrammar(final StringBuilder builder) {
		builder.append("<div class='row'>\n");
		builder.append("<h2>Full Grammar of PQL</h2>\n");
		builder.append("<table class='table  table-striped'>\n");
		builder.append("<tr><td>QUERY           </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("(STREAM | VIEW | SOURCE)+")).append("</td></tr>\n");
		builder.append("<tr><td>STREAM          </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("STREAM \"=\" OPERATOR")).append("</td></tr>\n");
		builder.append("<tr><td>VIEW            </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("VIEWNAME \":=\" OPERATOR")).append("</td></tr>\n");
		builder.append("<tr><td>SOURCE          </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("SOURCENAME \"::=\" OPERATOR")).append("</td></tr>\n");
		builder.append("<tr><td>OPERATOR        </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize(
						"QUERY | [OUTPUTPORT \":\"] OPERATORTYPE \"(\" (PARAMETERLIST [ \",\" OPERATORLIST ] | OPERATORLIST) \")\""))
				.append("</td></tr>\n");
		builder.append("<tr><td>OPERATORLIST    </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("[ OPERATOR (\",\" OPERATOR)* ]"))
				.append("</td></tr>\n");
		builder.append("<tr><td>PARAMETERLIST   </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("\"{\" PARAMETER (\",\" PARAMETER)* \"}\""))
				.append("</td></tr>\n");
		builder.append("<tr><td>PARAMETER       </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("NAME \"=\" PARAMETERVALUE")).append("</td></tr>\n");
		builder.append("<tr><td>PARAMETERVALUE  </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("LONG | DOUBLE | STRING | PREDICATE | LIST | MAP"))
				.append("</td></tr>\n");
		builder.append("<tr><td>LIST            </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("\"[\" [PARAMETERVALUE (\",\" PARAMETERVALUE)*] \"]\""))
				.append("</td></tr>\n");
		builder.append("<tr><td>MAP             </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("\"[\" [MAPENTRY (\",\" MAPENTRY*] \"]\""))
				.append("</td></tr>\n");
		builder.append("<tr><td>MAPENTRY        </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("PARAMETERVALUE \"=\" PARAMETERVALUE"))
				.append("</td></tr>\n");
		builder.append("<tr><td>STRING          </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("\"'\" [~']* \"'\"")).append("</td></tr>\n");
		builder.append("<tr><td>PREDICATE       </td><td> <b>=</b> ")
				.append(GenerateHTMLCheatSheetCommand.sanitize("PREDICATETYPE \"(\" STRING \")\""))
				.append("</td></tr>\n");
		builder.append("</table>\n");
		builder.append("</div>\n");
	}

	//@SuppressWarnings("nls")
	private static String sanitize(final String string) {
		String result = string;
		result = result.replace("&", "&amp;");
		result = result.replace("\\", "&bsol;");
		result = result.replace("{", "&lcub;");
		result = result.replace("}", "&rcub;");
		result = result.replace("$", "&dollar;");
		result = result.replace("#", "&num;");
		result = result.replace("^", "&caret;");
		result = result.replace("~", "&tilde;");
		result = result.replace("%", "&percnt;");
		result = result.replace("<", "&lt;");
		result = result.replace(">", "&gt;");
		result = result.replace("|", "&verbar;");

		return result;
	}

	//@SuppressWarnings("nls")
	private static String concatDatatypes(final SDFDatatype[] types) {
		if (types == null || types.length == 0) {
			return "";
		}

		final StringBuilder sb = new StringBuilder();

		if (types.equals(SDFDatatype.NUMBERS)) {
			sb.append("<abbr title='" + GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.NUMBERS))
					+ "'>Numbers</abbr>");
		} else if (types.equals(SDFDatatype.DISCRETE_NUMBERS)) {
			sb.append("<abbr title='"
					+ GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.DISCRETE_NUMBERS))
					+ "'>Discrete Numbers</abbr>");
		} else if (types.equals(SDFDatatype.FLOATING_NUMBERS)) {
			sb.append("<abbr title='"
					+ GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.FLOATING_NUMBERS))
					+ "'>Floating Numbers</abbr>");
		} else if (types.equals(SDFDatatype.LONG_NUMBERS)) {
			sb.append(
					"<abbr title='" + GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.LONG_NUMBERS))
							+ "'>Long Numbers</abbr>");
		} else if (types.equals(SDFDatatype.getLists())) {
			sb.append("<abbr title='" + GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.getLists()))
					+ "'>Lists</abbr>");
		} else if (types.equals(SDFDatatype.NUMBERS_OBJECT)) {
			sb.append("<abbr title='"
					+ GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.NUMBERS_OBJECT))
					+ "'>Numbers | Object</abbr>");
		} else if (types.equals(SDFDatatype.SIMPLE_TYPES)) {
			sb.append(
					"<abbr title='" + GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.SIMPLE_TYPES))
							+ "'>Simple Types</abbr>");
		} else if (types.equals(SDFDatatype.MATRIXS)) {
			sb.append("<abbr title='" + GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.MATRIXS))
					+ "'>Matrixs</abbr>");
		} else if (types.equals(SDFDatatype.VECTORS)) {
			sb.append("<abbr title='" + GenerateHTMLCheatSheetCommand.sanitize(Arrays.toString(SDFDatatype.VECTORS))
					+ "'>Vectors</abbr>");
		} else {
			if (types.length >= 1 && types[0] != null) {
				sb.append(GenerateHTMLCheatSheetCommand.sanitize(types[0].getQualName()));
			}
		}

		return sb.toString();
	}
}
