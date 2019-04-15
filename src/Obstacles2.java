

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Cell.ObjectMap;
import Parameters.*;

public class Obstacles2 extends ObjectMap
{
	private double rigidity;
	private double height;
	private Shape shape;
	
	private double widthRectangle;
	private double heightRectangle;
	private double angleRectangle;
	
	public final static double MAX_HEIGHT_OBSTACLE_BOUNDS = 10;
	public final static double MAX_WIDTH_OBSTACLE_BOUNDS = 10;
	
	private Obstacles2(){}
	
	public Obstacles2(double x, double y, double w, double h, double angle, double rigidity, double height)
	{
		super(x, y);
		this.rigidity = rigidity;
		this.height = height;
		this.widthRectangle = w;
		this.heightRectangle = h;
		this.angleRectangle = Math.toRadians(angle);
		//create the shape (rotated rectangle)
	    this.shape = createShape(x, y, w, h, angle);
	}
	
	private Shape createShape(double x, double y, double w, double h, double angle)
	{
		AffineTransform at = new AffineTransform();
		Rectangle2D rect = new Rectangle2D.Double(x, y, w, h);
		at.rotate(Math.toRadians(angle), rect.getWidth() / 2, rect.getHeight() / 2);
	    return at.createTransformedShape(rect);
	}
	
	public Obstacles2(double x, double y, double w, double h, double angle)
	{
		this(x, y, w, h, angle, Math.random(), Math.random()*Parms.MAX_HEIGHT_OBSTACLE);
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
	
	public double getHeight2DShape()
	{
		return heightRectangle;
	}
	
	public double getWidth2DShape()
	{
		return widthRectangle;
	}
	
	public double getAngle2DShape()
	{
		return angleRectangle;
	}
	
	public boolean contains(double x, double y)
	{
		return shape.contains(x, y);
	}
	
	
	public boolean lineIntersectShape(Line2D l) 
	{
		//https://www.developpez.net/forums/d867605/java/interfaces-graphiques-java/graphisme/2d/intersection-line2d-shape-voire-polygon/
		boolean intersecte = false;
		double[] segment = new double[2]; //Tableau qui contiendra les coordonnées d'un des points du segment
		Point2D p1 = new Point2D.Double(); //Premier point du segment
		Point2D p2 = new Point2D.Double(); //Deuxième point du segment
		PathIterator pi = shape.getPathIterator(null); //Le PathIterator pour parcourir les segments du Shape
	 
		//On initialise le premier premier segment ainsi que chaque point du segment
		pi.currentSegment(segment);
		p1 = new Point2D.Double(segment[0], segment[1]);
		pi.next();
		pi.currentSegment(segment);
		p2 = new Point2D.Double(segment[0], segment[1]);
	 
		//Tant qu'on a pas parcouru tous les points et qu'il n'y a pas d'intersection
		while (!pi.isDone() && !intersecte) {        		
			//Line2D représentant le segment actuel
			Line2D l2 = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			//Si la ligne intersecte avec le segment 
			if (l.intersectsLine(l2))
				intersecte = true;
			//Sinon on passe au segment suivant
			else {
				//Premier point du segment
				p1 = new Point2D.Double(segment[0], segment[1]);
				pi.next();
				pi.currentSegment(segment);
				//Deuxieme point du segment		
				p2 = new Point2D.Double(segment[0], segment[1]);
			}
		}
		return intersecte;
	}
	
	public Shape transform(AffineTransform at)
	{
		return at.createTransformedShape(shape);
	}
	
	public Shape resize(double newX, double newY, double newW, double newH)
	{
		return createShape(newX, newY, newW, newH, angleRectangle);
	}
}
