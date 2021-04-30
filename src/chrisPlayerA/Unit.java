package chrisPlayerA;

import battlecode.common.Direction;
import battlecode.common.GameActionException;

public abstract class Unit extends Robot {

    static int secretNumba = 1203123;

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

    boolean tryMove() throws GameActionException {
        for (Direction dir : directions)
            if (tryMove(dir))
                return true;
        return false;
    }

    boolean tryMove(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMove(dir) && !rc.senseFlooding(rc.getLocation().add(dir))) {
            rc.move(dir);
            System.out.println("Moved sucessfully");
            return true;
        } else if (rc.isReady()) {
            for (int i = 0; i < 7; i++) {
                Direction toMove = dir.rotateRight();
                if (rc.canMove(toMove) && !rc.senseFlooding(rc.getLocation().add(toMove))) {
                    rc.move(toMove);
                    System.out.println("Path blocked, moving " + dir.toString());
                    return true;
                }
                dir = toMove;
            }
            System.out.println("Move failed.");
        }

        return false;
    }
}


