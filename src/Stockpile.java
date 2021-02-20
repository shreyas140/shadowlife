/**
 *  Represents a Stockpile - a type of stationary actor with specific abilities
 *  @author Shreya Sharma
 *  @studentNumber 910986
 *  @version 1.0
 */
public class Stockpile extends StationaryActor implements Printable, Addable, Removable{
    int fruitCount = 0;

    /**
     * The following constructor is used to declare the position of the Stockpile within shadowLife
     * @param xPos The x coordinate of the stockpile
     * @param yPos The y coordinate of the stockpile
     */
    public Stockpile(int xPos, int yPos, ActorType actorType){
        super(xPos, yPos, actorType);
    }

    /**
     * The following method returns the number of fruit in the stockpile
     * @return int the number of fruit in the stockpile
     */
    public int getFruitCount(){
        return fruitCount;
    }

    /**
     * The following method extends the Addable interface and adds a singular fruit to the stockpile
     */
    @Override
    public void addFruit(){
        this.fruitCount += 1;
    }

    /**
     * The following method extends the Removable interface and removes a singular fruit from the stockpile
     */
    @Override
    public void removeFruit(){
        this.fruitCount -= 1;
    }

    /**
     * The following method returns a String representation of the amount of fruit in the stockpile
     * @return String The amount of fruit in the stockpile
     */
    @Override
    public String print(){
        return Integer.toString(fruitCount);
    }
}
