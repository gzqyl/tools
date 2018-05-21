package test;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download {
	
    public static String path = "https://github-production-release-asset-2e65be.s3.amazonaws.com/6488049/9376ba7c-5eeb-11e6-8689-990c9e4e5cd6?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20180521%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180521T094324Z&X-Amz-Expires=300&X-Amz-Signature=906f7448748ca30f152be4457b63704bb3a24b4cd09b8043d5111b1944886979&X-Amz-SignedHeaders=host&actor_id=39096450&response-content-disposition=attachment%3B%20filename%3Dueditor1_4_3_3-utf8-php.zip&response-content-type=application%2Foctet-stream";
    public static int threadCount = 6;
    public static void main(String[] args) throws Exception{
        //1.���ӷ���������ȡһ���ļ�����ȡ�ļ��ĳ��ȣ��ڱ��ش���һ����������һ����С����ʱ�ļ�
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        if (code == 200) {
            //�������˷��ص����ݵĳ��ȣ�ʵ���Ͼ����ļ��ĳ���
            int length = conn.getContentLength();
            System.out.println("�ļ��ܳ��ȣ�"+length);
            //�ڿͻ��˱��ش�������һ����С����������һ����С����ʱ�ļ�
            RandomAccessFile raf = new RandomAccessFile("ude.zip", "rwd");
            //ָ������������ļ��ĳ���
            raf.setLength(length);
            raf.close();
            //������3���߳�ȥ������Դ��
            //ƽ��ÿһ���߳����ص��ļ���С.
            int blockSize = length / threadCount;
            for (int threadId = 1; threadId <= threadCount; threadId++) {
                //��һ���߳����صĿ�ʼλ��
                int startIndex = (threadId - 1) * blockSize;
                int endIndex = threadId * blockSize - 1;
                if (threadId == threadCount) {//���һ���߳����صĳ���Ҫ��΢��һ��
                    endIndex = length;
                }
                System.out.println("�̣߳�"+threadId+"����:---"+startIndex+"--->"+endIndex);
                new DownLoadThread(path, threadId, startIndex, endIndex).start();
            }
        
        }else {
            System.out.printf("����������!");
        }
    }
    
    public static class DownLoadThread extends Thread{
        private int threadId;
        private int startIndex;
        private int endIndex;
        /**
         * @param path �����ļ��ڷ������ϵ�·��
         * @param threadId �߳�Id
         * @param startIndex �߳����صĿ�ʼλ��
         * @param endIndex    �߳����صĽ���λ��
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
                //��Ҫ:������������ز����ļ� ָ���ļ���λ��
                conn.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
                //�ӷ���������ȫ����Դ����200 ok����ӷ��������󲿷���Դ ���� 206 ok
                int code = conn.getResponseCode();
                System.out.println("code:"+code);
                InputStream is = conn.getInputStream();//�Ѿ������������λ�ã����ص��ǵ�ǰλ�ö�Ӧ���ļ���������
                RandomAccessFile raf = new RandomAccessFile("ude.zip", "rwd");
                //���д�ļ���ʱ����ĸ�λ�ÿ�ʼд
                raf.seek(startIndex);//��λ�ļ�
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                }
                is.close();
                raf.close();
                System.out.println("�̣߳�"+threadId+"�������");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}