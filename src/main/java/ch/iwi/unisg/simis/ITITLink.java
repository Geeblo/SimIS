package ch.iwi.unisg.simis;

public class ITITLink {
	public ITNode source;
	public ITNode target;
	public double weight = 1;

	public ITITLink(ITNode source, ITNode target, double weight)
	{
		this.source = source;
		this.target = target;
		this.weight = weight;
	}
	
	public ITITLink(ITNode source, ITNode target)
	{
		this(source, target, 1);
	}
}
