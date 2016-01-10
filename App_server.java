import java.util.*;
import encode_decode_package.*;
import java.io.*;

class queue_creator extends App_server implements Runnable
{
	encode_decode_package.encode_decode l_oConvert=new encode_decode_package.encode_decode();
	Thread t1;

	queue_creator()
	{
		t1=new Thread(this);
		t1.start();
	}

	public void run()
	{
		while(true)
		{
		// sending phase
		if(list.size()!=0)
		{
			int length=list.size();

			for(int i=0;i<length;i++)
			{
				sending_format temp= new sending_format();
				temp=(sending_format) list.getFirst();
				boolean status= sms_client.send(temp.phone_no,temp.secured_text_msg);
				System.out.println(temp.phone_no+" "+temp.secured_text_msg);
				if(status==true)
				{
					//System.out.println("message sent at :"+temp.curr_date_time);
				}
				else
				{
					//System.out.println("sending failed");
				}
				list.removeFirst();
			}
		}

		//receiving phase
		else if(sms_client.existence_of_msgs()==true)
		{
			return_format array_of_objs[]=sms_client.receive();
			int length = array_of_objs.length;
			sms_client=null;

			try
			{
				PrintWriter out=new PrintWriter(new FileWriter("received_data.txt",true));

				for(int i=0;i<length;i++)
				{
					String text_contents="";
					text_contents=array_of_objs[i].phone_no+", "+array_of_objs[i].curr_date_time+", "+array_of_objs[i].read_date_time;
					String temp=l_oConvert.Encode_Decode(array_of_objs[i].decoded_msg);
					text_contents=", "+temp+"\n";
					out.println(text_contents);
				}
				out.close();
			}
			catch(Exception ex)
			{
				System.out.println(ex.toString());
			}
		}

		//sleeping phase
		else
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception ex)
			{
				//System.out.println(ex.toString());
			}
		}
	}
	}
}


public class App_server
{
	static LinkedList list=new LinkedList();
	static SMSClient sms_client=new SMSClient();

	public static void main(String v[]) throws IOException
	{
		String port_id="";
		encode_decode_package.encode_decode l_oConvert=new encode_decode_package.encode_decode();

		BufferedReader read=new BufferedReader(new InputStreamReader(System.in));

		System.out.println("enter the port id to open:");
		port_id=read.readLine();

		boolean status= sms_client.config_port(port_id);

		if(status==false)
		{
			System.out.println("Error in opening port");
			System.exit(0);
		}


		BufferedReader reader=new BufferedReader(new FileReader("sending_data.txt"));
		queue_creator que=new queue_creator();

		String dat1;
		while(true)
		{
			while( (dat1= reader.readLine()) !=null)
			{
				//System.out.println("enter the phone number");
				//System.out.println("enter the text");
				//String text_msg=read.readLine();

				String send_data[]=dat1.split(",");
				String secured_text=l_oConvert.Encode_Decode(send_data[1]);
				sending_format obj=new sending_format(send_data[0],secured_text); // send_data[0]= phone num and send_data[1]= the text message

				list.addLast(obj);
			}
			reader.close();
		}
	}
}
