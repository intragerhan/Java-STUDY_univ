package iducs.management.pim2021.view;

import iducs.management.pim2021.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberView {
    public void printHeader() {
        System.out.printf("%-17s", "이메일" + "\t");
        System.out.printf("%-5s","이름" + "\t");
        System.out.printf("%-15s", "연락처" + "\t");
        System.out.println("주소" + "\n");
    }

    public void printList(List<Member> memberList) {
        printHeader();
        for(Member m : memberList) {
            printOne(m);
        }
    }

    public ArrayList<String> printOne(Member m) {
        System.out.print(m.getEmail() + "\t");
        System.out.print(m.getName() + "\t");
        System.out.print(m.getPhone() + "\t");
        System.out.print(m.getAddress() + "\n");
        return null;
    }

    public void printSuccess(String msg) {
        System.out.println(msg + "작업 성공");
    }
    public void printSuccess(Member m, String msg) {
        printSuccess(msg);
        printOne(m);
    }
    public void printFail(String msg) {
        System.out.println(msg + "작업 실패");
    }
}
