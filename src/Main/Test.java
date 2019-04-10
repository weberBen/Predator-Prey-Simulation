package Main;
import java.util.ArrayList;

import Cell.*;
import Animal.*;
import Group.*;
import Parameters.*;
import Main.*;

public class Test 
{
	private ArrayList<Animal> list;
	
	public Test()
	{
		list = new ArrayList<Animal>();
	}
	
	public static void setObstacles(Cell cell, int i, int j)
	{
		int maxObstacle = 1;
		double numObstacles = Math.random()*maxObstacle;
		double x, y;
		double rigidity, height, width_ellipse, height_ellipse, rotation;
		Obstacle ob;
		
		for(int k=0; k<numObstacles; k++)
		{
			//create a new obstacle for the cell
			x = i*Parms.DIM_CELL + Math.random()*Parms.DIM_CELL;
			y = j*Parms.DIM_CELL + Math.random()*Parms.DIM_CELL;
			
			height = Math.random()*Parms.MAX_HEIGHT_OBSTACLE;
			rigidity = Math.random();
			
			width_ellipse = Math.random()*Obstacle.MAX_WIDTH_ELLIPSE;
			height_ellipse = Math.random()*Obstacle.MAX_HEIGHT_ELLIPSE;
			rotation = Math.random();
			
			ob = new Obstacle(x, y,rigidity, height, width_ellipse, height_ellipse, rotation);
			//check if the new obstacle extend to other cells
			
			cell.addObstacle(ob);
		}
	}
	
	public void test1()
	{
		list.add(new Carnivorous());
		list.add(new Carnivorous());
		list.add(new Carnivorous());
		
		Run.addGroup(list.get(0));
		Run.addGroup(list.get(1));
		Run.addGroup(list.get(2));
	}
	
	public void test2()
	{
		Run.removeGroup(list.get(1));
	}
	
	private static void main(String[] args)
	{
		ArrayList<Animal> animals = new ArrayList<Animal>();
		int DIM = 5;
		int quantity = 5;
		
		
		Cell[][] map = new Cell[DIM][DIM];
		for(int i=0; i<map.length; i++)
    	{
    		for(int j=0; j<map[i].length; j++)
    		{
    			map[i][j] = new Cell(Math.random()*Parms.MAX_QUANTITY_CELL);
    			
    			for(int k=0; k<Math.random()*10; k++)
    			{
    				if(Math.random()<0.5)
    				{
    					map[i][j].addAnimals(new Pack(new Herbivorous()));
    				}else
    				{
    					map[i][j].addAnimals(new Pack(new Carnivorous()));
    				}
    			}
    			
    			setObstacles(map[i][j], i, j);
    		}
    	}
		System.out.println("end");
		GraphicInterface I = new GraphicInterface(map);
		I.update();
		//I.display();
		/* Création d'une famille
		 * 
		 * Family f = new Family();
		 * f.setFather(new Animal());
		 * f.setMother(new Animal());
		 * f.addChild(new Animal()); <-- force = 0
		 * 
		 * Ajout de f.getFather() et f.getMother() (de type Pack) dans le monde
		 * Ajout de f dans la liste des groupes
		 * 
		 * Sur la map :
		 * 	Si Groupe p1 attaque le père de la famille (attaque le groupe du père)
		 * 			On récupère le groupe famille par l'intermédiaire du père, on regarde
		 * 			si la mère et les enfants sont à proximité du père et on agit en conséquence
		 * 	Si Groupe p2 attaque le groupe mère de la famille, 
		 * 			On récupère le groupe famille par l'intermédiaire du la mère (ou des enfants)
		 * 			et on regarde si le père est a proximité
		 * 
		 * Lors de la simulation :
		 * 	Lorsque l'on passe une partie de la map au animaux pour les faire évoluer, on passe la map
		 *	au groupe famille directement (une partie de la map pour le père et pour la mère)
		 * 
		 * 
		 * 	Pour les meutes pour intéragir avec la map traitement différents selon que la meute est un animal ou une vraie meute
		 * 	Si la meute est un animal, alors on fait intéragir l'animal selon le groupe auquel il appartient (ou s'il n'en a pas 
		 * 	selon les règles normale pour un animal solitaire)
		 */
		
		/*
		 * Pour un animal à chaque itération :
		 * 	 veillir()
		 *   move() 
		 * 	 
		 *Percours de la liste de sgroupes, si taille  0 -> supression du groupe de la liste 
		 * 
		 * 
		 * */
		 
	}
}
