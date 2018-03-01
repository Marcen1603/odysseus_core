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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
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
public class GeneratePDFCheatSheetCommand extends AbstractHandler {
    static final Logger LOG = LoggerFactory.getLogger(GeneratePDFCheatSheetCommand.class);
    private static final String PDFLATEX_CMD = "/usr/bin/pdflatex";

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            @Override
            public void run() {
                final String cmd = (System.getProperty("latex") != null) ? System.getProperty("latex") : PDFLATEX_CMD;
                final Shell shell;
                if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null) {
                    shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
                }
                else {
                    shell = new Shell();
                }
                try {
                    final FileDialog dialog = new FileDialog(shell, SWT.SAVE);

                    dialog.setFilterNames(new String[] { "Tex Files (*.tex)" });
                    dialog.setFilterExtensions(new String[] { "*.tex" });
                    dialog.setText("Select file..");
                    final String file = dialog.open();
                    if (file != null) {
                        final StringBuilder builder = new StringBuilder();
                        GeneratePDFCheatSheetCommand.build(builder);
                        Path path = (new File(file)).toPath();
                        Path directory = path.getParent();
                        if (!Files.exists(directory)) {
                            Files.createDirectory(directory);
                        }
                        try (FileOutputStream out = new FileOutputStream(file)) {
                            try (PrintStream print = new PrintStream(out)) {
                                print.println(builder.toString());
                            }
                        }
                        final String[] env = new String[] {};
                        if ((new File(cmd)).exists()) {
                            GeneratePDFCheatSheetCommand.LOG.info("Executing '{} -synctex=1 -interaction nonstopmode -output-directory {} {}'",
                                    new Object[] { cmd, directory.toString(), path.toString() });
                            final Process process = Runtime.getRuntime().exec(cmd + " -synctex=1 -interaction nonstopmode -output-directory " + directory + " " + file, env);
                            try {
                                process.waitFor();
                                final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    GeneratePDFCheatSheetCommand.LOG.error(line);
                                }
                            }
                            catch (final InterruptedException e) {
                                GeneratePDFCheatSheetCommand.LOG.error(e.getMessage(), e);
                            }
                        }
                        else {
                            GeneratePDFCheatSheetCommand.LOG.warn("Unable to execute {} to generate cheatsheet PDF", cmd);
                        }
                    }
                }
                catch (final Exception e) {
                    GeneratePDFCheatSheetCommand.LOG.error(e.getMessage(), e);
                }
            }
        });
        return null;
    }

    static void build(final StringBuilder builder) {
        builder.append("\\documentclass[10pt,landscape]{article}\n\n");
        builder.append("\\usepackage{multicol}\n");
        builder.append("\\usepackage{calc}\n");
        builder.append("\\usepackage{ifthen}\n");
        builder.append("\\usepackage[landscape]{geometry}\n");
        builder.append("\\usepackage{textcomp}\n");
        builder.append("\\usepackage[english]{babel}\n");
        builder.append("\\usepackage[svgnames]{xcolor}\n");
        builder.append("\\usepackage{ifxetex,ifluatex}\n");
        builder.append("\\ifxetex\n");
        builder.append("  \\usepackage{fontspec,xltxtra,xunicode}\n");
        builder.append("  \\defaultfontfeatures{Mapping=tex-text, Scale=MatchLowercase, Ligatures=TeX}\n");
        builder.append("  \\setmainfont[Numbers={OldStyle}]{Linux Libertine O}\n");
        builder.append("\\else\n");
        builder.append("  \\ifluatex\n");
        builder.append("    \\usepackage{fontspec}\n");
        builder.append("  \\else\n");
        builder.append("    \\usepackage[utf8]{inputenc}\n");
        builder.append("  \\fi\n");
        builder.append("\\fi\n");
        builder.append("\\ifxetex\n");
        builder.append("  \\usepackage[setpagesize=false,unicode=false,xetex]{hyperref}\n");
        builder.append("\\else\n");
        builder.append("  \\usepackage[unicode=true]{hyperref}\n");
        builder.append("\\fi\n\n");

        builder.append("\\definecolor{uniolblue}{rgb}{0.0, 0.31765, 0.61961}\n");

        builder.append("\\ifthenelse{\\lengthtest { \\paperwidth = 11in}}\n");
        builder.append("{ \\geometry{top=.5in,left=.5in,right=.5in,bottom=.5in} }\n");
        builder.append("{\\ifthenelse{ \\lengthtest{ \\paperwidth = 297mm}}\n");
        builder.append("    {\\geometry{top=1cm,left=1cm,right=1cm,bottom=1cm} }\n");
        builder.append("    {\\geometry{top=1cm,left=1cm,right=1cm,bottom=1cm} }\n");
        builder.append("}\n");

        builder.append("\\pagestyle{empty}\n");
        builder.append("\\makeatletter\n");
        builder.append("\\renewcommand{\\section}{\\@startsection{section}{1}{0mm}%\n");
        builder.append("  {-1ex plus -.5ex minus -.2ex}%\n");
        builder.append("  {0.5ex plus .2ex}%\n");
        builder.append("  {\\normalfont\\large\\bfseries}}\n");
        builder.append("\\renewcommand{\\subsection}{\\@startsection{subsection}{2}{0mm}%\n");
        builder.append("  {-1explus -.5ex minus -.2ex}%\n");
        builder.append("  {0.5ex plus .2ex}%\n");
        builder.append("  {\\normalfont\\normalsize\\bfseries}}\n");
        builder.append("\\renewcommand{\\subsubsection}{\\@startsection{subsubsection}{3}{0mm}%\n");
        builder.append("  {-1ex plus -.5ex minus -.2ex}%\n");
        builder.append("  {1ex plus .2ex}%\n");
        builder.append("  {\\normalfont\\small\\bfseries}}\n");
        builder.append("\\makeatother\n");
        builder.append("\\setcounter{secnumdepth}{0}\n");
        builder.append("\\setlength{\\parindent}{0pt}\n");
        builder.append("\\setlength{\\parskip}{0pt plus 0.5ex}\n\n");

        builder.append("\\clubpenalty = 10000\n");
        builder.append("\\widowpenalty = 10000\n");
        builder.append("\\displaywidowpenalty = 10000\n\n");

        builder.append("\\begin{document}\n");

        builder.append("\\raggedright\n");
        builder.append("\\footnotesize\n");
        builder.append("\\begin{multicols}{3}\n");
        builder.append("\\setlength{\\premulticols}{1pt}\n");
        builder.append("\\setlength{\\postmulticols}{1pt}\n");
        builder.append("\\setlength{\\multicolsep}{1pt}\n");
        builder.append("\\setlength{\\columnsep}{2pt}\n\n");

        builder.append("\\begin{center}\n");
        builder.append("\\textcolor{uniolblue}{\\huge{\\textbf{Odysseus\\ Cheat Sheet}}} \\\\\n");
        builder.append("\\end{center}\n\n");

        builder.append("\\newlength{\\MyLen}\n");
        builder.append("\\settowidth{\\MyLen}{\\texttt{letterpaper}/\\texttt{a4paper} \\ }\n\n");
        GeneratePDFCheatSheetCommand.buildPQLGrammar(builder);
        GeneratePDFCheatSheetCommand.buildPQLOperators(builder);
        GeneratePDFCheatSheetCommand.buildAggregationFunctions(builder);
        GeneratePDFCheatSheetCommand.buildMEPFunctions(builder);
        GeneratePDFCheatSheetCommand.buildMetadatas(builder);
        GeneratePDFCheatSheetCommand.buildDatatypes(builder);
        GeneratePDFCheatSheetCommand.buildHandlers(builder);
        GeneratePDFCheatSheetCommand.buildScheduling(builder);
        GeneratePDFCheatSheetCommand.buildBufferPlacementStrategies(builder);
        GeneratePDFCheatSheetCommand.buildOdysseusScript(builder);

        GeneratePDFCheatSheetCommand.buildSample(builder);
        builder.append("\\rule{0.3\\linewidth}{0.25pt}\n");
        builder.append("\\scriptsize\n\n");
        builder.append("Copyright \\copyright\\ ").append(Calendar.getInstance().get(Calendar.YEAR)).append(" ODYSSEUS Team\n\n");
        builder.append("\\href{http://odysseus.informatik.uni-oldenburg.de}{http://odysseus.informatik.uni-oldenburg.de}\n\n");
        builder.append("Wiki: \\href{http://wiki.odysseus.informatik.uni-oldenburg.de}{http://wiki.odysseus.informatik.uni-oldenburg.de}\n\n");
        builder.append("Forum: \\href{http://forum.odysseus.informatik.uni-oldenburg.de}{http://forum.odysseus.informatik.uni-oldenburg.de}\n\n");
        builder.append("\\end{multicols}\n");
        builder.append("\\end{document}\n");
    }

    private static void buildDatatypes(final StringBuilder builder) {
        builder.append("\\section{Datatypes}\n");
        if (Activator.getExecutor() != null) {
            List<SDFDatatype> datatypes = new ArrayList<>(Activator.getExecutor().getRegisteredDatatypes(Activator.getSession()));
            Collections.sort(datatypes, new Comparator<SDFDatatype>() {

                @Override
                public int compare(SDFDatatype o1, SDFDatatype o2) {
                    return o1.getQualName().compareTo(o2.getQualName());
                }
            });
            for (final SDFDatatype datatype : datatypes) {
                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(datatype.getQualName().toUpperCase())).append("}\\\\\n");
            }
        }
    }

    private static void buildMetadatas(final StringBuilder builder) {
        builder.append("\\section{Metadata}\n");
        if (Activator.getExecutor() != null) {
            List<String> metadatas = new ArrayList<>(MetadataRegistry.getNames());
            Collections.sort(metadatas);
            for (final String metadata : metadatas) {
                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(metadata.toUpperCase())).append("}\\\\\n");
            }
        }
    }

    private static void buildScheduling(final StringBuilder builder) {
        builder.append("\\section{Scheduling}\n");
        GeneratePDFCheatSheetCommand.buildSchedulers(builder);
        GeneratePDFCheatSheetCommand.buildSchedulingStrategies(builder);
    }

    private static void buildSchedulers(final StringBuilder builder) {
        builder.append("\\subsection{Schedulers}\n");
        if (Activator.getExecutor() != null) {
            List<String> schedulers = new ArrayList<>(Activator.getExecutor().getRegisteredSchedulers(Activator.getSession()));
            Collections.sort(schedulers);
            for (final String scheduler : schedulers) {
                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(scheduler.toUpperCase())).append("}\\\\\n");
            }
        }
    }

    private static void buildSchedulingStrategies(final StringBuilder builder) {
        builder.append("\\subsection{Scheduling Strategies}\n");
        if (Activator.getExecutor() != null) {
            List<String> schedulingStrategies = new ArrayList<>(Activator.getExecutor().getRegisteredSchedulingStrategies(Activator.getSession()));
            Collections.sort(schedulingStrategies);
            for (final String schedulingStrategy : schedulingStrategies) {
                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(schedulingStrategy.toUpperCase())).append("}\\\\\n");
            }
        }
    }

    private static void buildBufferPlacementStrategies(final StringBuilder builder) {
        builder.append("\\section{Buffer Placement Strategies}\n");
        if (Activator.getExecutor() != null) {
            List<String> bufferPlacementStrategies = new ArrayList<>(Activator.getExecutor().getRegisteredBufferPlacementStrategiesIDs(Activator.getSession()));
            Collections.sort(bufferPlacementStrategies);
            for (final String bufferPlacementStrategy : bufferPlacementStrategies) {
                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(bufferPlacementStrategy.toUpperCase())).append("}\\\\\n");
            }
        }
    }

    private static void buildPQLOperators(final StringBuilder builder) {
        if (Activator.getExecutor() != null) {
            final List<LogicalOperatorInformation> operators = Activator.getExecutor().getOperatorInformations(Activator.getSession());

            Map<String, List<LogicalOperatorInformation>> categories = new HashMap<>();
            for (final LogicalOperatorInformation operator : operators) {
                if (!operator.isHidden()) {
                    for (String category : operator.getCategories()) {
                        if (!categories.containsKey(category)) {
                            categories.put(category, new ArrayList<LogicalOperatorInformation>());
                        }
                        categories.get(category).add(operator);
                    }
                }
            }
            List<String> sortedCategories = new ArrayList<>(categories.keySet());
            Collections.sort(sortedCategories);
            for (String category : sortedCategories) {
                builder.append("\\section{").append(category.substring(0, 1).toUpperCase()).append(category.substring(1).toLowerCase()).append(" Operators}\n");

                Collections.sort(categories.get(category), new Comparator<LogicalOperatorInformation>() {

                    @Override
                    public int compare(final LogicalOperatorInformation o1, final LogicalOperatorInformation o2) {
                        return o1.getOperatorName().compareTo(o2.getOperatorName());
                    }

                });
                for (final LogicalOperatorInformation operator : categories.get(category)) {
                    builder.append("\\subsection{").append(GeneratePDFCheatSheetCommand.sanitize(operator.getOperatorName().toUpperCase()));
                    if (operator.isDeprecated()) {
                        builder.append(" \\textit{(Deprecated)}");
                    }
                    builder.append("}\n");
                    builder.append(GeneratePDFCheatSheetCommand.sanitize(operator.getDoc())).append("\n");
                    String len = "";
                    for (final LogicalParameterInformation parameter : operator.getParameters()) {
                        if (parameter.getName().length() > len.length()) {
                            len = GeneratePDFCheatSheetCommand.sanitize(parameter.getName());
                        }
                    }
                    builder.append("\\settowidth{\\MyLen}{\\texttt{").append(len).append("} }\n");
                    builder.append("\\begin{tabular}{@{}p{\\the\\MyLen}@{}p{\\linewidth-\\the\\MyLen}@{}}\n");

                    for (final LogicalParameterInformation parameter : operator.getParameters()) {
                        if ((!parameter.getName().equalsIgnoreCase("NAME")) && (!parameter.getName().equalsIgnoreCase("ID")) && (!parameter.getName().equalsIgnoreCase("DESTINATION"))) {
                            if (!parameter.getDoc().equalsIgnoreCase("No description")) {
                                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(parameter.getName())).append("}  & ")
                                        .append(GeneratePDFCheatSheetCommand.sanitize(parameter.getDoc())).append(" \\\\\n");
                            }
                            else {
                                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(parameter.getName())).append("}  &  \\\\\n");
                            }
                        }
                    }

                    builder.append("\\end{tabular}\n");
                }
            }
        }
    }

    private static void buildAggregationFunctions(final StringBuilder builder) {
        builder.append("\\section{Aggregates}\n");
        builder.append("\\subsection{Example}\n");
        builder.append("\\begin{verbatim}\n");
        builder.append("aggregate = AGGREGATE({',\n");
        builder.append("            group_by = ['y'],\n");
        builder.append("            aggregations=[\n");
        builder.append("              ['SUM', 'x', 'sum_x', 'double']\n");
        builder.append("             ]\n");
        builder.append("            }, input)\n");
        builder.append("\\end{verbatim}\n\n");

        builder.append("\\subsection{Functions}\n");

        final List<String> functions = new ArrayList<>(AggregateFunctionBuilderRegistry.getFunctionNames(Tuple.class));
        Collections.sort(functions);
        builder.append("\\begin{multicols}{2}\n");
        for (final String function : functions) {
            builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(function.toUpperCase())).append("}\\\\\n");
        }
        builder.append("\\end{multicols}\n\n");

    }

    private static void buildMEPFunctions(final StringBuilder builder) {
        builder.append("\\section{Functions}\n");
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
            if (((function.getSymbol().charAt(0) >= 'A') && (function.getSymbol().charAt(0) <= 'Z')) || ((function.getSymbol().charAt(0) >= 'a') && (function.getSymbol().charAt(0) <= 'z'))) {
                String packageName = function.getClass().getPackage().getName();
                final int index = packageName.lastIndexOf(".");
                packageName = packageName.substring(index + 1, index + 2).toUpperCase() + packageName.substring(index + 2);
                if (!functions.containsKey(packageName)) {
                    functions.put(packageName, new ArrayList<IMepFunction<?>>());
                }
                functions.get(packageName).add(function);
            }
            else {
                if (function.getSymbol().charAt(0) != '_') {
                    symbols.add(function);
                }
            }
        }
        final List<String> packages = new ArrayList<>(functions.keySet());
        Collections.sort(packages);
        for (final String packageName : packages) {
            builder.append("\\subsection{").append(packageName).append("}\n");
            String len = "";
            for (final IMepFunction<?> function : functions.get(packageName)) {
                if (function.getSymbol().length() > len.length()) {
                    len = GeneratePDFCheatSheetCommand.sanitize(function.getSymbol());
                }
            }
            for (final IMepFunction<?> function : functions.get(packageName)) {
                final String name = function.getSymbol();
                final String returnType = function.getReturnType().getQualName();
                final StringBuilder sb = new StringBuilder();

                for (int i = 0; i < function.getArity(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(GeneratePDFCheatSheetCommand.concatDatatypes(function.getAcceptedTypes(i)));
                }
                Deprecated annotation = function.getClass().getAnnotation(Deprecated.class);
                builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(name)).append("(\\textit{").append(GeneratePDFCheatSheetCommand.sanitize(sb.toString())).append("})}");
                if (annotation != null) {
                    builder.append(" $\\rightarrow$ ").append(GeneratePDFCheatSheetCommand.sanitize(returnType)).append(" \\textit{(Deprecated)}\\\\\n");
                }
                else {
                    builder.append(" $\\rightarrow$ ").append(GeneratePDFCheatSheetCommand.sanitize(returnType)).append("\\\\\n");
                }
            }
        }

        builder.append("\\section{Symbols}\n");
        for (final IMepFunction<?> symbol : symbols) {
            final String name = symbol.getSymbol();
            final String returnType = symbol.getReturnType().getQualName();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < symbol.getArity(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(GeneratePDFCheatSheetCommand.concatDatatypes(symbol.getAcceptedTypes(i)));
            }

            builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(name)).append("(\\textit{").append(GeneratePDFCheatSheetCommand.sanitize(sb.toString())).append("})}");
            builder.append(" $\\rightarrow$ ").append(GeneratePDFCheatSheetCommand.sanitize(returnType)).append("\\\\\n");
        }
    }

    private static void buildHandlers(final StringBuilder builder) {
        builder.append("\\section{Handlers}\n");
        GeneratePDFCheatSheetCommand.buildDataHandlers(builder);
        GeneratePDFCheatSheetCommand.buildProtocolHandlers(builder);
        GeneratePDFCheatSheetCommand.buildTransportHandlers(builder);
    }

    private static void buildDataHandlers(final StringBuilder builder) {
        builder.append("\\subsection{Data Handlers}\n");
        final List<String> datas = new ArrayList<>(DataHandlerRegistry.instance.getHandlerNames());
        Collections.sort(datas);
        builder.append("\\begin{multicols}{2}\n");
        for (final String data : datas) {
            builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(data.toUpperCase())).append("}\\\\\n");
        }
        builder.append("\\end{multicols}\n\n");
    }

    private static void buildProtocolHandlers(final StringBuilder builder) {
        builder.append("\\subsection{Protocol Handlers}\n");
        final List<String> protocols = new ArrayList<>(ProtocolHandlerRegistry.instance.getHandlerNames());
        Collections.sort(protocols);
        builder.append("\\begin{multicols}{2}\n");
        for (final String protocol : protocols) {
            builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(protocol.toUpperCase())).append("}\\\\\n");
        }
        builder.append("\\end{multicols}\n\n");
    }

    private static void buildTransportHandlers(final StringBuilder builder) {
        builder.append("\\subsection{Transport Handlers}\n");
        final List<String> transports = new ArrayList<>(TransportHandlerRegistry.instance.getHandlerNames());
        Collections.sort(transports);
        builder.append("\\begin{multicols}{2}\n");
        for (final String transport : transports) {
            builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(transport.toUpperCase())).append("}\\\\\n");
        }
        builder.append("\\end{multicols}\n\n");
    }

    private static void buildOdysseusScript(final StringBuilder builder) {
        builder.append("\\section{Odysseus Script}\n");

        builder.append("\\subsection{Commands}\n");
        final OdysseusScriptParser parser = new OdysseusScriptParser();
        final List<String> commands = new ArrayList<>(parser.getKeywordNames());
        Collections.sort(commands);
        builder.append("\\begin{multicols}{2}\n");
        for (final String command : commands) {
            builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(command.toUpperCase())).append("}\\\\\n");
        }
        builder.append("\\end{multicols}\n\n");

        builder.append("\\subsection{Constants}\n");
        final List<String> constants = new ArrayList<>();
        for (final String key : ReplacementProviderManager.generateProviderMap().keySet()) {
            constants.add(key.substring(1));
        }
        Collections.sort(constants);
        for (final String constant : constants) {
            builder.append("\\texttt{").append(GeneratePDFCheatSheetCommand.sanitize(constant.toUpperCase())).append("}\\\\\n");
        }
    }

    private static void buildSample(final StringBuilder builder) {
        builder.append("\\section{Sample Odysseus\\ query}\n");
        builder.append("\\begin{verbatim}\n");
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
        builder.append("\\end{verbatim}\n\n");
    }

    private static void buildPQLGrammar(final StringBuilder builder) {
        builder.append("\\section{Full Grammar of PQL}\n");
        builder.append("\\settowidth{\\MyLen}{\\texttt{PARAMETERVALUE} }\n");
        builder.append("\\begin{tabular}{@{}p{\\the\\MyLen}@{}p{\\linewidth-\\the\\MyLen}@{}}\n");
        builder.append("\\texttt{QUERY}           & \\texttt{= ").append(sanitize("(STREAM | VIEW | SOURCE)+")).append("}\\\\\n");
        builder.append("\\texttt{STREAM}          & \\texttt{= ").append(sanitize("STREAM \"=\" OPERATOR")).append("}\\\\\n");
        builder.append("\\texttt{VIEW}            & \\texttt{= ").append(sanitize("VIEWNAME \":=\" OPERATOR")).append("}\\\\\n");
        builder.append("\\texttt{SOURCE}          & \\texttt{= ").append(sanitize("SOURCENAME \"::=\" OPERATOR")).append("}\\\\\n");
        builder.append("\\texttt{OPERATOR}        & \\texttt{= ").append(sanitize("QUERY | [OUTPUTPORT \":\"] OPERATORTYPE \"(\" (PARAMETERLIST [ \",\" OPERATORLIST ] | OPERATORLIST) \")\""))
                .append("}\\\\\n");
        builder.append("\\texttt{OPERATORLIST}    & \\texttt{= ").append(sanitize("[ OPERATOR (\",\" OPERATOR)* ]")).append("}\\\\\n");
        builder.append("\\texttt{PARAMETERLIST}   & \\texttt{= ").append(sanitize("\"{\" PARAMETER (\",\" PARAMETER)* \"}\"")).append("}\\\\\n");
        builder.append("\\texttt{PARAMETER}       & \\texttt{= ").append(sanitize("NAME \"=\" PARAMETERVALUE")).append("}\\\\\n");
        builder.append("\\texttt{PARAMETERVALUE}  & \\texttt{= ").append(sanitize("LONG | DOUBLE | STRING | PREDICATE | LIST | MAP")).append("}\\\\\n");
        builder.append("\\texttt{LIST}            & \\texttt{= ").append(sanitize("\"[\" [PARAMETERVALUE (\",\" PARAMETERVALUE)*] \"]\"")).append("}\\\\\n");
        builder.append("\\texttt{MAP}             & \\texttt{= ").append(sanitize("\"[\" [MAPENTRY (\",\" MAPENTRY*] \"]\"")).append("}\\\\\n");
        builder.append("\\texttt{MAPENTRY}        & \\texttt{= ").append(sanitize("PARAMETERVALUE \"=\" PARAMETERVALUE")).append("}\\\\\n");
        builder.append("\\texttt{STRING}          & \\texttt{= ").append(sanitize("\"'\" [~']* \"'\"")).append("}\\\\\n");
        builder.append("\\texttt{PREDICATE}       & \\texttt{= ").append(sanitize("PREDICATETYPE \"(\" STRING \")\"")).append("}\\\\\n");
        builder.append("\\end{tabular}\n");
    }

    private static String sanitize(final String string) {
        String result = string;

        result = result.replace("\\", "\\textbackslash{}");
        result = result.replace("{", "\\{");
        result = result.replace("}", "\\}");
        result = result.replace("$", "\\$");
        result = result.replace("&", "\\&");
        result = result.replace("#", "\\#");
        result = result.replace("^", "\\textasciicircum{}");
        result = result.replace("_", "\\_");
        result = result.replace("~", "\\textasciitilde{}");
        result = result.replace("%", "\\%");
        result = result.replace("<", "\\textless{}");
        result = result.replace(">", "\\textgreater{}");
        result = result.replace("|", "\\textbar{}");

        return result;
    }

    private static String concatDatatypes(final SDFDatatype[] types) {
        if ((types == null) || (types.length == 0)) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        if (types.equals(SDFDatatype.NUMBERS)) {
            sb.append("Number");
        }
        else if (types.equals(SDFDatatype.DISCRETE_NUMBERS)) {
            sb.append("Discrete Number");
        }
        else if (types.equals(SDFDatatype.FLOATING_NUMBERS)) {
            sb.append("Floating Number");
        }
        else if (types.equals(SDFDatatype.LONG_NUMBERS)) {
            sb.append("Long Number");
        }
        else if (types.equals(SDFDatatype.getLists())) {
            sb.append("List");
        }
        else if (types.equals(SDFDatatype.NUMBERS_OBJECT)) {
            sb.append("Number | Object");
        }
        else if (types.equals(SDFDatatype.SIMPLE_TYPES)) {
            sb.append("Simple Type");
        }
        else if (types.equals(SDFDatatype.MATRIXS)) {
            sb.append("Matrix");
        }
        else if (types.equals(SDFDatatype.VECTORS)) {
            sb.append("Vector");
        }
        else {
            if ((types.length >= 1) && (types[0] != null)) {
                sb.append(types[0].getQualName());
            }
        }

        return sb.toString();
    }
}
