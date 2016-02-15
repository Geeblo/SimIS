package ch.iwi.unisg.simis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

public class ITNetworkFactory {

	public ITNetwork getPFNetwork() throws IOException 
	{
		return Util.readPFData(Conf.PF_GF_FILE_NAME, Conf.PF_METIFY_FILE_NAME, Conf.PF_PFAPAM_FILE_NAME, Conf.PF_SAP_FILE_NAME);
	}
	
	public ITNetwork getSimpleITNetwork(
			int numNodes, 
			double meanNodeDegree, double sdNodeDegree,
			ArrayList<TaskType> taskTypes,
			double meanNumTaskTypes, double sdNumTaskTypes, 
			double meanSkill, double sdSkill,
			double meanCapacity, double sdCapacity)
	{
		Random rand = new Random();
		
		HashMap<TaskType, Integer> skills = new HashMap<TaskType, Integer>();
		NormalDistribution distNumTaskTypes = new NormalDistribution(meanNumTaskTypes, sdNumTaskTypes);
		NormalDistribution distSkill = new NormalDistribution(meanSkill, sdSkill);
		NormalDistribution distCapacity = new NormalDistribution(meanCapacity, sdCapacity);
		
		//1.)Generate Nodes
		ITNode[] itNodes = new ITNode[numNodes];
		for(int i = 0; i < numNodes; i ++)
		{
			// Round and set to 0 if negative
			int numTaskTypes = Math.max((int)Math.round(distNumTaskTypes.sample()), 0);
			int capacity = Math.max((int)Math.round(distCapacity.sample()), 0); 
			
			for(int j=0; j < numTaskTypes; j++){
			
				Integer skill = (int)Math.round(Math.max(distSkill.sample(), 0));
				
				// randomly choose a task type
				TaskType taskType = taskTypes.get(rand.nextInt(taskTypes.size()));
				
				// only add if not already present
				if(!skills.containsKey(taskType))
					skills.put(taskType, skill);
			}
			itNodes[i] = new ITNode(i, capacity, skills);
		}
	
		
		//2.)Generate network structure

		NormalDistribution distNodeDegree = new NormalDistribution(meanNodeDegree, sdNodeDegree);
		
		ITITLink[][] itMatrix = new ITITLink[numNodes][numNodes];
		for(int i = 0; i < numNodes; i++)
		{
			int nodeDegree = Math.max((int)Math.round(distNodeDegree.sample()), 0);
			for(int linkCount = 0; linkCount < nodeDegree; linkCount++){
				// select random node to connect with
				int nodeToId = rand.nextInt(numNodes);
				if(itMatrix[i][nodeToId] == null)
					itMatrix[i][nodeToId] = new ITITLink(itNodes[i],itNodes[nodeToId]);
			}
		}
		
		
		return new ITNetwork(itMatrix,  itNodes, numNodes);
	}
}
