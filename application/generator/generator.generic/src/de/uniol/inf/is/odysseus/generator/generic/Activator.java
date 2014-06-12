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
import de.uniol.inf.is.odysseus.generator.valuegenerator.TimeGenerator;
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
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.MultivariateNormalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.NormalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.ParetoDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.PascalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.RayleighDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.TDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.TriangularDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.UniformDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.UniformIntegerDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.UniformRealDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.WeibullDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.ZipfDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.CosineGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.DirichletEtaFunctionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.PrimeGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.SineGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.StepIncreaseGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.TangentGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.spatial.MovingCircleGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.spatial.MovingPolynomialGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.spatial.WaypointGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.switching.AlternatingDurationSwitchGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.switching.SignGenerator;
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
        generators.put("ConstantValueGenerator".toUpperCase(), ConstantValueGenerator.class);
        generators.put("BetaDistributionGenerator".toUpperCase(), BetaDistributionGenerator.class);
        generators.put("BinomialDistributionGenerator".toUpperCase(), BinomialDistributionGenerator.class);
        generators.put("CauchyDistributionGenerator".toUpperCase(), CauchyDistributionGenerator.class);
        generators.put("ChiSquaredDistributionGenerator".toUpperCase(), ChiSquaredDistributionGenerator.class);
        generators.put("EmpiricalDistributionGenerator".toUpperCase(), EmpiricalDistributionGenerator.class);
        generators.put("EnumeratedIntegerDistributionGenerator".toUpperCase(), EnumeratedIntegerDistributionGenerator.class);
        generators.put("EnumeratedRealDistributionGenerator".toUpperCase(), EnumeratedRealDistributionGenerator.class);
        generators.put("ExponentialDistributionGenerator".toUpperCase(), ExponentialDistributionGenerator.class);
        generators.put("FDistributionGenerator".toUpperCase(), FDistributionGenerator.class);
        generators.put("GammaDistributionGenerator".toUpperCase(), GammaDistributionGenerator.class);
        generators.put("GaussianRandomGenerator".toUpperCase(), GaussianRandomGenerator.class);
        generators.put("GeometricDistributionGenerator".toUpperCase(), GeometricDistributionGenerator.class);
        generators.put("HypergeometricDistributionGenerator".toUpperCase(), HypergeometricDistributionGenerator.class);
        generators.put("LevyDistributionGenerator".toUpperCase(), LevyDistributionGenerator.class);
        generators.put("LogNormalDistributionGenerator".toUpperCase(), LogNormalDistributionGenerator.class);
        generators.put("NormalDistributionGenerator".toUpperCase(), NormalDistributionGenerator.class);
        generators.put("ParetoDistributionGenerator".toUpperCase(), ParetoDistributionGenerator.class);
        generators.put("PascalDistributionGenerator".toUpperCase(), PascalDistributionGenerator.class);
        generators.put("RayleighDistributionGenerator".toUpperCase(), RayleighDistributionGenerator.class);
        generators.put("TDistributionGenerator".toUpperCase(), TDistributionGenerator.class);
        generators.put("TriangularDistributionGenerator".toUpperCase(), TriangularDistributionGenerator.class);
        generators.put("UniformDistributionGenerator".toUpperCase(), UniformDistributionGenerator.class);
        generators.put("UniformIntegerDistributionGenerator".toUpperCase(), UniformIntegerDistributionGenerator.class);
        generators.put("UniformRealDistributionGenerator".toUpperCase(), UniformRealDistributionGenerator.class);
        generators.put("WeibullDistributionGenerator".toUpperCase(), WeibullDistributionGenerator.class);
        generators.put("ZipfDistributionGenerator".toUpperCase(), ZipfDistributionGenerator.class);
        generators.put("AlternatingGenerator".toUpperCase(), AlternatingGenerator.class);
        generators.put("IncreaseGenerator".toUpperCase(), IncreaseGenerator.class);
        generators.put("StepIncreaseGenerator".toUpperCase(), StepIncreaseGenerator.class);
        generators.put("SineGenerator".toUpperCase(), SineGenerator.class);
        generators.put("CosineGenerator".toUpperCase(), CosineGenerator.class);
        generators.put("TangentGenerator".toUpperCase(), TangentGenerator.class);
        generators.put("PrimeGenerator".toUpperCase(), PrimeGenerator.class);
        generators.put("DirichletEtaFunctionGenerator".toUpperCase(), DirichletEtaFunctionGenerator.class);
        generators.put("TimeGenerator".toUpperCase(), TimeGenerator.class);
        generators.put("AlternatingDurationSwitchGenerator".toUpperCase(), AlternatingDurationSwitchGenerator.class);
        generators.put("SwitchGenerator".toUpperCase(), SwitchGenerator.class);
        generators.put("SignGenerator".toUpperCase(), SignGenerator.class);
        generators.put("MultivariateNormalDistributionGenerator".toUpperCase(), MultivariateNormalDistributionGenerator.class);
        generators.put("MovingCircleGenerator".toUpperCase(), MovingCircleGenerator.class);
        generators.put("MovingPolynomialGenerator".toUpperCase(), MovingPolynomialGenerator.class);
        generators.put("WaypointGenerator".toUpperCase(), WaypointGenerator.class);
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
        if (bundleContext != null) {
            Activator.context = bundleContext;

            bundleContext.registerService(CommandProvider.class.getName(), new ConsoleCommands(), null);
        }
        StreamServer genericServer = new StreamServer(54325, new GenericProvider("schema.txt", 1000l));
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
