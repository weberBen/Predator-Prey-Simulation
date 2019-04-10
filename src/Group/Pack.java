package Group;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;
import Cell.*;
import Parameters.*;

public class Pack extends Group
{
	private Animal chief;
	private Family chiefFamily;
	private ArrayList<Group> others;
	
	public Pack()
	{
		super(Parms.TYPE_PACK);
		chief = null;
		chiefFamily = null;
		others = new ArrayList<Group>();
	}
	
	public Pack(Animal a)//a single animal is consider as a pack of one animal
	{
		this();
		chief = a;
		chiefFamily = null;
	}
	
	public Pack(Pack p)//a single animal is consider as a pack of one animal
	{
		this();
		setChief(p);
	}
	
	public int getSize() throws IllegalArgumentException
	{
		int count = 0;
		for(Group o : others)
		{
			//only single animal and family can be a member of the pack
			if(o.isAnimal() || o.isFamily())
			{
				count+=o.getSize();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
			}
		}
		
		return (chief!=null)?1:0 +count;
	}
	
	
	//chief
	public void setChief(Pack o)
	{
		chief = o.getChief();
		if(o.isFamilyMember())
			chiefFamily = (Family)o.getGroup();
	}
	
	public Animal getChief()
	{
		return chief;
	}
	
	public Family getChiefFamily()
	{
		return chiefFamily;
	}
	
	
	//the others
	public void addMember(Animal a)
	{
		Pack p = new Pack(a);
		addMember(p);
		p.setGroup(this);
	}
	
	public void addFamily(Family f)
	{
		addMember(f);
		f.setGroup(this);
	}
	
	public void addMember(Group o) throws IllegalArgumentException
	{
		if(!(o.isAnimal() || o.isFamily()))
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
		
		double s1 = o.getStrength();
		double s2 = (chief==null)?0:chief.getStrength();
		boolean replaceChief = false;
		
		//if the new animal is stronger than the chief, then the old chief is replace
		if(s1>=s2)
		{
			if(s1==s2)//if the two animals have the same strength, the the chief is replaced or not randomly
			{
				if(Math.random()<0.5)
				{
					replaceChief = true;
				}
			}else
			{
				replaceChief = true;
			}
		}
		
		if(replaceChief)
		{
			Animal temp_a = chief;
			Family temp_f = chiefFamily;
			
			//set new chief
			if(o.isFamily())
			{
				Family f = (Family)o;
				Animal a = f.getFather().getChief();
				
				chief = a;
				chiefFamily = f;
			}else if(o.isAnimal())
			{
				Pack p = (Pack)o;
				chief = p.getChief();
			}
			
			//remove old chief
			if(temp_f==null)
			{
				others.add(new Pack(temp_a));
			}else
			{
				others.add(chiefFamily);
			}
			
		}else
		{
			others.add(o);
		}
	}
	
	
	public void setDeath(Group o)
	{
		if(o==null)
			return;
		
		if(o.isAnimal())
		{
			Pack p = (Pack)o;
			if(p.getChief()==chief)
			{
				setDeathForChief();
			}else
			{
				setDeathForMember(o);
			}
		}else if(o.isFamily())
		{
			Family f = (Family)o;
			Pack father = f.getFather();
			if(father!=null)
			{
				if(father.getChief()==chief)
				{
					setDeathForChief();
				}else
				{
					setDeathForMember(o);
				}
			}
		}
	}
	
	
	public void setDeath()
	{
		int size = others.size();
		for(int i=size-1; i>=0; i++)
		{
			setDeath(others.get(i));
		}
		
		
		
	}
	
	public void setDeath(int number)
	{
		if(number>getSize())
		{
			int size = others.size();
			Group o;
			for(int i=size-1; i>=0; i--)
			{
				o = others.get(i);
				if(o.isAnimal())
				{
					others.remove(i);
				}else if(o.isFamily())
				{
					
				}
			}
			
			setDeathForChief();//need to be after the loop of the group (to set the new chief)
		}
	}
	
	private void setDeathForChief() throws IllegalArgumentException
	{
		chief = null;
		if(others.size()>0)//set the new chief as the animal with the highest strength
		{
			//fin the animal with the highest strength
			int index = 0;
			double max_strength = -1;
			double temp;
			Group o;
			
			for(int i=0; i<others.size(); i++)
			{
				o = others.get(i);
				if(o.isAnimal() || o.isFamily())
				{
					if((temp = o.getStrength())>max_strength)
					{
						index = i;
						max_strength = temp;
					}
				}else
				{
					throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
				}
			}
			
			
			//set the new chief
			chief=null;
			others.add(chiefFamily);
			chiefFamily=null;
			
			o = others.get(index);
			if(o.isFamily())
			{
				Family f = (Family)o;
				Animal a = f.getFather().getChief();
				
				chief = a;
				chiefFamily = f;
			}else if(o.isAnimal())
			{
				Pack p = (Pack)o;
				chief = p.getChief();
			}
			others.remove(index);
		}
	}
	
	public void clear()
	{
		chief = null;
		if(chiefFamily!=null)
		{
			chiefFamily.setGroup(null);
			chiefFamily=null;
		}
		
		others.clear();
	}
	
	private void setDeathForMember(Group group)
	{
		for(Group o : others)
		{
			if(o==group)
			{
				if(o.isFamily())
				{
					o.setDeath();
				}else if(o.isAnimal())
				{
					others.remove(o);
				}
			}
		}
	}
	
	
	public void splitFamily(Family f)
	{
		//remove family without killing animals in it
		Pack g = f.getFather();
		if(g!=null)
		{
			Animal father = f.getFather().getChief();
			if(father!=chief)
			{
					others.add(g);
			}
		}
		
		g = f.getMother();
		if(g!=null)
			others.add(g);
		
		others.remove(f);
	}
	
	//implement
	public double getStrength() throws IllegalArgumentException
	{
		double res = (chief!=null)?(chief.getStrength()):0;
		for(Group o : others)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getStrength();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return res;
	}
	
	public double getAgility() throws IllegalArgumentException
	{
		double res = (chief!=null)?(chief.getAgility()):0;
		
		for(Group o : others)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getAgility();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return res/( ((chief!=null)?1:0) + others.size());
	}
	
	
	public double getAgressivity() throws IllegalArgumentException
	{
		return chief.getAgressivity();
	}
	
	public double getSociability()
	{
		return Parms.sociabilityPack(getSize());
	}
	
	
	public String getSpecie() throws IllegalArgumentException
	{
		String specie = null;
		if(others.size()>0)
		{
			specie = others.get(0).getSpecie();
			for(Group o : others)
			{
				if(o.isFamily() || o.isAnimal())
				{
					if(!specie.equals(o.getSpecie()))
					{
						return null;
					}
				}else
				{
					throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
				}
			}
		}
		
		if(chief!=null)
		{
			if(specie==null)//the pack is only composed by one animal
			{
				return chief.getSpecie();
			}else if(!specie.equals(chief.getSpecie()))
			{
				return null;
			}
		}
		
		return null;
	}
	
	
	public void decreaseEnergy(double ep)
	{
		if(chief!=null)
		{
			chief.decreaseEnergy(ep);
		}
		
		for(Group o : others)
		{
			o.decreaseEnergy(ep);
		}
	}
	
	public boolean needToEat()
	{
		return chief.needToEat();
	}
	
	public double getNeedsToEat() throws IllegalArgumentException
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		if(chief.needToEat())
		{
			output+=chief.getNeedsToEat();
		}
		
		
		for(Group o : others)
		{
			if(o.isFamily() || o.isAnimal())
			{
				output+=o.getNeedsToEat();//if the group does not need to eat return 0
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return Math.abs(output);
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	
	public boolean isCarnivorous()
	{
		return chief.isCarnivorous();
	}
	
	public boolean isHerbivorous()
	{
		return chief.isHerbivorous();
	}
	
	public void findFood(Cell[][] map)
	{
		double angle = Parms.getDirectionForAnimal(map, chief);
		
		for(Group o : others)
		{
			if(o.isAnimal())
			{
				Animal a = ((Pack)o).getChief();
				a.move(Math.cos(angle)*Parms.DELTA_MOVE, Math.sin(angle)*Parms.DELTA_MOVE);
				a.eat(map);	
			}else if(o.isFamily())
			{
				
			}
		}
	}
	
	public boolean _fight(Pack o)
	{
		if(o.isAnimal())
		{
			if(o.isFamilyMember())
			{
				
			}else//lonely animal
			{
				Animal a = (Animal)o.getChief();
				a.
			}
		}
		
		return false;
	}
	
	public boolean _fight(Herd o)
	{
		
		return false;
	}
	
	
}
