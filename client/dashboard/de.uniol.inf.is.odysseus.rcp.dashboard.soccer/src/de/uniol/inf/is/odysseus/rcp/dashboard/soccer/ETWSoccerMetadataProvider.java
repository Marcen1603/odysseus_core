package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ETWSoccerMetadataProvider {

	private ETWSoccerMetadataProvider() {
		
	}
	
	public static List<Integer> getSidBalls() {
		List<Integer> sidBalls = Lists.newArrayList();
		sidBalls.add(22);
		return sidBalls;
	}
	
	public static List<Integer> getSidTeamA() {
		List<Integer> sidTeamA = Lists.newArrayList();
		sidTeamA.add(0);
		sidTeamA.add(1);
		sidTeamA.add(2);
		sidTeamA.add(3);
		sidTeamA.add(4);
		sidTeamA.add(5);
		sidTeamA.add(6);
		sidTeamA.add(7);
		sidTeamA.add(8);
		sidTeamA.add(9);
		sidTeamA.add(10);
		return sidTeamA;
	}

	public static List<Integer> getSidTeamB() {
		List<Integer> sidTeamB = Lists.newArrayList();
		sidTeamB.add(11);
		sidTeamB.add(12);
		sidTeamB.add(13);
		sidTeamB.add(14);
		sidTeamB.add(15);
		sidTeamB.add(16);
		sidTeamB.add(17);
		sidTeamB.add(18);
		sidTeamB.add(19);
		sidTeamB.add(20);
		sidTeamB.add(21);
		return sidTeamB;
	}

	public static List<Integer> getSidReferee() {
		List<Integer> sidReferee = Lists.newArrayList();
		sidReferee.add(23);
		return sidReferee;
	}

	public static Map<Integer, Integer> getSensorPlayerMap() {
		Map<Integer, Integer> sensorIdToPlayerId = Maps.newHashMap();
		sensorIdToPlayerId.put(0, 0);
		sensorIdToPlayerId.put(1, 1);
		sensorIdToPlayerId.put(2, 2);
		sensorIdToPlayerId.put(3, 3);
		sensorIdToPlayerId.put(4, 4);
		sensorIdToPlayerId.put(5, 5);
		sensorIdToPlayerId.put(6, 6);
		sensorIdToPlayerId.put(7, 7);
		sensorIdToPlayerId.put(8, 8);
		sensorIdToPlayerId.put(9, 9);
		sensorIdToPlayerId.put(10, 10);
		
		sensorIdToPlayerId.put(11, 11);
		sensorIdToPlayerId.put(12, 12);
		sensorIdToPlayerId.put(13, 13);
		sensorIdToPlayerId.put(14, 14);
		sensorIdToPlayerId.put(15, 15);
		sensorIdToPlayerId.put(16, 16);
		sensorIdToPlayerId.put(17, 17);
		sensorIdToPlayerId.put(18, 18);
		sensorIdToPlayerId.put(19, 19);
		sensorIdToPlayerId.put(20, 20);
		
		sensorIdToPlayerId.put(21, 21);
		sensorIdToPlayerId.put(22, 22);
		sensorIdToPlayerId.put(23, 23);
		return sensorIdToPlayerId;
	}
}
