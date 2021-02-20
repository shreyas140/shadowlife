import java.util.ArrayList;
/**
 * Represents a non-stationary actor - a sub type of actor which has the ability to move around the world
 * @author Shreya Sharma
 * @studentNumber 910986
 * @version 1.0
 */
public abstract class NonStationaryActor extends Actor {
    //size of each square
    protected static final int SQUARE_SIZE = 64;

    /**
     * Location of file containing image of actor
     */
    public String filename;

    //all stationary actors
    protected ArrayList<StationaryActor> stationaryActors = new ArrayList<StationaryActor>();
    //all non-stationary actors
    protected ArrayList<NonStationaryActor> nonStationaryActors = new ArrayList<NonStationaryActor>();
    //all non-stationary actors which need to be added to the world
    protected ArrayList<NonStationaryActor> newNonStationaryActors = new ArrayList<NonStationaryActor>();

    //the time the actor moved last
    protected long lastMove = System.currentTimeMillis();

    //all possible directions that the actor could move in
    protected final static String[] clockwise = {"UP", "RIGHT", "DOWN", "LEFT"};
    //the index in the clockwise array that indicates the direction the actor is currently moving in
    protected int clockLocation;

    //flag determining whether duplicates of the actor need to be made (mitosis pool)
    protected int duplicate = 0;

    /**
     * The following constructor is used in the initial declaration of non-stationary actors within the main method
     * @param filename This is the String filepath to the image of the actor
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate for the actor
     */
    public NonStationaryActor (String filename, int xPos, int yPos){
        super(xPos, yPos);
        this.filename = filename;
    }

    /**
     * The following constructor is used in the constructor of ShadowLife and the associated update method - it contains
     * additional information to help the Non-Stationary Actor to determine if they are 'standing' on something
     * @param filename This is the String filepath to the image of the actor
     * @param xPos This is the x-coordinate of the actor
     * @param yPos This is the y-coordinate for the actor
     * @param nonStationaryActors This is an ArrayList of all non-stationary actors in the ShadowLife world
     * @param stationaryActors This is an ArrayList of all stationary actors in the ShadowLife world
     */
    public NonStationaryActor (String filename, int xPos, int yPos, ArrayList<NonStationaryActor> nonStationaryActors, ArrayList<StationaryActor> stationaryActors){
        super(xPos, yPos);
        this.filename = filename;
        this.stationaryActors = stationaryActors;
        this.nonStationaryActors = nonStationaryActors;
    }


    /**
     * The following abstract method is implemented by each non-stationary actor to determine how each actor moves
     * across the ShadowLife world
     */
    public abstract void move();

    /**
     * The following abstract method is implemented by each non-stationary actor to retrieve their active status - this
     * helps in determining whether all non-stationary actors have set the active attribute to false and whether the
     * window should close
     * @return boolean This returns the active status for each actor
     */
    public abstract boolean getActive();

    /**
     * The following abstract method is implemented by each non-stationary actor to re-set their 'duplicate' attribute
     * after the actor has stepped on a mitosis pool and the update program in ShadowLife has been notified that
     * duplicates of the actor must be generated
     * @param duplicate This is the value that the attribute duplicate should be set to -
     *                  1 if duplicates need to be made
     *                  0 if duplicates do not need to be made
     */
    public abstract void setDuplicate(int duplicate);

    /**
     * The following abstract method is implemented by each non-stationary actor to return the value of their duplicate
     * attribute
     * @return int This is the value of the duplicate attribute
     *                  1 if duplicates need to be made
     *                  0 if duplicates do not need to be made
     */
    public abstract int getDuplicate();

    /**
     * The following method returns the time the non-stationary moved at last
     * @return long The time in milliseconds
     */
    public long getLastMove() {
        return lastMove;
    }

    //reset the time the actor moved at last

    /**
     * The following method sets the time of the non-stationary actor moved at
     * @param newLastMove The time in milliseconds when the non-stationary actor moved
     */
    public void setLastMove(long newLastMove){
        this.lastMove = newLastMove;
    }

    /**
     * The following method returns the array list of all new non-stationary actors that need to be added to the
     * ShadowLife world as a result of the current actor's actions (stepped in a mitosis pool)
     * @return ArrayList<NonStationaryActor> This ArrayList contains all non-stationary actors that need to be added
     */
    public ArrayList<NonStationaryActor> getNewNonStationaryActors(){
        return newNonStationaryActors;
    }

}
