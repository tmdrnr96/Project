package common;

public class MyCommon {
	//main폴더
	public static class MainPage{
		public static final String VIEW_PATH = "/WEB-INF/views/main/";
	}
	
	//
	public static class Account{
		public static final String VIEW_PATH = "/WEB-INF/views/account/";
	}
	
	//입금하기 폴더
	public static class Deposit{
		public static final String VIEW_PATH = "/WEB-INF/views/deposit/";
	}
	
	//출금하기 폴더
	public static class Withdrawal{
		public static final String VIEW_PATH = "/WEB-INF/views/withdrawal/";
	}
	
	//이체하기 폴더
	public static class Transfer{
		public static final String VIEW_PATH = "/WEB-INF/views/transfer/";
	}
	
	//상세보기
	public static class Detail{
		public static final String VIEW_PATH = "/WEB-INF/views/bank/";
	}
	
	//Q&A 게시판
	public static class Board{
				
		public static final String VIEW_PATH = "/WEB-INF/views/Q&A/";
				
		public final static int BLOCKLIST = 5;	// 한 페이지에 보여 줄 게시글의 수
				
		//< 1 2 3 4 5> -> < 6 7 >
		public final static int BLOCKPAGE = 3;	//현재 페이지에 보여 줄 최대 페이지 메뉴 수
	}
	
	//공지사항 게시판
	public static class Notice{
					
		public static final String VIEW_PATH = "/WEB-INF/views/notice/";
					
		public final static int BLOCKLIST = 5;	// 한 페이지에 보여 줄 게시글의 수
					
		public final static int BLOCKPAGE = 3;	//현재 페이지에 보여 줄 최대 페이지 메뉴 수
	}
	
}
