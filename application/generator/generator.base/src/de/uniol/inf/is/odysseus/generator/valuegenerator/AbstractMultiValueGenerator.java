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
package de.uniol.inf.is.odysseus.generator.valuegenerator;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractMultiValueGenerator implements IMultiValueGenerator {

    protected IErrorModel errorModel;

    public AbstractMultiValueGenerator(IErrorModel errorModel) {
        this.errorModel = errorModel;
    }

    @Override
    public final double[] nextValue() {
        double[] newValue = generateValue();
        for (int i = 0; i < newValue.length; i++) {
            newValue[i] = this.errorModel.pollute(newValue[i]);
        }
        return newValue;
    }

    public abstract double[] generateValue();

    public abstract void initGenerator();

    @Override
    public final void init() {
        errorModel.init();
        initGenerator();
    }

}
