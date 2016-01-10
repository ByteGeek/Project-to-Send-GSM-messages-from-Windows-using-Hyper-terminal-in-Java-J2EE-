import java.util.*;
import java.text.*;


public class return_format
{
	public static int length;
	public String curr_date_time;
	public String read_date_time;
	public String decoded_msg;
	public String phone_no;

	return_format()
	{
	}
	return_format(String time,String phone_no,String secured_text_msg)
	{
		length++;
		this.read_date_time=time;
		this.phone_no=phone_no;
		this.decoded_msg=secured_text_msg;
		Format formatter;
		Date date = new Date();
		formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		curr_date_time=formatter.format(date);
	}

	public void text_message(String text)
	{
		this.decoded_msg=text;
	}

	public int getLength()
	{
		return length;
	}
}