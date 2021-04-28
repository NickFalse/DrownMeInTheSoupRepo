package chrisPlayerA;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public abstract class Robot {
    RobotController rc;
    static Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST
    };

    boolean tryBuild(RobotType type, Direction dir){
        try {
            if (rc.isReady() && rc.canBuildRobot(type, dir)) {
                rc.buildRobot(type, dir);
                return true;
            } else return false;
        }catch(Throwable e){
            e.printStackTrace();

        }
        return false;
    }
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }
    abstract void run() throws GameActionException;
}