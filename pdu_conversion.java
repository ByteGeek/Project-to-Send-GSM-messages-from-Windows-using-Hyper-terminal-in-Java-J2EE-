import java.io.*;



public class pdu_conversion
{
	public String ByteSwapStr(String v)
	{
		String l_sOut="";
		for(int l_iI=0;l_iI<v.length();l_iI+=2)
		{
			String l_sTemp=v.substring(l_iI,l_iI+2);
			l_sOut=l_sOut+l_sTemp.charAt(1)+l_sTemp.charAt(0);
		}
		return l_sOut;
	}
	public String toHex(int l_iIn)
	{
		switch(l_iIn)
		{
			case 10: return "A";
			case 11: return "B";
			case 12: return "C";
			case 13: return "D";
			case 14: return "E";
			case 15: return "F";
		}
		return "X";
	}
	public String encode(String text)
	{
		String out="",oct1="",oct2="";
		for(int i=0;i<=text.length();i++)
		{
			if(i==text.length())
			{
				if(oct2 != "")
				{
					try
					{
					String ts=Integer.toHexString(Integer.parseInt(oct2,2));
					if(ts.length()==1)
					{
						out+="0"+ts;
					}
					else
					{
						out+=ts;
					}
					}
					catch( java.lang.NumberFormatException e)
					{}
				}
				break;
			}
			String curr,curr_oct;
			curr=Integer.toBinaryString(text.charAt(i));
			if(curr.length() != 7)
			{
				int diff=7-curr.length();
				for(int j=1;j<=diff;j++)
				{
					curr="0"+curr;
				}
			}
			if(i!=0 && i%8!=0)
			{
				oct1=curr.substring(7-i%8);
				curr_oct=oct1+oct2;
				String ts=Integer.toHexString(Integer.parseInt(curr_oct,2));
				if(ts.length()==1)
				{
					out+="0"+ts;
				}
				else
				{
					out+=ts;
				}
				oct2=curr.substring(0,7-i%8);
			}
			else
			{
				oct2=curr.substring(0,7-i%8);
			}
		}
		return out;
	}

	public String pdu_msg_former(String m_sPhoneNum,String secured_string) throws IOException
	{
		//System.out.println("ancbdhhsgdas");
		//String smsc_no="919032155002";
		String pdu_msg="";
		try
		{
			String receiver_no=m_sPhoneNum,encoded_text;
                        int length;
			//int smsc_len=(smsc_no.length())/2+1;
			int receiver_len=receiver_no.length();
			BufferedReader read=new BufferedReader(new InputStreamReader(System.in));
			/*while(!((smsc_len==6||smsc_len==7)&&(receiver_len==10||receiver_len==12)))
			{
				System.out.println(" enter the valid smsc number\n");
				smsc_no=read.readLine();
				System.out.println(" enter the valid receiver number\n");
				receiver_no=read.readLine();
				smsc_len=(smsc_no.length())/2+1;
				receiver_len=receiver_no.length();
			}*/
			pdu_msg="00";   //+smsc_len + "91";
			String temp=""; //ByteSwapStr(smsc_no);
			pdu_msg+="11";
			if(receiver_len>9)
			{
				temp=toHex(receiver_len);
			}
			pdu_msg+="000"+temp+"91";

			/*System.out.println("number length: "+receiver_len+" "+"number is "+receiver_no);
			if( (receiver_len==10) )
			{
				receiver_no="91"+receiver_no;
				receiver_len=receiver_no.length();
				System.out.println("number length: "+receiver_len+" "+"number is "+receiver_no);
			}*/
				
			temp=ByteSwapStr(receiver_no);
			pdu_msg+=temp+"0000AA";

			String n=Integer.toHexString( (secured_string.length()) );
			if(n.length()==1)
			{
				pdu_msg+="0"+n;
			}
			else
			{
				pdu_msg+=n;
			}
			if( (secured_string.length() )>160)
			{
				//System.out.println("number of characters must not exceed 160\n");
				System.exit(0);
			}

			//System.out.println("length must be an even number: "+secured_string.length() );
			//System.out.println("secured string: "+secured_string);
			encoded_text=encode(secured_string);

			pdu_msg+=encoded_text;

			//System.out.println(smsc_no+" "+receiver_no+" "+pdu_msg.toUpperCase());
			//System.out.println(smsc_len+" "+receiver_len);
			//System.out.println(text.length());
			//length=(pdu_msg.length())/2-7-1;
                        //System.out.println(length);

		}
		catch( ArrayIndexOutOfBoundsException e )
		{
			System.out.println("enter valid nums\n");
		}
		/*catch(java.lang.InterruptedException e)
		{}*/
		//System.out.println("the pdu msg :"+pdu_msg);
		return pdu_msg;
	}
}