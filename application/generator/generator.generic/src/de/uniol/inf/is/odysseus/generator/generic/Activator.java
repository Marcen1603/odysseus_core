package de.uniol.inf.is.odysseus.generator.generic;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.PredifinedValueGenerator;
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
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.PoissonDistributionGenerator;
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
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.ISAACGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.MersenneTwisterGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.Well1024aGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.Well19937aGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.Well19937cGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.Well44497aGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.Well44497bGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.random.Well512aGenerator;
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
    private static List<StreamServer> server = new ArrayList<>();

    private static Map<String, Class<?>> generators = new HashMap<>();
    static {
        // Misc
        Activator.generators.put("ConstantValueGenerator".toUpperCase(), ConstantValueGenerator.class);
        Activator.generators.put("PredifinedValueGenerator".toUpperCase(), PredifinedValueGenerator.class);
        Activator.generators.put("TimeGenerator".toUpperCase(), TimeGenerator.class);

        // Distribution
        Activator.generators.put("BetaDistributionGenerator".toUpperCase(), BetaDistributionGenerator.class);
        Activator.generators.put("BinomialDistributionGenerator".toUpperCase(), BinomialDistributionGenerator.class);
        Activator.generators.put("CauchyDistributionGenerator".toUpperCase(), CauchyDistributionGenerator.class);
        Activator.generators.put("ChiSquaredDistributionGenerator".toUpperCase(), ChiSquaredDistributionGenerator.class);
        Activator.generators.put("EmpiricalDistributionGenerator".toUpperCase(), EmpiricalDistributionGenerator.class);
        Activator.generators.put("EnumeratedIntegerDistributionGenerator".toUpperCase(), EnumeratedIntegerDistributionGenerator.class);
        Activator.generators.put("EnumeratedRealDistributionGenerator".toUpperCase(), EnumeratedRealDistributionGenerator.class);
        Activator.generators.put("ExponentialDistributionGenerator".toUpperCase(), ExponentialDistributionGenerator.class);
        Activator.generators.put("FDistributionGenerator".toUpperCase(), FDistributionGenerator.class);
        Activator.generators.put("GammaDistributionGenerator".toUpperCase(), GammaDistributionGenerator.class);
        Activator.generators.put("GaussianRandomGenerator".toUpperCase(), GaussianRandomGenerator.class);
        Activator.generators.put("GeometricDistributionGenerator".toUpperCase(), GeometricDistributionGenerator.class);
        Activator.generators.put("HypergeometricDistributionGenerator".toUpperCase(), HypergeometricDistributionGenerator.class);
        Activator.generators.put("LevyDistributionGenerator".toUpperCase(), LevyDistributionGenerator.class);
        Activator.generators.put("LogNormalDistributionGenerator".toUpperCase(), LogNormalDistributionGenerator.class);
        Activator.generators.put("MultivariateNormalDistributionGenerator".toUpperCase(), MultivariateNormalDistributionGenerator.class);
        Activator.generators.put("NormalDistributionGenerator".toUpperCase(), NormalDistributionGenerator.class);
        Activator.generators.put("ParetoDistributionGenerator".toUpperCase(), ParetoDistributionGenerator.class);
        Activator.generators.put("PascalDistributionGenerator".toUpperCase(), PascalDistributionGenerator.class);
        Activator.generators.put("PoissonDistributionGenerator".toUpperCase(), PoissonDistributionGenerator.class);
        Activator.generators.put("RayleighDistributionGenerator".toUpperCase(), RayleighDistributionGenerator.class);
        Activator.generators.put("TDistributionGenerator".toUpperCase(), TDistributionGenerator.class);
        Activator.generators.put("TriangularDistributionGenerator".toUpperCase(), TriangularDistributionGenerator.class);
        Activator.generators.put("UniformDistributionGenerator".toUpperCase(), UniformDistributionGenerator.class);
        Activator.generators.put("UniformIntegerDistributionGenerator".toUpperCase(), UniformIntegerDistributionGenerator.class);
        Activator.generators.put("UniformRealDistributionGenerator".toUpperCase(), UniformRealDistributionGenerator.class);
        Activator.generators.put("WeibullDistributionGenerator".toUpperCase(), WeibullDistributionGenerator.class);
        Activator.generators.put("ZipfDistributionGenerator".toUpperCase(), ZipfDistributionGenerator.class);

        // Evolve
        Activator.generators.put("AlternatingGenerator".toUpperCase(), AlternatingGenerator.class);
        Activator.generators.put("CosineGenerator".toUpperCase(), CosineGenerator.class);
        Activator.generators.put("DirichletEtaFunctionGenerator".toUpperCase(), DirichletEtaFunctionGenerator.class);
        Activator.generators.put("IncreaseGenerator".toUpperCase(), IncreaseGenerator.class);
        Activator.generators.put("PrimeGenerator".toUpperCase(), PrimeGenerator.class);
        Activator.generators.put("SineGenerator".toUpperCase(), SineGenerator.class);
        Activator.generators.put("StepIncreaseGenerator".toUpperCase(), StepIncreaseGenerator.class);
        Activator.generators.put("TangentGenerator".toUpperCase(), TangentGenerator.class);

        // Random
        Activator.generators.put("ISAACGenerator".toUpperCase(), ISAACGenerator.class);
        Activator.generators.put("MersenneTwisterGenerator".toUpperCase(), MersenneTwisterGenerator.class);
        Activator.generators.put("Well1024aGenerator".toUpperCase(), Well1024aGenerator.class);
        Activator.generators.put("Well19937aGenerator".toUpperCase(), Well19937aGenerator.class);
        Activator.generators.put("Well19937cGenerator".toUpperCase(), Well19937cGenerator.class);
        Activator.generators.put("Well44497aGenerator".toUpperCase(), Well44497aGenerator.class);
        Activator.generators.put("Well44497bGenerator".toUpperCase(), Well44497bGenerator.class);
        Activator.generators.put("Well512aGenerator".toUpperCase(), Well512aGenerator.class);

        // Spatial
        Activator.generators.put("MovingCircleGenerator".toUpperCase(), MovingCircleGenerator.class);
        Activator.generators.put("MovingPolynomialGenerator".toUpperCase(), MovingPolynomialGenerator.class);
        Activator.generators.put("WaypointGenerator".toUpperCase(), WaypointGenerator.class);

        // Switching
        Activator.generators.put("AlternatingDurationSwitchGenerator".toUpperCase(), AlternatingDurationSwitchGenerator.class);
        Activator.generators.put("SignGenerator".toUpperCase(), SignGenerator.class);
        Activator.generators.put("SwitchGenerator".toUpperCase(), SwitchGenerator.class);

    }

    static BundleContext getContext() {
        return Activator.context;
    }

    static Class<?> getGeneratorClass(final String name) {
        return Activator.generators.get(name);
    }

    static List<String> getGenerators() {
        List<String> generatorList = new ArrayList<>(Activator.generators.keySet());
        Collections.sort(generatorList);
        return generatorList;
    }

    static List<List<String>> getGeneratorParameters(final String name) {
        List<List<String>> parameters = new ArrayList<>();
        Class<?> generatorClass = Activator.generators.get(name);
        final Constructor<?>[] constructors = generatorClass.getDeclaredConstructors();

        for (final Constructor<?> constructor : constructors) {
            List<String> parameter = new ArrayList<>();
            final Class<?>[] params = constructor.getParameterTypes();
            for (int i = 1; i < params.length; i++) {
                parameter.add(params[i].getSimpleName());
            }
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        if (bundleContext != null) {
            Activator.context = bundleContext;

            bundleContext.registerService(CommandProvider.class.getName(), new ConsoleCommands(), null);
        }
        final StreamServer genericServer = new StreamServer(54325, new GenericProvider("schema.txt", 1000l));
        genericServer.start();
        Activator.server.add(genericServer);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

    /**
     *
     */
    public static void pause() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.pauseClients();
            }
        }
    }

    public static void proceed() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.proceedClients();
            }
        }
    }

    public static void stop() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.stopClients();
            }
        }
    }

    /**
     *
     */
    public static void printStatus() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.printStats();
            }
        }

    }
}
