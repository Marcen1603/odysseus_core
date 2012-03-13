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
package de.uniol.inf.is.odysseus.args.test;

import org.junit.Test;

import de.uniol.inf.is.odysseus.args.Args;
import de.uniol.inf.is.odysseus.args.ArgsException;
import de.uniol.inf.is.odysseus.args.Args.REQUIREMENT;

import junit.framework.TestCase;

public class ArgsTest extends TestCase {

	public void testMandatory() {
		Args args = new Args();
		args.addInteger("-t", REQUIREMENT.MANDATORY);
		try {
			args.parse(new String[]{});
			fail("ArgsException expected");
		} catch (ArgsException e) {
		}
	}
	
	@Test
	public void testOptional() throws ArgsException {
		Args args = new Args();
		args.addInteger("-t", REQUIREMENT.OPTIONAL);
		args.parse(new String[]{});
	}
	
	@Test
	public void testBoolean() throws ArgsException {
		Args args = new Args();
		String[] params = new String[] { "-t" };

		args.addBoolean("-t");
		args.addBoolean("-u");
		args.parse(params);

		assertTrue((Boolean) args.get("-t"));
		assertFalse((Boolean) args.get("-u"));
	}

	@Test
	public void testInteger() throws ArgsException {
		Args args = new Args();
		args.addInteger("-value",  REQUIREMENT.MANDATORY);

		String[] params = new String[] { "-value", "10" };
		args.parse(params);

		assertEquals(10, args.get("-value"));
	}

	@Test
	public void testDouble() throws ArgsException {
		Args args = new Args();
		args.addDouble("-value", REQUIREMENT.MANDATORY);

		String[] params = new String[] { "-value", "10.12" };
		args.parse(params);

		assertEquals(10.12d, args.get("-value"));
	}

	@Test
	public void testFloat() throws ArgsException {
		Args args = new Args();
		args.addFloat("-value",  REQUIREMENT.MANDATORY);

		String[] params = new String[] { "-value", "10.12" };
		args.parse(params);

		assertEquals(10.12f, args.get("-value"));
	}

	@Test
	public void testChar() throws ArgsException {
		Args args = new Args();
		args.addCharacter("-value", REQUIREMENT.MANDATORY);

		String[] params = new String[] { "-value", "a" };
		args.parse(params);

		assertEquals('a', args.get("-value"));
	}

	@Test
	public void testCharException() {
		Args args = new Args();
		args.addCharacter("-value", REQUIREMENT.MANDATORY);

		String[] params = new String[] { "-value", "abc" };
		try {
			args.parse(params);
			fail("ArgsException expected");
		} catch (ArgsException e) {
		}
	}
}
