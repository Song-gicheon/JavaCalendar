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
			return "<html><font color=red>신정</html>";
		}
		else if(m == 3 && d == 1) {
			red = true;
			return "<html><font color=red>삼일절</html>";
		}
		else if(m == 5 && d == 5) {
			red = true;
			return "<html><font color=red>어린이날</html>";
		}
		else if(m == 6 && d == 6) {
			red = true;
			return "<html><font color=red>현충일</html>";
		}
		else if(m == 8 && d == 15) {
			red = true;
			return "<html><font color=red>광복절</html>";
		}
		else if(m == 10 && d == 3) {
			red = true;
			return "<html><font color=red>개천절</html>";
		}
		else if(m == 10 && d == 9) {
			red = true;
			return "<html><font color=red>한글날</html>";
		}
		else if(m == 12 && d == 25) {
			red = true;
			return "<html><font color=red>성탄절</html>";
		}
		else if(m == 7 && d == 17) {
			return "<html><font color=black>제헌절</html>";
		}
		
		// 절기
		else if(m == 2 && d == 4) {
			return "<html><font color=gray>입춘</html>";
		}
		else if(m == 2 && d == 19) {
			return "<html><font color=gray>우수</html>";
		}
		else if(m == 3 && d == 6) {
			return "<html><font color=gray>경칩</html>";
		}
		else if(m == 3 && d == 21) {
			return "<html><font color=gray>춘분</html>";
		}
		else if(m == 4 && d == 5) {
			return "<html><font color=gray>청명</html>";
		}
		else if(m == 4 && d == 20) {
			return "<html><font color=gray>곡우</html>";
		}
		else if(m == 5 && d == 6) {
			return "<html><font color=gray>입하</html>";
		}
		else if(m == 5 && d == 21) {
			return "<html><font color=gray>소만</html>";
		}
		else if(m == 6 && d == 5) {
			return "<html><font color=gray>망종</html>";
		}
		else if(m == 6 && d == 22) {
			return "<html><font color=gray>하지</html>";
		}
		else if(m == 7 && d == 7) {
			return "<html><font color=gray>소서</html>";
		}
		else if(m == 7 && d == 23) {
			return "<html><font color=gray>대서</html>";
		}
		else if(m == 8 && d == 8) {
			return "<html><font color=gray>입추</html>";
		}
		else if(m == 8 && d == 23) {
			return "<html><font color=gray>처서</html>";
		}
		else if(m == 9 && d == 8) {
			return "<html><font color=gray>백로</html>";
		}
		else if(m == 9 && d == 23) {
			return "<html><font color=gray>추분</html>";
		}
		else if(m == 10 && d == 8) {
			return "<html><font color=gray>한로</html>";
		}
		else if(m == 10 && d == 24) {
			return "<html><font color=gray>상강</html>";
		}
		else if(m == 11 && d == 8) {
			return "<html><font color=gray>입동</html>";
		}
		else if(m == 11 && d == 22) {
			return "<html><font color=gray>소설</html>";
		}
		else if(m == 12 && d == 7) {
			return "<html><font color=gray>대설</html>";
		}
		else if(m == 12 && d == 22) {
			return "<html><font color=gray>동지</html>";
		}
		else if(m == 1 && d == 6) {
			return "<html><font color=gray>소한</html>";
		}
		else if(m == 1 && d == 20) {
			return "<html><font color=gray>대한</html>";
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
			return "<html><font color=red>설날</html>";
		}
		else if(m == 12 && d == lastDate_inMonth) {
			red = true;
			return "<html><font color=red>설 연휴</html>";
		}
		else if(m == 1 && d == 2) {
			red = true;
			return "<html><font color=red>설 연휴</html>";
		}
		else if(m == 8 && d == 15) {
			red = true;
			return "<html><font color=red>추석</html>";
		}
		else if(m == 8 && d == 14) {
			red = true;
			return "<html><font color=red>추석연휴</html>";
		}
		else if(m == 8 && d == 16) {
			red = true;
			return "<html><font color=red>추석연휴</html>";
		}
		
		else {
			return null;
		}
	}
}
