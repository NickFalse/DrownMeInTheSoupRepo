package DrownMeInTheSoup;
import battlecode.common.*;

public strictfp class RobotPlayer {

    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        Robot robot;
        switch (rc.getType()) {
            case HQ:
                robot = new HQ(rc);
                break;
            case MINER:
                robot = new Miner(rc);
                break;
            case REFINERY:
                robot = new Refinery(rc);
                break;
            case VAPORATOR:
                robot = new Vaporator(rc);
                break;
            case DESIGN_SCHOOL:
                robot = new DesignSchool(rc);
                break;
            case FULFILLMENT_CENTER:
                robot = new FulfillmentCenter(rc);
                break;
            case LANDSCAPER:
                robot = new Landscaper(rc);
                break;
            case DELIVERY_DRONE:
                robot = new Drone(rc);
                break;
            default:
                robot = new NetGun(rc);
                break;
        }
        while (true){
            robot.run();
            Clock.yield();
        }
    }

}
