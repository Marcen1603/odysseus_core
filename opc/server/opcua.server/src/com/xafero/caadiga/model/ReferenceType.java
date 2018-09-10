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
package com.xafero.caadiga.model;

/**
 * The reference type.
 */
public enum ReferenceType {

	/** Node has a type definition. */
	HasTypeDefinition,

	/** Node has a sub-type. */
	HasSubtype,

	/** Node has a property. */
	HasProperty,

	/** Node has a modeling rule. */
	HasModellingRule,

	/** Node organizes something. */
	Organizes,

	/** Node has a component. */
	HasComponent;

}