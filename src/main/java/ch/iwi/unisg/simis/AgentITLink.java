package ch.iwi.unisg.simis;


public class AgentITLink {
	
	public Agent agent;
	public ITNode itNode;
	
	public double weight = 1;
	
	
	
	public AgentITLink(Agent agent, ITNode itNode)
	{
		this.agent = agent;
		this.itNode = itNode;
	}
}
