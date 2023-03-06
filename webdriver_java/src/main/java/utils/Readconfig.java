package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Readconfig {
    Properties pro;

    public Readconfig() throws IOException {
        File src = new File("./Configuration/config.properties");
        FileInputStream fis = new FileInputStream(src);
        pro = new Properties();
        pro.load(fis);
    }
    public String getAppURL(){
        return pro.getProperty("baseURL");
    }
    public String getusername(){
        return pro.getProperty("username");
    }
    public String getpassword(){
        return pro.getProperty("pwd");
    }
    public String getchromepath(){
        return pro.getProperty("chromepath");
    }
    public String getedgepath(){
        return pro.getProperty("edgepath");
    }
    public String getfirefoxpath(){
        return pro.getProperty("firefoxpath");
    }
}
