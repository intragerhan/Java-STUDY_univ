package iducs.management.pim2021.model;

public class Member {
    public int id;  // 고유 번호, 유일키
    private String email;   // 길이 20, 이메일, 유일키
    private String pw;  // 길이 15, 암호
    private String name;    // 길이 15, 이름
    private String phone;   // 길이 20, 전화번호
    private String address; // 길이 30, 주소

    public int count = 1000;

    public int getId() {
        return id;
    }
    public void setId(int count) {
        this.id =count;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
