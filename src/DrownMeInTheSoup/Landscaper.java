package DrownMeInTheSoup;

import battlecode.common.*;

public class Landscaper extends Unit {
    static MapLocation hqLoc;

    Landscaper(RobotController rc) {
        this.rc = rc;
    }

    static boolean cheater = false;

    //Looks through the blockchain for the HQ broadcast message
    public void findHQ() throws GameActionException {
        if (hqLoc == null) {
            int numRounds = rc.getRoundNum();
            for (int i = 1; i < numRounds; i++) {
                for (Transaction tx : rc.getBlock(i)) {
                    int[] message = tx.getMessage();
                    if (message[0] == secretNumba && message[1] == 0) {
                        this.hqLoc = new MapLocation(message[2], message[3]);
                    }
                }

            }
        }
    }

    boolean tryDig() throws GameActionException {
        Direction dir = randomDirection();
        if (rc.canDigDirt(dir)) {
            rc.digDirt(dir);
            return true;
        }
        return false;
    }


    public void run() throws GameActionException {
        //We first locate the HQ.
        if (hqLoc == null) findHQ();

        MapLocation ourLocation = rc.getLocation();

        if(rc.isReady()){
        //We see if we are adjacent to the enemy hq.
        RobotInfo[] enemyHQLook = rc.senseNearbyRobots(4);
        //We we find the enemy hq, we dig from opposite us to ontop of it
        for(RobotInfo r : enemyHQLook){
            if(!r.team.equals(rc.getTeam())  && r.type == RobotType.HQ){
                Direction enemyDirection = rc.getLocation().directionTo(r.location);
                if(rc.getDirtCarrying() > 1){
                    if(rc.canDepositDirt(enemyDirection)){
                        rc.depositDirt(enemyDirection);
                    }
                }
                else{
                    if(rc.canDigDirt(r.location.directionTo(ourLocation))){
                        rc.digDirt(r.location.directionTo(ourLocation));
                    }
                }
            }
        }
}

        //We try to move adjacent to the hq to start building the wall
        if (!ourLocation.isAdjacentTo(hqLoc)) {
            System.out.println("Not adjacent to hq, moving there!");
            Direction dir = ourLocation.directionTo(hqLoc);
            tryMove(dir);
        }

        Direction hqDir = ourLocation.directionTo(hqLoc);
        //We always start by digging on the hq to protect it from mean people
        if (rc.isReady() && rc.canDigDirt(hqDir)) {
            System.out.println("Digging dirt thats on the hq!");
            rc.digDirt(hqDir);
        }

        //Otherwise we deposit our dirt we are carrying.
        if (rc.getDirtCarrying() > 0 && rc.isReady()) {
            System.out.println("Finding tile to deposit dirt on!");
            //We sense the elevation at all of the 8 adjecent tiles and try to deposit dirt on the lowest elevation tile.
            Direction dir = Direction.NORTH;
            Direction bestDir = null;
            int minElevation = Integer.MAX_VALUE;
            for (int i = 0; i < 8; i++) {
                MapLocation checkLoc = hqLoc.add(dir);
                int el = rc.senseElevation(checkLoc);
                if (el < minElevation) {
                    bestDir = dir;
                    minElevation = el;
                }
                dir = dir.rotateRight();
            }

            MapLocation bestLocation = hqLoc.add(bestDir);
            System.out.println("Found " + bestLocation.x +  ", " + bestLocation.y + " with elevation " + minElevation);

            //If we are adjacent to that location we place dirt on it.
            Direction direction_to = ourLocation.directionTo(bestLocation);
            if(ourLocation.isAdjacentTo(bestLocation)){
                if(rc.canDepositDirt(direction_to)){
                    System.out.println("Depositing dirt on wall");

                    rc.depositDirt(direction_to);
                }
            }
            //If we aren't adjacent to that, we move towards it.
            else if(rc.canMove(direction_to) && !rc.senseFlooding(ourLocation.add(direction_to))){
                rc.move(direction_to);
                System.out.println("Moving towards lowest elevation tile.");
            }
        }

        //If there is no dirt, but we are at the hq, we try to mine dirt.
        else if(rc.isReady()){
            System.out.println("Digging on opposite side of hq.");
           Direction inverseDirection = hqLoc.directionTo(ourLocation);
           if(rc.canDigDirt(inverseDirection)){
               rc.digDirt(inverseDirection);
           }
        }

    }
}