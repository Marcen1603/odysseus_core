/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.script.parser.parameter;

/**
 * Base interface for definition of keyword parameters. This is use in PreParserKeywordParameterHelper
 * 
 * @author ChrisToenjesDeye
 *
 */
public interface IKeywordParameter{
	
	/**
	 * Returns the paramerer name, which is used in Odysseus script
	 * @return parameter name
	 */
	String getName();
	
	/**
	 * returns the position if parameters. this is needed if the parameters are defined without parameter names 
	 * @return
	 */
	int getPosition();
	
	/**
	 * Defines if an given parameter is optional or mandatory
	 * @return
	 */
	boolean isOptional();
}
