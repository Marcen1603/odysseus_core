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
/*
 * Copyright (C) 2003-2007 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */

package com.Ostermiller.util;

/**
 * An Illegal delimiter was specified.
 * <p>
 * This class exists to fix a spelling error in BadDelimeterException.
 *
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.02.20
 * @see BadDelimeterException
 */
public class BadDelimiterException extends BadDelimeterException {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -3250803278822032684L;

	/**
	 * Constructs an exception with null as its error detail message.
	 *
	 * @since ostermillerutils 1.02.20
	 */
	public BadDelimiterException(){
		super();
	}

	/**
	 * Constructs an exception with the specified detail message.
	 * The error message string s can later be retrieved by the
	 * Throwable.getMessage()  method of class java.lang.Throwable.
	 *
	 * @param s the detail message.
	 * @since ostermillerutils 1.02.20
	 */
	public BadDelimiterException(String s){
		super(s);
	}
}
