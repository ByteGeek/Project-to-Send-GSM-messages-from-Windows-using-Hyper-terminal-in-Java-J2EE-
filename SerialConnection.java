import javax.comm.*;
import java.io.*;
import java.awt.TextArea;
import java.awt.event.*;
import java.util.TooManyListenersException;

//CLASS THAT TAKES CARE OF RECIEVING THE MESSAGE BYTE BY BYTE FROM THE SIM//
//CLASS ALSO TAKES CARE OF SENDING THE COMMANDS TO THE TERMINAL BYTE BY BYTE//
//USES INPUT STREAM AND OUTPUTSTREAM BUFFER//


public class SerialConnection implements SerialPortEventListener,CommPortOwnershipListener
{
	public String m_sReadDat="";
	private SerialParameters parameters;
	private OutputStream os;
	private InputStream is;
	private KeyHandler keyHandler;

	private CommPortIdentifier portId;
	private SerialPort sPort;

	private boolean m_bOpen;

	private String m_sReceptionString="";

	public String getIncommingString()
	{
		//System.out.println("get incomming string");
		byte[] bVal= m_sReceptionString.getBytes();
		m_sReceptionString="";
		return new String (bVal);
  	}

	public SerialConnection(SerialParameters parameters)
	{
		//System.out.println("serial connection");
		this.parameters = parameters;
		m_bOpen = false;
	}

	public void openConnection() throws SerialConnectionException
	{
		System.out.println("open connection");
		try
		{
			portId = CommPortIdentifier.getPortIdentifier(parameters.getPortName());
		}
		catch (NoSuchPortException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			sPort = (SerialPort)portId.open("SMSConnector", 30000);
		}
		catch (PortInUseException e)
		{
			throw new SerialConnectionException(e.getMessage());
		}
		sPort.sendBreak(1000);

		try
		{
			setConnectionParameters();
		}
		catch (SerialConnectionException e)
		{
			sPort.close();
			throw e;
		}

		try
		{
			os = sPort.getOutputStream();
			is = sPort.getInputStream();
		}
		catch (IOException e)
		{
			sPort.close();
			throw new SerialConnectionException("Error opening i/o streams");
		}

		try
		{
			sPort.addEventListener(this);
		}
		catch (TooManyListenersException e)
		{
			sPort.close();
			throw new SerialConnectionException("too many listeners added");
		}
		sPort.notifyOnDataAvailable(true);
		sPort.notifyOnBreakInterrupt(true);

		try
		{
			sPort.enableReceiveTimeout(30);
		}
		catch (UnsupportedCommOperationException e)
		{
		}

		portId.addPortOwnershipListener(this);
		m_bOpen = true;
	}

	public void setConnectionParameters() throws SerialConnectionException
	{
		int oldBaudRate = sPort.getBaudRate();
		int oldDatabits = sPort.getDataBits();
		int oldStopbits = sPort.getStopBits();
		int oldParity   = sPort.getParity();
		int oldFlowControl = sPort.getFlowControlMode();

		try
		{
			System.out.println("set params");
			sPort.setSerialPortParams(parameters.getBaudRate(),
			parameters.getDatabits(),
			parameters.getStopbits(),
			parameters.getParity());
		}
		catch (UnsupportedCommOperationException e)
		{
			parameters.setBaudRate(oldBaudRate);
			parameters.setDatabits(oldDatabits);
			parameters.setStopbits(oldStopbits);
			parameters.setParity(oldParity);
			throw new SerialConnectionException("Unsupported parameter");
		}
		try
		{
			sPort.setFlowControlMode(parameters.getFlowControlIn() | parameters.getFlowControlOut());
		}
		catch (UnsupportedCommOperationException e)
		{
			throw new SerialConnectionException("Unsupported flow control");
		}
	}

	public void closeConnection()
	{
		if (!m_bOpen)
		{
			return;
		}

		if (sPort != null)
		{
			try
			{
				System.out.println("close outputstream usage");
				os.close();
				is.close();
			}
			catch (IOException e)
			{
				System.err.println(e);
			}
			sPort.close();

			portId.removePortOwnershipListener(this);
		}
		m_bOpen = false;
	}

	public void sendBreak()
	{
		sPort.sendBreak(1000);
	}

	public boolean isOpen()
	{
		return m_bOpen;
	}

	public void serialEvent(SerialPortEvent e)
	{
		StringBuffer inputBuffer = new StringBuffer();
		int newData = 0;

		switch (e.getEventType())
		{
			case SerialPortEvent.DATA_AVAILABLE: while (newData != -1)
								{
									try
									{
										newData = is.read();
										if (newData == -1)
										{
											break;
										}
										if ('\r' == (char)newData)
										{
											inputBuffer.append('\n');
										}
										else
										{
											inputBuffer.append((char)newData);
											//System.out.println("here "+(char)newData);
											m_sReadDat+=(char)newData;
										}
									}
									catch (IOException ex)
									{
										System.err.println(ex);
										return;
									}
								}
								//System.out.println("m_sReadDat= "+m_sReadDat+" length="+m_sReadDat.length());
								m_sReceptionString=m_sReceptionString+ (new String(inputBuffer));
								break;

			case SerialPortEvent.BI: m_sReceptionString=m_sReceptionString+("\n--- BREAK RECEIVED ---\n");
		}

	}

	public void ownershipChange(int type)
	{
	}

	class KeyHandler extends KeyAdapter
	{
		OutputStream os;

		public KeyHandler(OutputStream os)
		{
			super();
			this.os = os;
		}

		public void keyTyped(KeyEvent evt)
		{
			char newCharacter = evt.getKeyChar();
			if ((int)newCharacter==10) newCharacter = '\r';
			System.out.println ((int)newCharacter);
			try
			{
				//System.out.println("key handlr outputstream usage");
				os.write((int)newCharacter);
			}
			catch (IOException e)
			{
				System.err.println("OutputStream write error: " + e);
			}
		}
	}

	public void send(String message)
	{
		byte[] theBytes= (message+"\n").getBytes();
		for (int i=0; i<theBytes.length;i++)
		{
			char newCharacter = (char)theBytes[i];
			if ((int)newCharacter==10) newCharacter = '\r';

			try
			{
				//System.out.println("outputstream usage");
				os.write((int)newCharacter);
				
			}
			catch (IOException e)
			{
				System.err.println("OutputStream write error: " + e);
			}

		}
		try
		{
			Thread.sleep(600);
		}
		catch(java.lang.InterruptedException e)
		{}
	}

	public String decode_help()
	{
		String l_sRet="";
		try
		{
		this.send("at+cmgf=0");
		this.send("at+cmgl=4");
		//System.out.println("m_sReadDat:"+m_sReadDat);
		int l_sIndex1=m_sReadDat.indexOf("0791");
		int l_sIndex2=m_sReadDat.indexOf("OK",l_sIndex1+1);
		l_sRet=m_sReadDat.substring(l_sIndex1,l_sIndex2-2);
		//System.out.println("in serialConnection: \n m_sReadDat ="+m_sReadDat+"length:"+m_sReadDat.length());
		}
		catch( java.lang.StringIndexOutOfBoundsException e)
		{
			System.out.println("no messages");
			System.exit(0);
		}
		return l_sRet;
	}
	public void delete_msg()
	{
		this.send("at+cmgd=1");
		return;
	}
}
