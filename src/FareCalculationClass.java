public class FareCalculationClass {

    private final static int FIRST_CLASS = 0;
    private final static int SECOND_CLASS = 1;
    private final static int SOFT_SLEEPER_CLASS = 2;
    private final static int SOFT_SEAT_CLASS = 3;
    private final static int HARD_SLEEPER_CLASS = 4;
    private final static int HARD_SEAT_CLASS = 5;

    //===========================================================================================================================================================
    public static double calculateFare(int totalDistance, int guangzhouDistance, int xizangDistance, char trainType, int seatClass, boolean isAirConditioned) {
	if(trainType == 'G') {
	    return gTrainCalculateFare(totalDistance, guangzhouDistance, seatClass);
	}
	else if(trainType == 'D') {
	    return dTrainCalculateFare(totalDistance, guangzhouDistance, seatClass);
	}
	else if(trainType == 'Z' || trainType == 'T') {
            //[FOR FUTURE DEVELOPMENT] Display a warning to the user; "WARNING: The route search result may not be accurate. || REASON: Some Z and T trains have irregular fare calculation formula."
            return kTrainCalculateFare(totalDistance, guangzhouDistance, xizangDistance, seatClass, isAirConditioned);
	}
	else if(trainType == 'K' || trainType == 'Y') {
	    return kTrainCalculateFare(totalDistance, guangzhouDistance, xizangDistance, seatClass, isAirConditioned);
	}
	else if(trainType == 'B') {
	    return nonPrefixTrainCalculateFare(totalDistance, guangzhouDistance, seatClass, isAirConditioned);
	}
	else {
	    System.out.println("DEBUG: Error in calculateFare() in FareCalculationClass!!!");
	    return 9999.99;
	}
    } // calculateFare()

    //=======================================================================================================================================================
    private static double gTrainCalculateFare(int totalDistance, int guangzhouDistance, int seatClass) {
	return 1.25 * dTrainCalculateFare(totalDistance, guangzhouDistance, seatClass);
    } // gTrainCalculateFare()

    //========================================================================================================================================================
    private static double dTrainCalculateFare(int totalDistance, int guangzhouDistance, int seatClass) {
	if(seatClass == FIRST_CLASS) {
	    return (totalDistance - guangzhouDistance) * 10 + guangzhouDistance * 20;
	}
	if(seatClass == SECOND_CLASS) {
	    return (totalDistance - guangzhouDistance) * 8 + guangzhouDistance * 10;
	}

	System.out.println("DEBUG: Something wrong in dTrainCalculateFare() in FareCalculacionClass class)");
	return 9999.99;
    }// dTrainCalculateFare()

    //=========================================================================================================================================================
    private static double kTrainCalculateFare(int totalDistance, int guangzhouDistance, int xizangDistance, int seatClass, boolean isAirConditioned) {
	if (!isAirConditioned) {
	    return 0.5 * kTrainCalculateFare(totalDistance, guangzhouDistance, xizangDistance, seatClass, true);
	}
	if(seatClass == SOFT_SLEEPER_CLASS) {
	    return (totalDistance - guangzhouDistance - xizangDistance) * 9 + guangzhouDistance * 18 + xizangDistance * 13.5;
	}
	if(seatClass == SOFT_SEAT_CLASS) {
	    return (totalDistance - guangzhouDistance - xizangDistance) * 4.5 + guangzhouDistance * 9 + xizangDistance * 6.75;
	}
	if(seatClass == HARD_SLEEPER_CLASS) {
	    return (totalDistance - guangzhouDistance - xizangDistance) * 6 + guangzhouDistance * 12 + xizangDistance * 9;
	}
	if(seatClass == HARD_SEAT_CLASS) {
	    return (totalDistance - guangzhouDistance -xizangDistance) * 3 + guangzhouDistance * 6 + xizangDistance * 4.5;
	}

	System.out.println("DEBUG: Something wrong in kTrainCalculateFare() in FareCalculacionClass class)");
	return 9999.99;
    }// kTrainCalculateFare()

    //===========================================================================================================================================================
    private static double nonPrefixTrainCalculateFare(int totalDistance, int guangzhouDistance, int seatClass, boolean isAirConditioned) {
	return 0.75 * kTrainCalculateFare(totalDistance, guangzhouDistance, 0, seatClass, isAirConditioned);
    }// nonPrefixTrainCalculateFare()
} // class FareCalculationClass




