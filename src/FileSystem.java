import java.util.HashMap; 
import java.util.Map; 

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 

public class FileSystem {

    private String  root_path       = "/";
    private String  start_path      = "/";
    private Integer directory_num   = 0;
    private Integer file_num        = 0;
    private Float   used_space      = 0.0;
    private Float   total_space     = 0.0;
    private Directory dir_tree;
    private HashMap<String, DataFile> files_info;

    public FileSystem () {
        dir_tree    = new Directory(start_path);
        files_info  = new HashMap<String, DataFile>();
    }

    public FileSystem (String path) {
        start_path  = new String(path);
        dir_tree    = new Directory(path);
        files_info  = new HashMap<String, DataFile>();
    }

    public static String intoducePath () throw IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        System.out.print("Introduce a path to start analyzing files: ");
        start_path = reader.readLine();        
        return start_path;
    }

}
