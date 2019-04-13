package Group;
import Parameters.Parms;
import Cell.*;

import Animal.Animal;
import Interfaces.I_Living;

public abstract class Group implements I_Living
{
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
	
	public Group()
	{
		group =  null;
	}
	
	public double getDir()
	{
		return dir;
	}
	
	public void setDir(double dir)
	{
		this.dir = dir;
	}
	
	public boolean isFamily()
	{
		return this instanceof Family;
	}
	
	public boolean isInFamily(Family f, Group p)
	{
		if(f==null || p==null)
			return false;
		if(!p.isFamilyMember())
			return false;
		
		return p.getGroup()==f;
	}
	
	public boolean isHerd()
	{
		return this instanceof Herd;
	}
	
	public boolean isPack()
	{
		return this instanceof Pack;
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
	public abstract void setDeathRandom();
	public abstract void setDeath(Group o);
	
	
	public void setDeath(int number)
	{
		if(number>getSize())
		{
			setDeath();
			return;
		}
		
		for(int i=0; i<number; i++)
		{
			setDeathRandom();
		}
	}
	
	
	public void interact(Group o)
	{
		
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
	public abstract void comeBack(Cell[][] map);
	
	public void move(Cell[][] map)
	{
		if(needToEat())
		{
			findFood(map);
		}else
		{
			comeBack(map);
		}
	}
	
}
