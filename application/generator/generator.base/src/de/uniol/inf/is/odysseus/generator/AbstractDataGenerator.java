package de.uniol.inf.is.odysseus.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.valuegenerator.DataType;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IMultiValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ISingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;

abstract public class AbstractDataGenerator implements IDataGenerator {

    private List<IValueGenerator> generators = new ArrayList<IValueGenerator>();
    private Map<IValueGenerator, DataType> datatypes = new HashMap<IValueGenerator, DataType>();
    private IProviderRunner runner;

    @Override
    public void init(IProviderRunner runner) {
        for (IValueGenerator gen : this.generators) {
            gen.init();
        }
        process_init();
    }

    public IProviderRunner getRunner() {
        return runner;
    }

    abstract protected void process_init();

    public List<DataTuple> buildDataTuple() {
        DataTuple tuple = new DataTuple();
        for (IValueGenerator v : this.generators) {
            DataType datatype = datatypes.get(v);
            double[] value = new double[0];
            if (v instanceof IMultiValueGenerator) {
                value = ((IMultiValueGenerator) v).nextValue();
            }
            else if (v instanceof ISingleValueGenerator) {
                value = new double[] { ((ISingleValueGenerator) v).nextValue() };
            }
            for (int i = 0; i < value.length; i++) {
                switch (datatype) {
                    case BYTE:
                        tuple.addByte(value[i]);
                        break;
                    case BOOLEAN:
                        tuple.addBoolean(value[i]);
                        break;
                    case DOUBLE:
                        tuple.addDouble(value[i]);
                        break;
                    case INTEGER:
                        tuple.addInteger(value[i]);
                        break;
                    case LONG:
                        tuple.addLong(value[i]);
                        break;
                    case OBJECT:
                        tuple.addAttribute(value[i]);
                        break;
                    case STRING:
                        tuple.addString(value[i]);
                        break;
                }
            }
        }
        return tuple.asList();
    }

    protected void addGenerator(IValueGenerator generator, DataType datatype) {
        this.generators.add(generator);
        this.datatypes.put(generator, datatype);
    }

    protected void addGenerator(IValueGenerator generator) {
        addGenerator(generator, DataType.DOUBLE);
    }

    protected void removeGenerator(IValueGenerator generator) {
        this.generators.remove(generator);
        this.datatypes.remove(generator);
    }

    public void pause(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSA() {
        return false;
    }

    @Override
    abstract public IDataGenerator newCleanInstance();

}
