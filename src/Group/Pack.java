package Group;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;
import Cell.*;
import Parameters.*;
import Main.*;

public class Pack extends Group
{
	public Animal chief;
	public Family chiefFamily;
	public ArrayList<Group> others;
	
	/* Une meute part chasser en groupe avec tout le monde mais seul les "pères" de chaque famille paritcipent au combat
	 * Cela se justifie par le fait que les enfant naissent à un état assez developpé, donc qu'il apprend à chasser avec les autres
	 */
	
	public Pack()
	{
		super();
		chief = null;
		chiefFamily = null;
		others = new ArrayList<Group>();
	}
	
	public Pack(Animal a) throws IllegalArgumentException
	{
		//a single animal is consider as a pack of one animal
		this();
		if(!a.isCarnivorous())
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
		
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
				count+=o.getSize();//return only the size of the father if the family is not together
			}
			else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
			}
		}
		
		if(chiefFamily!=null)
		{
			count+=chiefFamily.getSize();
		}else if(chief!=null)
		{
			count++;
		}
		
		return count;
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
	public void add(Group o) throws IllegalArgumentException
	{
		if(!(o.isAnimal() || o.isFamily() || o.isPack()))
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
		
		if(o.isPack()) //merge two Pack
		{
		}else
		{
			o.setGroup(this);
			addMember(o);
		}
	}
	
	private void merge(Pack p)
	{
		//Animal temp = p.duel();
		
	}
	
	private void addMember(Group o)
	{
		if(o.isFamily())
		{
			Family f = (Family)o;
			Run.removeGroup(f);
		}else
		{
			Run.removeGroup(o);
		}
		
		double s1 = o.getStrength();
		double s2 = (chief==null)?-1:chief.getStrength();
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
				Animal a = ((Pack)f.getFather()).getChief();
				
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
				if(temp_a!=null)
					others.add(new Pack(temp_a));
			}else
			{
				others.add(temp_f);
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
		}else if(isInFamily(chiefFamily, o))
		{
			chiefFamily.setDeath(o);
		}else if(o.isFamily())
		{
			Family f = (Family)o;
			Animal father = ((Pack)f.getFather()).getChief();
			if(father!=null)
			{
				if(father==chief)
				{
					chiefFamily = null;
					setDeathForChief();//chief's family will be added to the list of members
				}else
				{
					setDeathForMember(o);
				}
			}
		}
		
		if(getSize()==0)
			Run.removeGroup(this);
			
	}
	
	
	public void setDeath()
	{
		ArrayList<Group> temp = new ArrayList<Group>(others);
		int size = temp.size();
		
		for(int i=size-1; i>=0; i--)
		{
			setDeath(temp.get(i));
		}
		
		if(chiefFamily!=null)
		{
			chiefFamily.setDeath();
		}else if(chief!=null)
		{
			chief = null;
		}
		
		Run.removeGroup(this);
	}
	
	private void setDeathRandom()
	{
		int index = -1 + (int)(Math.random()*(others.size()+1));//-1 is for the chief
		if(index==-1)
		{
			/* setDeathRandom could kill the father of the family which is a 
			 * problem when the chief of the family is also the chief of the current pack
			 * To solve that problem we deal with the "father" and the "mother" of the family
			 * without having to call setDeathRandom() for the family
			 */
			if(chiefFamily!=null && 0!=(int)(Math.random()*chiefFamily.getSize()))
			{
				chiefFamily.setDeath(1);
			}else
			{
				setDeathForChief();
			}
			return;
		}
		
		Group o = others.get(index);
		if(o.isFamily())
		{
			o.setDeath(1);
			return;
		}
		
		setDeath(o);
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
	
	private int getNewChief()
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
		return index;
	}
	
	private void setDeathForChief() throws IllegalArgumentException
	{
		if(chiefFamily!=null)
		{
			chiefFamily.setDeath(chiefFamily.getFather());
			if(chiefFamily.getSize()>2)
			{
				/*If the family is composed by two members only, then we split the family 
				 * and add each parent into the list of animal of the pack
				 * Else, the family remain alive and we have to add the whole family as a member of 
				 * the pack
				 */
				others.add(chiefFamily);
			}
		}
		chief = null;
		chiefFamily=null;
		
		if(others.size()>0)//set the new chief as the animal with the highest strength
		{
			
			int index = getNewChief();
			Group o = others.get(index);
			
			//set the new chief
			
			if(o.isFamily())
			{
				Family f = (Family)o;
				Animal a = ((Pack)f.getFather()).getChief();
				
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
		if(group==null)
			return;
		
		for(Group o : others)
		{
			if(o==group)
			{
				if(o.isFamily())
				{
					o.setDeath();
					return;
				}else if(o.isAnimal())
				{
					others.remove(o);
					return;
				}
			}else if(o.isFamily() && isInFamily((Family)o, group))//only kill a member of the family
			{	
				o.setDeath(group);
				return;
			}
		}
	}
	
	
	public void splitFamily(Family f)
	{
		//remove family without killing animals in it
		Pack p = (Pack)f.getFather();
		if(p!=null)
		{
			Animal a = p.getChief();
			if(a!=chief)
			{
					p.setGroup(this);
					others.add(p);
			}
		}
		
		p = (Pack)f.getMother();
		if(p!=null)
		{
			p.setGroup(this);
			others.add(p);
		}
		
		others.remove(f);
	}
	
	//implement
	public double getStrength() throws IllegalArgumentException
	{
		double res = (chiefFamily!=null)?(chiefFamily.getStrength()):chief.getStrength();
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
			if(o.isAnimal())
			{
				res+=o.getAgility();
			}
			else if(o.isFamily())
			{
				res+=((Family)o).getFather().getAgility();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return res/( ((chief!=null)?1:0) + others.size() );
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
	
	
	public void age()
	{
		if(chiefFamily!=null)
		{
			chiefFamily.age();
		}else if(chief!=null)
		{
			chief.age();
		}
		
		for(Group o : others)
		{
			o.age();
		}
	}
	
	public boolean needToEat()
	{
		if(chiefFamily!=null)
		{
			return chiefFamily.needToEat();
		}
		return chief.needToEat();
	}
	
	public double getNeedsToEat() throws IllegalArgumentException
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		
		if(chiefFamily!=null)
		{
			output+=chiefFamily.getNeedsToEat();
		}else if(chief!=null)
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
		
		return output;
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	
	public boolean isCarnivorous()
	{
		return true;
	}
	
	public boolean isHerbivorous()
	{
		return false;
	}
	
	public void comeBack(Cell map[][])
	{
		
	}
	
	
	public void findFood(Cell[][] map)
	{
		
		double angle = Parms.getDirectionForAnimal(map, chief);

		eat(map, angle);
	}
	
	public void eat(Cell[][] map, double angle)
	{
		if(chiefFamily!=null)
		{
			chiefFamily.eat(map, angle);
		}else if(chief!=null)
		{
			chief.eat(map, angle);
		}
		
		for(Group o : others)
		{
			if(o.isAnimal())
			{
				Animal a = ((Pack)o).getChief();
				a.eat(map, angle);
			}else if(o.isFamily())
			{
				Family f = (Family)o;
				f.eat(map, angle);
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
				
			}
		}
		
		return false;
	}
	
	public boolean _fight(Herd o)
	{
		
		return false;
	}
	
	public String toString()
	{
		if(isAnimal())
			return chief.toString();
		
		String res="Meute : \n";
		res+="\tChef : " + ((chief!=null)?chief.toString():"");
		res+="\n\tMembres : ";
		for(Group o : others)
		{
			res+="\n\t\t";
			if(o.isAnimal())
			{
				Pack p = (Pack)o;
				res+= p.getChief().toString();
			}else
			{
				res+= o.toString();
			}
		}
		return res;
	}
	
	
}
