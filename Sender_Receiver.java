import java.util.Date;

public class Sender_Receiver
{
	private static final long STANDARD=500;
	private static final long LONG=2000;
	private static final long  VERYLONG=10000;

	SerialConnection mySerial =null;

	static final private char m_cCntrlZ=(char)26;
	String m_sIn, m_sOut;
	private long m_lDelay=STANDARD;
	String m_sPduMsg=null;
	int m_iLength=0;
	String m_sPort_Id;
	public String m_sTextMsg;
	public String m_sPhoneNum;


	// the message center
	//private SerialParameters defaultParameters= new SerialParameters (m_sPort_Id,9600,0,0,8,1,0);
	
	public int m_iStep;
	public int m_iStatus=-1;
	public long m_lMessageNo=-1;
	pdu_conversion convert=new pdu_conversion();

  /**
   * connect to the port and start the dialogue thread
   */

	public int openPort (String m_sPort_Id) throws Exception
	{
		this.m_sPort_Id=m_sPort_Id;
		SerialParameters params = new SerialParameters (m_sPort_Id,9600,0,0,8,1,0);
		mySerial =new SerialConnection (params);
		mySerial.openConnection();

		//log("start");
		return 0;
	}

  /**
   * implement the dialogue thread,
   * message / response via steps,
   * handle time out
   */


	public boolean setMode(String m_sCommand)
	{
		//log ("m_iStep:"+m_iStep);
		mySerial.send(m_sCommand);
		String result=  mySerial.getIncommingString() ;
		System.out.println("result :"+result);
		int l_iExpectedResult=-1;
		l_iExpectedResult=result.indexOf("OK");

		if(l_iExpectedResult!=-1)
		return true;
		else
		return false;
	}
	public boolean send(String m_sPhoneNum,String m_sSecured)
	{
		try
		{
			m_sPduMsg=convert.pdu_msg_former(m_sPhoneNum,m_sSecured);
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
		m_iLength=(m_sPduMsg.length())/2-1;

		//System.out.println("sender");
		mySerial.send("at+cmgs="+m_iLength+"\n");
		//log("length :"+m_iLength);

		String result=  mySerial.getIncommingString() ;
		int l_iExpectedResult=-1;
		l_iExpectedResult=result.indexOf(">");
		if(l_iExpectedResult!=-1)
		{
			mySerial.send(m_sPduMsg+m_cCntrlZ);
			result=  mySerial.getIncommingString() ;
			//l_iExpectedResult=result.indexOf("OK");
			if(l_iExpectedResult!=-1)
			{
				//log(" message sent\n");
				return true;
			}
			else
			{
				//log ("sending failed\n");
				return false;
			}
		}
		else
		{
			//log ("sending failed\n");	
			return false;
		}
	}

	public return_format receive(String l_sCommand)
	{
		String return_text="";
		return_format ret_obj=new return_format();

		try
		{
			mySerial.send(l_sCommand);
			//log(l_sCommand);

			String l_sReceivedMsg=mySerial.getIncommingString();
			int l_iOffset=l_sReceivedMsg.indexOf("0791");

			String l_sDecodableMsg=l_sReceivedMsg.substring(l_iOffset);
			//log("received string :"+l_sDecodableMsg);

			pdu_decode obj=new pdu_decode();
			ret_obj=obj.decode(l_sDecodableMsg);
			//log(return_text);
		}
		catch(Exception e)
		{
			log(e.toString());
		}
		return ret_obj;
	}

	public String receive_length(String l_sCommand)
	{
		mySerial.send(l_sCommand);
		//log(l_sCommand);
		return (mySerial.getIncommingString() );
	}
	/**
 	* logging function, includes date and class name
 	*/

	private void log(String s)
	{
		System.out.println (new java.util.Date()+":"+this.getClass().getName()+":"+s);
	}
}