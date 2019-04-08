package Group;
import Parameters.Parms;
import Cell.*;

import Animal.Animal;
import Interfaces.I_Living;

public abstract class Group implements I_Living
{
	private final int type;
	
	public Group(int type)
	{
		this.type = type;
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
		if(p.getChief()==null)
			return false;
		
		return p.getChief().getGroup()!=null;
	}
	
	public abstract double getNeedsToEat();	
	public abstract int getSize();
	public abstract void setDeath(Animal a);
	
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
