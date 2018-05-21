package test;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download {
	
    public static String path = "https://github-production-release-asset-2e65be.s3.amazonaws.com/6488049/9376ba7c-5eeb-11e6-8689-990c9e4e5cd6?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20180521%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180521T094324Z&X-Amz-Expires=300&X-Amz-Signature=906f7448748ca30f152be4457b63704bb3a24b4cd09b8043d5111b1944886979&X-Amz-SignedHeaders=host&actor_id=39096450&response-content-disposition=attachment%3B%20filename%3Dueditor1_4_3_3-utf8-php.zip&response-content-type=application%2Foctet-stream";
    public static int threadCount = 6;
    public static void main(String[] args) throws Exception{
        //1.连接服务器，获取一个文件，获取文件的长度，在本地创建一个跟服务器一样大小的临时文件
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        if (code == 200) {
            //服务器端返回的数据的长度，实际上就是文件的长度
            int length = conn.getContentLength();
            System.out.println("文件总长度："+length);
            //在客户端本地创建出来一个大小跟服务器端一样大小的临时文件
            RandomAccessFile raf = new RandomAccessFile("ude.zip", "rwd");
            //指定创建的这个文件的长度
            raf.setLength(length);
            raf.close();
            //假设是3个线程去下载资源。
            //平均每一个线程下载的文件大小.
            int blockSize = length / threadCount;
            for (int threadId = 1; threadId <= threadCount; threadId++) {
                //第一个线程下载的开始位置
                int startIndex = (threadId - 1) * blockSize;
                int endIndex = threadId * blockSize - 1;
                if (threadId == threadCount) {//最后一个线程下载的长度要稍微长一点
                    endIndex = length;
                }
                System.out.println("线程："+threadId+"下载:---"+startIndex+"--->"+endIndex);
                new DownLoadThread(path, threadId, startIndex, endIndex).start();
            }
        
        }else {
            System.out.printf("服务器错误!");
        }
    }
    
    public static class DownLoadThread extends Thread{
        private int threadId;
        private int startIndex;
        private int endIndex;
        /**
         * @param path 下载文件在服务器上的路径
         * @param threadId 线程Id
         * @param startIndex 线程下载的开始位置
         * @param endIndex    线程下载的结束位置
         */
        public DownLoadThread(String path, int threadId, int startIndex, int endIndex) {
            super();
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                //重要:请求服务器下载部分文件 指定文件的位置
                conn.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
                //从服务器请求全部资源返回200 ok如果从服务器请求部分资源 返回 206 ok
                int code = conn.getResponseCode();
                System.out.println("code:"+code);
                InputStream is = conn.getInputStream();//已经设置了请求的位置，返回的是当前位置对应的文件的输入流
                RandomAccessFile raf = new RandomAccessFile("ude.zip", "rwd");
                //随机写文件的时候从哪个位置开始写
                raf.seek(startIndex);//定位文件
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                }
                is.close();
                raf.close();
                System.out.println("线程："+threadId+"下载完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}