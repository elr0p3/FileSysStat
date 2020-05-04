package r0p3;

import java.util.HashSet;
import java.util.Set;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap; 
import java.util.Map; 

public class Directory {

    private String name = "";
    private Path path;
    private Float size  = 0.0f;
    private Set<Directory> dirs_content;
    private Map<String, Float> files_content;

    /**
     * Directory constructor
     * Requires a path and a name for the directory
     * */
    public Directory (String name, Path path) {
        String[] aux = name.split("/");
        this.name       = aux[aux.length - 1];
        this.path       = path;
        dirs_content    = new HashSet<Directory>();
        files_content   = new HashMap<String, Float>();
    }

    /**
     * Directory constructor
     * Requires a String which represent a path
     * */
    public Directory (String path) {
        String[] aux = path.split("/");
        this.name       = aux[aux.length - 1];
        this.path       = Paths.get(path);
        dirs_content    = new HashSet<Directory>();
        files_content   = new HashMap<String, Float>();
    }

    
    /**
     * Returns the directory's name
     * */
    public String getName () {
        return name;
    }

    /**
     * Returns the directory's path
     * */
    public Path getPath () {
        return path;
    }

    /**
     * Returns the directory's size
     * */
    public Float getSizeContent () {
        return size;
    }

    /**
     * Returns the directories content at the directory
     * */
    public Set<Directory> getDirsContent () {
        return dirs_content;
    }

    /**
     * Returns the files content at the directory
     * */
    public Map<String, Float> getFilesContent () {
        return files_content;
    }

    /**
     * Add to the set a directory contained by the actual one
     * It requieres a Directory objetc
     * */
    public void addDirectory (Directory dir) {
        
    }

    /**
     * Add a 'file' into the actual directory
     * It requires a name, a String representing a path, and its size
     * */
    public void addFile (String name, Float size) {
       String[] aux = name.split("/");

    }

}
