

import com.bloducspauter.excelutil.ewxce.RawUseWrapperExcelUtil;
import com.bloducspauter.excelutil.origin.BsExcelUtil;
import com.bloducspauter.excelutil.origin.ExcelUtil;
import com.bloducspauter.excelutil.ewxce.WrapperExcelUtil;
import com.bloducspauter.excelutil.ewxce.wrapper.ReadWrapper;
import com.bloducspauter.text.TextService;
import com.bloducspauter.text.TextUtil;
import org.junit.Test;
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
        ExcelUtil excelService = new ExcelUtil();
        excelService.readSheetAt(5);
        List<Map<String, Object>> list = excelService.readToList("C:\\users\\32306\\desktop\\2.xlsm");
        excelService.output(list, "C:\\users\\32306\\desktop\\1.xlsx");

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
    public void test5() throws Exception {
        ReadWrapper readWrapper = ReadWrapper.builder()
                .path("D:\\upload\\Teacher.xlsx")
                .password("12356")
                .endRow(10)
                .sheetIndex(0)
                .build();
        WrapperExcelUtil<Teacher> wrapperExcelUtil = new WrapperExcelUtil<>(Teacher.class, readWrapper);
        try {
            long startTime = System.currentTimeMillis();
            //Insert your code
            List<Teacher> list = wrapperExcelUtil.readData();
            long endTime = System.currentTimeMillis();
            System.out.println("----cost time: " + (endTime - startTime) + "ms");
            long startTime1 = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Insert your code
        List<Teacher> list1 = wrapperExcelUtil.readAll();

        List<Map<String, Object>> readMap = wrapperExcelUtil.readFiledKeyMap();
        readMap.forEach(id -> {
            id.forEach((f, v) -> {
                System.out.println(f + "=" + v);
            });
        });
    }

    @Test
    public void test6() throws Exception {
        ReadWrapper readWrapper = ReadWrapper.builder()
                .path("D:\\documents\\计算机科学与工程学院-不合格学-生学时统计 (计科2102).xlsx")
                .sheetIndex(1)
                .startRow(2)
                .titleLine(1)
                .build();
        RawUseWrapperExcelUtil  wrapperExcelUtil = new RawUseWrapperExcelUtil(readWrapper);
        List<Map<String,Object>>list=wrapperExcelUtil.readToSimpleMap();
        System.out.println(list.size());
        list.forEach(l->{
            l.forEach((k,v)->{
                System.out.println(k+"="+v);
            });
        });
    }
}
