package iducs.management.pim2021.service;

import iducs.management.pim2021.model.Member;
import iducs.management.pim2021.repository.MemberRepository;
import iducs.management.pim2021.view.MemberView;

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
        if(retMember != null) {
            memberView.printSuccess("로그인 성공 - ");
            MemberController.session.put("member",(Member) retMember);
        }else {
            MemberController.session.put("member", (Member) retMember);
            memberView.printFail("로그인 실패 - ");
        }
        return null;
    }


}
