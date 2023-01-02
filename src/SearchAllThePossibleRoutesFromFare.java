import java.util.Hashtable;
import java.util.Vector;
import java.io.IOException;
import java.util.Stack;

//class FacilityNode============================================================================================================================================================
class FacilityNode {
    FacilityNode previous;
    Facility current;

    Station nextStoppingStation;

    Vector<Facility> visitedFacilitiesIncludingCurrent;

    int totalDistanceSoFar; // distance passed so far including sections in the Guangzhou Jurisdiction area and on Xizang Line
    int passedGuangzhouJurisdictionRouteDistance; //cumulative distance so far in the Guangzhou Jurisdiction area which has a different fare calculation formula
    int passedXizangLineDistanceSoFar; //cumulative distance so far on Xizang Line which has a special fare calculation formula

    //[For Future Development] int passedHuRongLineDistanceSoFar; 
    //[For Future Development] int passedDachengLineDistanceSoFar; 
} // class FacilityNode

//===============================================================================================================================================================================
//class SearchAllThePossibleRoutesFromFare=======================================================================================================================================
public class SearchAllThePossibleRoutesFromFare {

    private static final int DEFAULT_SEAT_CLASS = 4;
    private static final boolean DEFAULT_AIR_CONDITIONER = true;
    private static final char DEFAULT_TRAIN_TYPE = 'K';

    private Station fromWhere; //The starting point of route search
    private Station toWhere; //The ending point of route search
    private Vector<Station> stoppingStations;//The stopping stations include the beginning and ending stations
    private double fare; 
    private char trainType; //G train,  D train, Z train, T train, K train, Y train, B train (no character train)
    private int seatClass; //0 = First Class, 1 = Second Class, 2 = Soft-Sleeper Class, 3 = Soft-Seat Class, 4 = Hard-Sleeper Class, 5 = Hards-Seat Class
    private boolean isAirConditioned;

    private Vector<FacilityNode> lastFacilityNodesOfPossibleFacilityCombinations; //This data structure stores last nodes of all the possible sequences of nodes that represent possible routes.
    private Vector<Vector<Facility>> possibleFacilityCombinations; // This data structure stores sequences of an alternation between section and station. Each sequence represents one possible route. 
    private Vector<Vector<Section>> possibleSectionCombinations; //This data structure stores sequences of sections. Each sequence represents one possible route.
    private Vector<Station[]> locationsOfUndeterminedSections; //This data structure stores information about between which stations the route is unidentified.

    //commonMethodAmongAllConstructors()=============================================================================================================================================
    //The following method is called by all the 12 constructors written at the bottom of this program.
    private void commonMethodAmongAllConstructors(Station fromWhere, Station toWhere, double fare, boolean isAirConditioned, char trainType, int seatClass, Vector<Station> stoppingStations) {

        this.fromWhere = fromWhere;
	this.toWhere = toWhere;
	this.stoppingStations = stoppingStations;
	this.fare = fare;
	this.trainType = trainType;
	this.seatClass = seatClass;
	this.isAirConditioned = isAirConditioned;

	/*If the user input of stations is correct, then set the following variables:
	//lastFacilityNodesOfPossibleFacilityCombinations, 
	//possibleFacilityCombinations, 
	//possibleSectionCombinations, and
	//locationsOfUndeterminedSections
	*/
        if(stoppingStations.contains(fromWhere) && stoppingStations.contains(toWhere)) { 
	    setLastFacilityNodesOfPossibleFacilityCombinations();
	    setPossibleFacilityCombinations();
	    setPossibleSectionCombinations();
	    setLocationsOfUndeterminedSections();
	}
	//If the user input of station is wrong, set those four vaiables null so that an error message is displayed to the user.
	else {
            lastFacilityNodesOfPossibleFacilityCombinations = null;
            possibleFacilityCombinations = null;
            possibleSectionCombinations = null;
            locationsOfUndeterminedSections = null;
        }
    } // commonMethodAmongAllConstructors()

    //================================================================================================================================================================
    //Getter 1
    public Section[][] getPossibleSectionCombinations () {
	if(possibleSectionCombinations == null || possibleSectionCombinations.size() == 0) { //if no possible route is found
	    return null;
	}
	return HelperMethodsClass.convertVectorOfVectorToMultidimentionalArray(possibleSectionCombinations);
    } // getPossibleSectionCombinations()

    //===================================================================================================================================================================
    //Getter 2
    public Station[][] getLocationsOfUndeterminedSections() {
	if(locationsOfUndeterminedSections == null || locationsOfUndeterminedSections.size() == 0) {
	    return null;
	}
	return HelperMethodsClass.convertVectorOfStationArrayToMultidimentionalArray(locationsOfUndeterminedSections);
    } // getLocationsOfUndeterminedSections()

    //====================================================================================================================================================================
    //This method stores possible routes into the field "lastFacilityNodesOfPossibleFacilityCombinations"
    private void setLastFacilityNodesOfPossibleFacilityCombinations() {

	int originalNumberOfTimesCanBePassedOfOrigin = fromWhere.getNumberOfTimesCanBePassed();
	if(fromWhere == toWhere) {
	    fromWhere.setNumberOfTimesCanBePassed_value2();
	}
	
	this.lastFacilityNodesOfPossibleFacilityCombinations = new Vector<FacilityNode>(); 
    
	FacilityNode firstFacilityNode = new FacilityNode();
	firstFacilityNode.previous = null;
	firstFacilityNode.current = fromWhere; 
	firstFacilityNode.nextStoppingStation = nextStoppingStationOf(fromWhere);
	firstFacilityNode.totalDistanceSoFar = fromWhere.getDistance();//This should be 0.
	firstFacilityNode.passedGuangzhouJurisdictionRouteDistance = 0;
	firstFacilityNode.passedXizangLineDistanceSoFar = 0;

	firstFacilityNode.visitedFacilitiesIncludingCurrent = new Vector<Facility>(); 
	if(fromWhere != toWhere) {
	    firstFacilityNode.visitedFacilitiesIncludingCurrent.add(fromWhere);
	}	      

	addFacilityNode(firstFacilityNode);

	fromWhere.setNumberOfTimesCanBePassed(originalNumberOfTimesCanBePassedOfOrigin);
    } // setLastFacilityNodesOfPossibleFacilityCombinations()

    //==========================================================================================================================================================
    //This method adds a facility node recursively as long as adding node could possibly lead to discovery of a possible route.
    private void addFacilityNode (FacilityNode a){

	if(needToStopAddingNodeOrNot(a)) {
	    return;
	}

        Vector<Facility> nextFacilitiesToGo = findNextFacilitiesToGo(a);

	//If there is no facility curentnode can go, then return.
	if(nextFacilitiesToGo.isEmpty()) { 
	    return;
	}

	for(int i = 0; i < nextFacilitiesToGo.size(); i++) {

	    FacilityNode aNextNode = new FacilityNode();
	    Facility aNextFacility = nextFacilitiesToGo.elementAt(i);

	    aNextNode.previous = a;
	    aNextNode.current = aNextFacility;
	    aNextNode.visitedFacilitiesIncludingCurrent = HelperMethodsClass.copyVectorFacility(a.visitedFacilitiesIncludingCurrent);
	    aNextNode.visitedFacilitiesIncludingCurrent.add(aNextFacility);
	    aNextNode.totalDistanceSoFar = a.totalDistanceSoFar + aNextFacility.getDistance();

	    if(aNextNode.current instanceof Station) {
		if(a.nextStoppingStation == (Station)aNextNode.current) {

		    aNextNode.nextStoppingStation = nextStoppingStationOf(a.nextStoppingStation);
		}
		else{
		    aNextNode.nextStoppingStation = (Station)a.nextStoppingStation;
		}

		    aNextNode.passedGuangzhouJurisdictionRouteDistance = a.passedGuangzhouJurisdictionRouteDistance;
		    aNextNode.passedXizangLineDistanceSoFar = a.passedXizangLineDistanceSoFar;
	    }

	    if(aNextNode.current instanceof Section) {

                aNextNode.nextStoppingStation = (Station) a.nextStoppingStation;

                Section nextSection = (Section)aNextNode.current;

		if(nextSection.getBelongingJurisdiction().equals("広州")) {//[For Future Development] In the real data, the condition needs to be modified. 
		    aNextNode.passedGuangzhouJurisdictionRouteDistance = a.passedGuangzhouJurisdictionRouteDistance + aNextFacility.getDistance();
		}
		else {
		    aNextNode.passedGuangzhouJurisdictionRouteDistance = a.passedGuangzhouJurisdictionRouteDistance;
		}

		if(nextSection.getBelongingRouteString().equals("Xizang")){ //[For Future Development] In the real data, the condition needs to be modified.
		    aNextNode.passedXizangLineDistanceSoFar = a.passedXizangLineDistanceSoFar + aNextFacility.getDistance();
		}
		else {
		    aNextNode.passedXizangLineDistanceSoFar = a.passedXizangLineDistanceSoFar;
		}
	    }

	    addFacilityNode(aNextNode);
	}
    } // addFacilityNode()

    //============================================================================================================================================================
    //This method judges whether the route search needs to add one more node to the last added node.
    private boolean needToStopAddingNodeOrNot(FacilityNode lastAddedNode) {

	//If current facilityNode.current is a section, we cannot end our search, as a search always ends in a station.
	if(lastAddedNode.current instanceof Section) {
	    return false;
	}

	//if lastAddedNode.previous == null, then it means that lastAddedNode.current is the departing station. 
        if(lastAddedNode.previous == null) {
            return false;
        }

	double currentFare = FareCalculationClass.calculateFare(lastAddedNode.totalDistanceSoFar, lastAddedNode.passedGuangzhouJurisdictionRouteDistance, lastAddedNode.passedXizangLineDistanceSoFar, trainType, seatClass, isAirConditioned);

	if((Station)lastAddedNode.current == toWhere && (Station)lastAddedNode.current == lastAddedNode.previous.nextStoppingStation) {
	    if(currentFare == this.fare) { // If currentFare == this.fare, then the search needs to stop because a route is found.
		lastFacilityNodesOfPossibleFacilityCombinations.add(lastAddedNode);
		return true;
	    }
	    else { //If current != this fare, then search also needs to stop because the search reached the destination without fulfilling the fare condition.
		return true;
	    }
	}

	//If currentFare > this.fare, then the search needs to stop because the fare for the searched route exceeded the user-input fare.
	if(currentFare > this.fare) {
	    return true;
	}

	Station currentStation = (Station) lastAddedNode.current;
	//If two conditions below are true, then the searchreached a stopping station which is not the next stopping station.
	if(this.stoppingStations.contains(currentStation) && currentStation != lastAddedNode.previous.nextStoppingStation) { 
	    return true;
	}

	return false;
    } // needToStopAddingNodeOrNot()

    //========================================================================================================================================================================
    //This method tells facilities the current facility of the last added node can go. 
    private Vector<Facility> findNextFacilitiesToGo (FacilityNode lastAddedNode) {
        Vector<Facility> toReturn = new Vector<Facility>();

	if(lastAddedNode.current instanceof Section) { //If next is Station, then do the following things.
	    Section currentSection = (Section) lastAddedNode.current;
	    if(howManyTimesHasAFacilityBeenPassed(lastAddedNode.visitedFacilitiesIncludingCurrent, (Facility)(currentSection.getEndingStation())) <= currentSection.getEndingStation().getNumberOfTimesCanBePassed() - 1) {
		toReturn.add(currentSection.getEndingStation());
	    }
	}

	if(lastAddedNode.current instanceof Station) { //If next is Section, then do the following things.

	    //When this method is read for the first time of the search, lastAddedNode should be an instance of Station, and its previous node should be null.
	    //The following if-condition deals with the case of the node whose current is the starting station.
            if(lastAddedNode.previous == null) { 
                   Vector<Section> sections = fromWhere.getSectionsStartingFromThisStation();
                   for(int i = 0; i < sections.size(); i ++) {
		       Section aSection = sections.elementAt(i);
		       if(aSection.isThisTypeOfTrainRunnable(this.trainType)){
			   toReturn.add(aSection);
		       }
                   }
                   return toReturn;
            }
  
	    //To determine sections the current (which is Station) of the last facility node can go, the route search needs to retrieve the neighbor sections of the previous section.
	    //This is because the route search can or cannot go to the twin section (the section who shares the two edges but has the other direction) of the last section, depending on whether the last section is returnable or not.
	    Section previousSection = (Section) lastAddedNode.previous.current;

	    if(previousSection.getNeighborSectionsOnEndingSide() == null) {
		previousSection.setNeighborSectionsOnEndingSide();
	    }

	    Vector<Section> sections = previousSection.getNeighborSectionsOnEndingSide();

	    for(int i = 0; i < sections.size(); i++) {
		Section aSection = sections.elementAt(i);
		//If aSection is not passed yet and the train type of this route search can run on aSection, then aSection is one of the next facilities to go.
		if(!lastAddedNode.visitedFacilitiesIncludingCurrent.contains((Facility)aSection) && aSection.isThisTypeOfTrainRunnable(this.trainType)) {
		    toReturn.add(aSection);
		}
	    }
	}

	return toReturn;
    } // findNextFacilitiesToGo()

    //=================================================================================================================================================================
    //This method takes a station in stoppingStations as an argument and returns the next stopping station in stoppingStations.
    private Station nextStoppingStationOf(Station sta) {
	if(stoppingStations.indexOf(sta) == stoppingStations.size() - 1) {
            return null;
	}
	return stoppingStations.elementAt(stoppingStations.indexOf(sta) + 1);
    } //nextStoppingStationOf()

    //========================================================)==================================================================================================
    //This method counts how many of a facility (aFailicy) a facility vector (facilityVector) contains. 
    private int howManyTimesHasAFacilityBeenPassed(Vector<Facility> facilityVector, Facility aFacility) {
	int count = 0;
	for(int i = 0; i < facilityVector.size(); i++) {
	    if(facilityVector.elementAt(i) == aFacility) {
		count++;
	    }
	}
	return count;
    } //howManyTimesHasAFacilityBeenPassed()

    //===============================================================================================================================================
    //The method below sets up possibleFacilityCombinations by:
    //1. store all the nodes of every possible route from the bottom (toWhere) to the top(fromWhere) into a stack
    //2. pop each element in the stack and add it to possibleFacilityCombinations, until the stack becomes empty.
    private void setPossibleFacilityCombinations() {

	possibleFacilityCombinations = new Vector<Vector<Facility>>(lastFacilityNodesOfPossibleFacilityCombinations.size());

	for(int i = 0; i < lastFacilityNodesOfPossibleFacilityCombinations.size(); i++) {
	    MinatoStack<Facility> temp = new MinatoStack<Facility>();
	    FacilityNode currentNode = lastFacilityNodesOfPossibleFacilityCombinations.elementAt(i);
	    while(currentNode != null) {
		temp.push(currentNode.current);
		currentNode = currentNode.previous;
	    }

	    possibleFacilityCombinations.add(i, new Vector<Facility>());
	    
	    while(!temp.empty()){
		possibleFacilityCombinations.elementAt(i).add(temp.pop());
	    }
	    
	}
    } //setPossibleFacilityCombinations()
    
    //==============================================================================================================================================
    //This method below adds possible routes represented by sequences of sections into possibleSectionCombination.
    private void setPossibleSectionCombinations() {

	possibleSectionCombinations = new Vector<Vector<Section>>(possibleFacilityCombinations.size());

	for(int i = 0; i < possibleFacilityCombinations.size(); i++) {
	    possibleSectionCombinations.add(i, new Vector<Section>());  
	    for(int j = 0; j < possibleFacilityCombinations.elementAt(i).size(); j++) {
		if(possibleFacilityCombinations.elementAt(i).elementAt(j) instanceof Section) {
		    possibleSectionCombinations.elementAt(i).add((Section)possibleFacilityCombinations.elementAt(i).elementAt(j));
		}
	    }
	}
    } //setPossibleSectionCombinations()

    //===============================================================================================================================================
    private void setLocationsOfUndeterminedSections() {
	if(possibleFacilityCombinations == null || possibleFacilityCombinations.size() == 0 || possibleFacilityCombinations.size() == 1) {
	    locationsOfUndeterminedSections = null;
	    return;
	}

	locationsOfUndeterminedSections = new Vector<Station[]>(); 

	Station[] test = new Station[2];
	test[0] = World.getStationsInJapaneseHashTable().get("minato");
	test[1] = World.getStationsInJapaneseHashTable().get("robert");
	locationsOfUndeterminedSections.add(test);
    } // setLocationsOfUndeterminedSections()

    //===============================================================================================================================================
    //The method below is for debugging.
    private void debug_PrintOutInfomationOfThisNode(FacilityNode a) {
	System.out.println("-------------------------CurrentFacility at the top of addFacilityNodeUntilReachesACertainFare() is-----------------------------------------------------" + a.current);
	System.out.print("-----------@Search Class VisitedSections is: ");
	for(int i = 0; i < a.visitedFacilitiesIncludingCurrent.size(); i++) {
	    if(a.visitedFacilitiesIncludingCurrent.elementAt(i) instanceof Section) {
		System.out.print((Section)a.visitedFacilitiesIncludingCurrent.elementAt(i) + " || ");
	    }
	}
	System.out.println();
	System.out.println("-----------@Search Class a.nextStoppingStation: " + a.nextStoppingStation);
	System.out.println("-----------@Search Class a.totalDistanceSofar: " + a.totalDistanceSoFar + " || passedGuangzhouDistance: " + a.passedGuangzhouJurisdictionRouteDistance + " || passedXizangLineDistanceSoFar: " + a.passedXizangLineDistanceSoFar);
	System.out.println("-----------@Search class currentFare is now: " + FareCalculationClass.calculateFare(a.totalDistanceSoFar, a.passedGuangzhouJurisdictionRouteDistance, a.passedXizangLineDistanceSoFar, trainType, seatClass, isAirConditioned));
    } // debug_PrintOutInfomationOfThisNode()
    
    //=====================================================================================================================================================
    //Constructors 
    //12 constructors that represent 12 argument patterns. These constructors lead to commonMethodAmongAllConstructor()
    //argument pattern 1
    public SearchAllThePossibleRoutesFromFare (String fromWhereString, String toWhereString, double fare) {
	this(fromWhereString, toWhereString, fare, DEFAULT_AIR_CONDITIONER, DEFAULT_TRAIN_TYPE, DEFAULT_SEAT_CLASS);
    }
    //argument pattern 2
    public SearchAllThePossibleRoutesFromFare (String fromWhereString, String toWhereString, double fare, boolean isAirConditioned) {
        this(fromWhereString, toWhereString, fare, isAirConditioned, DEFAULT_TRAIN_TYPE, DEFAULT_SEAT_CLASS);
    }
    //argument pattern 3
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, char trainType) {
        this(fromWhereString, toWhereString, fare, DEFAULT_AIR_CONDITIONER, trainType, DEFAULT_SEAT_CLASS);
    }
    //argumnent pattern 4
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, int seatClass) {
        this(fromWhereString, toWhereString, fare, DEFAULT_AIR_CONDITIONER, DEFAULT_TRAIN_TYPE, seatClass);
    }
    //argument pattern 5
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, boolean isAirConditioned, char trainType) {
        this(fromWhereString, toWhereString, fare, isAirConditioned, trainType, DEFAULT_SEAT_CLASS);
    }
    //argument pattern 6
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, char trainType, int seatClass) {
        this(fromWhereString, toWhereString, fare, DEFAULT_AIR_CONDITIONER, trainType, seatClass);
    }
    //argument pattern 7
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, boolean isAirConditioned, int seatClass) {
        this(fromWhereString, toWhereString, fare, isAirConditioned, DEFAULT_TRAIN_TYPE, seatClass);
    }
    //argument pattern 8 
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, boolean isAirConditioned, char trainType, int seatClass) {
	Station station1 = World.getStationsInJapaneseHashTable().get(fromWhereString); 
	Station station2 = World.getStationsInJapaneseHashTable().get(toWhereString);
	Vector<Station> stoppingStationsVector = new Vector<Station>(2);
	stoppingStationsVector.add(station1); 
        stoppingStationsVector.add(station2);
	commonMethodAmongAllConstructors(station1, station2, fare, isAirConditioned, trainType, seatClass, stoppingStationsVector);
    }
    //argument pattern 9
    public SearchAllThePossibleRoutesFromFare(double fare, String trainName) {
	this(fare, DEFAULT_SEAT_CLASS, trainName);
    }
    //argument pattern 10
    public SearchAllThePossibleRoutesFromFare(double fare, int seatClass, String trainName) {
	Train trainObject = World.getTrainsHashTable().get(trainName);

	Vector<Station> stoppingStationsVector = trainObject.getStoppingStations();//Originally COPY!!!!!!!!!!!!!!
	Station station1 = stoppingStationsVector.elementAt(0);
	Station station2 = stoppingStationsVector.elementAt(stoppingStationsVector.size() - 1);

	commonMethodAmongAllConstructors(station1, station2, fare, trainObject.getIsAirConditioned(), trainObject.getTrainType(), seatClass, stoppingStationsVector);
    }
    //argument Pattern 11
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, String trainName) {
	this(fromWhereString, toWhereString, fare, DEFAULT_SEAT_CLASS, trainName);
    }
    //Argument Pattern 12
    public SearchAllThePossibleRoutesFromFare(String fromWhereString, String toWhereString, double fare, int seatClass, String trainName) {
	Station station1 = World.getStationsInJapaneseHashTable().get(fromWhereString);
	Station station2 = World.getStationsInJapaneseHashTable().get(toWhereString);
	Train trainObject = World.getTrainsHashTable().get(trainName);

	Vector<Station> allStoppingStationsOfThisTrain = trainObject.getStoppingStations();
	Vector<Station> stoppingStationsVector = HelperMethodsClass.allTheElementsBetweenTwoElementsInVector(allStoppingStationsOfThisTrain, station1, station2);

	commonMethodAmongAllConstructors(station1, station2, fare, trainObject.getIsAirConditioned(), trainObject.getTrainType(), seatClass, stoppingStationsVector);
    }
} //class SearchAllThePossibleRoutesFromFare



