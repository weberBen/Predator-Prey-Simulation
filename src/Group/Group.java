package Group;
import Parameters.Parms;
import Cell.*;

import Animal.Animal;
import Interfaces.I_Living;

public abstract class Group implements I_Living
{
	private final int type;
	private double dir;
	private Group group;
	
	public void setGroup(Group g)
	{
		group = g;
	}
	
	public Group getGroup()
	{
		return group;
	}
	
	public Group(int type)
	{
		this.type = type;
		this.group =  null;
	}
	
	public double getDir()
	{
		return dir;
	}
	
	public void setDir(double dir)
	{
		this.dir = dir;
	}
	
	public int getType()
	{
		return type;
	}
	
	public boolean isFamily()
	{
		return (type==Parms.TYPE_FAMILY);
	}
	
	public boolean isHerd()
	{
		return (type==Parms.TYPE_HERD);
	}
	
	public boolean isPack()
	{
		return (type==Parms.TYPE_PACK);
	}
	
	public boolean isAnimal()
	{
		return isPack() && getSize()==1;
	}
	
	
	public boolean isFamilyMember()
	{
		if(!isPack())
			return false;
		
		Pack p = (Pack)this;
		if(p.getSize()!=1)
			return false;
		
		Group o = p.getGroup();
		return (o instanceof Family);
	}
	
	public abstract double getNeedsToEat();	
	public abstract int getSize();
	public abstract void setDeath();
	public abstract void setDeath(Animal a);
	public abstract void setDeath(int number);
	
	public void interact(Group o)
	{
		
		if(this.isFamilyMember())//père ou mère par exemple
		{
			if(o.isFamilyMember())
			{
				
			}else if(o.isAnimal())
			{
				
			}else if(o.isPack())
			{
				
			}else if(o.isHerd())
			{
				
			}
		}else if(o.isAnimal())
		{
			
		}
	}
	
	public boolean fight(Group o)
	{
		if(o instanceof Herd)
			return _fight((Herd)o);
		else if(o instanceof Pack)
			return _fight((Pack)o);
		
		return false;
	}
	
	protected abstract boolean _fight(Pack o);
	protected abstract boolean _fight(Herd o);
	
	
	public abstract void findFood(Cell[][] map);
	public abstract void setCanEat(boolean a);
	
	public void move(Cell[][] map)
	{
		if(needToEat())
		{
			findFood(map);
		}else if (false)
		{
			
		}
	}
	
}
