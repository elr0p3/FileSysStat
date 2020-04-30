
public class DataFile {

    private String extention    = "";
    private Float size          = 0.0;
    private Float percentage    = 0.0;

    public DataFile (String extention) {
        this.extention = extention;
        this.size = 0.0;
    }


    public DataFile (String extention, Float size) {
        this.extention = extention;
        this.size = size;
    }

}
