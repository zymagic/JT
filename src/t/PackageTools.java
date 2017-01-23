package t;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PackageTools {

    private String srcPath;
    private String dataFile;
    private String output;

    public PackageTools(String path, String filePath) {
        this.dataFile = path;
        this.srcPath = filePath;
        this.output = new File(srcPath, "output").getAbsolutePath();
    }

    public PackageTools setOutput(String path) {
        return this;
    }

    public void exe() throws IOException {

        HashMap<String, String> map = new HashMap<String, String>();

        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        String readed = null;
        while ((readed = br.readLine()) != null) {
            System.out.println("read " + readed);
            String[] pair = readed.split("\\|\\|");
            String converted = "app_" + pair[0].toLowerCase().replace(".", "_") + ".png";
            map.put(pair[1].trim().toLowerCase() + ".png", converted);
        }
        br.close();

        int moved = 0;
        File outoutFolder = new File(output);
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(new File(srcPath).listFiles()));
        for (File f : files) {
            String newName = map.get(f.getName().toLowerCase());
            System.out.println("parsing " + f.getName() + " to " + newName);
            if (newName != null) {
                moveFile(f, outoutFolder, newName);
                moved++;
            }
        }

        System.out.println("done " + moved);
    }

    private void moveFile(File src, File of, String name) throws IOException {
        if (!of.exists()) {
            of.mkdirs();
        }
        src.renameTo(new File(of, name));
    }

    private void copyFile(File src, File of, String name) throws IOException {
        if (!of.exists()) {
            of.mkdirs();
        }
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(new File(of, name));
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fis.close();
        fos.close();
    }
}
