package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.List;

public class TopicBuilder {

	private static ArrayList<Topic> topics;

	public static void main(String[] args){
		List<String> list = new ArrayList<String>();
		list.add("Nachrichten.Wirtschaft");
		list.add("nachrichten.Ausland.Afganistan");
		list.add("boerse.Deutschland");
		list.add("Boerse.USA.");
		list.add("Nachrichten.*");
		list.add("*");
		ConvertStringsToTopics(list);
	}
	
	public static List<Topic> ConvertStringsToTopics(List<String> topicStrings){
		topics = new ArrayList<Topic>();
		
		// Iterate over all topic Strings
		for (String topicString : topicStrings) {
			String[] topicElements = topicString.split("\\.");
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
		return topics;
	}
	
	private static Topic getRootTopicIfExists(String topicString){
		for (Topic temp : topics) {
			if (temp.getName().toLowerCase().equals(topicString.toLowerCase())){
				return temp;
			}
		}
		return null;
	}
	
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
			return createTree(topicElements, currentElementIndex, child);
		} else {			
			return true;
		}
	}
	
	private static Topic getChildIfExist(String topicString, List<Topic> childs){
		for (Topic child : childs) {
			if (child.getName().toLowerCase().equals(topicString.toLowerCase())){
				return child;
			}
		}
		return null;
	}
}
