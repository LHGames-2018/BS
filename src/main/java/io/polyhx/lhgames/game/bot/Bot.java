package io.polyhx.lhgames.game.bot;

import io.polyhx.lhgames.game.GameInfo;
import io.polyhx.lhgames.game.Map;
import io.polyhx.lhgames.game.Player;
import io.polyhx.lhgames.game.action.IAction;
import io.polyhx.lhgames.game.point.Point;

import java.util.List;

public class Bot extends BaseBot {

	// Etats possibles, ajoutez-en tant que vous voulez.
	private enum State {RUN_STRAIGHT, ATTACK, MOVE, GATHER, HOME, FLEE};

	// L'etat principal du robot
	private State mainState = RUN_STRAIGHT;

	public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
		switch (mainState) {
		case RUN_STRAIGHT: {
			return getRunStraightAction();
		}
		case ATTACK: {
			return getRunStraightAction();
		}
		case MOVE: {
			return getRunStraightAction();
		}
		case GATHER: {
			return getRunStraightAction();
		}
		case HOME: {
			return getRunStraightAction();
		}
		case FLEE: {
			return getRunStraightAction();
		}
		default : {
			return null;
		}
		}
	}
	
	// Courrir tout droit
	public IAction getRunStraightAction() {
		
	}
	
}
