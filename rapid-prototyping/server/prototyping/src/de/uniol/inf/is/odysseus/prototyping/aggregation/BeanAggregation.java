/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
/**
 *
 */
package de.uniol.inf.is.odysseus.prototyping.aggregation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

/**
 * Aggregation function to use plain old java objects (POJO) for datastream
 * aggregation.
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version 1.0
 */
public class BeanAggregation extends AbstractAggregateFunction<Tuple<?>, Tuple<?>> {
    /**
     *
     */
    private static final long serialVersionUID = -911617324088128058L;
    private final Logger LOG = LoggerFactory.getLogger(BeanAggregation.class);
    /** The bean instance */
    private Object bean;
    /** The bean class name */
    private final String beanClassName;
    /** The init method */
    private Method initMethod = null;
    /** The merge method */
    private Method mergeMethod = null;
    /** The evaluate method */
    private Method evaluateMethod = null;
    /** Position array to support multiple attributes for aggregation */
    private final int[] positions;
    final private String datatype;

    /**
     *
     * @param pos
     * @param className
     */
    public BeanAggregation(final int pos, final String className, final boolean partialAggregateInput, final String datatype) {
        this(new int[] { pos }, className, partialAggregateInput, datatype);
    }

    /**
     *
     * @param pos
     * @param className
     */
    public BeanAggregation(final int[] pos, final String className, final boolean partialAggregateInput, final String datatype) {
        super(className, partialAggregateInput);
        this.positions = pos;
        this.beanClassName = className;
        this.datatype = datatype;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
        IPartialAggregate<Tuple<?>> pa = null;
        try {
            Tuple<?> ret = null;
            final Object pojo = this.getBean();
            if (pojo != null) {
                // initialize all methods (init, merge, evaluate)
                this.initMethods(this.getBean().getClass());
                // Check if the init method has variable arguments
                if (this.initMethod.isVarArgs()) {
                    // cast the attribute array into an object to call the
                    // vararg method
                    final Object attributes = BeanAggregation.getAttributes(in, this.positions);
                    // check if the init method needs additional meta
                    // attributes
                    if (this.initMethod.getAnnotation(Init.class).meta()) {
                        // call the init method with meta attributes
                        ret = new Tuple(new Object[] { this.initMethod.invoke(pojo, in.getMetadata(), attributes) }, false);
                    }
                    else {
                        // call the init method without meta attributes
                        ret = new Tuple(new Object[] { this.initMethod.invoke(pojo, attributes) }, false);
                    }
                }
                else {
                    // get all parameters of the init method if it has a fix
                    // set of parameters
                    final Class<?>[] params = this.mergeMethod.getParameterTypes();
                    // check if the init method need additional meta
                    // attributes
                    if (this.initMethod.getAnnotation(Init.class).meta()) {
                        // call the init method with meta attributes and the
                        // possible number of attributes from the tuple
                        final Object[] attributes = new Object[params.length];
                        attributes[0] = in.getMetadata();
                        System.arraycopy(BeanAggregation.getAttributes(in, this.positions), 0, attributes, 1, params.length - 1);
                        ret = new Tuple(new Object[] { this.initMethod.invoke(pojo, attributes) }, false);
                    }
                    else {
                        // call the init method with the possible number of
                        // attributes from the tuple
                        ret = new Tuple(new Object[] { this.initMethod.invoke(pojo, Arrays.copyOfRange(BeanAggregation.getAttributes(in, this.positions), 0, params.length)) }, false);
                    }
                }
            }
            pa = new ElementPartialAggregate<Tuple<?>>(ret, this.datatype);

        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
            this.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge, final boolean createNew) {
        // Create the partial object for holding temp. results
        ElementPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            pa = new ElementPartialAggregate<Tuple<?>>(((ElementPartialAggregate<Tuple<?>>) p).getElem(), this.datatype);
        }
        else {
            pa = (ElementPartialAggregate<Tuple<?>>) p;
        }
        try {
            Tuple<?> ret = null;

            final Object pojo = this.getBean();
            if (pojo != null) {
                // Check if the merge method has variable arguments
                if (this.mergeMethod.isVarArgs()) {
                    // cast the attribute array into an object to call the
                    // vararg method
                    final Object attributes = BeanAggregation.getAttributes(toMerge, this.positions);
                    // check if the merge method needs additional meta
                    // attributes
                    if (this.mergeMethod.getAnnotation(Merge.class).meta()) {
                        // call the merge method with meta attributes
                        ret = new Tuple(new Object[] { this.mergeMethod.invoke(pojo, pa.getElem().getAttribute(0), toMerge.getMetadata(), attributes) }, false);
                    }
                    else {
                        // call the merge method with meta attributes
                        ret = new Tuple(new Object[] { this.mergeMethod.invoke(pojo, pa.getElem().getAttribute(0), attributes) }, false);
                    }
                }
                else {
                    // get all parameters of the merge method if it has a
                    // fix set of parameters
                    final Class<?>[] params = this.mergeMethod.getParameterTypes();
                    if (this.mergeMethod.getAnnotation(Merge.class).meta()) {
                        // call the merge method with meta attributes and
                        // the possible number of attributes from the tuple
                        final Object[] attributes = new Object[params.length];
                        attributes[0] = pa.getElem().getAttribute(0);
                        attributes[1] = toMerge.getMetadata();
                        System.arraycopy(BeanAggregation.getAttributes(toMerge, this.positions), 0, attributes, 2, params.length - 2);
                        ret = new Tuple(new Object[] { this.mergeMethod.invoke(pojo, attributes) }, false);
                    }
                    else {
                        // call the merge method with the possible number of
                        // attributes from the tuple
                        final Object[] attributes = new Object[params.length];
                        attributes[0] = pa.getElem().getAttribute(0);
                        System.arraycopy(BeanAggregation.getAttributes(toMerge, this.positions), 0, attributes, 1, params.length - 1);
                        ret = new Tuple(new Object[] { this.mergeMethod.invoke(pojo, attributes) }, false);
                    }
                }
            }
            pa.setElem(ret);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
            this.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
        // The result tuple
        final ElementPartialAggregate<Tuple<?>> pa = new ElementPartialAggregate<Tuple<?>>(((ElementPartialAggregate<Tuple<?>>) p).getElem(), this.datatype);

        Tuple<?> ret = null;
        try {
            final Object pojo = this.getBean();
            if (pojo != null) {
                // call the evaluate method with the partial object
                ret = new Tuple(new Object[] { this.evaluateMethod.invoke(pojo, pa.getElem().getAttribute(0)) }, false);
            }
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException e) {
            this.LOG.error(e.getMessage(), e);
            ret = new Tuple(new Object[] { null }, false);
            throw new RuntimeException(e);
        }

        return ret;
    }

    /**
     * Getter for the aggregation bean instance
     *
     * @return The bean
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public Object getBean() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (this.bean == null) {
            this.bean = this.createBean();
        }
        return this.bean;
    }

    /**
     * Search the bean class and create an instance of it
     *
     * @return The created instance of the bean
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private Object createBean() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (this.beanClassName != null) {
            final Class<?> type = Class.forName(this.beanClassName, true, this.getClass().getClassLoader());
            if (type != null) {
                return type.newInstance();
            }
        }
        throw new ClassNotFoundException();
    }

    /**
     * Initialize the three needed methods based on their annotations
     *
     * @param targetClass
     */
    private void initMethods(final Class<?> targetClass) {
        Objects.requireNonNull(targetClass);
        Class<?> clazz = targetClass;
        do {
            final Method[] methods = clazz.getDeclaredMethods();
            for (final Method method : methods) {
                if (method.getAnnotation(Init.class) != null) {
                    this.initMethod = method;
                }
                if (method.getAnnotation(Merge.class) != null) {
                    this.mergeMethod = method;
                }
                if (method.getAnnotation(Evaluate.class) != null) {
                    this.evaluateMethod = method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        while (clazz != null);
    }

    /**
     * Return the attributes from the tuple
     *
     * @param in
     * @param positions
     * @return
     */
    private static Object[] getAttributes(final Tuple<?> in, final int[] positions) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(positions);
        final Object[] attributes = new Object[positions.length];
        for (int i = 0; i < positions.length; ++i) {
            attributes[i] = in.getAttribute(positions[i]);
        }
        return attributes;
    }
}
