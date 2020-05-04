package r0p3;

public class DataFile {

    private String extention = "";
    private Float size;
    private Float percentage = 0.0f;

    /**
     * DataFile constructor
     *
     * @param   extention   a String which represents the file extention
     * */
    public DataFile (String extention) {
        String[] aux = extention.split(".");
        this.extention = "." + aux[aux.length - 1];
        this.size = 0.0f;
    }


    /**
     * DataFile constuctor
     * It requires a file extention and a size
     *
     * @param   extention   a String which represents the file extention
     * @param   size        a Float which represents the file size
     * */
    public DataFile (String extention, Float size) {
        String[] aux = extention.split(".");
        this.extention = "." + aux[aux.length - 1];
        this.size = size;
    }


    /**
     * File extention getter
     *
     * @return  the file extention
     * */
    public String getExtention () {
        return extention;
    }

    /**
     * Add a size to that file extention
     *
     * @param   s   a Float which represents the file size
     * */
    public void addSize (Float s) {
        size += s;
    }

    /**
     * File size getter
     *
     * @return  a Float which represents the file size
     * */
    public Float getSize () {
        return size;
    }

    /**
     * Calculate the percentage that represents the total presence of the file with this extension
     *
     * @param   s       a Float which represents the file size
     * @param   total   a Float which represents the total disk size
     * */
    public void calculatePercentage (Float s, Float total) {
        percentage = s * 100 / total;   // regla de 3, ole ole
    }

    /**
     * File percentage getter
     *
     * @return  a Float which represents the file percentage
     * */
    public Float getPercentage () {
        return percentage;
    }

}
