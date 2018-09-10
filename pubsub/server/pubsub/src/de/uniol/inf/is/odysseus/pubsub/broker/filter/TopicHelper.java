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
import java.util.Collections;
import java.util.List;

/**
 * This helper Class provides functions for work with hierarchical topic trees
 * 
 * @author ChrisToenjesDeye
 *
 */
public class TopicHelper {

	private static ArrayList<Topic> topics;
	
	/**
	 * Creates a topic hierarchy on given topic strings
	 * 
	 * @param topicStrings for creating hierarchical structure			
	 * @return list with created topics				
	 */
	public static List<Topic> convertStringsToTopics(List<String> topicStrings){
		topics = new ArrayList<Topic>();
		
		Collections.sort(topicStrings);
		// Iterate over all topic Strings
		for (String topicString : topicStrings) {
			String[] topicElements = topicString.trim().split("\\.");
			int currentElementIndex = 0;
			
			// If no elements exists return null
			if (topicElements.length == 0 || topicElements[currentElementIndex].equals("*")){
				break;
			}
			Topic root = getRootTopicIfExists(topicElements[currentElementIndex]);
			
			if (root == null){
				// Root does not exists, create new root
				root = new Topic(topicElements[currentElementIndex].toLowerCase());
				topics.add(root);
			}
			
			// Root is created or exists, now travers through the tree
			createTree(topicElements, currentElementIndex, root);
		}
		Collections.sort(topics);
		return topics;
	}
	
	/**
	 * returns a root topic with a given name
	 * @param topicString
	 * @return root topic with given name, null if not exists
	 */
	private static Topic getRootTopicIfExists(String topicString){
		for (Topic temp : topics) {
			if (temp.getName().toLowerCase().equals(topicString.toLowerCase())){
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * creates a topic tree
	 * @param topicElements
	 * @param currentElementIndex
	 * @param topic
	 * @return
	 */
	private static boolean createTree(String[] topicElements, int currentElementIndex, Topic topic){
		currentElementIndex++;
		if (topicElements.length > currentElementIndex){
			String currentString = topicElements[currentElementIndex];
			
			// if String is like *, no leaf need to be created
			if (currentString.equals("*")){
				return true;
			}
			
			// Search for Child
			Topic child = getChildIfExist(currentString, topic.getChilds());
			if (child == null){
				// Create new Child
				child = new Topic(currentString.toLowerCase());
				topic.getChilds().add(child);
			}
			// recursive call, needed for tree structure
			return createTree(topicElements, currentElementIndex, child);
		} else {			
			return true;
		}
	}
	
	/**
	 * returns a child with given name if exists
	 * @param topicString
	 * @param childs
	 * @return child with name, null if not exists
	 */
	private static Topic getChildIfExist(String topicString, List<Topic> childs){
		for (Topic child : childs) {
			if (child.getName().toLowerCase().equals(topicString.toLowerCase())){
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Compares the topic trees of an advertisement and subscription
	 * 
	 * @param advertisementTopic hierarchical topic of the advertisement
	 * @param subscriptionTopic hierarchical topic of the subscription
	 * @return if subscription topic matches advertisement topic
	 */
	public static boolean compareTrees(Topic advertisementTopic,
			Topic subscriptionTopic) {
		if (advertisementTopic.getNumberOfChilds() > 0 && advertisementTopic.getName().equals(subscriptionTopic.getName())){
			// if advertisement topic has child's and is equal with subscription topic
			if (subscriptionTopic.getNumberOfChilds() == 0){
				return true;
			}
			
			if (advertisementTopic.getNumberOfChilds() > subscriptionTopic.getNumberOfChilds()){
				// if advertisement has more child topics, subscription does not match
				return false;
			} else {
				// if advertisement has less or equal number of child's than subscription, check subtree
				boolean subTreeMatches = true;
				for (Topic advChild : advertisementTopic.getChilds()) {
					// for each child topic, check if name...
					Topic subChild = subscriptionTopic.getChildWithName(advChild.getName());
					if (subChild != null){
						// ... and subtrees match
						compareTrees(advChild, subChild);
					} else {
						return false;
					} 
				}
				return subTreeMatches;
			}
		} else {
			if (subscriptionTopic.getNumberOfChilds() > 0){
				// if advertisement topic has no child's, but subscription has filter does not match
				return false;
			}
			return true;			
		}
	}
}
