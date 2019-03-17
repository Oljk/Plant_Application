package app.olly.plant_application.sdata;

/**
 * structure of plant
 * */
public abstract class DefaultPlant {

    public static final String CONST_CREATE= "create";
    /**
     * Generating id for unique objects
     */
        int id;

        /**
         * image of item

         * */
        int image;

        /**
         * period of water in seconds
         */
        int period;

        /**
         * name
         */
        String name;

    public DefaultPlant() {
        }

        public DefaultPlant(int image, int period, String name, int id) {
            this.image = image;
            this.period = period;
            this.name = name;
            this.id = id;
        }


        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int generateNewId(int prevMax) {
            return prevMax + 1;
        }

    }
