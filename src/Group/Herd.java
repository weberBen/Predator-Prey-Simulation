package Group;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;
import Cell.*;

public class Herd extends Group
{
	ArrayList<Animal> members;
	//all the animal will move together (no need to know with which group the animal is linked with)
	/*Une famille d'herbivore est une horde*/
	
	public Herd()
	{
		super(Parms.TYPE_HERD);
		members = new ArrayList<Animal>();
	}
	
	public void addMember(Animal a)
	{
		members.add(a);
	}
	
	public void merge(Herd herd)
	{
		for(Animal a : herd.members)
		{
			members.add(a);
		}
	}
	
	public void setDeath(Group o)
	{
		if(o!=null && o.isAnimal())
		{
			Pack p = (Pack)o;
			Animal a = p.getChief();
			setDeath(a);
		}
	}
	
	public void setDeath(Animal a)
	{
		members.remove(a);
	}
	
	public void setDeath(int number)
	{
		if(number>getSize())
		{
			
		}
		
		for(int i=0; i<number; i++)
		{
			if(i<getSize())
			{
				
			}
		}
	}
	
	public int getSize()
	{
		return members.size();
	}
	
	//implement interface
	public double getStrength()
	{
		double res = 0;
		for(Animal o : members)
		{
			res+=o.getStrength();
		}
		
		return res*getSociability();
	}
	
	public double getAgility()
	{
		double res = 0;
		
		for(Animal o : members)
		{
			res+=o.getAgility();
		}
		
		return (res/members.size());
	}
	
	
	public double getAgressivity()
	{
		double res = 0;
		
		for(Animal o : members)
		{
			res+=o.getAgressivity();
		}
		
		return (res/members.size());
	}
	
	public double getSociability()
	{
		return Parms.sociabilityHerd(getSize());
	}
	
	
	public String getSpecie()
	{
		String specie = null;
		if(members.size()>0)
		{
			specie = members.get(0).getSpecie();
			for(Animal o: members)
			{
				if(!specie.equals(o.getSpecie()))
				{
					return null;
				}
			}
		}
		
		return specie;
	}
	
	public void age()
	{
		for(Animal o : members)
		{
			o.age();
		}
	}
	
	public boolean needToEat()
	{
		return getNeedsToEat()!=0;
	}
	
	public double getNeedsToEat()
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		
		for(Animal o : members)
		{
			if(o.needToEat())
			{
				output+=o.getNeedsToEat();//if the group does not need to eat return 0
			}
		}
		
		return (output<0)?Math.abs(output):0;
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	
	public boolean isCarnivorous()
	{
		return false;
	}
	
	public boolean isHerbivorous()
	{
		return true;
	}
	
	
	public void findFood(Cell[][] map)
	{
		double angle = 0;
		
		for(Animal o : members)
		{
			angle+=Parms.getDirectionForAnimal(map, o);
		}
		angle/=getSize();
		setDir(angle);
		
		for(Animal o :members)
		{
			o.setCanEat(true);
			o.move(map, angle);
			o.setCanEat(false);
		}
	}
	
	public void setDeath()
	{
		members.clear();
	}
	
	public boolean _fight(Herd o)
	{
		//herd-->herd
		
		
		return false;
	}

	
	public boolean _fight(Pack o)
	{
		//herd-->pack
		
		return false;
	}
	
	public String toString()
	{
		String res = "Troupeau :\n";
		for(Animal a : members)
			res+="\t"+a.toString()+"\n";
		
		return res;
	}

}
