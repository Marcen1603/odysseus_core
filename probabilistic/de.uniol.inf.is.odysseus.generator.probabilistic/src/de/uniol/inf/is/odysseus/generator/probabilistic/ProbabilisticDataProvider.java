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
        final String line = this.reader.readLine();
        if (line != null) {
            tuple.addLong(System.currentTimeMillis());
            final String[] values = line.split(",");
            for (final String value : values) {
                if (value.startsWith("(")) {
                    final String[] discreteValues = value.substring(1, value.length() - 1).split(";");
                    tuple.addInteger(discreteValues.length);
                    for (final String discreteValue2 : discreteValues) {
                        final String[] discreteValue = discreteValue2.split(":");
                        tuple.addDouble(discreteValue[0]);
                        tuple.addDouble(discreteValue[1]);
                    }
                }
                else {
                    final String[] continuousValue = value.split(":");
                    tuple.addDouble(continuousValue[0]);
                    tuple.addDouble(continuousValue[1]);
                }
            }
            return tuple;
        }
        else {
            this.close();
            this.init();
        }
        return null;
    }
}
