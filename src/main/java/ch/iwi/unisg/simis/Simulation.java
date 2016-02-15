package ch.iwi.unisg.simis;

import java.util.ArrayList;
import java.util.Random;

public class Simulation {
	
	public Organization org;
	public Strategy strat;
	public int startNodeId;
	
	public Simulation(Organization org, Strategy strat, int startNodeId)
	{
		this.org = org;
		this.strat = strat;
		this.startNodeId = startNodeId;
	}
	
	public enum Strategy {
		NONE,
		RANDOM,
		MAX_DEGREE
	}
	
	public Organization step()
	{
		Random rand = new Random();
		ArrayList<AgentAgentLink> ingoingLinks = new ArrayList<AgentAgentLink>();
		double[] newWeights = new double[org.N];
		
		if(strat == Strategy.RANDOM)
		{
			// Handle startNode separately:
			while(true){
				int randomNodeId = rand.nextInt(org.N);
				AgentAgentLink randomLink = org.linkMatrix[randomNodeId][startNodeId];
				if(randomLink != null && randomLink.trust > 0){
					org.nodes[randomLink.targetID] = org.nodes[randomLink.targetID] + (1-org.nodes[randomLink.targetID])*randomLink.trust*2;
					break;
				}
			}
			
			for(int j = 0; j < org.N; j++){
				ingoingLinks.clear();
				double trust = org.nodes[j];
				for(int i = 0; i < org.N; i++){
					if(org.linkMatrix[i][j] != null){
						if(i != startNodeId)
							if(org.nodes[i] != 0 && rand.nextFloat() < org.linkMatrix[i][j].weight)
								ingoingLinks.add(org.linkMatrix[i][j]);
					}
				}
				for(AgentAgentLink curLink : ingoingLinks){
					trust += (1 - trust) * curLink.trust * org.nodes[curLink.sourceID - 1];
				}
				if(trust < 0 || trust > 1)
					trust = 0;
				newWeights[j] = trust;
			}
			org.nodes = newWeights;
		}
		else if(strat == Strategy.MAX_DEGREE)
		{
			//calculate best link and maxValue
			double maxValue = 0;
			int bestId = 1;
			for(int i=0; i < org.N; i++){
				if(org.linkMatrix[startNodeId][i] != null && org.linkMatrix[startNodeId][i].weight > 0){
					double curValue = 0;
					for(int j=0; j < org.N; j++){
						if(org.linkMatrix[i][j] != null && org.linkMatrix[i][j].weight > 0){
							curValue += org.linkMatrix[i][j].weight * org.linkMatrix[i][j].trust;
						}
					}
					curValue /= (1-org.nodes[i]);
					curValue /= org.linkMatrix[startNodeId][i].trust;
					if(curValue > maxValue){
						maxValue = curValue;
						bestId = i;
					}
				}
			}
			org.nodes[bestId] = org.nodes[bestId] + (1-org.nodes[bestId])*org.linkMatrix[startNodeId][bestId].trust*2;
			
			for(int j = 0; j < org.N; j++){
				ingoingLinks.clear();
				double trust = org.nodes[j];
				for(int i = 0; i < org.N; i++){
					if(org.linkMatrix[i][j] != null){
						if(i != startNodeId)
							if(org.nodes[i] != 0 && rand.nextFloat() < org.linkMatrix[i][j].weight)
								ingoingLinks.add(org.linkMatrix[i][j]);
					}
				}
				for(AgentAgentLink curLink : ingoingLinks){
					trust += (1 - trust) * curLink.trust * org.nodes[curLink.sourceID - 1];
				}
				if(trust < 0 || trust > 1)
					trust = 0;
				newWeights[j] = trust;
			}
			org.nodes = newWeights;
		}
		else
		{
			for(int j = 0; j < org.N; j++){
				ingoingLinks.clear();
				double trust = org.nodes[j];
				for(int i = 0; i < org.N; i++){
					if(org.linkMatrix[i][j] != null){
						if(org.nodes[i] != 0 && rand.nextFloat() < org.linkMatrix[i][j].weight)
							ingoingLinks.add(org.linkMatrix[i][j]);
					}
				}
				for(AgentAgentLink curLink : ingoingLinks){
					trust += (1 - trust) * curLink.trust * org.nodes[curLink.sourceID - 1];
				}
				if(trust < 0 || trust > 1)
					trust = 0;
				newWeights[j] = trust;
			}
			org.nodes = newWeights;
		}
		
		return this.org;
	}
	
	public Organization run(int steps)
	{
		for(int i = 0; i < steps; i ++)
		{
			this.step();
		}
		return this.org;
	}
}
