package ch.iwi.unisg.simis;


public class ITNetwork {

	public ITITLink[][] itMatrix;
	public ITNode[] itNodes;
	public int numNodes;
	
	
	public ITNetwork(ITITLink[][] itMatrix, ITNode[] itNodes, int numNodes)
	{
		this.itMatrix = itMatrix;
		this.itNodes = itNodes;
		this.numNodes = numNodes;
	}
	

}
