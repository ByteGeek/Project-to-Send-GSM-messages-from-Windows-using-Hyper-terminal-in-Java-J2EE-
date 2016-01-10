package encode_decode_package;


public class encode_decode
{
	public String Encode_Decode(String message)
	{
		int bLoopIndex = 0;
		int ilastCounterVal = 0;
    		int iCurrCtrVal = 0;
		String l_sResultString = "";
	
		/* last byte of message packet contains SendCommand = 0x1A which should not be encoded. hence sizeof(MessagePacket)-1 */
		for( bLoopIndex = 0; bLoopIndex < message.length(); bLoopIndex++) 
		{
        		if ( iCurrCtrVal == 0 ) 
			{
            			if ( ilastCounterVal < 5 ) 
				{
					++ilastCounterVal;
				}
            			else
				{
					ilastCounterVal = 0;
				}
			
            			iCurrCtrVal = ilastCounterVal;
				l_sResultString += message.charAt(bLoopIndex);
        		}
			else
			{
            			l_sResultString += (char)(0x7F - (int)message.charAt(bLoopIndex));
            			--iCurrCtrVal;
        		}
		}
		return l_sResultString;
	}
}