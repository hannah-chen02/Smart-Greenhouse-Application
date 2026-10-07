//SensorReading Class
//Stores one sensor reading from the data.csv file
//Each reading has an ID, a sensor type, a zone, a recorded value and a Timestamp
//contains Timestamp object 
public class SensorReading
{
    //Fields
    //All fields are private so must be accessed through accessor or mutator methods
    private String    sensorID;
    private String    sensorType;
    private String    zone;
    private double    value;
    private Timestamp timestamp;

    //Default constructor
    //creates a SensorReading with placeholder values in every field
    //blank Timestemp is created using timestamp's own constructor
    public SensorReading()
    {
        sensorID   = "UNKNOWN";
        sensorType = "UNKNOWN";
        zone       = "UNKNOWN";
        value      = 0.0;
        timestamp  = new Timestamp();
    }


    //Constructor with parameters
    //Creates a fully populated SensorReading from all 5 parameters.
    //This is the constructor used in parseCSVLine() and addReading()
    public SensorReading(String pSensorID, String pSensorType, String pZone,
                         double pValue, Timestamp pTimestamp)
    {
        sensorID   = pSensorID;
        sensorType = pSensorType;
        zone       = pZone;
        value      = pValue;
        timestamp  = pTimestamp;
    }

    //Copy constructor
    //Creates a new Timestamp object rather than copying reference
    //Therefore both objects remain independent in memory
    public SensorReading(SensorReading pReading)
    {
        sensorID   = pReading.getSensorID();
        sensorType = pReading.getSensorType();
        zone       = pReading.getZone();
        value      = pReading.getValue();
        timestamp  = new Timestamp(pReading.getTimestamp());
    }

    //Accessor methods
    //Each method returns the current value of one private field
    //Other classes use these to read field values without being able to change them
    public String    getSensorID()   { return sensorID;   }
    public String    getSensorType() { return sensorType; }
    public String    getZone()       { return zone;       }
    public double    getValue()      { return value;      }
    public Timestamp getTimestamp()  { return timestamp;  }

    //Mutator methods
    //Each method updates the value of one private field
    //Allows individual fields to be changed after the object has already been created
    public void setSensorID(String pSensorID)      { sensorID   = pSensorID;   }
    public void setSensorType(String pSensorType)  { sensorType = pSensorType; }
    public void setZone(String pZone)              { zone       = pZone;       }
    public void setValue(double pValue)            { value      = pValue;      }
    public void setTimestamp(Timestamp pTimestamp) { timestamp  = pTimestamp;  }


    // Checks whether this reading's value falls outside the known safe range for its sensor type
    // Returns true if outside safe range, false if within or type is unknown
    public boolean isOutsideSafeRange()
    {
        switch (sensorType)
        {
            case "temperature":
                return (value < 18.0 || value > 30.0);
            case "humidity":
                return (value < 40.0 || value > 70.0);
            case "soilMoisture":
                return (value < 30.0 || value > 60.0);
            case "light":
                return (value < 300.0 || value > 1200.0);
            default:
                return false;
        }
    }

    //Returns a formatted string representation to look like a table row
    //example output: TMP332 | temperature | ZoneA | 27.40 | 12/03/2024 14:30
    public String toString()
    {
        return String.format("%-10s | %-12s | %-6s | %8.2f | %s", 
                            sensorID, sensorType, zone, value, timestamp.toString());
    }

}
