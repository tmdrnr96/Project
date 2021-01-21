package common;

public class Common {

	
	public static final String VIEW_PATH = "/WEB-INF/views/bank/";
	
	public static class Board{
		//한 페이지에 보여줄 게시글의 수
		public final static int BLOCKLIST = 4;
		
		//현재 페이지에 보여줄 최대 페이지 메뉴 수
		//< 1 2 3 4 5 >
		public final static int BLOCKPAGE = 3;
	}

	public static class Notice{
		//한 페이지에 보여줄 게시글의 수
		public final static int BLOCKLIST = 4;
	}
	
	
	public static class BoardView{
		//한 페이지에 보여줄 게시글의 수
		public final static int BLOCKLIST = 5;
		
		//현재 페이지에 보여줄 최대 페이지 메뉴 수
		//< 1 2 3 4 5 >
		public final static int BLOCKPAGE = 3;
	}
	
}
