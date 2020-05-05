package r0p3;

public class FileData {

    private String extention = "";
    private Integer number;
    private Double size;
    private Double percentage = 0.0;

    /**
     * DataFile constructor
     *
     * @param   extention   a String which represents the file extention
     * */
    public FileData (String extention) {
        String[] aux = extention.split(".");
        this.number = 1;
        this.extention = "." + aux[aux.length - 1];
        this.size = 0.0;
    }


    /**
     * DataFile constuctor
     * It requires a file extention and a size
     *
     * @param   extention   a String which represents the file extention
     * @param   size        a Double which represents the file size
     * */
    public FileData (String extention, Double size) {
        String[] aux = extention.split(".");
        this.number = 1;
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
     *
     * */
    public Integer getNumberOfFiles () {
        return number;
    }

    /**
     *
     * */
    public void addFileNumber (Integer n) {
        number += n;
    }


    /**
     * Add a size to that file extention
     *
     * @param   s   a Double which represents the file size
     * */
    public void addSize (Double s) {
        size += s;
    }

    /**
     * File size getter
     *
     * @return  a Double which represents the file size
     * */
    public Double getSize () {
        return size;
    }

    /**
     * Calculate the percentage that represents the total presence of the file with this extension
     *
     * @param   s       a Double which represents the file size
     * @param   total   a Double which represents the total disk size
     * */
    public void calculatePercentage (Double s, Double total) {
        percentage = s * 100 / total;   // regla de 3, ole ole
    }

    /**
     * File percentage getter
     *
     * @return  a Double which represents the file percentage
     * */
    public Double getPercentage () {
        return percentage;
    }

}
