import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Represents a Gatherer - a type of non-stationary actor with specific movements around the ShadowLife world
 *  @author Shreya Sharma
 *  @studentNumber 910986
 *  @version 1.0
 */
public class Gatherer extends NonStationaryActor{
    //gatherer information
    private String direction = "LEFT";
    private boolean carrying = false;
    private boolean active = true;

    /**
     * The following constructor is used in the initial declaration of Gatherers within the main method
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate for the actor
     */
    public Gatherer(int xPos, int yPos){
        super("res/images/gatherer.png", xPos, yPos);
        clockLocation = Arrays.asList(clockwise).indexOf(direction);
    }

    /**
     * The following constructor is used in the constructor of ShadowLife and the associated update method - it contains
     * additional information to help the Gatherer to determine if they are 'standing' on something
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate for the actor
     * @param nonStationaryActors This is an ArrayList of all non-stationary actors in the ShadowLife world
     * @param stationaryActors This is an ArrayList of all stationary actors in the ShadowLife world
     */
    public Gatherer(int xPos, int yPos, ArrayList<NonStationaryActor> nonStationaryActors, ArrayList<StationaryActor> stationaryActors){
        super("res/images/gatherer.png", xPos, yPos, nonStationaryActors, stationaryActors);
        clockLocation = Arrays.asList(clockwise).indexOf(direction);
    }

    /**
     * The following constructor is used in the constructor of ShadowLife and the associated update method - it contains
     * additional information to help the Gatherer to determine if they are 'standing' on something and sets an
     * initial direction for the Gatherer that is (potentially) different than the default value
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate for the actor
     * @param direction This is the direction that is specified for the Gatherer upon its declaration
     * @param nonStationaryActors This is an ArrayList of all non-stationary actors in the ShadowLife world
     * @param stationaryActors This is an ArrayList of all stationary actors in the ShadowLife world
     */
    public Gatherer(int xPos, int yPos, String direction,  ArrayList<NonStationaryActor> nonStationaryActors, ArrayList<StationaryActor> stationaryActors){
        super("res/images/gatherer.png", xPos, yPos, nonStationaryActors, stationaryActors);
        this.direction = direction;
        clockLocation = Arrays.asList(clockwise).indexOf(direction);
    }

    private boolean getCarrying(){
        return carrying;
    }

    /**
     * The following method is implemented to retrieve their active status - this helps in determining whether
     * all non-stationary actors have set the active attribute to false and whether the window should close
     * @return boolean The active attribute - whether the actor moves or not
     */
    @Override
    public boolean getActive(){
        return active;
    }

    /**
     * The following method is implemented by each gatherer to return the value of their duplicate attribute
     * @return int The value of the duplicate attribute
     *                  1 if duplicates need to be made
     *                  0 if duplicates do not need to be made
     */
    @Override
    public int getDuplicate(){
        return duplicate;
    }

    private void setActive(boolean activeResult){
        this.active = activeResult;
    }

    private void setDirection(String newDirection){
        this.direction = newDirection;
        this.clockLocation = Arrays.asList(clockwise).indexOf(this.direction);
    }
    private void setCarrying(boolean carryingType){
        this.carrying = carryingType;
    }

    /**
     * The following method is implemented by each gatherer to re-set their 'duplicate' attribute
     * after the actor has stepped on a mitosis pool and the update program in ShadowLife has been notified that
     * duplicates of the actor must be generated
     * @param duplicate This is the value that the attribute duplicate should be set to -
     *                  1 if duplicates need to be made
     *                  0 if duplicates do not need to be made
     */
    @Override
    public void setDuplicate(int duplicate){
        this.duplicate = duplicate;
    }

    /**
     * The following method determines how each Gatherer moves across the ShadowLife world, including default movements
     * and specialised movements when the Gatherer interacts with other (Stationary) actors in the world
     */
    @Override
    public void move() {
        if (active) {
            switch (direction) {
                case "LEFT":
                    this.xPos -= SQUARE_SIZE;
                    break;
                case "RIGHT":
                    this.xPos += SQUARE_SIZE;
                    break;
                case "UP":
                    this.yPos -= SQUARE_SIZE;
                    break;
                case "DOWN":
                    this.yPos += SQUARE_SIZE;
                    break;
            }
        }

        for (StationaryActor sActor : stationaryActors) {
            // if the gatherer is standing on a stockpile or hoard
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && (sActor instanceof Stockpile || sActor instanceof Hoard)) {
                if (getCarrying()) {
                    setCarrying(false);
                    if (sActor instanceof Stockpile) {
                        //if standing on stockpile and holding fruit, add fruit to that stockpile
                        ((Stockpile) sActor).addFruit();
                    } else if (sActor instanceof Hoard) {
                        //if standing on hoard and holding fruit, add fruit to that hoard
                        ((Hoard) sActor).addFruit();
                    }
                }
                //turn gatherer 180 degrees
                clockLocation += 2;
                //loop back around clockwise array
                if (clockLocation > 3) {
                    clockLocation = clockLocation - 4;
                }

                //set new direction
                setDirection(clockwise[clockLocation]);
            }

            // if the gatherer is standing on a tree
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor instanceof Tree && !getCarrying()) {
                if (((Tree) sActor).getFruitCount() >= 1) {
                    //if there is fruit on the tree, remove the fruit and make the gatherer carry it
                    ((Tree) sActor).removeFruit();
                    setCarrying(true);

                    //turn the gatherer 180 degrees
                    clockLocation += 2;
                    //loop back around clockwise array
                    if (clockLocation > 3) {
                        clockLocation = clockLocation - 4;
                    }

                    //set new direction
                    setDirection(clockwise[clockLocation]);
                }
            }
            // if the gatherer is standing on a golden tree
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.goldenTree) && !getCarrying()) {
                    //golden tree has infinite fruit - don't need to deduct fruit from tree
                    setCarrying(true);

                    //turn the gatherer 180 degrees
                    clockLocation += 2;
                    //loop back around clockwise array
                    if (clockLocation > 3) {
                        clockLocation = clockLocation - 4;
                    }

                    //set new direction
                    setDirection(clockwise[clockLocation]);
            }
            // if the gatherer is standing on a up arrow
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signUp)) {
                setDirection("UP");
            }

            // if the gatherer is standing on a down arrow
            else if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signDown)) {
                setDirection("DOWN");
            }

            // if the gatherer is standing on a left arrow
            else if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signLeft)) {
                setDirection("LEFT");
            }

            // if the gatherer is standing on a right arrow
            else if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signRight)) {
                setDirection("RIGHT");
            }

            // if the gatherer is standing on a fence
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.fence)) {
                setActive(false);
                switch (direction) {
                    case "LEFT":
                        xPos += SQUARE_SIZE;
                        break;
                    case "RIGHT":
                        xPos -= SQUARE_SIZE;
                        break;
                    case "UP":
                        yPos += SQUARE_SIZE;
                        break;
                    case "DOWN":
                        yPos -= SQUARE_SIZE;
                        break;
                }
            }

            // if the gatherer is standing on a mitosis pool
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.mitosisPool)) {
                setDuplicate(1);
                //turn 90 degrees counter clockwise
                int counterClockLocation = clockLocation - 1;
                //loop back around clockwise array
                if (counterClockLocation < 0) {
                    counterClockLocation = counterClockLocation + 4;
                }

                //change direction of gatherer
                switch (clockwise[counterClockLocation]) {
                    case "UP":
                        newNonStationaryActors.add(new Gatherer(xPos, yPos, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "DOWN":
                        newNonStationaryActors.add(new Gatherer(xPos, yPos + SQUARE_SIZE, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "LEFT":
                        newNonStationaryActors.add(new Gatherer(xPos - SQUARE_SIZE, yPos, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "RIGHT":
                        newNonStationaryActors.add(new Gatherer(xPos + SQUARE_SIZE, yPos, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                }

                //turn 90 degrees clockwise
                int clockwiseLocation = clockLocation + 1;
                //loop back around clockwise array
                if (clockwiseLocation > 3) {
                    clockwiseLocation = clockwiseLocation - 4;
                }

                //change direction of gatherer
                switch (clockwise[clockwiseLocation]) {
                    case "UP":
                        newNonStationaryActors.add(new Gatherer(xPos, yPos - SQUARE_SIZE, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "DOWN":
                        newNonStationaryActors.add(new Gatherer(xPos, yPos + SQUARE_SIZE, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "LEFT":
                        newNonStationaryActors.add(new Gatherer(xPos - SQUARE_SIZE, yPos, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "RIGHT":
                        newNonStationaryActors.add(new Gatherer(xPos + SQUARE_SIZE, yPos, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                }
                break;
            }
        }
    }
}
