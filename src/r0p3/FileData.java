package r0p3;

public class FileData {

    private String  extention = "";
    private Long    number;
    private Long    size;
    private Float   percentage = 0f;

    /**
     * DataFile constructor
     *
     * @param   extention   a String which represents the file extention
     * */
    public FileData (String extention) {
        this(extention, 0L);
    }


    /**
     * DataFile constuctor
     * It requires a file extention and a size
     *
     * @param   extention   a String which represents the file extention
     * @param   size        a Long which represents the file size
     * */
    public FileData (String extention, Long size) {
        String[] aux = extention.split(".");
        if (aux.length != 0)
            this.extention = aux[aux.length - 1];
        else
            this.extention = extention;
        this.number = 1L;
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
     * Number of files scanned getter
     *
     * @return  number of files scanned
     * */
    public Long getNumberOfFiles () {
        return number;
    }

    /**
     * Add some files, if they were scanned
     * It helps because java hashmaps are miserable
     *
     * @param   n   number of files to add
     * */
    public void addFileNumber (Integer n) {
        number += n;
    }


    /**
     * Add a size to that file extention
     *
     * @param   s   a Long which represents the file size
     * */
    public void addSize (Long s) {
        size += s;
    }

    /**
     * File size getter
     *
     * @return  a Long which represents the file size
     * */
    public Long getSize () {
        return size;
    }

    /**
     * Calculate the percentage that represents the total presence of the file with this extension
     *
     * @param   s       a Long which represents the file size
     * @param   total   a Long which represents the total disk size
     * */
    public void calculateTotalSizePercentage (Long s, Long total) {
        percentage = (float)s * 100 / total;   // regla de 3, ole ole
    }

    /**
     * File percentage getter
     *
     * @return  a Long which represents the file percentage
     * */
    public Float getPercentage () {
        return percentage;
    }

}
