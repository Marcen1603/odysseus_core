package de.uniol.inf.is.odysseus.generator.probabilistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;

public class ProbabilisticDataProvider extends StreamClientHandler {
    private BufferedReader reader;

    public ProbabilisticDataProvider() {

    }

    @Override
    public synchronized List<DataTuple> next() {
        List<DataTuple> tuples = new ArrayList<DataTuple>();

        try {
            tuples.add(generateDataTuple());
        }
        catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tuples;
    }

    @Override
    public void init() {
        URL fileURL = Activator.getContext().getBundle().getEntry("/data/data");
        try {
            InputStream inputStream = fileURL.openConnection().getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (this.reader != null) {
            try {
                this.reader.close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
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

    @SuppressWarnings("unchecked")
    private DataTuple generateDataTuple() throws IOException {
        DataTuple tuple = new DataTuple();
        tuple.addAttribute(new Double(1.0));

        String line = this.reader.readLine();
        if (line != null) {
            String[] values = line.split(",");
            for (String value : values) {
                if (value.startsWith("(")) {
                    String[] discreteValues = value.substring(1, value.length() - 2).split(";");
                    Pair<Double, Double>[] discreteProbabilisticValue = new Pair[discreteValues.length];
                    for (int i = 0; i < discreteValues.length; i++) {

                        String[] discreteValue = discreteValues[i].split(":");
                        discreteProbabilisticValue[i] = new Pair<Double, Double>(Double.parseDouble(discreteValue[0]),
                                Double.parseDouble(discreteValue[1]));
                    }
                    tuple.addAttribute(new ProbabilisticDouble(discreteProbabilisticValue));
                }
                else {
                    String[] continuousValue = value.split(":");
                    tuple.addAttribute(new ProbabilisticContinuousDouble(Double.parseDouble(continuousValue[0]), Double
                            .parseDouble(continuousValue[1])));

                }
            }
            System.out.println(line);
        }
        return tuple;
    }
}
