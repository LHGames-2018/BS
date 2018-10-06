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
    	if (i < 5) {
    		i++;
    		return createMoveAction(Point.RIGHT);
    	}
    	if (i >= 5 && i < 9) {
    		i++;
    		return createMoveAction(Point.DOWN);
    	}
    	else {
    		//if(map.getTileBelowOf(player.getPosition()).getDensity() == 0) {
    			
    		//}
    		return createCollectAction(Point.DOWN);
    	}
    }
}
