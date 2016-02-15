package ch.iwi.unisg.simis;

import java.util.HashMap;


public class ITNode {
	public int id; //Note: Always set this! Needs to be unique 
	public int capacity;
	public HashMap<TaskType, Integer> skills;
	
	
	public ITNode(int id, int capacity, HashMap<TaskType, Integer> skills)
	{
		this.id = id;
		this.capacity = capacity;
		this.skills = skills;
	}

	
	
}
