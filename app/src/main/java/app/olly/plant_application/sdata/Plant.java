package app.olly.plant_application.sdata;

import java.text.SimpleDateFormat;
import java.util.Date;

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


    static int CONST_WEEK = 7*24*60*60;
    static int CONST_THREE_DAYS = 3*24*60*60;
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyy-mm-dd");

    public Plant(int period, int id) {
        this.period = period;
        // createOwnPlantType(id);
    }

    public Plant(int image, int period, String name, int id, Date last_time, String plantTypeName) {
        super(image, period, name, id);
        this.last_time = last_time;
        this.plantTypeName = plantTypeName;
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
