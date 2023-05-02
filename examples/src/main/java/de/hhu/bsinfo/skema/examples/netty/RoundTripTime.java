package de.hhu.bsinfo.skema.examples.netty;

import java.util.Arrays;

public class RoundTripTime {

    private long sendTime;
    private long receivedTime;

    private byte[] data = new byte[1024 * 8];

    public RoundTripTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public RoundTripTime(long sendTime, long receivedTime) {
        this.sendTime = sendTime;
        this.receivedTime = receivedTime;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long p_sendTime) {
        sendTime = p_sendTime;
    }

    public void setReceivedTime(long p_receivedTime) {
        receivedTime = p_receivedTime;
    }

    public long getElapsedTime() {
        return receivedTime - sendTime;
    }

    @Override
    public String toString() {
        return String.format("[%d | %d] %s", sendTime, receivedTime, Arrays.toString(data));
    }
}
