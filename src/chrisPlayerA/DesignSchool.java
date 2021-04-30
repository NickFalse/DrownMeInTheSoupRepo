package chrisPlayerA;

import battlecode.common.*;

import static chrisPlayerA.Unit.secretNumba;

public class DesignSchool extends Building {
    DesignSchool(RobotController rc){
        this.rc = rc;
    }

    void createLandscaper() throws GameActionException {
        int[] message = new int[7];
        message[0] = secretNumba;
        //5 is our key for new design school
        message[1] = 5;
        if (rc.canSubmitTransaction(message, 5)) {
            rc.submitTransaction(message, 5);
        }
    }

    int countLandscaper() throws GameActionException {
        int numRounds = rc.getRoundNum();
        int landscaperCount = 0;
        for (int i = 1; i < numRounds; i++) {
            for (Transaction tx : rc.getBlock(i)) {
                int[] message = tx.getMessage();
                if (message[0] == secretNumba && message[1] == 5) {
                    landscaperCount++;
                }
            }
        }
        return landscaperCount;
    }

    public void run() throws GameActionException {
        //If we don't have 5 landscapers, we spawn 5 of them.
        int count = countLandscaper();
        if(rc.isReady() && count < 5){
            for(Direction d : directions){
                if(rc.canBuildRobot(RobotType.LANDSCAPER, d)){
                    rc.buildRobot(RobotType.LANDSCAPER, d);
                    createLandscaper();
                }
            }

        }
    }
}
