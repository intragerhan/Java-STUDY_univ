package iducs.management.pim2021.repository;

import iducs.management.pim2021.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryImpl<T> implements MemberRepository<T> {
    public static long memberId = 1;
    public List<T> memberList = null;

    public MemberRepositoryImpl() {
        memberList = new ArrayList<T>();
    }

    @Override
    public int create(T member) {
        int ret = 0;
        try {
            memberList.add((T) member);
            ret = 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    @Override
    public T readById(T member) {
        T retMember = null;
        for (T m : memberList) {
            if (((Member) m).getId() == ((Member) member).getId()) {
                retMember = m;
                break;
            }
        }
        return retMember;
    }

    @Override
    public T readByEmail(T member) {
        T retMember = null;
        for (T m : memberList) {
            if (((Member) member).getEmail().equals(((Member) m).getEmail())) {
                retMember = m;
                break;
            }
        }
        return retMember;
    }

    @Override
    public T readByPasswd(T member) {
        T retMember = null;
        for (T m : memberList) {
            if (((Member) member).getPw().equals(((Member) m).getPw())) {
                retMember = m;
                break;
            }
        }
        return retMember;
    }

    @Override
    public List<T> readList() {
        return getMemberList();
    }

    @Override
    public int update(T member) {
        int ret = 0;
        for (T m : memberList) {
            if (((Member) m).getId() == ((Member) member).getId()) {
                memberList.set(ret, member);
            }
            ret++;
        }
        return ret;
    }

    @Override
    public int delete(T member) {
        int ret = 0;
        for (T m : memberList) {
            if (((Member) m).getId() == ((Member) member).getId()) {
                memberList.remove(m);
                ret++;
                break;  // 유일키이므로 1개만 삭제하려면 break 필요함
            }
        }
        return ret;
    }

    @Override
    public List<T> getMemberList() {
        return memberList;
    }

    @Override
    public void setMemberList(List<T> memberList) {
        this.memberList = memberList;
    }

    @Override
    public List<T> readListByPhone(T member) {
        List<T> phoneCheck = new ArrayList<T>();
        for (T mem : memberList) {
            if (((Member) mem).getPhone().contains(((Member) member).getPhone()))
                phoneCheck.add(mem);
        }
        return phoneCheck;
    }

    @Override
    public List<T> readListByName(String order) {
        List<T> memberDTO = new ArrayList<T>(memberList);

        if (order.equals("desc")) {
            for (int i = 0; i < memberDTO.size() - 1; i++) {
                if (((Member) memberDTO.get(i)).getName().compareToIgnoreCase(((Member) memberDTO.get(i + 1)).getName()) < 0) {
                    Member temp = (Member) memberDTO.get(i);
                    memberDTO.set(i, memberDTO.get(i + 1));
                    memberDTO.set(i + 1, (T)temp);
                    i = -1;
                }
            }
        } else if (order.equals("asc")) {
            for(int i = 0; i < memberDTO.size() - 1; i++) {
                if(((Member) memberDTO.get(i)).getName().compareToIgnoreCase(((Member) memberDTO.get(i + 1)).getName()) > 0) {
                    Member temp = (Member) memberDTO.get(i);
                    memberDTO.set(i, memberDTO.get(i + 1));
                    memberDTO.set(i + 1, (T)temp);
                    i = -1;
                }
            }
        }
        return memberDTO;
    }

    @Override
    public List<T> readListByPerPage(int page, int perPage) {
        List<T> printPage = new ArrayList<T>();
        int start = page * perPage - perPage + 1;
        int end = start + perPage;
        for(int i = start; i < end; i++) {
            if(i <= memberList.size()) {
                printPage.add(memberList.get(i - 1));
            }
        }
        return printPage;
    }
}
