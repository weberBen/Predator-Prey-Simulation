package Cell;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Parameters.*;

public class Obstacle extends ObjectMap
{
	private double rigidity;
	private double height;
	
	private double radius;
	private Shape shape;
	
	public final static double MAX_RADIUS_OBSTACLE = 10;
	
	private Obstacle(){}
	
	public Obstacle(double x, double y, double radius, double rigidity, double height)
	{
		super(x, y);
		this.rigidity = rigidity;
		this.height = height;
		this.radius = radius;
		this.shape = new Ellipse2D.Double(x, y, radius, radius);
	}
	
	public Obstacle(double x, double y, double radius)
	{
		this(x, y,radius, Math.random(), Math.random()*Parms.MAX_HEIGHT_OBSTACLE);
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
	
	public double getRadius2DShape()
	{
		return radius;
	}
	
	public boolean contains(double x, double y)
	{
		return (Math.pow((x-this.x), 2) + Math.pow((y-this.y), 2)<=1);
	}
	
	public boolean intersect(Line2D line)
	{
		return intersect(line.getP1(), line.getP2());
	}
	
	
	public boolean intersect(Point2D pointA, Point2D pointB) 
	{
		//https://stackoverflow.com/questions/13053061/circle-line-intersection-points
			
        double baX = pointB.getX() - pointA.getX();
        double baY = pointB.getY() - pointA.getY();
        double caX = this.x - pointA.getX();
        double caY = this.y - pointA.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return false;
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Point2D p1 = new Point2D.Double(pointA.getX() - baX * abScalingFactor1, pointA.getY() - baY * abScalingFactor1);
        if (disc == 0) 
        { // abScalingFactor1 == abScalingFactor2
            return true;
        }
        
        Point2D p2 = new Point2D.Double(pointA.getX() - baX * abScalingFactor2, pointA.getY() - baY * abScalingFactor2);
        return true;
    }
	
	public Shape getShape()
	{
		return shape;
	}
}
