package de.uniol.inf.is.odysseus.generator.probabilistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDataProvider extends StreamClientHandler {
    private BufferedReader reader;

    public ProbabilisticDataProvider() {

    }

    @Override
    public synchronized List<DataTuple> next() {
        final List<DataTuple> tuples = new ArrayList<DataTuple>();
        DataTuple tuple = null;
        try {
            tuple = this.generateDataTuple();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        if (tuple != null) {
            tuples.add(tuple);
        }
        else {
            this.close();
            this.init();
        }
        try {
            Thread.sleep(500);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        return tuples;
    }

    @Override
    public void init() {
        final URL fileURL = Activator.getContext().getBundle().getEntry("/data/data");
        try {
            final InputStream inputStream = fileURL.openConnection().getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (this.reader != null) {
            try {
                this.reader.close();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            finally {
                this.reader = null;
            }
        }
    }

    @Override
    public ProbabilisticDataProvider clone() {
        return new ProbabilisticDataProvider();
    }

    private DataTuple generateDataTuple() throws IOException {
        final DataTuple tuple = new DataTuple();
        String line = null;
        while (line == null) {
            line = this.reader.readLine();
            if (line == null) {
                break;
            }
            if (line.startsWith("#")) {
                line = null;
            }
        }
        if (line != null) {
            tuple.addLong(System.currentTimeMillis());
            final String[] values = line.split(",");
            for (final String value : values) {
                if (value.startsWith("(")) {
                    final String[] probabilisticValues = value.substring(1, value.length() - 1).split(";");
                    if (probabilisticValues.length > 0) {
                        if (probabilisticValues[0].split(":").length == 2) {
                            // Send discrete probabilistic value
                            tuple.addInteger(probabilisticValues.length);
                            for (final String probabilisticValue : probabilisticValues) {
                                final String[] probabilisticParameter = probabilisticValue.split(":");
                                // The value
                                tuple.addDouble(probabilisticParameter[0]);
                                // The probability
                                tuple.addDouble(probabilisticParameter[1]);
                            }
                        }
                        else {
                            // Send continuous probabilistic value
                            tuple.addInteger(probabilisticValues.length);
                            for (final String probabilisticValue : probabilisticValues) {
                                final String[] probabilisticParameter = probabilisticValue.split(":");
                                // The Covariance ID
                                tuple.addInteger(probabilisticParameter[0]);
                                // The Covariance Index
                                tuple.addInteger(probabilisticParameter[1]);
                                // The mean
                                tuple.addDouble(probabilisticParameter[2]);
                                // The probability
                                tuple.addDouble(probabilisticParameter[3]);
                            }
                        }
                    }
                }
                else {
                    final String[] parameters = value.split(":");
                    if (parameters.length > 1) {
                        // The Covariance ID
                        tuple.addInteger(parameters[0]);
                        // Number of Covariance Matrix entries (Triangle)
                        tuple.addInteger(parameters.length - 1);
                        for (int i = 0; i < parameters.length - 1; i++) {
                            // The Covariance Entry i
                            tuple.addDouble(parameters[1 + i]);
                        }
                    }
                    else {
                        tuple.addDouble(value);
                    }
                }
            }
            return tuple;
        }
        return null;
    }
}
