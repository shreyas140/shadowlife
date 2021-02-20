/**
 *  Represents a Tree - a type of stationary actor with specific abilities
 *  @author Shreya Sharma
 *  @studentNumber 910986
 *  @version 1.0
 */
public class Tree extends StationaryActor implements Printable, Removable {
    protected int fruitCount = 3;

    /**
     * The following constructor is used to declare the position of the Tree within shadowLife
     * @param xPos The x coordinate of the Tree
     * @param yPos The y coordinate of the Tree
     */
    public Tree(int xPos, int yPos){
        super(xPos, yPos, ActorType.tree);
    }

    /**
     * The following method returns the number of fruit in the Tree
     * @return int the number of fruit in the Tree
     */
    public int getFruitCount(){
        return fruitCount;
    }

    /**
     * The following method extends the Removable interface and removes a singular fruit from the Tree
     */
    @Override
    public void removeFruit() {
        this.fruitCount = fruitCount - 1;
    }

    /**
     * The following method returns a String representation of the amount of fruit in the Tree
     * @return String The amount of fruit in the Tree
     */
    @Override
    public String print(){
        return Integer.toString(fruitCount);
    }

}
