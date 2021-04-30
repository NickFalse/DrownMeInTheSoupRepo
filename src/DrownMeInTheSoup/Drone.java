package DrownMeInTheSoup;

import battlecode.common.*;

public class Drone extends Unit {
    static MapLocation hqLoc = null;

    //Hi, I'm a drone and I'm a filthy cheater
    static boolean cheated = false;
    static MapLocation enemyLocation = null;
    Drone(RobotController rc) {
        this.rc = rc;
    }

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
    //OMG THIS IS PROCEDUARAL
    // Team A: 5, 27
    // Team B: 54, 27

    //NOU
    // Team A: 9, 38
    // Team B: 41, 38

    //Soup on the side
    //Team A: 20, 20
    //Team B: 27, 27

    //Eagles
    //Team A: 5, 57
    //Team B: 58, 6

    //Cow farm
    //Team A; 1, 1
    //Team B: 38, 1
    public void run() throws GameActionException {
        if (hqLoc == null) findHQ();

        if (!cheated) {
            //Procedural
            if (hqLoc.x == 5 && hqLoc.y == 27) {
                enemyLocation = new MapLocation(54, 27);
            } else if (hqLoc.x == 54 && hqLoc.y == 27) {
                enemyLocation = new MapLocation(5, 27);
            }
            //NoU
            if (hqLoc.x == 9 && hqLoc.y == 38) {
                enemyLocation = new MapLocation(41, 38);
            } else if (hqLoc.x == 41 && hqLoc.y == 38) {
                enemyLocation = new MapLocation(9, 38);
            }
            //SoupOnTheSide
            if (hqLoc.x == 20 && hqLoc.y == 20) {
                enemyLocation = new MapLocation(27, 27);
            } else if (hqLoc.x == 27 && hqLoc.y == 27) {
                enemyLocation = new MapLocation(20, 20);
            }
            //Eagles
            if (hqLoc.x == 5 && hqLoc.y == 57) {
                enemyLocation = new MapLocation(58, 6);
            } else if (hqLoc.x == 58 && hqLoc.y == 6) {
                enemyLocation = new MapLocation(5, 57);
            }
            //CowFarm
            if (hqLoc.x == 1 && hqLoc.y == 1) {
                enemyLocation = new MapLocation(38, 1);
            } else if (hqLoc.x == 1 && hqLoc.y == 1) {
                enemyLocation = new MapLocation(38, 1);
            }
        }

        //If are adjacant to the enemy locaiton we drop the landscaper.
        if (rc.getLocation().add(rc.getLocation().directionTo(enemyLocation)).isAdjacentTo(enemyLocation) && rc.isReady()) {
            //We drop our unit adjacent
            if (rc.canDropUnit(rc.getLocation().directionTo(enemyLocation))) {
                rc.dropUnit(rc.getLocation().directionTo(enemyLocation));
            }
            //Otherwise move a little to try to find an open slot????????
            else {
                tryMoveDrone(Direction.NORTH);
            }

        }

        //If we aren't adjacent we move in that direction.
        if (rc.isCurrentlyHoldingUnit() && rc.isReady()) {
            tryMoveDrone(rc.getLocation().directionTo(enemyLocation));
        }


        //If we aren't holding a unit, try to pickup a landscaper
        if (!rc.isCurrentlyHoldingUnit() && rc.isReady()) {
            RobotInfo[] robots = rc.senseNearbyRobots();
            RobotInfo designSchool = null;
            RobotInfo landscaper = null;
            for (RobotInfo r : robots) {
                if (r.type == RobotType.DESIGN_SCHOOL) {
                    designSchool = r;
                } else if (r.type == RobotType.LANDSCAPER) {
                    landscaper = r;
                }
            }

            //If we find a landscaper we try to pick it up
            if (landscaper != null) {
                if (rc.getLocation().isAdjacentTo(landscaper.location)) {
                    if (rc.canPickUpUnit(landscaper.ID)) {
                        rc.pickUpUnit(landscaper.ID);
                    }
                }
                //Or move towards it
                else {
                    tryMoveDrone(rc.getLocation().directionTo(landscaper.location));
                }
            }
            //Otherwise we move towards a design school and wait for it to spawn
            else if (designSchool != null) {
                tryMoveDrone(rc.getLocation().directionTo(designSchool.location));
            }
            //Othewise we check the blockchain for design schools
            else {
                int numRounds = rc.getRoundNum();
                for (int i = 1; i < numRounds; i++) {
                    for (Transaction tx : rc.getBlock(i)) {
                        int[] message = tx.getMessage();
                        if (message[0] == secretNumba && message[1] == 1) {
                            tryMoveDrone(rc.getLocation().directionTo(new MapLocation(message[2], message[3])));
                        }
                    }
                }
            }


        }
    }

}
