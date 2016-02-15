package ch.iwi.unisg.simis;

import java.util.ArrayList;

//External setting for some variables that influence system behavior
//E.g. what are "tasks" that can be generated?
//     What are limits for it-components in terms of technology available?
//Mostly public static variables
public class Environment {
	public ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
	public TaskGenerator taskGenerator;
	
	public Environment(TaskGenerator taskGenerator, ArrayList<TaskType> taskTypes)
	{
		this.taskGenerator = taskGenerator;
		this.taskTypes = taskTypes;
	}
	
	public ArrayList<Task> generateTasks(int currentTimestep){
		return taskGenerator.generateTasks();
	}
}
