import java.util.Scanner;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.Double;
import java.lang.Boolean;
import java.io.IOException;
import java.util.Iterator;

public class World {

    private final static int NUMBER_OF_STATIONS = 4157;
    private final static int NUMBER_OF_SECTIONS = 9863;
    private final static int NUMBER_OF_ROUTES = 5342;
    private final static int NUMBER_OF_TRAINS = 10278;

    /*Explanation For The Following Five Fields-----------------------------------------------------------------------
    //stationsInJapaneseHashTable: hashtable for station objects, using stations written in Japanese Chinese characters as key.
    //stationsInChineseHashTable: hashtable for the same station objects as in stationsInJapaneseHashTable, using stations written in Chinese Chinese characters as key.
    //routesHashTable: hashtable for route objects, using Japanese Chinese characters as keys. 
    //sectionsList: vector to contain section objects. The section names are in Japanese Chinese characters.
    //trainsHashTable: hashtable for train objects, using Chinese Chinese characters as keys.
    //-------------------------------------------------------------------------------------------------------------------
    //Some hashtables use Japanese Chinese characters as key, while others use Chinese Chinese characters. 
    //This is because some data sets are written in Japanese Chinese characters while others are in Chinese Chinese characters.
    */

    private static Hashtable<String, Station> stationsInJapaneseHashTable;
    private static Hashtable<String, Station> stationsInChineseHashTable;
    private static Hashtable<String, Route> routesHashTable;
    private static Vector<Section> sectionsList;
    private static Hashtable<String, Train> trainsHashTable;

    //==========================================================================================================
    //Constructor
    public World() {
	setStationsHashTables();
	setRoutesHashTable();
	setSectionsList();
	setTrainsHashTable();
    } // World()

    //===========================================================================================================
    //Getters
    public static Hashtable<String, Station> getStationsInJapaneseHashTable() {
	return stationsInJapaneseHashTable;
    } //getStationsInJapaneseHashTable()

    public static Hashtable<String, Station> getStationsInChineseHashTable() {
	return stationsInChineseHashTable;
    } // getStationsInChineseHashTable()

    public static Hashtable<String, Route> getRoutesHashTable() {
	return routesHashTable;
    } //getRoutesHashTable()

    public static Vector<Section> getSectionsList() {
	return sectionsList;
    } //getSectionsList()

    public static Hashtable<String, Train> getTrainsHashTable() {
	return trainsHashTable;
    } //getTrainsHashTable()

    //================================================================================================================
    //The following method sets two station hashtables using Japanese Chinese characters and Chinese Chinese characters.
    private void setStationsHashTables() {

	stationsInJapaneseHashTable = new Hashtable<String, Station>(NUMBER_OF_STATIONS);
	stationsInChineseHashTable = new Hashtable<String, Station>(NUMBER_OF_STATIONS);

	try {
	   Scanner infile = new Scanner(new FileReader("VirtualStationList.txt"));
	   while(infile.hasNext()) {
	       String chineseStationName = infile.next();
	       String japaneseStationName = infile.next();

	       Station stationObject = new Station(chineseStationName, japaneseStationName);//This object is shared between the two hashtables.

	       stationsInJapaneseHashTable.put(japaneseStationName, stationObject);
	       stationsInChineseHashTable.put(chineseStationName, stationObject);
	   }
           infile.close();
	}
	catch (java.io.FileNotFoundException e) {
	    System.out.println("File name in the first try-catch in setStationsHashTables() in World Class is wrong");
	    System.exit(1);
	}

	try {
	    Scanner infile = new Scanner(new FileReader("VirtualReturnableList.txt"));
	    while(infile.hasNext()) {
		infile.next();//read "FromHere"
		infile.next();//read the first Station name, which can be passed only once.
		String nextString = infile.next();
		while(!nextString.equals("ToHere")) {
		    stationsInJapaneseHashTable.get(nextString).setNumberOfTimesCanBePassed_value2();
		    nextString = infile.next();
		}
	    }

	    infile.close();
	}
	catch (java.io.FileNotFoundException e) {
	    System.out.println("File name in the second try-catch in setStationsHashTables() in World Class is wrong");
	    System.exit(1);
	}
    } // setStationsHashTables()

    //===================================================================================================================
    //The following method sets routesHashTable.
    private void setRoutesHashTable() {

	routesHashTable = new Hashtable <String, Route>(NUMBER_OF_ROUTES);

	try{
	    Scanner infile = new Scanner(new FileReader("VirtualRouteList.txt"));

            String routeName;
	    String nextRouteName = infile.next();
	    while(infile.hasNext()) {
		Vector<Station> stationList = new Vector<Station>(30);

		routeName = nextRouteName; 
		String belongingJurisdiction = infile.next();//Belonging jurisdiction here means the jurisdiction the first section of a route belongs to. Different sections in the same note can have different belonging jurisdictions.
                String stationName = infile.next();

		stationList.add(stationsInJapaneseHashTable.get(stationName));

		infile.next(); //read distance information which will only be used when setting up sectionsList
		infile.next(); //read track status information which will only be used when setting up sectionsList
		infile.next(); //read another track status information which will only be used when setting up sectionsList
		
		if(infile.hasNext()) {
		    nextRouteName = infile.next();

		    while(infile.hasNext() && nextRouteName.equals(routeName)) {
			infile.next(); //read jurisdiction string
			stationName = infile.next();
			stationList.add(stationsInJapaneseHashTable.get(stationName));

			infile.next(); //read  distance informationwhich will only be used when setting up sectionsList
			infile.next(); //read track status information which will only be ussed when setting up sectionsLst
			infile.next(); //read another track status infomation which will only be used when setting up sectionsList
			if(infile.hasNext()) {
			    nextRouteName = infile.next();
			}
		    }
		}

		Route routeObject = new Route(routeName, stationList, belongingJurisdiction);
		routesHashTable.put(routeName, routeObject);
	    }

            infile.close();
	}
	catch (java.io.FileNotFoundException e) {
	    System.out.println("File name in setRoutesHashTable() in World Class is wrong");
	    System.exit(1);
	}
    } //setRoutesHashTable()

    //======================================================================================================
    //The following method sets sectionsList
    private void setSectionsList() {

	sectionsList = new Vector<Section>(NUMBER_OF_SECTIONS);

	try{
	    //First, set returnableSectionList	    
            Scanner returnableFile = new Scanner(new FileReader("VirtualReturnableList.txt"));
	    Vector<Vector<Station>> returnableSectionList = new Vector<Vector<Station>>();

	    while(returnableFile.hasNext()) {
		returnableFile.next();//read "FromHere"
		Station sta1 = stationsInJapaneseHashTable.get(returnableFile.next());
		Station sta2 = stationsInJapaneseHashTable.get(returnableFile.next());

		Vector<Station> temp = new Vector<Station>(2);
		temp.add(sta2); temp.add(sta1);

		returnableSectionList.add(temp);

		String nextString = returnableFile.next();
		while(!nextString.equals("ToHere")) {
		    nextString = returnableFile.next();
		}
	    }
	   
	    //Then, set sectionsList which is one of the fields of this class.
	    Scanner infile = new Scanner(new FileReader("VirtualRouteList.txt"));

	    //Contents of Line X
	    String routeName1; String jurisdiction1; String stationName1; int distance1; boolean isHighSpeed1; boolean isPassanger1;
	    //Contents of Line X+1
            String routeName2; String jurisdiction2; String stationName2; int distance2; boolean isHighSpeed2; boolean isPassanger2;
	   
	    routeName1 = infile.next();
	    jurisdiction1 = infile.next();
	    stationName1 = infile.next();
	    distance1 = infile.nextInt();
	    isHighSpeed1 = (infile.nextInt() == 1);
	    isPassanger1 = (infile.nextInt() == 1);

	    while(infile.hasNext()) {
		routeName2 = infile.next();
		jurisdiction2 = infile.next();
		stationName2 = infile.next();
		distance2 = infile.nextInt();
		isHighSpeed2 = (infile.nextInt() == 1);
		isPassanger2 = (infile.nextInt() == 1);

		if(routeName1.equals(routeName2)) {

		    int sectionDistance = distance2 - distance1;
		    Station stationObject1 = stationsInJapaneseHashTable.get(stationName1);
		    Station stationObject2 = stationsInJapaneseHashTable.get(stationName2);

		    Vector<Station> temp1 = new Vector<Station>(2); temp1.add(stationObject1); temp1.add(stationObject2);
		    Vector<Station> temp2 = new Vector<Station>(2); temp2.add(stationObject2); temp2.add(stationObject1);

		    //check if this section is returnable or not (for direction A)
		    boolean isReturnableA = HelperMethodsClass.contains(returnableSectionList, temp1);
		    boolean isReturnableB = HelperMethodsClass.contains(returnableSectionList, temp2);

		    Section sectionObjectA = new Section(sectionDistance, stationObject1 , stationObject2, routeName1, 'A', isReturnableA, isHighSpeed1, isPassanger1, jurisdiction1);
		    Section sectionObjectB = new Section(sectionDistance, stationObject2, stationObject1, routeName1, 'B' , isReturnableB, isHighSpeed1, isPassanger1, jurisdiction1);

		    sectionsList.add(sectionObjectA);
		    sectionsList.add(sectionObjectB);
		}

		routeName1 = routeName2;
		jurisdiction1 = jurisdiction2;
		stationName1 = stationName2;
		distance1 = distance2;
		isHighSpeed1 = isHighSpeed2;
		isPassanger1 = isPassanger2;
	    }

	    infile.close();
	}
	catch (java.io.FileNotFoundException e) {
	    System.out.println("File name in setSectionsList() in World Class is wrong");
	    System.exit(1);
	}
    } //setSectionsList()
  
    // ===========================================================================================================
    //The following method sets trainsHashTable
    private void setTrainsHashTable() {

	trainsHashTable = new Hashtable<String, Train>(NUMBER_OF_TRAINS);

	try{
	    Scanner infile = new Scanner(new FileReader("VirtualTrainList.txt"));

	    while(infile.hasNextLine()) {

		Vector<Station> stoppingStations = new Vector<Station>(15);
		Vector<Integer> arrivalTimes = new Vector<Integer>(15);
		Vector<Integer> departureTimes = new Vector<Integer>(15);

		String nextLine = infile.nextLine();
		String[] everyWordInThisLine = nextLine.split("\\s+");

		String trainIDString = everyWordInThisLine[0];
		String trainNameString = everyWordInThisLine[1];
		String isAirConditionedString = everyWordInThisLine[2];
		boolean isAirConditioned = isAirConditionedString.equals("1");

		String firstStationName = everyWordInThisLine[3];
		Station firstStationObject = stationsInChineseHashTable.get(firstStationName);
		stoppingStations.add(firstStationObject);

		arrivalTimes.add((Integer)9999);

		String firstDepartureTimeString = everyWordInThisLine[4];
		departureTimes.add(Integer.valueOf(firstDepartureTimeString));

		int index = 5;

		while(index < everyWordInThisLine.length) {
		    String stationName = everyWordInThisLine[index];
		    index++;
		    Station stationObject = stationsInChineseHashTable.get(stationName);
		    stoppingStations.add(stationObject);

		    String arrivalTimeString = everyWordInThisLine[index];
		    index++;
		    arrivalTimes.add(Integer.valueOf(arrivalTimeString));

		    if(index < everyWordInThisLine.length) {
			String departureTimeString = everyWordInThisLine[index];
			index++;
			departureTimes.add(Integer.valueOf(departureTimeString));
		    }
		}
	    
		departureTimes.add((Integer)9999);

		Train trainObject = new Train(trainIDString, trainNameString, isAirConditioned, stoppingStations, arrivalTimes, departureTimes);

		trainsHashTable.put(trainNameString, trainObject);
	    }

            infile.close();
	}
	catch (java.io.FileNotFoundException e) {
	    System.out.println("File name in setTrainsHashTables() in World Class is wrong");
	    System.exit(1);
	}
    } //setTrainsHashTable()
}// class World