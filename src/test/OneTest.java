

import com.bloducspauter.excel.BsExcelUtil;
import com.bloducspauter.excel.ExcelUtil;
import com.bloducspauter.excel.WrapperExcelUtil;
import com.bloducspauter.excel.service.ExcelService;
import com.bloducspauter.excel.read.WorkBookReader;
import com.bloducspauter.origin.wrapper.ReadWrapper;
import com.bloducspauter.text.TextService;
import com.bloducspauter.text.TextUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * @author 32306
 */
public class OneTest {
    @Test
    public void test() throws Exception {
        BsExcelUtil<Teacher> excelUtil = new BsExcelUtil<>(Teacher.class);
        List<Teacher> list = excelUtil.readAll("D:\\upload\\Teacher.xlsx");
        list.forEach(System.out::println);
        Object[][] objects = excelUtil.readToArray("D:\\upload\\Teacher.xlsx");
        System.out.println(objects[0].length + "," + objects.length);
        List<Map<String, Object>> mapList = excelUtil.readToList("D:\\upload\\Teacher.xlsx");
        mapList.forEach(id -> {
            id.forEach((f, v) -> {
                System.out.printf(f + ":" + v + ",");
            });
            System.out.println();
        });
    }

    @Test
    public void test2() throws Exception {
        ExcelService excelService = new ExcelUtil();
        long startTime = System.currentTimeMillis();
        //Insert your code
        List<Map<String, Object>> list = excelService.readToList("C:\\Users\\32306\\Desktop\\Microsoft Edge 密码.csv");
        excelService.output(list, "C:\\users\\32306\\desktop\\1.csv");

    }

    @Test
    public void test3() throws Exception {
        TextService textService = new TextUtil();
        List<Map<String, Object>> list = textService.readToList("D:\\spauter\\JAVA\\Interships\\intership\\input.dat", "\\t");
        list.forEach(id -> {
            id.forEach((f, v) -> {
                System.out.printf(f + ":" + v + ",");
            });
            System.out.println();
        });
        textService.output(list, "C:\\users\\32306\\desktop");
    }

    @Test
    public void test4() {
        Test1 test1 = new Test1();
        boolean a = test1.isMirrorString("qwertrewq");
        System.out.println(a);
        Test2 test2 = new Test2();
        System.out.println(test2.function(10));
    }

    @Test
    public void test5() throws Exception {
        ReadWrapper readWrapper = ReadWrapper.builder()
                .path("D:\\upload\\Teacher.xlsx")
                .password("123456")
                .endRow(20)
                .startRow(3)
                .build();
       WrapperExcelUtil<Teacher> wrapperExcelUtil = new WrapperExcelUtil<>(Teacher.class,readWrapper);
        List<Teacher> list = wrapperExcelUtil.readAll();
        System.out.println(list.size());
        list.forEach(System.out::println);
    }
}


class Test1 {
    public boolean isMirrorString(String s) {
        return isMirrorString(s, 0, s.length() - 1, s.length());
    }


    private boolean isMirrorString(String s, int start, int end, int length) {
        if (length == 0 || length == 1) {
            return true;
        }
        if (s.indexOf(start) == s.indexOf(end)) {
            return isMirrorString(s, start + 1, end - 1, length - 2);
        } else {
            return false;
        }
    }
}

class Test2 {
    int function(int n) {
        if (n < 0) {
            return -1;
        }
        if (n == 0 || n == 1) {
            return 2;
        } else {
            return 2 * n + function(n - 1);
        }
    }
}
