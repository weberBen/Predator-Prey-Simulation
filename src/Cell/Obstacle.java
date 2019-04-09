package Cell;
public class Obstacle extends ObjectMap
{
	private double rigidity;
	private double height;
	private double dimX;
	private double dimY;
	
	public Obstacle(double x, double y, double rigidity, double height, double dimX, double dimY)
	{
		super(x, y);
		this.rigidity = rigidity;
		this.height = height;
		this.dimX = dimX;
		this.dimY = dimY;
	}
	

	public double getRigidity()
	{
		return rigidity;
	}
	
	public void setRigidity(double r)
	{
		rigidity = r;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public void setHeight(double h)
	{
		height = h;
	}
	
	public double getDimX()
	{
		return dimX;
	}
	
	public void setDimX(double dimX)
	{
		this.dimX=dimX;
	}
	
	public double getDimY()
	{
		return dimY;
	}
	
	public void setDimY(double dimY)
	{
		this.dimY=dimY;
	}
}
