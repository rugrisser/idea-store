package edu.mshp.ideastore.module;

/**
 * Конвертер байтов
 * @author rugrisser
 */
public class ByteConverter {
    /**
     * Конвертирует массив байтов в строку
     * @param bytes - массив байтов
     * @return полученная из байтов строка
     */
    public static String toString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append((char) b);
        }
        return result.toString();
    }
}
