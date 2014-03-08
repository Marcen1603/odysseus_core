/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator.valuegenerator.switching;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SignGenerator extends AbstractSingleValueGenerator {

    private final double value;
    private double current;

    public SignGenerator(final IErrorModel errorModel, final double value) {
        super(errorModel);
        this.value = value;
        this.current = value;
    }

    public SignGenerator(final IErrorModel errorModel) {
        this(errorModel, 1.0);
    }

    @Override
    public double generateValue() {
        this.current = -this.current;
        return this.current;
    }

    @Override
    public void initGenerator() {
        this.current = this.value;
    }

}
