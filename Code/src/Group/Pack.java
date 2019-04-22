package Group;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;
import Cell.*;
import Parameters.*;
import Main.*;

/* A pack is only created on carnivorous animals. The pack is composed of a chief that will determine
 * all the action the group need to make and others members of the group.
 * Note that when the pack will hunt for food only the animal in charge of the food (the "father") inside families (that is part of 
 * the pack) will move with the rest of the pack. As the family stay together, the father will move with his family but only the father
 * will be exposed to injure and others animals. Thus, when we need to find a new chief we search inside the members of the pack
 * and if we get a family then only the father of the family could pretend to be the new chief.
 */


public class Pack extends Group
{
	/************************************************************************************************
	 * 
	 * 									ATTRIBUTES
	 * 
	 ************************************************************************************************/
	
	private Animal chief;
	private Family chiefFamily;//family of the chief if he has one
	private ArrayList<Group> others;
	
	private Pack()
	{
		super();
		chief = null;
		chiefFamily = null;
		others = new ArrayList<Group>();
	}
	
	public Pack(Animal a) throws IllegalArgumentException//create a single animal
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
	
	/************************************************************************************************
	 * 
	 * 									GETTER/SETTER
	 * 
	 ************************************************************************************************/
	
	
	public int getSize()
	{
		if(chief==null)
			return 0;
		
		int count = 0;
		for(Group o : others)
		{
			//only single animal and family can be a member of the pack
			count+=o.getSize();
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
	
	
	public void setChief(Group o)
	{
		if(o.isAnimal())
		{
			chief = o.getAnimal();
		}else if(o.isFamily())
		{
			Family f = (Family)o;
			Animal a = f.getFather().getAnimal();
			
			chief = a;
			chiefFamily = f;
			chiefFamily.setGroup(this);
		}
	}
	
	public Animal getChief()
	{
		return chief;
	}
	
	public Family getChiefFamily()
	{
		return chiefFamily;
	}
	
	
	private int getNewChief()
	{
		/* Based on the strength of each member find the one with the highest strength
		 * and return it position inside the list of members of the pack
		 */
		int index = 0;
		double max_strength = -1;
		double temp;
		Group o;
		
		for(int i=0; i<others.size(); i++)
		{
			o = others.get(i);
			if((temp = o.getStrength())>max_strength)
			{
				index = i;
				max_strength = temp;
			}
		}
		return index;
	}
	
	
	public double getStrength()
	{
		/* return the global strength of the pack (which is the sum of the strength of its members)*/
		double res = (chiefFamily!=null)?(chiefFamily.getStrength()):((chief==null)?0:chief.getStrength());
		for(Group o : others)
		{
				res+=o.getStrength();
		}
		
		return res;
	}
	
	public double getAgility()
	{
		/* return the global strength of the pack (which is the average of the agility of its members)*/
		double res = (chief!=null)?(chief.getAgility()):0;
		
		for(Group o : others)
		{
			res+=o.getAgility();
		}
		
		return res/( ((chief!=null)?1:0) + others.size() );
	}
	
	
	public double getAgressivity()
	{
		/* Because the chief make all the decision, agressivity of the
		 * pack only depends on the chief's agressivity.
		 */
		if(chief==null)
			return 0;
		return chief.getAgressivity();
	}
	
	public double getSociability()
	{
		/* The sociability of a group must decrease while its size (its number of members) increase*/
		return Parms.sociabilityPack(getSize());
	}
	
	
	public String getSpecie()
	{
		/* If all the members are part of the same specie then return that one
		 * else return unknown specie
		 */
		String specie = null;
		if(others.size()>0)
		{
			specie = others.get(0).getSpecie();
			for(Group o : others)
			{
				if(!specie.equals(o.getSpecie()))
				{
					return null;
				}
			}
		}
		
		if(chief!=null)
		{
			if(specie==null)//the pack is only composed by one animal
			{
				if(chief==null)
					return null;
				return chief.getSpecie();
			}else if(!specie.equals(chief.getSpecie()))
			{
				return null;
			}
		}
		
		return null;
	}
	
	
	public boolean isCarnivorous()
	{
		//a pack is only made of carnivorous animals
		return true;
	}
	
	public boolean isHerbivorous()
	{
		//a pack is only made of carnivorous animals
		return false;
	}
	
	/************************************************************************************************
	 * 
	 * 									ADD METHODS
	 * 
	 ************************************************************************************************/
	
	
	private void addToOthers(Animal a)//add animal as member of the pack
	{
		Pack p = new Pack(a);
		p.setGroup(this);//set the group as a part of the current one
		others.add(p);
	}
	
	private void addToOthers(Group o)//add a group (animal or family) as member of the pack
	{
		if(o==null)
			return;
		
		if(o.isFamily())
		{
			Family f = (Family)o;
			f.setGroup(this);
			others.add(f);
		}else
		{
			o.setGroup(this);
			others.add(o);
		}
	}
	
	//the others
	public void add(Group o) throws IllegalArgumentException//add a group to the pack
	{	
		if(o==null || o.getSize()==0)
			return;
		if(!(o.isCarnivorousAnimal() || o.isCarnivorousFamily()  || o.isPack()))
		{
			throw new IllegalArgumentException("Ajout d'un membre nom reconnu pour la meute\n");
		}
		if(!Parms.nearToInteract(this, o))
		{
			/* Group can only interact (especially merge) if there are close enough*/
			throw new IllegalArgumentException("Les deux groupes d'animaux doivent être proches pour intéragir\n"
					+ "\tPosition groupe 1 : "+this.getLocation()+"\n\tPosition groupe 2 : "+o.getLocation());
			/* If the group to add is not on the same cell that the current one, some troubles could appears 
			 * during the removal of the group to add on the map
			*/
		}
		if(o.isPack()&&!o.isCarnivorousAnimal()) //merge two Pack
		{
			merge((Pack)o);
		}else//single carnivorous animal of carnivorous family
		{
			o.setGroup(this);
			addMember(o);
		}
		World.addToLog("\t+ Assimilation dans "+ getFormatedInfo() +" de "+o);//write the add to the log of the simulation
	}
	
	private void merge(Pack o)
	{
		Animal chief1 = this.chief;
		Animal chief2 = o.chief;
		if (chief1.getStrength()<chief2.getStrength())
		{
			if (chief1.getAgility()<chief2.getAgility())
			{
				this.setDeathForChief();
			}
			else if(Math.random()<chief2.getStrength())
			{
				this.setDeathForChief();
			}else
				o.setDeathForChief();
		}else
		{
			if (chief1.getAgility()>chief2.getAgility())
			{
				o.setDeathForChief();
			}
			else if(Math.random()<chief1.getStrength())
			{
				o.setDeathForChief();
			}else
				this.setDeathForChief();
		}
		
		for(Group p : o.others)
		{
			this.others.add(p);
		}
		
		if(o.chiefFamily!=null)
		{
			this.addMember(o.chiefFamily);
		}else if (o.chief!=null)
		{
			this.addMember(World.createGroup(o.chief, o.getGroup()));
		}
		
		if(o.getGroup()==null)
			World.removeGroup(o);
		
	}
	
	private void addMember(Group o)//add a group to the pack
	{
		if(o==null)
			return ;
		
		/* To add a group we need to remove the reference of that group in the list of groups inside the main (the run
		 * of the simulation). Indeed all the living groups are registered into a single list inside the run of the simulation
		 * When we add a group to another we don't want to have the reference of the group to add inside the list of groups 
		 * because it will not be a stand alone group anymore, because it will be a part of an other group.
		 * Thus before doing anything, we need to remove the reference of the group to add
		 */
		if(o.isFamily())
		{
			Family f = (Family)o;
			if(f.getGroup()==null)
				World.removeGroup(f);
		}else
		{
			World.removeGroup(o);
		}
		
		/* Inside a pack, the chief is the animal with the highest strength of all pack's members
		 * Then before adding the group we need to check if it will be the new chief or not.
		 * If the group to add replace the new chief, then the chief is added as a simple members of the pack
		 */
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
		
		if(replaceChief)//need to replace the chief
		{
			Animal temp_a = chief;
			Family temp_f = chiefFamily;
			
			//set new chief
			setChief(o);
			
			//remove old chief
			/* If the old chief has a family, then he is already a part of that family, 
			 * then we cannot add the chief and the chief's family as a members of the pack. 
			 * We must add the chief's family and not the chief in that case.
			 */
			if(temp_f==null)//the old chief has no family
			{
				if(temp_a!=null)
					addToOthers(temp_a);
			}else
			{
				addToOthers(temp_f);
			}
			
		}else//the group to add does not replace the chief, then added as a simple members of the pack
		{
			addToOthers(o);
		}
	}
	
	
	private void newFamilies()
	{
		ArrayList<Pack> loners = new ArrayList<>();
		for (Group o : others)
		{
			if(o.isCarnivorousAnimal())
				loners.add((Pack)o);
		}
		if(loners.size()==0)
			return;
		
		Pack p1 = loners.get((int)(Math.random()*loners.size()));
		Pack p2 = loners.get((int)(Math.random()*loners.size()));
		if(p1.getAnimal().isMale()^p2.getAnimal().isMale())
		{
			others.remove(p1);
			others.remove(p2);
			Family f = new Family(p1,p2);
			f.setGroup(this);
			others.add(f);
		}
	}
	
	/************************************************************************************************
	 * 
	 * 									DEATH METHODS
	 * 
	 ************************************************************************************************/
	
	
	public double setDeath(Group o)
	{
		if(o==null)
			return 0;
		
		/* To kill a member of the pack some case may appears : 
		 *   - the group is the chief. Then we have to find a new chief and if the 
		 *   chief was a part of a family, then we need to notify that family that the father is dead
		 *   - the group is a part of a family, then when we will loop throught the members of the pack
		 *   we will need to check if the element is a family and, if so, if the group is a part of that family
		 *   to notify the family that one of its members is dead
		 *   - the group is a family or a single animal, then we just have to remove the group of the list of the members of the pack
		 */
		double res = 0;
		if(o.isAnimal())//a single animal
		{
			if(o.getAnimal()==chief)//the animal is the chief
			{
				res = setDeathForChief();
			}else//remove the members
			{
				res = setDeathForMember(o);
			}
		}else if(isInFamily(chiefFamily, o))//need to be after isAnimal()
		{
			res = chiefFamily.setDeath(o);
		}
		else if(o.isFamily())
		{
			Family f = (Family)o;
			Animal father = f.getFather().getAnimal();
			if(father!=null)
			{
				if(father==chief)
				{
					res = ((Pack)chiefFamily.getFather()).getChief().getEnergy()+((Pack)chiefFamily.getMother()).getChief().getEnergy();
					for (Animal a : chiefFamily.getChildren())
						res+=a.getEnergy();
					chiefFamily = null;
					setDeathForChief();//chief's family will be added to the list of members
				}else
				{
					res = setDeathForMember(o);
				}
			}
		}
		
		manageIntegrity();
		return res;
	}
	
	
	public double setDeath()
	{
		/* kill every animals inside the pack using the previous methods
		 * to kill animals
		 */
		ArrayList<Group> temp = new ArrayList<Group>(others);
		int size = temp.size();
		double res = 0;
		for(int i=size-1; i>=0; i--)
		{
			res+=setDeath(temp.get(i));
		}
		
		if(chiefFamily!=null)
		{
			res+=chiefFamily.setDeath();
		}else if(chief!=null)
		{
			res+=chief.getEnergy();
			chief = null;
		}
		
		World.removeGroup(this);//remove the group to the list of groups in the run of the simulation
		return res;
	}
	
	
	
	private double setDeathRandom()
	{
		/* Kill randomly a member of the pack */
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
				return chiefFamily.setDeath(1);
			}else
			{
				return setDeathForChief();
			}
		}
		/* If the random member to kill is a family then we need to randomly kill one
		 * element inside that family (and not the whole family)
		 */
		Group o = others.get(index);
		if(o.isFamily())//the selected member is a family
		{
			return o.setDeath(1);//kill randomly one element
		}
		
		return setDeath(o);
	}
	
	public double setDeath(int number)
	{
		if(number>getSize())//if we kill more than the number of animals inside the pack
		{
			return setDeath();
		}
		
		double res = 0;
		for(int i=0; i<number; i++)
		{
			res+=setDeathRandom();
		}
		return res;
	}
	
	
	private double setDeathForChief()
	{
		/* Before killing the chief we need to find a new chief 
		 * and if the chief is a member of a family we need to notify the family
		 * that the "father" die, then add that family as member of the pack only 
		 * if there is more than 2 members inside the family (because a family is defined
		 * by more than 2 members). Else, the family will split itself and add each of its element
		 * inside as a single member of the pack. Note that if the family is composed of an adult as 
		 * the "father" and a child as the "mother" (because the real mother die) then even if the child
		 * is too young it will be add as a single member.
		 */
		double res=0;
		if(chief!=null)
		{
			res = chief.getEnergy();
		}
		//kill the current chief
		if(chiefFamily!=null)//the chief has a family
		{
			chiefFamily.setDeath(chiefFamily.getFather());//notify that the father is dead
			
			if(chiefFamily.getSize()>2)//the family remain a family even after the death of the father
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
		
		if(others.size()>0)//if there is other members inside the pack find a new chief
		{
			
			int index = getNewChief();
			Group o = others.get(index);
			
			//set the new chief
			
			if(o.isFamily())
			{
				Family f = (Family)o;
				if(f.getFather()==null)
					return 0;
				Animal a = f.getFather().getAnimal();//only the "father" of a family can pretend to be the chief
				
				chief = a;
				chiefFamily = f;
			}else if(o.isAnimal())
			{
				chief = o.getAnimal();
			}
			others.remove(index);
		}
		return res;
	}
	
	
	private double setDeathForMember(Group group)
	{
		/* Kill a group that is part of the pack and which is not related to the chief (as family or obviously as chief)*/
		if(group==null)
			return 0;
		
		for(Group o : others)
		{
			if(o==group)
			{
				if(o.isFamily())//kill the whole family
				{
					/* By killing the whole family, it will remove itsefl from the current pack*/
					return o.setDeath();
				}else if(o.isAnimal())
				{
					others.remove(o);//just move the element from
					return ((Pack)o).getChief().getEnergy();
				}
			}else if(o.isFamily() && isInFamily((Family)o, group))//only kill a member of the family
			{	/* The animal to kill is a part of a family, then we need to notify the
				 * family that we need to kill one of its members. The family will update the pack itself
				*/
				o.setDeath(group);
				return ((Pack)group).getChief().getEnergy();
			}
		}
		return 0;
	}
	
	
	private boolean manageIntegrity()
	{
		/* Check if the pack is still a pack
		 * If there is no more animal in the pack, then it must disappear from the list of groups inside the run of the simulation*
		 * Else if the pack is only made of a family then we remove the pack and add the family as a stand alone group
		 */
		if(chief==null)
		{
			World.removeGroup(this);
			return true;
		}
		if(getSize()==0)//no more animals inside the pack
		{
			World.removeGroup(this);
			return true;
		}else if(chiefFamily!=null && others.size()==0)//there is only a family inside the pack
		{
			World.removeGroup(this);
			World.addGroup(chiefFamily);
			return true;
		}
		return false;
	}
	
	
	public void splitFamily(Family f)
	{
		/* That method is the only possible way for a family to modify the pack from itself.
		 * In fact when the family is not anymore a family then from itself the family
		 * will call that method (if the family is part of the pack) to remove the 
		 * reference of the family from the list inside the pack and to add new reference 
		 * (single animal) into that same list
		 */
		Pack p = (Pack)f.getFather();
		if(p!=null)//if there is a "father"
		{
			/* If the "father" is the current chief, then we just need to remove the family
			 * but not to add the chief as member of the pack
			 */
			Animal a = p.getChief();
			if(a!=chief)//the "father" is not the chief
			{
					p.setGroup(this);
					others.add(p);//add the "father" as a single animal to the pack
			}
		}
		
		p = (Pack)f.getMother();
		if(p!=null)//if there is a mother
		{
			p.setGroup(this);
			others.add(p);
		}
		
		others.remove(f);//remove the reference of the family inside the pack
	}
	
	private void checkDeath()
	{
		/* If an animal dies of old age, or dies because it energy was too low
		 * then we need to remove it from the pack.
		 */
		ArrayList<Group> temp = new ArrayList<Group>(others);
		for(Group o : temp)
		{
			if(o.isAnimal())
			{
				if(o.getAnimal().isDead())//check if the animal is dead
				{
					setDeath(o);
				}
			}//do nothing for a family
		}
		
		if(chief!=null && chief.isDead())
		{
			setDeathForChief();
		}
		
		manageIntegrity();
	}
	
	/************************************************************************************************
	 * 
	 * 									UPDATE METHODS
	 * 
	 ************************************************************************************************/
	
	
	public void age()
	{
		/* Based on the method age of Animal, only apply that method to each member of the pack*/
		if(chiefFamily!=null)
		{
			chiefFamily.age();
		}else if(chief!=null)
		{
			chief.age();
		}
		
		ArrayList<Group> temp = new ArrayList<Group>(others);
		for(Group o : temp)
		{
			o.age();
		}
		
		checkDeath();
	}
	
	
	public boolean interact(Group o)
	{
		if(o==null)
			return false;
		/*only positive social interractions
		 * interracts only whith carnivorous*/
		if(Math.min(this.getSociability(),o.getSociability())<0.7||o.isHerbivorous())
			return false;
		add(o);
		return true;
	}
	
	public void update(Cell[][] map)
	{
		/*deplacer
		 * chasser
		 * interragir
		 * vieillir
		 */
		if(acted)
			return;
		if(manageIntegrity())
			return;
		
		double angle = Parms.getDirectionForAnimal(map, this.getChief());
		moveTo(angle);
		
		Cell pos = World.getCell(location.getX(), location.getY());
		for(Group o : new ArrayList<Group>(pos.getGroups()))
		{
			if(o!=this)
				if(interact(o))
					break;
		}
		
		if(manageIntegrity())
			return;
		
		for(Group o : new ArrayList<Group>(pos.getGroups()))
		{
			if(o!=this)
				if(hunt(o))
					break;
		}
		age();
		newFamilies();
	}
	/************************************************************************************************
	 * 
	 * 									FOOD METHODS
	 * 
	 ************************************************************************************************/
	
	public boolean needToEat()
	{
		/* A pack can eat only when the chief need to eat*/
		if(chiefFamily!=null)
		{
			return chiefFamily.needToEat();
		}
		return chief.needToEat();
	}
	
	public double getNeedsToEat()
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
			output+=o.getNeedsToEat();//if the group does not need to eat return 0
		}
		
		return output;
	}
	
	public void findFood(Cell[][] map)
	{
		
		double angle = Parms.getDirectionForAnimal(map, chief);//the new direction of the group is the chief's one
		
		eat(map, angle);
	}
	
	public void eat(Cell[][] map, double angle)
	{
		/* Based on the method eat from Animal */
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
				Animal a = o.getAnimal();
				a.eat(map, angle);
			}else if(o.isFamily())
			{
				Family f = (Family)o;
				f.eat(map, angle);
			}
		}
	}
	
	
	public double setSatiety()
	{
		//the Pack is fully fed
		if(this.isCarnivorousAnimal())
		{
			if(chief!=null)
				return chief.setSatiety();
		}
		return 0;
	}
	
	public double feed(double dispo)
	{
		if (dispo>=this.getNeedsToEat())
		{
			return dispo-setSatiety();
		}
		chief.setEnergy(chief.getEnergy()+dispo);
		return 0;
	}
	
	/************************************************************************************************
	 * 
	 * 									LOCATION METHODS
	 * 
	 ************************************************************************************************/
	
	public Point2D getLocation()
	{
		/* The location of the group is the location of the chief*/
		if(chief==null)
			return null;
		
		return new Point2D.Double(chief.getX(), chief.getY());
	}
	

	public void moveTo(double x, double y)
	{
		Point2D old_pos = getLocation();
		
		if(chiefFamily!=null)
		{
			chiefFamily.moveTo(x, y);
		}else if(chief!=null)
		{
			chief.moveTo(x, y);
		}
		
		
		for(Group o : others)
		{
			o.moveTo(x, y);
		}

		Point2D new_pos = getLocation();
		if(getGroup()==null)
		{
			/* When the current group move to another cell, then the current group is added to the new cell 
			 * and remove from the previous one. If the current group is part of another, the we must not 
			 * add it to the cell because the whole group which if the "parent" of the current one will be added later
			 */
			updateLocationOnMap(old_pos, new_pos);
		}
		
		setLocation();//need to set the location
	}
	
	
	public boolean hunt(Group o)
	{
		if(o==null)
			return true;
		/*modelisate the fight whith a weaker group ang feeds the pack whith it
		 * 
		 */
		if (this.getStrength()<o.getStrength())
			return false;
		if (this.getAgility()<o.getAgility() && Math.random()>chief.getAgressivity())
			return false;
		double aManger = 0;
		boolean riposte = Math.random()<o.getAgressivity();
		if (this.getAgility()>o.getAgility())
		{
			if (riposte)
				aManger = o.setDeath();
			else
				aManger = o.setDeath(this.getSize());
		}
		else
		{
			if (riposte)
			{
				double s1 = this.getStrength(), s2 = o.getStrength();
				this.setDeath((int)((s2*s2*this.getSize())/(s1+s2)));
				aManger = o.setDeath((int)((s2*s1*this.getSize())/(s1+s2)));
			}
			else
				aManger = setDeath((int)((o.getAgility()-this.getAgility())*o.getSize()));
		}
		if(chief!=null && chief.needToEat())
		{
			if (chiefFamily!=null)
				if(aManger>=chiefFamily.getNeedsToEat())
					aManger-=chiefFamily.setSatiety();
				else
				{
					chiefFamily.feed(aManger);
					return true;
				}
			else
				if(aManger>=chief.getNeedsToEat())
					aManger -= chief.setSatiety();
				else
				{
					chief.setEnergy(aManger+chief.getEnergy());
					return true;
				}
		}
		for(Group g : others)
		{
			if(aManger>=g.getNeedsToEat())
				aManger-=g.setSatiety();
			else
			{
				aManger=g.feed(aManger);
				break;
			}
		}
	
		return true;
	}
	
	
	
	public String toString()
	{
		if(chief==null)
			return null;
		
		if(isAnimal())
			return getAnimal().toString();
		
		String res=getFormatedInfo()+"\n";
		res+="\tChef : " + ((chief!=null)?chief.toString():"");
		if(chiefFamily!=null)
		{
			res+="\n\tFamille du chef : \n\t\t"+chiefFamily.toString();
		}
		res+="\n\tMembres : ";
		for(Group o : others)
		{
			res+="\n\t\t";
			if(o.isAnimal())
			{
				res+= o.getAnimal().toString();
			}else
			{
				res+= o.toString();
			}
		}
		return res;
	}
}
