package app.olly.plant_application.sdata;

import app.olly.plant_application.R;

/*
 * instance of each plant
 * */
public class Plant extends DefaultPlant {

    /**
     * link to the planttype
     */
    PlantType plantType;

    static int CONST_WEEK = 7*24*60*60;
    static int CONST_THREE_DAYS = 3*24*60*60;

    public Plant(PlantType plantType, int image, int period, String name) {
        super(image, period, name);
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

    public Plant(int period) {
        this.period = period;
        createOwnPlantType();
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    private void createOwnPlantType() {
        this.plantType = new PlantType(R.drawable.first,1000, "ficus" );
        //   this.plantType.setImage();   // не забыть добаивть картинку
        this.period = CONST_THREE_DAYS;
    }
}
