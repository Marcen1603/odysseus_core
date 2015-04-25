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
package de.uniol.inf.is.odysseus.rcp.test.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Objects;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.test.Activator;
import de.uniol.inf.is.odysseus.rcp.test.model.TestModel;
import de.uniol.inf.is.odysseus.rcp.test.model.TestModel.AttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TestGenerator implements IRunnableWithProgress {
    static final Logger LOG = LoggerFactory.getLogger(TestGenerator.class);

    private final TestModel model;

    /**
     * Class constructor.
     *
     */
    public TestGenerator(/* @NonNull */final TestModel model) {
        Objects.requireNonNull(model);
        this.model = model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.beginTask("Generate test streams", this.model.getOperator().getMaxPorts() + (this.model.getMetadatas().size() * 2));
        final Path root = Paths.get(this.model.getDirectory()).toAbsolutePath();
        if (!root.toFile().exists()) {
            root.toFile().mkdirs();
        }
        for (int i = 0; i < this.model.getOperator().getMaxPorts(); i++) {
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
            monitor.worked(1);
        }
        monitor.setTaskName("Generate test queries");
        for (final String metadata : this.model.getMetadatas()) {
            final File output = root.resolve(this.model.getName() + "_" + metadata + ".qry").toFile();
            try {
                output.createNewFile();
                try (BufferedWriter out = new BufferedWriter(new FileWriter(output))) {
                    this.generateQuery(out, metadata);
                }
            }
            catch (final IOException e) {
                TestGenerator.LOG.error(e.getMessage(), e);
            }
            monitor.worked(1);
        }

        monitor.setTaskName("Generate test results");
        for (final String metadata : this.model.getMetadatas()) {
            final File input = root.resolve(this.model.getName() + "_" + metadata + ".qry").toFile();
            final File ouput = root.resolve(this.model.getName() + "_" + metadata + ".csv").toFile();

            try {
                try (BufferedReader in = new BufferedReader(new FileReader(input))) {
                    this.generateOutput(in, metadata, ouput);
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

    public void generateOutput(final BufferedReader in, final String metadata, final File output) throws IOException {
        final StringBuilder query = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            query.append(line.replace("${BUNDLE-ROOT}", output.getParentFile().getAbsolutePath())).append("\n");
        }
        query.append("file = SENDER({\n");
        query.append("    sink='sink',\n");
        query.append("    wrapper='GenericPush',\n");
        query.append("    transport='file',\n");
        query.append("    protocol='SimpleCSV',\n");
        query.append("    dataHandler='Tuple',\n");
        query.append("    options=[\n");
        query.append("        ['filename', '" + output.getAbsolutePath() + "'],\n");
        query.append("        ['csv.delimiter', ';'],\n");
        query.append("        ['csv.trim', 'true']\n");
        query.append("        ]}, output)\n");

        final IExecutor executor = Activator.getExecutor();
        executor.addQuery(query.toString(), "OdysseusScript", Activator.getSession(), Context.empty());

    }

    public void generateQuery(final BufferedWriter out, final String metadata) throws IOException {
        this.writeTestdata(out);

        this.writeHeader(out);
        this.writeMetadata(out, metadata);

        out.write("#RUNQUERY\n\n");

        for (int i = 0; i < this.model.getOperator().getMaxPorts(); i++) {
            this.writeAccess(i, out);
        }
        out.write("output = ");
        out.write(this.model.getOperator().getOperatorName().toUpperCase());
        out.write("(");
        if (this.model.getParameters().size() > 0) {
            out.write("{");
            final StringBuilder sb = new StringBuilder();
            for (final Entry<String, String> parameter : this.model.getParameters().entrySet()) {
                if ((parameter.getValue() != null) && (!"".equals(parameter.getValue()))) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    sb.append(parameter.getKey()).append("=").append(parameter.getValue());
                }
            }
            out.write(sb.toString());
            out.write("}");
        }
        out.write(", ");
        for (int i = 0; i < this.model.getOperator().getMaxPorts(); i++) {
            if (i != 0) {
                out.write(", ");
            }
            out.write("input" + i);
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
        out.write("#DROPALLSOURCES\n");
        out.write("#DROPALLQUERIES\n");
        out.write("\n");
    }

    private void writeMetadata(final BufferedWriter out, final String metadata) throws IOException {
        out.write("#METADATA ");
        out.write(metadata);
        out.write("\n");
    }

    private void writeAccess(final int port, final BufferedWriter out) throws IOException {
        out.write("input" + port + " = ACCESS({\n");
        out.write("    source='source',\n");
        out.write("    wrapper='GenericPull',\n");
        out.write("    transport='file',\n");
        out.write("    protocol='SimpleCSV',\n");
        out.write("    dataHandler='Tuple',\n");
        out.write("    options=[\n");
        out.write("        ['filename', '${BUNDLE-ROOT}/input" + port + ".csv'],\n");
        out.write("        ['csv.delimiter', ';'],\n");
        out.write("        ['csv.trim', 'true']\n");
        out.write("        ],\n");
        out.write("    schema=[");
        final StringBuilder sb = new StringBuilder();
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

    private Object[][] generateValues(final int port) {
        final int attributes = this.model.getSchema(port).size();
        final int num = (int) (Math.pow(2.0, attributes) * 4.0);
        final Object[][] values = new Object[num - 4][];
        int testCase = -1;
        for (int n = 0; n < num; n++) {
            final int pos = n % (int) (Math.pow(2.0, attributes));
            if (pos == 0) {
                testCase++;
            }
            else {
                final Object[] value = new Object[attributes];
                int i = 0;
                for (final Entry<String, AttributeParameter> attribute : this.model.getSchema(port).entrySet()) {
                    if ((n & (0x1 << i)) != 0) {
                        if (testCase == 0) {
                            value[i] = this.model.getMin(port, attribute.getKey());
                        }
                        else if (testCase == 1) {
                            value[i] = this.model.getMax(port, attribute.getKey());
                        }
                        else if (testCase == 2) {
                            if (attribute.getValue().isNullValue()) {
                                value[i] = null;
                            }
                            else {
                                value[i] = this.model.getZero(port, attribute.getKey());
                            }
                        }
                        else if (testCase == 3) {
                            value[i] = this.model.getZero(port, attribute.getKey());
                        }
                    }
                    else {
                        value[i] = this.model.getAverage(port, attribute.getKey());
                    }
                    i++;
                }
                values[n - (int) (1.0 + (n / (Math.pow(2, attributes))))] = value;
            }
        }
        return values;

    }

}
