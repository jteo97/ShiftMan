package shiftman.server;

/**
 * A class that holds information of each staff
 */
public class Staff implements Comparable<Staff>{

    private String _givenName;
    private String _familyName;

    /*
     * Constructs a staff object
     * @param first name
     * @param family name
     */
    public Staff(String givenname, String familyName) throws EmptyNameException{
        if (givenname.equals("") || familyName.equals("")) {
            throw new EmptyNameException("ERROR: Either one or both names are empty");
        }
        _givenName = givenname;
        _familyName = familyName;
    }

    public String get_givenName() {
        return _givenName;
    }

    public String get_familyName() {
        return _familyName;
    }

    /*
     * an overridden compareTo method to compare our own way
     * @param other staff object
     * @return a negative integer, zero, or a positive integer if this object is less
     * than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Staff other) {
        return _familyName.compareTo(other._familyName);
    }
}