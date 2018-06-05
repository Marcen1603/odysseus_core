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
package de.uniol.inf.is.odysseus.developer.testcase.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.developer.testcase.Activator;
import de.uniol.inf.is.odysseus.developer.testcase.model.TestModel;
import de.uniol.inf.is.odysseus.developer.testcase.model.TestModel.AttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TestGenerator implements IRunnableWithProgress {
    static final Logger LOG = LoggerFactory.getLogger(TestGenerator.class);
    private static final long MAX_PROCESSING_TIME = 60000;
    private final TestModel model;

    private final IContainer container;

    /**
     * Class constructor.
     *
     */
    public TestGenerator(/* @NonNull */final IContainer container, /* @NonNull */final TestModel model) {
        Objects.requireNonNull(container);
        Objects.requireNonNull(model);
        this.container = container;
        this.model = model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.beginTask("Generate test streams", this.model.getOperator().getMaxPorts() + (this.model.getMetadatas().size() * 2));
        Path path = Paths.get(this.model.getDirectory());
        Path root;
        if (!path.isAbsolute()) {
            root = Paths.get(this.container.getRawLocation().toOSString(), this.model.getDirectory());
        }
        else {
            root = path.toAbsolutePath();
        }
        if (!root.toFile().exists()) {
            root.toFile().mkdirs();
        }
        for (int i = 0; i < this.model.getOperator().getMaxPorts() && i < 10; i++) {
            if (this.model.getSchema(i).size() > 0) {

                final File output = root.resolve("input" + i + ".csv").toFile();
                try {
                    output.createNewFile();
                    try (BufferedWriter out = new BufferedWriter(new FileWriter(output))) {
                        this.generateInput(i, out);
                    }
                }
                catch (final IOException e) {
                    TestGenerator.LOG.error(e.getMessage(), e);
                }
            }
            monitor.worked(1);
        }
        monitor.setTaskName("Generate test queries");

        List<List<String>> metadataPermutations = getMetadataCombinations(this.model.getMetadatas());
        for (final List<String> metadata : metadataPermutations) {
            final File output = root.resolve(getFilename(this.model, metadata) + ".qry").toFile();
            try {
                output.createNewFile();
                try (BufferedWriter out = new BufferedWriter(new FileWriter(output))) {
                    this.generateQuery(out, path, metadata);
                }
            }
            catch (final IOException e) {
                TestGenerator.LOG.error(e.getMessage(), e);
            }
            monitor.worked(1);
        }

        monitor.setTaskName("Generate test results");
        for (final List<String> metadata : metadataPermutations) {

            final File input = root.resolve(getFilename(this.model, metadata) + ".qry").toFile();
            final File ouput = root.resolve(getFilename(this.model, metadata) + ".csv").toFile();

            try {
                try (BufferedReader in = new BufferedReader(new FileReader(input))) {
                    this.generateOutput(in, this.container, ouput);
                }
            }
            catch (final IOException e) {
                TestGenerator.LOG.error(e.getMessage(), e);
            }
            monitor.worked(1);
        }
        monitor.done();
    }

    public void generateInput(final int port, final BufferedWriter out) throws IOException {
        final Object[][] values = this.generateValues(port);
        for (final Object[] line : values) {
            final StringBuilder sb = new StringBuilder();
            for (final Object value : line) {
                if (sb.length() != 0) {
                    sb.append("; ");
                }
                sb.append(value);
            }
            sb.append("\n");
            out.write(sb.toString());
        }
    }

    public void generateOutput(final BufferedReader in, final IContainer container, final File output) throws IOException {
        final StringBuilder query = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            query.append(line.replace("${BUNDLE-ROOT}", container.getRawLocation().toOSString())).append("\n");
        }
        query.append("file = SENDER({\n");
        query.append("    sink='sink',\n");
        query.append("    wrapper='GenericPush',\n");
        query.append("    transport='file',\n");
        query.append("    protocol='Text',\n");
        query.append("    dataHandler='Tuple',\n");
        query.append("    options=[\n");
        query.append("        ['filename', '" + output.getAbsolutePath() + "'],\n");
        query.append("        ['delimiter', '\n']\n");
        query.append("        ]}, output)\n");

        final IExecutor executor = Activator.getExecutor();
        try {
            Collection<Integer> ids = executor.addQuery(query.toString(), "OdysseusScript", Activator.getSession(), Context.empty());

            try {
                for (int id : ids) {
                    executor.startQuery(id, Activator.getSession());
                }
                boolean running = true;
                long time = 0;
                while ((running) && (time < MAX_PROCESSING_TIME)) {
                    running = false;
                    for (int id : ids) {
                        QueryState state = executor.getQueryState(id, Activator.getSession());
                        if (state == QueryState.RUNNING) {
                            running = true;
                        }
                    }
                    try {
                        Thread.sleep(100);
                        time += 100;
                    }
                    catch (InterruptedException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
            finally {
                for (int id : ids) {
                    executor.removeQuery(id, Activator.getSession());
                }
            }
        }
        catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void generateQuery(final BufferedWriter out, final Path root, final List<String> metadata) throws IOException {
        this.writeTestdata(out);

        this.writeHeader(out);
        // this.writeMetadata(out, metadata);

        out.write("#ADDQUERY\n\n");

        for (int i = 0; i < this.model.getOperator().getMaxPorts() && i < 10; i++) {
            if (this.model.getSchema(i).size() > 0) {
                this.writeAccess(i, root, metadata, out);
            }
        }
        out.write("output = ");
        out.write(this.model.getOperator().getOperatorName().toUpperCase());
        out.write("(");
        if (this.model.getParameters().size() > 0) {
            final StringBuilder sb = new StringBuilder();
            for (final Entry<String, String> parameter : this.model.getParameters().entrySet()) {
                if ((parameter.getValue() != null) && (!"".equals(parameter.getValue()))) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    sb.append(parameter.getKey()).append("=").append(parameter.getValue());
                }
            }
            if (sb.length() > 0) {
                out.write("{");
                out.write(sb.toString());
                out.write("}");
                out.write(", ");
            }
        }
        for (int i = 0; i < this.model.getOperator().getMaxPorts() && i < 10; i++) {
            if (this.model.getSchema(i).size() > 0) {
                if (i != 0) {
                    out.write(", ");
                }
                if (this.model.getWindow(i) > 0) {
                    out.write("ELEMENTWINDOW({SIZE = " + this.model.getWindow(i) + "}, input" + i + ")");
                }
                else {
                    out.write("input" + i);
                }
            }
        }
        out.write(")\n");
    }

    private void writeTestdata(final BufferedWriter out) throws IOException {
        out.write("/// Odysseus Testcase: " + model.getName() + "\n");
        out.write("/// Operator: " + model.getOperator().getOperatorName() + "\n");
        out.write("/// Date: " + (new Date()).toString() + "\n");
        out.write("/// User: " + System.getProperty("user.name") + "\n");
        out.write("/// Parameter: \n");
        for (Entry<String, String> parameter : model.getParameters().entrySet()) {
            out.write("///  " + parameter.getKey() + ": " + parameter.getValue() + "\n");
        }
        out.write("\n\n");

    }

    private void writeHeader(final BufferedWriter out) throws IOException {
        out.write("#PARSER PQL\n\n");
        out.write("#DROPALLQUERIES\n");
        out.write("#DROPALLSINKS\n");
        out.write("#DROPALLSOURCES\n");
        out.write("\n");
    }

    @SuppressWarnings("unused")
    @Deprecated
    private void writeMetadata(final BufferedWriter out, final List<String> metadatas) throws IOException {
        for (String metadata : metadatas) {
            out.write("#METADATA ");
            out.write(metadata);
            out.write("\n");
        }
    }

    private void writeAccess(final int port, final Path root, List<String> metadatas, final BufferedWriter out) throws IOException {
        out.write("input" + port + " = ACCESS({\n");

        out.write("    source='source" + port + "_" + metadatasToString(metadatas) + "',\n");
        out.write("    wrapper='GenericPull',\n");
        out.write("    transport='file',\n");
        out.write("    protocol='SimpleCSV',\n");
        out.write("    dataHandler='Tuple',\n");
        out.write("    metaattribute=[\n");
        StringBuilder sb = new StringBuilder();
        for (String metadata : metadatas) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append("'").append(metadata).append("'");
        }
        out.write("                  " + sb.toString() + "\n");
        out.write("                  ],\n");
        out.write("    options=[\n");
        out.write("        ['filename', '${BUNDLE-ROOT}/" + root.toString() + "/input" + port + ".csv'],\n");
        out.write("        ['csv.delimiter', ';'],\n");
        out.write("        ['csv.trim', 'true']\n");
        out.write("        ],\n");
        out.write("    schema=[");
        sb = new StringBuilder();
        if (this.model.isTimestamp()) {
            sb.append("['timestamp', 'STARTTIMESTAMP']");
        }
        for (final Entry<String, AttributeParameter> attribute : this.model.getSchema(port).entrySet()) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append("['");
            sb.append(attribute.getKey());
            sb.append("', '");
            sb.append(attribute.getValue().getType().getQualName());
            sb.append("']");
        }
        out.write(sb.toString());
        out.write("]})\n\n");
    }

    private String metadatasToString(List<String> metadatas) {
        StringBuilder sb = new StringBuilder();
        for (String metadata : metadatas) {
            if (sb.length() != 0) {
                sb.append("_");
            }
            sb.append(metadata);
        }
        return sb.toString();
    }

    private Object[][] generateValues(final int port) {
        final int attributes = this.model.getSchema(port).size();
        if (attributes == 0) {
            return new Object[0][0];
        }
        final int num = (int) (Math.pow(2.0, attributes) * 4.0);
        final Object[][] values = new Object[num - 4][];
        int testCase = -1;
        for (int n = 0; n < num; n++) {
            final int pos = n % (int) (Math.pow(2.0, attributes));
            if (pos == 0) {
                testCase++;
            }
            else {
                final Object[] value;
                int offset = 0;
                if (this.model.isTimestamp()) {
                    value = new Object[attributes + 1];
                    offset = 1;
                }
                else {
                    value = new Object[attributes];
                }
                int i = 0;
                for (final Entry<String, AttributeParameter> attribute : this.model.getSchema(port).entrySet()) {
                    if ((n & (0x1 << i)) != 0) {
                        if (testCase == 0) {
                            value[offset + i] = this.model.getMin(port, attribute.getKey());
                        }
                        else if (testCase == 1) {
                            value[offset + i] = this.model.getMax(port, attribute.getKey());
                        }
                        else if (testCase == 2) {
                            if (attribute.getValue().isNullValue()) {
                                value[offset + i] = null;
                            }
                            else {
                                value[offset + i] = this.model.getZero(port, attribute.getKey());
                            }
                        }
                        else if (testCase == 3) {
                            value[offset + i] = this.model.getZero(port, attribute.getKey());
                        }
                    }
                    else {
                        value[offset + i] = this.model.getAverage(port, attribute.getKey());
                    }
                    i++;
                }
                values[n - (int) (1.0 + (n / (Math.pow(2, attributes))))] = value;
                if (this.model.isTimestamp()) {
                    values[n - (int) (1.0 + (n / (Math.pow(2, attributes))))][0] = n - (int) (1.0 + (n / (Math.pow(2, attributes))));
                }
            }
        }
        return values;

    }

    private String getFilename(TestModel model, List<String> metadatas) {
        StringBuilder sb = new StringBuilder();
        for (String metadata : metadatas) {
            if (sb.length() > 0) {
                sb.append("_");
            }
            sb.append(metadata);
        }
        return this.model.getName() + "_" + sb.toString();
    }

    private List<List<String>> getMetadataCombinations(Collection<String> metadatas) {
        final int num = (int) (Math.pow(2.0, metadatas.size()));
        final List<List<String>> values = new ArrayList<>(num);

        for (int n = 0; n < num; n++) {
            final int pos = n % (int) (Math.pow(2.0, metadatas.size()));
            if (pos != 0) {
                final List<String> value = new ArrayList<>(metadatas.size());

                int i = 0;
                for (final String metadata : metadatas) {
                    if ((n & (0x1 << i)) != 0) {
                        value.add(metadata);
                    }
                    i++;
                }
                values.add(value);
            }
        }

        return values;
    }

}
