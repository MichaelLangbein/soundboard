package org.langbein.michael.soundboard.sound;

import android.content.Context;
import android.util.Log;

import org.langbein.michael.soundboard.osc.OSCMessage;
import org.langbein.michael.soundboard.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;


public class OscWrapperThread extends Thread {

    private class MessageTask {
        private Object[] args;
        private String addressString;

        public MessageTask(String addressString, Object[] args) {
            this.addressString = addressString;
            this.args = args;
        }
    };

    private boolean alive;
    private OSCPortOut sender;
    private LinkedList<MessageTask> queue;

    public OscWrapperThread(Context context) {
        queue = new LinkedList<MessageTask>();
    }

    public void setTarget(String ip, int port) throws UnknownHostException, SocketException {
        InetAddress ipAdress = InetAddress.getByName(ip);
        sender = new OSCPortOut(ipAdress, port);
    }

    public void oscStart() {
        if(alive) return;
        alive = true;
        run();
    }

    public void oscStop() {
        alive = false;
        sender.close();
    }

    @Override
    public void run() {
        while(alive) {
            MessageTask mt = queue.getFirst();
            sendMessage(mt.addressString, mt.args);
        }
    }


    private void sendMessage(String addressString, Object[] args) {
        OSCMessage msg = new OSCMessage(addressString, args);
        if(sender != null) {
            try {
                sender.send(msg);
            } catch (Exception e) {
                Log.e("OSCWrapper", "Fehler beim senden: " + e.getMessage());
            }
        }
    }

    public void soundOn(double intensity, double frequency) {

        //Object args[] = new Object[2];
        //args[0] = new Integer(3);
        //args[1] = "hello";
        //queue.add(new MessageTask(addressString, args));
    }

    public void soundOff(double frequency) {

        //Object args[] = new Object[2];
        //args[0] = new Integer(3);
        //args[1] = "hello";
        //queue.add(new MessageTask(addressString, args));

    }

}
