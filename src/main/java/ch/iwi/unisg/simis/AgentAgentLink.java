package ch.iwi.unisg.simis;

public class AgentAgentLink {
	public Agent source;
	public Agent target;
	public double weight;

	public AgentAgentLink(Agent source, Agent target, double weight)
	{
		this.source = source;
		this.target = target;
		this.weight = weight;
	}
}
