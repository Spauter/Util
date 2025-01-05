import com.bloducspauter.excelutil.annotation.FiledProperty;
import com.bloducspauter.excelutil.annotation.TableProperty;
import lombok.Data;

@Data
@TableProperty(ignoreOtherCells = true)
public class Student {
    @FiledProperty(value = "学号")
    private String id;
    @FiledProperty("姓名")
    private String name;
    @FiledProperty("所在班级")
    private String clazz;
    @FiledProperty("性别")
    private String sex;
    @FiledProperty("联系电话")
    private String tel;
}
