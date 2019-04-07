package Cell;
import java.util.ArrayList;

import Animal.Animal;
import Group.Group;
import Group.Pack;
import Parameters.Parms;

public abstract class Cell extends ObjectMap
{
	protected double rigidity;
	protected double height;
	protected double length;
	protected ArrayList<Group> animals;
	
	public Cell(double rigidity, double height, double length)
	{
		this.rigidity = rigidity;
		this.height = height;
		this.length = length;
		this.animals = new ArrayList<Group> ();
	}
	public Cell()
	{
		this(0,0,0);
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
	
	
	public void add(Group o)
	{
		animals.add(o);
	}
	
	public void remove(Group o)
	{
		animals.remove(o);
	}
	
	public boolean canBeCircled(Group o)
	{
		if(length<Parms.DIM_CELL)
		{
			return true;
		}else if(rigidity<o.getStrength() && o.getAgressivity()>0.5)
		{
			return true;
		}else if(o.getAgility()>0.5)
		{
			return true;
		}else if(o instanceof Pack)
		{
			Pack p =(Pack)o;
			Animal a = p.getChief();
			if(a.getHeight()*a.getStrength()>=height)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean eatable(Group o)
	{
		return o.isHerbivorous();
	}
	
}
