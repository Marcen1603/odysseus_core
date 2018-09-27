package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SoccerMetadataProvider {

	private SoccerMetadataProvider() {
		
	}
	
	public static List<Integer> getSidBalls() {
		List<Integer> sidBalls = Lists.newArrayList();
		sidBalls.add(4);
		sidBalls.add(8);
		sidBalls.add(10);
		sidBalls.add(12);
		return sidBalls;
	}
	
	public static List<Integer> getSidTeamA() {
		List<Integer> sidTeamA = Lists.newArrayList();
		sidTeamA.add(13);
		sidTeamA.add(14);
		sidTeamA.add(97);
		sidTeamA.add(98);
		sidTeamA.add(47);
		sidTeamA.add(16);
		sidTeamA.add(49);
		sidTeamA.add(88);
		sidTeamA.add(19);
		sidTeamA.add(52);
		sidTeamA.add(53);
		sidTeamA.add(54);
		sidTeamA.add(23);
		sidTeamA.add(24);
		sidTeamA.add(57);
		sidTeamA.add(58);
		sidTeamA.add(59);
		sidTeamA.add(28);
		return sidTeamA;
	}

	public static List<Integer> getSidTeamB() {
		List<Integer> sidTeamB = Lists.newArrayList();
		sidTeamB.add(61);
		sidTeamB.add(62);
		sidTeamB.add(99);
		sidTeamB.add(100);
		sidTeamB.add(63);
		sidTeamB.add(64);
		sidTeamB.add(65);
		sidTeamB.add(66);
		sidTeamB.add(67);
		sidTeamB.add(68);
		sidTeamB.add(69);
		sidTeamB.add(38);
		sidTeamB.add(71);
		sidTeamB.add(40);
		sidTeamB.add(73);
		sidTeamB.add(74);
		sidTeamB.add(75);
		sidTeamB.add(44);
		return sidTeamB;
	}

	public static List<Integer> getSidReferee() {
		List<Integer> sidReferee = Lists.newArrayList();
		sidReferee.add(105);
		sidReferee.add(106);
		return sidReferee;
	}

	public static Map<Integer, Integer> getSensorPlayerMap() {
		Map<Integer, Integer> sensorIdToPlayerId = Maps.newHashMap();
		sensorIdToPlayerId.put(13, 1);
		sensorIdToPlayerId.put(47, 2);
		sensorIdToPlayerId.put(49, 3);
		sensorIdToPlayerId.put(19, 4);
		sensorIdToPlayerId.put(53, 5);
		sensorIdToPlayerId.put(23, 6);
		sensorIdToPlayerId.put(57, 7);
		sensorIdToPlayerId.put(59, 8);
		
		sensorIdToPlayerId.put(61, 11);
		sensorIdToPlayerId.put(63, 12);
		sensorIdToPlayerId.put(65, 13);
		sensorIdToPlayerId.put(67, 14);
		sensorIdToPlayerId.put(69, 15);
		sensorIdToPlayerId.put(71, 16);
		sensorIdToPlayerId.put(73, 17);
		sensorIdToPlayerId.put(75, 18);
		
		sensorIdToPlayerId.put(4, 4);
		sensorIdToPlayerId.put(8, 8);
		sensorIdToPlayerId.put(10, 10);
		sensorIdToPlayerId.put(12, 12);
		return sensorIdToPlayerId;
	}
}
