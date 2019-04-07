package Cell;

public class ObjectMap 
{
	protected double x;
	protected double y;
	
	public ObjectMap(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public ObjectMap()
	{
		this(0,0);
	}
	
	public double getX()
	{
		return x;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public double getY()
	{
		return x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
}
