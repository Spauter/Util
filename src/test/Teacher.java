

import java.util.Date;

import com.bloducspauter.excelutil.annotation.FiledProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 *
 */
@Setter
@Getter
public class Teacher {
	@FiledProperty(value = "教职工号",index = 0)
    private int id;
    @FiledProperty(value = "姓名",index = 1)
    private String name;
    @FiledProperty(value = "职位")
    private String processor;
    @FiledProperty(value = "入职年份",index = 2)
    private Date time;
    @FiledProperty(value = "联系电话",index = 4)
    private String tel;
    @FiledProperty(value = "性别",index = 1)
    private String sex;
    private String address;
    @FiledProperty(isExists = false)
    private double salary;

    @Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", processor=" + processor + ", time=" + time + ", tel=" + tel
				+ ", sex=" + sex + ", address=" + address + ", salary=" + salary + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getProcessor()=" + getProcessor() + ", getTime()=" + getTime()
				+ ", getTel()=" + getTel() + ", getSex()=" + getSex() + ", getAddress()=" + getAddress()
				+ ", getSalary()=" + getSalary() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
