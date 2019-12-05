package shiftman.server;


import java.util.ArrayList;
import java.util.List;

/**
 * A class that holds the information of a shift
 */
public class Shift implements Comparable<Shift>{

    private String _day;
    private String _dayNumber;
    private String _startTime;
    private String _endTime;
    private String _minimumWorkers;
    private List<Staff> _listOfWorkers = new ArrayList<Staff>();
    private Staff _managerOnShift;
    private boolean _hasManager;

    /*
     * Constructs a shift object
     * @param day
     * @param start time
     * @param end time
     * @param minimum number of workers
     */
    public Shift(String day, String startTime, String endTime, String minimumWorkers) throws InvalidDayException{
        switch (day) { //Switch statement to assign the correct alphabet to the shift for easy sorting, also act as a checker for invalid day
            case "Monday":
                _dayNumber = "a";
                break;
            case "Tuesday":
                _dayNumber = "b";
                break;
            case "Wednesday":
                _dayNumber = "c";
                break;
            case "Thursday":
                _dayNumber = "d";
                break;
            case "Friday":
                _dayNumber = "e";
                break;
            case "Saturday":
                _dayNumber = "f";
                break;
            case "Sunday":
                _dayNumber = "g";
                break;
            default:
                throw new InvalidDayException("ERROR: Day given (" + day + ") is invalid");

        }
        _day = day;
        _startTime = startTime;
        _endTime = endTime;
        _minimumWorkers = minimumWorkers;
    }


    public String get_day() {
        return _day;
    }

    public String get_startTime() {
        return _startTime;
    }

    public String get_endTime() {
        return _endTime;
    }

    /*
     * Assigns the staff to this shift as a worker or manager
     * @param staff
     * @param first name
     * @param family name
     * @param Manager or not
     * @return an empty string if no errors or a description of the error
     */
    public String assignmentOfStaff(Staff staff, String givenName, String familyName, boolean isManager) {

        try {
            if (isManager) {
                Manager manager = new Manager(givenName, familyName);
                _managerOnShift = manager;
                this._hasManager = true;
            } else {
                _listOfWorkers.add(staff);
            }
        } catch (EmptyNameException e) {
            return e.getMessage();
        }

        return "";
    }

    public boolean get_hasManager() {
        return _hasManager;
    }

    public List<Staff> get_workersOnShift() {
        return _listOfWorkers;
    }

    public Staff get_managerOnShift() {
        return _managerOnShift;
    }

    public String get_minimumWorkers() {
        return _minimumWorkers;
    }

    public int get_numWorkerOnShift() {
        return _listOfWorkers.size();
    }

    /*
     * an overridden compareTo method to compare our own way
     * @param other shift object
     * @return a negative integer, zero, or a positive integer if this object is less
     * than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Shift other) {
        int compare =  _dayNumber.compareTo(other._dayNumber);
        if (compare == 0) {
            int one = Integer.parseInt(_startTime.substring(0,2) + _startTime.substring(3,5)); //pick out relevant information
            int two = Integer.parseInt(other._startTime.substring(0,2) + other._startTime.substring(3,5)); //pick out relevant information
            compare = Integer.compare(one, two);
        }
        return compare;
    }
}
