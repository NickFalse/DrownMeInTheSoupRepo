package chrisPlayerA;

import battlecode.common.*;
import java.lang.Math;

public class Landscaper extends Unit {
    Landscaper(RobotController rc){
        this.rc = rc;
    }
    static MapLocation hqLoc;

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

    boolean tryDig() throws GameActionException{
        Direction dir = randomDirection();
        if(rc.canDigDirt(dir)) {
            rc.digDirt(dir);
            return true;
        }
        return false;
    }

    boolean goTo(Direction dir) throws GameActionException{
        Direction[] toTry = {dir, dir.rotateLeft(), dir.rotateRight()};
        for(Direction d : toTry){
            if(tryMove(d)){
                return true;
            }
        }
        return false;
    }

    boolean goTo(MapLocation destination) throws GameActionException{
        return goTo(rc.getLocation().directionTo(destination));
    }

    public void run() throws GameActionException {
        if(rc.getDirtCarrying() == 0){
            tryDig();
        }
        if(hqLoc != null){
            MapLocation bestPlaceToBuildWall = null;
            int lowestElevation = -9999999;
            for(Direction dir : directions){
                MapLocation tileToCheck = hqLoc.add(dir);

                if(rc.getLocation().distanceSquaredTo(tileToCheck) < 4 && rc.canDepositDirt(rc.getLocation().directionTo(tileToCheck))){
                    if(rc.senseElevation(tileToCheck) < lowestElevation){
                        lowestElevation = rc.senseElevation(tileToCheck);
                        bestPlaceToBuildWall = tileToCheck;
                    }
                }
                if(Math.random() < 0.4){
                    if(bestPlaceToBuildWall != null) {
                        rc.depositDirt(rc.getLocation().directionTo(tileToCheck));
                        System.out.println("Building a wall");
                    }
                }
                if(hqLoc != null) {
                    goTo(hqLoc);
                } else{
                    tryMove(randomDirection());
                }
            }
        }
    }
}