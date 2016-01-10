import java.util.*;
import java.text.*;


public class sending_format
{
	public String curr_date_time;
	public String secured_text_msg;
	public String phone_no;

	sending_format()
	{
	}
	sending_format(String phone_no,String secured_text_msg)
	{
		this.phone_no=phone_no;
		this.secured_text_msg=secured_text_msg;
		Format formatter;
		Date date = new Date();
		formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		curr_date_time=formatter.format(date);
	}
}