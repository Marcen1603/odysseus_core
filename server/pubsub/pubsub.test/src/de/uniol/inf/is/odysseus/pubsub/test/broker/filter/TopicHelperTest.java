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

package de.uniol.inf.is.odysseus.pubsub.test.broker.filter;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.TopicHelper;


/**
 * Tests for TopicHelper
 * 
 * @author ChrisToenjesDeye
 *
 */
public class TopicHelperTest {
	
	/**
	 * Checks if Convert works correctly
	 */
	@Test
	public void convertStringsToTopicsTest(){
		List<String> topicStrings = new ArrayList<String>();
		topicStrings.add("news.*");
		topicStrings.add("News.wirtschaft");
		topicStrings.add("boerse.ausland.*");
		topicStrings.add("boerse.ausland.usa  ");
		topicStrings.add("boerse.ausland.japan");
		topicStrings.add("  news.pOlitik.inland");
		topicStrings.add("News.politik.ausland");
		
		List<Topic> topics = TopicHelper.convertStringsToTopics(topicStrings);
		Assert.assertTrue(topics.get(0).getName().equals("boerse"));
		Assert.assertEquals(1, topics.get(0).getNumberOfChilds());
		Assert.assertTrue(topics.get(0).getChilds().get(0).getName().equals("ausland"));
		Assert.assertEquals(2, topics.get(0).getChilds().get(0).getNumberOfChilds());

		Assert.assertEquals("news", topics.get(1).getName());
		Assert.assertEquals(2, topics.get(1).getNumberOfChilds());
		Assert.assertEquals("politik", topics.get(1).getChilds().get(0).getName());
		Assert.assertEquals("wirtschaft", topics.get(1).getChilds().get(1).getName());
		Assert.assertEquals(2, topics.get(1).getChilds().get(0).getNumberOfChilds());
		Assert.assertEquals(0, topics.get(1).getChilds().get(1).getNumberOfChilds());
		
	}
	
	/**
	 * Trees are equal -> Filter matches
	 * 
	 * 		Advertisement Tree						Subscription Tree
	 * 
	 *  			News			  					News
	 *  			 /\									 /\
	 *  			/  \								/  \
	 *    Wirtschaft    Politik				  Wirtschaft    Politik
	 */
	@Test
	public void compareTreesCase1Test(){
		Topic advertisementTopic = new Topic("News");
		advertisementTopic.getChilds().add(new Topic("Wirtschaft"));
		advertisementTopic.getChilds().add(new Topic("Politik"));
		Topic subscriptionTopic = new Topic("News");
		subscriptionTopic.getChilds().add(new Topic("Wirtschaft"));
		subscriptionTopic.getChilds().add(new Topic("Politik"));
		Assert.assertTrue(TopicHelper.compareTrees(advertisementTopic, subscriptionTopic));
	}
	
	/**
	 * Subscription has additional Subtree -> Filter matches
	 * 
	 * 		Advertisement Tree						Subscription Tree
	 * 
	 *  			News			  					News
	 *  			 /									 /\
	 *  			/  									/  \
	 *    Wirtschaft    					  Wirtschaft    Politik
	 */
	@Test
	public void compareTreesCase2Test(){
		Topic advertisementTopic = new Topic("News");
		advertisementTopic.getChilds().add(new Topic("Wirtschaft"));
		Topic subscriptionTopic = new Topic("News");
		subscriptionTopic.getChilds().add(new Topic("Wirtschaft"));
		subscriptionTopic.getChilds().add(new Topic("Politik"));
		Assert.assertTrue(TopicHelper.compareTrees(advertisementTopic, subscriptionTopic));
	}
	
	/**
	 * Subscription has additional Subtree -> Filter matches
	 * 
	 * 		Advertisement Tree						Subscription Tree
	 * 
	 *  			News			  					News
	 *  			 /\									
	 *  			/  \								
	 *    Wirtschaft    Politik					 
	 */
	@Test
	public void compareTreesCase6Test(){
		Topic advertisementTopic = new Topic("News");
		advertisementTopic.getChilds().add(new Topic("Wirtschaft"));
		advertisementTopic.getChilds().add(new Topic("Politik"));
		Topic subscriptionTopic = new Topic("News");
		Assert.assertTrue(TopicHelper.compareTrees(advertisementTopic, subscriptionTopic));
	}
	
	/**
	 * Advertisement has additional Subtree -> Filter matches not
	 * 
	 * 		Advertisement Tree						Subscription Tree
	 * 
	 *  			News			  					News
	 *  			 /\									 /
	 *  			/  \								/  
	 *    Wirtschaft    Politik				  Wirtschaft    
	 */
	@Test
	public void compareTreesCase3Test(){
		Topic advertisementTopic = new Topic("News");
		advertisementTopic.getChilds().add(new Topic("Wirtschaft"));
		advertisementTopic.getChilds().add(new Topic("Politik"));
		Topic subscriptionTopic = new Topic("News");
		subscriptionTopic.getChilds().add(new Topic("Wirtschaft"));
		Assert.assertFalse(TopicHelper.compareTrees(advertisementTopic, subscriptionTopic));
	}
	
	
	/**
	 * Subscription has Subtrees but Advertisement not -> Filter matches not
	 * 
	 * 		Advertisement Tree						Subscription Tree
	 * 
	 *  			News			  					News
	 *  			 									 /\
	 *  												/  \
	 *    									  Wirtschaft    Politik
	 */
	@Test
	public void compareTreesCase4Test(){
		Topic advertisementTopic = new Topic("News");
		Topic subscriptionTopic = new Topic("News");
		subscriptionTopic.getChilds().add(new Topic("Wirtschaft"));
		subscriptionTopic.getChilds().add(new Topic("Politik"));
		Assert.assertFalse(TopicHelper.compareTrees(advertisementTopic, subscriptionTopic));
	}
	
	/**
	 * Trees are not equal -> Filter matches not
	 * 
	 * 		Advertisement Tree						Subscription Tree
	 * 
	 *  			News			  					News
	 *  			 /\									 /\
	 *  			/  \								/  \
	 *    Wirtschaft    Politik				  Wirtschaft    Unterhaltung
	 */
	@Test
	public void compareTreesCase5Test(){
		Topic advertisementTopic = new Topic("News");
		advertisementTopic.getChilds().add(new Topic("Wirtschaft"));
		advertisementTopic.getChilds().add(new Topic("Politik"));
		Topic subscriptionTopic = new Topic("News");
		subscriptionTopic.getChilds().add(new Topic("Wirtschaft"));
		subscriptionTopic.getChilds().add(new Topic("Unterhaltung"));
		Assert.assertFalse(TopicHelper.compareTrees(advertisementTopic, subscriptionTopic));
	}

}
