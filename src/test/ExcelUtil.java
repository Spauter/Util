

import java.io.*;
import java.security.GeneralSecurityException;
 
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * @author 32306
 */
public class ExcelUtil {
    public static void main(String[] args) throws Exception {
//        encryptExcl("D:\\upload\\Teacher.xlsx", "123456");//加密
//        encryptExcl("G:\\学生2.xlsx", "test");//加密
//        decryptExcel_xlsx("G:\\学生2.xlsx", "test");//解密xlsx
        decryptExcel_xlsx("D:\\upload\\Teacher.xlsx", "12346");//解密xls
 
 
    }
 
    /**
     *解密xls
     * @param FILE 文件名
     * @param password 密码
     */
    public static void decryptExcel_xls(String FILE, String password){
        try{
            POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream(FILE));
            //解密，这个密码不是指保护工作表和工作博密码，而是打开文件密码
            Biff8EncryptionKey.setCurrentUserPassword(password);
            HSSFWorkbook wb = new HSSFWorkbook(pfs);
            //读取测试
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = sheet.getRow(0);
            HSSFCell cell = row.getCell(0);
            System.out.println(cell.getStringCellValue());
            wb.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
        }
    }
    /**
     * 解密xlsx
     * @param FILE 文件名
     * @param password 密码
     * @throws Exception
     */
    public static void decryptExcel_xlsx(String FILE, String password) throws Exception{
        Workbook wb = null;
        try (FileInputStream in = new FileInputStream(FILE)) {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
            EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
            Decryptor decryptor = Decryptor.getInstance(encInfo);
            decryptor.verifyPassword(password);
            wb = new XSSFWorkbook(decryptor.getDataStream(poifsFileSystem));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("=================================");
        System.out.println("Number of Sheets:" + wb.getNumberOfSheets());
        System.out.println("Sheet1's name:" + wb.getSheetName(0));
        System.out.println();
    }
 
    /**
     * 加密Excl
     * @param FILE
     * @param password
     * @throws IOException
     * @throws InvalidFormatException
     * @throws GeneralSecurityException
     */
    public static void encryptExcl(String FILE, String password) throws IOException, InvalidFormatException, GeneralSecurityException {
        if (FILE.toLowerCase().endsWith("xlsx")) {
            System.out.println("=====加密 xlsx===="+FILE);
            // 设置密 码 保 护 ·
            POIFSFileSystem fs = new POIFSFileSystem();
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
            Encryptor enc = info.getEncryptor();
            //设置密码
            enc.confirmPassword(password);
            //加密文件
            OPCPackage opc = OPCPackage.open(new File(FILE), PackageAccess.READ_WRITE);
            OutputStream os = enc.getDataStream(fs);
            opc.save(os);
            opc.close();
            // 这一步特别注意，导出之前一定要先关闭加密文件流，不然导出文件会损坏而无法打开
            os.close();
            //把加密后的文件写回到流
            FileOutputStream fos = new FileOutputStream(FILE);
            fs.writeFilesystem(fos);
            fos.close();
        } else {
            System.out.println("=====加密 xls===="+FILE);
            POIFSFileSystem poif = new POIFSFileSystem(new FileInputStream(FILE));
            HSSFWorkbook wb = new HSSFWorkbook(poif);
            // 设置密 码 保 护 ·
            Biff8EncryptionKey.setCurrentUserPassword(password);
            wb.writeProtectWorkbook(Biff8EncryptionKey.getCurrentUserPassword(), "管理员");
            wb.unwriteProtectWorkbook();
            // 写入excel文件
            FileOutputStream fileOut = new FileOutputStream(FILE);
            wb.write(fileOut);
            fileOut.close();
        }
 
        System.out.println("over");
 
    }
 
 
}