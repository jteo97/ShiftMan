package shiftman.server;

/**
 * A class which extends Staff class which holds information of each manager
 */
public class Manager extends Staff {

    /*
     * Constructs a Manager object
     * @param first name
     * @param family name
     */
    public Manager(String givenname, String familyName) throws EmptyNameException {
        super(givenname, familyName);
    }
}
