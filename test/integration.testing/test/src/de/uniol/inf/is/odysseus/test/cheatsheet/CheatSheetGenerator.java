/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.test.cheatsheet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.ReplacementProviderManager;
import de.uniol.inf.is.odysseus.test.ExecutorProvider;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class CheatSheetGenerator {
    static final Logger LOG = LoggerFactory.getLogger(CheatSheetGenerator.class);
    private static final String LATEX_CMD = "/usr/bin/pdflatex";

    public static void execute(final String file) throws FileNotFoundException, IOException {

        final StringBuilder builder = new StringBuilder();
        CheatSheetGenerator.build(builder);
        try (FileOutputStream out = new FileOutputStream(file)) {
            try (PrintStream print = new PrintStream(out)) {
                print.println(builder.toString());
                final String[] env = new String[] {};
                final String directory = (new File(file)).getParent();
                if ((new File(CheatSheetGenerator.LATEX_CMD)).exists()) {
                    CheatSheetGenerator.LOG.info("Executing '{} -synctex=1 -interaction nonstopmode -output-directory {} {}'", new String[] { CheatSheetGenerator.LATEX_CMD, directory, file });
                    final Process process = Runtime.getRuntime().exec("/usr/bin/pdflatex -synctex=1 -interaction nonstopmode -output-directory " + directory + " " + file, env);
                    try {
                        process.waitFor();
                        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            CheatSheetGenerator.LOG.error(line);
                        }
                    }
                    catch (final InterruptedException e) {
                        CheatSheetGenerator.LOG.error(e.getMessage(), e);
                    }
                }
                else {
                    CheatSheetGenerator.LOG.warn("Unable to execute {} to generate cheatsheet PDF", CheatSheetGenerator.LATEX_CMD);
                }
            }
        }

    }

    static void build(final StringBuilder builder) {
        builder.append("\\documentclass[10pt,landscape]{article}\n\n");
        builder.append("\\usepackage{multicol}\n");
        builder.append("\\usepackage{calc}\n");
        builder.append("\\usepackage{ifthen}\n");
        builder.append("\\usepackage[landscape]{geometry}\n");
        builder.append("\\usepackage{textcomp}\n");

        builder.append("\\usepackage{ifxetex,ifluatex}\n");
        builder.append("\\ifxetex\n");
        builder.append("  \\usepackage{fontspec,xltxtra,xunicode}\n");
        builder.append("  \\setmainfont[Ligatures=TeX]{Linux Libertine O}\n");
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
        builder.append("\\widowpenalty = 10000 \\displaywidowpenalty = 10000\n\n");

        builder.append("\\begin{document}\n");

        builder.append("\\raggedright\n");
        builder.append("\\footnotesize\n");
        builder.append("\\begin{multicols}{3}\n");
        builder.append("\\setlength{\\premulticols}{1pt}\n");
        builder.append("\\setlength{\\postmulticols}{1pt}\n");
        builder.append("\\setlength{\\multicolsep}{1pt}\n");
        builder.append("\\setlength{\\columnsep}{2pt}\n\n");

        builder.append("\\begin{center}\n");
        builder.append("\\Large{\\textbf{Odysseus\\ Cheat Sheet}} \\\\\n");
        builder.append("\\end{center}\n\n");

        builder.append("\\newlength{\\MyLen}\n");
        builder.append("\\settowidth{\\MyLen}{\\texttt{letterpaper}/\\texttt{a4paper} \\ }\n\n");
        CheatSheetGenerator.buildPQLOperators(builder);
        CheatSheetGenerator.buildAggregationFunctions(builder);
        CheatSheetGenerator.buildMEPFunctions(builder);
        CheatSheetGenerator.buildHandlers(builder);
        CheatSheetGenerator.buildOdysseusScript(builder);

        CheatSheetGenerator.buildSample(builder);
        builder.append("\\rule{0.3\\linewidth}{0.25pt}\n");
        builder.append("\\scriptsize\n\n");
        builder.append("Copyright \\copyright\\ ").append(Calendar.getInstance().get(Calendar.YEAR)).append(" ODYSSEUS Team\n\n");
        builder.append("\\href{http://odysseus.informatik.uni-oldenburg.de/}{http://odysseus.informatik.uni-oldenburg.de/}\n\n");
        builder.append("\\end{multicols}\n");
        builder.append("\\end{document}\n");
    }

    private static void buildPQLOperators(final StringBuilder builder) {
        builder.append("\\section{Operators}\n");
        if (ExecutorProvider.getExeuctor() != null) {
            final BasicTestContext context = new BasicTestContext();
            context.setPassword("manager");
            context.setUsername("System");
            final ISession session = UserManagementProvider.getSessionmanagement().login(context.getUsername(), context.getPassword().getBytes(), UserManagementProvider.getDefaultTenant());

            final List<LogicalOperatorInformation> operators = ExecutorProvider.getExeuctor().getOperatorInformations(session);
            Collections.sort(operators, new Comparator<LogicalOperatorInformation>() {

                @Override
                public int compare(final LogicalOperatorInformation o1, final LogicalOperatorInformation o2) {
                    return o1.getOperatorName().compareTo(o2.getOperatorName());
                }

            });
            for (final LogicalOperatorInformation operator : operators) {
                builder.append("\\subsection{").append(CheatSheetGenerator.sanitize(operator.getOperatorName().toUpperCase())).append("}\n");
                builder.append(CheatSheetGenerator.sanitize(operator.getDoc())).append("\n");
                String len = "";
                for (final LogicalParameterInformation parameter : operator.getParameters()) {
                    if (parameter.getName().length() > len.length()) {
                        len = CheatSheetGenerator.sanitize(parameter.getName());
                    }
                }
                builder.append("\\settowidth{\\MyLen}{\\texttt{").append(len).append("} }\n");
                builder.append("\\begin{tabular}{@{}p{\\the\\MyLen}@{}p{\\linewidth-\\the\\MyLen}@{}}\n");

                // name, id und destination rausfiltern
                for (final LogicalParameterInformation parameter : operator.getParameters()) {
                    if ((!parameter.getName().equalsIgnoreCase("NAME")) && (!parameter.getName().equalsIgnoreCase("ID")) && (!parameter.getName().equalsIgnoreCase("DESTINATION"))) {
                        if (!parameter.getDoc().equalsIgnoreCase("No description")) {
                            builder.append("\\texttt{").append(CheatSheetGenerator.sanitize(parameter.getName())).append("}  & ").append(CheatSheetGenerator.sanitize(parameter.getDoc()))
                                    .append(" \\\\\n");
                        }
                        else {
                            builder.append("\\texttt{").append(CheatSheetGenerator.sanitize(parameter.getName())).append("}  & -- \\\\\n");
                        }
                    }
                }

                builder.append("\\end{tabular}\n");
            }
        }
    }

    private static void buildAggregationFunctions(final StringBuilder builder) {
        builder.append("\\section{Aggregates}\n");

        final List<String> functions = new ArrayList<>(AggregateFunctionBuilderRegistry.getFunctionNames(Tuple.class));
        Collections.sort(functions);
        builder.append("\\begin{tabular}{@{}l@{\\hspace{1em}}l@{\\hspace{1em}}l@{}}\n");
        int i = 0;
        for (final String function : functions) {
            if (i > 0) {
                builder.append(" & ");
            }
            builder.append(CheatSheetGenerator.sanitize(function));
            if (i == 1) {
                builder.append(" \\\\\n");
                i = 0;
            }
            else {
                i++;
            }
        }
        builder.append("\\end{tabular}\n\n");

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

        final List<IFunction<?>> symbols = new ArrayList<>();
        final Map<String, List<IFunction<?>>> functions = new HashMap<>();

        for (final FunctionSignature functionSignature : functionSignatures) {
            final IFunction<?> function = MEP.getFunction(functionSignature);
            if (((function.getSymbol().charAt(0) >= 'A') && (function.getSymbol().charAt(0) <= 'Z')) || ((function.getSymbol().charAt(0) >= 'a') && (function.getSymbol().charAt(0) <= 'z'))) {
                String packageName = function.getClass().getPackage().getName();
                final int index = packageName.lastIndexOf(".");
                packageName = packageName.substring(index + 1, index + 2).toUpperCase() + packageName.substring(index + 2);
                if (!functions.containsKey(packageName)) {
                    functions.put(packageName, new ArrayList<IFunction<?>>());
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
            for (final IFunction<?> function : functions.get(packageName)) {
                if (function.getSymbol().length() > len.length()) {
                    len = CheatSheetGenerator.sanitize(function.getSymbol());
                }
            }
            // builder.append("\\begin{multicols}{2}\n");
            for (final IFunction<?> function : functions.get(packageName)) {
                final String name = function.getSymbol();
                final String returnType = function.getReturnType().getQualName();
                final StringBuilder sb = new StringBuilder();

                for (int i = 0; i < function.getArity(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(CheatSheetGenerator.concatDatatypes(function.getAcceptedTypes(i)));
                }

                builder.append("\\texttt{").append(CheatSheetGenerator.sanitize(name)).append("(\\textit{").append(CheatSheetGenerator.sanitize(sb.toString())).append("})}");
                builder.append(" $\\rightarrow$ ").append(CheatSheetGenerator.sanitize(returnType)).append("\\\\\n");
            }
            // builder.append("\\end{multicols}\n\n");
        }

        builder.append("\\section{Symbols}\n");
        for (final IFunction<?> symbol : symbols) {
            final String name = symbol.getSymbol();
            final String returnType = symbol.getReturnType().getQualName();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < symbol.getArity(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(CheatSheetGenerator.concatDatatypes(symbol.getAcceptedTypes(i)));
            }

            builder.append("\\texttt{").append(CheatSheetGenerator.sanitize(name)).append("(\\textit{").append(CheatSheetGenerator.sanitize(sb.toString())).append("})}");
            builder.append(" $\\rightarrow$ ").append(CheatSheetGenerator.sanitize(returnType)).append("\\\\\n");
        }
    }

    private static void buildHandlers(final StringBuilder builder) {
        builder.append("\\section{Handlers}\n");
        CheatSheetGenerator.buildDataHandlers(builder);
        CheatSheetGenerator.buildProtocolHandlers(builder);
        CheatSheetGenerator.buildTransportHandlers(builder);
    }

    private static void buildDataHandlers(final StringBuilder builder) {
        builder.append("\\subsection{Data Handlers}\n");
        final List<String> datas = new ArrayList<>(DataHandlerRegistry.getHandlerNames());
        Collections.sort(datas);
        builder.append("\\begin{tabular}{@{}l@{\\hspace{1em}}l@{\\hspace{1em}}l@{}}\n");
        int i = 0;
        for (final String data : datas) {
            if (i > 0) {
                builder.append(" & ");
            }
            builder.append(CheatSheetGenerator.sanitize(data.toUpperCase()));
            if (i == 1) {
                builder.append(" \\\\\n");
                i = 0;
            }
            else {
                i++;
            }
        }
        builder.append("\\end{tabular}\n\n");
    }

    private static void buildProtocolHandlers(final StringBuilder builder) {
        builder.append("\\subsection{Protocol Handlers}\n");
        final List<String> protocols = new ArrayList<>(ProtocolHandlerRegistry.getHandlerNames());
        Collections.sort(protocols);
        builder.append("\\begin{tabular}{@{}l@{\\hspace{1em}}l@{\\hspace{1em}}l@{}}\n");
        int i = 0;
        for (final String protocol : protocols) {
            if (i > 0) {
                builder.append(" & ");
            }
            builder.append(CheatSheetGenerator.sanitize(protocol.toUpperCase()));
            if (i == 1) {
                builder.append(" \\\\\n");
                i = 0;
            }
            else {
                i++;
            }
        }
        builder.append("\\end{tabular}\n\n");
    }

    private static void buildTransportHandlers(final StringBuilder builder) {
        builder.append("\\subsection{Transport Handlers}\n");
        final List<String> transports = new ArrayList<>(TransportHandlerRegistry.getHandlerNames());
        Collections.sort(transports);
        builder.append("\\begin{tabular}{@{}l@{\\hspace{1em}}l@{\\hspace{1em}}l@{}}\n");
        int i = 0;
        for (final String transport : transports) {
            if (i > 0) {
                builder.append(" & ");
            }
            builder.append(CheatSheetGenerator.sanitize(transport.toUpperCase()));
            if (i == 1) {
                builder.append(" \\\\\n");
                i = 0;
            }
            else {
                i++;
            }
        }
        builder.append("\\end{tabular}\n\n");
    }

    private static void buildOdysseusScript(final StringBuilder builder) {
        builder.append("\\section{Odysseus Script}\n");
        final OdysseusScriptParser parser = new OdysseusScriptParser();
        builder.append("\\subsection{Commands}\n");
        final List<String> commands = new ArrayList<>(parser.getKeywordNames());
        Collections.sort(commands);
        for (final String command : commands) {
            builder.append(" ").append(CheatSheetGenerator.sanitize(command.toUpperCase())).append("\\\\\n");
        }
        builder.append("\\subsection{Constants}\n");
        final List<String> constants = new ArrayList<>();
        for (final String key : ReplacementProviderManager.generateProviderMap().keySet()) {
            constants.add(key.substring(1));
        }
        Collections.sort(constants);
        for (final String constant : constants) {
            builder.append(" ").append(CheatSheetGenerator.sanitize(constant.toUpperCase())).append("\\\\\n");
        }
    }

    private static void buildSample(final StringBuilder builder) {
        builder.append("\\section{Sample Odysseus\\ query}\n");
        builder.append("\\begin{verbatim}\n");
        builder.append("#PARSER PQL\n");
        builder.append("#ADDQUERY\n\n");
        builder.append("input = ACCESS({source='source',\n");
        builder.append("        wrapper='GenericPush',\n");
        builder.append("        transport='File',\n");
        builder.append("        protocol='CSV',\n");
        builder.append("        dataHandler='Tuple',\n");
        builder.append("        options=[['filename','example.csv']],\n");
        builder.append("        schema=[['value','Double']]\n");
        builder.append("})\n");
        builder.append("output = MAP({expressions = ['value + 3']}, input)\n");
        builder.append("\\end{verbatim}\n\n");

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
        else if (types.equals(SDFDatatype.LISTS)) {
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
            if (types.length >= 1) {
                sb.append(types[0].getQualName());
            }
        }

        return sb.toString();
    }
}
