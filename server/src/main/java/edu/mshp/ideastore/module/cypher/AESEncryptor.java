package edu.mshp.ideastore.module.cypher;

import edu.mshp.ideastore.module.ByteConverter;
import edu.mshp.ideastore.module.PropertyReader;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES-шифратор
 * @author rugrisser
 */
public class AESEncryptor implements IEncryptor {
    /** Название алгоритма */
    private static final String NAME = "AES";

    /** Ключ */
    private SecretKeySpec key;
    /** Объект шифратора */
    private Cipher cipher;

    /**
     * Конструктор без указания типа строки (устанавливается "default")
     */
    public AESEncryptor() {
        getKey("default");
        initCipher();
    }

    /**
     * Конструктор с указанием типа строки
     * @param type - тип строки
     */
    public AESEncryptor(String type) {
        getKey(type);
        initCipher();
    }

    /**
     * Инициализирует объект шифратора
     */
    private void initCipher() {
        try {
            cipher = Cipher.getInstance(NAME);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение ключа из файла конфигурации
     * @param type - тип строки
     */
    private void getKey(String type) {
        try {
            PropertyReader reader = new PropertyReader("cipher");
            String key = reader.read("key." + type);

            this.key = new SecretKeySpec(key.getBytes(), NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Шифратор
     * @param string Строка
     * @return Зашифрованная строка
     * @throws InvalidKeyException Неправильный ключ
     * @throws BadPaddingException Неправильный формат
     * @throws IllegalBlockSizeException Некорректный формат
     */
    @Override
    public String encrypt(String string) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(string.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Дешифратор
     * @param string Строка
     * @return Расшифрованная строка
     * @throws InvalidKeyException Неправильный ключ
     * @throws BadPaddingException Неправильный формат
     * @throws IllegalBlockSizeException Некорректный формат
     */
    @Override
    public String decrypt(String string) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] source = Base64.getDecoder().decode(string.getBytes());
        byte[] bytes = cipher.doFinal(source);
        return ByteConverter.toString(bytes);
    }
}
