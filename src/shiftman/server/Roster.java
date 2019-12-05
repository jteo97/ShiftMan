package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the bulk of the code required to run the Shift Manager
 */
public class Roster {

    private String _name;
    private String[] _day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String[] _startTime = new String[7];
    private String[] _endTime = new String[7];
    private List<Staff> _listOfStaff = new ArrayList<Staff>();
    private List<Shift> _listOfShift = new ArrayList<Shift>();

    /*
     * Constructs a roster object
     */
    public Roster(String shopName){
        _name = shopName;
    }

    /*
     * Sets working hours for the day
     * @param the day of the week
     * @param start time
     * @param end time
     */
    public void settingWorkingHours (String dayOfWeek, String startTime, String endTime) throws InvalidDayException, InvalidTimeException{

        boolean validOpeningHours = TimeCalc.midnightChecker(startTime, endTime);
        if (validOpeningHours) {
            boolean validDay = false;
            for (int i = 0; i < 7; i++) {
                if (dayOfWeek.equals(_day[i])) { //checks if day parameter is exactly one of the seven strings
                    _startTime[i] = startTime;
                    _endTime[i] = endTime;
                    validDay = true;
                }
            }
            if (!validDay) {
                throw new InvalidDayException("ERROR: Day given (" + dayOfWeek + ") is invalid");
            }
        } else {
            throw new InvalidTimeException("ERROR: Shop is open through midnight OR the start time is the same as or after the end time");
        }
    }

    /*
     * adds valid shifts to the roster
     * @param the day the shift is on
     * @param start time
     * @param end time
     * @param minimum number of workers to be in the shift
     * @returns an empty string if no errors or a description of the error
     */
    public String addingShift (String dayOfWeek, String startTime, String endTime, String minimumWorkers) throws InvalidTimeException{

        try {
            for (int i = 0; i < _listOfShift.size(); i++) {
                //checks if new shift is overlapping with existing shifts
                boolean overlappingShifts = TimeCalc.overlappingShifts(_listOfShift.get(i).get_startTime(), _listOfShift.get(i).get_endTime(), startTime, endTime);
                //checks if new and existing shift is on the same day
                boolean sameDay = dayOfWeek.equalsIgnoreCase(_listOfShift.get(i).get_day());
                if (overlappingShifts & sameDay) { //same day and overlapping mean throw an error
                    throw new InvalidTimeException("ERROR; Shifts overlap");
                }
            }
            Shift shift = new Shift(dayOfWeek, startTime, endTime, minimumWorkers);
            _listOfShift.add(shift);
        } catch (InvalidDayException e) {
            return e.getMessage();
        }
        return "";
    }

    /*
     * registers staff and ignores registering already registered staff
     * @param First name of the staff to be registered
     * @param Family name of the staff to be registered
     * @returns an empty string if no errors or a description of the error
     */
    public String registeringStaff (String givenName, String familyName) throws StaffAlreadyRegisteredException{

        try {
            for (int i = 0; i < _listOfStaff.size(); i++) {
                if (givenName.equalsIgnoreCase(_listOfStaff.get(i).get_givenName()) && familyName.equalsIgnoreCase(_listOfStaff.get(i).get_familyName())) {
                    throw new StaffAlreadyRegisteredException("ERROR: Staff already registered");
                }
            }
            Staff staff = new Staff(givenName, familyName);
            _listOfStaff.add(staff);
        } catch (EmptyNameException e) {
            return e.getMessage();
        }
        return "";
    }

    /*
     * assigns staff to a particular shift
     * @param the day of the shift
     * @param start time of the shift
     * @param end time of the shift
     * @param First name of the staff being assigned to the shift
     * @param Family name of the staff being assigned to the shift
     * @param boolean indicating manager or not
     * @returns an empty string if no errors or a description of the error
     */
    public String assigningStaff (String dayOfWeek, String startTime, String endTime, String givenName, String familyName, boolean isManager) throws InvalidDayException {

        boolean validDay = false;
        for (int k = 0; k < 7; k++) {
            if (dayOfWeek.equals(_day[k])) {
                validDay = true;
            }
        }
        if (!validDay) {
            throw new InvalidDayException("ERROR: Day given (" + dayOfWeek + ") is invalid");
        }

        for (int i = 0; i < _listOfShift.size(); i++) {
            //find the exact shift
            if (dayOfWeek.equals(_listOfShift.get(i).get_day())) {
                if (startTime.equals(_listOfShift.get(i).get_startTime())) {
                    if (endTime.equals(_listOfShift.get(i).get_endTime())) {

                        for (int j = 0; j < _listOfStaff.size(); j++) {
                            //find the exact staff
                            if (givenName.equalsIgnoreCase(_listOfStaff.get(j).get_givenName())) {
                                if (familyName.equalsIgnoreCase(_listOfStaff.get(j).get_familyName())) {
                                    //assign that staff to that shift as manager or worker
                                    return _listOfShift.get(i).assignmentOfStaff(_listOfStaff.get(j), givenName, familyName, isManager);
                                }
                            }
                        }
                    }
                }
            }
        }

        return "";
    }

    /*
     * returns a List<String> of all the registered staff sorted in order of family name
     * @param an empty arraylist of strings
     * @returns a List if no errors or a description of the error
     */
    public List<String> getRegisteredStaff (List<String> listOfStaff) {
        Collections.sort(_listOfStaff); //sort the arraylist using our own definition
        for (int i = 0; i < _listOfStaff.size(); i++) {
            listOfStaff.add(_listOfStaff.get(i).get_givenName() + " " + _listOfStaff.get(i).get_familyName());
        }
        return listOfStaff;
    }

    /*
     * returns a List<String> of all unassigned registered staff sorted in order of family name
     * @param an empty arraylist of strings
     * @returns a List if no errors or a description of the error
     */
    public List<String> getUnassignedStaff (List<String> listOfUnassignedStaff) {
        Collections.sort(_listOfStaff);
        for (int i = 0; i < _listOfStaff.size(); i++) {
            //populate the arraylist with all registered staff
            listOfUnassignedStaff.add(_listOfStaff.get(i).get_givenName() + " " + _listOfStaff.get(i).get_familyName());
        }

        //remove staff in arraylist if they are assigned to at least one shift
        for (int i = 0; i < _listOfStaff.size(); i++) {
            String Name = _listOfStaff.get(i).get_givenName() + " " + _listOfStaff.get(i).get_familyName();
            for (int j = 0; j < _listOfShift.size(); j++) {
                int numWorkerOnShift = _listOfShift.get(j).get_numWorkerOnShift();
                List<Staff> workersOnShift = _listOfShift.get(j).get_workersOnShift();
                for (int k = 0; k < numWorkerOnShift; k++) {
                    if (Name.equalsIgnoreCase(workersOnShift.get(k).get_givenName() + " " + workersOnShift.get(k).get_familyName())) {
                        listOfUnassignedStaff.remove(Name);
                    }
                }
                if (_listOfShift.get(j).get_hasManager()) {
                    Staff managerOnShift = _listOfShift.get(j).get_managerOnShift();
                    if (Name.equalsIgnoreCase(managerOnShift.get_givenName() + " " + managerOnShift.get_familyName())) {
                        listOfUnassignedStaff.remove(Name);
                    }
                }
            }
        }

        return listOfUnassignedStaff;
    }

    /*
     * returns a List<String> of shifts without managers sorted by day then by start time
     * @param an empty arraylist of strings
     * @returns a List if no errors or a description of the error
     */
    public List<String> shiftsWithoutManagers (List<String> noManager) {
        Collections.sort(_listOfShift);
        for (int i = 0; i < _listOfShift.size(); i++) {
            if (!_listOfShift.get(i).get_hasManager()) { //retrieve all the shifts without managers and add them to list
                noManager.add(_listOfShift.get(i).get_day() + "[" + _listOfShift.get(i).get_startTime() + "-" + _listOfShift.get(i).get_endTime() + "]");
            }
        }

        return noManager;
    }

    /*
     * returns a List<String> of shifts understaffed sorted by day then by start time
     * @param an empty arraylist of strings
     * @returns a List if no errors or a description of the error
     */
    public List<String> understaffedShifts(List<String> underStaffed) {
        Collections.sort(_listOfShift);
        for (int i = 0; i < _listOfShift.size(); i++) {
            int minimum = Integer.parseInt(_listOfShift.get(i).get_minimumWorkers()); //retrieve minimumWorker field for each shift
            int numWorkers = _listOfShift.get(i).get_numWorkerOnShift(); //retrieve the number of workers assigned to that shift
            if (numWorkers < minimum) { //checks if understaffed
                underStaffed.add(_listOfShift.get(i).get_day() + "[" + _listOfShift.get(i).get_startTime() + "-" + _listOfShift.get(i).get_endTime() + "]");
            }
        }

        return underStaffed;
    }

    /*
     * returns a List<String> of shifts overstaffed sorted by day then by start time
     * @param an empty arraylist of strings
     * @returns a List if no errors or a description of the error
     */
    public List<String> overstaffedShifts(List<String> overStaffed) {
        Collections.sort(_listOfShift);
        for (int i = 0; i < _listOfShift.size(); i++) {
            int minimum = Integer.parseInt(_listOfShift.get(i).get_minimumWorkers()); //retrieve minimumWorker field for each shift
            int numWorkers = _listOfShift.get(i).get_numWorkerOnShift(); //retrieve the number of workers assigned to that shift
            if (numWorkers > minimum) { //checks if overstaffed
                overStaffed.add(_listOfShift.get(i).get_day() + "[" + _listOfShift.get(i).get_startTime() + "-" + _listOfShift.get(i).get_endTime() + "]");
            }
        }

        return overStaffed;
    }

    /*
     * returns a List<String> of shifts for a particular day of the week sorted by day then by start time
     * @param an empty arraylist of strings
     * @param the day to retrieve shift information
     * @returns a List if no errors or a description of the error
     */
    public List<String> getRosterForDay(List<String> rosterForDay, String dayOfWeek) throws InvalidDayException{
        Collections.sort(_listOfShift);
        rosterForDay.add(_name); //add the name of the shop
        for (int i = 0; i < 7; i++) {
            if (dayOfWeek.equals(_day[i])) {
                rosterForDay.add(_day[i] + " " + _startTime[i] + "-" + _endTime[i]); //add the working hours for the shop
            }
        }

        //throw an error if no such working day exists in the roster
        if (rosterForDay.size() != 2) {
            rosterForDay.clear();
            throw new InvalidDayException("ERROR: Day given (" + dayOfWeek + ") is invalid");
        }

        //retrieve shift/manager/worker information to store into a string to add to the arraylist as one event
        for (int j = 0; j < _listOfShift.size(); j++) {
            if (dayOfWeek.equals(_listOfShift.get(j).get_day())) {
                String shiftString;
                shiftString = _listOfShift.get(j).get_day() + "[" + _listOfShift.get(j).get_startTime() + "-" + _listOfShift.get(j).get_endTime() + "] ";
                if (_listOfShift.get(j).get_hasManager()) {
                    shiftString += "Manager: " + _listOfShift.get(j).get_managerOnShift().get_familyName() + ", " + _listOfShift.get(j).get_managerOnShift().get_givenName() + " ";
                } else {
                    shiftString += "[No manager assigned] ";
                }
                int numWorker = _listOfShift.get(j).get_numWorkerOnShift();
                if (numWorker == 1) {
                    List<Staff> workers = _listOfShift.get(j).get_workersOnShift();
                    Collections.sort(workers);
                    shiftString += "[" + workers.get(0).get_givenName() + " " + workers.get(0).get_familyName() + "]";
                } else if (numWorker > 1) {
                    List<Staff> workers = _listOfShift.get(j).get_workersOnShift();
                    Collections.sort(workers);
                    shiftString += "[";
                    for (int k = 0; k < numWorker; k++) {
                        shiftString += workers.get(k).get_givenName() + " " + workers.get(k).get_familyName();
                        if (k < numWorker - 1) {
                            shiftString += ", ";
                        }
                    }
                    shiftString += "]";
                } else {
                    shiftString += "[No workers assigned]";
                }
                rosterForDay.add(shiftString);
            }
        }
        //if the roster is of size two meaning no shifts/manager/worker are working that day, so clear it as per instructions in the javadoc
        if (rosterForDay.size() == 2){
            rosterForDay.clear();
        }

        return rosterForDay;
    }

    /*
     * returns a List<String> of shifts that a particular worker is assigned to sorted by day then by start time
     * @param an empty arraylist of strings
     * @param the name of the worker to retrieve shift information
     * @returns a List if no errors or a description of the error
     */
    public List<String> getRosterForWorker(List<String> workerRoster, String workerName) {
        Collections.sort(_listOfShift);
        for (int i = 0; i < _listOfStaff.size(); i++) {
            if (workerName.equalsIgnoreCase(_listOfStaff.get(i).get_givenName() + " " + _listOfStaff.get(i).get_familyName())) {
                workerRoster.add(_listOfStaff.get(i).get_familyName() + ", " + _listOfStaff.get(i).get_givenName());

                //iterate over all of the shifts to see if the worker in question is assigned to it
                for (int j = 0; j < _listOfShift.size(); j++) {
                    int numWorker = _listOfShift.get(j).get_numWorkerOnShift();
                    List<Staff> workers = _listOfShift.get(j).get_workersOnShift();
                    for (int k = 0; k < numWorker; k++) {
                        if (workerName.equalsIgnoreCase(workers.get(k).get_givenName() + " " + workers.get(k).get_familyName())) {
                            workerRoster.add(_listOfShift.get(j).get_day() + "[" + _listOfShift.get(j).get_startTime() + "-" + _listOfShift.get(j).get_endTime() + "]");
                        }
                    }
                }
            }
        }
        //if the roster is of size one meaning worker has no assigned shift, so clear it as per instructions in the javadoc
        if (workerRoster.size() == 1){
            workerRoster.clear();
        }

        return workerRoster;
    }

    /*
     * returns a List<String> of shifts for a particular manager sorted by day then by start time
     * @param an empty arraylist of strings
     * @param the name of the manager to retrieve shift information
     * @returns a List if no errors or a description of the error
     */
    public List<String> getShiftsManagedBy(List<String> managerRoster, String managerName) {
        Collections.sort(_listOfShift);
        int printName = 0;
        for (int i = 0; i < _listOfShift.size(); i++) {
            if (_listOfShift.get(i).get_hasManager()) {
                Staff manager = _listOfShift.get(i).get_managerOnShift();
                if (managerName.equalsIgnoreCase(manager.get_givenName() + " " + manager.get_familyName())) {
                    if (printName == 0) {
                        managerRoster.add(manager.get_familyName() + ", " + manager.get_givenName());
                        printName = 1; //print the name only once
                    }
                    managerRoster.add(_listOfShift.get(i).get_day() + "[" + _listOfShift.get(i).get_startTime() + "-" + _listOfShift.get(i).get_endTime() + "]");
                }
            }
        }

        return managerRoster;
    }
}
