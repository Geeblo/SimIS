package ch.iwi.unisg.simis;

import java.util.Hashtable;

public class Task {

	// Time until task should be fulfilled
	public int timeRemaining;
	public Hashtable<TaskType,Integer> taskComponents;
	
	public Task(int timeRemaining){
		this.taskComponents = new Hashtable<TaskType,Integer>();
		this.timeRemaining = timeRemaining;
	}
	
	public void addTaskComponent(TaskType taskType, int taskSize)
	{
		if(taskComponents.containsKey(taskType))
		{
			taskComponents.replace(taskType, taskComponents.get(taskType) + taskSize);
		}
		else
		{
			taskComponents.put(taskType, taskSize);
		}
	}
	
	public boolean hasType(TaskType taskType)
	{
		return taskComponents.containsKey(taskType);
	}
	
	
}
