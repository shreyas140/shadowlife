/**
 *  Represents a Hoard - a type of stationary actor with specific abilities
 *  @author Shreya Sharma
 *  @studentNumber 910986
 *  @version 1.0
 */
public class Hoard extends StationaryActor implements Printable, Addable, Removable{
    int fruitCount = 0;

    /**
     * The following constructor is used to declare the position of the Hoard within shadowLife
     * @param xPos The x coordinate of the hoard
     * @param yPos The y coordinate of the hoard
     */
    public Hoard(int xPos, int yPos){
        super(xPos, yPos, ActorType.hoard);
    }

    /**
     * The following method returns the number of fruit in the hoard
     * @return int the number of fruit in the hoard
     */
    public int getFruitCount(){
        return fruitCount;
    }

    /**
     * The following method extends the Addable interface and adds a singular fruit to the hoard
     */
    @Override
    public void addFruit(){
        this.fruitCount += 1;
    }

    /**
     * The following method extends the Removable interface and removes a singular fruit from the hoard
     */
    @Override
    public void removeFruit(){
        this.fruitCount -= 1;
    }

    /**
     * The following method returns a String representation of the amount of fruit in the hoard
     * @return String The amount of fruit in the hoard
     */
    @Override
    public String print(){
        return Integer.toString(fruitCount);
    }
}
