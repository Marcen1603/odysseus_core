package de.uniol.inf.is.odysseus.probabilistic.test.continuous.physicaloperator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticProjectPO;

@Test
public class TestProject {
	@SuppressWarnings("rawtypes")
	@Test(dataProvider = "tuple")
	public final void testProject(
			final ProbabilisticTuple<IMetaAttribute> tuple, int[] projectMatrix) {
		try {
			Class<ProbabilisticProjectPO> operator = ProbabilisticProjectPO.class;
			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = projectMatrix.getClass();
			Constructor<ProbabilisticProjectPO> constructor = operator
					.getConstructor(parameterTypes);

			ProbabilisticProjectPO obj = constructor.newInstance(projectMatrix);

			Method processMethod = ProbabilisticProjectPO.class
					.getDeclaredMethod("process_next", new Class[] {
							ProbabilisticTuple.class, Integer.TYPE });
			processMethod.setAccessible(true);
			processMethod.invoke(obj, new Object[] { tuple, new Integer(0) });
			
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(tuple);
	}

	@DataProvider(name = "tuple")
	public final Object[][] provideTuple() {
		return new Object[][] { { provideSimpleTuple(), new int[] { 0 } },
				{ provideUnivariateTuple(), new int[] { 0 } },
				{ provideMultivariateTuple(), new int[] { 0 } } };
	}

	private IStreamObject<?> provideSimpleTuple() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0 },
				new CovarianceMatrix(new double[] { 1.5 }));
		final Object[] attrs = new Object[] { new ProbabilisticContinuousDouble(
				0) };
		mixture.setAttributes(new int[] { 0 });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);

		return tuple;
	}

	private IStreamObject<?> provideUnivariateTuple() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0 },
				new CovarianceMatrix(new double[] { 1.5 }));
		final Object[] attrs = new Object[] { "FirstAttribute",
				new ProbabilisticContinuousDouble(0), "ThirdAttribute" };
		mixture.setAttributes(new int[] { 1 });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);

		return tuple;
	}

	private IStreamObject<?> provideMultivariateTuple() {
		final NormalDistributionMixture mixture = new NormalDistributionMixture(
				new double[] { 2.0, 3.0 }, new CovarianceMatrix(new double[] {
						1.5, 2.0, 2.5 }));
		final Object[] attrs = new Object[] { "FirstAttribute",
				new ProbabilisticContinuousDouble(0), "ThirdAttribute",
				new ProbabilisticContinuousDouble(1) };
		mixture.setAttributes(new int[] { 1, 3 });
		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture }, true);

		return tuple;
	}

	private void appendDistribution(ProbabilisticTuple<?> tuple,
			NormalDistributionMixture mixture) {
		int distributionIndex = tuple.getDistributions().length;
		tuple.append(new ProbabilisticContinuousDouble(distributionIndex),
				mixture, false);
	}
}
