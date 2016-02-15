package ch.iwi.unisg.simis;

import java.util.ArrayList;


public class Organization {
	public enum OrganizationType {
		SIMPLE_STRUCTURE,
		MACHINE_BUREAUCRACY,
		PROFESSIONAL_BUREAUCRACY,
		DIVISIONALIZED_FORM,
		ADHOCRACY
	}

	public OrganizationType type;
	
	public int numAgents; 
	public int numIt;
	
	public Agent[] agentArray;
	public ITNode[] itNodeArray;
	
	public AgentAgentLink[][] agentMatrix;
	public ITITLink[][] itMatrix;
	public AgentITLink[][] agentItMatrix;
	
	public int[][] partNodeIDRanges; //OC, SA, ML, TS, SS
	
	
	
	
	public Organization(
			OrganizationType type, 
			int numAgents, 
			int numItNodes, 
			Agent[] agentArray, 
			ITNode[] itNodeArray,
			AgentAgentLink[][] agentMatrix, 
			ITITLink[][] itMatrix, 
			AgentITLink[][] agentItMatrix, 
			int[][] partNodeIDRanges)
	{
		this.type = type;
		this.numAgents = numAgents;
		this.agentMatrix = agentMatrix;
		this.itMatrix = itMatrix;
		this.agentItMatrix = agentItMatrix;
		this.partNodeIDRanges = partNodeIDRanges;
		this.numAgents = agentMatrix.length;
		this.numIt = itMatrix.length;
	}
	
	public void assignTasksToAgents(ArrayList<Task> taskList)
	{
		
	}

}
