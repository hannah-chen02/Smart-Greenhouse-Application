//File: GreenhouseMenu.java
//Author: Hannah Chen
//UNIT: COMP1007
//COMMENTS: The arrays hold readings for a Greenhouse that can updated and deleted
//REQUIRES: SensorReading.java and Timestamp.java
//LAST MOD: 12/05/2026

import java.util.Scanner;
import java.io.*;

public class GreenhouseMenu
{
    public static void main(String[] args)
    {
        //variable declarations
        int maxReadings = 5000;
        String csvFile = "data.csv";
        String logFile = "20610447_logFile.txt";

        //valid sensor types, stored in arrays
        String[] validTypes = {"temperature", "humidity", "soilMoisture", "light"};
        String[] validZones = {"ZoneA", "ZoneB", "ZoneC"};
        
        //scanner to read keyboard input from the user
        Scanner sc = new Scanner(System.in);

        //allocate the master array that holds all SensorReading objects
        //starts empty
        SensorReading[] readings = new SensorReading[maxReadings];

        //one element array to track how many readings are stored
        //the method receives the reference and changes countHolder
        int[] countHolder = new int[1];
        countHolder[0] = 0;

        //open log file
        PrintWriter logWriter = openLogWriter(logFile);

        //auto load CSV file on startup
        logAndPrint(logWriter, "=== Smart Greenhouse Monitoring System ===");
        logAndPrint(logWriter, "Loading data from: " + csvFile);

        //loads all of the sensor readings from the CSV file, with countHolder[0]
        //telling us how many rows were loaded
        loadCSV(csvFile, readings, countHolder, maxReadings, validTypes, validZones, logWriter);
        //tells user how many readings were loaded
        logAndPrint(logWriter, "Loaded " + countHolder[0] + " readings.\n");

        //main menu loop
        boolean running = true;
        while (running)
        {
            printMainMenu();
            int choice = readInt(sc);

            switch (choice)
            {
                //case 1: statistics for greenhouse menu
                //case 1 passes null for filterZone and filterType - includes all readings
                case 1:
                    logAndPrint(logWriter, "\n=== Statistics: Entire Greenhouse ===");
                    showStatisticsMenu(readings, countHolder[0], null, null, logWriter, sc);
                    break;
                //case 2: statistics filtered by a specific zone
                case 2:
                    logAndPrint(logWriter, "\n=== Statistics: By Sensor Zone ===");
                    System.out.println("Available zones: 'ZoneA', 'ZoneB', 'ZoneC'");
                    System.out.print("Please enter zone: ");
                    String zone = readValidZone(validZones, sc);
                    showStatisticsMenu(readings, countHolder[0], zone, null, logWriter, sc);
                    break;
                //case 3: statistics filtered by a specific sensor type
                case 3:
                    logAndPrint(logWriter, "\n=== Statistics: By Sensor Type ===");
                    System.out.println("Available types: 'temperature', 'humidity', 'soilMoisture', 'light'");
                    System.out.print("Please enter type: ");
                    String type = readValidSensorType(validTypes, sc);
                    showStatisticsMenu(readings, countHolder[0], null, type, logWriter, sc);
                    break;
                //case 4: user wants to manually add a new sensor reading
                case 4:
                    addReading(readings, countHolder, maxReadings, validTypes, validZones, logWriter, sc);
                    break;
                //case 5: user wants to delete an exisitng sensor reading by its sensor ID
                case 5:
                    deleteReading(readings, countHolder, logWriter, sc);
                    break;
                //case 6: user wants to exit the Menu. Sets running to false so while loop ends.
                case 6:
                    logAndPrint(logWriter, "\n=== Exiting program. Goodbye! ===");
                    running = false;
                    break;
                //The user did not type an integer 1 through 6.
                default:
                    System.out.println("\nInvalid choice. Please enter integers 1-6.");
                    break;

            }
        }
        //close the Scanner as no more user input
        sc.close();

        //close the log file - checks for null first in case log file never opened at startup
        if (logWriter != null)
        {
            logWriter.flush();
            logWriter.close();
        }
    }
    //menu display
    private static void printMainMenu()
    {
        System.out.println("\n=== Welcome to the Smart Greenhouse Monitoring System. ===");
        System.out.println("\n Please select an option by typing the integer for the menu option:");
        System.out.println("\n 1. Statistics for the entire greenhouse");
        System.out.println("\n 2. Statistics by zone");
        System.out.println("\n 3. Statistics by sensor type");
        System.out.println("\n 4. Add data readings");
        System.out.println("\n 5. Delete data readings");
        System.out.println("\n 6. Exit program");
        System.out.print("\n Enter choice: ");
    }
    //submenu display
    private static void printStatisticsMenu()
    {
        System.out.println("\nSelect a statistic by typing the integer for the menu option:");
        System.out.println("\n 1. Total number of readings");
        System.out.println("\n 2. Average value");
        System.out.println("\n 3. Minimum value");
        System.out.println("\n 4. Maximum value");
        System.out.println("\n 5. Number of readings outside safe range");
	System.out.println("\n 6. Percentage of readings outside safe range");
        System.out.println("\n 7. All statistics");
        System.out.print("Enter choice: ");
    }
    //statistics sub-menu
    //runs the chosen calculation
    public static void showStatisticsMenu(SensorReading[] readings, int count, String filterZone, String filterType, PrintWriter logWriter, Scanner sc)
    {
        printStatisticsMenu();
        int choice = readInt(sc);

        switch (choice)
        {  
            //case 1: calculate and display the total number of matching readings
            case 1:
                printTotal(readings, count, filterZone, filterType, logWriter);
                break;
            //case 2: calculate and display the average value of matching readings
            case 2:
                printAverage(readings, count, filterZone, filterType, logWriter);
                break;
            //case 3: calculate and displaty the lowest value among matching readings
            case 3:
                printMin(readings, count, filterZone, filterType, logWriter);
                break;
            //case 4: calculate and display the highest value among matching readings
            case 4:
                printMax(readings, count, filterZone, filterType, logWriter);
                break;
            //case 5: count and display how many matching readings are outside the safe range
            case 5:
                printOutsideCount(readings, count, filterZone, filterType, logWriter);
                break;
            //case 6: calculate and display the percentage of matching readings outside the safe range
            case 6:
                printOutsidePercent(readings, count, filterZone, filterType, logWriter);
                break;
            //case 7: run all six statistics one after the other
            case 7:
                printTotal(readings, count, filterZone, filterType, logWriter);
                printAverage(readings, count, filterZone, filterType, logWriter);
                printMin(readings, count, filterZone, filterType, logWriter);
                printMax(readings, count, filterZone, filterType, logWriter);
                printOutsideCount(readings, count, filterZone, filterType, logWriter);
                printOutsidePercent(readings, count, filterZone, filterType, logWriter);
                break;
            //the user has typed something outside of 1-7
            default:
                System.out.println("Invalid choice. Returning to the main menu.");
                break;
        }
    }
    //Statistics calculation methods
    //calcTotal - counts how many readings in the array match the given filters
    public static int calcTotal(SensorReading[] readings, int count, String filterZone, String filterType)
    {
        //initialises counter as zero
        int total = 0;
        int i = 0;

        //goes through every reading that has been loaded up to count
        while (i < count)
        {
            if (matchesFilter(readings[i], filterZone, filterType))
            {
                total++;
            }
            i++;    
        }
        return total;
    }
    //calcAverage - Calculates the average value across matching readings
    public static double calcAverage(SensorReading [] readings, int count, String filterZone, String filterType)
    {
        double sum = 0.0;
        int total = 0;
        int i = 0;
        while (i < count)
        {
            if (matchesFilter(readings[i], filterZone, filterType))
            {
                //add this reading's value to running total
                sum += readings[i].getValue();
                total++;
            }
            i++;
        }
        //returns 0.0 if none of the values match
        if (total == 0)
        {
            return 0.0;
        }
        //divide sum by the number of matching readings to get the average
        return sum / total;
    }
    //calcMin- Returns the minimum value across matching readings
    public static double calcMin(SensorReading[] readings, int count, String filterZone, String filterType)
    {
        //starts with the largest possible double so any real value will be lower
        double min = Double.MAX_VALUE;
        //track whether a matching reading has been found
        boolean found = false;
        int i = 0;
        while (i < count)
        {
            if(matchesFilter(readings[i], filterZone, filterType))
            {  
                //if this value is smaller than current minimum, update minimum
                if (readings[i].getValue() < min)
                {
                    min = readings[i].getValue();
                }
                found = true;
            }
            i++;
        }
        //returns 0.0 if none match
        if (!found)
        {
            return 0.0;
        }
        return min;
    }
    //calcMax - Returns the maximum value across matching readings
    public static double calcMax(SensorReading[] readings, int count, String filterZone, String filterType)
    {
        //starts with the most negative possible double so any real value will be higher
        double max = -Double.MAX_VALUE;
        //track whether a matching reading has been found
        boolean found = false;
        int i = 0;
        while (i < count)
        {
            if(matchesFilter(readings[i], filterZone, filterType))
            {
                //if this value is bigger than current maximum, update maximum
                if (readings[i].getValue() > max)
                {
                    max = readings[i].getValue();
                }
                found = true;
            }
            i++;  
        }
        //returns 0.0 if none match
        if (!found)
        {
            return 0.0;
        }
        return max;
    }
    //calcOutsideCount - Returns the count of matching readings whose value is outside the safe range
    public static int calcOutsideCount(SensorReading[] readings, int count, String filterZone, String filterType)
    {
        int outside = 0;
        int i = 0;
        while(i < count)
        {
            if (matchesFilter(readings[i], filterZone, filterType))
            {  
                //asks the reading whether its value is outside the safe range
                if (readings[i].isOutsideSafeRange())
                {
                    outside++;
                }
            }
            i++;
        }
        return outside;
    }
    //Print and log helpers for each statistic in the menu
    //printTotal - calls calcTotal to get the count, prints and logs the result
    public static void printTotal(SensorReading[] readings, int count, String filterZone, String filterType, PrintWriter lw)
    {
        int total = calcTotal(readings, count, filterZone, filterType);
        logAndPrint(lw, "Total readings: " + total);
        
        //print the details of the matching reading below the count
        int i = 0;
        while (i < count)
        {
            if(matchesFilter(readings[i], filterZone, filterType))
            {
                logAndPrint(lw, readings[i].toString());
            }
            i++;
        }
    }
    //printAverage - calls calcAverage to get the result, then prints and logs it to two decimal places
    public static void printAverage(SensorReading[] readings, int count, String filterZone, String filterType, PrintWriter lw)
    {
        double avg = calcAverage(readings, count, filterZone, filterType);
        logAndPrint(lw, String.format("Average value: %.2f", avg));
    }
    //printMin - calls calcMin to get the result, then prints and logs it
    public static void printMin(SensorReading[] readings, int count, String filterZone, String filterType, PrintWriter lw)
    {
        double min = calcMin(readings, count, filterZone, filterType);
        logAndPrint(lw, String.format("Minimum value: %.2f", min));
    }
    //printMax - calls calcMax to get the result, then prints and logs it
    public static void printMax(SensorReading[] readings, int count, String filterZone, String filterType, PrintWriter lw)
    {
        double max = calcMax(readings, count, filterZone, filterType);
        logAndPrint(lw, String.format("Maximum value: %.2f", max));
    }
    //printOutsideCount - calls calcOutsideCount to get the result, then prints and logs it
    public static void printOutsideCount(SensorReading[] readings, int count, String filterZone, String filterType, PrintWriter lw)
    {
        int outside = calcOutsideCount(readings, count, filterZone, filterType);
        logAndPrint(lw, "Outside safe range (count): " + outside);
    }
    //printOutsidePercent - calculates the percentage of matching readings outside the safe range, then prints and logs it
    //gets total count and outside count, then divides
    public static void printOutsidePercent(SensorReading[] readings, int count, String filterZone, String filterType, PrintWriter lw)
    {
        int total = calcTotal(readings, count, filterZone, filterType);
        int outside = calcOutsideCount(readings, count, filterZone, filterType);
        //casting to double ensures result is in decimal rather than integer
        double pct = (total == 0) ? 0.0 : ((double) outside / total) * 100.0;
        logAndPrint(lw, String.format("Outside safe range (%%): %.2f%%", pct));
    }
    //matchesFilter - null filter value means "accept all" for that field
    public static boolean matchesFilter(SensorReading r, String filterZone, String filterType)
    {
        //a null reading means array slot is empty, so skip
        if (r == null)
        {
            return false;
        }
        //check the zone filter
        //if filterZone is null, accept any zone otherwise set to required filter
        boolean zoneOk = (filterZone == null) || r.getZone().equals(filterZone);
        //check the sensor type filter in the same way
        boolean typeOk = (filterType == null) || r.getSensorType().equals(filterType);
        //both conditions must be met for the reading to pass the filter
        return zoneOk && typeOk;
    }
    //addReading - add a reading manually
    //maxReadings and the valid-value arrays are passed in as parameters
    public static void addReading(SensorReading[] readings, int[] countHolder, int maxReadings, String[] validTypes, String[] validZones, PrintWriter lw, Scanner sc)
    {
        //check if array is already full before trying to add anything
        if (countHolder[0] >= maxReadings)
        {
            logAndPrint(lw, "Array is full. Cannot add more readings.");
            return;
        }
        System.out.println("\n--- Add New Reading ---");
        System.out.print("Enter Sensor ID: ");
        String sensorID = readString(sc);
        System.out.print("Enter Sensor Type (temperature/humidity/soilMoisture/light) ");
        String sensorType = readValidSensorType(validTypes, sc);
        System.out.print("Enter Zone (ZoneA/ZoneB/ZoneC) ");
        String zone = readValidZone(validZones, sc);
        System.out.print("Enter value including decimal values: ");
        double value = readDouble(sc);
        
        //date validation check
        int day = 0;
        int month = 0;
        int year = 0;
        int hour = 0;
        int minute = 0;
        boolean validDate = false;
        while (!validDate)
        {
            System.out.print("Enter day (1-31): ");
            day = readIntRange(sc, 1, 31);
            System.out.print("Enter month (1-12): ");
            month = readIntRange(sc, 1, 12);
            System.out.print("Enter year (2000-2100): ");
            year = readIntRange(sc, 2000, 2100);

            if (isValidDate(day, month, year))
            {
                validDate = true;
            }
            else
            {
                System.out.println("Invalid date combination. Please re-enter.");
            }
        }
        System.out.print("Enter hour (0-23): ");
        hour = readIntRange(sc, 0, 23);
        System.out.print("Enter minute (0-59): ");
        minute = readIntRange(sc, 0, 59);
        
        //creates a Timestamp object from the fate and time values that the user types
        Timestamp     ts = new Timestamp(day, month, year, hour, minute);
        //creates the SensorReading object using the Timestamp just made
        SensorReading sr = new SensorReading(sensorID, sensorType, zone, value, ts);

        //Places the new reading into the next free slot in array
        readings[countHolder[0]] = sr;
        //Increase the count so next addition goes into the slot after this one
        countHolder[0]++;

        logAndPrint(lw, "Reading added: " + sr.toString());
    }
    //deleteReading - delete a reading by sensor ID
    public static void deleteReading(SensorReading[] readings, int[] countHolder, PrintWriter lw, Scanner sc)
    {
        //if there are zero readings, cannot delete anything.
        if (countHolder[0] == 0)
        {
            logAndPrint(lw, "There are no readings to delete.");
            return;
        }
        System.out.print("\nEnter Sensor ID to delete: ");
        String target = readString(sc);

        //searches array for reading whose sensorID matches what user has typed
        //foundIndex will stay at -1 if nothing matches
        int foundIndex = -1;
        int i = 0;
        while (i < countHolder[0])
        {
            if(readings[i].getSensorID().equals(target))
            {  
                //records where we found it
                foundIndex = i;
            }
            i++;
        }
        //if match not found, tells the user and return
        if (foundIndex == -1)
        {
            logAndPrint(lw, "SensorID " + target + " not found.");
            return;
        }
        //shifts every reading after deleted one to one position to the left
        //ensures no gaps - no null readings in the array
        int j = foundIndex;
        while (j < countHolder[0] - 1)
        {
            readings[j] = readings[j + 1];
            j++;
        }
        //last used slot now holds a duplicate reference, so clear it to null
        readings[countHolder[0] - 1] = null;
        //reduce the count by one as a reading is removed
        countHolder[0]--;
        logAndPrint(lw, "Deleted reading with Sensor ID: " + target);        
    }
    //loadCSV - opens the CSV file, reads it line by line
    //turns the line into a SensorReading object
    public static void loadCSV(String fileName, SensorReading[] readings, int[] countHolder, int maxReadings, String[] validTypes, String[] validZones, PrintWriter lw)
    {
        FileInputStream fis = null;
        InputStreamReader isr;
        BufferedReader br;
       
        try
        {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
           
            //read and negate first line - it is the header row
            String line = br.readLine();
           
            //read the first actual data line
            line = br.readLine();

            if (line == null)
            {
                logAndPrint(lw, "Warning: CSV file is empty");
                fis.close();
                return;
            }
            //keep reading lines until we run out of lines or array is full
            while (line != null && countHolder[0] < maxReadings)
            {
                SensorReading sr = parseCSVLine(line, validTypes, validZones);
               
                //parseCSVLine returns null if the line was malformed or invalid
                //only stores if it comes back as a real object
                if (sr != null)
                {
                    readings[countHolder[0]] = sr;
                    countHolder[0]++;
                }
                line = br.readLine();
            }
            br.close();
            fis.close();
        }
        catch (IOException e)
	    {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException ex)
                {
                    // already failing, nothing to do
                    logAndPrint(lw, "Error loading CSV: " + e.getMessage());
                }   
            }
        }
    }
    //parseCSVLine - parses one CSV line into a SensorReading object
    //returns null if the line is wrong so caller knows to skip
    //format: day,month,year,hour,minute,sensorID,sensorType,zone,value
    public static SensorReading parseCSVLine(String line, String[] validTypes, String[] validZones)
    {
        //splits the line at a comma
        String[] parts = line.split(",");

        //there should only be 9 columns - the line is bad otherwise
        if (parts.length != 9)
        {
            return null;
        }
        try
        {
            //parse each column into the correct data type
            //trim() removes accidental spaces around the value
            int day = Integer.parseInt(parts[0].trim());
            int month = Integer.parseInt(parts[1].trim());
            int year = Integer.parseInt(parts[2].trim());
            int hour = Integer.parseInt(parts[3].trim());
            int minute = Integer.parseInt(parts[4].trim());
            String sensorID = parts[5].trim();
            String sensorType = parts[6].trim();
            String zone = parts[7].trim();
            double value = Double.parseDouble(parts[8].trim());
           
            //check sensorType and sensorZone is recognised - if not the row is skipped and returned as null
            if (!isValidType(sensorType, validTypes) || !isValidZone(zone, validZones))
            {
                return null;
            }
            
            //check if date is impossible - reject if not a valid date
            if (!isValidDate(day, month, year))
            {
                return null;
            }
            //Build Timestamp and SensorReading objects and return the reading
            Timestamp ts = new Timestamp(day, month, year, hour, minute);
            return new SensorReading(sensorID, sensorType, zone, value, ts);

        }
        //if parseInt or parseDouble failed, the row had non-numeric data
        //where a number was expected - it is skipped and returned as null
        catch (NumberFormatException e)
        {
            return null;
        }
    }
    //isValidType - checks if given sensor type string exists in the validTypes array
    public static boolean isValidType(String type, String[] validTypes)
    {
        int i = 0;
        while (i < validTypes.length)
        {
            //if (validTypes[i].equals(type))
            if (validTypes[i].toLowerCase().equals(type.toLowerCase()))
            {
                return true;
            }
            i++;
        }
        return false;
    }
    //isValidZone - checks whether given zone string exists in validZones array
    public static boolean isValidZone(String zone, String[] validZones)
    {
        int i = 0;
        while (i < validZones.length)
        {
           // if (validZones[i].equals(zone))
            if (validZones[i].toLowerCase().equals(zone.toLowerCase()))
            {
                return true;
            }
            i++;
        }
        return false;
    }
    //readValidSensorType - validates the Sensor type that user has selected
    public static String readValidSensorType(String[] validTypes, Scanner sc)
    {
        String type = readString(sc);
        while(!isValidType(type, validTypes))
        {
            System.out.print("Invalid type. Enter temperature, humidity, soilMoisture, or light: ");
            type = readString(sc);
        }
        int i = 0;
        while (i < validTypes.length)
        {
            if (validTypes[i].toLowerCase().equals(type.toLowerCase()))
            {
                return validTypes[i];
            }
            i++;
        }
        return type;
    }
    //readValidZone - validates the Zone that user has selected
    public static String readValidZone(String[] validZones, Scanner sc)
    {
        String zone = readString(sc);
        while(!isValidZone(zone, validZones))
        {
            System.out.print("Invalid zone. Enter ZoneA, ZoneB, or ZoneC: ");
            zone = readString(sc);
        }
        int i = 0;
        while (i < validZones.length)
        {
            if (validZones[i].toLowerCase().equals(zone.toLowerCase()))
            {
                return validZones[i];
            }
            i++;
        }
        return zone;
    }
    //readString - String scanner for user input
    public static String readString(Scanner sc)
    {
        //.trim() to get rid of whitespace from both ends of input
        String input = sc.nextLine().trim();
       
        while(input.isEmpty())
        {
            System.out.print("Input cannot be empty: ");
            input = sc.nextLine().trim();
        }
        return input;
    }
    //readInt - Int scanner for user input
    public static int readInt(Scanner sc)
    {
        int result = -1;
        boolean valid = false;

        while(!valid)
        {
            String input = readString(sc);
            try
            {
                result = Integer.parseInt(input);
                valid = true;
            }
            catch (NumberFormatException e)
            {
                System.out.print("Please enter a whole number: ");
            }
        }
        return result;
    }
    //readDouble - Double scanner for user input
    public static double readDouble(Scanner sc)
    {
        double result = 0.0;
        boolean valid = false;

        while (!valid)
        {
            String input = readString(sc);
            try
            {
                result = Double.parseDouble(input);
                valid = true;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Input error! Please enter a numeric value: ");
            }
        }
        return result;
    }
    //readIntRange - reads an integer from the keyboard and ensures it falls within
    //the range that is specified through min and max values (inclusive)
    public static int readIntRange(Scanner sc, int min, int max)
    {
        int result = -1;
        boolean valid = false;

        while (!valid)
        {
            String input = readString(sc);
            try
            {
                result = Integer.parseInt(input);
                if (result >= min && result <= max)
                {
                    valid = true;
                }
                else
                {
                    //valid integer but outside the specified min and max
                    System.out.print("Please enter a value between " + min + " and " + max + ": ");
                }
            }
            catch (NumberFormatException e)
            {
                //character input was not a number at all
                System.out.print("please enter a whole number between " + min + " and " + max + ": ");
            }
        }
        return result;


    }
    //isValidDate - checks that the day given is valid for the given month and year
    //acounts for leap years and the different number of days in each month
    public static boolean isValidDate(int day, int month, int year)
    {
        if (month < 1 || month > 12)
        {
            return false;
        }
    
        //index 0 is unused therefore month maps directly to index value
        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
        //leap year check
        //leap year is divisble by 4 AND not divisible by 100 OR divisble by 400 (to catch 2100)
        if (month == 2)
        {
            boolean isLeap = (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
            if (isLeap)
            {
                daysInMonth[2] = 29;
            }
        }
        //day must be at least 1 and no more than the max for that month
        return (day >= 1 && day <= daysInMonth[month]);
    }
    //openLogWriter - opens the log file for writing and returns a PrintWriter to write lines to
    //false argument to fos means the file is overwritten each run, not appended to
    //returns null if file could not be opened, so callers must check for null before using writer
    public static PrintWriter openLogWriter(String fileName)
    {
        PrintWriter pw = null;
        try
        {
            FileOutputStream fos = new FileOutputStream(fileName, false);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            pw = new PrintWriter(osw);
        }
        catch (IOException e)
        {
            System.out.println("Warning: Cannot open log file: " + e.getMessage());
        }
        return pw;
    }
    //logAndPrint - prints a message to the screen using System.out.println
    //also writes the same message to the log file
    public static void logAndPrint(PrintWriter lw, String message)
    {
        System.out.println(message);
        if (lw != null)
        {
            lw.println(message);
            lw.flush();
        }
    }  
}
