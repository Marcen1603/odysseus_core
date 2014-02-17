/**
 * 
 */
package de.uniol.inf.is.odysseus.generator.clock;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.TimeGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ClockProvider extends AbstractDataGenerator {
    private IValueGenerator generator;

    /**
     * 
     */
    public ClockProvider() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<DataTuple> next() {
        DataTuple tuple = new DataTuple();
        tuple.addLong(this.generator.nextValue());
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DataTuple> list = new ArrayList<DataTuple>();
        list.add(tuple);
        return list;
    }

    @Override
    public void process_init() {
        generator = new TimeGenerator(new NoError());
    }

    @Override
    public void close() {

    }

    @Override
    public ClockProvider newCleanInstance() {
        return new ClockProvider();
    }

    public static void main(final String[] args) {
        Activator activator = new Activator();
        try {
            activator.start(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
