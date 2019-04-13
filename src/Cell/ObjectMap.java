package Cell;

import java.awt.Point;

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
	
	public boolean isNearFrom(ObjectMap o)
	{
		Point p1 = new Point((int)x, (int)y);
		Point p2 = new Point((int)o.x, (int)o.y);
		
		return p1.equals(p2);
	}
	
	public void moveTo(double x, double y)
	{
		this.x=x;
		this.y=y;
	}
}
