package app.olly.plant_application.sdata;

import android.text.method.HideReturnsTransformationMethod;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import app.olly.plant_application.R;

/*
 * instance of each plant
 * */
public class Plant extends DefaultPlant {

    /**
     * link to the planttype
     */
    private PlantType plantType;
    /**
     * Last time of water
     */
    private Date last_time;

    /**
     * name of plattype (realise the logic without planttype links)
     */
    private String plantTypeName;


    public static int CONST_WEEK = 7*24*60*60;
    public static int CONST_THREE_DAYS = 3*24*60*60;
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Plant(int period, int id) {
        this.period = period;
        // createOwnPlantType(id);
    }

    public Plant(int image, int period, String name, int id, Date last_time, String plantTypeName) {
        super(image, period, name, id);
        this.last_time = last_time;
        this.plantTypeName = plantTypeName;
    }

    /**
     *
     * @return true if plant need water
     */
    public boolean checkNeedWater() {
        Calendar curtime = new GregorianCalendar();
        curtime.setTime(new Date(System.currentTimeMillis()));
        Calendar c = new GregorianCalendar();
        c.setTime(getLast_time());
        c.add(Calendar.DATE, getPeriod());
        if (c.before(curtime)) {
          return true;
        }
        if (c.get(Calendar.YEAR) == curtime.get(Calendar.YEAR) && c.get(Calendar.DAY_OF_MONTH) == curtime.get(Calendar.DAY_OF_MONTH) && c.get(Calendar.MONTH) == curtime.get(Calendar.MONTH)) {
            return true;
        }
        return false;
    }
    public Plant(PlantType plantType, int image, int period, String name, int id) {
        super(image, period, name, id);
        setPlantType(plantType);
    }

    public Plant(PlantType plantType, int period, String name) {
        this.period = period;
        this.name = name;
        setPlantType(plantType);
    }

    public Plant(PlantType plantType,int period) {
        this.period = period;
        setPlantType(plantType);
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    private void createOwnPlantType(int id) {
        this.plantType = new PlantType(R.drawable.first,1000, "ficus", id );
        //   this.plantType.setImage();   // не забыть добаивть картинку
        this.period = CONST_THREE_DAYS;
    }

    public Date getLast_time() {
        return last_time;
    }

    public void setLast_time(Date last_time) {
        this.last_time = last_time;
    }

    public String getPlantTypeName() {
        return plantTypeName;
    }

    public void setPlantTypeName(String plantTypeName) {
        this.plantTypeName = plantTypeName;
    }
}
