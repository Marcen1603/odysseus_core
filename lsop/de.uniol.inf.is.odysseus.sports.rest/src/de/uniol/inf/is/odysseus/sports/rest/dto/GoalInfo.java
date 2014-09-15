package de.uniol.inf.is.odysseus.sports.rest.dto;

import java.io.Serializable;

/**
 * This class represents a data transfer object for a goal
 * @author Thomas
 *
 */
public class GoalInfo implements Serializable{

	private static final long serialVersionUID = 5061385579315643449L;
	private int position;
	private double first_post;
	private double second_post;
	private double deep;
	private double height;
	
	public GoalInfo(int position, double first_post, double second_post, double deep, double height) {
		
		this.position = position;
		this.first_post = first_post;
		this.second_post = second_post;
		this.deep = deep;
		this.height = height;
		
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public double getFirst_post() {
		return first_post;
	}

	public void setFirst_post(double first_post) {
		this.first_post = first_post;
	}

	public double getSecond_post() {
		return second_post;
	}

	public void setSecond_post(double second_post) {
		this.second_post = second_post;
	}

	public double getDeep() {
		return deep;
	}

	public void setDeep(double deep) {
		this.deep = deep;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}
