/** Copyright [2011] [The Odysseus Team]
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

import java.lang.reflect.Method;
import java.util.Arrays;

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
 * @author Christian Kuka <christian.kuka@offis.de>
 * @version 1.0
 */
public class BeanAggregation extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -911617324088128058L;
	private final Logger LOG = LoggerFactory.getLogger(BeanAggregation.class);
	/** The bean instance */
	private Object bean;
	/** The bean class name */
	private String beanClassName;
	/** The init method */
	private Method initMethod = null;
	/** The merge method */
	private Method mergeMethod = null;
	/** The evaluate method */
	private Method evaluateMethod = null;
	/** Position array to support multiple attributes for aggregation */
	private int[] positions;

	/**
	 * 
	 * @param pos
	 * @param className
	 */
	public BeanAggregation(int pos, String className) {
		this(new int[] { pos }, className);
	}

	/**
	 * 
	 * @param pos
	 * @param className
	 */
	public BeanAggregation(int[] pos, String className) {
		super(className);
		this.positions = pos;
		this.beanClassName = className;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IInitializer#init(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		Tuple<?> ret = null;
		try {
			Object pojo = this.getBean();
			if (pojo != null) {
				// initialize all methods (init, merge, evaluate)
				this.initMethods(getBean().getClass());
				try {
					// Check if the init method has variable arguments
					if (this.initMethod.isVarArgs()) {
						// cast the attribute array into an object to call the
						// vararg method
						Object attributes = getAttributes(in, this.positions);
						// check if the init method needs additional meta
						// attributes
						if (this.initMethod.getAnnotation(Init.class).meta()) {
							// call the init method with meta attributes
							ret = new Tuple(
									new Object[] { this.initMethod.invoke(pojo,
											in.getMetadata(), attributes) });
						} else {
							// call the init method without meta attributes
							ret = new Tuple(
									new Object[] { this.initMethod.invoke(pojo,
											attributes) });
						}
					} else {
						// get all parameters of the init method if it has a fix
						// set of parameters
						Class<?>[] params = this.mergeMethod
								.getParameterTypes();
						// check if the init method need additional meta
						// attributes
						if (this.initMethod.getAnnotation(Init.class).meta()) {
							// call the init method with meta attributes and the
							// possible number of attributes from the tuple
							Object[] attributes = new Object[params.length];
							attributes[0] = in.getMetadata();
							System.arraycopy(getAttributes(in, this.positions),
									0, attributes, 1, params.length - 1);
							ret = new Tuple(
									new Object[] { this.initMethod.invoke(pojo,
											attributes) });
						} else {
							// call the init method with the possible number of
							// attributes from the tuple
							ret = new Tuple(
									new Object[] { this.initMethod.invoke(pojo,
											Arrays.copyOfRange(
													getAttributes(in,
															this.positions), 0,
													params.length)) });
						}
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		} catch (InstantiationException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
		IPartialAggregate<Tuple<?>> pa = new ElementPartialAggregate<Tuple<?>>(
				ret);
		return pa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IMerger
	 * #merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate
	 * .basefunctions .IPartialAggregate, java.lang.Object, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p,
			Tuple<?> toMerge, boolean createNew) {
		// Create the partial object for holding temp. results
		ElementPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new ElementPartialAggregate<Tuple<?>>(
					((ElementPartialAggregate<Tuple<?>>) p).getElem());
		} else {
			pa = (ElementPartialAggregate<Tuple<?>>) p;
		}
		Tuple<?> ret = null;
		try {
			Object pojo = this.getBean();
			if (pojo != null) {
				try {
					// Check if the merge method has variable arguments
					if (this.mergeMethod.isVarArgs()) {
						// cast the attribute array into an object to call the
						// vararg method
						Object attributes = getAttributes(toMerge,
								this.positions);
						// check if the merge method needs additional meta
						// attributes
						if (this.mergeMethod.getAnnotation(Merge.class).meta()) {
							// call the merge method with meta attributes
							ret = new Tuple(
									new Object[] { this.mergeMethod.invoke(
											pojo, pa.getElem().getAttribute(0),
											toMerge.getMetadata(), attributes) });
						} else {
							// call the merge method with meta attributes
							ret = new Tuple(
									new Object[] { this.mergeMethod.invoke(
											pojo, pa.getElem().getAttribute(0),
											attributes) });
						}
					} else {
						// get all parameters of the merge method if it has a
						// fix set of parameters
						Class<?>[] params = this.mergeMethod
								.getParameterTypes();
						if (this.mergeMethod.getAnnotation(Merge.class).meta()) {
							// call the merge method with meta attributes and
							// the possible number of attributes from the tuple
							Object[] attributes = new Object[params.length];
							attributes[0] = pa.getElem().getAttribute(0);
							attributes[1] = toMerge.getMetadata();
							System.arraycopy(
									getAttributes(toMerge, this.positions), 0,
									attributes, 2, params.length - 2);
							ret = new Tuple(
									new Object[] { this.mergeMethod.invoke(
											pojo, attributes) });
						} else {
							// call the merge method with the possible number of
							// attributes from the tuple
							Object[] attributes = new Object[params.length];
							attributes[0] = pa.getElem().getAttribute(0);
							System.arraycopy(
									getAttributes(toMerge, this.positions), 0,
									attributes, 1, params.length - 1);
							ret = new Tuple(
									new Object[] { this.mergeMethod.invoke(
											pojo, attributes) });
						}
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		} catch (InstantiationException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
		pa.setElem(ret);
		return pa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IEvaluator
	 * #evaluate(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate
	 * .basefunctions.IPartialAggregate)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		// The result tuple
		ElementPartialAggregate<Tuple<?>> pa = new ElementPartialAggregate<Tuple<?>>(
				((ElementPartialAggregate<Tuple<?>>) p).getElem());

		Tuple<?> ret = null;
		try {
			Object pojo = this.getBean();
			if (pojo != null) {
				try {
					// call the evaluate method with the partial object
					ret = new Tuple(new Object[] { this.evaluateMethod.invoke(
							pojo, pa.getElem().getAttribute(0)) });
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		} catch (InstantiationException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
		return ret;
	}

	/**
	 * Getter for the aggregation bean instance
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public Object getBean() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (this.bean == null) {
			this.bean = createBean();
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
	private Object createBean() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (this.beanClassName != null) {
			Class<?> type = Class.forName(beanClassName, true, this.getClass()
					.getClassLoader());
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
	private void initMethods(Class<?> targetClass) {
		do {
			Method[] methods = targetClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
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
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null);
	}

	/**
	 * Return the attributes from the tuple
	 * 
	 * @param in
	 * @param positions
	 * @return
	 */
	private static Object[] getAttributes(Tuple<?> in, int[] positions) {
		Object[] attributes = new Object[positions.length];
		for (int i = 0; i < positions.length; ++i) {
			attributes[i] = in.getAttribute(positions[i]);
		}
		return attributes;
	}
}
