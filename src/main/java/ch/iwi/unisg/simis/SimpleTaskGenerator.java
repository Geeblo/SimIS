package ch.iwi.unisg.simis;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Generate tasks by normal distributions.
 * @author D048980
 *
 */
public class SimpleTaskGenerator implements TaskGenerator{
	
	private double meanNumTasks;
	private double sdNumTasks;
	private double meanNumTaskComponents;
	private double sdNumTaskComponents;
	private double meanSizeTaskComponents;
	private double sdSizeTaskComponents;
	private double meanTaskTime;
	private double sdTaskTime;
	private ArrayList<TaskType> taskTypes;
	
	public SimpleTaskGenerator(
			ArrayList<TaskType> taskTypes,
			double meanNumTasks, double sdNumTasks,
			double meanNumTaskComponents, double sdNumTaskComponents,
			double meanSizeTaskComponents, double sdSizeTaskComponents,
			double meanTaskTime, double sdTaskTime) {
		this.meanNumTasks = meanNumTasks;
		this.sdNumTasks = sdNumTasks;
		this.meanNumTaskComponents = meanNumTaskComponents;
		this.sdNumTaskComponents = sdNumTaskComponents;
		this.meanSizeTaskComponents = meanSizeTaskComponents;
		this.sdSizeTaskComponents = sdSizeTaskComponents;
		this.meanTaskTime = meanTaskTime;
		this.sdTaskTime = sdTaskTime;
		this.taskTypes = taskTypes;
	}

	@Override
	public ArrayList<Task> generateTasks() {
		Random rand = new Random();
		
		NormalDistribution distNumTasks = new NormalDistribution(meanNumTasks, sdNumTasks);
		NormalDistribution distNumTaskComponents = new NormalDistribution(meanNumTaskComponents, sdNumTaskComponents);
		NormalDistribution distSizeTaskComponents = new NormalDistribution(meanSizeTaskComponents, sdSizeTaskComponents);
		NormalDistribution distTaskTime = new NormalDistribution(meanTaskTime, sdTaskTime);
		int numTasks = Math.max((int)Math.round(distNumTasks.sample()), 0); // Round and set to 0 if negative
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(int i=0; i < numTasks; i++){
		
			int taskTime = Math.max((int)Math.round(distTaskTime.sample()), 1);
			int numTaskComponents = Math.max((int)Math.round(distNumTaskComponents.sample()), 0);
			
			Task task = new Task(taskTime);
			for(int j=0; j < numTaskComponents; j++){
				// randomly choose a task type and size
				int taskComponentSize = Math.max((int)Math.round(distSizeTaskComponents.sample()), 0);
				TaskType taskType = taskTypes.get(rand.nextInt(taskTypes.size()));
				task.addTaskComponent(taskType, taskComponentSize);
			}
			tasks.add(task);
		}
		
		
		return tasks;
	}
	
}
