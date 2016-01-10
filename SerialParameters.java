
import javax.comm.*;

/**
A class that stores parameters for serial ports.
*/
public class SerialParameters {

    private String m_sPortName;
    private int m_iBaudRate;
    private int m_iFlowControlIn;
    private int m_iFlowControlOut;
    private int m_iDatabits;
    private int m_iStopbits;
    private int m_iParity;

    /**
    Default constructer. Sets parameters to no port, 9600 baud, no flow
    control, 8 data bits, 1 stop bit, no parity.
    */
    public SerialParameters () {
	this("",
	     9600,
	     SerialPort.FLOWCONTROL_NONE,
	     SerialPort.FLOWCONTROL_NONE,
	     SerialPort.DATABITS_8,
	     SerialPort.STOPBITS_1,
	     SerialPort.PARITY_NONE );

    }

    /**
    Paramaterized constructer.

    @param portName The name of the port.
    @param baudRate The baud rate.
    @param flowControlIn Type of flow control for receiving.
    @param flowControlOut Type of flow control for sending.
    @param databits The number of data bits.
    @param stopbits The number of stop bits.
    @param parity The type of parity.
    */
    public SerialParameters(String m_sPortName,
			    int m_iBaudRate,
			    int m_iFlowControlIn,
			    int m_iFlowControlOut,
			    int m_iDatabits,
			    int m_iStopbits,
			    int m_iParity) {

    	this.m_sPortName = m_sPortName;
    	this.m_iBaudRate = m_iBaudRate;
    	this.m_iFlowControlIn = m_iFlowControlIn;
    	this.m_iFlowControlOut = m_iFlowControlOut;
    	this.m_iDatabits = m_iDatabits;
    	this.m_iStopbits = m_iStopbits;
    	this.m_iParity = m_iParity;
    }

    /**
    Sets port name.
    @param portName New port name.
    */
    public void setPortName(String m_sPortName) {
	this.m_sPortName = m_sPortName;
    }

    /**
    Gets port name.
    @return Current port name.
    */
    public String getPortName() {
	return m_sPortName;
    }

    /**
    Sets baud rate.
    @param baudRate New baud rate.
    */
    public void setBaudRate(int m_iBaudRate) {
	this.m_iBaudRate = m_iBaudRate;
    }

    /**
    Sets baud rate.
    @param baudRate New baud rate.
    */
    public void setBaudRate(String m_iBaudRate) {
	this.m_iBaudRate = Integer.parseInt(m_iBaudRate);
    }

    /**
    Gets baud rate as an <code>int</code>.
    @return Current baud rate.
    */
    public int getBaudRate() {
	return m_iBaudRate;
    }

    /**
    Gets baud rate as a <code>String</code>.
    @return Current baud rate.
    */
    public String getBaudRateString() {
	return Integer.toString(m_iBaudRate);
    }

    /**
    Sets flow control for reading.
    @param flowControlIn New flow control for reading type.
    */
    public void setFlowControlIn(int m_iFlowControlIn) {
	this.m_iFlowControlIn = m_iFlowControlIn;
    }

    /**
    Sets flow control for reading.
    @param flowControlIn New flow control for reading type.
    */
    public void setFlowControlIn(String m_iFlowControlIn) {
	this.m_iFlowControlIn = stringToFlow(m_iFlowControlIn);
    }

    /**
    Gets flow control for reading as an <code>int</code>.
    @return Current flow control type.
    */
    public int getFlowControlIn() {
	return m_iFlowControlIn;
    }

    /**
    Gets flow control for reading as a <code>String</code>.
    @return Current flow control type.
    */
    public String getFlowControlInString() {
	return flowToString(m_iFlowControlIn);
    }

    /**
    Sets flow control for writing.
    @param flowControlIn New flow control for writing type.
    */
    public void setFlowControlOut(int m_iFlowControlOut) {
	this.m_iFlowControlOut = m_iFlowControlOut;
    }

    /**
    Sets flow control for writing.
    @param flowControlIn New flow control for writing type.
    */
    public void setFlowControlOut(String m_iFlowControlOut) {
	this.m_iFlowControlOut = stringToFlow(m_iFlowControlOut);
    }

    /**
    Gets flow control for writing as an <code>int</code>.
    @return Current flow control type.
    */
    public int getFlowControlOut() {
	return m_iFlowControlOut;
    }

    /**
    Gets flow control for writing as a <code>String</code>.
    @return Current flow control type.
    */
    public String getFlowControlOutString() {
	return flowToString(m_iFlowControlOut);
    }

    /**
    Sets data bits.
    @param databits New data bits setting.
    */
    public void setDatabits(int m_iDatabits) {
	this.m_iDatabits = m_iDatabits;
    }

    /**
    Sets data bits.
    @param databits New data bits setting.
    */
    public void setDatabits(String m_iDatabits) {
	if (m_iDatabits.equals("5")) {
	    this.m_iDatabits = SerialPort.DATABITS_5;
	}
	if (m_iDatabits.equals("6")) {
	    this.m_iDatabits = SerialPort.DATABITS_6;
	}
	if (m_iDatabits.equals("7")) {
	    this.m_iDatabits = SerialPort.DATABITS_7;
	}
	if (m_iDatabits.equals("8")) {
	    this.m_iDatabits = SerialPort.DATABITS_8;
	}
    }

    /**
    Gets data bits as an <code>int</code>.
    @return Current data bits setting.
    */
    public int getDatabits() {
	return m_iDatabits;
    }

    /**
    Gets data bits as a <code>String</code>.
    @return Current data bits setting.
    */
    public String getDatabitsString() {
	switch(m_iDatabits) {
	    case SerialPort.DATABITS_5:
		return "5";
	    case SerialPort.DATABITS_6:
		return "6";
	    case SerialPort.DATABITS_7:
		return "7";
	    case SerialPort.DATABITS_8:
		return "8";
	    default:
		return "8";
	}
    }

    /**
    Sets stop bits.
    @param stopbits New stop bits setting.
    */
    public void setStopbits(int m_iStopbits) {
	this.m_iStopbits = m_iStopbits;
    }

    /**
    Sets stop bits.
    @param stopbits New stop bits setting.
    */
    public void setStopbits(String m_iStopbits) {
	if (m_iStopbits.equals("1")) {
	    this.m_iStopbits = SerialPort.STOPBITS_1;
	}
	if (m_iStopbits.equals("1.5")) {
	    this.m_iStopbits = SerialPort.STOPBITS_1_5;
	}
	if (m_iStopbits.equals("2")) {
	    this.m_iStopbits = SerialPort.STOPBITS_2;
	}
    }

    /**
    Gets stop bits setting as an <code>int</code>.
    @return Current stop bits setting.
    */
    public int getStopbits() {
	return m_iStopbits;
    }

    /**
    Gets stop bits setting as a <code>String</code>.
    @return Current stop bits setting.
    */
    public String getStopbitsString() {
	switch(m_iStopbits) {
	    case SerialPort.STOPBITS_1:
		return "1";
	    case SerialPort.STOPBITS_1_5:
		return "1.5";
	    case SerialPort.STOPBITS_2:
		return "2";
	    default:
		return "1";
	}
    }

    /**
    Sets parity setting.
    @param parity New parity setting.
    */
    public void setParity(int m_iParity) {
	this.m_iParity = m_iParity;
    }

    /**
    Sets parity setting.
    @param parity New parity setting.
    */
    public void setParity(String m_iParity) {
	if (m_iParity.equals("None")) {
	    this.m_iParity = SerialPort.PARITY_NONE;
	}
	if (m_iParity.equals("Even")) {
	    this.m_iParity = SerialPort.PARITY_EVEN;
	}
	if (m_iParity.equals("Odd")) {
	    this.m_iParity = SerialPort.PARITY_ODD;
	}
    }

    /**
    Gets parity setting as an <code>int</code>.
    @return Current parity setting.
    */
    public int getParity() {
	return m_iParity;
    }

    /**
    Gets parity setting as a <code>String</code>.
    @return Current parity setting.
    */
    public String getParityString() {
	switch(m_iParity) {
	    case SerialPort.PARITY_NONE:
		return "None";
 	    case SerialPort.PARITY_EVEN:
		return "Even";
	    case SerialPort.PARITY_ODD:
		return "Odd";
	    default:
		return "None";
	}
    }

    /**
    Converts a <code>String</code> describing a flow control type to an
    <code>int</code> type defined in <code>SerialPort</code>.
    @param flowControl A <code>string</code> describing a flow control type.
    @return An <code>int</code> describing a flow control type.
    */
    private int stringToFlow(String l_sFlowControl) {
	if (l_sFlowControl.equals("None")) {
	    return SerialPort.FLOWCONTROL_NONE;
	}
	if (l_sFlowControl.equals("Xon/Xoff Out")) {
	    return SerialPort.FLOWCONTROL_XONXOFF_OUT;
	}
	if (l_sFlowControl.equals("Xon/Xoff In")) {
	    return SerialPort.FLOWCONTROL_XONXOFF_IN;
	}
	if (l_sFlowControl.equals("RTS/CTS In")) {
	    return SerialPort.FLOWCONTROL_RTSCTS_IN;
	}
	if (l_sFlowControl.equals("RTS/CTS Out")) {
	    return SerialPort.FLOWCONTROL_RTSCTS_OUT;
	}
	return SerialPort.FLOWCONTROL_NONE;
    }

    /**
    Converts an <code>int</code> describing a flow control type to a
    <code>String</code> describing a flow control type.
    @param flowControl An <code>int</code> describing a flow control type.
    @return A <code>String</code> describing a flow control type.
    */
    String flowToString(int l_iFlowControl) {
	switch(l_iFlowControl) {
	    case SerialPort.FLOWCONTROL_NONE:
		return "None";
	    case SerialPort.FLOWCONTROL_XONXOFF_OUT:
		return "Xon/Xoff Out";
	    case SerialPort.FLOWCONTROL_XONXOFF_IN:
		return "Xon/Xoff In";
	    case SerialPort.FLOWCONTROL_RTSCTS_IN:
		return "RTS/CTS In";
	    case SerialPort.FLOWCONTROL_RTSCTS_OUT:
		return "RTS/CTS Out";
	    default:
		return "None";
	}
    }
}
