package io.polyhx.lhgames.game.bot;

import io.polyhx.lhgames.game.*;
import io.polyhx.lhgames.game.action.*;
import io.polyhx.lhgames.game.point.*;
import io.polyhx.lhgames.game.tile.*;

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
	int i = 0;
	public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
		
		if ( i < 7) {
			return createMoveAction(Point.LEFT);
		}
		else return null;
		/*this.map = map;
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
		return null; */
		return
		//return pathfind(player.getHousePosition());
		
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
		
		Point nearestMineral = getNearestResourcePoint();
		
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
	public Point getNearestResourcePoint(){
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
		
		// le bot se deplace en ligne droite vers sa destination, ne tient pas compte des obstacles
		if (Math.abs(diffX) > Math.abs(diffY)) {
			if (diffX > 0) {
				return createMoveAction(Point.RIGHT);				
			} else {
				return createMoveAction(Point.LEFT);
			}
		} else {
			if (diffY > 0) {
				return createMoveAction(Point.DOWN);				
			} else {
				return createMoveAction(Point.UP);
			}
		}
		
	}
	
	public void checkIfOthersNear() {
		List<Player> others = info.getOtherPlayers();
		for(int i = 0; i < others.size(); i++) {
			if(player.getDistanceTo(others.get(i)) < MAX_DISTANCE_BETWEEN_PLAYERS)
				mainState = State.ATTACK;
				enemy = others.get(i);
		}
	}
}
