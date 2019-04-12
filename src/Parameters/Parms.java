package Parameters;
import Group.Group;
import Cell.*;
import Animal.*;

public final class Parms 
{	
	public static final double FACTOR_ORGANIZATION_HERD = 0.5;
	
	public static final double hungerThreshold = 0.7;
	public static final double criticalLifeThreshold = 0.3;
	
	public static final double DISTANCE_NEIGHBOOR = 1;
	
	public static final double DIM_CELL = 10;
	public static final double DELTA_MOVE = 1.5;
	
	public static final double MAX_QUANTITY_CELL = 10;
	public static final double MAX_HEIGHT_OBSTACLE = 10;
	
	private Parms(){}
	
	public static boolean AnimalNeedToEat(double needs)
	{
		return needs>=0;
	}
	
	public static double sociabilityPack(int number_element)
	{
		double lambda, mu;
		lambda = 0.5;
		mu = 0.1;
		
		return Math.exp(-lambda*number_element) + mu;
	}
	
	public static double sociabilityHerd(int number_element)
	{
		double lambda, mu;
		lambda = 0.5;
		mu = 0.1;
		
		return Math.exp(-lambda*number_element) + mu;
	}
	
	public static double distance(Group p, Group p2)
	{
		return 0;
	}
	
	public  static double getDirectionForAnimal(Cell[][] map, Animal a)
	{
		/* Retourne la direction sour forme d'angle par rapport à l'axe des abscisses qui indique la direction
		 * la plus attractive pour l'animal
		 * Si obtscale mais que taille animal plus grande que taille obstacles, alors ca peut être attractif 
		 * (il peut voir au delà de l'obstacle)
		 * Et sa vision s'arrette après sont champ vision
		 * 
		 * Ne pas reboucler (les bords de la map sont peu attractifs)
		 * 
		 * fonction de herbivore ou carnivore
		 */
		
		/* Pour un carnivor : 
		 * 		On se dirige vers la zone de chasse peut importe ce qu'il y a entre les deux sauf si autre prédateur (Group) plus
		 * 		fort, sinon déviation préventive (simulation rencontre Group)
		 * 		
		 * 		Si on est sur la zone de chasse :
		 * 			on cherche la proie la plus proche en prenant en compte des obstacles
		 */	
		return 0;
	}
	
}
