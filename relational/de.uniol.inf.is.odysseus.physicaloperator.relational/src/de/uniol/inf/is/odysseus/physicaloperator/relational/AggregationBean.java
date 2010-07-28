/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @version 1.0
 * 
 */
public class AggregationBean extends
		AbstractAggregateFunction<RelationalTuple<?>> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Object bean;
	private String beanClassName;
	private Method initMethod = null;
	private Method mergeMethod = null;
	private Method evaluateMethod = null;
	private int pos;

	/**
	 * @param name
	 */
	public AggregationBean(int pos, String name) {
		super(name);
		this.pos = pos;
		this.beanClassName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions
	 * .IInitializer#init(java.lang.Object)
	 */
	@Override
	public IPartialAggregate<RelationalTuple<?>> init(RelationalTuple<?> in) {
		PartialAggregateData result = new PartialAggregateData();
		try {
			Object pojo = this.getBean();
			if (pojo != null) {
				this.initMethods(getBean().getClass());
				try {
					if (this.initMethod.isVarArgs()) {
						if (this.initMethod.getAnnotation(Init.class).meta()) {
							result.setPartial(this.initMethod.invoke(pojo, in
									.getMetadata(), in.getAttributes()));
						} else {
							result.setPartial(this.initMethod.invoke(pojo, in
									.getAttributes()));
						}
					} else {
						Class<?>[] params = this.initMethod.getParameterTypes();
						if (this.initMethod.getAnnotation(Init.class).meta()) {
							if (params.length == (in.getAttributeCount() + 1)) {
								result.setPartial(this.initMethod.invoke(pojo,
										in.getMetadata(), in.getAttributes()));
							} else {
								if (params.length == in.getAttributeCount()) {
									result.setPartial(this.initMethod.invoke(
											pojo, in.getAttributes()));
								}
							}
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
	 * de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions
	 * .IMerger
	 * #merge(de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions
	 * .IPartialAggregate, java.lang.Object, boolean)
	 */
	@Override
	public IPartialAggregate<RelationalTuple<?>> merge(
			IPartialAggregate<RelationalTuple<?>> partial,
			RelationalTuple<?> in, boolean create) {
		PartialAggregateData result = new PartialAggregateData();
		try {
			// TODO What is "create"? And when it is called?
			// final PartialAggregateData _partial = (create ?
			// (PartialAggregateData) partial
			// : result);
			final PartialAggregateData _partial = (PartialAggregateData) partial;
			Object pojo = this.getBean();
			if (pojo != null) {
				try {
					if (this.mergeMethod.isVarArgs()) {
						if (this.mergeMethod.getAnnotation(Merge.class).meta()) {
							result.setPartial(this.mergeMethod.invoke(pojo,
									_partial.getPartial(), in.getMetadata(), in
											.getAttributes()));
						} else {
							result.setPartial(this.mergeMethod.invoke(pojo,
									_partial.getPartial(), in.getAttributes()));
						}
					} else {
						Class<?>[] params = this.mergeMethod
								.getParameterTypes();
						if (this.mergeMethod.getAnnotation(Merge.class).meta()) {
							if (params.length == in.getAttributeCount()) {
								result.setPartial(this.mergeMethod.invoke(pojo,
										_partial.getPartial(),
										in.getMetadata(), in.getAttributes()));
							}
						} else {
							if (params.length == in.getAttributeCount()) {
								result.setPartial(this.mergeMethod.invoke(pojo,
										_partial.getPartial(), in
												.getAttributes()));
							}
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
	 * de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions
	 * .IEvaluator
	 * #evaluate(de.uniol.inf.is.odysseus.physicaloperator.base.aggregate
	 * .basefunctions.IPartialAggregate)
	 */
	@Override
	public RelationalTuple<?> evaluate(
			IPartialAggregate<RelationalTuple<?>> partial) {
		RelationalTuple<?> result = new RelationalTuple(1);
		try {
			Object pojo = this.getBean();
			if (pojo != null) {
				try {
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

	public Object getBean() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (this.bean == null) {
			this.bean = createBean();
		}
		return this.bean;
	}

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

	private class PartialAggregateData implements
			IPartialAggregate<RelationalTuple<?>> {
		private Object partial = null;

		public PartialAggregateData() {

		}

		public PartialAggregateData(PartialAggregateData instance) {
			this.partial = instance.getPartial();
		}

		public Object getPartial() {
			return partial;
		}

		public void setPartial(Object partial) {
			this.partial = partial;
		}

		public PartialAggregateData clone() {
			return new PartialAggregateData(this);
		}

	}
}
