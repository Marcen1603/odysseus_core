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
package de.uniol.inf.is.odysseus.core.server.predicate;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;

/**
 * Diese Klasse dient als Migrationshilfe um eine einfache Umstellung von
 * complex predicates auf Mep Predicates zu realisieren
 * 
 * @author Marco Grawunder
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComplexPredicateHelper {

    public static IPredicate createAndPredicate(IPredicate leftPredicate, IPredicate rightPredicate) {
        return leftPredicate.and(rightPredicate);
    }

    public static IPredicate createOrPredicate(IPredicate leftPredicate, IPredicate rightPredicate) {
        return leftPredicate.or(rightPredicate);
    }

    public static IPredicate createNotPredicate(IPredicate predicate) {
        return predicate.not();
    }

    @Deprecated
    public static IPredicate pushDownNegation(IPredicate predicate, boolean negatived) {
        if (predicate instanceof NotPredicate) {
            return pushDownNegation(((NotPredicate) predicate).getChild(), !negatived);
        }
        if (predicate instanceof ComplexPredicate) {
            ComplexPredicate compPred = (ComplexPredicate) predicate;
            if (negatived) {
                if (predicate instanceof OrPredicate) {
                    compPred = new AndPredicate();
                }
                else {
                    compPred = new OrPredicate();
                }
            }
            compPred.setLeft(pushDownNegation(compPred.getLeft(), negatived));
            compPred.setRight(pushDownNegation(compPred.getRight(), negatived));
            return compPred;
        }
        if (negatived) {
            return new NotPredicate(predicate);
        }
        return predicate;
    }

    @Deprecated
    public static List<IPredicate> conjunctiveSplit(IPredicate predicate) {
        List<IPredicate> result = new LinkedList<>();
        Stack<IPredicate> predicateStack = new Stack<>();
        predicateStack.push(predicate);
        while (!predicateStack.isEmpty()) {
            IPredicate curPredicate = predicateStack.pop();
            if (curPredicate instanceof AndPredicate) {
                predicateStack.push(((AndPredicate) curPredicate).getLeft());
                predicateStack.push(((AndPredicate) curPredicate).getRight());
            }
            else {
                result.add(curPredicate);
            }
        }
        return result;
    }

    /**
     * Execute the given function on every predicate
     * 
     * @param predicate
     *            The predicate
     * @param functor
     *            The function
     */
    public static void visitPredicates(IPredicate<?> predicate, IUnaryFunctor<IPredicate<?>> functor) {
        Stack<IPredicate<?>> predicates = new Stack<>();
        predicates.push(predicate);
        while (!predicates.isEmpty()) {
            IPredicate<?> curPred = predicates.pop();
            if (curPred instanceof ComplexPredicate<?>) {
                predicates.push(((ComplexPredicate<?>) curPred).getLeft());
                predicates.push(((ComplexPredicate<?>) curPred).getRight());
            }
            else if (curPred instanceof NotPredicate) {
                predicates.push(((NotPredicate<?>) curPred).getChild());
            }
            else {
                functor.call(curPred);
            }
        }
    }

    /**
     * Checks whether the given predicate is an {@link AndPredicate}.
     * 
     * @param predicate
     *            The predicate to check
     * @return <code>true</code> if the predicate is a {@link AndPredicate},
     *         <code>false</code> otherwise
     */
    public static boolean isAndPredicate(IPredicate predicate) {
        return (predicate instanceof AndPredicate);
    }

    /**
     * Checks whether the given predicate is an {@link OrPredicate}.
     * 
     * @param predicate
     *            The predicate to check
     * @return <code>true</code> if the predicate is a {@link OrPredicate},
     *         <code>false</code> otherwise
     */
    public static boolean isOrPredicate(IPredicate predicate) {
        return predicate instanceof OrPredicate;
    }

    /**
     * Checks whether the given predicate is an {@link NotPredicate}.
     * 
     * @param predicate
     *            The predicate to check
     * @return <code>true</code> if the predicate is a {@link NotPredicate},
     *         <code>false</code> otherwise
     */
    public static boolean isNotPredicate(IPredicate predicate) {
        return predicate instanceof NotPredicate;
    }

    @Deprecated
    public static boolean contains(IPredicate oPred, IPredicate pred) {
        if (oPred instanceof OrPredicate) {
            return ((OrPredicate) oPred).contains(pred);
        }
        return false;
    }

    @Deprecated
    public static IPredicate getChild(IPredicate notPred) {
        if (notPred instanceof NotPredicate) {
            return ((NotPredicate) notPred).getChild();
        }
        throw new IllegalArgumentException("Argument is not a NotPredicate");
    }
}
