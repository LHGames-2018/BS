package io.polyhx.lhgames.game.bot;

import io.polyhx.lhgames.game.*;
import io.polyhx.lhgames.game.action.*;
import io.polyhx.lhgames.game.point.*;
import io.polyhx.lhgames.game.tile.*;

import java.util.List;

public class Bot extends BaseBot {

	// Etats possibles, ajoutez-en tant que vous voulez.
	private enum State {RUN_STRAIGHT, ATTACK, MOVE, GATHER, HOME, FLEE};

	// L'etat principal du robot
	private State mainState = State.RUN_STRAIGHT;

	Map map;
	Player player;
	List<Player> others;
	GameInfo info;

	public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
		
		this.map = map;
		this.player = player;
		this.others = others;
		this.info = info;
		
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
				//return getGatherAction();
				break;
			}
			case HOME: {
				//return getHomeAction();
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
	 * Donne la direction a aller pour se rendre a la cible
	 * @param target la destination
	 * @return un MoveAction dans la bonne direction pour se rendre
	 */
	public MoveAction pathfind(IPoint target) {
		
		int diffX = target.getX() - player.getPosition().getX();
		int diffY = target.getY() - player.getPosition().getY();
		
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
