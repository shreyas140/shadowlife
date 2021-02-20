import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Represents a Thief - a type of non-stationary actor with specific movements around the ShadowLife world
 *  @author Shreya Sharma
 *  @studentNumber 910986
 *  @version 1.0
 */
public class Thief extends NonStationaryActor{
    //thief information
    private String direction = "UP";
    private boolean carrying = false;
    private boolean consuming = false;
    private boolean active = true;

    /**
     * The following constructor is used in the initial declaration of Thieves within the main method
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate of the actor
     */
    public Thief(int xPos, int yPos){
        super("res/images/thief.png", xPos, yPos);
        clockLocation = Arrays.asList(clockwise).indexOf(direction);
    }

    /**
     * The following constructor is used in the constructor of ShadowLife and the associated update method - it contains
     * additional information to help the Thief to determine if they are 'standing' on something
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate of the actor
     * @param nonStationaryActors This is an ArrayList of all non-stationary actors in the ShadowLife world
     * @param stationaryActors This is an ArrayList of all stationary actors in the ShadowLife world
     */
    public Thief(int xPos, int yPos, ArrayList<NonStationaryActor> nonStationaryActors, ArrayList<StationaryActor> stationaryActors){
        super("res/images/thief.png", xPos, yPos, nonStationaryActors, stationaryActors);
        clockLocation = Arrays.asList(clockwise).indexOf(direction);
    }

    /**
     * The following constructor is used in the constructor of ShadowLife and the associated update method - it contains
     * additional information to help the Thief to determine if they are 'standing' on something and sets an
     * initial direction for the Thief that is (potentially) different than the default value
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate for the actor
     * @param direction This is the direction that is specified for the Thief
     * @param nonStationaryActors This is an ArrayList of all non-stationary actors in the ShadowLife world
     * @param stationaryActors This is an ArrayList of all stationary actors in the ShadowLife world
     */
    public Thief(int xPos, int yPos, String direction, ArrayList<NonStationaryActor> nonStationaryActors, ArrayList<StationaryActor> stationaryActors){
        super("res/images/thief.png", xPos, yPos, nonStationaryActors, stationaryActors);
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
    public boolean getActive(){
        return active;
    }

    private boolean getConsuming(){
        return consuming;
    }

    /**
     * The following method is implemented by each thief to return the value of their duplicate attribute
     * @return int The value of the duplicate attribute
     *                  1 if duplicates need to be made
     *                  0 if duplicates do not need to be made
     */
    public int getDuplicate(){
        return duplicate;
    }

    private void setActive(boolean newActive){
        this.active = newActive;
    }

    private void setDirection(String newDirection){
        this.direction = newDirection;
        this.clockLocation = Arrays.asList(clockwise).indexOf(this.direction);
    }

    private void setConsuming(boolean newConsuming){
        this.consuming = newConsuming;
    }

    private void setCarrying(boolean newCarrying){
        this.carrying = newCarrying;
    }

    /**
     * The following method is implemented by each thief to re-set their 'duplicate' attribute
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
     * The following method determines how each Thief moves across the ShadowLife world, including default movements
     * and specialised movements when the Thief interacts with other (Stationary) actors in the world
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
        for (NonStationaryActor nSActor : nonStationaryActors) {

            //if the thief is standing on a gatherer
            if (nSActor.xPos == this.xPos && nSActor.yPos == this.yPos && nSActor instanceof Gatherer) {
                //turn the thief 270 degrees
                clockLocation += 3;
                //loop back around clockwise array
                if (clockLocation > 3) {
                    clockLocation = clockLocation - 4;
                    //set new direction
                }

                //set new direction for thief
                setDirection(clockwise[clockLocation]);
            }
        }

        for (StationaryActor sActor : stationaryActors) {
            //if the thief is standing on a fence
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

            //if the thief is standing on a mitosis pool
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.mitosisPool)) {
                this.duplicate = 0;

                //turn 90 degrees counter clockwise
                int counterClockLocation = clockLocation - 1;
                //loop back around clockwise array
                if (counterClockLocation < 0) {
                    counterClockLocation = counterClockLocation + 4;
                }
                switch (clockwise[counterClockLocation]) {
                    case "UP":
                        newNonStationaryActors.add(new Thief(xPos, yPos - SQUARE_SIZE, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "DOWN":
                        newNonStationaryActors.add(new Thief(xPos, yPos + SQUARE_SIZE, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "LEFT":
                        newNonStationaryActors.add(new Thief(xPos - SQUARE_SIZE, yPos, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "RIGHT":
                        newNonStationaryActors.add(new Thief(xPos + SQUARE_SIZE, yPos, clockwise[counterClockLocation], nonStationaryActors, stationaryActors));
                        break;
                }

                //turn thief 90 degrees clockwise
                int clockwiseLocation = clockLocation + 1;
                
                //loop back around clockwise array
                if (clockwiseLocation > 3) {
                    clockwiseLocation = clockwiseLocation - 4;
                }

                //create a new thief and add it
                switch (clockwise[clockwiseLocation]) {
                    case "UP":
                        newNonStationaryActors.add(new Thief(xPos, yPos - SQUARE_SIZE, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "DOWN":
                        newNonStationaryActors.add(new Thief(xPos, yPos + SQUARE_SIZE, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "LEFT":
                        newNonStationaryActors.add(new Thief(xPos - SQUARE_SIZE, yPos, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                    case "RIGHT":
                        newNonStationaryActors.add(new Thief(xPos + SQUARE_SIZE, yPos, clockwise[clockwiseLocation], nonStationaryActors, stationaryActors));
                        break;
                }
                break;
            }
            //if the thief is standing on an up sign
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signUp)) {
                setDirection("UP");
            }

            //if the thief is standing on a down sign
            else if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signDown)) {
                setDirection("DOWN");
            }

            //if the thief is standing on a left sign
            else if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signLeft)) {
                setDirection("LEFT");
            }

            //if the thief is standing on a right sign
            else if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.signRight)) {
                setDirection("RIGHT");
            }

            //if the thief is standing on a pad
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.pad)) {
                setConsuming(true);
            }

            //if the thief is standing on a tree (and not carrying any fruit)
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor instanceof Tree && !getCarrying()) {
                if (((Tree) sActor).getFruitCount() >= 1) {
                    //if fruit on the tree, pick up fruit from tree
                    ((Tree) sActor).removeFruit();
                    setCarrying(true);
                }
            }

            //if the thief is standing on a golden tree (and not carrying any fruit)
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor.getActorType().equals(ActorType.goldenTree) && !getCarrying()) {
                    //golden tree has infinite fruit - don't need to deduct fruit from tree
                    setCarrying(true);
            }

            //if the thief is standing on a hoard
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor instanceof Hoard) {
                if (getConsuming()) {
                    setConsuming(false);
                    if (!getCarrying()) {
                        if (((Hoard) sActor).getFruitCount() >= 1) {
                            //if there is fruit in the hoard, pick up a fruit from the hoard
                            setCarrying(true);
                            ((Hoard) sActor).removeFruit();

                        } else {
                            //turn thief 90 degrees
                            clockLocation += 1;
                            //loop back around clockwise array
                            if (clockLocation > 3) {
                                clockLocation = clockLocation - 4;
                            }

                            //set new direction for thief
                            setDirection(clockwise[clockLocation]);
                        }
                    }
                } else if (getCarrying()) {
                    //if currently carrying fruit, add the fruit to the hoard
                    setCarrying(false);
                    ((Hoard) sActor).addFruit();

                    //turn thief 90 degrees
                    clockLocation += 1;
                    //loop back around clockwise array
                    if (clockLocation > 3) {
                        clockLocation = clockLocation - 4;
                    }

                    //set new direction for thief
                    setDirection(clockwise[clockLocation]);
                }
            }

            //if the thief is standing on a stockpile
            if (sActor.xPos == this.xPos && sActor.yPos == this.yPos && sActor instanceof Stockpile) {
                if (!getCarrying()) {
                    if (((Stockpile) sActor).getFruitCount() >= 1) {
                        //if the stockpile has fruit on it, pick up a fruit from the pile
                        setCarrying(true);
                        setConsuming(false);
                        ((Stockpile) sActor).removeFruit();

                        //turn thief 90 degrees
                        clockLocation += 1;
                        //loop back around clockwise array
                        if (clockLocation > 3) {
                            clockLocation = clockLocation - 4;
                        }

                        //set new direction for thief
                        setDirection(clockwise[clockLocation]);
                    }
                } else {
                    //turn the thief 90 degrees
                    clockLocation += 1;
                    //loop back around clockwise array
                    if (clockLocation > 3) {
                        clockLocation = clockLocation - 4;
                    }

                    //set new direction for thief
                    setDirection(clockwise[clockLocation]);
                }
            }
        }
    }
}
