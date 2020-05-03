import java.util.HashMap; 
// import java.util.Map; 

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 

import java.nio.file.Path;
import java.nio.file.Files;

public class FileSystem {

    private String  root_path       = "/";
    private String  start_path      = "/";
    private Integer directory_num   = 0;
    private Integer file_num        = 0;
    private Float   used_space      = 0.0f;
    private Float   total_space     = 0.0f;
    private Directory dir_tree;
    private HashMap<String, DataFile> files_info;

    public FileSystem () {
        this("/");
    }

    public FileSystem (String path) {
        start_path  = path;
        dir_tree    = new Directory(path);
        files_info  = new HashMap<String, DataFile>();
    }

    public static String intoducePath () throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        System.out.print("Introduce a path to start analyzing files: ");
        String path = reader.readLine();        
        return path;
    }

    public Integer getNumDirectories () {
        return directory_num;
    }

    public Integer getNumFiles () {
        return file_num;
    }

    public Float getUsedSpace () {
        return used_space;
    }

    public Float getTotalSpace () {
        return total_space;
    }


    /**
     * Allows you to loop through the file system recursively
     * */
    public void travelDirectories () {
        travelDirectories(start_path);
    }
    public void travelDirectories (String path) {
                    
    }

}
