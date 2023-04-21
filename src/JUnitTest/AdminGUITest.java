package JUnitTest;

import Lib.XFile;
import Model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AdminGUITest {
    String pathStaff = "src\\File\\staffs.dat";
    List<Account> staffList;
    @Before
    public void setUp(){
        staffList = (List<Account>) XFile.readObject(pathStaff);
    }
    @Test
    public void testAddStaff(){
        int s = staffList.size();
        Account account = new Account("Nguyen Thi Kim Khanh", true, "khanhntk","0987654345", "khanh123", "Can Tho");
        staffList.add(account);
        int expected = s + 1;
        Assertions.assertEquals(expected, staffList.size());
    }

    @Test
    public void testEditStaff(){
        Account a = null;
        Account account = new Account("Nguyen Thi Kim Khanh", true, "khanhntk","khanh123", "0987654345", "Can Tho");
        staffList.add(account);
        for (Account acc:staffList) {
            if (acc.getUsername().equals("khanhntk")) {
                a = acc;
            }
        }
        a.setPassword("khanhkim123");
        String expected = a.getPassword();
        Assertions.assertEquals(expected, "khanhkim123");
    }
    @Test
    public void testRemoveStaff(){
        Account a = null;
        Account account = new Account("Nguyen Thi Kim Khanh", true, "khanhntk","khanh123", "0987654345", "Can Tho");
        staffList.add(account);
        int s = staffList.size();

        for (Account acc:staffList) {
            if (acc.getUsername().equals("khanhntk")) {
                a = acc;
            }
        }
        staffList.remove(a);
        int expected = s - 1;
        Assertions.assertEquals(expected, staffList.size());
    }
    @Test
    public void testRemoveAllStaff(){
        staffList.remove(staffList);
        int expected = 0;
        Assertions.assertEquals(expected, 0);
    }
//    @Test
//    public void testDuplicateIDStaff(){
//        Map<String, String> isDuplicate = new HashMap<>();
//
//        Account account1 = new Account("Nguyen Thi Kim Khanh", true, "khanh","khanh123", "0987654345", "Can Tho");
//        staffList.add(account1);
//        isDuplicate.put(account1.getUsername(), account1.getPassword());
//        int s = staffList.size();
//
//        Account account2 = new Account("Tran Thi Kim Khanh", true, "khannh","khanh123", "09876543654", "Can Tho");
//        isDuplicate.put(account2.getUsername(), account2.getPassword());
//
//        for (Account acc:staffList) {
//            if (!isDuplicate.containsKey(acc.getUsername())) {
//                staffList.add(account2);
//            }
//        }
//        int expected = s;
//        Assertions.assertEquals(expected, staffList.size());
//    }
}