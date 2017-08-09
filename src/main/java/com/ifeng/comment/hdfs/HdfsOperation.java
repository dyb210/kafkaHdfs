package com.ifeng.comment.hdfs;

import java.io.*;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;


public class HdfsOperation {

    /**
     * 上传文件到HDFS上去
     */
    private static void uploadToHdfs(String localSrc, String dst) throws FileNotFoundException, IOException {

        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
//        if(fs.exists(new Path(dst))){
//
//        }
        OutputStream out = fs.create(new Path(dst), new Progressable() {
            public void progress() {
                System.out.print(".");
            }
        });
        IOUtils.copyBytes(in, out, 40960, true);


    }

    /**
     * 拷贝文件
     *
     * @param src
     * @param dst
     * @param conf
     * @return
     * @throws Exception
     */
    public static boolean copyFile(String src, String dst, Configuration conf) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        FileSystem fs = null;
        try {
            System.out.println("copy");
            fs = FileSystem.get(URI.create(dst), conf);
            File file = new File(src);
            if(file.exists()){
                if (fs.exists(new Path(dst))) {
                    FileStatus status = fs.getFileStatus(new Path(dst));
                    if(status.getLen()==file.length()){
                        System.out.println("File is exists and equal length!"+file.getName()+"  "+status.getLen()+"  "+file.length());
                    }else{
                        System.out.println("copy1");
                        in = new BufferedInputStream(new FileInputStream(file));
                        System.out.println("copy2" + " " + dst);
                        /**
                         * FieSystem的create方法可以为文件不存在的父目录进行创建，
                         */
                        out = fs.create(new Path(dst), new Progressable() {
                            public void progress() {
                                System.out.print(".");
                            }
                        });
                        IOUtils.copyBytes(in, out, 40960, true);
                        System.out.println("copy3");
                        out.flush();
                        out.close();
                        in.close();
                    }
                } else {
                    System.out.println("copy1");
                    in = new BufferedInputStream(new FileInputStream(file));
                    System.out.println("copy2" + " " + dst);
                    /**
                     * FieSystem的create方法可以为文件不存在的父目录进行创建，
                     */
                    out = fs.create(new Path(dst), new Progressable() {
                        public void progress() {
                            System.out.print(".");
                        }
                    });
                    IOUtils.copyBytes(in, out, 40960, true);
                    System.out.println("copy3");
                    out.flush();
                    out.close();
                    in.close();
                }
            }
        } catch (Exception e) {
            copyFile(src, dst, conf);
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
            if (fs != null) {
                fs.close();
                fs = null;
            }
        }
        return true;
    }

    /**
     * 拷贝文件夹
     *
     * @param src
     * @param dst
     * @param conf
     * @return
     * @throws Exception
     */
    public static boolean copyDirectory(String src, String dst, Configuration conf) throws Exception {

        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create(dst), conf);
            if (!fs.exists(new Path(dst))) {
                fs.mkdirs(new Path(dst));
            }
            System.out.println("copyDirectory:" + dst);
            FileStatus status = fs.getFileStatus(new Path(dst));
            File file = new File(src);
            System.out.println("file");

            if (status.isFile()) {
                System.exit(2);
                System.out.println("You put in the " + dst + "is file !");
            }
            File[] files = file.listFiles();
            System.out.println("start111");
            System.out.println(files.length + "@@@@@@@@@@@@@@");
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                System.out.println(f.getPath());
                if (f.isDirectory()) {
                    System.out.println(f.getPath() + "   " + dst.replaceAll("\\\\", "/") + "!!!dadasd");
                    copyDirectory(f.getPath(), dst.replaceAll("\\\\", "/"), conf);
                } else {
                    System.out.println(f.getPath() + "   " + dst.replaceAll("\\\\", "/") + "!!");
                    copyFile(f.getPath(), (dst + files[i].getName()).replaceAll("\\\\", "/"), conf);
                }
            }
            fs.close();
        } catch (Exception e) {
        } finally {
            if (fs != null) {
                fs.close();
                fs = null;
            }
        }
        return true;
    }


    /**
     * 从HDFS上读取文件
     */
    private static void readFromHdfs() throws FileNotFoundException, IOException {
        String dst = "hdfs://192.10.5.76:9000/user/root/input0/a.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        FSDataInputStream hdfsInStream = fs.open(new Path(dst));
        OutputStream out = new FileOutputStream("/home/li");
        byte[] ioBuffer = new byte[1024];
        int readLen = hdfsInStream.read(ioBuffer);
        while (-1 != readLen) {
            out.write(ioBuffer, 0, readLen);
            readLen = hdfsInStream.read(ioBuffer);
        }
        out.close();
        hdfsInStream.close();
        fs.close();
    }

    /**
     * HDFS上删除文件
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void deleteFromHdfs() throws FileNotFoundException, IOException {
        String dst = "hdfs://master:9000/user/root/input0/a.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        fs.deleteOnExit(new Path(dst));
        fs.close();
    }

    /**
     * 遍历HDFS上的文件和目录
     */
    private static void getDirectoryFromHdfs() throws FileNotFoundException, IOException {
        String dst = "hdfs://master:9000/user/root/input0";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        FileStatus fileList[] = fs.listStatus(new Path(dst));
        int size = fileList.length;
        for (int i = 0; i < size; i++) {
            System.out.println("name:" + fileList[i].getPath().getName() + "\t\tsize:" + fileList[i].getLen());
        }
        fs.close();
    }

    /**
     * main函数
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {

//            if (args.length < 2) {
//                System.out.println("Please input two number");
//                System.exit(2);
//            }
//            String localSrc = args[0];
//            String dst = args[1];

           String localSrc = "F:\\dyb\\JinshanBk\\out\\artifacts\\JinshanBk_jar2";
            String dst = "hdfs://10.90.4.195:8020/dyb/dyb/news/20170504/video/";
            Configuration conf = new Configuration();
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            File srcFile = new File(localSrc);
            if (srcFile.isDirectory()) {
                copyDirectory(localSrc, dst, conf);
            } else {
                copyFile(localSrc, dst, conf);
            }
            //Configuration conf = new Configuration();
//            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
//            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
////            conf.addResource("hdfs://10.90.4.195:8020/dyb/dyb/");
//            copyDirectory("/data/logs/logsrc/zmtdb/20170331/fhh","hdfs://10.90.4.195:8020/dyb/dyb/news/20170331",conf);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(2);
            e.printStackTrace();
        } finally {
            System.out.println("SUCCESS");
            System.out.println(3);
        }
    }

}