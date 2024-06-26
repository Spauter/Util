package com.bloducspauter.newexcel.read;

import com.bloducspauter.origin.exceptions.WrongPasswordException;
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
        InputStream in = new FileInputStream(path);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
        // 创建一个 EncryptionInfo 对象，用于获取加密信息
        EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
        // 创建一个 Decryption 对象，用于解密文件
        Decryptor decryptor = Decryptor.getInstance(encInfo);
        boolean isPasswordCorrect = verifyPassword(password, decryptor);
        // 关闭文件系统和输入流
        try {
            poifsFileSystem.close();
            in.close();
        } catch (IOException e) {
            System.out.println("Close file system and input stream failed");
        }
        return isPasswordCorrect;
    }

    /**
     * 验证密码是否正确
     *
     * @param path 文件路径
     * @throws IOException              文件读取异常
     * @throws GeneralSecurityException 加密异常
     * @throws WrongPasswordException   密码错误
     */
    public static InputStream decrypt(String path, String password) throws IOException, GeneralSecurityException {
        InputStream in = new FileInputStream(path);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
        // 创建一个 EncryptionInfo 对象，用于获取加密信息
        EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
        // 创建一个 Decryption 对象，用于解密文件
        Decryptor decryptor = Decryptor.getInstance(encInfo);
        // 使用 Decryption 对象，验证密码是否正确
        boolean isPasswordCorrect = verifyPassword(password, decryptor);
        if (!isPasswordCorrect) {
            // 关闭文件系统和输入流
            poifsFileSystem.close();
            in.close();
            if (password == null) {
                // 如果密码为空，则抛出 WrongPasswordException 异常，提示用户输入密码
                throw new WrongPasswordException("The document is encrypted, please enter the password");
            }
            // 如果密码不为空，但不正确，则抛出 WrongPasswordException 异常，提示用户检查密码
            throw new WrongPasswordException("The password is wrong,please check the password");
        }
        return decryptor.getDataStream(poifsFileSystem);
    }
}
