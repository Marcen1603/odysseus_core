/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Jonas Jacobi Annotation for properties (set-methods) of logical
 *         operators, so that they can be used in query languages and guis
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parameter {
	/**
	 * Type of the parameter object that is used to provide the property with
	 * its content
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends IParameter> type();

	/**
	 * Name of the parameter. If no name is given, the name of the annotated
	 * property (e.g. there must be a getter and setter for this property) is used.
	 */
	public String name() default "";

	/**
	 * Defines whether a parameter is optional and not mandatory for the logical
	 * operator. Default is false (==parameter is mandatory).
	 */
	public boolean optional() default false;
	
	/**
	 * Defines whether the parameter type should be encapsulated by a ListParameter,
	 * so the return value of the created parameter would be List<value of parameter type>.
	 * 
	 * if isMap is used too, isList indicates if the value of the map is a list 
	 */
	public boolean isList() default false;
	
	/**
	 * Defines if the parameter is deprecated
	 */
	public boolean deprecated() default false;

	/**
	 * Defines whether the parameter should be encapsulated by a MapParameter,
	 * so the return value of the created parameter would be Map<String, value of parameter type>.
	 * 
	 * set isList to true to indicate that the value of the map is a list
	 */
	public boolean isMap() default false;

	/**
	 * Defines the type of the key for map parameters. its similar to the type parameter.
	 * the value part of the map is provided via the type attribute 
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends IParameter> keytype() default StringParameter.class;
	
	String doc() default "No description";
}
