import java.io.*;
import java.util.*;


class convert1
{
	public String ByteSwapStr(String v)
	{
		String out="";
		for(int i=0;i<v.length();i+=2)
		{
			String temp=v.substring(i,i+2);
			out=out+temp.charAt(1)+temp.charAt(0);
		}
		return out;
	}
	public String TimeStampConverter(String v)
	{
		String out="";
		String date=v.substring(0,6);
		String rev_date= new StringBuffer(date).reverse().toString();
		out+=rev_date.substring(4,6)+"/"+rev_date.substring(2,4)+"/"+rev_date.substring(0,2)+" ";
		String time=v.substring(6,12);
		time=this.ByteSwapStr(time);
		out+=time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
		return out;
	}
	public String Decode(String v,int length)
	{
		String bit_string="";
		String []oct_array=new String[length];
		String []rest_array=new String[length];
		String []sept_array=new String[length];
		int count=0,s=1;
		String text_msg="";
		int length_count=0;
		for(int i=0;i<v.length();i+=2)
		{
			String var=v.substring(i,i+2);
			try
			{
				String temp=Integer.toBinaryString(Integer.parseInt(var,16));
			
			int len=temp.length();
			if(len!=8)
			{
				for(int j=0;j<8-len;j++)
				temp="0"+temp;
				//System.out.println(var+":"+temp);
				bit_string+=temp;
			}
			else
			{
				//System.out.println(var+":"+temp);
				bit_string+=temp;
			}
			}
			catch( java.lang.NumberFormatException e)
			{}
		}

		for(int i=0;i<bit_string.length();i+=8)
		{
			
			oct_array[count]=bit_string.substring(i,i+8);
			rest_array[count]=oct_array[count].substring(0,s%8);
			sept_array[count]=oct_array[count].substring((s%8),8);
			s++;
			count++;
			if(s==8)
			{
				s=1;
			}
		}

		for(int i=0;i<rest_array.length;i++)
		{
			try
			{
				if(i%7==0)
				{
					if(i!=0)
					{
						text_msg+=(char) Integer.parseInt( (Integer.toHexString(Integer.parseInt(rest_array[i-1],2))),16);
						length_count++;
					}
					text_msg+=(char) Integer.parseInt( (Integer.toHexString(Integer.parseInt(sept_array[i],2))),16);
					length_count++;
				}
				else
				{
					text_msg+=(char) Integer.parseInt( (Integer.toHexString(Integer.parseInt((sept_array[i]+rest_array[i-1]),2))) ,16);
					length_count++;
				}
			}
			catch ( java.lang.NumberFormatException e)
			{ }
		}
		try
		{
			if(length_count!=length)
			{
				text_msg+=(char)Integer.parseInt( (Integer.toHexString(Integer.parseInt(rest_array[rest_array.length]))),16 );
			}
		}
		catch( java.lang.ArrayIndexOutOfBoundsException ee)
		{ }
		return text_msg.substring(0,length);
	}
}

public class pdu_decode
{
	String decodable_str;
	String smsc_no="";
	String secured_text_msg="";
	String text_msg="";
	String sender_no="";
	String time_stamp="";
	int msg_length;
	int start=4;

	public return_format decode(String decodable_str) throws Exception
	{
		convert1 con = new convert1();

		//System.out.println("decodable string:"+decodable_str);

		String temp=decodable_str.substring(start,start+12);
		start=start+12+6;
		smsc_no=con.ByteSwapStr(temp);
		//System.out.println("smsc num:"+smsc_no);

		temp=decodable_str.substring(start,start+12);
		sender_no=con.ByteSwapStr(temp);
		//System.out.println("sender num:"+sender_no);
		start+=12+4;

		temp=decodable_str.substring(start,start+12);
		time_stamp=con.TimeStampConverter(temp);
		//System.out.println(time_stamp);
		start+=12+2;

		String hex=decodable_str.substring(start,start+2);
		msg_length=Integer.parseInt(hex,16);
		//System.out.println("length of text msg:"+msg_length);
		start+=2;

		temp=decodable_str.substring(start);
		secured_text_msg=con.Decode(temp,msg_length);

		//System.out.println("pdu msg:"+temp);
		//System.out.println("secured string: "+secured_text_msg); 

		//System.out.println("text message:"+text_msg);
		return_format obj = new return_format(time_stamp,sender_no,secured_text_msg);

		return obj;
	}
}