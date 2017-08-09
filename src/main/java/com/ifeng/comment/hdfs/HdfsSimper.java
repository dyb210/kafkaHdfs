package com.ifeng.comment.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;


public class HdfsSimper {
    private int batchSize = 40960;
    private String dstpath ="hdfs://10.90.4.195:8020/dyb/dyb/news/tmp/aa1";
    private Configuration configuration;
    private InputStream inputStream;
    private  OutputStream out = null;
    private   FileSystem fs = null;



    public HdfsSimper() {
        configuration = new Configuration();
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        //configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
//        this.dstpath = PropertiesUtil.getProperties("config.properties").getParam("hdfs.dfspath");
      //  this.batchSize = Integer.parseInt(PropertiesUtil.getProperties("config.properties").getParam("hdfs.batchSize"));
    }




    /**
     * 拷贝文件
     *
     * @return
     * @throws Exception
     */
    public boolean update(InputStream inputStream) throws Exception {

        try {
            fs = FileSystem.get(URI.create(dstpath), configuration);
            out = fs.create(new Path(dstpath), new Progressable() {
                public void progress() {
                    ///
                    System.out.print(".");
                }
            });
            IOUtils.copyBytes(inputStream, out, batchSize, true);
            out.flush();
            out.close();
            inputStream.close();
        } catch (Exception e) {
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (fs != null) {
                fs.close();
                fs = null;
            }
        }
        return true;
    }


}