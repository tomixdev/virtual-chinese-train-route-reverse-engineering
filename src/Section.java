import java.util.Vector;

public class Section extends Facility {

    private final Station startingStation;
    private final Station endingStation;

    private final String belongingRouteString; 
    private final Route belongingRouteObject;

    private final char direction; // 'A' corresponding to UP in the real world, or 'B' corresponding to DOWN in the real world.
    private final boolean isReturnable; //whether or not a train can run the twin section of this section after passing this section. The twin section is the section that shares the two edge stations but has the other direction.
    private final boolean isHighSpeed; //whether high-speed trains can run this section or not
    private final boolean isNormalSpeed; //whether normal-speed trains can run this section or not

    private final String belongingJurisdiction;

    private Vector<Section> neighborSectionsOnEndingSide;

    //Constructor
    public Section (int _distance, Station station1, Station station2, String belongingRouteName, char _direction, boolean _returnableOrNot, boolean highSpeedOrNot, boolean passangerOrNot, String belongingJurisdiction) {

	super.setDistance(_distance);
	this.startingStation = station1;
	this.endingStation = station2;
	this.belongingRouteString = belongingRouteName;
	this.belongingRouteObject = World.getRoutesHashTable().get(belongingRouteName);
	this.direction = _direction;
	this.isReturnable = _returnableOrNot;
	this.isHighSpeed = highSpeedOrNot;
	this.isNormalSpeed = passangerOrNot;
	this.belongingJurisdiction = belongingJurisdiction;

	station1.addSectionsStartingFromThisStation(this);
	station2.addSectionsEndingInThisStation(this);
    }

    //=============================================================================================================
    //Getters
    public Station getStartingStation() {
	return startingStation;
    }
    //getStartingStation()

    public Station getEndingStation() {
	return endingStation;
    } 
    //getEndingStation()

    public boolean getIsReturnable() {
	return isReturnable;
    } //getIsReturnable()

    public String getBelongingJurisdiction() {
	return belongingJurisdiction;
    } //getBelongingJurisdiction()

    public Route getBelongingRouteObject() {
	return belongingRouteObject;
    } //getBelongingRouteObject

    public String getBelongingRouteString() {
	return belongingRouteString;
    } //getBelongingRouteString()

    public Vector<Section> getNeighborSectionsOnEndingSide () {
	return neighborSectionsOnEndingSide;
    } //getNeighborSectionsOnEndingSide()

    public boolean getIsHighSpeed() {
	return isHighSpeed;
    } // getIsHighSpeed()

    //=====================================================================================================================================================
    //toString
    public String toString() {
	return startingStation.getJapaneseStationName() + " " + belongingRouteString +  " " + direction + " " + endingStation.getJapaneseStationName() + " " + super.getDistance();
    } // toString()

    //============================================================================================================================================================== 
    //This method sets neighbor sections from the ending station of this section. 
    //If this section is designated as returnable, then all the sections starting from the ending station of this section are neighbor sections of this section. 
    //Otherwise, the twin section of this section which shares the two edge stations but has the other direction should be excluded from neighbor sections of this section.
    public void setNeighborSectionsOnEndingSide() {
	if(this.isReturnable == true) {
	    neighborSectionsOnEndingSide = HelperMethodsClass.copyVector(endingStation.getSectionsStartingFromThisStation());
	}
	else {
	    Vector<Section> sectionsStartingFromTheEndingStation = endingStation.getSectionsStartingFromThisStation();
	    neighborSectionsOnEndingSide = new Vector<Section> (sectionsStartingFromTheEndingStation.size() - 1);

	    for(int i = 0; i < sectionsStartingFromTheEndingStation.size(); i++) {

		Section sectionToAdd = sectionsStartingFromTheEndingStation.get(i);
		if(sectionToAdd.getEndingStation() != this.startingStation) {
		    neighborSectionsOnEndingSide.add(sectionToAdd);
		}
	    }
	}	
    } // setNeighborSectionsOnEndingSide()

    //===============================================================================================================================================================
    //This method takes train type as an argument and returns whether trains of the train type can run this section or not.
    //High speed trains ('G' and 'D) can run only on high-speed sections.
    //Non-prefix trains ('B') can run only on normal-speed sections.
    //Other types of trains can run either high-speed or normal-speed sections. 
    public boolean isThisTypeOfTrainRunnable(char trainType) {
	if(this.isHighSpeed && this.isNormalSpeed) {
	    if(trainType == 'B') {
		return false;
	    }
	    else{ 
		return true;
	    }
	}
	else if(this.isHighSpeed && !this.isNormalSpeed) {
	    if(trainType == 'G' || trainType == 'D') {
		return true;
	    }
	    else{
		return false;
	    }
	}
	else if(!this.isHighSpeed && this.isNormalSpeed) {
	    if(trainType == 'G' || trainType == 'D') {
		return false;
	    }
	    else{
		return true;
	    }
	}
	else {   
            System.out.println("Error in isThisTypeOfTrainRunnable() in Section Class");
	    System.exit(1);
	    return false;
	}
    } //isThisTypeOfTrainRunnable()
} // class Section

