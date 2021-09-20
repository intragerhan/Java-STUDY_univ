package iducs.management.pim2021.controller;

import iducs.management.pim2021.model.Member;
import iducs.management.pim2021.service.MemberService;
import iducs.management.pim2021.service.MemberServiceImpl;
import iducs.management.pim2021.view.MemberView;
import iducs.management.pim2021.view.TUIView;

import java.util.*;

public class MemberController {
    public static Map<String, Member> session = new HashMap<String, Member>();  // session 상태를 map 객체로 처리
    public static TUIView tui = new TUIView();
    final String MemberDB = "db2021.txt";

    MemberService<Member> memberService = null;
    MemberView memberView = null;

    public MemberController() {
        memberService = new MemberServiceImpl<Member>(MemberDB) { };
    }
    public void dispatch() {
        /**
         *  파일을 읽어서 회원 목록 객체 생성
         *  메뉴를 출력(로그인 여부와 관리자 여부에 따라 다름)
         *  메뉴를 선택 -> controller 객체가 요청을 받아
         *  -> 서비스 객체의 해당 메서드가 호출 -> repository 객체의 해당 메서드 호출하여 데이터 처리
         *  -> 반환된 결과를 서비스 객체의 메서드로 반환, 처리 -> controller 객체에게 반환되면 뷰에게 전달함
         *  -> 뷰 객체가 처리된 결과(화면)를 출력
         */
        memberView = new MemberView();
        Member sessionMember = null;    // session에 저장된 객체를 처리하는 곳에 사용
        Member member = null;   // 임시적으로 사용할 목적

        boolean isLogin = false;    // 로그인 여부
        boolean isRoot = false;     // 관리자 여부

        // 파일로부터 등록된 사용자 정보 읽기
        memberService.readFile();

        int menu = 0;
        do {
            sessionMember = session.get("member");
            if (sessionMember != null) {
                isLogin = true;
                if (sessionMember.getEmail().toString().equals("adminHan@induk.ac.kr"))
                    isRoot = true;
            } else {
                isLogin = false;
                isRoot = false;
            }

            tui.showMenu(isLogin, isRoot);  // 로그인 여부, 루트 여부에 따라 메뉴가 다름

            menu = tui.inputMenu();
            switch (menu) {
                case 0: // 종료 : 로그아웃, 파일로 저장
                    memberService.logout();
                    memberService.saveFile();
                    break;
                case 1: // 등록
                    member = new Member();

                    // email 중복 확인 필요
                    member.setEmail(MemberController.tui.inputEmail());
                    while (memberService.validateEmail(member.getEmail())) {
                        member.setEmail(MemberController.tui.inputEmail());
                    }

                    member.setPw(MemberController.tui.inputPw());
                    member.setName(MemberController.tui.inputName());
                    member.setPhone(MemberController.tui.inputPhone());
                    member.setAddress(MemberController.tui.inputAddress());

                    while (!isValid(member)) {   // 유효성 확인 : @, -, null 등
                    }
                    if (memberService.postMember(member) > 0) {
                        memberService.applyUpdate();
                    } else {
                        memberView.printFail("등록 실패 - ");
                    }
                    break;
                case 2: // 로그인
                    sessionMember = memberService.login(tui.inputEmail(), tui.inputPw());
                    break;
                case 3: // 조회
                    memberView.printOne((Member) memberService.getMember(sessionMember));
                    break;
                case 4: // 수정
                    member = new Member();
                    member.setId(sessionMember.getId());
                    member.setEmail(sessionMember.getEmail());

                    member.setPw(MemberController.tui.inputPw());
                    member.setName(MemberController.tui.inputName());
                    member.setPhone(MemberController.tui.inputPhone());
                    member.setAddress(MemberController.tui.inputAddress());

                    while (!isValid(member))
                        ;
                    if (memberService.putMember(member) > 0) {
                        memberView.printSuccess("업데이트 성공 - ");
                        memberView.printOne(member);
                    } else {
                        memberView.printFail("업데이트 실패 - ");
                    }
                    break;

                case 5: // 탈퇴
                    if (memberService.deleteMember(sessionMember) > 0) {
                        memberService.logout();
                        memberView.printSuccess("삭제 성공 - ");
                    } else {
                        memberView.printFail("삭제 실패 - ");
                    }
                    break;

                case 6: // 로그아웃
                    memberService.logout();
                    break;

                case 7: // 전체 목록
                    memberView.printList(memberService.getMemberList());
                    break;

                case 8: // 전화번호 검색
                    Member tempMember = new Member();
                    tempMember.setPhone(MemberController.tui.inputPhone());
                    memberView.printList(memberService.findMemberByPhone(tempMember));
                    break;

                case 9: // 이름 정렬
                    String arrayInput = arrayName();
                    memberView.printList(memberService.sortByName(arrayInput));
                    break;

                case 10: // 범위지정 회원목록
                    int inPage = numInputPage();
                    int inPerPage = numPerPage();

                    memberView.printList(memberService.paginateByPerPage(inPage, inPerPage));
                    break;
            }
        } while (menu != 0);
    }

    private boolean isValid(Member member) {
        boolean isValid = true;
        // 이메일 유효성 검사
        if (member.getEmail().length() == 0 && !member.getEmail().contains(new StringBuffer("@"))) {     // 이메일 패턴 확인
            isValid = false;
            member.setEmail(MemberController.tui.inputEmail());
        }

        int emailSize = member.getEmail().length();
        boolean atSymbol = false;
        for (int i = 0; i < emailSize; i++) {
            if (member.getEmail().charAt(i) == '@') {
                atSymbol = true;
            }
        }
        if (!atSymbol) {
            System.out.println("이메일 형식 오류");
            isValid = false;
            member.setEmail(MemberController.tui.inputEmail());
        }
        if (member.getPw().length() == 0)
            member.setPw(MemberController.tui.inputPw());
        if (member.getName().length() == 0)
            member.setName(MemberController.tui.inputName());

        // 전화번호 입력
        if (member.getPhone().length() == 0 && !member.getPhone().contains(new StringBuffer("-"))) {     // 전화번호 패턴 확인
            isValid = false;
            member.setPhone(MemberController.tui.inputPhone());
        }
        int phoneSize = member.getPhone().length();
        boolean hyphen = false;
        for (int i = 0; i < phoneSize; i++) {
            if (member.getPhone().charAt(i) == '-') {
                hyphen = true;
            }
        }
        if (!hyphen) {
            System.out.println("전화번호 형식 오류");
            isValid = false;
            member.setPhone(MemberController.tui.inputPhone());
        }
        return isValid;
    }

    private int numInputPage() {
        System.out.println("몇 페이지부터 출력하시겠어요?");
        Scanner inputPage = new Scanner(System.in);
        int page = 0;
        boolean flagNum = false;
        do {
            try {
                page = Integer.parseInt(inputPage.nextLine());
                if (page < 0) {
                    System.out.println("0 이상의 번호를 입력하세요");
                } else {
                    flagNum = true;
                }
            } catch (InputMismatchException | NumberFormatException ime) {
                System.out.println("숫자 형식을 입력해주세요");
            }
        } while (!flagNum);
        return page;
    }

    private int numPerPage() {
        System.out.println("1페이지당 몇 개만큼 출력하시겠어요?");
        Scanner inputPerPage = new Scanner(System.in);
        int perPage = 0;
        boolean flagNum = false;
        do {
            try {
                perPage = Integer.parseInt(inputPerPage.nextLine());
                if (perPage < 0) {
                    System.out.println("0이상의 번호를 입력하세요");
                } else {
                    flagNum = true;
                }
            } catch (InputMismatchException | NumberFormatException ime) {
                System.out.println("숫자 형식을 입력하세여");
            }
        } while (!flagNum);
        return perPage;
    }

    private String arrayName() {
        System.out.println("오름차순은 asc, 내림차순은 desc를 입력해주세요");
        Scanner nameAscDesc = new Scanner(System.in);
        String ascDesc = "";
        boolean flagNum = false;
        do {
            try {
                ascDesc = nameAscDesc.nextLine();
                if (ascDesc.equals("asc")) {
                    flagNum = true;
                } else if (ascDesc.equals("desc")) {
                    flagNum = true;
                } else {
                    System.out.println("asc 혹은 desc를 입력해주세요");
                }
            } catch (InputMismatchException ime) {
                System.out.println("asc 혹은 desc를 입력하세요");
            }
        } while (!flagNum);
        return ascDesc;
    }
}
