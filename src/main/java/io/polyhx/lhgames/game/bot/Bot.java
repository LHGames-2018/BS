package io.polyhx.lhgames.game.bot;

import io.polyhx.lhgames.game.GameInfo;
import io.polyhx.lhgames.game.Map;
import io.polyhx.lhgames.game.Player;
import io.polyhx.lhgames.game.action.IAction;
import io.polyhx.lhgames.game.point.Point;

import java.util.List;

public class Bot extends BaseBot {
	private int i = 0; 
    public IAction getAction(Map map, Player player, List<Player> others, GameInfo info) {
    	if (i < 1) {
    		i++;
    		return createMoveAction(Point.DOWN);
    	}
    	if (i >= 1 && i < 2) {
    		i++;
    		return createMoveAction(Point.RIGHT);
    	}
    	else {
    		return createCollectAction(Point.RIGHT);
    	}
    }
}
