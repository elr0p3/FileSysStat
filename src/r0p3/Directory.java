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
     *
     * @param   name    a String which represents a path or a directory's name
     * @param   path    a Path objetc
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
     *
     * @param   name    a String which represents a path or a directory's name
     * */
    public Directory (String path) {
        String[] aux = path.split("/");
        this.name       = aux[aux.length - 1];
        this.path       = Paths.get(path);
        dirs_content    = new HashSet<Directory>();
        files_content   = new HashMap<String, Float>();
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
     * @return  a Float which represents a directory's size
     * */
    public Float getSizeContent () {
        return size;
    }

    /**
     * Directories content at the directory getter
     *
     * @return  a Set containing Directory objects
     * */
    public Set<Directory> getDirsContent () {
        return dirs_content;
    }

    /**
     * Files content at the directory getter
     *
     * @return  a Map which represent files size, key=String, value=Float
     * */
    public Map<String, Float> getFilesContent () {
        return files_content;
    }

    /**
     * Add to the set a directory contained by the actual one
     *
     * @param   dir a Directory object
     * */
    public void addDirectory (Directory dir) {
        
    }

    /**
     * Add a 'file' into the actual directory
     *
     * @param   name    a String which represents a file name
     * @param   size    a Float which represents its size
     * */
    public void addFile (String name, Float size) {
       String[] aux = name.split("/");

    }

}
