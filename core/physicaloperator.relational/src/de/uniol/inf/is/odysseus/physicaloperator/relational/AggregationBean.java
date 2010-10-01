/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @version 1.0
 * 
 *          Aggregation function to use plain old java objects (POJO) for
 *          datastream aggregation.
 * 
 */
public class AggregationBean extends
		AbstractAggregateFunction<RelationalTuple<?>> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
	 * @param name
	 */
	public AggregationBean(int pos, String name) {
		this(new int[] { pos }, name);
	}

	/**
	 * 
	 * @param pos
	 * @param name
	 */
	public AggregationBean(int[] pos, String name) {
		super(name);
		this.positions = pos;
		this.beanClassName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions
	 * .IInitializer#init(java.lang.Object)
	 */
	@Override
	public IPartialAggregate<RelationalTuple<?>> init(RelationalTuple<?> in) {
		// Create the partial object for holding temp. results
		PartialAggregateData result = new PartialAggregateData();
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
							result.setPartial(this.initMethod.invoke(pojo, in
									.getMetadata(), attributes));
						} else {
							// call the init method without meta attributes
							result.setPartial(this.initMethod.invoke(pojo,
									attributes));
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
							result.setPartial(this.initMethod.invoke(pojo,
									attributes));
						} else {
							// call the init method with the possible number of
							// attributes from the tuple
							result
									.setPartial(this.initMethod.invoke(pojo,
											Arrays.copyOfRange(getAttributes(
													in, this.positions), 0,
													params.length)));
						}
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions
	 * .IMerger
	 * #merge(de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions
	 * .IPartialAggregate, java.lang.Object, boolean)
	 */
	@Override
	public IPartialAggregate<RelationalTuple<?>> merge(
			IPartialAggregate<RelationalTuple<?>> partial,
			RelationalTuple<?> in, boolean create) {
		// Create the partial object for holding temp. results
		PartialAggregateData result = new PartialAggregateData();
		try {
			final PartialAggregateData _partial = (PartialAggregateData) partial;
			Object pojo = this.getBean();
			if (pojo != null) {
				try {
					// Check if the merge method has variable arguments
					if (this.mergeMethod.isVarArgs()) {
						// cast the attribute array into an object to call the
						// vararg method
						Object attributes = getAttributes(in, this.positions);
						// check if the merge method needs additional meta
						// attributes
						if (this.mergeMethod.getAnnotation(Merge.class).meta()) {
							// call the merge method with meta attributes
							result.setPartial(this.mergeMethod.invoke(pojo,
									_partial.getPartial(), in.getMetadata(),
									attributes));
						} else {
							// call the merge method with meta attributes
							result.setPartial(this.mergeMethod.invoke(pojo,
									_partial.getPartial(), attributes));
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
							attributes[0] = _partial.getPartial();
							attributes[1] = in.getMetadata();
							System.arraycopy(getAttributes(in, this.positions),
									0, attributes, 2, params.length - 2);
							result.setPartial(this.mergeMethod.invoke(pojo,
									attributes));
						} else {
							// call the merge method with the possible number of
							// attributes from the tuple
							Object[] attributes = new Object[params.length];
							attributes[0] = _partial.getPartial();
							System.arraycopy(getAttributes(in, this.positions),
									0, attributes, 1, params.length - 1);
							result.setPartial(this.mergeMethod.invoke(pojo,
									attributes));
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions
	 * .IEvaluator
	 * #evaluate(de.uniol.inf.is.odysseus.physicaloperator.aggregate
	 * .basefunctions.IPartialAggregate)
	 */
	@Override
	public RelationalTuple<?> evaluate(
			IPartialAggregate<RelationalTuple<?>> partial) {
		// The result tuple
		RelationalTuple<?> result = new RelationalTuple(1);
		try {
			Object pojo = this.getBean();
			if (pojo != null) {
				try {
					// call the evaluate method with the partial object
					result.setAttribute(0, this.evaluateMethod.invoke(pojo,
							((PartialAggregateData) partial).getPartial()));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
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
	private Object[] getAttributes(RelationalTuple<?> in, int[] positions) {
		Object[] attributes = new Object[positions.length];
		for (int i = 0; i < positions.length; ++i) {
			attributes[i] = in.getAttribute(positions[i]);
		}
		return attributes;
	}

	/**
	 * class of the partial object for storing the results of the init and
	 * merge methods
	 * 
	 */
	public static class PartialAggregateData implements
			IPartialAggregate<RelationalTuple<?>> {
		public Object partial = null;

		public PartialAggregateData(PartialAggregateData partialAggregateData) {
			this.partial = partialAggregateData.getPartial();

		}

		public PartialAggregateData() {
		}

		public Object getPartial() {
			return partial;
		}

		public void setPartial(Object partial) {
			this.partial = partial;
		}

		@Override
		public PartialAggregateData clone() {
			return new PartialAggregateData(this);
		}
	}
}
