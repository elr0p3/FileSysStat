package r0p3;

public class DataFile {

    private String extention = "";
    private Float size;
    private Float percentage = 0.0f;

    /**
     * Contructor, it requires a file extention
     * */
    public DataFile (String extention) {
        String[] aux = extention.split(".");
        this.extention = "." + aux[aux.length - 1];
        this.size = 0.0f;
    }


    /**
     * Constuctor
     * It requires a file extention and a size
     * */
    public DataFile (String extention, Float size) {
        String[] aux = extention.split(".");
        this.extention = "." + aux[aux.length - 1];
        this.size = size;
    }


    /**
     * Returns the file extention
     * */
    public String getExtention () {
        return extention;
    }

    /**
     * Add a size to that file extention
     * */
    public void addSize (Float s) {
        size += s;
    }

    /**
     * Returns the file size
     * */
    public Float getSize () {
        return size;
    }

    /**
     * Calculate the percentage that represents the total presence of the file with this extension
     * */
    public void calculatePercentage (Float s, Float total) {
        percentage = s * 100 / total;   // regla de 3, ole ole
    }

    /**
     * Returns the file percentage
     * */
    public Float getPercentage () {
        return percentage;
    }

}
