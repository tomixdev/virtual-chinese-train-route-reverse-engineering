import java.util.Vector;

//This class contains helper methods used in other classes. 
public class HelperMethodsClass {

    //=============================================================================================================
    //The method below checks if a vector of vector of stations contains a specific vector of stations.
    public static boolean contains (Vector<Vector<Station>> returnableSectionList, Vector<Station> twoStations) {
	for(int i= 0; i< returnableSectionList.size(); i++) {
	    if(returnableSectionList.elementAt(i).elementAt(0) == twoStations.elementAt(0) 
	       && returnableSectionList.elementAt(i).elementAt(1) == twoStations.elementAt(1)) {		
		return true;
	    }
	}
	return false;
    } // contains()
 
    //=============================================================================================================   
    public static Station[][] convertVectorOfStationArrayToMultidimentionalArray(Vector<Station[]> original) {
	Station[][] toReturn = new Station[original.size()][2];
	for(int i = 0; i < original.size(); i++) {
	    toReturn[i][0] = original.elementAt(i)[0];
	    toReturn[i][1] = original.elementAt(i)[1];
	}

	return toReturn;
    } // convertVectorOfStationArrayToMulcidimentionalArray()

    //============================================================================================================
    //The method below duplicates a vector of stations.
    public static Vector<Station> copyVectorStation(Vector<Station> original) {
	Vector<Station> toReturn = new Vector<Station>(original.size());
	for(int i = 0; i < original.size(); i++) {
	    toReturn.add(i, original.elementAt(i));
	}
	return toReturn;
    } // copyVectorStation()

    //============================================================================================================
    //The method below duplicates a vector of sections.
    public static Vector<Section> copyVector(Vector<Section> toBeCopied) {
	Vector<Section> toReturn = new Vector<Section>(toBeCopied.size());
	for(int i = 0; i < toBeCopied.size(); i++) {
	    toReturn.add(toBeCopied.elementAt(i));
	}
	return toReturn;
    } // copyVector()

    //============================================================================================================
    //The method below duplicates a vector of facilities
    public static Vector<Facility> copyVectorFacility(Vector<Facility> original) {
	Vector<Facility> toReturn = new Vector<Facility>(original.size());
	for(int i = 0; i < original.size(); i++) {
	    toReturn.add(i, original.elementAt(i));
	}
	return toReturn;
    } // copyVectorFacility()

    //===========================================================================================================
    //The method below converts a vector of sections to an array of sections
    public static Section[] convertVectorToArray (Vector<Section> vector) {
	Section[] toReturn = new Section[vector.size()];
	for(int i = 0; i < toReturn.length; i++) {
	    toReturn[i] = vector.elementAt(i);
	}
	return toReturn;
    } // convertVectorToArray()
   
    //===========================================================================================================
    //The method below converts an array of sections to a vector of secitions.
    public static Vector<Section> convertArrayToVector(Section[] array) {
	Vector<Section> toReturn = new Vector<Section>(array.length);
	for(int i = 0; i < array.length; i++) {
	    toReturn.add(array[i]);
	}
	return toReturn;
    } // convertArrayToVector()

    //===========================================================================================================
    //The method below converts a multi-dimentional vector of sections into a multi-dimentional array of sections.
    public static Section[][] convertVectorOfVectorToMultidimentionalArray(Vector<Vector<Section>> original) {
	Section[][] toReturn = new Section[original.size()][];
	for(int i = 0; i< toReturn.length; i++) {
	    toReturn[i] = new Section[original.elementAt(i).size()];
	    for(int j = 0; j < toReturn[i].length; j++) {
		toReturn[i][j] = original.elementAt(i).elementAt(j);
	    }
	}
	return toReturn;
    } // convertVectorOfVectorToMultidimentionalArray()

    //==================================================================================================================================
    //The method below returns all the station objects between a station and another station in a vector of stations. 
    //The vector is assumed to contain both station objects.
    public static Vector<Station> allTheElementsBetweenTwoElementsInVector(Vector<Station> vector, Station element1, Station element2) {

	int indexOfElement1 = vector.indexOf(element1);
	int indexOfElement2 = vector.indexOf(element2);

	if(indexOfElement1 > indexOfElement2) {
	    return allTheElementsBetweenTwoElementsInVector(vector, element2, element1);
	}

	Vector<Station> toReturn = new Vector<Station>();

	for(int i = indexOfElement1; i <= indexOfElement2 ; i++) {
	    toReturn.add(vector.elementAt(i));
	}

        return toReturn;
    } // allTheElementsBetweenTwoElementsInVector()			
} // class HelperMethodsClass




 
