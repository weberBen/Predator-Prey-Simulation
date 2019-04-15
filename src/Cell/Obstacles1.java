package Cell;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Obstacles1 extends ObjectMap
{
	private double rigidity;
	private double height;
	private double widthEllipse;
	private double heightEllipse;
	private double angle;//in radian
	
	public static final double MAX_HEIGHT_ELLIPSE = 10;
	public static final double MAX_WIDTH_ELLIPSE = 10;
	
	public Obstacles1(double x, double y, double rigidity, double height, double widthEllipse, double heightEllipse, double rotation)
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
	
	private Point2D rotation(Point2D p1, double _angle)
	{
		Point2D p = new Point2D.Double();
		double cos = Math.cos(_angle);
		double sin = Math.sin(_angle);
		
		double x = cos*p1.getX() - sin*p1.getY();
		double y = sin*p1.getX() + cos*p1.getY();
		
		p.setLocation(x,y);
		
		return p;
	}
	
	private Point2D translation(Point2D p1, Point2D vector)
	{
		Point2D p = new Point2D.Double();
		double x = getX() + vector.getX();
		double y = getY() + vector.getY();
		
		p.setLocation(x,y);
		
		return p;
	}
	
	
	private Point2D transformationForEllipse(Point2D p1)
	{
		Point2D vetor_translation = new Point2D.Double(-this.getX(), -this.getY());//center of the ellipse
		return translation(rotation(p1, -1*angle), vetor_translation);
	}
	
	private boolean intersectionLineCenterEllipse(Point2D start, Point2D end)
	{
		//equation of the line (y=ax+b)
		double a = (start.getY()-end.getY())/(start.getX()-end.getX());
		double b = start.getY()-a*start.getX();
		
		//a2*x^2 + a1*x + a0 = 0
		double a0 = Math.pow(b/heightEllipse,2)-1;
		double a1 = 2*a*b/Math.pow(widthEllipse, 2);
		double a2 = (1/Math.pow(widthEllipse, 2)) + Math.pow(a/heightEllipse, 2);
		
		double delta = a1*a1 - 4*a2*a0;
		if(delta<0)
			return false;
		
		return true;
	}
	
	public boolean intersect(Point2D start, Point2D end)
	{
		//check if the line from start to end intersect the obstacle
		
		//equation of the line (y=ax+b)
		start = transformationForEllipse(start);
		end = transformationForEllipse(end);
		
		return intersectionLineCenterEllipse(start, end);
		
	}
	
}
