package nhatminh1.springboot_activeMQ.data;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {
	private long mssv;
	private String hoten;
	private Date ngaysinh;

	public Person(long mssv) {
		super();
		this.mssv = mssv;
	}
	

	public Person() {
		super();
	}


	public long getMssv() {
		return mssv;
	}

	public void setMssv(long mssv) {
		this.mssv = mssv;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public Date getNgaysinh() {
		return ngaysinh;
	}

	public void setNgaysinh(Date ngaysinh) {
		this.ngaysinh = ngaysinh;
	}
	

	@Override
	public String toString() {
		return "Thông tin [mssv =" + mssv + ",hoten=" + hoten + ",ngaysinh=" + ngaysinh + "]";
	}
}
