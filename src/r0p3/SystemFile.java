package r0p3;

import java.util.HashMap; 
// import java.util.Map; 

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemFile {

    private String      root_path_str = "/";
    private String      start_path_str;
    private Integer     directory_num;
    private Integer     file_num;
    private Float       used_space;
    private Float       total_space;
    private Directory   dir_tree;
    private HashMap<String, FileData> files_info;

    /**
     * Default constructor, will start the path to scan the file system at the root
     * */
    public SystemFile () {
        this("/");
    }

    /**
     * Contructor overwritten 
     *
     * @param   path    represents the path where the scan starts
     * */
    public SystemFile (String path) {
        start_path_str  = path;
        directory_num   = 0;
        file_num        = 0;
        used_space      = 0.0f;
        total_space     = 0.0f;
        dir_tree        = new Directory(path);
        files_info      = new HashMap<String, FileData>();
    }

    /**
     * Ask for a path, usefull if the path isn't setted up at the constructor
     *
     * @return  String which represents the path
     * */
    public static String intoducePath () throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        System.out.print("Introduce a path to start analyzing files: ");
        String path = reader.readLine();        
        return path;
    }

    /**
     * Directories number getter
     *
     * @return  total number of direcotries scanned
     * */
    public Integer getNumDirectories () {
        return directory_num;
    }

    /**
     * File number getter
     *
     * @return  total number of files scanned
     * */
    public Integer getNumFiles () {
        return file_num;
    }

    /**
     * Used space getter
     *
     * @return  files size scanned
     * */
    public Float getUsedSpace () {
        return used_space;
    }

    /**
     * Total space getter
     *
     * @return  total disk space
     * */
    public Float getTotalSpace () {
        return total_space;
    }


    /**
     * Allows you to loop through the file system recursively
     * It doesn't requires a String which represent the path, so it'll take the default start path
     * */
    public void travelDirectories () throws IOException {
        travelDirectories(start_path_str);
    }

    /**
     * Allows you to loop through the file system recursively
     * It requires a String which represent the path to start scanning
     * */
    public void travelDirectories (String path) throws IOException {
        Path root = Paths.get( path );
        Iterable<Path> list = Files.newDirectoryStream(root);

        if (list == null) return;

        for (Path f : list) {
            if (Files.isSymbolicLink(f)) {
                System.out.println( "Sym:" + f.toRealPath() );
            } else if ( Files.isDirectory(f) ) {
                System.out.println( "Dir:" + f.toRealPath() );
                travelDirectories( f.toAbsolutePath().toString() );
            } else {
                System.out.println( "File:" + f.toRealPath() );
            }
        }
    }

    /**
     * There is where all the magic of visiting directories happens
     * */
    private void walk () {

    }

    /**
     * Prints all the file system scanned
     * */
    public void printFileSystem () {

    }

    /**
     * Prints the data inside a specific directory
     * Returns a Directory objetc, BUT IM NOT SHURE ABOUT IT
     * */
    public Directory printDirectoryData () {
        return null;
    }

    /**
     * Prints the data of a specific file
     *
     * @param   path    a Path objetc
     * */
    public void printFileData (Path path) {
        printFileData(path.toString());
    }

    /**
     * Prints the data of a specific file
     *
     * @param   path    a Strign which represents a path
     * */
    public void printFileData (String path) {

    }

    /**
     * Prints the content of a file
     *
     * @param   path    a Path objetc
     * */
    public void readFile (Path path) {
        readFile(path.toString());
    }

    /**
     * Prints the content of a file
     *
     * @param   path    a Strign which represents a path
     * */
    public void readFile (String path) {

    }

}
