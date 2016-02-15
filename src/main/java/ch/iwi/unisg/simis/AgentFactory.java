package ch.iwi.unisg.simis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

public class AgentFactory {

	public Agent generateSimpleAgent(
			int id,
			ArrayList<TaskType> taskTypes,
			double meanNumTaskTypes, double sdNumTaskTypes, 
			double meanSkill, double sdSkill,
			int capacity)
	{
		Random rand = new Random();
		
		HashMap<TaskType, Integer> skills = new HashMap<TaskType, Integer>();
		
		NormalDistribution distNumTaskTypes = new NormalDistribution(meanNumTaskTypes, sdNumTaskTypes);
		NormalDistribution distSkill = new NormalDistribution(meanSkill, sdSkill);
		int numTaskTypes = Math.max((int)Math.round(distNumTaskTypes.sample()), 0); // Round and set to 0 if negative
		
		for(int i=0; i < numTaskTypes; i++){
		
			Integer skill = (int)Math.round(Math.max(distSkill.sample(), 0));
			
			// randomly choose a task type
			TaskType taskType = taskTypes.get(rand.nextInt(taskTypes.size()));
			
			// only add if not already present
			if(!skills.containsKey(taskType))
				skills.put(taskType, skill);
		}
		
		return new Agent(id, capacity, skills);
	}
}
