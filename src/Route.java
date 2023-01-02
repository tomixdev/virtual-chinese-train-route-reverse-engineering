import java.util.Vector;

public class Route {

    private final String routeName;
    private final Vector<Station> stations;
    private final String belongingJurisdiction;
    private final boolean isSpecialFareRoute;

    //Constructor 1
    public Route (String routeName, Vector<Station> stations, String belongingJurisdiction) {
	this(routeName, stations, belongingJurisdiction, belongingJurisdiction.equals("広州") || routeName.equals("Xizang"));
    } // Route()

    //Constructor 2
    public Route (String routeName, Vector<Station>stations, String belongingJurisdiction, boolean doesSectionBelongToSpecialFareRoute) {
	this.routeName = routeName;
	this.stations= stations;
	this.belongingJurisdiction = belongingJurisdiction;
	this.isSpecialFareRoute = doesSectionBelongToSpecialFareRoute;
    } // Route()

    //====================================================================================
    //getters
    public String getRouteName() {
	return routeName;
    } // getRouteName()

    public String getBelongingJurisdiction() {
	return belongingJurisdiction;
    } // getBelongingJurisdiction()

    public Vector<Station> getStations() {
	return stations;
    } // getStations()

    public boolean getIsSpecialFareRoute() {
	return isSpecialFareRoute;
    } // getIsSpecialFareRoute()

    //toString
    public String toString() {
	return routeName;
    } // toString()
} // class Route
