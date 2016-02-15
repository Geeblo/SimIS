package ch.iwi.unisg.simis;

import ch.iwi.unisg.simis.Simulation.Strategy;

import java.io.IOException;
import java.util.ArrayList;

import ch.iwi.unisg.simis.Conf;

public class App 
{	
	public static ArrayList<TaskType> taskTypes;
	public static Environment environment;
	
    public static void main( String[] args )
    {
        init();
        
        ITNetworkFactory itNetworkFactory = new ITNetworkFactory();
        OrganizationFactory organizationFactory = new OrganizationFactory(environment);
        ITNetwork itNetwork = null;
        
        //Test-------------------
        ArrayList<Task> curTasks = environment.generateTasks(1);
        curTasks = environment.generateTasks(1);
        curTasks = environment.generateTasks(1);
        curTasks = environment.generateTasks(1);
        curTasks = environment.generateTasks(1);
        curTasks = environment.generateTasks(1);
        curTasks = environment.generateTasks(1);
        //End--------------------
        
        try{
        	itNetwork = itNetworkFactory.getPFNetwork();
        }catch(IOException e){
        	System.out.println(e.getMessage());
        	return;
        }

        int startNodeId = 30;
        double socialNetworkFactor = 0.20;
        double organizationNetworkFactor = 0.3;
        
        Organization organization = organizationFactory.getSimpleStructure(socialNetworkFactor, organizationNetworkFactor, environment);
        //Organization organization = organizationFactory.getMachineBureaucracy(0.01, 0.5);
        Util.storeAsGV(organization.linkList, Conf.orgGraphPath);
        
        //Run the simulation
        organization.nodes[startNodeId] = 1;
        Simulation sim = new Simulation(organization, Strategy.MAX_DEGREE, startNodeId);
        
        Util.storeNodeList(sim.org.nodes, Conf.nodeListPath);
        for(int step = 0; step < 30; step++){
        	Util.storeNodeList(sim.step().nodes, Conf.nodeListPath);
        }
    }
    
    public static void init()
    {
    	taskTypes = new ArrayList<TaskType>();
        taskTypes.add(new TaskType(1, "research"));
        taskTypes.add(new TaskType(2, "organisational"));
        taskTypes.add(new TaskType(3, "it problem"));
        taskTypes.add(new TaskType(4, "personell"));
        taskTypes.add(new TaskType(5, "programming"));
        taskTypes.add(new TaskType(6, "marketing"));
        taskTypes.add(new TaskType(7, "infrastructure"));
		
        environment = new Environment(
        		new SimpleTaskGenerator(taskTypes, 4, 2, 3, 1, 10, 4, 5, 3),
        		taskTypes);
    }
}
