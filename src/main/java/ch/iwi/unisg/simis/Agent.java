package ch.iwi.unisg.simis;

import java.util.HashMap;

public class Agent {
	
	public int id; //Note: Always set this! Needs to be unique (used in AgentList of OrgFactory)
	public int capacity; // Time available (e.g. 8)
	public HashMap<TaskType, Integer> skills; // Task-completion if one time unit is spent
	
	
	public Agent(int id, int capacity, HashMap<TaskType, Integer> skills)
	{
		this.id = id;
		this.capacity = capacity;
		this.skills = skills;
	}
}
