package Cell;

import java.awt.Point;

public class Obstacle extends ObjectMap
{
	private double rigidity;
	private double height;
	private double widthEllipse;
	private double heightEllipse;
	private double angle;//in radian
	
	public static final double MAX_HEIGHT_ELLIPSE = 10;
	public static final double MAX_WIDTH_ELLIPSE = 10;
	
	public Obstacle(double x, double y, double rigidity, double height, double Ellipse, double heightEllipse, double rotation)
	{
		super(x, y);//center of the eclipse (absolute position relative to the map)
		this.rigidity = rigidity;
		this.height = height;
		//parms of the eclipse
		this.widthEllipse = widthEllipse;
		this.heightEllipse = heightEllipse;
		this.angle = rotation;
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
	
	public double getWidthEllipse()
	{
		return widthEllipse;
	}
	
	public void setWidthEllipse(double a)
	{
		this.widthEllipse=a;
	}
	
	public double getHeightEllipse()
	{
		return heightEllipse;
	}
	
	public void setHeightEllipse(double b)
	{
		this.heightEllipse=b;
	}
	
	public double getRotation()
	{
		return angle;
	}
	
	public void setRotation(double angle)
	{
		this.angle=angle;
	}
	
	public boolean contains(double x, double y)
	{
		//check if the point of coordinates (x,y) is inside the obstacle (the eclipse)
		
		x = x-this.x; //(x-xc) where xc est the X-coordinate of the center
		y = y-this.y; //(y-yc) where yc est the Y-coordinate of the center
		
		double p1 = x*Math.cos(angle) + y*Math.sin(angle);
		p1/=widthEllipse;
		double p2 = x*Math.sin(angle) - y*Math.cos(angle);
		p2/=heightEllipse;
		
		return (p1*p1 + p2*p2)<=1;
	}
	
	public Point intersect(Point start, Point end)
	{
		//check if the line from start to end intersect the obstacle
		
		//equation of the line (y=ax+b)
		double a = (start.getY()-end.getY())/(start.getX()-end.getX());
		double b = start.getY()-a*start.getX();
		
		double x, y;
		
		return null;
	}
	
}
