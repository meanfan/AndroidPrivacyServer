package com.mean.androidprivacy.server.demo.analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: ProcessOutputThread
 * @Description: FlowDroid 运行时的异步输出线程类
 * @Author: MeanFan
 * @Create: 2020-05-24 11:23
 * @Version: 1.0
 **/
class ProcessOutputThread extends Thread
{
    private InputStream is;
    private OutputStream os;
    public ProcessOutputThread(InputStream is, OutputStream os) throws IOException
    {
        if(is == null)
        {
            throw new IOException("InputStream is null");
        }
        if(os == null)
        {
            throw new IOException("OutputStream is null");
        }
        this.is = is;
        this.os = os;
    }

    /**
    * @Author: MeanFan
    * @Description: 重写Thread类的run()方法
    * @Param: []
    * @return: void
    **/

    @Override
    public void run() {
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            inputStreamReader = new InputStreamReader(is);
            br = new BufferedReader(inputStreamReader);
            outputStreamWriter = new OutputStreamWriter(os);
            bw = new BufferedWriter(outputStreamWriter);
            String output = null;
            while ((output = br.readLine()) != null) {
                bw.write(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (is != null) {
                    is.close();
                }
                if (bw != null) {
                    bw.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
