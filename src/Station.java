import java.util.Vector;

public class Station extends Facility {

    private final String chineseStationName;
    private final String japaneseStationName;
    private int numberOfTimesCanBePassed;
    private Vector<Section> sectionsStartingFromThisStation;
    private Vector<Section> sectionsEndingInThisStation;

    //Constructor
    public Station (String chineseStationName, String japaneseStationName) {
	super.setDistance(0);
	this.chineseStationName = chineseStationName;
	this.japaneseStationName = japaneseStationName;
	this.sectionsStartingFromThisStation = new Vector<Section>();
	this.sectionsEndingInThisStation = new Vector<Section>();
	this.numberOfTimesCanBePassed = 1;
    }

    //==========================================================================
    //Getters
    public String getChineseStationName() {
	return chineseStationName;
    } // getChineseStationName()

    public String getJapaneseStationName() {
        return japaneseStationName;
    } // getJapaneseStationName()

    public int getNumberOfTimesCanBePassed() {
	return numberOfTimesCanBePassed;
    } // getNumberOfTimesCanBePassed()

    public Vector<Section> getSectionsStartingFromThisStation() {
	return sectionsStartingFromThisStation;
    } // getSectionsStartingFromThisStation()

    public Vector<Section> getSectionsEndingInThisSection() {
	return sectionsEndingInThisStation;
    } // getSectionsEndingInThisSection()  
    
    //===========================================================================
    //Setters
    public void setNumberOfTimesCanBePassed (int times) {
	this.numberOfTimesCanBePassed = times;
    } //setNumberOfTimesCanBePassed()

    public void setNumberOfTimesCanBePassed_value2() {
	this.setNumberOfTimesCanBePassed(2);
    } //setNumberOfTimesCanBePassed_value2()

    //===========================================================================
    //toString
    public String toString() {
	return japaneseStationName;
    } //toString()

    //===========================================================================
    //The method below adds a section to sectionsStartingFromThisStation.
    public void addSectionsStartingFromThisStation (Section toAdd) {
	sectionsStartingFromThisStation.add(toAdd);
    } // addSectionsStartingFromThisStation()

    //===========================================================================
    //The method below adds a section to sectionsEndingInThisStation.
    public void addSectionsEndingInThisStation (Section toAdd) {
	sectionsEndingInThisStation.add(toAdd);
    } // addSectionsEndingInThisStation()
} // class Station





