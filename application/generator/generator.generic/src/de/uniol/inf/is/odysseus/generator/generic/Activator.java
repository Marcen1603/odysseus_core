package de.uniol.inf.is.odysseus.generator.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.BetaDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.BinomialDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.CauchyDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.ChiSquaredDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.EmpiricalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.EnumeratedIntegerDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.EnumeratedRealDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.ExponentialDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.FDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.GammaDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.GaussianRandomGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.GeometricDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.HypergeometricDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.LevyDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.LogNormalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.NormalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.ParetoDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.PascalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.TDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.TriangularDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.UniformDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.UniformIntegerDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.UniformRealDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.WeibullDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.ZipfDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.switching.AlternatingDurationSwitchGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.switching.SwitchGenerator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Activator implements BundleActivator {

    private static BundleContext context;
    private static List<StreamServer> server = new ArrayList<StreamServer>();

    private static Map<String, Class<?>> generators = new HashMap<String, Class<?>>();
    static {
        generators.put("ConstantValueGenerator", ConstantValueGenerator.class);
        generators.put("BetaDistributionGenerator", BetaDistributionGenerator.class);
        generators.put("BinomialDistributionGenerator", BinomialDistributionGenerator.class);
        generators.put("CauchyDistributionGenerator", CauchyDistributionGenerator.class);
        generators.put("ChiSquaredDistributionGenerator", ChiSquaredDistributionGenerator.class);
        generators.put("EmpiricalDistributionGenerator", EmpiricalDistributionGenerator.class);
        generators.put("EnumeratedIntegerDistributionGenerator", EnumeratedIntegerDistributionGenerator.class);
        generators.put("EnumeratedRealDistributionGenerator", EnumeratedRealDistributionGenerator.class);
        generators.put("ExponentialDistributionGenerator", ExponentialDistributionGenerator.class);
        generators.put("FDistributionGenerator", FDistributionGenerator.class);
        generators.put("GammaDistributionGenerator", GammaDistributionGenerator.class);
        generators.put("GaussianRandomGenerator", GaussianRandomGenerator.class);
        generators.put("GeometricDistributionGenerator", GeometricDistributionGenerator.class);
        generators.put("HypergeometricDistributionGenerator", HypergeometricDistributionGenerator.class);
        generators.put("LevyDistributionGenerator", LevyDistributionGenerator.class);
        generators.put("LogNormalDistributionGenerator", LogNormalDistributionGenerator.class);
        generators.put("NormalDistributionGenerator", NormalDistributionGenerator.class);
        generators.put("ParetoDistributionGenerator", ParetoDistributionGenerator.class);
        generators.put("PascalDistributionGenerator", PascalDistributionGenerator.class);
        generators.put("TDistributionGenerator", TDistributionGenerator.class);
        generators.put("TriangularDistributionGenerator", TriangularDistributionGenerator.class);
        generators.put("UniformDistributionGenerator", UniformDistributionGenerator.class);
        generators.put("UniformIntegerDistributionGenerator", UniformIntegerDistributionGenerator.class);
        generators.put("UniformRealDistributionGenerator", UniformRealDistributionGenerator.class);
        generators.put("WeibullDistributionGenerator", WeibullDistributionGenerator.class);
        generators.put("ZipfDistributionGenerator", ZipfDistributionGenerator.class);
        generators.put("AlternatingGenerator", AlternatingGenerator.class);
        generators.put("IncreaseGenerator", IncreaseGenerator.class);
        generators.put("AlternatingDurationSwitchGenerator", AlternatingDurationSwitchGenerator.class);
        generators.put("SwitchGenerator", SwitchGenerator.class);
    }

    static BundleContext getContext() {
        return context;
    }

    static Class<?> getGeneratorClass(String name) {
        return generators.get(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;

        bundleContext.registerService(CommandProvider.class.getName(), new ConsoleCommands(), null);

        StreamServer genericServer = new StreamServer(54325, new GenericProvider("schema.txt"));
        genericServer.start();
        server.add(genericServer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

    /**
     * 
     */
    public static void pause() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.pauseClients();
            }
        }
    }

    public static void proceed() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.proceedClients();
            }
        }
    }

    public static void stop() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.stopClients();
            }
        }
    }

    /**
     * 
     */
    public static void printStatus() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.printStats();
            }
        }

    }

}
