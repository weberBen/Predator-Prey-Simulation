import java.util.ArrayList;

import Animal.Animal;

public class Test 
{
	public static void main(String[] args)
	{
		ArrayList<Animal> animals = new ArrayList<Animal>();
		
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
		 * 
		 * 
		 * 
		 * */
		 
	}
}
