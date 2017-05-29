package com.TCPData;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class TCPData_1 extends JFrame implements Runnable{
	TextArea jta=null;
	JButton jb=null;
	private static int m_counter=0;
	
	public TCPData_1() throws IOException{
    	this.setTitle("abc");
    	this.setSize(1000,600);
    	this.setLocation(300,300);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setVisible(true); 	
    	
    	jta=new TextArea();
    	jb=new JButton();
 	
    	jta.setForeground(Color.green);
    	jta.setBackground(Color.black);
    	jta.setFont(new Font("Serif", Font.PLAIN, 16));
    	this.add(jta);
    	//.add(jb);
    	//BoxLayout layout=new BoxLayout(this, BoxLayout.Y_AXIS); 
    	//this.setLayout(layout);

    	//jta.setText(jta.getText()+"hello");
        //jta.setCaretPosition(jta.getText().length());
    	
    	//Thread thread=new Thread(this);
    	//thread.start();
    	
    	final NetworkInterface[] devices = JpcapCaptor.getDeviceList();
    	NetworkInterface nc = devices[0];  	
    	jta.setText("des:"+nc.datalink_description+"\n");
    	JpcapCaptor jpcap;
		jpcap = JpcapCaptor.openDevice(nc, 2000, true, 20);
    	jpcap.loopPacket(2000, new TestPacketReceiver());	
    }
	class TestPacketReceiver  implements PacketReceiver{       
	    public void receivePacket(Packet packet) {    
	      //Tcp包,在java Socket中只能得到负载数据    
	      if(packet instanceof jpcap.packet.TCPPacket){    
	          TCPPacket p=(TCPPacket)packet;    
	          String s="TCPPacket:| dst_ip "+p.dst_ip+":"+p.dst_port    
	                   +"|src_ip "+p.src_ip+":"+p.src_port    
	                   +" |len: "+p.len;    
	          jta.append(s+"\n"); 
	          byte[] data1;
	          data1=p.data;
	          jta.append("dataLength:"+data1.length+":\n");

	          for (int i=0;i<data1.length;i++){
	        	  s=Integer.toHexString(data1[i]&0xFF);
	        	  //String s1=Integer.toString(data1[i]&0xFF);
	        	  jta.append(s+" ");
	          }
	          jta.append("\n"+s+"\n");
	          //data = p.getTCPData();
	      }
	      //UDP包,开着QQ,你就会看到:它是tcp+udp    
	     /* else if(packet instanceof jpcap.packet.UDPPacket){    
	          UDPPacket p=(UDPPacket)packet;    
	          String s="UDPPacket:| dst_ip "+p.dst_ip+":"+p.dst_port    
	           +"|src_ip "+p.src_ip+":"+p.src_port    
	          +" |len: "+p.len;    
	          jta.append(s+"\n"); 	          
	      }    */
	      //如果你要在程序中构造一个ping报文,就要构建ICMPPacket包    
	     else if(packet instanceof jpcap.packet.ICMPPacket){    
	         ICMPPacket p=(ICMPPacket)packet;    
	         //ICMP包的路由链    
	         String router_ip="";    
	         for(int i=0;i<p.router_ip.length;i++){    
	             router_ip+=" "+p.router_ip[i].getHostAddress();    
	         }    
	          String s="@ @ @ ICMPPacket:| router_ip "+router_ip    
	           +" |redir_ip: "+p.redir_ip    
	           +" |mtu: "+p.mtu    
	           +" |length: "+p.len;    
	          jta.append(s+"\n");     
	      }    
	      //是否地址转换协议请求包    
	     else if(packet instanceof jpcap.packet.ARPPacket){    
	         ARPPacket p=(ARPPacket)packet;    
	         //Returns the hardware address (MAC address) of the sender    
	         Object  saa=   p.getSenderHardwareAddress();    
	         Object  taa=p.getTargetHardwareAddress();    
	         String s="* * * ARPPacket:| SenderHardwareAddress "+saa    
	           +"|TargetHardwareAddress "+taa    
	           +" |len: "+p.len;    
	         jta.append(s+"\n");    
	              
	      }    
	  //取得链路层数据头 :如果你想局网抓包或伪造数据包，嘿嘿    
	   DatalinkPacket datalink  =packet.datalink;    
	   //如果是以太网包    
	   if(datalink instanceof jpcap.packet.EthernetPacket){    
	       EthernetPacket ep=(EthernetPacket)datalink;    
	        String s="datalink layer packet: "    
	            +"|DestinationAddress: "+ep.getDestinationAddress()    
	            +"|SourceAddress: "+ep.getSourceAddress();    
	        	jta.append("\n"+s+"\n");      
	   		}        
	    }    
	} 

    public void run(){
    	while(true){
    		try {
	    		
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
	public static void main(String args[]) throws IOException {
        new TCPData_1();
    }
}


 