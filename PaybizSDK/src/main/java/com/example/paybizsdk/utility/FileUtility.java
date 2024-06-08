package com.example.paybizsdk.utility;

import java.io.File;
import java.util.List;

public class FileUtility {

    public static String findAPK(String apk, String path){
        File file = new File(path+apk);
        if(file.exists()){
            return "File Exist";
        }
        return null;
    }

    public static String findFileInPaths(String file, List<String> paths){
        for(String path:paths){
            if(new File(path+file).exists()){
                return "File Exist";
            }
        }
        return null;
    }

}
