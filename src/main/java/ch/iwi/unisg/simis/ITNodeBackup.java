package ch.iwi.unisg.simis;

import java.util.Hashtable;


public class ITNodeBackup {
	public int id;
	//public String name;
	//public String SAPNr;
	//public int quartierId;
	
	// Potential uses of the ITNode
	public Hashtable<TaskType,Integer> capacity;
	
	
	/*public ITNode(int id, String name, String SAPNr, int quartierId)
	{
		this.id = id;
		this.name = name;
		this.SAPNr = SAPNr;
		this.quartierId = quartierId;
	}*/
	
	public ITNodeBackup(int id)
	{
		this.id = id;
		this.capacity = new Hashtable<TaskType,Integer>();
	}
	
	public ITNodeBackup(int id, Hashtable<TaskType,Integer> capacity)
	{
		this.id = id;
		this.capacity = capacity;
	}
	
	public Task use(Task task)
	{
		//TODO
		throw new UnsupportedOperationException();
	}
	
	
}
