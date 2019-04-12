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
		
		
		System.out.println(Run._toString());
		
		h.add(f);
		
		System.out.println(Run._toString());
		
		h.setDeath();
		
		
		System.out.println(Run._toString());
		
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
	
	
}
