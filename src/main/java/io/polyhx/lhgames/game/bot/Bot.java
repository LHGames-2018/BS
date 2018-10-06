package io.polyhx.lhgames.game.bot;

import io.polyhx.lhgames.game.*;
import io.polyhx.lhgames.game.action.*;
import io.polyhx.lhgames.game.point.*;
import io.polyhx.lhgames.game.tile.*;

import java.util.HashMap;
import java.util.List;
	   
public class Bot extends BaseBot {

	//constantes, idk how to declare it outside
	public static final int MAX_DISTANCE_BETWEEN_PLAYERS = 10;
	
	// Etats possibles, ajoutez-en tant que vous voulez.
	private enum State {RUN_STRAIGHT, ATTACK, MOVE, GATHER, HOME, FLEE};

	// L'etat principal du robot
	private State mainState = State.RUN_STRAIGHT;

	private Map map;
	private Player player;
	private List<Player> others;
	private GameInfo info;
	private Player enemy;

	public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
		
		this.map = map;
		this.player = player;
		this.others = others;
		this.info = info;
		this.enemy = null;
		
		switch (mainState) {
			case RUN_STRAIGHT: {
				return getRunStraightAction();
			}
			case ATTACK: {
				//return getAttackAction();
				break;
			}
			case MOVE: {
				//return getRunMoveAction();
				break;
			}
			case GATHER: {
				return gather();
			}
			case HOME: {
				if(player.getPosition()==player.getHousePosition())
					mainState = State.GATHER;
				pathfind(player.getHousePosition());
				break;
			}
			case FLEE: {
				//return getFleeAction();
				break;
			}
			default : {
				return null;
			}
		}
		return null;
	}
	
	// Courrir tout droit
	public IAction getRunStraightAction() {
		return createMoveAction(Point.UP);
	}
	
	
	/**
	 * Donne l'action que le bot doit executer pout amasser des ressources
	 * @return
	 */
	public IAction gather() {
		
		Point nearestMineral = getNearestResourcePoint(map, player);
		
		if (isNextTo(nearestMineral)) {
			return createCollectAction(directionOf(nearestMineral));
		} else {
			return pathfind(nearestAdjacentSpaceOf(nearestMineral));
		}
		
	}
	
	public boolean isNextTo(IPoint target) {
		return nearestAdjacentSpaceOf(target).equals(player.getPosition());
	}
	
	/**
	 * Donne l'espace adjacent d'une cible le plus pres de nous
	 * @param target
	 * @return l'espace
	 */
	public IPoint nearestAdjacentSpaceOf(IPoint target) {
		
		int diffX = target.getX() - player.getPosition().getX();
		int diffY = target.getY() - player.getPosition().getY();
		
		int targetX = target.getX();
		int targetY = target.getY();
		
		if (Math.abs(diffX) > Math.abs(diffY)) {
			if (diffX > 0) {
				targetX--;
			} else {
				targetX++;
			}
		} else {
			if (diffY > 0) {
				targetY--;
			} else {
				targetY++;
			}
		}
		
		return new Point(targetX, targetY);
		
	}
	
	/**
	 * Donne la direction d'un point
	 * @param target
	 * @return
	 */
	public Point directionOf(IPoint target) {
		
		int diffX = target.getX() - player.getPosition().getX();
		int diffY = target.getY() - player.getPosition().getY();
		
		if (Math.abs(diffX) > Math.abs(diffY)) {
			if (diffX > 0) {
				return Point.RIGHT;				
			} else {
				return Point.LEFT;
			}
		} else {
			if (diffY > 0) {
				return Point.DOWN;				
			} else {
				return Point.UP;
			}
		}
		
	}
	
	/**
	 * Donne la position de la ressource la plus proche
	 * @param carte, joueur
	 * @return position de la ressource la plus proche
	 */
	public Point getNearestResourcePoint(Map map, Player player){
		List<ResourceTile> resources = map.getResources();
		Point positionPlayer = player.getPosition();
		Point nearest = new Point();
		for(int i = 0; i < resources.size(); i++) {
			if(positionPlayer.getDistanceTo(resources.get(i).getPosition()) < positionPlayer.getDistanceTo(nearest))
				nearest = resources.get(i).getPosition();
		}
		return nearest;
	}

	/**
	 * Donne la direction a aller pour se rendre a la cible
	 * @param target la destination
	 * @return un MoveAction dans la bonne direction pour se rendre
	 */
	public IAction pathfind(IPoint target) {
		
		int diffX = target.getX() - player.getPosition().getX();
		int diffY = target.getY() - player.getPosition().getY();
		
		// si le bot est deja a destination
		if (diffX == 0 && diffY == 0) {
			return createMoveAction(new Point(0, 0));
		}
		
		IPoint nextStep = nextOptimalPoint(player.getPosition(), target);
		
		return createActionForTile(nextStep);
		
		//return createMoveAction(directionOf(target));
		
	}
	
	/**
	 * Donne l'action appropriee pour le tile
	 * @param target
	 * @return
	 */
	public IAction createActionForTile(IPoint target) {
		
		Tile tile = this.map.getTile(target);
		
		if (tile.isEmpty() || tile.isHouse()) {
			return createMoveAction(directionOf(target));
		} else if (tile.isPlayer()) {
			return createMeleeAttackAction(directionOf(target));
		} else if (tile.isResource()) {
			return createCollectAction(directionOf(target));
		} else if (tile.isWall()) {
			return createMeleeAttackAction(directionOf(target));
		} else {
			return createMoveAction(directionOf(target));
		}
		
	}
	
	/**
	 * Donne le meilleur prochain point par ou passer
	 * @param a
	 * @param b
	 */
	public IPoint nextOptimalPoint(IPoint a, IPoint b) {
		
		HashMap<IPoint, AStarNode> open = new HashMap<IPoint, AStarNode>();
		HashMap<IPoint, AStarNode> closed = new HashMap<IPoint, AStarNode>();
		
		AStarNode origin = new AStarNode();
		origin.gCost = 0;
		origin.hCost = (int)(a.getDistanceTo(b));
		origin.fCost = origin.hCost;
		
		open.put(origin.p, origin);
		
		AStarNode current;
		AStarNode[] neighbors = new AStarNode[4];
		
		while (true) {
			
			// trouver le plus petit fcost
			AStarNode smallest = null;
			for (java.util.Map.Entry<IPoint, AStarNode> c : open.entrySet()) {
				if (smallest == null) {
					smallest = c.getValue();
				} else if (c.getValue().compareTo(smallest) < 0) {
					smallest = c.getValue();
				}
			}
			current = smallest;
			
			// enlever le plus petit de open
			open.remove(current.p);
			
			// l'ajouter dans closed
			closed.put(current.p, current);
			
			// condition d'arret
			if (current.p.equals(b)) {
				while (current.parent != origin) {
					current = current.parent;
				}
				return current.p;
			}
			
			neighbors[0] = new AStarNode(new Point(current.p.getX(), current.p.getY()+1), current, b);
			neighbors[1] = new AStarNode(new Point(current.p.getX(), current.p.getY()-1), current, b);
			neighbors[2] = new AStarNode(new Point(current.p.getX()+1, current.p.getY()), current, b);
			neighbors[3] = new AStarNode(new Point(current.p.getX()-1, current.p.getY()), current, b);
			
			for (AStarNode node : neighbors) {
				if (!closed.containsKey(node.p)) {
					
					if (open.containsKey(node.p)) {
						if (node.compareTo(open.get(node.p)) < 0) {
							open.put(node.p, node);
						}
					} else {
						open.put(node.p, node);
					}
					
				}
			}
			
		}
		
	}
	
	private class AStarNode implements Comparable<AStarNode> {
		public IPoint p;
		public int hCost;
		public int gCost;
		public int fCost;
		
		public AStarNode parent;
		
		public AStarNode () {
		}
		
		public AStarNode (IPoint p, AStarNode parent, IPoint dest) {
			this.p = p;
			this.parent = parent;
			gCost = parent.gCost + findCostOfTile(p);
			hCost = (int)(10*p.getDistanceTo(dest));
			fCost = gCost + hCost;
		}

		@Override
		public int compareTo(AStarNode n) {
			if (n == null || this == null) {
				return 0;
			}
			
			else if (this.fCost > n.fCost) {
				return 1;
			} else if (this.fCost < n.fCost) {
				return -1;
			} else if (this.hCost > n.hCost) {
				return 1;
			} else if (this.hCost < n.hCost) {
				return -1;
			} else {
				return 0;
			}
		}
		
		public boolean equals(AStarNode n) {
			return this.p.equals(n.p);
		}
		
		
		
	}
	
	public int findCostOfTile (IPoint target) {
		
		Tile tile = this.map.getTile(target);
		
		if (tile.isEmpty() || tile.isHouse()) {
			return 10;
		} else if (tile.isPlayer()) {
			return 200;
		} else if (tile.isResource()) {
			if (player.getCarriedResource() + ((ResourceTile)tile).getResource() >= player.getResourceCapacity()) {
				return 10000;
			} else {
				return 10*((ResourceTile)tile).getResource() / 
						(int)(((ResourceTile)tile).getDensity()*player.getCollectingSpeed()*100);
			}
		} else if (tile.isWall()) {
			return 10*(int)Math.ceil(5.0/player.getAttack());
		} else {
			return 10000;
		}
		
	}
	
	public void checkIfOthersNear(Player player, GameInfo info) {
		List<Player> others = info.getOtherPlayers();
		for(int i = 0; i < others.size(); i++) {
			if(player.getDistanceTo(others.get(i)) < MAX_DISTANCE_BETWEEN_PLAYERS)
				mainState = State.ATTACK;
				enemy = others.get(i);
		}
	}
}
