package nickPlayerA;

import battlecode.common.Direction;
import battlecode.common.GameActionException;

public abstract class Unit extends Robot{
    boolean tryMove(){
        for (Direction dir : directions)
            if (tryMove(dir))
                return true;
        return false;
    }
    boolean tryMove(Direction dir){
        try {
            // System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
            if (rc.isReady() && rc.canMove(dir)) {
                rc.move(dir);
                return true;
            } else return false;
        }catch(Throwable e){
            e.printStackTrace();
        }
        return false;
    }
}
