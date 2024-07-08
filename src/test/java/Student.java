import com.bloducspauter.excelutil.annotation.FiledProperty;
import lombok.Data;

@Data
public class Student {
    @FiledProperty("学号")
    private String id;
    @FiledProperty("姓名")
    private String name;
    @FiledProperty("所在班级")
    private String clazz;
    @FiledProperty("性别")
    private String sex;
    @FiledProperty("联系电话")
    private String tel;
    @FiledProperty("入学年份")
    private String joinTime;
}
