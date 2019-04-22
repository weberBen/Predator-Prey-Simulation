# Predator-Prey simulation

## Introduction 

The aim of the project is to partially reproduce the behevior of animals in nature. We focus our simulation around the social capabilities of animals. In other words, we allow agents in our simulation to organize their moves in groups of animals. For that purpose we desscoiate three types of groups :

- Familly made of a "father" (independent of his sex) who must bring back the needed food for the whole family, a "mother" who must protects children, and obviously the children. A family can be entirely made of herbivorous or carnivorous animals.
- Pack made of a chief who takes all the decision for the group and members of the pack. The pack is exclusively composed of carnivorous animals.
- Herd for herbivorous animals in which there is no hierarchy between members of the group.

In the simulation animals interact with other animals eclusively through their group (a single carnivorous animal is represented by a pack of one element, the chief, and a herbivorous animal is represented by a herd of one element).

## Interaction between groups

An animal is mainly described by the following attributes :

- his strength 
- his agility
- his aggressivity
- his sociability
- his age
- his vital energy, which can be seen as a life gauge
All these attributes are represented by a real between 0. and 1. (where 1 is the maximum possible value).



## Evolution of the attributes

At each iteration of the simulation animals will lose energy (because their body cosumes ressources). Below a threshold, animal will have to find food (vegetal ressources for herbivorous, and other animals for carnivorous). By eating ressources, animals will increase their vital energy.

Below another threshold, attributes of animal will be changed (for example strength and agility will be decreased, while aggressivity will be increased). These changes will remain until the animal find food, then modified attributes will slowly come back to the normal.
Following the age of an animal, attributes will also be changed : for a teenager attributes will be increased (like the strength) and for an old animal, attributes will be decreased.

Moreover, an animal has the possibility to espace a fight with another animal. In that case, attributes will be boosted and will rapidly come back to normal. This change allow the animal to run faster for example.
