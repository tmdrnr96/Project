package vo;

import org.springframework.web.multipart.MultipartFile;

public class NoticeVO {
	private int idx;
	private int readhit;
	private int del_info;
	
	private String subject;
	private String content;
	private String ip;
	private String regdate;
	private String filename; // 업로드가 완료 된 파일명
	private MultipartFile photo; // 이미지를 저장하기 위한 객체
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getReadhit() {
		return readhit;
	}
	public void setReadhit(int readhit) {
		this.readhit = readhit;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public int getDel_info() {
		return del_info;
	}
	public void setDel_info(int del_info) {
		this.del_info = del_info;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public MultipartFile getPhoto() {
		return photo;
	}
	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}
	
	
	

}
