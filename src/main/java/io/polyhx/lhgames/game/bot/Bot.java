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
	private State mainState = State.RUN_STRAIGHT;

	public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
		
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
	
}
