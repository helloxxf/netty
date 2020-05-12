package com.xxf.netty.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class BioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 6666);
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            String s = "hello";
            outputStream.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }


    }
}
