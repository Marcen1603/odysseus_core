/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;

/**
 * Class for Content based Filtering
 * needed for creating predicate tree and filtering
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ContentBasedFiltering<T extends IStreamObject<?>> extends
		AbstractFiltering<T> {

	private List<BrokerSubscription<T>> subscriptions = new ArrayList<BrokerSubscription<T>>();

	private List<WeightedPredicate<T>> weightedPredicates = new ArrayList<WeightedPredicate<T>>();

	private List<PredicateNode<T>> predicateNodes = new ArrayList<PredicateNode<T>>();

	/**
	 * Initialize content based filtering all predicates are compressed, so each
	 * predicate needs to be evaluated one time
	 */
	@Override
	public void reinitializeFilter(
			Collection<BrokerSubscription<T>> subscriptions,
			Collection<BrokerAdvertisements> advertisements) {
		this.subscriptions.clear();
		this.subscriptions = new ArrayList<BrokerSubscription<T>>(subscriptions);
		this.weightedPredicates.clear();

		// Step1: conjunctive split on predicates and weighting
		for (BrokerSubscription<T> brokerSubscription : this.subscriptions) {
			List<IPredicate<? super T>> splittedPredicates = new ArrayList<IPredicate<? super T>>();

			// Step 1a: conjunctive split
			for (IPredicate<? super T> predicate : brokerSubscription
					.getPredicates()) {
								
					splittedPredicates
					.addAll(predicate
							.conjunctiveSplit());
			}

			// Step 1b: Save predicates with weight
			brokerSubscription.getPredicates().clear();
			
			for (IPredicate<? super T> splittedPredicate : splittedPredicates) {
				WeightedPredicate<T> predicateAlreadyAdded = getPredicateIfAlreadyAdded(splittedPredicate);
				if (predicateAlreadyAdded == null) {
					WeightedPredicate<T> newWeightedPredicate = new WeightedPredicate<T>(
							splittedPredicate);
					weightedPredicates.add(newWeightedPredicate);
					brokerSubscription.getPredicates().add(splittedPredicate);
				} else {
					predicateAlreadyAdded.incrementWeight();
					brokerSubscription.getPredicates().add(splittedPredicate);
				}

			}
		}

		// Step 1c: Sort weighted predicates
		Collections.sort(weightedPredicates, Collections.reverseOrder());

		// Step2: Save weighted predicates on subscription
		for (BrokerSubscription<T> brokerSubscription : this.subscriptions) {
			brokerSubscription.getWeightedPredicates().clear();
			for (IPredicate<? super T> predicate : brokerSubscription
					.getPredicates()) {

				brokerSubscription.getWeightedPredicates().add(
						getPredicateIfAlreadyAdded(predicate));
			}
			Collections.sort(brokerSubscription.getWeightedPredicates(),
					Collections.reverseOrder());
		}

		// Step3: Create Predicate Tree
		this.predicateNodes.clear();

		// Step 3a: Subscriptions need to be sorted by highest predicate weight
		Collections.sort(this.subscriptions, Collections.reverseOrder());

		// Step 3b: create tree
		for (BrokerSubscription<T> brokerSubscription : this.subscriptions) {
			PredicateNode<T> currentNode = null;
			PredicateNode<T> childNode = null;

			int level = 0;
			for (WeightedPredicate<T> weightedPredicate : brokerSubscription
					.getWeightedPredicates()) {
				if (level == 0) {
					currentNode = getRootNode(weightedPredicate.getPredicate());
					if (currentNode == null) {
						// Create new root node
						currentNode = new PredicateNode<T>(
								weightedPredicate.getPredicate());
						predicateNodes.add(currentNode);
					}		
				} else {
					childNode = getChildNodeIfExists(currentNode,
							weightedPredicate.getPredicate());
					if (childNode == null) {
						// Create new Child
						childNode = new PredicateNode<T>(
								weightedPredicate.getPredicate());
						currentNode.getChildPredicates().add(childNode);
					}
					currentNode = childNode;
				}

				// Step 3c: if last subscriber predicate, add subscriber Uid to
				// node
				if (brokerSubscription.getNumberOfPredicates() - 1 == level) {
					currentNode.getSubscriberUids().add(
							brokerSubscription.getSubscriber().getIdentifier());
				}

				level++;
			}
		}

		setReinitializationMode(false);
	}

	/**
	 * Checks if given weighted predicate is already added
	 * @param predicate
	 * @return
	 */
	private WeightedPredicate<T> getPredicateIfAlreadyAdded(
			IPredicate<? super T> predicate) {
		for (WeightedPredicate<T> weightedPredicate : weightedPredicates) {
			if (weightedPredicate.isPredicateEqual(predicate)) {
				return weightedPredicate;
			}
		}
		return null;
	}

	/**
	 * Returns the Root node from predicate trees, for a given predicate
	 * if not root node with given predicate exists, return null
	 * @param predicate
	 */
	private PredicateNode<T> getRootNode(IPredicate<? super T> predicate) {
		for (PredicateNode<T> rootNode : predicateNodes) {
			if (rootNode.isPredicateEqual(predicate)) {
				return rootNode;
			}
		}
		return null;
	}

	/**
	 * checks and returns a child node from a given tree if exists
	 * @param currentNode
	 * @param predicate
	 * @return
	 */
	private PredicateNode<T> getChildNodeIfExists(PredicateNode<T> currentNode,
			IPredicate<? super T> predicate) {
		for (PredicateNode<T> child : currentNode.getChildPredicates()) {
			if (child.isPredicateEqual(predicate)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * filters given object, if all subscriber predicates match
	 */
	@Override
	public List<String> filter(T object, String publisherUid) {
		ArrayList<String> result = new ArrayList<String>();

		for (PredicateNode<T> node : predicateNodes) {
			if (node.evaluate(object)) {
				result.addAll(node.getSubscriberUids());
				evaluateChilds(node, object, result);
			}
		}

		// add if no predicates are available (--> object matches)
		for (BrokerSubscription<T> subscription : subscriptions) {
			if (!subscription.hasPredicates()) {
				result.add(subscription.getSubscriber().getIdentifier());
			}
		}
		return result;
	}

	/**
	 * Recursive method for evaluating childs in predicate tree
	 * @param rootNode
	 * @param object
	 * @param result
	 * @return subscribers, for which filter matches
	 */
	private List<String> evaluateChilds(PredicateNode<T> rootNode, T object,
			ArrayList<String> result) {
		for (PredicateNode<T> node : rootNode.getChildPredicates()) {
			if (node.evaluate(object)) {
				// if predicate is true. Add all subscribers to result
				result.addAll(node.getSubscriberUids());
				// and check childs
				evaluateChilds(node, object, result);
			}
		}
		return result;
	}

}
