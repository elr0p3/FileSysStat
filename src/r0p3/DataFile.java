package r0p3;

public class DataFile {

    private String extention = "";
    private Float size;
    private Float percentage = 0.0f;

    public DataFile (String extention) {
        this.extention = extention;
        this.size = 0.0f;
    }


    public DataFile (String extention, Float size) {
        this.extention = extention;
        this.size = size;
    }

}
