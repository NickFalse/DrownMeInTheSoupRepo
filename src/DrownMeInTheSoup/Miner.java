package DrownMeInTheSoup;

import battlecode.common.*;

public class Miner extends Unit {
    static RobotType[] spawnedByMiner = {RobotType.REFINERY, RobotType.VAPORATOR, RobotType.DESIGN_SCHOOL, RobotType.FULFILLMENT_CENTER, RobotType.NET_GUN};
    static MapLocation hqLoc;
    //    static int numMiners = 0;
    static int numDesignSchools = 0;
    static int fufCount = 0;
    static boolean needToAnnounce = false;
    static boolean needToAnnounceFuf = false;

    Miner(RobotController rc) {
        this.rc = rc;
    }

    static RobotType randomSpawnedByMiner() {
        return spawnedByMiner[(int) (Math.random() * spawnedByMiner.length)];
    }

    static Direction randomDirection() {
        Direction direction = directions[random.nextInt(8)];
        return direction;
    }


    public void annouceRefinery(MapLocation loc) throws GameActionException {
        int[] message = new int[7];
        message[0] = secretNumba;
        //420 is our key for refinery
        message[1] = 420;
        message[2] = loc.x;
        message[3] = loc.y;
        if (rc.canSubmitTransaction(message, 5)) {
            rc.submitTransaction(message, 5);
        }
    }

    public MapLocation findRefinery() throws GameActionException {

        int numRounds = rc.getRoundNum();
        for (int i = 1; i < numRounds; i++) {
            for (Transaction tx : rc.getBlock(i)) {
                int[] message = tx.getMessage();
                if (message[0] == secretNumba && message[1] == 420) {
                    return new MapLocation(message[2], message[3]);
                }
            }

        }
        return null;
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

    void addFuf() throws GameActionException {
        int[] message = new int[7];
        message[0] = secretNumba;
        //69 is our key for new design school
        message[1] = 69;
        if (rc.canSubmitTransaction(message, 5)) {
            rc.submitTransaction(message, 5);
            needToAnnounceFuf = false;
        }
        else{
            needToAnnounceFuf = true;
        }
    }



    int countFuf() throws GameActionException {
        int fufCount = 0;
        if(needToAnnounceFuf){
            addFuf();
            fufCount++;
        }
        int numRounds = rc.getRoundNum();
        for (int i = 1; i < numRounds; i++) {
            for (Transaction tx : rc.getBlock(i)) {
                int[] message = tx.getMessage();
                if (message[0] == secretNumba && message[1] == 69) {
                    fufCount++;
                }
            }
        }
        System.out.println("Counted " + fufCount +  "fufillment centers");
        return fufCount;
    }


    void blockchainAddDesignSchool(MapLocation loc) throws GameActionException {
        int[] message = new int[7];
        message[0] = secretNumba;
        //1 is our key for new design school
        message[1] = 1;
        message[2] = loc.x;
        message[3] = loc.y;
        if (rc.canSubmitTransaction(message, 5)) {
            rc.submitTransaction(message, 5);
            needToAnnounce = false;
        }
        else{
            needToAnnounce = true;
        }
    }

    int countDesignSchools() throws GameActionException {
        int designSchoolCount = 0;
        if(needToAnnounce){
            blockchainAddDesignSchool(rc.getLocation());
            designSchoolCount++;
        }
        int numRounds = rc.getRoundNum();
        for (int i = 1; i < numRounds; i++) {
            for (Transaction tx : rc.getBlock(i)) {
                int[] message = tx.getMessage();
                if (message[0] == secretNumba && message[1] == 1) {
                    designSchoolCount++;
                }
            }
        }
        System.out.println("Counted " + designSchoolCount + " design schools");
        return designSchoolCount;
    }

    void tellPeopleAboutSoup() throws GameActionException {
        int[] message = new int[7];
        message[0] = secretNumba;
        //3 is our key for soup locations
        message[1] = 3;
        MapLocation location = rc.getLocation();
        message[2] = location.x;
        message[3] = location.y;
        if (rc.canSubmitTransaction(message, 5)) {
            rc.submitTransaction(message, 5);
        }
    }

    //Looks for the most recent soup broadcast can return null.
    MapLocation findMostRecentSoup() throws GameActionException {
        int numRounds = rc.getRoundNum();
        MapLocation soup = null;
        for (int i = 1; i < numRounds; i++) {
            for (Transaction tx : rc.getBlock(i)) {
                int[] message = tx.getMessage();
                if (message[0] == secretNumba && message[1] == 3) {
                    soup = new MapLocation(message[3], message[4]);
                }
            }
        }
        return soup;
    }


    public void run() throws GameActionException {
        //Miner searching for HQ, should only run on startup.
        if (hqLoc == null) findHQ();
        System.out.println("HQ LOCATION X: " + hqLoc.x + " Y: " + hqLoc.y);
        System.out.println("I have " + rc.getSoupCarrying() + "soup!");

        //If we are by something that lets us deposit, we deposit all our soup.
        for (Direction d : directions) {
            if (rc.isReady() && rc.canDepositSoup(d)) {
                System.out.println("Depositing soup");
                rc.depositSoup(d, rc.getSoupCarrying());
            }
        }

        //Miner moving to refinery of some kind  if full.
        if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
            Direction hqPath = rc.getLocation().directionTo(hqLoc);
            tryMove(hqPath);
            System.out.println("Heading towards HQ!");
        }
//        if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
//            //Check to see if a refinery exists
//            MapLocation refinery = findRefinery();
//
//            //If it doesn't and we are full, we make one
//            if(refinery == null){
//                if(rc.isReady()){
//                    Direction d = Direction.NORTH;
//                    for(int i = 0; i < 8; i++){
//                        if(rc.canBuildRobot(RobotType.REFINERY, d)){
//                            rc.buildRobot(RobotType.REFINERY, d);
//                            annouceRefinery(rc.getLocation().add(d));
//                        }
//                    }
//                }
//            }
//
//            //Otherwise we go to the closest one.
//            else if(rc.getLocation().distanceSquaredTo(hqLoc) > rc.getLocation().distanceSquaredTo(refinery)) {
//                Direction hqPath = rc.getLocation().directionTo(hqLoc);
//                tryMove(hqPath);
//                System.out.println("Heading towards HQ!");
//            }
//            else{
//                Direction refPath = rc.getLocation().directionTo(refinery);
//                tryMove(refPath);
//                System.out.println("Heading towards refinery!");
//            }
//        }

        //Otherwise we see if we are in a location where we can actually mine the soup.
        for (Direction d : directions) {
            if (rc.isReady() && rc.canMineSoup(d)) {
                rc.mineSoup(d);
                tellPeopleAboutSoup();
                System.out.println("Mining Soup, telling people about it.");
            }
        }

        //We first spawn a fufillment center to get a rush drone.
        fufCount = countFuf();
        if(fufCount < 1){
            Direction d = randomDirection();
            if (rc.isReady() && rc.canBuildRobot(RobotType.FULFILLMENT_CENTER, d)) {
                rc.buildRobot(RobotType.FULFILLMENT_CENTER, d);
                addFuf();
            }
        }

        //We try to make two design schools asap to start building walls.
        numDesignSchools = countDesignSchools();
        if (numDesignSchools < 1) {
            Direction d = randomDirection();
            if (rc.isReady() && rc.canBuildRobot(RobotType.DESIGN_SCHOOL, d)) {
                rc.buildRobot(RobotType.DESIGN_SCHOOL, d);
                blockchainAddDesignSchool(rc.getLocation().add(d));
            }
        }

        MapLocation myLoc = rc.getLocation();

        if (rc.getSoupCarrying() != RobotType.MINER.soupLimit) {
            //Next we try to sense any soup we can, if there is soup nearby, and we aren't mining, we move towards it.
            MapLocation[] soupLocations = rc.senseNearbySoup();
            if (soupLocations.length != 0) {
                int distance = Integer.MAX_VALUE;
                MapLocation closest = null;
                for(MapLocation m : soupLocations){
                    int dist = m.distanceSquaredTo(myLoc);
                    if(dist < distance){
                        closest = m;
                        distance = dist;
                    }
                }
                System.out.println("Sensed soup, going there to mine");
                tryMove(rc.getLocation().directionTo(closest));
            } else {
                //Otherwise we scan the blockchain for the most recent soup location:
                MapLocation soupFromChain = findMostRecentSoup();
                if (soupFromChain != null) {
                    tryMove(rc.getLocation().directionTo(soupFromChain));
                    System.out.println("Going to location from blockchain");
                } else {
                    //Lastly we just random wander.
                    tryMove(randomDirection());
                    System.out.println("Moving in random direction");

                }
            }
        }
    }
}