/**
 * Represents an actor
 * @author Shreya Sharma
 * @studentNumber 910986
 * @version 1.0
 */
public class Actor {

    protected int xPos;
    protected int yPos;

    /**
     * The following is a constructor for the Actor class - at a very minimum every actor must be located somewhere
     * on the ShadowLife world (with an x and y coordinate).
     * @param xPos The x coordinate of the actor
     * @param yPos The y coordinate of the actor
     */
    public Actor(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

}
