package iducs.management.pim2021.service;

import iducs.management.pim2021.controller.MemberController;
import iducs.management.pim2021.model.Member;

import iducs.management.pim2021.repository.*;   // intellij 에서 import 문제가 발생하여 *로 처리
import iducs.management.pim2021.view.MemberView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public abstract class MemberServiceImpl<T> implements MemberService<T> {

    MemberView memberView = new MemberView();
    MemberRepository<T> memberRepository = null;
    private String memberDB = null;
    Object temporary = null;


    public MemberServiceImpl(String memberDB) {
        memberRepository = new MemberRepositoryImpl<T>();
        this.memberDB = memberDB;
    }

    @Override
    public T login(String email, String pw) {
        boolean isLogin = false;

        T member = (T) new Member();
        ((Member) member).setEmail(email);
        ((Member) member).setPw(pw);
        T retMember = ((T) memberRepository.readByEmail(member));
        if (retMember != null) {
            memberView.printSuccess("로그인 성공 - ");
            MemberController.session.put("member", (Member) retMember);
        } else {
            MemberController.session.put("member", (Member) retMember);
            memberView.printFail("로그인 실패 - ");
        }
        return null;
    }

    @Override
    public void logout() {
        T member = (T) MemberController.session.get("member");
        if (member != null) {
            System.out.println(((Member) member).getName() + "님이 로그아웃 하셨습니다.");
            MemberController.session.remove("member");  // session invalidate
        }
    }

    @Override
    public int postMember(T member) {
        // 담을 객체 정보 가져오기
        List<T> memberList = memberRepository.getMemberList();
        // 멤버 id 값 가져오기 // 멤버 id 값 1증가
        if(memberList.size() > 0) {
            ((Member) member).setId(
                    ((Member) memberList.get(memberList.size() - 1)).getId() + 1);
        } else {
            ((Member) member).setId(1);
        }


        if (memberRepository.create(member) > 0) {
            memberView.printSuccess("회원 등록");
            return 1;
        } else {
            memberView.printFail("회원 등록 실패");
            return 0;
        }
    }

    @Override
    public T getMember(T member) {
        // readById()는 T 유형의 객체를 매개변수로
        return memberRepository.readById(member);
    }

    @Override
    public List<T> getMemberList() {
        return memberRepository.readList();
    }

    @Override
    public int putMember(T member) {
        return memberRepository.update(member);
    }

    @Override
    public int deleteMember(T member) {
        return memberRepository.delete(member);
    }

    @Override
    public List<T> findMemberByPhone(T member) {
        return memberRepository.readListByPhone(member);
    }

    @Override
    public List<T> sortByName(String order) {
        return memberRepository.readListByName(order);
    }

    @Override
    public List<T> paginateByPerPage(int pageNo, int perPage) {
        return memberRepository.readListByPerPage(pageNo, perPage);
    }

    public void readFile() {    // 파일을 읽어서 member list 생성
        File file = new File(memberDB); // 파일 객체를 생성
        if (file.canRead()) {    // 존재해서 읽을 수 있는 상태
            try {
                MemberFileReader<Member> mfr = new MemberFileReader<Member>(file);
                memberRepository.setMemberList((List<T>) mfr.readMember());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {    // 존재하지 않아서 읽을 수 없는 상태
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile() { // member list를 파일로 저장
        File file = new File(memberDB);
        try {
            MemberFileWriter<Member> mfw = new MemberFileWriter<Member>(file);
            mfw.saveMember((List<Member>) memberRepository.readList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyUpdate() {
        saveFile();
        readFile();
    }

    @Override
    public boolean validateEmail(String email) {
        // 새 멤버 객체에 이메일만 집어넣음
        Member member = new Member();
        member.setEmail(email);
        try {
            if(memberRepository.readByEmail((T) member)== null) {
                return false;
            }
            else {
                throw new Exception("중복된 이메일입니다.");
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }
}
