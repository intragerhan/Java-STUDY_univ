package iducs.management.pim2021;

import iducs.management.pim2021.controller.MemberController;

public class Main {

    public static void main(String[] args) {
        MemberController memberController = new MemberController();
        memberController.dispatch();
    }
}
