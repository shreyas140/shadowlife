import java.util.AbstractMap;
import java.util.Map;
/**
 * Represents a stationary actor - a sub type of actor which does not have the ability to move around the world
 * @author Shreya Sharma
 * @studentNumber 910986
 * @version 1.0
 */
public class StationaryActor extends Actor {
    private ActorType actorType;

    //Contains all image locations for stationary actors
    private Map<ActorType, String> map = Map.ofEntries(
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.tree, "res/images/tree.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.goldenTree, "res/images/gold-tree.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.stockpile, "res/images/cherries.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.hoard, "res/images/hoard.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.pad, "res/images/pad.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.fence, "res/images/fence.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.mitosisPool, "res/images/pool.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.signUp, "res/images/up.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.signDown, "res/images/down.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.signLeft, "res/images/left.png"),
            new AbstractMap.SimpleEntry<ActorType, String>(actorType.signRight, "res/images/right.png")
    );

    /**
     * The following constructor is used to declare the position and type of stationary actor in the ShadowLife world
     * @param xPos The x position of the stationary actor
     * @param yPos The y position of the stationary actor
     * @param actorType The type of stationary actor - a enum value of ActorType
     */
    public StationaryActor(int xPos, int yPos, ActorType actorType){
        super(xPos, yPos);
        this.actorType = actorType;
    }

    /**
     * The following method gets the actor type (as an enum) of a stationary actor
     * @return ActorType This is the enum representation of the type of stationary actor
     */
    public ActorType getActorType(){
        return actorType;
    }

    /**
     * The following gets the FileLocation for all stationary actors
     * @return Map This is a map linking every ActorType to a String filename of where their images are located
     */
    public Map getFileLocation(){
        return map;
    }
}
