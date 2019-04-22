package Parameters;
import Group.Group;
import Main.World;
import Cell.*;

import java.awt.Point;
import java.awt.geom.Point2D;

import Animal.*;

/* Contains all the needed parameters and useful function*/

public final class Parms 
{	
	public static final double FACTOR_ORGANIZATION_HERD = 0.5;
	
	public static final double hungerThreshold = 0.7;
	public static final double criticalLifeThreshold = 0.3;
	public static final double deltaE = 0.05; 
	
	public static final double DISTANCE_NEIGHBOOR = 1;
	
	public static final double DIM_CELL = 10;
	public static final double DELTA_MOVE = 1.5;
	
	public static final double MAX_QUANTITY_FOOD_CELL = 10;
	public static final double MAX_HEIGHT_OBSTACLE = 10;
	
	public static final int INITIAL_NUMBER_PER_SPECIE = 10;
	public static final double DISPERSION_VALUE_INSTANCE_SPECIE = 0.25;
	
	public static final int FACTOR_FOR_AGE = 100;
	public static final double DELTA_ENERGY = 0.05;
	
	private Parms(){}
	
	public static boolean AnimalNeedToEat(double needs)
	{
		return needs>0;
	}
	
	public static double sociabilityPack(int number_element)
	{
		double lambda, mu;
		lambda = 0.2;
		mu = 0.5;
		
		return Math.exp(-lambda*number_element)/2 + mu;
	}
	
	public static double sociabilityHerd(int number_element)
	{
		double lambda, mu;
		lambda = 0.15;
		mu = 0.5;
		
		return Math.exp(-lambda*number_element)/2 + mu;
	}
	
	public static double distance(Group p, Group p2)
	{
		return 0;
	}
	
	public static boolean nearToInteract(Group g1, Group g2)
	{
		Point2D p1 = g1.getLocation();
		Point2D p2 = g2.getLocation();
		
		double delta_x = Math.abs(p1.getX()-p2.getX());
		double delta_y = Math.abs(p1.getY()-p2.getY());
		
		return delta_x<DIM_CELL && delta_y<DIM_CELL;//on the same cell
	}
	
	public static double getValueDispersion(double val)
	{
		return Math.random()*(val*DISPERSION_VALUE_INSTANCE_SPECIE)+ val*(1-DISPERSION_VALUE_INSTANCE_SPECIE/2);
	}
	
	public  static double getDirectionForAnimal(Cell[][] map, Animal a)
	{
		/* Retourne la direction sour forme d'angle par rapport � l'axe des abscisses qui indique la direction
		 * la plus attractive pour l'animal
		 * Si obtscale mais que taille animal plus grande que taille obstacles, alors ca peut �tre attractif 
		 * (il peut voir au del� de l'obstacle)
		 * Et sa vision s'arrette apr�s sont champ vision
		 * 
		 * Ne pas reboucler (les bords de la map sont peu attractifs)
		 * 
		 * fonction de herbivore ou carnivore
		 */
		
		/* Pour un carnivor : 
		 * 		On se dirige vers la zone de chasse peut importe ce qu'il y a entre les deux sauf si autre pr�dateur (Group) plus
		 * 		fort, sinon d�viation pr�ventive (simulation rencontre Group)
		 * 		
		 * 		Si on est sur la zone de chasse :
		 * 			on cherche la proie la plus proche en prenant en compte des obstacles
		 */	
		
		if (a.getIsEscaping())
			return a.getDir();
		a.setDir(Math.PI*Math.random()*2);
		return a.getDir();
	}
	
	public static Point getCellCoordinate(double x, double y)
	{
		int i = (int)(x/DIM_CELL);
		int j = (int)(y/DIM_CELL);
		
		return new Point(i,j);
	}
	
	public static boolean isOutOfBounds(double x, double y)
	{
		return (x<0) || (x>=World.getSize())|| (y<0) || (y>World.getSize());
	}
	
	
}