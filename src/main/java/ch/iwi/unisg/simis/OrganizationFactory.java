package ch.iwi.unisg.simis;

import ch.iwi.unisg.simis.Organization.OrganizationType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class OrganizationFactory {
	
	public Environment environment;
	
	/*
	 * Definitions for simple structure 
	 */
	public static int SS_N = 100;
	public static int SS_N_OC = 80, SS_MAXLEVEL_OC = 2, SS_GROUPSIZE_OC = 9;
	public static int SS_N_SA = 5, SS_MAXLEVEL_SA = 1, SS_GROUPSIZE_SA = 5;
	public static int SS_N_ML = 5, SS_MAXLEVEL_ML = 1, SS_GROUPSIZE_ML = 5;
	public static int SS_N_TS = 0, SS_MAXLEVEL_TS = 0, SS_GROUPSIZE_TS = 0;
	public static int SS_N_SS = 10, SS_MAXLEVEL_SS = 1, SS_GROUPSIZE_SS = 10;
	public static double SS_U_W_OC = .5; 
	public static double SS_D_W_OC = .3; 
	public static double SS_E_W_OC = .6; 
	public static double SS_U_W_SA = 0; 
	public static double SS_D_W_SA = 0; 
	public static double SS_E_W_SA = .7;
	public static double SS_U_W_ML = 0; 
	public static double SS_D_W_ML = 0; 
	public static double SS_E_W_ML = .7;
	public static double SS_U_W_TS = 0; 
	public static double SS_D_W_TS = 0; 
	public static double SS_E_W_TS = 0;
	public static double SS_U_W_SS = 0; 
	public static double SS_D_W_SS = 0; 
	public static double SS_E_W_SS = .5;
	public static OLink[][] SS_LINKS = new OLink[][]{
		{null, new OLink(20,.2), new OLink(40,.3), new OLink(0,0), new OLink(30,.3)},
		{new OLink(10,.2), null, new OLink(10,.3), new OLink(0,0), new OLink(0,0)},
		{new OLink(30,.3), new OLink(20,.4), null, new OLink(0,0), new OLink(0,0)},
		{new OLink(0,0), new OLink(0,0), new OLink(0,0), null, new OLink(0,0)},
		{new OLink(40,.3), new OLink(5,.2), new OLink(10,.2), new OLink(0,0), null}
	};
	
	
	/*
	 * Definitions for machine bureaucracy
	 */
	public static int MB_N = 1000;
	public static int MB_N_OC = 700, MB_MAXLEVEL_OC = 3, MB_GROUPSIZE_OC = 15;
	public static int MB_N_SA = 20, MB_MAXLEVEL_SA = 2, MB_GROUPSIZE_SA = 5;
	public static int MB_N_ML = 80, MB_MAXLEVEL_ML = 3, MB_GROUPSIZE_ML = 6;
	public static int MB_N_TS = 150, MB_MAXLEVEL_TS = 3, MB_GROUPSIZE_TS = 6;
	public static int MB_N_SS = 50, MB_MAXLEVEL_SS = 2, MB_GROUPSIZE_SS = 8;
	public static double MB_U_W_OC = .2; 
	public static double MB_D_W_OC = .1; 
	public static double MB_E_W_OC = .3; 
	public static double MB_U_W_SA = .3; 
	public static double MB_D_W_SA = .2; 
	public static double MB_E_W_SA = .6;
	public static double MB_U_W_ML = .3; 
	public static double MB_D_W_ML = .1; 
	public static double MB_E_W_ML = .3;
	public static double MB_U_W_TS = .3; 
	public static double MB_D_W_TS = .3; 
	public static double MB_E_W_TS = .7;
	public static double MB_U_W_SS = .1; 
	public static double MB_D_W_SS = .1; 
	public static double MB_E_W_SS = .1;
	public static OLink[][] MB_LINKS 
		= new OLink[][]{
		{null, new OLink(0,0), new OLink(150,.2), new OLink(100,.2), new OLink(50,.1)},
		{new OLink(0,0), null, new OLink(30,.1), new OLink(60,.1), new OLink(0,0)},
		{new OLink(100,.1), new OLink(40,.2), null, new OLink(100,.1), new OLink(10,.1)},
		{new OLink(300,.3), new OLink(20,.1), new OLink(100,.2), null, new OLink(20,.1)},
		{new OLink(50,.1), new OLink(0,0), new OLink(10,.1), new OLink(20,.1), null}
	};	
	
	public OrganizationFactory(Environment environment){
		this.environment = environment;
	}
	
	public Organization getOrganization(
		Environment environment,
		double socialNetworkFactor, double organizationNetworkFactor,
		int N, OrganizationType orgType,
		int N_OC, int GROUPSIZE_OC, int MAXLEVEL_OC, double U_W_OC, double D_W_OC, double E_W_OC,
		int N_SA, int GROUPSIZE_SA, int MAXLEVEL_SA, double U_W_SA, double D_W_SA, double E_W_SA, 
		int N_ML, int GROUPSIZE_ML, int MAXLEVEL_ML, double U_W_ML, double D_W_ML, double E_W_ML, 
		int N_TS, int GROUPSIZE_TS, int MAXLEVEL_TS, double U_W_TS, double D_W_TS, double E_W_TS, 
		int N_SS, int GROUPSIZE_SS, int MAXLEVEL_SS, double U_W_SS, double D_W_SS, double E_W_SS, 
		OLink[][] LINKS,
		Path SOCIAL_NETWORK_FILE)
	{
		Random rand = new Random();
		
		
		AgentAgentLink[][] F = new AgentAgentLink[N][N]; //Link matrix
		Agent[] agentList = new Agent[N]; //List of agents in organization
		
		// generate agents
		AgentFactory agentFactory = new AgentFactory();
		for(int id = 0; id < agentList.length; id++)
			agentList[id] = agentFactory.generateSimpleAgent(id, environment.taskTypes, 3, 0.5, 20, 3, 10);
			//double meanNumTaskTypes, double sdNumTaskTypes, double meanSkill, double sdSkill, int capacity
		
		int[] maxNodeId = new int[1];
		maxNodeId[0] = 0;
		int[] nodeCounter = new int[1];
		
		
		// Create basic parts
		ArrayList<AgentAgentLink> linkList = new ArrayList<AgentAgentLink>();
		int minOCNodeID = maxNodeId[0] + 1; 
		nodeCounter[0] = 0;
		createOrganizationPart(maxNodeId[0], maxNodeId, 0, N_OC, nodeCounter, GROUPSIZE_OC, MAXLEVEL_OC, linkList, U_W_OC, D_W_OC, E_W_OC, agentList);
		int maxOCNodeID = maxNodeId[0]; 
		
		int minSANodeID = maxNodeId[0] + 1; 
		nodeCounter[0] = 0;
		createOrganizationPart(maxNodeId[0], maxNodeId, 0, N_SA, nodeCounter, GROUPSIZE_SA, MAXLEVEL_SA, linkList, U_W_SA, D_W_SA, E_W_SA, agentList);
		int maxSANodeID = maxNodeId[0]; 
		
		int minMLNodeID = maxNodeId[0] + 1; 
		nodeCounter[0] = 0;
		createOrganizationPart(maxNodeId[0], maxNodeId, 0, N_ML, nodeCounter, GROUPSIZE_ML, MAXLEVEL_ML, linkList, U_W_ML, D_W_ML, E_W_ML, agentList);
		int maxMLNodeID = maxNodeId[0]; 
		
		int minTSNodeID = maxNodeId[0] + 1;
		nodeCounter[0] = 0;
		createOrganizationPart(maxNodeId[0], maxNodeId, 0, N_TS, nodeCounter, GROUPSIZE_TS, MAXLEVEL_TS, linkList, U_W_TS, D_W_TS, E_W_TS, agentList);
		int maxTSNodeID = maxNodeId[0];
		
		int minSSNodeID = maxNodeId[0] + 1;
		nodeCounter[0] = 0;
		createOrganizationPart(maxNodeId[0], maxNodeId, 0, N_SS, nodeCounter, GROUPSIZE_SS, MAXLEVEL_SS, linkList, U_W_SS, D_W_SS, E_W_SS, agentList);
		int maxSSNodeID = maxNodeId[0];
		
		// Link parts
		int[][] partNodeIDRanges = new int[][]{
			{minOCNodeID, maxOCNodeID},
			{minSANodeID, maxSANodeID},
			{minMLNodeID, maxMLNodeID},
			{minTSNodeID, maxTSNodeID},
			{minSSNodeID, maxSSNodeID}
		};
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if(i != j){
					//Link parts i and j:
					for(int linkCounter = 0; linkCounter < LINKS[i][j].number; linkCounter++){
						if(partNodeIDRanges[i][0] > partNodeIDRanges[i][1] || partNodeIDRanges[j][0] > partNodeIDRanges[j][1])
							break;
						//select random nodes
						int inode = rand.nextInt(partNodeIDRanges[i][1] - partNodeIDRanges[i][0]) + partNodeIDRanges[i][0];
						int jnode = rand.nextInt(partNodeIDRanges[j][1] - partNodeIDRanges[j][0]) + partNodeIDRanges[j][0];
						linkList.add(new AgentAgentLink(agentList[inode], agentList[jnode], LINKS[i][j].averageWeight));
					}
				}
			}
		}
		
		// Store:
		Util.storeAsGV(linkList, Conf.fileOutputPath + orgType.toString() + "_org_network.gv");
		
		// Factor strength of organizational network
		for(AgentAgentLink curLink: linkList){
			curLink.weight *= organizationNetworkFactor;
		}

		// Read the LFR benchmark data
		int oldLinkListSize = linkList.size();
		double[] maxWeight = new double[]{0.0};
		try (Stream<String> lines = Files.lines(SOCIAL_NETWORK_FILE, Charset.defaultCharset())) {
			lines.forEachOrdered(line -> processLFRResult(line, linkList, maxWeight, agentList));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Normalize weights of LFR; and factor strength of organizational network
		for(int i = oldLinkListSize; i < linkList.size(); i++){
			linkList.get(i).weight = (linkList.get(i).weight / maxWeight[0]) * socialNetworkFactor;
		}
		
		// Add links to matrix and handle double or too weak links
		for(AgentAgentLink curLink: linkList){
			if(F[curLink.source.id-1][curLink.target.id-1] == null){
				F[curLink.source.id-1][curLink.target.id-1] = curLink;
			}else{
				//Use usual formula
				F[curLink.source.id-1][curLink.target.id-1].weight = 
						F[curLink.source.id-1][curLink.target.id-1].weight + curLink.weight -
						F[curLink.source.id-1][curLink.target.id-1].weight * curLink.weight;
			}
		}
		// Copy links back from matrix after normalizing
		linkList.clear();
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				if(F[i][j] != null)
					linkList.add(F[i][j]);
			}
		}
		
		// Create nodes with zero information
		double[] nodes = new double[N];
		for(int i = 0; i < N; i++)
			nodes[i] = 0;
		
		// Create IT network
		ITNetworkFactory itNetworkFactory = new ITNetworkFactory();
		
		//numNodes, meanNodeDegree, sdNodeDegree, taskTypes, meanNumTaskTypes, sdNumTaskTypes, 
		//meanSkill, sdSkill, meanCapacity, sdCapacity
		ITNetwork itNetwork = itNetworkFactory.getSimpleITNetwork(
				50, 10, 2, environment.taskTypes, 4, 1, 
				1, 0, 30, 3);
		
		
		//Organization(type, agentMatrix, itMatrix, agentItMatrix, partNodeIDRanges)
		new Organization(orgType, )
		return new Organization(orgType, F, linkList, partNodeIDRanges, nodes);
	}
	
	private void processLFRResult(String line, ArrayList<AgentAgentLink> linkList, double[] maxWeight, Agent[] agentList ) {
		Pattern pattern = Pattern.compile("(\\d+)\\t(\\d+)\\t(\\d+\\.\\d+)"); //(\\d+)..\\[weight=(\\d+\\.\\d+)\\]
		Matcher matcher = pattern.matcher(line);	
		if(matcher.find()){
			double weight = Double.parseDouble(matcher.group(3));
			if (weight > maxWeight[0])
				maxWeight[0] = weight;
			linkList.add(new AgentAgentLink(
					agentList[Integer.parseInt(matcher.group(1))], 
					agentList[Integer.parseInt(matcher.group(2))], 
					weight));
		}
	}

	public Organization getSimpleStructure(double socialNetworkFactor, double organizationNetworkFactor, Environment environment)
	{
		return getOrganization(environment, socialNetworkFactor, organizationNetworkFactor,
				OrganizationFactory.SS_N, OrganizationType.SIMPLE_STRUCTURE,
				OrganizationFactory.SS_N_OC, OrganizationFactory.SS_GROUPSIZE_OC, OrganizationFactory.SS_MAXLEVEL_OC, 
				OrganizationFactory.SS_U_W_OC, OrganizationFactory.SS_D_W_OC, OrganizationFactory.SS_E_W_OC, 
				OrganizationFactory.SS_N_SA, OrganizationFactory.SS_GROUPSIZE_SA, OrganizationFactory.SS_MAXLEVEL_SA, 
				OrganizationFactory.SS_U_W_SA, OrganizationFactory.SS_D_W_SA, OrganizationFactory.SS_E_W_SA, 
				OrganizationFactory.SS_N_ML, OrganizationFactory.SS_GROUPSIZE_ML, OrganizationFactory.SS_MAXLEVEL_ML, 
				OrganizationFactory.SS_U_W_ML, OrganizationFactory.SS_D_W_ML, OrganizationFactory.SS_E_W_ML, 
				OrganizationFactory.SS_N_TS, OrganizationFactory.SS_GROUPSIZE_TS, OrganizationFactory.SS_MAXLEVEL_TS, 
				OrganizationFactory.SS_U_W_TS, OrganizationFactory.SS_D_W_TS, OrganizationFactory.SS_E_W_TS,
				OrganizationFactory.SS_N_SS, OrganizationFactory.SS_GROUPSIZE_SS, OrganizationFactory.SS_MAXLEVEL_SS, 
				OrganizationFactory.SS_U_W_SS, OrganizationFactory.SS_D_W_SS, OrganizationFactory.SS_E_W_SS,
				OrganizationFactory.SS_LINKS,
				Conf.SS_SOCIAL_NETWORK_FILE);
	}
	
	public Organization getMachineBureaucracy(double socialNetworkFactor, double organizationNetworkFactor, Environment environment)
	{
		return getOrganization(environment, socialNetworkFactor, organizationNetworkFactor,
				OrganizationFactory.MB_N, OrganizationType.MACHINE_BUREAUCRACY, 
				OrganizationFactory.MB_N_OC, OrganizationFactory.MB_GROUPSIZE_OC, OrganizationFactory.MB_MAXLEVEL_OC, 
				OrganizationFactory.MB_U_W_OC, OrganizationFactory.MB_D_W_OC, OrganizationFactory.MB_E_W_OC,
				OrganizationFactory.MB_N_SA, OrganizationFactory.MB_GROUPSIZE_SA, OrganizationFactory.MB_MAXLEVEL_SA, 
				OrganizationFactory.MB_U_W_SA, OrganizationFactory.MB_D_W_SA, OrganizationFactory.MB_E_W_SA, 
				OrganizationFactory.MB_N_ML, OrganizationFactory.MB_GROUPSIZE_ML, OrganizationFactory.MB_MAXLEVEL_ML, 
				OrganizationFactory.MB_U_W_ML, OrganizationFactory.MB_D_W_ML, OrganizationFactory.MB_E_W_ML, 
				OrganizationFactory.MB_N_TS, OrganizationFactory.MB_GROUPSIZE_TS, OrganizationFactory.MB_MAXLEVEL_TS, 
				OrganizationFactory.MB_U_W_TS, OrganizationFactory.MB_D_W_TS, OrganizationFactory.MB_E_W_TS, 
				OrganizationFactory.MB_N_SS, OrganizationFactory.MB_GROUPSIZE_SS, OrganizationFactory.MB_MAXLEVEL_SS, 
				OrganizationFactory.MB_U_W_SS, OrganizationFactory.MB_D_W_SS, OrganizationFactory.MB_E_W_SS, 
				OrganizationFactory.MB_LINKS,
				Conf.MB_SOCIAL_NETWORK_FILE);
	}
	
	/**
	 * Use level = 0 to create with multiple people at the top level
	 */
	public void createOrganizationPart(
			int nodeId, int[] maxNodeId, int curLevel, int maxNodes, int nodeCounter[], int spread, int maxLevel, ArrayList<AgentAgentLink> links,
			double U_W, double D_W, double E_W, Agent[] agentList)
	{
		if(nodeCounter[0] == maxNodes)
			return;
		int oldMaxNodeId = maxNodeId[0];
		// Create new nodes and link with upper level
		for(int spreadCounter = 0; spreadCounter < spread; spreadCounter++){
			if(nodeCounter[0] == maxNodes)
				break;
			maxNodeId[0]++; nodeCounter[0]++;
			if(curLevel != 0){
				links.add(new AgentAgentLink(agentList[nodeId], agentList[maxNodeId[0]], D_W));
				links.add(new AgentAgentLink(agentList[maxNodeId[0]], agentList[nodeId], U_W));
			}
		}
		//create within-level links
		int levelMaxNodeId = maxNodeId[0];
		for(int i = oldMaxNodeId + 1; i <= levelMaxNodeId; i++){
			for(int j = i + 1; j <= levelMaxNodeId; j++){
				links.add(new AgentAgentLink(agentList[i], agentList[j], E_W));
				links.add(new AgentAgentLink(agentList[j], agentList[i], E_W));
			}
		}
		//recursively create lower levels
		if(curLevel < maxLevel - 1)
			for(int newNodeId = oldMaxNodeId + 1; newNodeId <= levelMaxNodeId; newNodeId++)
				createOrganizationPart(newNodeId, maxNodeId, curLevel + 1, maxNodes, nodeCounter, spread, maxLevel, 
						links, U_W, D_W, E_W, agentList);
		return;
	}
}

