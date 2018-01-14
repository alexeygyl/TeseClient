package com.example.smit.teseclient;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.smit.teseclient.Fragments.List;
import com.example.smit.teseclient.Units.MusicUnits;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



public  class General {
    static Context context;
    public static DatagramSocket serversocket;
    public static InetAddress serverAddr = null;
    public static JSONObject jsonObject;

    public static String getStrTime1000(Integer curTime){
        String time  = new String();
        Integer h,m,s,tmp;
        if(curTime/1000>=60&&curTime/1000<60*60){
            m = curTime/(1000*60);
            s = (curTime/1000)-(60*m);
            time = m<10?"0"+Integer.toString(m):Integer.toString(m);
            time += ":";
            time += s<10?"0"+Integer.toString(s):Integer.toString(s);
        }else if(curTime/1000<60){
            s = curTime/1000;
            time ="00:";
            time += s<10?"0"+Integer.toString(s):Integer.toString(s);
        }else if(curTime/1000 >= 60*60 ){
            h = curTime/(1000*60*60);
            m = ((curTime/1000)-(h*60*60))/60;
            s = (curTime/1000)-(h*60*60)-(m*60);
            time = Integer.toString(h);
            time +=":";
            time = m<10?"0"+Integer.toString(m):Integer.toString(m);
            time +=":";
            time += s<10?"0"+Integer.toString(s):Integer.toString(s);
        }

        return time;
    }

    public static String getStrTime(Integer curTime){
        String time  = new String();
        Integer h,m,s,tmp;
        if(curTime>=60&&curTime<60*60){
            m = curTime/60;
            s = (curTime)-(60*m);
            time = m<10?"0"+Integer.toString(m):Integer.toString(m);
            time += ":";
            time += s<10?"0"+Integer.toString(s):Integer.toString(s);
        }else if(curTime<60){
            s = curTime;
            time ="00:";
            time += s<10?"0"+Integer.toString(s):Integer.toString(s);
        }else if(curTime >= 60*60 ){
            h = curTime*60*60;
            m = ((curTime)-(h*60*60))/60;
            s = (curTime)-(h*60*60)-(m*60);
            time = Integer.toString(h);
            time +=":";
            time = m<10?"0"+Integer.toString(m):Integer.toString(m);
            time +=":";
            time += s<10?"0"+Integer.toString(s):Integer.toString(s);
        }

        return time;
    }

    public static void runUDPserver(final String ip, final short port){

        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    InetAddress serv_address = InetAddress.getByName(ip);
                    serversocket = new DatagramSocket(port,serv_address);


                    byte [] rxBuff = new byte[1450];
                    int pos;
                    while(true) {
                        final DatagramPacket packet= new DatagramPacket(rxBuff, rxBuff.length);
                        serversocket.receive(packet);
                        switch (packet.getData()[0]){
                            case (byte) 0xff:
                                serverAddr = packet.getAddress();
                             break;
                            case (byte)230:

                                final  short s = (short) ((packet.getData()[2]  & 0xFF)<<8 | (packet.getData()[1] & 0xFF));
                                MainActivity.handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        List.setSeekBar((int)s);
                                    }
                                });
                            break;
                            case (byte)210:
                                try {
                                    final int id;
                                    final String title;
                                    final String artist;
                                    final String album;
                                    final String year;
                                    final Integer duration;
                                    final String result_mess=new String(packet.getData(),1,packet.getLength()-1);
                                    //Log.d("result_mess",result_mess);
                                    jsonObject = new JSONObject(result_mess);
                                    id = jsonObject.getInt("id");
                                    title = jsonObject.getString("title");
                                    artist = jsonObject.getString("artist");
                                    album = jsonObject.getString("album");
                                    duration = jsonObject.getInt("duration");
                                    year = jsonObject.getString("year");
                                    MainActivity.stopMusicListUpdateSheduler();
                                    MainActivity.handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            List.musicUnits.add(new MusicUnits(id,title,artist,album,duration,year));
                                            List.updateList();
                                            MainActivity.stopMusicListUpdateSheduler();

                                        }
                                    });

                                }catch (StringIndexOutOfBoundsException e){
                                    e.getMessage();
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                break;

                        }
                        for (int i=0;i<rxBuff.length;i++) rxBuff[i]='\0';
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thrd.start();


    }

    public   void stopUDPServer(){
    serversocket.close();
}


    public static void getMusicList(){
        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] rxBuff = new byte[1];
                    rxBuff[0] = (byte) 210;
                    final DatagramPacket packet = new DatagramPacket(rxBuff, rxBuff.length);
                    packet.setAddress(serverAddr);
                    packet.setPort(9999);
                    while(serversocket == null){};
                    serversocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
        thrd.start();
    }

    public static void startPlay(final int Mid){
        Log.d("startPlay","startPlay");
        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                  byte[] rxBuff = new byte[2];
                  rxBuff[0] = (byte) 200;
                  rxBuff[1] = (byte)Mid;
                  final DatagramPacket packet = new DatagramPacket(rxBuff, rxBuff.length);
                  packet.setAddress(serverAddr);
                  packet.setPort(9999);
                  serversocket.send(packet);
              } catch (IOException e) {
                  e.printStackTrace();
              } catch(NullPointerException e){
                  e.printStackTrace();
              }

             }
        });
        thrd.start();
    }

    public static void sendClientAccept(){
        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] rxBuff = new byte[1];
                    rxBuff[0] = (byte) 205;
                    final DatagramPacket packet = new DatagramPacket(rxBuff, rxBuff.length);
                    packet.setAddress(serverAddr);
                    packet.setPort(9999);
                    serversocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
        thrd.start();
    }


    public static void Pause(){
        Log.e("Pause","Pause");
        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] rxBuff = new byte[1];
                    rxBuff[0] = (byte) 202;
                    final DatagramPacket packet = new DatagramPacket(rxBuff, rxBuff.length);
                    packet.setAddress(serverAddr);
                    packet.setPort(9999);
                    serversocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
        thrd.start();

    }

    public static void stopPlay() {
        Log.e("stopPlay","stopPlay");
       // if(MainActivity.status == MainActivity.PLAY  ) {
            Thread thrd = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] rxBuff = new byte[1];
                        rxBuff[0] = (byte) 201;
                        final DatagramPacket packet = new DatagramPacket(rxBuff, rxBuff.length);
                        packet.setAddress(InetAddress.getByName("192.168.1.200"));
                        packet.setPort(9999);
                        serversocket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            });
            thrd.start();
            MainActivity.status = MainActivity.STOP;
       // }
    }

    public static void changeVolume(final int volume){

            Thread thrd = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] rxBuff = new byte[2];
                        rxBuff[0] = (byte) 203;
                        rxBuff[1] = (byte)volume;
                        final DatagramPacket packet = new DatagramPacket(rxBuff, rxBuff.length);
                        packet.setAddress(InetAddress.getByName("192.168.1.200"));
                        packet.setPort(9999);
                        serversocket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            });
            thrd.start();

    }

    public static void changePosition(final short position){

        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] rxBuff = new byte[3];
                    rxBuff[0] = (byte) 204;
                    rxBuff[1] = (byte)(position);
                    rxBuff[2] = (byte)(position >>8);
                    final DatagramPacket packet = new DatagramPacket(rxBuff, rxBuff.length);
                    packet.setAddress(InetAddress.getByName("192.168.1.200"));
                    packet.setPort(9999);
                    serversocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
        thrd.start();

    }


}
