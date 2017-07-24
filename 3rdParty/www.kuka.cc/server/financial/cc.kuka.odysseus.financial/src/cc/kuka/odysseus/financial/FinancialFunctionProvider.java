/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.financial;

import java.util.ArrayList;
import java.util.List;

import cc.kuka.odysseus.financial.function.financial.AnnualPercentageRateFunction;
import cc.kuka.odysseus.financial.function.financial.AnnualPercentageYieldFunction;
import cc.kuka.odysseus.financial.function.financial.ResidualValueFunction;
import cc.kuka.odysseus.financial.function.financial.ValueAddedTaxFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FinancialFunctionProvider implements IFunctionProvider {

    public FinancialFunctionProvider() {
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();
        functions.add(new AnnualPercentageRateFunction());
        functions.add(new AnnualPercentageYieldFunction());
        functions.add(new ResidualValueFunction());
        functions.add(new ValueAddedTaxFunction());

        return functions;
    }

}
