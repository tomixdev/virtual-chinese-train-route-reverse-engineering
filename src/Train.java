import java.util.Vector;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Train {
    private final String trainID;
    private final String trainName;   
    private final boolean isAirConditioned; 
    private char trainType; //'G', 'D', 'Z', 'T', 'K', 'Y', 'S', or 'B'(represents no prefix) 
    private final Vector<Station> stoppingStations;
    private final Vector<Integer> arrivalTimes;
    private final Vector<Integer> departureTimes;

    private Vector<Section> determinedPassingSections;//if not determined, just null.
   
    //Constructor 
    public Train (String trainID, String name, boolean isAirConditioned, Vector<Station> stoppingStations, Vector<Integer> arrivalTimes, Vector<Integer> departureTimes) {
	this.trainID = trainID;
	this.trainName = name;
	this.isAirConditioned = isAirConditioned;
	setTrainType();
	this.stoppingStations = stoppingStations;
	this.arrivalTimes = arrivalTimes;
	this.departureTimes = departureTimes;
    }
    
    //Private Method Attached to Constructor
    private void setTrainType() {
	char _char = this.trainName.toCharArray()[0];
	//if the first character of trainName is a number, that means that the train has no prefix.
	if((int)_char <= 64) {
	    trainType = 'B';
	}
	else{
	    trainType = _char;
	}
    }

    //===========================================================================================	
    //Getters
    public String getTrainName() {
	return trainName;
    } // getTrainName()

    //The following method converts vector to array as another student using this method does not know vector.
    public Section[] getDeterminedPassingSections() {
	return HelperMethodsClass.convertVectorToArray(determinedPassingSections);
    } // getDeterminedPassingSections()

    public Vector<Station> getStoppingStations() {
	return stoppingStations;
    } // getStoppingStations()

    public boolean getIsAirConditioned() {
	return isAirConditioned;
    } // getIsAirConditioned()

    public char getTrainType() {
	return trainType;
    } // getTrainType()

    //===============================================================================================
    //toString
    public String toString() {
        return "Train No. : " + trainName + " || trainType: " + trainType + " || airCondition: " + isAirConditioned + " || From " + stoppingStations.elementAt(0) + " to " + stoppingStations.elementAt(stoppingStations.size() - 1); 
    } // toString()

    //==============================================================================================
    //Setter
    public void setDeterminedPassingSections(Section[] determinedPassingSections) {
	try{
	    this.determinedPassingSections = HelperMethodsClass.convertArrayToVector(determinedPassingSections);
	    exportTrainPassingSectionInfoToFile();
	}
	catch (FileNotFoundException e) {
	    System.out.println("Error in setDeterminedPassingSections() in Train.java");
	    System.exit(1);
	}
    } // setDeterminedPassingSections()

    //===============================================================================================
    private void exportTrainPassingSectionInfoToFile() throws FileNotFoundException {
	PrintWriter outfile = new PrintWriter(trainID + " " + trainName + " " + "Route Info");

	outfile.println("Below is the determined route info for the following train:");
	outfile.println("TrainID: " + trainID + " || " + "Train Number: " + trainName);
	outfile.println("------------------------------------------------------------");

	for (int i = 0; i < determinedPassingSections.size(); i++) {
	    outfile.println(determinedPassingSections.elementAt(i));
	}

        outfile.close();
    } // exportTrainPassingSectionInfoToFile()
} // class Train

