package r0p3;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FileData {

    private String  extension = "";
    private Long    number;
    private Long    size;
    private Float   percentage = 0f;

    /**
     * DataFile constructor
     *
     * @param   extension   a String which represents the file extension
     * */
    public FileData (String extension) {
        this(extension, 0L);
    }


    /**
     * DataFile constuctor
     * It requires a file extension and a size
     *
     * @param   extension   a String which represents the file extension
     * @param   size        a Long which represents the file size
     * */
    public FileData (String extension, Long size) {
        String[] aux = extension.split(".");
        if (aux.length != 0)
            this.extension = aux[aux.length - 1];
        else
            this.extension = extension;
        this.number = 1L;
        this.size = size;
    }


    /**
     * File extension getter
     *
     * @return  the file extension
     * */
    public String getExtension () {
        return extension;
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
     * Number of files setter
     *
     * @param f     number of files
     * */
    public void setNumberOfFiles (Long f) {
        number = f;
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
     * Add a size to that file extension
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
     * Human readable size getter, it comes with the unit
     *
     * @return  a String with the formated size
     * */
    public String getSizeUnit () {
        return getSizeUnit(this.size);
    }

    /**
     * Human readable size getter, it comes with the unit
     * And it let us transform an entered value
     *
     * @param zs    the size to be transformed
     * @return  a String with the formated size
     * */
    public static String getSizeUnit (Long sz) {
        Float value = 0f;

        if (sz < 1024)
            return sz.toString() + " B";
        
        value = sz / 1024f;
        if (value < 1024)
            return value.toString() + " KiB";

        value = value / 1024f;
        if (value < 1024)
            return value.toString() + " MiB";

        value = value / 1024f;
        if (value < 1024)
            return value.toString() + " GiB";

        value = value / 1024f;
        if (value < 1024)
            return value.toString() + " TiB";

        value = value / 1024f;
        if (value < 1024)
            return value.toString() + " PiB";

        value = value / 1024f;
        if (value < 1024)
            return value.toString() + " EiB";
        
        value = value / 1024f;
        if (value < 1024)
            return value.toString() + " ZiB";

        value = value / 1024f;
        if (value < 1024)
            return value.toString() + " YiB";
        
        return sz.toString() + " B";
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

    /**
     * File percentage formated getter
     *
     * @return  a String which represents the file percentage
     * */
    public String getPercentageFormat () {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        return df.format(this.percentage);
    }

}
