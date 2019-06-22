package com.java.io.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SourcPortListener extends Thread {

    private final int listenPort;
    private final CountDownLatch countDownLatch;
    private final List<Device> devices = new ArrayList<>();
    private boolean done = false;
    private DatagramSocket ds = null;

    public SourcPortListener(int listenPort, CountDownLatch countDownLatch) {
        this.listenPort = listenPort;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        // 通知已启动
        countDownLatch.countDown();
        try {
            // 监听回送端口
            ds = new DatagramSocket(listenPort);

            while (!done) {
                // 构建接收实体
                final byte[] buf = new byte[512];
                DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

                // 接收
                ds.receive(receivePack);

                // 打印接收到的信息与发送者的信息
                // 发送者的IP地址
                String ip = receivePack.getAddress().getHostAddress();
                int port = receivePack.getPort();
                int dataLen = receivePack.getLength();
                String data = new String(receivePack.getData(), 0, dataLen);
                System.out.println("UDPSearcher receive form ip:" + ip + "\tport:" + port + "\tdata:" + data);

                String sn = MessageCreator.parseSn(data);
                if (sn != null) {
                    Device device = new Device(port, ip, sn);
                    devices.add(device);
                }
            }
        } catch (Exception ignored) {

        } finally {
            close();
        }
        System.out.println("UDPSearcher listener finished.");

    }

    private void close() {
        if (ds != null) {
            ds.close();
            ds = null;
        }
    }

    List<Device> getDevicesAndClose() {
        done = true;
        close();
        return devices;
    }
}