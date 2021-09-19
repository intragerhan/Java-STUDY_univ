package iducs.management.pim2021.service;

import java.util.List;

public interface MemberService<T> {
    T login(String email, String pw);   // 로그인 처리
    void logout();  // 로그아웃 처리
    int postMember(T member); // 회원 등록, http에서 post 메서드로 처리 -> repository create
    T getMember(T member);  // 회원 정보 조회, get 메서드로 처리 -> repository read one
    List<T> getMemberList();    // 회원 목록 조회, get 메서드로 처리 ->repository read list
    int putMember(T member);    // 회원정보 수정, put 메서드로 처리 -> repository update
    int deleteMember(T member); // 회원 탈퇴(삭제), delete 메서드로 처리 0> repository delete

    List<T> findMemberByPhone(T member);    // 전화번호뒷 4자리로 검색, 여러개가 검색될 수 있음
    List<T> sortByName(String order);   // order가 desc면 내림차순, asc면 오름차순
    List<T> paginateByPerPage(int pageNo, int perPage);

    void readFile();    // 파일로부터 읽어서 멤버 목록 객체 생성
    void saveFile();    // 멤버 목록 객체를 파일에 저장
    void applyUpdate(); // saveFile() -> readFile() 순으로 호출하여 처리함. 등록, 수정 시 발생

    boolean validateEmail(String email);    // 이메일 중복 검사
}
