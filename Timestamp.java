//Timestamp class
//Stores date and time information for a sensor reading
//SensorReading object contains a Timestamp object as one of its fields

public class Timestamp
{
    //Fields
    //All fields are private so must be accessed through accessor or mutator methods
    private int dayOfMonth;
    private int monthOfYear;
    private int year;
    private int hour;
    private int minute;

    //Default constructor
    //creates a Timestamp with all fields set to zero
    //used when Timestamp object is needed but does not have any date or time information
    public Timestamp()
    {
        dayOfMonth  = 0;
        monthOfYear = 0;
        year        = 0;
        hour        = 0;
        minute      = 0;
    }

    //Constructor with Parameters
    //Populates constructors with populated values
    public Timestamp(int pDay, int pMonth, int pYear, int pHour, int pMinute)
    {
        dayOfMonth  = pDay;
        monthOfYear = pMonth;
        year        = pYear;
        hour        = pHour;
        minute      = pMinute;
    }

    //Copy Constructor
    //Creates a timestamp by copying every field from existing Timestamp
    public Timestamp(Timestamp pTimestamp)
    {
        dayOfMonth  = pTimestamp.getDayOfMonth();
        monthOfYear = pTimestamp.getMonthOfYear();
        year        = pTimestamp.getYear();
        hour        = pTimestamp.getHour();
        minute      = pTimestamp.getMinute();
    }

    //Accessor methods
    //Each method returns the current value of one private field
    //Other classes use these fields without being able to change them
    public int getDayOfMonth()  { return dayOfMonth;  }
    public int getMonthOfYear() { return monthOfYear; }
    public int getYear()        { return year;        }
    public int getHour()        { return hour;        }
    public int getMinute()      { return minute;      }

    //Mutator methods
    //Each method updates the value of one private field
    //Allows individual fields to be changed after object has already been created
    public void setDayOfMonth(int pDay)    { dayOfMonth  = pDay;    }
    public void setMonthOfYear(int pMonth) { monthOfYear = pMonth;  }
    public void setYear(int pYear)         { year        = pYear;   }
    public void setHour(int pHour)         { hour        = pHour;   }
    public void setMinute(int pMinute)     { minute      = pMinute; }

    //Returns a formatted string representation
    //Example output: 12/03/2024 14:30
    public String toString()
    {
        return String.format("%02d/%02d/%04d %02d:%02d",
                             dayOfMonth, monthOfYear, year, hour, minute);
    }
}
