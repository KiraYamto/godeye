package com.xsmart.camera.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author tian.xubo
 * @created 2019 - 07 - 04 9:35
 */
@RestController
public class SystemCommandController {
    private static final Logger logger = LoggerFactory.getLogger(SystemCommandController.class);
    @GetMapping("linux/commandcall/{command}")
    public void commandcall(@PathVariable("command")String command) {
        logger.info("=======http request commandcall begin ===== ");
        String pid = getPID(command);
        logger.info("=======http request commandcall end and pid is \r\n {}===== ",pid);
    }
    public  String getPID(String command){
        BufferedReader reader =null;
        try{
            //显示所有进程
            String[] cmds = {"/bin/sh","-c","ps -ef|grep "+command};
            Process process = Runtime.getRuntime().exec(cmds);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while((line = reader.readLine())!=null){
                logger.info("find line is {} and commnad is {}-----> ",line,command);
                if(line.contains(command)){
                    String[] strs = line.split("\\s+");
                    return strs[1];
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        //System.out.println(getPID("chrome.exe"));
    }
}
