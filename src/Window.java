import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Collection;
import java.util.Iterator;

public class Window extends JPanel implements ActionListener {

    private final int HEIGHT = 756;
    private final int WIDTH = 1024;

    JPanel box1, box2, box3, box4, box5;

    JLabel _start, _arrive, _fare, _trainNum, _trainType, _classType, _airCondition;
    JTextField start, arrive, fare, trainNum;
    JButton search, confirm;
    JComboBox<Object> trainType, classType, airCondition;

    JTextArea display, instruction, _userChoice, separationLine;
    JScrollPane scroll;
    JTextField userChoice;

    World world;

    Train train;
    Section[][] possibleOptions;

    //Constructor
    public Window(World _world) {
        this.world = _world;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); //set the size for the window
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        init();
    } // Window()

    //=================================================================================
    //The method below initializes the window.
    private void init() {

        this.add(Box.createRigidArea(new Dimension(0,20)));
        //box 1
        box1 = new JPanel();

        //add the starting station label
        _start = new JLabel("Departing Station:");
        box1.add(_start);

        //add the starting station textfield
        start = new JTextField();
        start.setColumns(6);
        box1.add(start);

        //add the arriving station label
        _arrive = new JLabel("Arriving Station:");
        box1.add(_arrive);

        //add the arriving station textfield
        arrive = new JTextField();
        arrive.setColumns(6);
        box1.add(arrive);

        //add trip fare label
        _fare = new JLabel("Fare:");
        box1.add(_fare);

        //add trip fare textfield
        fare = new JTextField();
        fare.setColumns(6);
        box1.add(fare);

        //add train number label
        _trainNum = new JLabel("Train Number:");
        box1.add(_trainNum);

        //add train number textfield
        trainNum = new JTextField();
        trainNum.setColumns(6);
        box1.add(trainNum);
        this.add(box1);

        //box 2
        box2 = new JPanel();

        //add train type label
        _trainType = new JLabel("Train Type:");
        box2.add(_trainType);

        //add drop down menu for train type
        trainType = new JComboBox<>();
        trainType.addItem("");
        trainType.addItem("G");
        trainType.addItem("D");
        trainType.addItem("Z");
        trainType.addItem("T");
        trainType.addItem("K");
        trainType.addItem("Y");
        trainType.addItem("S");
        trainType.addItem("No Prefix");
        box2.add(trainType);

        //add class type label
        _classType = new JLabel("Class Type:");
        box2.add(_classType);

        //add drop down menu for class type
        classType = new JComboBox<>();
        classType.addItem("");
        classType.addItem("First Class");
        classType.addItem("Second Class");
        classType.addItem("Soft-Sleeper");
        classType.addItem("Soft-Seat");
        classType.addItem("Hard-Sleeper");
        classType.addItem("Hard-Seat");
        box2.add(classType);

        //add air conditioning label
        _airCondition = new JLabel("Air Conditioning:");

        box2.add(_airCondition);

        //add air conditioning menu
        airCondition = new JComboBox<>();
        airCondition.addItem("");
        airCondition.addItem("Yes");
        airCondition.addItem("No");
        box2.add(airCondition);

        //add search button
        search = new JButton("Search");
        search.addActionListener(this);
        box2.add(search);

        this.add(box2);

        //box 3
        box3 = new JPanel();
        box3.setLayout(new BoxLayout(box3, BoxLayout.X_AXIS));
        box3.add(Box.createRigidArea(new Dimension(100,0)));

        //create text area to display instruction
        instruction = new JTextArea();
        instruction.setEditable(false);
        instruction.setText("Welcome to the Chinese Rail Route Search Program!\n" +
                "Please provide the following information of your trip (only the following combinations of information is accepted by the program).\n" +
                "Default air condition status is YES. Default seat class is Hard-sleeper class. Default train type is K.\n" +
                "1. Starting station, arriving station, fare\n" +
                "2. Starting station, arriving station, fare, air conditioning of the train\n" +
                "3. Starting station, arriving station, fare, type of the train\n" +
                "4. Starting station, arriving station, fare, your seat class for this trip\n" +
                "5. Starting station, arriving station, fare, air conditioning of the train, type of the train\n" +
                "6. Starting station, arriving station, fare, air conditioning of the train, your seat class for this trip\n" +
                "7. Starting station, arriving station, fare, type of the train, your seat class for this trip\n" +
                "8. Starting station, arriving station, fare, air conditioning of the train, type of the train, seat class for this trip\n" +
                "9. Starting station, arriving station, fare, train number \n" +
                "10. Starting station, arriving station, fare, train number, seat class for this trip\n" +
                "11. Train number, fare, seat class for this trip\n" +
                "12. Train number, fare\n" +
		"\n" +
                "*Note that you must provide the fare information.\n" +
		" If you provide train number, please do not select the train type and air conditioning service.\n");
        instruction.setBackground(null);
        box3.add(instruction);

        this.add(box3);

        this.add(Box.createRigidArea(new Dimension(0,20)));

        //box 4
        box4 = new JPanel();
        box4.setLayout(new BoxLayout(box4, BoxLayout.X_AXIS));
        box4.add(Box.createRigidArea(new Dimension(100,0)));

        //create text area and set text to instructions
        display = new JTextArea();
        display.setEditable(false);
        display.setRows(50);
        display.setText("");

        //make it scrollable
        scroll = new JScrollPane(display);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        box4.add(scroll);

        box4.add(Box.createRigidArea(new Dimension(100,0)));
        this.add(box4);

        this.add(Box.createRigidArea(new Dimension(0,20)));

	//add separation line
	separationLine = new JTextArea("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        separationLine.setBackground(null);
	this.add(separationLine);

        this.add(Box.createRigidArea(new Dimension(0,20)));

	//box 5
        box5 = new JPanel();

        //add user choice instruction
        _userChoice = new JTextArea();
        _userChoice.setBackground(null);
        _userChoice.setEditable(false);
        _userChoice.setText("Train runs only one of the routes shown above. \n" +
                            "Choose which one is the correct one from other\n" +
                            "resources you have (input option number):");
        box5.add(_userChoice);

        //add user choice
        userChoice = new JTextField();
        userChoice.setColumns(6);
        userChoice.setEditable(false);
        box5.add(userChoice);

        //add confirm button
        confirm = new JButton("confirm");
        confirm.addActionListener(this);
        confirm.setEnabled(false);
        box5.add(confirm);

        this.add(box5);
        this.add(Box.createRigidArea(new Dimension(0,20)));
    } // init()

    //=========================================================================================================================================
    public void actionPerformed(ActionEvent e)  {

        //if event is fired from the search button:
        if (e.getSource() == search) {

            //first set text to empty;
            display.setText("");

	    if (checkAllInputs() == false) {
		return;
	    }

            if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String) trainType.getSelectedItem()).equals("") && ((String)classType.getSelectedItem()).equals("") && ((String)airCondition.getSelectedItem()).equals("")) {
                // #1

                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()));
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }

            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String)trainType.getSelectedItem()).equals("") && ((String)classType.getSelectedItem()).equals("") && ((String)airCondition.getSelectedItem()).equals("") == false) {
                // #2

                boolean booleanAirCondition = setBooleanAirConditioning((String) airCondition.getSelectedItem());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), booleanAirCondition);
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String)trainType.getSelectedItem()).equals("")==false && ((String)classType.getSelectedItem()).equals("") && ((String)airCondition.getSelectedItem()).equals("")) {
                // #3

                int selectedTrainType = convertTrainType((String) trainType.getSelectedItem());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), selectedTrainType);
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String)trainType.getSelectedItem()).equals("") && ((String)classType.getSelectedItem()).equals("") == false && ((String)airCondition.getSelectedItem()).equals("")) {
                // #4

                int selectedClassType = convertClass((String) classType.getSelectedItem());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), selectedClassType);
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String)trainType.getSelectedItem()).equals("") == false && ((String)classType.getSelectedItem()).equals("") == false && ((String)airCondition.getSelectedItem()).equals("")) {
                // #5

		char selectedTrainType = convertTrainType((String) trainType.getSelectedItem());;
                int selectedClassType = convertClass((String) classType.getSelectedItem());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), selectedTrainType, selectedClassType);
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String)trainType.getSelectedItem()).equals("") == false && ((String)classType.getSelectedItem() ).equals("") && ((String)airCondition.getSelectedItem()).equals("") == false) {
                // #6

                char selectedTrainType = convertTrainType((String) trainType.getSelectedItem());
                boolean booleanAirCondition = setBooleanAirConditioning((String) airCondition.getSelectedItem());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), booleanAirCondition, selectedTrainType);
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String)trainType.getSelectedItem()).equals("") && ((String)classType.getSelectedItem()).equals("") == false && ((String)airCondition.getSelectedItem()).equals("") == false) {
                // #7
                int selectedClassType = convertClass((String) classType.getSelectedItem());
                boolean booleanAirCondition = setBooleanAirConditioning((String) airCondition.getSelectedItem());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), booleanAirCondition, selectedClassType);
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() && ((String)trainType.getSelectedItem()).equals("") == false && ((String)classType.getSelectedItem()).equals("") == false && ((String)airCondition.getSelectedItem()).equals("") == false) {
                // #8
                char selectedTrainType = convertTrainType((String) trainType.getSelectedItem());
                int selectedClassType = convertClass((String) classType.getSelectedItem());
                boolean booleanAirCondition = setBooleanAirConditioning((String) airCondition.getSelectedItem());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), booleanAirCondition, selectedTrainType, selectedClassType);
                if (sr.getPossibleSectionCombinations() != null) {
                    displayRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() == false && ((String)trainType.getSelectedItem()).equals("") && ((String)classType.getSelectedItem()).equals("") == false && ((String)airCondition.getSelectedItem()).equals("")) {
                // #9
                int selectedClassType = convertClass((String) classType.getSelectedItem());
                train = world.getTrainsHashTable().get(trainNum.getText());
		if(!(train.getStoppingStations().contains(World.getStationsInJapaneseHashTable().get(start.getText())) && train.getStoppingStations().contains(World.getStationsInJapaneseHashTable().get(arrive.getText())))) {
		    display.setText("The train you input does not stop at one of the stations you input!");
		}
		else {  
		    SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), selectedClassType, trainNum.getText());
		    if (sr.getPossibleSectionCombinations() != null) {
			possibleOptions = sr.getPossibleSectionCombinations();
			displayTrainRoutes(sr.getPossibleSectionCombinations(), null);
		    }
		    else{
			display.setText("No Such Route! Route Not Found!");
			userChoice.setEditable(false);
			confirm.setEnabled(false);
		    }
		}
            }
            else if (start.getText().isEmpty() == false && arrive.getText().isEmpty() == false && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() == false && ((String)trainType.getSelectedItem()).equals("") && ((String)classType.getSelectedItem()).equals("") && ((String)airCondition.getSelectedItem()).equals("")) {
                // #10
                train = world.getTrainsHashTable().get(trainNum.getText());

		if(!(train.getStoppingStations().contains(World.getStationsInJapaneseHashTable().get(start.getText())) && train.getStoppingStations().contains(World.getStationsInJapaneseHashTable().get(arrive.getText())))) {
		    display.setText("The train you input does not stop at one of the stations you input!");
		}
		else {
		    SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(start.getText(), arrive.getText(), Double.parseDouble(fare.getText()), trainNum.getText());
		    if (sr.getPossibleSectionCombinations() != null) {
			possibleOptions = sr.getPossibleSectionCombinations();
			displayTrainRoutes(sr.getPossibleSectionCombinations(), null);
		    }
		    else{
			display.setText("No Such Route! Route Not Found!");
			userChoice.setEditable(false);
			confirm.setEnabled(false);
		    }
		}
		
            }
            else if (start.getText().isEmpty() && arrive.getText().isEmpty() && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() == false && ((String)trainType.getSelectedItem()).equals("") && ((String)classType.getSelectedItem()).equals("") && ((String)airCondition.getSelectedItem()).equals("")) {
                // #11
                train = world.getTrainsHashTable().get(trainNum.getText());
		SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(Double.parseDouble(fare.getText()), trainNum.getText());
		if (sr.getPossibleSectionCombinations() != null) {
		    possibleOptions = sr.getPossibleSectionCombinations();
		    displayTrainRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
		}
		else{
		    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
		}
            }
            else if (start.getText().isEmpty() && arrive.getText().isEmpty() && fare.getText().isEmpty() == false && trainNum.getText().isEmpty() == false && ((String)trainType.getSelectedItem()).equals("")&& ((String)classType.getSelectedItem()).equals("") == false && ((String)airCondition.getSelectedItem()).equals("")) {
                // #12
                int selectedClassType = convertClass((String) classType.getSelectedItem());
                train = world.getTrainsHashTable().get(trainNum.getText());
                SearchAllThePossibleRoutesFromFare sr = new SearchAllThePossibleRoutesFromFare(Double.parseDouble(fare.getText()), selectedClassType, trainNum.getText());
                if (sr.getPossibleSectionCombinations() != null) {
                    possibleOptions = sr.getPossibleSectionCombinations();
                    displayTrainRoutes(sr.getPossibleSectionCombinations(), sr.getLocationsOfUndeterminedSections());
                }
                else{
                    display.setText("No Such Route! Route Not Found!");
		    userChoice.setEditable(false);
		    confirm.setEnabled(false);
                }
            }
            else {
                display.setText("Wrong combo of information! Please reenter according to the instruction.");
            }


        }

	if (e.getSource() == confirm) {
	    try {
		int userChoiceInt = Integer.parseInt(userChoice.getText());
		if (userChoiceInt > possibleOptions.length || userChoiceInt <= 0){
		    display.setText("This is not a valid option!");
		}
		else {
		    display.setText("You have selected option: " + userChoiceInt);
		    Section[] toReturn = possibleOptions[userChoiceInt - 1];
		    train.setDeterminedPassingSections(toReturn);
		}
	    }
	    catch (NumberFormatException nfe) { 	 
		display.setText("The option needs to be a positive integer number");
	    }
	}
    } // actionPerformed()

    //==============================================================================================================
    //This method converts the train type infomation input by the user to character values.
    //For non prefix trains, we assign 'B' as train type.
    //For the other types of trains, the prefix of train number is the type of train.
    private char convertTrainType(String letter) {
	if(letter == "No Prefix") {
	    return 'B';
	}
	else {
	    return letter.charAt(0);
	}
    } // convertTrainType()

    //=========================================================================================================
    //This method converts the class type information input by the user to interger values, which will be used in the route search program.
    private int convertClass(String letter) {
        //convert the class selection into number
        if (letter == "First Class"){
            return 0;
        }
        else if (letter == "Second Class"){
            return 1;
        }
        else if (letter == "Soft-Sleeper"){
            return 2;
        }
        else if (letter == "Soft-Seat"){
            return 3;
        }
        else if (letter == "Hard-Sleeper"){
            return 4;
        }
        else if (letter == "Hard-Seat"){
            return 5;
        }
	else {
	    System.out.println("Error in Window.java, convertClass() method.");
	    System.exit(1);
	    return -9999;
	}
    } // convertClass()

    //===================================================================================================================
    private boolean setBooleanAirConditioning (String condition) {
	return condition.equals("Yes");
    } // setBooleanAirConditioning()

    //====================================================================================================================
    private void displayRoutes (Section[][] possibleSectionCombinations, Station[][] locationsOfUndeterminedSections){
        //display information in the text area. set confirm button to disabled.
        userChoice.setEditable(false);
        confirm.setEnabled(false);
        String toDisplay = "Possible Options:";
        int numOption = 0;
        while (numOption < possibleSectionCombinations.length){
            toDisplay = toDisplay + "\n" + "Option " + (numOption+1) + ": ";
            int numSection = 0;
            while (numSection < possibleSectionCombinations[numOption].length){
                toDisplay = toDisplay  + possibleSectionCombinations[numOption][numSection].toString() + " || ";
                numSection++;
            }
            numOption++;
        }

	//toDisplay = addLocationsOfUndeterminedSections (toDisplay, locationsOfUndeterminedSections);
	
        display.setText(toDisplay);
    } // displayRoutes()

    //=================================================================================================================
    private void displayTrainRoutes (Section[][] possibleRoutes, Station[][] locationsOfUndeterminedSections) {
        if(possibleRoutes.length == 1){
            String toDisplay = "Option 1: ";
            int numSection = 0;
            while (numSection < possibleRoutes[0].length) {
                toDisplay = toDisplay + possibleRoutes[0][numSection].toString() + " || ";
                numSection++;
            }
	    if(train.getStoppingStations().elementAt(0) == possibleRoutes[0][0].getStartingStation() && train.getStoppingStations().elementAt(train.getStoppingStations().size() - 1) == possibleRoutes[0][possibleRoutes[0].length-1].getEndingStation()) {
		toDisplay = toDisplay + "\n" + "The route is already determined because there is only one possibility.";
		train.setDeterminedPassingSections(possibleRoutes[0]);
	    }
	    else {
		userChoice.setEditable(false);
		confirm.setEnabled(false);
	    }

	    display.setText(toDisplay);	    
        }

        //set userChoice text field to visible. set confirm button to enabled.
        else {
            userChoice.setEditable(true);
            confirm.setEnabled(true);

            //display information in the text area.
            String toDisplay = "Possible Options:";
            int numOption = 0;
            while (numOption < possibleRoutes.length) {
                toDisplay = toDisplay + "\n" + "Option " + (numOption + 1) + ": ";
                int numSection = 0;
                while (numSection < possibleRoutes[numOption].length) {
                    toDisplay = toDisplay + possibleRoutes[numOption][numSection].toString() + " || ";
                    numSection++;
                }
                numOption++;
            }

	    //toDisplay = addLocationsOfUndeterminedSections (toDisplay, locationsOfUndeterminedSections);

            display.setText(toDisplay);

        }
    } // displayTrainRoutes()

    //=======================================================================================================================================
    //The following method checks various wrong inputs.
    private boolean checkAllInputs() {
	if(!start.getText().isEmpty() && !arrive.getText().isEmpty()) {
	    if(!checkStationName(start.getText()) || !checkStationName(arrive.getText())) {
		display.setText("Station name is wrong. Check the spelling again!");
		return false;
	    }
	}

	if(!trainNum.getText().isEmpty()) {
	    if(!checkTrainName(trainNum.getText())) {
		display.setText("Train number is wrong. Check the number again!");
		return false;
	    }
	}

	if(!fare.getText().isEmpty()) {
	    if(!isPositiveNumber(fare.getText())) {
		display.setText("Fare is either non-numeric, 0, or negative. Check fare again!");
		return false;
	    }
	}

	if(!trainNum.getText().isEmpty() && !((String)(classType.getSelectedItem())).equals("")) {
	    if(!isClassTypeValid(trainNum.getText(), (String)(classType.getSelectedItem()))) {
		display.setText("Seat type is inivalid. G and D trains only have First Class and Second Class. The other types of trains only have the other class types.");
		return false;
	    }
	}

	if(!((String)(trainType.getSelectedItem())).equals("") && !((String)(classType.getSelectedItem())).equals("")) {
	    if(!isClassTypeValid((String)(trainType.getSelectedItem()), (String)(classType.getSelectedItem()))) {
		display.setText("Either seat type or train type is wrong. G and D trains only have First Class and Second Class. The other type only have the other class types.");
		return false;
	    }
	}

	if(((String)(classType.getSelectedItem())).equals("") && !(trainNum.getText()).isEmpty() && (trainNum.getText().charAt(0) == 'G' || trainNum.getText().charAt(0) == 'D')) {
	    display.setText("You need to specify class type. The train number indicates that this train only have First Class and Second Class");
	    return false;
	}

	if(trainNum.getText().isEmpty() && ((String)(trainType.getSelectedItem())).equals("") && !((String)(classType.getSelectedItem())).equals("")) {
	    if(!isClassTypeValid("K", (String)(classType.getSelectedItem()))) {
		display.setText("Remember that the default train type is K. K trains do not have First Class and Second Class");
		return false;
	    }
	}

	if( !((String)(trainType.getSelectedItem())).equals("") && ((String)(classType.getSelectedItem())).equals("")) {
	    if(!isClassTypeValid((String)(trainType.getSelectedItem()), "Hard-Sleeper")) {
		display.setText("Remember that the default class type is hard-sleeper. G, D trains do not have this class type.");
		return false;
	    }
	}

	return true;
    } // checkAllInputs()

    //======================================================================================================================================
    //The method below checks if a string is a station name or not.
    private boolean checkStationName(String stationName) {
	try {
	   Scanner infile = new Scanner(new FileReader("VirtualStationList.txt"));
	   while(infile.hasNext()) {
	       String japaneseStationName = infile.next();
	       if(japaneseStationName.equals(stationName)) {
		   return true;
	       }
	   }
	   infile.close();
	   return false;
	}
	catch (java.io.FileNotFoundException e) {
	    System.out.println("File name in the first try-catch in setStationsHashTables() in World Class is wrong");
	    System.exit(1);
	    return false;//this statenemt is never read!
	}
    } // checkStationName()

    //=======================================================================================================================================
    //The method below checks if a string is a train number or not.
    private boolean checkTrainName (String trainNum) {
	Collection<Train> trains = World.getTrainsHashTable().values();
	Iterator<Train> trainIterator = trains.iterator();
	while(trainIterator.hasNext()) {
	    if(trainIterator.next().getTrainName().equals(trainNum)) {
		return true;
	    }
	}
	return false;
    } //checkTrainName()

    //======================================================================================================================================
    //The method below is a string is a positive double number or not.
    private boolean isPositiveNumber(String str) {
	try {  
	    double d = Double.parseDouble(str); 
	    if(d > 0.0) {
		return true;
	    }
	    else {
		return false;
	    }
	}  
	catch(NumberFormatException nfe) { 	 
	    return false;  
	}
    } //isPositiveNumber()

    //=====================================================================================================================================
    //The method below checks if the combination of train type and class type is correct.
    //G and D trains only have First Class and Second Class.
    //Other types of trains do not have First Class and Second Class.
    private boolean isClassTypeValid(String trainType, String classType) {
	if(trainType.charAt(0) == 'G' || trainType.charAt(0) == 'D') {
	    if(classType.equals("First Class") || classType.equals("Second Class")) {
		return true;
	    }
	    else {
		return false;
	    }
	}
	else {
	    return !isClassTypeValid("G", classType);
	}
    } // isClassTypeValid()

    //===================================================================================================================================
    private String addLocationsOfUndeterminedSections(String toDisplay, Section[][] locationsOfUndeterminedSections) {
        if (locationsOfUndeterminedSections != null) {
            toDisplay = toDisplay + "\n" + "\n" + "Section route is undetermined between:";
            int num = 0;
	    toDisplay = toDisplay + "         //Dear Professor Alfeld, the information displayed below is wrong. Minato will add this feature after he graduates";
            while (num < locationsOfUndeterminedSections.length) {
                toDisplay = toDisplay + "\n" + (num + 1) + ". ";
                int numStation = 0;
                while (numStation < locationsOfUndeterminedSections[num].length) {
                    toDisplay = toDisplay + locationsOfUndeterminedSections[num][numStation].toString() + " || ";
                    numStation++;
                }
                num++;
            }
        }
	return toDisplay;
    } //addLocationsOfUndeterminedSections()
} // class Window





