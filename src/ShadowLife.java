//Bagel imports
import bagel.*;

//Reading in argument file
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

//Reading CSV
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * The simulation ShadowLife is generated using the code below
 * @author Shreya Sharma
 * @studentNumber 910986
 * @version 1.0
 */
public class ShadowLife extends AbstractGame {

    //User input saved
    private final int TICK_RATE;
    private final int MAX_TICKS;

    //ArrayList hold all (Non)Stationary Actors in World
    private ArrayList<StationaryActor> stationaryActors = new ArrayList<StationaryActor>();
    private ArrayList<NonStationaryActor> nonStationaryActors = new ArrayList<NonStationaryActor>();

    //Indicates the offset of the top left corner of each image
    private int TOP_X = 40;
    private int TOP_Y = 20;

    //Below variables are required for testing valid inputs and decide whether the program has finished simulating
    //Number of non-stationary actors (Gatherers & Thieves)
    private int noNonStationaryActors = 0;
    //Number of non-stationary actors (Gatherers & Thieves) who have active = false
    private int nonStationaryActorsNotActive = 0;
    //Number of elapsed ticks
    private int tickCount = 0;
    //Number of ticks per actor for all non-stationary actors
    private int tempTickCount = 0;

    //list of non-stationary actors that need to be added to the world
    private ArrayList<NonStationaryActor> tempNewNonStationary = new ArrayList<NonStationaryActor>();
    //list of non-stationary actors that need to be removed from the world
    private ArrayList<NonStationaryActor> deleteNSActorIndex = new ArrayList<NonStationaryActor>();

    //Background constant for all ShadowLife
    private Image background = new Image("res/images/background.png");
    //Font constant for all ShadowLife
    private Font font = new Font ("res/VeraMono.ttf", 20);

    /**
     * The following method updates the state of the game by executing around 60 times per second
     * @param input Provides access to input devices (keyboard and mouse) however is not used in the simulation
     */

    @Override
    public void update(Input input) {
        //draw background
        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        //draw each stationary actor on map
        for (StationaryActor sActor : stationaryActors) {
            Map map = sActor.getFileLocation();
            Image image = new Image((String) map.get(sActor.getActorType()));
            image.draw(sActor.xPos, sActor.yPos);
        }

        //draw each non-stationary actor and invoke method to move them in the world
        for (NonStationaryActor nSActor : nonStationaryActors) {
            //draw non-stationary actor
            Image image = new Image(nSActor.filename);
            image.draw(nSActor.xPos, nSActor.yPos);

            //if the correct amount of time has passed
            if (System.currentTimeMillis() - nSActor.getLastMove() > TICK_RATE && tickCount < MAX_TICKS) {
                //reset the last moved time and increment the number of ticks that have passed
                nSActor.setLastMove(System.currentTimeMillis());
                tempTickCount++;

                //move the actor
                nSActor.move();

                //if the actor has a flag get duplicate turned on (mitosis pool) add them to a list of new non-stationary actors
                if(nSActor.getDuplicate() == 1){
                    for(NonStationaryActor newNSA: nSActor.getNewNonStationaryActors()){
                        tempNewNonStationary.add(newNSA);
                    }
                    //resent the flag to zero
                    nSActor.setDuplicate(0);

                    //add the original non-stationary actor to the list of actors to delete
                    deleteNSActorIndex.add(nSActor);
                }
            }else if (tickCount >= MAX_TICKS){
                Window.close();
            }

        }

        //Find the tick count overall, not the cumulative per actor
        tickCount += tempTickCount/noNonStationaryActors;
        tempTickCount = 0;

        //delete the actors that have been flagged for deletion
        for(NonStationaryActor deleteActor: deleteNSActorIndex){
            nonStationaryActors.remove(deleteActor);
        }

        //add the actors that have been flagged for addition
        for(NonStationaryActor newActor: tempNewNonStationary){
            nonStationaryActors.add(newActor);
        }

        //clear the temporary arrays that hold the actors that need to be removed/added this iteration
        deleteNSActorIndex.clear();
        tempNewNonStationary.clear();

        //for trees, hoards and stockpiles, print a small integer indicating the number of fruit
        for (StationaryActor sActorPrintable : stationaryActors) {
            if (sActorPrintable instanceof Tree) {
                font.drawString(((Tree) sActorPrintable).print(), sActorPrintable.xPos - TOP_X, sActorPrintable.yPos - TOP_Y);
            } else if (sActorPrintable instanceof Hoard) {
                font.drawString(((Hoard) sActorPrintable).print(), sActorPrintable.xPos - TOP_X, sActorPrintable.yPos - TOP_Y);
            } else if (sActorPrintable instanceof Stockpile) {
                font.drawString(((Stockpile) sActorPrintable).print(), sActorPrintable.xPos - TOP_X, sActorPrintable.yPos - TOP_Y);
            }
        }

        //for each non-stationary actor, count the number where active == false and also check the elapsed ticks
        nonStationaryActorsNotActive = 0;
        //also count the number of non-stationary actors that exist in that update
        noNonStationaryActors = 0;
        for (NonStationaryActor nonStationaryActor : nonStationaryActors) {
            if (nonStationaryActor.getActive() == false) {
                nonStationaryActorsNotActive += 1;
            }
            noNonStationaryActors += 1;
        }


        //if all actors are not active, halt simulation and print required
        if (nonStationaryActorsNotActive == noNonStationaryActors) {
            //HALT Simulation
            Window.close();
            System.out.println(tickCount + " ticks");

            //check the amount of fruit at hoards and stockpiles
            for (StationaryActor stationaryActor : stationaryActors) {
                if (stationaryActor instanceof Hoard) {
                    System.out.println(((Hoard) stationaryActor).getFruitCount());
                } else if (stationaryActor instanceof Stockpile) {
                    System.out.println(((Stockpile) stationaryActor).getFruitCount());
                }
            }

            System.exit(0);

        //if the maximum ticks have elapsed stop the simulation
        }else if(tickCount == MAX_TICKS) {
            System.out.println("Timed out");
            System.exit(-1);
        }
    }


    protected ShadowLife(int tickRate, int maxTicks, ArrayList<StationaryActor> stationaryActors, ArrayList<NonStationaryActor> nonStationaryActors){

        //declare global variables from user input
        this.TICK_RATE = tickRate;
        this.MAX_TICKS = maxTicks;

        //create a new ArrayList where nonStationaryActors have access to all other actors in world
        ArrayList<NonStationaryActor> outputNonStationaryActor = new ArrayList<NonStationaryActor>();
        for (NonStationaryActor nSActor: nonStationaryActors){
            if (nSActor instanceof Gatherer){
                outputNonStationaryActor.add(new Gatherer(nSActor.xPos, nSActor.yPos, nonStationaryActors, stationaryActors));
            }else{
                outputNonStationaryActor.add(new Thief(nSActor.xPos, nSActor.yPos, nonStationaryActors, stationaryActors));
            }
        }

        //save lists of stationary & non-stationary actors
        this.nonStationaryActors = outputNonStationaryActor;
        this.stationaryActors = stationaryActors;

        //count number of non-stationary actors
        this.noNonStationaryActors = outputNonStationaryActor.size();

        //count number of actors who started off the game as active == false
        for (NonStationaryActor nonStationaryActor: outputNonStationaryActor){
            if(nonStationaryActor.getActive() == false){
                nonStationaryActorsNotActive += 1;
            }
        }
    }

    /**
     * The following main method reads in the contents of a file args.txt, saves crucial information to the
     * simulation (tick rate and maximum ticks) and reads in a .csv file (as specified in args.txt) to determine the
     * initial placement of actors on the world
     */
    public static void main(String[] args) {
        //Read input arguments from .txt file
        String[] inputArguments = argsFromFile();

        //Save all valid Actor Types
        String[] validActorTypes = {"Tree", "GoldenTree", "Stockpile", "Hoard", "Pad", "Fence", "SignUp", "SignDown", "SignLeft", "SignRight", "Pool", "Gatherer", "Thief"};

        try {

            //Check number of command line inputs are correct
            if(inputArguments.length != 3){
                System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
                throw new Exception();
            }

            //Declare global variables
            int tickRate = Integer.parseInt(inputArguments[0]);
            int maxTicks = Integer.parseInt(inputArguments[1]);

            //Save the user file input
            File world = new File(inputArguments[2].trim());

            //Check variables are within reasonable bound
            if (tickRate < 0 || maxTicks < 0){
                System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
                throw new Exception();
            }

            //Read in CSV file - use declaration of world File from earlier
            try(BufferedReader br = new BufferedReader(new FileReader(world))){
                String text = null;

                //Declare arrays for stationary and non-stationary actors
                ArrayList<StationaryActor> internalStationaryActors = new ArrayList<StationaryActor>();
                ArrayList<NonStationaryActor> internalNonStationaryActors = new ArrayList<NonStationaryActor>();

                //keep track of number of lines in file
                int lineNumber = 0;
                while((text = br.readLine()) != null) {
                    try {
                        //Split up actor information into array
                        String[] actorInfo = text.split(",");

                        //ensure that each line has the correct number of entries
                        if (actorInfo.length == 3) {
                            //ensure the Actors declared in the file are valid
                            if (Arrays.asList(validActorTypes).contains(actorInfo[0])) {

                                //If Thief or Gatherer, add to nonStationaryActors array
                                if (actorInfo[0].equals("Thief")) {
                                    internalNonStationaryActors.add(new Thief(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2])));
                                } else if (actorInfo[0].equals("Gatherer")) {
                                    internalNonStationaryActors.add(new Gatherer(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2])));
                                }

                                //Otherwise, add to StationaryActors array - some actors (e.g. Tree) are declared as sub-classes of StationaryActors
                                else if (actorInfo[0].equals("Tree")) {
                                    internalStationaryActors.add(new Tree(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2])));
                                } else if (actorInfo[0].equals("GoldenTree")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.goldenTree));
                                } else if (actorInfo[0].equals("Stockpile")) {
                                    internalStationaryActors.add(new Stockpile(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.stockpile));
                                } else if (actorInfo[0].equals("Hoard")) {
                                    internalStationaryActors.add(new Hoard(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2])));
                                } else if (actorInfo[0].equals("Pad")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.pad));
                                } else if (actorInfo[0].equals("Fence")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.fence));
                                } else if (actorInfo[0].equals("Pool")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.mitosisPool));
                                } else if (actorInfo[0].equals("SignUp")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.signUp));
                                } else if (actorInfo[0].equals("SignDown")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.signDown));
                                } else if (actorInfo[0].equals("SignLeft")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.signLeft));
                                } else if (actorInfo[0].equals("SignRight")) {
                                    internalStationaryActors.add(new StationaryActor(Integer.parseInt(actorInfo[1]), Integer.parseInt(actorInfo[2]), ActorType.signRight));
                                }
                                lineNumber++;

                            }else{
                                //This error will occur if there is an issue with one of the Actor Types in the file
                                System.out.println("error: in file \""+world+"\" at line <"+lineNumber+">");
                                throw new Exception();
                            }
                        }else{
                            //This error will occur if there are more/less than three pieces of information per line in the CSV
                            System.out.println("error: in file \""+world+"\" at line <"+lineNumber+">");
                            throw new Exception();
                        }
                    }  catch(Exception e){
                        //error reading lines in csv & ensuring their validity
                        System.exit(-1);
                    }
                }
                //Call ShadowLife after processing file & user input
                new ShadowLife(tickRate, maxTicks, internalStationaryActors, internalNonStationaryActors).run();
            }catch(Exception e){
                //error reading in file
                System.out.println("error: file \"<" + world+ ">\" not found\n");
                System.exit(-1);
            }

        }catch(Exception e){
            //error with command line arguments
            System.exit(-1);
        }

    }

    private static String[] argsFromFile() {
        //As given in assignment specification
        try {
            return Files.readString(Path.of("args.txt"), Charset.defaultCharset())
                    .split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
