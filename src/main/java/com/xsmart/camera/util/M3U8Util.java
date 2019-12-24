package com.xsmart.camera.util;

import com.xsmart.camera.GodeyeProperties;
import com.xsmart.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 16 9:42
 */
@Component
public class M3U8Util {
    private static final Logger logger = LoggerFactory.getLogger(M3U8Util.class);

    @Autowired
    private GodeyeProperties godeyeProperties;
    //m3u8Path 直播的m3u8路径 =tsBASE/provider/deivceId.m3u8

    //23679-152.ts
    public String readLastTsFile(String m3u8Path){
        BufferedReader br = null;
        FileReader fr = null;
        String line = null;
        String tsPath = null;
        List<Integer> tsFileList = new ArrayList<>();
        try {
            fr = new FileReader(m3u8Path);
            br = new BufferedReader(fr);
             while((line = br.readLine())!= null ){
                 if(line.endsWith(Constants.TS_SUFFIX)){
                     tsFileList.add(Integer.parseInt(line.split("\\.")[0].split("-")[1]));
                     tsPath = line;
                 }
             }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            try {
                if(fr != null){
                    fr.close();
                }
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //23679-152.
        tsFileList.sort(null);
        tsPath =  tsPath.split("\\.")[0].split("-")[0]+"-"+tsFileList.get(tsFileList.size()-1)+Constants.TS_SUFFIX;
        return tsPath;
    }
    public void buildConcatList(String startFile,String playListPath,String m3u8Path,String tsFilePath){
        //onepiece-4.ts
        logger.info("buildConcatList startFile is {}",startFile);
        //先清空原先的playList
        clearFile(playListPath);
        BufferedReader br = null;
        FileReader fr = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String line = null;
        try {
            fr = new FileReader(m3u8Path);
            br = new BufferedReader(fr);
            fw = new FileWriter(playListPath);
            bw = new BufferedWriter(fw);
            StringBuffer newLineBuffer = new StringBuffer();
            boolean startWrite = false;
            while((line = br.readLine())!= null ){
                if(line.endsWith(Constants.TS_SUFFIX)){
                    if(line.equals(startFile)){
                        startWrite = true;
                    }
                    if(startWrite){
                        newLineBuffer.append("file ").append(tsFilePath).append("/").append(line);
                        bw.write(newLineBuffer.toString());
                        bw.newLine();
                        newLineBuffer.delete(0,newLineBuffer.length());
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();

        } finally{
            try {
                if(br != null){
                    br.close();
                }
                if(fr != null){
                    fr.close();
                }
                if(bw != null){
                    bw.close();
                }
                if(fw != null){
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void buildConcatListOther(String startFile,String playListPath,String m3u8Path,String tsFilePath){
        //onepiece-4.ts
        //先清空原先的playList
        clearFile(playListPath);
        //onepiece-8.ts
        String endFile = readLastTsFile(m3u8Path);
        logger.info("buildConcatListOther startFile is {} and endFile is {}",startFile,endFile);
        String[] tsStartArr = startFile.split("\\.")[0].split("-");
        String[] tsEndArr = endFile.split("\\.")[0].split("-");
        int startIndex = Integer.parseInt(tsStartArr[1]);
        int endIndex = Integer.parseInt(tsEndArr[1]);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(playListPath);
            bw = new BufferedWriter(fw);
            StringBuffer newLineBuffer = new StringBuffer();
            while(startIndex <= endIndex ){
                newLineBuffer.append("file ").append(tsFilePath).append("/").append(tsStartArr[0]).append("-").append(startIndex).append(Constants.TS_SUFFIX);
                bw.write(newLineBuffer.toString());
                bw.newLine();
                newLineBuffer.delete(0,newLineBuffer.length());
                startIndex++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();

        } finally{
            try {
                if(bw != null){
                    bw.close();
                }
                if(fw != null){
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean buildReplayM3U8File(String directoryPath,String m3u8Path,String replayPath)throws Exception{
        File directory = new File(directoryPath);
        if(!directory.exists()){
            return false;
        }
        List tsList = new ArrayList();
        File[] tsFileArr = directory.listFiles();
        for(File tsFile:tsFileArr){
            tsList.add(tsFile.getName());
        }
        BufferedReader br = null;
        FileReader fr = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String line = null;
        try {
            fr = new FileReader(m3u8Path);
            br = new BufferedReader(fr);
            fw = new FileWriter(replayPath);
            bw = new BufferedWriter(fw);
            String extInfLabel = null;
            while((line = br.readLine())!= null ){
                if(line.startsWith(Constants.M3U8_TARGETDURATION_LABEL)){
                    //在TARGETDURATION_LABEL后加入点播标签。
                    bw.write(line);
                    bw.newLine();
                    bw.write(Constants.M3U8_VOD_LABEL);
                    bw.newLine();
                }else if(line.startsWith(Constants.M3U8_EXTINF_LABEL)){
                    //先缓存该标签行 #EXTINF:11.558, no desc
                    extInfLabel = line;
                }else if(tsList.contains(line)){
                    //如果该ts文件存在，把标签行跟文件名行写入
                    bw.write(extInfLabel);
                    bw.newLine();
                    bw.write(line);
                    bw.newLine();
                }else if(extInfLabel == null){
                    bw.write(line);
                    bw.newLine();
                }
            }
            //最后加上ENDLIST标签
            bw.write(Constants.M3U8_ENDLIST_LABEL);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e){
            throw e;
        } finally{
            try {
                if(br != null){
                    br.close();
                }
                if(fr != null){
                    fr.close();
                }
                if(bw != null){
                    bw.close();
                }
                if(fw != null){
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public void clearFile(String path){
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            bw.write("");
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            try {
                if(fw != null){
                    fw.close();
                }
                if(bw != null){
                    bw.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String transformAddr(String rtmpPath,String port){
        //rtmp://172.21.64.133:1935/hikvision/101
        //http://172.21.64.133:1935/hikvision/101.m3u8
        String cutPortStr = ":"+port;
        String proxyPort =":"+godeyeProperties.getNginxProxyPort();
        String m3u8Path = rtmpPath.replace("rtmp","http").replace(cutPortStr,proxyPort)+".m3u8";

        return m3u8Path;
    }
    public void moveFileToOtherDir(String path,String date){
        String directory = path+"/"+date;
        File srcPath = new File(path);
        if(!srcPath.exists()){
            logger.warn("srsPath {}is not exists! please check",path);
            return;
        }
        File newDirectory = new File(directory);
        if(!newDirectory.exists()){
            logger.info("directory {} is not exists ! ",directory);
            newDirectory.mkdir();
        }
        File[] fileArr = srcPath.listFiles();
        for(File file:fileArr){
            file.renameTo(new File(directory+"/"+file.getName()));
        }
    }
    public List<String> getAllMp4File(String filePath,String prePath){
        File mp4Directory = new File(filePath);
        if(!mp4Directory.exists()){
            logger.warn("mp4Directory {}is not exists! please check",filePath);
            return null;
        }

        List<String> fileList = new ArrayList<>();
        File[] fileArr = mp4Directory.listFiles();
        for(File file:fileArr){
            if(file.getName().endsWith(Constants.MP4_SUFFIX)){
                fileList.add(prePath+"/"+file.getName());
            }
        }
        return fileList;
    }

    public void removeAllFile(String path){
        File m3u8Dir = new File(path);

        if(!m3u8Dir.exists()){
            logger.warn("m3u8Dir {} is not exists! please check",path);
            return;
        }
        File[] fileArr = m3u8Dir.listFiles();
        for(File file : fileArr){
            if(file.getName().contains(Constants.M3U8_SUFFIX)||file.getName().contains(Constants.TS_SUFFIX)){
                file.delete();
            }
        }
    }
    public static void main(String[] args) {
        String m3u8Path = "D:\\IDEAWorkSpace\\other\\godeye\\src\\main\\resources\\ts\\hikvision-150001\\150001.m3u8";
                //"D:\\IDEAWorkSpace\\other\\godeye\\src\\main\\resources\\ts\\onepiece.m3u8";
        String replayM3u8Path = "D:\\IDEAWorkSpace\\other\\godeye\\src\\main\\resources\\ts\\hikvision-150001\\replay-150001.m3u8";

        String tsFilePath = "D:\\IDEAWorkSpace\\other\\godeye\\src\\main\\resources\\ts\\hikvision-150001";
        String playListPath = "D:\\IDEAWorkSpace\\other\\godeye\\src\\main\\resources\\ts\\hikvision-150001\\playlist.txt";
        String startFile = "150001-83.ts";
        String tmp = "/home/ztesoftai/IMChatService/srs/srs-2.0-r6/trunk/objs/nginx-1.5.7/_release/html/live/";
        M3U8Util m3U8Util = new M3U8Util();
        m3U8Util.buildConcatListOther(startFile,playListPath,m3u8Path,tsFilePath);
        System.out.println();
       // m3U8Util.buildConcatList(startFile,playListPath,m3u8Path,tmp);
       // m3U8Util.buildReplayM3U8File(m3u8Path,replayM3u8Path);
      //  m3U8Util.readLastTsFile(path);
        //m3U8Util.clearFile(playListPath); onepiece-0.ts
///home/ztesoftai/IMChatService/ffmpeg/ffmpeg-4.1.3/ffmpeg -f concat -safe 0 -i /home/ztesoftai/IMChatService/srs/srs-2.0-r6/trunk/objs/nginx/html/live/playlist.txt -c copy /home/ztesoftai/IMChatService/srs/srs-2.0-r6/trunk/objs/nginx/html/live/onepiece2019-07-22-20:00:10.mp4
    }
}
