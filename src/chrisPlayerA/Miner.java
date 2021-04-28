package chrisPlayerA;

import battlecode.common.*;

public class Miner extends Unit {
    Miner(RobotController rc){
        this.rc = rc;
    }
    static RobotType[] spawnedByMiner = {RobotType.REFINERY, RobotType.VAPORATOR, RobotType.DESIGN_SCHOOL,
            RobotType.FULFILLMENT_CENTER, RobotType.NET_GUN};
    static RobotType randomSpawnedByMiner() {
        return spawnedByMiner[(int) (Math.random() * spawnedByMiner.length)];
    }
    static MapLocation hqLoc;
    static int numMiners = 0;

    boolean tryMine(Direction dir){
        try {
            if (rc.isReady() && rc.canMineSoup(dir)) {
                rc.mineSoup(dir);
                return true;
            } else return false;
        }catch(Throwable e){
            e.printStackTrace();
            return false;
        }
    }
    boolean tryRefine(Direction dir){
        try {
            if (rc.isReady() && rc.canDepositSoup(dir)) {
                rc.depositSoup(dir, rc.getSoupCarrying());
                return true;
            } else return false;
        }catch(Throwable e){
            e.printStackTrace();
            return false;
        }
    }

    boolean nearbyRobot(RobotType target) throws GameActionException{
        RobotInfo[] robots = rc.senseNearbyRobots();

        for(RobotInfo r : robots) {
            if(r.getType() == target){
                return true;
            }
        }
        return false;
    }

    public void findHQ(){
        if (hqLoc == null){
            RobotInfo[] robots = rc.senseNearbyRobots();
            for(RobotInfo robot : robots){
                if(robot.type == RobotType.HQ && robot.team == rc.getTeam()){
                    this.hqLoc = robot.location;
                }
            }
        }
    }

    public void run() throws GameActionException {
        //Miner searching for HQ
        findHQ();

        //Miner moving to HQ
        if(rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
            Direction hqPath = rc.getLocation().directionTo(hqLoc);
            tryMove(hqPath);
            System.out.println("Heading towards HQ!");
        } else {
            tryMove(randomDirection());
            System.out.println("Moving in random direction");
        }
        /*
        if (!nearbyRobot(RobotType.DESIGN_SCHOOL)) {
            if (tryBuild(RobotType.DESIGN_SCHOOL, randomDirection())) {
                System.out.println("Successfully Built a design school!");
            }
        }
        */
        for(int i = 0; i < 1; i++){
            if (tryBuild(RobotType.FULFILLMENT_CENTER, randomDirection())) {
                System.out.println("Successfully Built a fulfillment center!");
            }
        }
        /*
        if (!nearbyRobot(RobotType.REFINERY)) {
            if (tryBuild(RobotType.REFINERY, randomDirection())) {
                System.out.println("Successfully Built a Refinery!");
            }
        }

        if (!nearbyRobot(RobotType.NET_GUN)) {
            if (tryBuild(RobotType.NET_GUN, randomDirection())) {
                System.out.println("Successfully Built a Net Gun!");
            }
        }
        */

    }
}