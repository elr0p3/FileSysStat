package r0p3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Map;

public class Directory {

    private String name = "";
    private Path path;
    private Long size  = (long)0;
    private ArrayList<Directory> dirs_content;
    private Map<String, Long> files_content;


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
        files_content   = new HashMap<String, Long>();
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
     * @return  a Long which represents a directory's size
     * */
    public Long getSizeContent () {
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
     * @return  a Map which represent files size, key=String, value=Long
     * */
    public Map<String, Long> getFilesContent () {
        return files_content;
    }

    /**
     *
     * */
    public void addSize (Long s) {
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
     * @param   size    a Long which represents its size
     * */
    public void addFile (String name, Long size) {
        this.files_content.put(name, size);
        // this.name = name;
        // this.size = size;

    }

}
