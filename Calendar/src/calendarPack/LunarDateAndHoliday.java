package calendarPack;

import java.util.Calendar;

import com.ibm.icu.util.ChineseCalendar;

public class LunarDateAndHoliday {

	public boolean red = false;
	public boolean lunar_red = false;
	public String checkSolar(int m, int d) {
		red = false;
		if(m == 1 && d == 1) {
			red = true;
			return "<html><font color=red>����</html>";
		}
		else if(m == 3 && d == 1) {
			red = true;
			return "<html><font color=red>������</html>";
		}
		else if(m == 5 && d == 5) {
			red = true;
			return "<html><font color=red>��̳�</html>";
		}
		else if(m == 6 && d == 6) {
			red = true;
			return "<html><font color=red>������</html>";
		}
		else if(m == 8 && d == 15) {
			red = true;
			return "<html><font color=red>������</html>";
		}
		else if(m == 10 && d == 3) {
			red = true;
			return "<html><font color=red>��õ��</html>";
		}
		else if(m == 10 && d == 9) {
			red = true;
			return "<html><font color=red>�ѱ۳�</html>";
		}
		else if(m == 12 && d == 25) {
			red = true;
			return "<html><font color=red>��ź��</html>";
		}
		else if(m == 7 && d == 17) {
			return "<html><font color=black>������</html>";
		}
		
		// ����
		else if(m == 2 && d == 4) {
			return "<html><font color=gray>����</html>";
		}
		else if(m == 2 && d == 19) {
			return "<html><font color=gray>���</html>";
		}
		else if(m == 3 && d == 6) {
			return "<html><font color=gray>��Ĩ</html>";
		}
		else if(m == 3 && d == 21) {
			return "<html><font color=gray>���</html>";
		}
		else if(m == 4 && d == 5) {
			return "<html><font color=gray>û��</html>";
		}
		else if(m == 4 && d == 20) {
			return "<html><font color=gray>���</html>";
		}
		else if(m == 5 && d == 6) {
			return "<html><font color=gray>����</html>";
		}
		else if(m == 5 && d == 21) {
			return "<html><font color=gray>�Ҹ�</html>";
		}
		else if(m == 6 && d == 5) {
			return "<html><font color=gray>����</html>";
		}
		else if(m == 6 && d == 22) {
			return "<html><font color=gray>����</html>";
		}
		else if(m == 7 && d == 7) {
			return "<html><font color=gray>�Ҽ�</html>";
		}
		else if(m == 7 && d == 23) {
			return "<html><font color=gray>�뼭</html>";
		}
		else if(m == 8 && d == 8) {
			return "<html><font color=gray>����</html>";
		}
		else if(m == 8 && d == 23) {
			return "<html><font color=gray>ó��</html>";
		}
		else if(m == 9 && d == 8) {
			return "<html><font color=gray>���</html>";
		}
		else if(m == 9 && d == 23) {
			return "<html><font color=gray>�ߺ�</html>";
		}
		else if(m == 10 && d == 8) {
			return "<html><font color=gray>�ѷ�</html>";
		}
		else if(m == 10 && d == 24) {
			return "<html><font color=gray>��</html>";
		}
		else if(m == 11 && d == 8) {
			return "<html><font color=gray>�Ե�</html>";
		}
		else if(m == 11 && d == 22) {
			return "<html><font color=gray>�Ҽ�</html>";
		}
		else if(m == 12 && d == 7) {
			return "<html><font color=gray>�뼳</html>";
		}
		else if(m == 12 && d == 22) {
			return "<html><font color=gray>����</html>";
		}
		else if(m == 1 && d == 6) {
			return "<html><font color=gray>����</html>";
		}
		else if(m == 1 && d == 20) {
			return "<html><font color=gray>����</html>";
		}
		else {
			return null;
		}
	}
	
	public String checkLunar(int ly, int lm, int ld) {
		ChineseCalendar cCal = new ChineseCalendar();
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR,  ly);
		cal.set(Calendar.MONTH, lm);
		cal.set(Calendar.DAY_OF_MONTH, ld);
		
		cCal.setTimeInMillis(cal.getTimeInMillis());
		
		int y = cCal.get(ChineseCalendar.EXTENDED_YEAR)-2637;
		int m = cCal.get(ChineseCalendar.MONTH)+1;
		int d = cCal.get(ChineseCalendar.DAY_OF_MONTH);
		
		int lastDate_inMonth = cCal.getActualMaximum(ChineseCalendar.DAY_OF_MONTH);
		if(m == 1 && d == 1) {
			red = true;
			return "<html><font color=red>����</html>";
		}
		else if(m == 12 && d == lastDate_inMonth) {
			red = true;
			return "<html><font color=red>�� ����</html>";
		}
		else if(m == 1 && d == 2) {
			red = true;
			return "<html><font color=red>�� ����</html>";
		}
		else if(m == 8 && d == 15) {
			red = true;
			return "<html><font color=red>�߼�</html>";
		}
		else if(m == 8 && d == 14) {
			red = true;
			return "<html><font color=red>�߼�����</html>";
		}
		else if(m == 8 && d == 16) {
			red = true;
			return "<html><font color=red>�߼�����</html>";
		}
		
		else {
			return null;
		}
	}
}
