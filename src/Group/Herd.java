package Group;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;
import Cell.*;
import Main.*;

public class Herd extends Group
{
	ArrayList<Animal> members;
	//all the animal will move together (no need to know with which group the animal is linked with)
	/*Une famille d'herbivore est une horde*/
	
	public Herd()
	{
		super();
		members = new ArrayList<Animal>();
	}
	
	public Herd(Animal a) throws IllegalArgumentException
	{
		//an hebivorous is considered as a herd of one animal
		this();
		if(!a.isHerbivorous())
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
		
		addAnimal(a);
	}
	
	public Animal _getAnimal()
	{
		if(members.size()!=1)
			return null;
		
		return members.get(0);
	}
	
	public void add(Group o) throws IllegalArgumentException 
	{
		if(o.isHerd())
		{
			addHerd((Herd)o);
		}else if(o.isFamily())
		{
			addFamily((Family)o);
		}else
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
	}
	
	private void addAnimal(Animal a)
	{
		members.add(a);
	}
	
	private void addFamily(Family f)
	{
		members.add(((Herd)f.getFather()).getAnimal());
		members.add(((Herd)f.getMother()).getAnimal());
		members.addAll(f.getChildren());
		Run.removeGroup(f);
	}
	
	private void addHerd(Herd herd)
	{
		members.addAll(herd.members);
		Run.removeGroup(herd);
	}
	
	public void setDeath()
	{
		members.clear();
		Run.removeGroup(this);
	}
	
	public void setDeath(Group o)
	{
		if(o!=null && o.isHerd())
		{
			Herd h = (Herd)o;
			Animal a = h.members.get(0);
			setDeath(a);
		}
	}
	
	public void setDeath(Animal a)
	{
		members.remove(a);
		if(members.size()==0)
		{
			Run.removeGroup(this);
		}
	}
	
	private void setDeathRandom()
	{
		int index = (int)(Math.random()*members.size());
		setDeath(members.get(index));
	}
	
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
	
	public void comeBack(Cell[][] map){
		//les troupeaux se déplacent toujours ensembles et sont nomades
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
