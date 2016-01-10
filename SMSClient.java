import java.io.*;


public class SMSClient
{
	final int SYNCHRONOUS=0;
		
	int m_iMode=-1;
	int m_iNo_Of_Msgs;

	int m_iStatus=-1;
	long m_lMessageNo=-1;
	String m_sPort_Id;

	String m_sSecured_Msg;
	String m_sPhoneNum;

	Sender_Receiver aSender = new Sender_Receiver();

	public boolean config_port(String l_sPort_Id)
	{
		this.m_sPort_Id=l_sPort_Id;

		int l_iPass=0;
		try
		{
			aSender.openPort(m_sPort_Id);
			l_iPass=1;
		}
		catch(Exception e)
		{
			System.out.println("enter valid port number");
			return false;
		}
		
		boolean l_bReturn_Status=aSender.setMode("ATE0");
		if(l_bReturn_Status==false)
		{
			System.out.println("ATE0 did not fire");
			return l_bReturn_Status;
		}
		
		l_bReturn_Status=aSender.setMode("AT");
		if(l_bReturn_Status==false)
		{
			System.out.println("AT did not fire");
			return l_bReturn_Status;
		}

		l_bReturn_Status=aSender.setMode("AT+CMGF=0");
		if(l_bReturn_Status==false)
		{
			System.out.println("AT+CMGF=0 did not fire");
			return l_bReturn_Status;
		}
		return l_bReturn_Status;
	}
		
	public boolean send(String l_sPhoneNum,String l_sSecured_Msg)
	{
		this.m_sSecured_Msg=l_sSecured_Msg;
		this.m_sPhoneNum=l_sPhoneNum;

		//String l_sTemp=l_oConvert.Encode_Decode(m_sTextMsg);

		boolean l_bStatus=aSender.send (m_sPhoneNum,l_sSecured_Msg);
				
		// System.out.println("sending ... ");
		//in SYNCHRONOUS mode wait for return : 0 for OK,
		//-2 for timeout, -1 for other errors

		if(aSender.m_iStatus == 0) m_lMessageNo=aSender.m_lMessageNo ;
		return l_bStatus;

	}

	public return_format[] receive()
	{
		String l_sNum=aSender.receive_length("AT+CPMS=\"SM\"");
		//System.out.println("at+cpms=SM :"+l_sNum+"\nlength is :"+l_sNum.length());
		int l_iOffset=l_sNum.indexOf("CPMS:");
		System.out.println("index: "+l_iOffset);
		l_iOffset+=6;
		String l_sTemp=l_sNum.substring(l_iOffset);
		String l_sNumbers[]=l_sTemp.split(",");
		//System.out.println(l_sTemp+"\n"+l_sNumbers[0]);

		m_iNo_Of_Msgs=Integer.parseInt(l_sNumbers[0]);

		return_format[] ret_objs=new return_format[m_iNo_Of_Msgs];
		for(int count=1;count<=m_iNo_Of_Msgs;count++)
		{
			String command="AT+CMGR="+Integer.toString(count);
			System.out.println(command);
			ret_objs[count-1] =aSender.receive(command);
			//System.out.println("message :"+message);
			//System.out.println("smsclient length: "+message.length());

			//String temp=l_oConvert.Encode_Decode(date_msg[1]);
				
			//System.out.println("decoded: "+temp);
			command="AT+CMGD="+Integer.toString(count);
			boolean del_stat=aSender.setMode(command);
			if(del_stat==false)
			{
				System.out.println("CMGD did not fire");
			}
		}
		return ret_objs;
	}
	public boolean existence_of_msgs()
	{
		String l_sNum=aSender.receive_length("AT+CPMS=\"SM\"");
		//System.out.println("at+cpms=SM :"+l_sNum+"\nlength is :"+l_sNum.length());
		int l_iOffset=l_sNum.indexOf("CPMS:");
		System.out.println("index: "+l_iOffset);
		l_iOffset+=6;
		String l_sTemp=l_sNum.substring(l_iOffset);
		String l_sNumbers[]=l_sTemp.split(",");
		//System.out.println(l_sTemp+"\n"+l_sNumbers[0]);

		m_iNo_Of_Msgs=Integer.parseInt(l_sNumbers[0]);

		if(m_iNo_Of_Msgs==0)
		return false;
		else
		return true;
	}
}
