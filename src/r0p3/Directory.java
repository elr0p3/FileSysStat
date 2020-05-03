package r0p3;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap; 
import java.util.Map; 

public class Directory {

    private String name     = "";
    private Float size      = 0.0f;
    private Set<Directory> dirs_content;
    private HashMap<String, Float> files_content;

    public Directory (String name) {
        this.name       = name;
        dirs_content    = new HashSet<Directory>();
        files_content   = new HashMap<String, Float>();
    }

}
