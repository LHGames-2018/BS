package io.polyhx.lhgames.game.bot;

import io.polyhx.lhgames.game.*;
import io.polyhx.lhgames.game.action.*;
import io.polyhx.lhgames.game.point.*;
import io.polyhx.lhgames.game.tile.*;
import java.util.List;
	   
public class Bot extends BaseBot {
	
	// Etats possibles, ajoutez-en tant que vous voulez.
	private enum State {GATHER, HOME, FINDRESOURCE};

	// L'etat principal du robot
	private State mainState = State.FINDRESOURCE;

	private Map map;
	private Player player;
	private List<Player> others;
	private GameInfo info;


	public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
		this.map = map;
		this.player = player;
		this.others = others;
		this.info = info;

        
		switch (mainState) {
		case FINDRESOURCE : {
			return findresource();
		}
			case GATHER: {
                return gather();
			}
			case HOME: {
				if(player.getPosition()==player.getHousePosition())
					mainState = State.GATHER;
				return pathfind(player.getHousePosition());
			}
			default : {
				return null;
			}
		}
	}
	
	public IAction findresource() {
		
		Point nearestMineral = getNearestResourcePoint();
		if(player.getPosition().equals(nearestAdjacentSpaceOf(nearestMineral))) {
			mainState = State.GATHER;
			return createCollectAction(directionOf(nearestMineral));
		}
			
		return pathfind(nearestAdjacentSpaceOf(nearestMineral));
	}
	/**
	 * Donne l'action que le bot doit executer pout amasser des ressources
	 * @return
	 */
	public IAction gather() {
		Point nearestMineral = getNearestResourcePoint();
		if(map.getTile(nearestMineral).isEmpty()) {
			mainState = State.HOME;
			 System.out.println("This will be visible from the dashboard.");
			return pathfind(player.getHousePosition());
		}
		 System.out.println("This will be visible from the dashboard.jk");
		 System.out.println(directionOf(nearestMineral).getX().toString());
		return createCollectAction(directionOf(nearestMineral));
		
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
	public IPoint directionOf(IPoint target) {
		
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
}
	

