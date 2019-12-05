package shiftman.server;

/**
 * A class to compute and checks for validity
 */
public class TimeCalc {

    /*
     * A checker to check if the shift is through midnight and if start time is later than end time
     * @param start time of the shift
     * @param end time of the shift
     * @return true if it doesn't and false if it does
     */
    public static boolean midnightChecker(String startTime, String endTime) {
        // pick out relevant information and change to minutes of the day
        int start = Integer.parseInt(startTime.substring(0,2)) * 60 + Integer.parseInt(startTime.substring(3,5));
        int end = Integer.parseInt(endTime.substring(0,2)) * 60 + Integer.parseInt(endTime.substring(3,5));
        int difference = end - start;

        if (difference > 0) { //checks if the end time is after the start time and if it is before midnight
            return true;
        } else {
            return false;
        }
    }

    /*
     * A checker to check if the new shift overlaps with an existing shift
     * @param start time of the existing shift
     * @param end time of the existing shift
     * @param start time of the new shift
     * @param end time of the new shift
     * @return true if it overlaps and false if it doesn't
     */
    public static boolean overlappingShifts(String startTime1, String endTime1, String startTime2, String endTime2) {
        // pick out relevant information and change to minutes of the day
        int startExisting = Integer.parseInt(startTime1.substring(0,2)) * 60 + Integer.parseInt(startTime1.substring(3,5));
        int endExisting = Integer.parseInt(endTime1.substring(0,2)) * 60 + Integer.parseInt(endTime1.substring(3,5));
        int startNew = Integer.parseInt(startTime2.substring(0,2)) * 60 + Integer.parseInt(startTime2.substring(3,5));
        int endNew = Integer.parseInt(endTime2.substring(0,2)) * 60 + Integer.parseInt(endTime2.substring(3,5));

        if (startNew >= startExisting & startNew < endExisting) { //checks if the starting time is overlapping
            return true;

        } else if (endNew > startExisting & endNew <= endExisting) { //checks if the end time is overlapping
            return true;

        } else if (startNew < startExisting & endNew > endExisting) { //checks if the whole shift overlaps
            return true;

        } else {
            return false;
        }
    }
}
