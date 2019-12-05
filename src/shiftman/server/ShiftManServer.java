package shiftman.server;

import java.util.ArrayList;
import java.util.List;

/*
 * This class implements the ShiftMan interface and implements all the methods in there
 * Some methods have a try/catch clause
*/
public class ShiftManServer implements ShiftMan {

    private Roster _roster = null;


    public String newRoster(String shopName) {
        _roster = new Roster(shopName);
        return "";
    }

    public String setWorkingHours(String dayOfWeek, String startTime, String endTime) {

        try {
            _roster.settingWorkingHours(dayOfWeek, startTime, endTime);
        } catch (InvalidDayException | InvalidTimeException e) {
            return e.getMessage();
        }

        return "";
    }

    public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {

        try {
            return _roster.addingShift(dayOfWeek, startTime, endTime, minimumWorkers);
        } catch (InvalidTimeException e) {
            return e.getMessage();
        }
    }

    public String registerStaff(String givenname, String familyName) {

        try {
            return _roster.registeringStaff(givenname, familyName);
        } catch (StaffAlreadyRegisteredException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, String familyName, boolean isManager) {

        try {
            return _roster.assigningStaff(dayOfWeek, startTime, endTime, givenName, familyName, isManager);
        } catch (InvalidDayException e) {
            return e.getMessage();
        }
    }

    public List<String> getRegisteredStaff() {
        List<String> listOfStaff = new ArrayList<>();
        _roster.getRegisteredStaff(listOfStaff);
        return listOfStaff;
    }

    public List<String> getUnassignedStaff() {
        List<String> listOfUnassignedStaff = new ArrayList<>();
        _roster.getUnassignedStaff(listOfUnassignedStaff);
        return listOfUnassignedStaff;
    }

    public List<String> shiftsWithoutManagers() {
        List<String> noManager = new ArrayList<>();
        _roster.shiftsWithoutManagers(noManager);
        return noManager;
    }

    public List<String> understaffedShifts() {
        List<String> underStaffed = new ArrayList<>();
        _roster.understaffedShifts(underStaffed);
        return underStaffed;
    }

    public List<String> overstaffedShifts() {
        List<String> overStaffed = new ArrayList<>();
        _roster.overstaffedShifts(overStaffed);
        return overStaffed;
    }

    public List<String> getRosterForDay(String dayOfWeek) {
        List<String> rosterForDay = new ArrayList<>();
        try {
            _roster.getRosterForDay(rosterForDay, dayOfWeek);
        } catch (InvalidDayException e) {
            rosterForDay.add(e.getMessage());
            return rosterForDay;
        }
        return rosterForDay;
    }

    public List<String> getRosterForWorker(String workerName) {
        List<String> workerRoster = new ArrayList<>();
        _roster.getRosterForWorker(workerRoster, workerName);
        return workerRoster;
    }

    public List<String> getShiftsManagedBy(String managerName) {
        List<String> managerRoster = new ArrayList<>();
        _roster.getShiftsManagedBy(managerRoster, managerName);
        return managerRoster;
    }

    public String reportRosterIssues() {

        return null;
    }

    public String displayRoster() {

        return null;
    }
}