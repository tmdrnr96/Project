package vo;

public class DetailVO {

	private String account;//계정
	private String content;//항목 이름
	private int deposit;//입금
	private int withdraw;//출금
	private int balance;//잔액
	private String usedate;//날짜
	private int det;//항목 번호
	
		
	public int getDet() {
		return det;
	}
	public void setDet(int det) {
		this.det = det;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	public int getWithdraw() {
		return withdraw;
	}
	public void setWithdraw(int withdraw) {
		this.withdraw = withdraw;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getUsedate() {
		return usedate;
	}
	public void setUsedate(String usedate) {
		this.usedate = usedate;
	}
	
	
}
