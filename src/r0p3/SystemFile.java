package r0p3;

import java.util.HashMap; 
// import java.util.Map; 

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemFile {

    private String      root_path_str = "/";
    private String      start_path_str;
    private Integer     directory_num;
    private Integer     file_num;
    private Double      used_space;
    private Double      total_space;
    private Directory   dir_tree;
    private HashMap<String, FileData> files_data;

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
        // dir_tree        = new Directory(path);
        files_data      = new HashMap<String, FileData>();
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
    public Double getUsedSpace () {
        return used_space;
    }

    /**
     * Total space getter
     *
     * @return  total disk space
     * */
    public Double getTotalSpace () {
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
     *
     * @param   path    a String which represent the path to start scanning
     * */
    public void travelDirectories (String path) throws IOException {
        // OJO CUIDAO CON ESTOS ERRORES
        // java.nio.file.AccessDeniedException      -> Archivo de superusuario
        // System.err.println("error at " + f.toRealPath().toString() + ":\nyou cannot perform this operation unless you are root.");
        // java.nio.file.DirectoryIteratorException -> 
        // java.nio.file.FileSystemException        -> Archivo corrupto

        used_space = walk(Paths.get(path), dir_tree, files_data);
        /*
         * CACLULATE FILE PERCENTAGE
         */
    }

    /**
     * There is where all the magic of visiting directories happens
     * */
    private Double walk (Path path, Directory dir, HashMap<String, FileData> files_data) throws IOException {
        Path root = path;
        Iterable<Path> list = Files.newDirectoryStream(root);
        Double size = 0.0;

        if (list == null) return 0.0;

        for (Path f : list) {
            try {
                if (Files.isSymbolicLink(f)) {
                    System.out.println( "Sym:" + f.toRealPath() );
                    size += scanFile(f, dir, files_data);
                } else if ( Files.isDirectory(f) ) {
                    Directory next_dir = new Directory(f.toRealPath().toString());
                    System.out.println( "Dir:" + f.toRealPath() );
                    dir.addDirectory(next_dir);
                    size += walk(f, next_dir, files_data);
                } else {
                    System.out.println( "File:" + f.toRealPath() );
                    size += scanFile(f, dir, files_data);
                }

            } catch (AccessDeniedException e) {
                System.err.println("AccessDeniedException: " + e.getMessage());
            } catch (DirectoryIteratorException e) {
                System.err.println("DirectoryIteratorException: " + e.getMessage());
            } catch (FileSystemException e) {
                System.err.println("FileSystemException: " + e.getMessage());
            }
        }
        dir.addSize(size);
        return size;
    }

    /**
     *
     * */
    private Double scanFile (Path f, Directory dir, HashMap<String, FileData> files_data) {
        Double size = new Double(Files.size(f));
        String file_name   = f.getFileName().toString();
        String[] extention = file_name.split(".");
        String real_extnt  = "";
        if (extention.length > 1)
            real_extnt = extention[extention.length - 1];
        if (files_data.containsKey(real_extnt)) {
            FileData aux = files_data.get(real_extnt);
            aux.addSize(aux.getSize() + size);
            aux.addFileNumber(1);
            files_data.put(real_extnt, aux);
        } else
            files_data.put(real_extnt, new FileData(real_extnt, size));
        
        dir.getFilesContent().put(file_name, size);

        return size;
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
