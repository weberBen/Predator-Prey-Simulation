package Main;
import Group.*;
import Animal.*;

import java.util.ArrayList;

public class Run 
{
	private static ArrayList<Group> list = new ArrayList<Group>();
	
	private Run(){}
	
	public static void main(String[] args)
	{
		//pack
		
		/*
		ArrayList<Animal> temp_l = new ArrayList<Animal>();
		
		for(int i=0; i<5; i++)
		{
			Animal a = new Carnivorous();
			temp_l.add(a);
			list.add(new Pack(a));
		}
		
		System.out.println("-----------------------\nliste=\n"+Run._toString());
		
		Pack  h= new Pack();
		ArrayList<Group> temp = new ArrayList<Group>(list);
		for(Group o : temp)
		{
			h.add(o);
		}
		list.add(h);
		
		//family
		Animal father = new Carnivorous();
		//father.setStrength(10);//!!!!!!!!!!!!!!!!!!!!!!!! problème
		Animal mother = new Carnivorous();
		Pack g_father = new Pack(father);
		Pack g_mother = new Pack(mother);
		
		Family f = new Family(g_father, g_mother);
		list.add(g_father);
		list.add(g_mother);
		
		h.add(f);
		*/
		
		//------------------------------------------------------------------------
		
		 Herd h1 = createHerbivorous();
		 Herd h2 =  createHerbivorous();
		 Herd h3 = createHerbivorous();
		 Herd h4 = createHerbivorous();
		 
		 h1.add(h2);
		 h1.add(h3);
		 h1.add(h4);
		 
		 h1.setDeath(10);
		 
		 display();
		 
		 
		//Herd  h = createHerd(3);
	}
	
	public static void display()
	{
		System.out.println(Run._toString());
	}
	
	public static Herd createHerbivorous()
	{
		Herbivorous a = new Herbivorous();
		Herd h = new Herd(a);
		Run.addGroup(h);
		
		return h;
	}
	
	public static Pack createCarnivorous()
	{
		Carnivorous a = new Carnivorous();
		Pack h = new Pack(a);
		Run.addGroup(h);
		
		return h;
	}
	
	public static Herd createHerd(int numMember)
	{
		Herd h = new Herd();
		for(int i=0; i<numMember; i++)
		{
			h.add(createHerbivorous());
		}
		Run.addGroup(h);
		
		return h;
	}
	
	public static String _toString()
	{
		String res ="---------------LISTE GROUPES-------------------\n";
		for(Group o : list)
			res+="*** "+o.toString()+"\n";
		res+="\n--------------FIN LISTE GROUPES--------------------";
		return res;
	}
	
	public static void removeGroup(Group o)
	{
		list.remove(o);
	}
	
	public static void addGroup(Group o)
	{
		list.add(o);
	}
	
	public static void addGroup(Animal a) throws IllegalArgumentException
	{
		Group o = null;
		if(a.isCarnivorous())
		{
			o = new Pack(a);
		}else if(a.isHerbivorous())
		{
			o = new Herd(a);
		}else
		{
			throw new IllegalArgumentException("Ajout d'un animal non autorisé");
		}
		
		list.add(o);
	}
}
