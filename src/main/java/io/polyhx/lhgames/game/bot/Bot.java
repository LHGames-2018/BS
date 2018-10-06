package io.polyhx.lhgames.game.bot;

import io.polyhx.lhgames.game.GameInfo;
import io.polyhx.lhgames.game.Map;
import io.polyhx.lhgames.game.Player;
import io.polyhx.lhgames.game.action.IAction;
import io.polyhx.lhgames.game.point.Point;

import java.util.List;

public class Bot extends BaseBot {
	static int i = 0; 
    public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
    	return createMoveAction(Point.LEFT);
    	//return createMeleeAttackAction(Point.RIGHT);
    }
}
