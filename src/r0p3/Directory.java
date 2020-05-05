package r0p3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap; 

public class Directory {

    private String name = "";
    private Path path;
    private Double size  = 0.0;
    private ArrayList<Directory> dirs_content;
    private HashMap<String, Double> files_content;


    /**
     * Directory constructor
     *
     * @param   name    a String which represents a path or a directory's name
     * */
    public Directory (String name) {
        this(name, Paths.get(name));
    }

    /**
     * Directory constructor
     *
     * @param   name    a String which represents a path or a directory's name
     * @param   path    a Path objetc
     * */
    public Directory (String name, Path path) {
        String root = "/";
        if (name.equals(root)) {
            // System.out.println("EQUALS /");
            this.name   = name;
        } else {
            // System.out.println("NOT EQUALS /");
            String[] aux = name.split("/");
            this.name   = aux[aux.length - 1];
        }
        this.path       = path;
        dirs_content    = new ArrayList<Directory>();
        files_content   = new HashMap<String, Double>();
    }

    
    /**
     * Directory's name getter
     *
     * @return  a String which represents a directory name
     * */
    public String getName () {
        return name;
    }

    /**
     * Directory's path getter
     *
     * @return  a Path object
     * */
    public Path getPath () {
        return path;
    }

    /**
     * Directory's size getter
     *
     * @return  a Double which represents a directory's size
     * */
    public Double getSizeContent () {
        return size;
    }

    /**
     * Directories content at the directory getter
     *
     * @return  a Set containing Directory objects
     * */
    public ArrayList<Directory> getDirsContent () {
        return dirs_content;
    }

    /**
     * Files content at the directory getter
     *
     * @return  a Map which represent files size, key=String, value=Double
     * */
    public HashMap<String, Double> getFilesContent () {
        return files_content;
    }

    /**
     *
     * */
    public void addSize (Double s) {
        size += s;
    }

    /**
     * Add to the set a directory contained by the actual one
     *
     * @param   dir a Directory object
     * */
    public void addDirectory (Directory dir) {
        dirs_content.add(dir);
    }

    /**
     * Add a 'file' into the actual directory
     *
     * @param   name    a String which represents a file name
     * @param   size    a Double which represents its size
     * */
    public void addFile (String name, Double size) {
       String[] aux = name.split("/");

    }

}
