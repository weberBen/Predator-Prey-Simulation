package Cell;
public class Obstacle extends ObjectMap
{
	private double rigidity;
	private double height;
	private double length;
	
	public Obstacle(double x, double y, double rigidity, double height, double length)
	{
		super(x, y);
		this.rigidity = rigidity;
		this.height = height;
		this.length = length;
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
	
	public double getLength()
	{
		return length;
	}
	
	public void setLength(double l)
	{
		length = l;
	}
}
