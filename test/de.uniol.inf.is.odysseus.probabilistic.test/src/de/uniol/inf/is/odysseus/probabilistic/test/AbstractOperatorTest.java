/**
 * Copyright 2017 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.common.base.Stopwatch;
import com.google.common.reflect.ClassPath;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.ProbabilisticFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticRelationalPredicate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
abstract public class AbstractOperatorTest {
    protected Optional<SDFSchema> schema = Optional.empty();
    protected Optional<SDFSchema> otherSchema = Optional.empty();

    protected Optional<ProbabilisticRelationalPredicate> predicate = Optional.empty();
    protected Optional<ProbabilisticRelationalPredicate> otherPredicate = Optional.empty();

    protected Optional<AbstractPipe<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<IProbabilistic>>> operator;
    protected Optional<AbstractPipe<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<IProbabilistic>>> otherOperator;
    protected Optional<ProbabilisticTuple<IProbabilistic>> inputTuple = Optional.empty();

    protected Optional<IPunctuation> lastPunctuation = Optional.empty();
    protected Optional<IStreamObject<?>> lastObject = Optional.empty();

    protected boolean isSemanticallyEqual;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        for (final FunctionSignature signature : MEP.getFunctions()) {
            MEP.unregisterFunction(MEP.getFunction(signature));
        }
        final ClassLoader classLoader = MEP.class.getClassLoader();
        for (final ClassPath.ClassInfo classInfo : ClassPath.from(classLoader).getTopLevelClasses()) {
            if (classInfo.getName().startsWith("de.uniol.inf.is.odysseus.mep.functions.")) {
                final Class<?> classObject = classInfo.load();
                if (IMepFunction.class.isAssignableFrom(classObject) && !Modifier.isAbstract(classObject.getModifiers())) {
                    try {
                        MEP.registerFunction((IMepFunction<?>) classObject.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        final ProbabilisticFunctionProvider probabilisticFunctionProvider = new ProbabilisticFunctionProvider();
        for (final IMepFunction<?> function : probabilisticFunctionProvider.getFunctions()) {
            MEP.registerFunction(function);
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        for (final FunctionSignature signature : MEP.getFunctions()) {
            MEP.unregisterFunction(MEP.getFunction(signature));
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.lastPunctuation = Optional.empty();
        this.lastObject = Optional.empty();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    protected void givenSchema(final SDFAttribute... attributes) {
        this.schema = Optional.of(SDFSchemaFactory.createNewSchema("test", ProbabilisticTuple.class, Arrays.asList(attributes)));
    }

    protected void givenOtherSchema(final SDFAttribute... attributes) {
        this.otherSchema = Optional.of(SDFSchemaFactory.createNewSchema("test", ProbabilisticTuple.class, Arrays.asList(attributes)));
    }

    protected void givenPredicate(final String predicate) {
        final IAttributeResolver resolver = new DirectAttributeResolver(this.schema.get());

        final SDFProbabilisticExpression expression = new SDFProbabilisticExpression(new SDFExpression(predicate, resolver, MEP.getInstance()));
        this.predicate = Optional.of(new ProbabilisticRelationalPredicate(expression));
    }

    protected void givenOtherPredicate(final String predicate) {
        final SDFProbabilisticExpression expression = new SDFProbabilisticExpression(predicate, MEP.getInstance());
        this.otherPredicate = Optional.of(new ProbabilisticRelationalPredicate(expression));
    }

    protected void givenInputTupleWithValues(final Object... elements) {
        final boolean requiresDeepClone = true;

        final Object[] attributes = new Object[elements.length];
        final List<MultivariateMixtureDistribution> distributions = new ArrayList<>();
        int dimension = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] instanceof MultivariateMixtureDistribution) {
                if (!distributions.contains(elements[i])) {
                    distributions.add((MultivariateMixtureDistribution) elements[i]);
                }
                attributes[i] = new ProbabilisticDouble(distributions.size() - 1);
                ((MultivariateMixtureDistribution) elements[i]).setAttribute(i, dimension);
                dimension++;
            } else {
                attributes[i] = elements[i];
            }
        }

        this.inputTuple = Optional.of(new ProbabilisticTuple<IProbabilistic>(attributes, distributions.toArray(new MultivariateMixtureDistribution[distributions.size()]), requiresDeepClone));
        this.inputTuple.get().setMetadata(new Probabilistic(1.0));
    }

    protected void givenInputTupleWithSeparateDistributions(final Object... elements) {
        final boolean requiresDeepClone = true;

        final Object[] attributes = new Object[elements.length];
        final List<MultivariateMixtureDistribution> distributions = new ArrayList<>();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] instanceof MultivariateMixtureDistribution) {
                distributions.add((MultivariateMixtureDistribution) elements[i]);
                attributes[i] = new ProbabilisticDouble(distributions.size() - 1);
                ((MultivariateMixtureDistribution) elements[i]).setAttribute(0, i);
            } else {
                attributes[i] = elements[i];
            }
        }

        this.inputTuple = Optional.of(new ProbabilisticTuple<IProbabilistic>(attributes, distributions.toArray(new MultivariateMixtureDistribution[distributions.size()]), requiresDeepClone));
        this.inputTuple.get().setMetadata(new Probabilistic(1.0));
    }

    protected void whenProcessTuple() {
        System.out.println(String.format("process_next(%s,%s)", this.inputTuple.get(), 0));

        try {
            final Method processNextMethod = this.operator.get().getClass().getDeclaredMethod("process_next", IStreamObject.class, int.class);

            processNextMethod.setAccessible(true);
            this.lastObject = Optional.empty();

            final Stopwatch timer = Stopwatch.createStarted();

            try {
                processNextMethod.invoke(this.operator.get(), this.inputTuple.get(), 0);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
            timer.stop();
            System.out.println("Test took: " + timer);
        } catch (NoSuchMethodException | SecurityException e) {
            fail(e.getMessage());
        }

    }

    protected void whenProcessPunctuationAt(final Date date) {
        System.out.println(String.format("processPunctuation(%s,%s)", date, 0));
        final IPunctuation punctuation = Heartbeat.createNewHeartbeat(date.getTime());
        this.lastPunctuation = Optional.empty();

        final Stopwatch timer = Stopwatch.createStarted();

        this.operator.get().processPunctuation(punctuation, 0);

        timer.stop();
        System.out.println("Test took: " + timer);
    }

    protected void whenProcessOpen() {
        System.out.println("process_open");

        try {
            final Method processNextMethod = this.operator.get().getClass().getDeclaredMethod("process_open", new Class<?>[] {});
            processNextMethod.setAccessible(true);

            final Stopwatch timer = Stopwatch.createStarted();
            try {
                processNextMethod.invoke(this.operator.get());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                fail(e.getMessage());
            }
            timer.stop();
            System.out.println("Test took: " + timer);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    protected void whenProcess_isSemanticallyEqual() {
        System.out.println(String.format("Process_isSemanticallyEqual(%s)", this.otherOperator.get()));
        final Stopwatch timer = Stopwatch.createStarted();

        this.isSemanticallyEqual = this.operator.get().process_isSemanticallyEqual(this.otherOperator.get());

        timer.stop();
        System.out.println("Test took: " + timer);
    }

    protected void thenOutputModeIs(final OutputMode outputMode) {
        assertThat("Invalid output mode", this.operator.get().getOutputMode(), is(outputMode));
    }

    protected void thenOperatorsAreSemanticallyEqual() {
        assertThat("Operators are not semantically equal", this.operator.get().isSemanticallyEqual(this.otherOperator.get()), is(true));
    }

    protected void thenOperatorsAreNotSemanticallyEqual() {
        assertThat("Operators are semantically equal", this.operator.get().isSemanticallyEqual(this.otherOperator.get()), is(not(true)));
    }

    protected void thenProcess_isSemanticallyEqualReturns(final boolean value) {
        assertThat("process_isSemanticallyEqual for other operator is invalid", this.isSemanticallyEqual, is(value));
    }

    protected void thenPunctuationEquals(final Date date) {
        if (date == null) {
            assertThat("Mismatching punctuation", this.lastPunctuation.isPresent(), is(false));
        } else {
            final IPunctuation punctuation = Heartbeat.createNewHeartbeat(date.getTime());
            assertThat("Invalid emitted output punctuation", this.lastPunctuation.get().getTime(), is(punctuation.getTime()));
            System.out.println(String.format("Result: %s", this.lastPunctuation.get()));
        }
    }

    @SuppressWarnings("unchecked")
    protected void thenOutputEquals(final double existence, final Object... elements) {
        if (elements == null) {
            assertThat("Mismatching tuple", this.lastObject.isPresent(), is(false));
        } else {
            assertThat("Missing output tuple", this.lastObject.isPresent(), is(true));
            final boolean requiresDeepClone = true;
            final Object[] attributes = new Object[elements.length];
            final List<MultivariateMixtureDistribution> distributions = new ArrayList<>();
            int dimension = 0;
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] instanceof MultivariateMixtureDistribution) {
                    if (!distributions.contains(elements[i])) {
                        distributions.add((MultivariateMixtureDistribution) elements[i]);
                    }
                    attributes[i] = new ProbabilisticDouble(distributions.size() - 1);
                    ((MultivariateMixtureDistribution) elements[i]).setAttribute(i, dimension);
                    dimension++;
                } else {
                    attributes[i] = elements[i];
                }
            }
            final ProbabilisticTuple<IProbabilistic> output = new ProbabilisticTuple<>(attributes, distributions.toArray(new MultivariateMixtureDistribution[distributions.size()]), requiresDeepClone);
            output.setMetadata(new Probabilistic(existence));

            System.out.println(String.format("Expected: %s", output));
            System.out.println(String.format("Result: %s", this.lastObject.get()));

            assertThat("Created output tuple has an invalid existence metadata value", output.getMetadata().getExistence(), is(existence));
            assertThat("Last emitted output tuple has an invalid existence metadata value", ((ProbabilisticTuple<IProbabilistic>) this.lastObject.get()).getMetadata().getExistence(),
                    is(closeTo(existence, 0.1)));
            assertThat("Invalid emitted output tuple", this.lastObject.get(), is(output));

            int successCounter = output.getDistributions().length;
            for (MultivariateMixtureDistribution distribution : ((ProbabilisticTuple<IProbabilistic>) this.lastObject.get()).getDistributions()) {
                for (MultivariateMixtureDistribution expectedDistribution : output.getDistributions()) {
                    boolean equal = true;

                    if (Math.abs(distribution.getScale() - expectedDistribution.getScale()) > 0.01) {
                        equal = false;
                    }
                    if (!Arrays.equals(distribution.getSupport(), expectedDistribution.getSupport())) {
                        equal = false;
                    }
                    if (!(new HashSet<>(distribution.getComponents()).equals(new HashSet<>(expectedDistribution.getComponents())))) {
                        equal = false;
                    }
                    if (equal) {
                        successCounter--;
                        break;
                    }
                }
            }
            if (successCounter > 0) {
                assertThat("Invalid emitted output distributions", ((ProbabilisticTuple<IProbabilistic>) this.lastObject.get()).getDistributions(), arrayContainingInAnyOrder(output.getDistributions()));
            }

            for (MultivariateMixtureDistribution distribution : ((ProbabilisticTuple<IProbabilistic>) this.lastObject.get()).getDistributions()) {
                final double[] lowerBound = new double[distribution.getDimension()];
                Arrays.fill(lowerBound, Double.NEGATIVE_INFINITY);
                final double[] upperBound = new double[distribution.getDimension()];
                Arrays.fill(upperBound, Double.POSITIVE_INFINITY);
                assertThat(String.format("Probability of %s is not equals to 1", distribution), distribution.probability(lowerBound, upperBound) * distribution.getScale(), is(closeTo(1.0, 0.01)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void thenOutputWithMultipleDistributionsEquals(final double existence, final Object... elements) {
        if (elements == null) {
            assertThat("Mismatching tuple", this.lastObject.isPresent(), is(false));
        } else {
            assertThat("Missing output tuple", this.lastObject.isPresent(), is(true));
            final boolean requiresDeepClone = true;
            final Object[] attributes = new Object[elements.length];
            final List<MultivariateMixtureDistribution> distributions = new ArrayList<>();
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] instanceof MultivariateMixtureDistribution) {
                    distributions.add((MultivariateMixtureDistribution) elements[i]);
                    attributes[i] = new ProbabilisticDouble(distributions.size() - 1);
                    ((MultivariateMixtureDistribution) elements[i]).setAttribute(0, i);
                } else {
                    attributes[i] = elements[i];
                }
            }

            final ProbabilisticTuple<IProbabilistic> output = new ProbabilisticTuple<>(attributes, distributions.toArray(new MultivariateMixtureDistribution[distributions.size()]), requiresDeepClone);
            output.setMetadata(new Probabilistic(existence));

            System.out.println(String.format("Expected: %s", output));
            System.out.println(String.format("Result: %s", this.lastObject.get()));

            assertThat("Created output tuple has an invalid existence metadata value", output.getMetadata().getExistence(), is(existence));
            assertThat("Last emitted output tuple has an invalid existence metadata value", ((ProbabilisticTuple<IProbabilistic>) this.lastObject.get()).getMetadata().getExistence(),
                    is(closeTo(existence, 0.01)));
            assertThat("Invalid emitted output tuple", this.lastObject.get(), is(output));
            assertThat("Invalid emitted output distributions", ((ProbabilisticTuple<IProbabilistic>) this.lastObject.get()).getDistributions(), arrayContainingInAnyOrder(output.getDistributions()));

            for (MultivariateMixtureDistribution distribution : ((ProbabilisticTuple<IProbabilistic>) this.lastObject.get()).getDistributions()) {
                final double[] lowerBound = new double[distribution.getDimension()];
                Arrays.fill(lowerBound, Double.NEGATIVE_INFINITY);
                final double[] upperBound = new double[distribution.getDimension()];
                Arrays.fill(upperBound, Double.POSITIVE_INFINITY);
                assertThat(String.format("Probability of %s is not equals to 1", distribution), distribution.probability(lowerBound, upperBound) * distribution.getScale(), is(1.0));
            }
        }
    }

    protected Attribute attribute(final String name) {
        return new Attribute(name);
    }

    protected class Attribute {
        private final String name;

        public Attribute(final String name) {
            this.name = name;
        }

        public SDFAttribute as(final SDFDatatype type) {
            return new SDFAttribute("", this.name, type);

        }
    }
}
