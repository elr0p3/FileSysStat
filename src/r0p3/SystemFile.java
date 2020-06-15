package r0p3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
// import java.io.FileWriter;
import java.io.IOException; 
import java.io.InputStreamReader; 

import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemFile {

    private String      start_path_str;
    private Long        directory_num;
    private Long        file_num;
    private Long        link_num;
    private Long        used_space;
    private Long        free_space;
    private Long        total_space;
    private Long        scanned_space;
    private Directory   dir_tree;
    private Map<String, FileData> files_data;
    private Map<String, Long> partition_used; 
    private Map<String, Long> partition_disk; 

    private Integer MAX_SPACE_PRINT = 0;

    // rutas en las que hay problemas
    // /proc/kcore -> representa la memoria RAM del sistema, tiene un tamaño de 128TB en sistemas de 64bits
    private final Set<String> DANGER_ZONE = new HashSet<String>();

    /**
     * Default constructor, will start the path to scan the file system at the root
     * */
    public SystemFile () throws IOException {
        this("/");
    }

    /**
     * Contructor overwritten 
     *
     * @param   path    represents the path where the scan starts
     * */
    public SystemFile (String path) throws IOException {
        start_path_str  = path;
        directory_num   = 0L;
        file_num        = 0L;
        link_num        = 0L;
        used_space      = (long)0;
        free_space      = (long)0;
        total_space     = (long)0;
        scanned_space   = (long)0;
        files_data      = new HashMap<String, FileData>();
        partition_used  = new HashMap<String, Long>();
        partition_disk  = new HashMap<String, Long>();
        // dir_tree        = new Directory(path);
        
           // Iterable<Path> roots = FileSystems.getDefault().getRootDirectories();
        // for (Path p : roots) {
            // FileStore fs = Files.getFileStore(p);
            // total_space += fs.getTotalSpace();
            // used_space  += (fs.getTotalSpace() - fs.getUsableSpace());
        // }

        FileSystem fileSystem = FileSystems.getDefault();
        Iterable<FileStore> iterable = fileSystem.getFileStores();
        iterable.forEach(s -> {
            try {
                if (System.getProperty("os.name").equals("Linux")) {
                    if (s.toString().contains("(/dev/")){
                        total_space += s.getTotalSpace();
                        used_space += s.getTotalSpace() - s.getUsableSpace();
                        free_space += s.getUsableSpace();
                        partition_disk.put(s.toString(), s.getTotalSpace());
                        partition_used.put(s.toString(), s.getTotalSpace() - s.getUsableSpace());
                    }
                } else {
                    total_space += s.getTotalSpace();
                    used_space += s.getTotalSpace() - s.getUsableSpace();
                    free_space += s.getUsableSpace();
                    partition_disk.put(s.toString(), s.getTotalSpace());
                    partition_used.put(s.toString(), s.getTotalSpace() - s.getUsableSpace());
                }
            } catch (IOException e) {
               e.printStackTrace();
            }
        });

        // https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/4/html/reference_guide/s2-proc-kcore
        // https://stackoverflow.com/questions/21170795/proc-kcore-file-is-huge
        // https://www.tldp.org/LDP/Linux-Filesystem-Hierarchy/html/proc.html
        DANGER_ZONE.add("/proc/kcore");
        DANGER_ZONE.add("/var/lib/dhcpcd/proc/kcore");
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
    public Long getNumDirectories () {
        return directory_num;
    }

    /**
     * File number getter
     *
     * @return  total number of files scanned
     * */
    public Long getNumFiles () {
        return file_num;
    }

    /**
     * Number of Symbolic Links getter
     *
     * @return  number of symbolic links scanned
     * */
    public Long getLinks () {
        return link_num;
    }

    /**
     * Used space getter
     *
     * @return  files size scanned
     * */
    public Long getUsedSpace () {
        return used_space;
    }

    /**
     * Total space getter
     *
     * @return  total disk space
     * */
    public Long getTotalSpace () {
        return total_space;
    }

    /**
     * Scanned space getter
     *
     * @return  scanned space from the path
     * */
    public Long getScannedSpace () {
        return scanned_space;
    }


    /**
     *  Free space getter
     *
     *  @return     total storage free space
     * */
    public Long getFreeSpace () {
        return free_space;
    }

    /**
     * Files data getter
     *
     * @return  a Map which contains the files data, key=String, value=FileData
     * */
    public Map<String, FileData> getFileData () {
        return files_data;
    }

    /**
     * Directory Tree getter
     *
     * @return the root of the directory tree
     * */
    public Directory getDirTree () {
        return dir_tree;
    }


    /**
     *  Partition total storage
     *
     *  @return     a map of the total memory size
     * */
    public Map<String, Long> getPartitionSize () {
        return partition_disk;
    }

    /**
     *  Partition storage used
     *
     *  @return     a map of the stored space used
     * */
    public Map<String, Long> getPartitionUsed () {
        return partition_used;
    }


    /**
     * Allows you to loop through the file system recursively
     * It doesn't requires a String which represent the path, so it'll take the default start path
     *
     * @param enable_print directories and content are printed if true else not
     * */
    public void travelDirectories (boolean enable_print) throws IOException {
        travelDirectories(start_path_str, enable_print);
    }

    /**
     * Allows you to loop through the file system recursively
     *
     * @param   path    a String which represent the path to start scanning
     * @param enable_print directories and content are printed if true else not
     * */
    public void travelDirectories (String path, boolean enable_print) throws IOException {
        // OJO CUIDAO CON ESTOS ERRORES
        // java.nio.file.AccessDeniedException      -> Archivo de superusuario
        // System.err.println("error at " + f.toRealPath().toString() + ":\nyou cannot perform this operation unless you are root.");
        // java.nio.file.DirectoryIteratorException -> 
        // java.nio.file.FileSystemException        -> Archivo corrupto

        start_path_str = path;
        dir_tree = new Directory(start_path_str);
        directory_num++;
        scanned_space = walk(Paths.get(path), dir_tree, files_data, enable_print);

        // Calcular el porcentaje de los archivos
        for (Map.Entry<String, FileData> entry : files_data.entrySet()) {
            Long size_f = entry.getValue().getSize();
            entry.getValue().calculateTotalSizePercentage(size_f, this.scanned_space);
        }
    }

    /**
     * There is where all the magic of visiting directories happens
     *
     * @param path          the path of the directory to be scanned
     * @param dir           the Directory node to store the data
     * @param files_data    object to store the files data
     * @param enable_print  directories and content are printed if true else not
     * 
     * @return  a Long which is the total scanned size
     * */
    private Long walk (Path path, Directory dir, Map<String, FileData> files_data, boolean enable_print) throws IOException {
        Path root = path;
        Iterable<Path> list = Files.newDirectoryStream(root);
        Long size = (long)0;

        if (list == null) return (long)0;

        for (Path f : list) {
            try {

                if (Files.isSymbolicLink(f)) {          // Encontrado enlace simbolico
                    if (enable_print)
                        System.out.println( "Sym:" + f.toRealPath() );
                    link_num++;
                    dir.addSymLink(f.getFileName().toString());
                    dir.addSymLink(1);
                } else if (Files.isDirectory(f)) {      // Encontrado directorio
                    if (enable_print)
                        System.out.println( "Dir:" + f.toRealPath() );
                    Directory next_dir = new Directory(f.toRealPath().toString());
                    dir.addDirectory(next_dir);
                    size += walk(f, next_dir, files_data, enable_print);
                    directory_num++;
                    dir.addSubDir(1);
                } else if (Files.isRegularFile(f) && Files.size(f) > 0) {    // Encontrado arhivo de texto
                    if (enable_print)
                        System.out.println( "File:" + f.toRealPath() );
                    if (!DANGER_ZONE.contains(f.toRealPath().toString()))
                        size += scanFile(f, dir, files_data);
                    file_num++;
                    dir.addFile(1);
                }

            } catch (AccessDeniedException e) {
                System.err.println("AccessDeniedException:      " + e.getMessage());
            } catch (DirectoryIteratorException e) {
                System.err.println("DirectoryIteratorException: " + e.getMessage());
            } catch (FileSystemException e) {
                System.err.println("FileSystemException:        " + e.getMessage());
            }
        }
        dir.addSize(size);
        return size;
    }

    /**
     * Helps walk() function to get the size of a file and store some files data
     *
     * @param path          the path of the file scanned
     * @param dir           the Directory node to store the data
     * @param files_data    object to store the files data
     * */
    private Long scanFile (Path f, Directory dir, Map<String, FileData> files_data) throws IOException {
        Long size = (long)Files.size(f);
        String file_name   = f.getFileName().toString();
        if (file_name.charAt(0) == '.') {
        	StringBuilder sb = new StringBuilder(file_name);
        	sb.deleteCharAt(0);
        	file_name = sb.toString();
        }
        // if (!file_name.contains(".")) {	
            // FileWriter file = new FileWriter("./info/total.data", true);
            // file.append(f.toString() + "\n");
            // file.close();        	
        // }
        String[] extension = file_name.split("\\.");
        String real_extnt  = "";
        if (extension.length > 1) {
            real_extnt = extension[extension.length - 1];
            if (real_extnt.length() > MAX_SPACE_PRINT)
                MAX_SPACE_PRINT = real_extnt.length();
        }
        if (files_data.containsKey(real_extnt)) {
            FileData aux = files_data.get(real_extnt);
            aux.addSize(size);
            aux.addFileNumber(1);
            files_data.put(real_extnt, aux);
        } else
            files_data.put(real_extnt, new FileData(real_extnt, size));
        
        // dir.getFilesContent().put(file_name, size);
        dir.addFile(file_name, size);

        return size;
    }

    /**
     * Prints all the file system scanned
     * */
    public void printFileSystem () {
        printFileSystemStyle(dir_tree, ""); 
    }

    /**
     * Prints the file system scanned
     *
     * @param   dir a Directory object
     * @param   t   a tab, just to make indentations
     * */
    private void printFileSystemStyle (Directory dir, String t) {
        String tab = "\t" + t;
        System.out.println(t + dir.getPath().toString() + " --> " + dir.getSizeContent());
        for (Map.Entry<String, Long> iterator : dir.getFilesContent().entrySet()) {
           System.out.println(tab + iterator.getKey() + " --> " + iterator.getValue());
        }
        for (Directory next_dir : dir.getDirsContent()){
            printFileSystemStyle(next_dir, tab);
        }
    }

    /**
     * Prints a table to show the files data
     *
     * @param limit the number of lines to be printed
     * */
    public void printFileData (Integer limit) {
        System.out.println("\n");
        System.out.println("Total space: " + getTotalSpace() + " B" + " -> " + getTotalSpace()/(1024*1024*1024f));
        System.out.println("Used space:  " + getUsedSpace() + " B" + " -> " + getUsedSpace()/(1024*1024*1024f));
        System.out.println("Scan space:  " + getScannedSpace() + " B" + " -> " + getScannedSpace()/(1024*1024*1024f));
        System.out.println("Dir num:     " + getNumDirectories());
        System.out.println("File num:    " + getNumFiles());
        System.out.println("Link num:    " + getLinks());
        System.out.println();

        String formatTitle = "%-" + MAX_SPACE_PRINT.toString() + "s|%-15s|%-15s|%-15s\n";
        System.out.format(formatTitle, "Extension", "Percentage (%)", "Number of files", "Size");
        printLine();
        int i = 0;
        for (FileData f : sortFiles()) {
            if (i == limit)
                break;
            String formatData = "%-" + MAX_SPACE_PRINT.toString() + "s|%-15f|%-15d|%-15s\n";
            System.out.format(formatData, f.getExtension(), f.getPercentage(), f.getNumberOfFiles(), f.getSizeUnit());
            printLine();
            i++;
        }
    }

    /**
     * Sort the files data, transform the Map into a List
     *
     * @return  a List which contains the files data
     * */
    private List<FileData> sortFiles () {
        // Perdon por hacer esto, pero es lo que se me ocurre mas rapido
        // si en el futuro se me ocurre otra cosa ya lo hare
        // |
        // |
        // Bueno....
        // Han pasado unas semanas y no se me ha ocurrido nada, de hecho lo
        // he vuelto a hacer en el archivo r0p3GUI/TableFileData.java
        // porque soy debil

        ArrayList<Long> sizes = new ArrayList<Long>();

        for (FileData f : files_data.values())
            sizes.add(f.getSize());

        Collections.sort(sizes, Collections.reverseOrder());

        // Map<String, FileData> aux = new HashMap<String, FileData>();
        List<FileData> aux = new ArrayList<>();

        for (Long size : sizes) {
            for (Map.Entry<String, FileData> entry : files_data.entrySet()) {
                if (entry.getValue().getSize() == size && !aux.contains(entry.getValue())){
                    aux.add(entry.getValue());
                    break;
                }
            }
        }
        return aux;
    }

    /**
     * Print the line to separate data in printFileData()
     * */
    private void printLine () {
        for (int i = 0; i < MAX_SPACE_PRINT; i++)
            System.out.print("-");
        for (int i = 0; i < 45; i++) {
            if (i % 16 == 0)
                System.out.print("+");
            else
                System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Prints the data inside a specific directory
     *
     * @return  a Directory objetc, BUT IM NOT SHURE ABOUT IT
     * */
    // public Directory printDirectoryData () {
        // return null;
    // }

    /**
     * Prints the data of a specific file
     *
     * @param   path    a Path objetc
     * */
    // public void printFileData (Path path) {
        // printFileData(path.toString());
    // }

    /**
     * Prints the data of a specific file
     *
     * @param   path    a Strign which represents a path
     * */
    // public void printFileData (String path) {

    // }

    /**
     * Prints the content of a file
     *
     * @param   path    a Path objetc
     * */
    // public void readFile (Path path) {
        // readFile(path.toString());
    // }

    /**
     * Prints the content of a file
     *
     * @param   path    a Strign which represents a path
     * */
    // public void readFile (String path) {

    // }

}
