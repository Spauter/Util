package com.bloducspauter.excelutil.ewxce.read;

import com.bloducspauter.excelutil.base.exceptions.WrongPasswordException;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * 此类提供用于解密使用密码加密的 Excel 文件的方法。它利用 Apache POI 库来处理加密的 Excel 文件和验证解密密码。
 *
 * @author Bloduc Spauter
 */
public class DecryptExcel {


    public DecryptExcel() {
        throw new UnsupportedOperationException("This class is not allowed to be instantiated");
    }

    private static boolean verifyPassword(String password, Decryptor decryptor) throws GeneralSecurityException {
        return decryptor.verifyPassword(password);
    }

    /**
     * 验证密码是否正确
     *
     * @param path     文件路径
     * @param password 密码
     * @return 密码是否正确
     */
    public static boolean verifyPassword(String path, String password) throws IOException, GeneralSecurityException {
        try (FileInputStream in = new FileInputStream(path);
             POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in)) {
            EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
            Decryptor decryptor = Decryptor.getInstance(encInfo);
            return verifyPassword(password, decryptor);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 解密 Excel 文件
     *
     * @param path 文件路径
     * @param password 密码
     * @return 解密后的输入流
     * @throws IOException 文件读取异常
     * @throws GeneralSecurityException 加密异常
     * @throws WrongPasswordException 密码错误
     */
    public static InputStream decrypt(String path, String password) throws IOException, GeneralSecurityException, WrongPasswordException {
        if (password == null || password.isEmpty()) {
            return new FileInputStream(path);
        }

        try (FileInputStream in = new FileInputStream(path);
             POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in)) {
            EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
            Decryptor decryptor = Decryptor.getInstance(encInfo);
            if (!verifyPassword(password, decryptor)) {
                throw new WrongPasswordException("The password is wrong, please check the password");
            }
            return decryptor.getDataStream(poifsFileSystem);
        }
    }
}
