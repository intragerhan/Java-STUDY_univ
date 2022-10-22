package iducs.management.pim2021.service;

import iducs.management.pim2021.model.Member;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MemberFileWriter<T> {
    BufferedWriter bw = null;
    FileWriter fw = null;
    public MemberFileWriter(File f) throws IOException {
        fw = new FileWriter(f);
        // bw = new BufferedWriter(fw); // BufferedWriter를 사용하여
    }
    public void saveMember(List<T> memberList) throws IOException {
        for(T member1 : memberList) {
            try {
                Member m = (Member) member1;
                fw.write(m.getId() + " ");
                fw.write(m.getEmail() + " ");
                fw.write(m.getPw() + " ");
                fw.write(m.getName() + " ");
                fw.write(m.getPhone() + " ");
                fw.write(m.getAddress() + "\n");
//                 fw.flush();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        fw.close();
    }
}
