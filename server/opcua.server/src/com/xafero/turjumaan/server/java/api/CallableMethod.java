/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package com.xafero.turjumaan.server.java.api;

import java.util.List;

import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;

/**
 * The interface for callable methods.
 */
public interface CallableMethod {

	/**
	 * Call the method.
	 *
	 * @param args
	 *            the arguments
	 * @param outs
	 *            the outputs
	 * @throws Exception
	 *             the exception
	 */
	void call(Variant[] args, List<Object> outs) throws Exception;

}