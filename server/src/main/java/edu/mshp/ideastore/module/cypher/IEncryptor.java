package edu.mshp.ideastore.module.cypher;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

/**
 * Интерфейс шифровальщика
 * @author rugrisser
 */
public interface IEncryptor {
    /**
     * Шифратор
     * @param string Строка
     * @return Зашифрованная строка
     * @throws InvalidKeyException Неправильный ключ
     * @throws BadPaddingException Неправильный формат
     * @throws IllegalBlockSizeException Некорректный формат
     */
    public String encrypt(String string) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException;

    /**
     * Дешифратор
     * @param string Строка
     * @return Расшифрованная строка
     * @throws InvalidKeyException Неправильный ключ
     * @throws BadPaddingException Неправильный формат
     * @throws IllegalBlockSizeException Некорректный формат
     */
    public String decrypt(String string) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException;
}
