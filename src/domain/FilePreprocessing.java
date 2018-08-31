package domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FilePreprocessing {

    public FilePreprocessing()
    {

    }

    private void readFile(String filePath) throws IOException
    {
        File file = new File(filePath);
        if(file.exists() && !file.isDirectory())
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        }
    }
}
