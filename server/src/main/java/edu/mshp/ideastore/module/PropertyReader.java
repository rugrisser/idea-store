package edu.mshp.ideastore.module;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Чтение файлов .properties
 * @author rugrisser
 */
public class PropertyReader {
    /** Путь к директории ресурсов */
    private final String DIRECTORY = "src/main/resources/";

    /** Путь к конфигу */
    private final String path;

    /**
     * Конструктор класса
     * @param name - название файла конфигурации (<samp><i>name</i>.properties</samp>)
     */
    public PropertyReader(String name) {
        this.path = DIRECTORY + name + ".properties";
    }

    /**
     * Получения значения по заданному ключу
     * @param key - ключ
     * @return полученное значение
     * @throws IOException - ошибка в чтении файла конфигурации
     */
    public String read(String key) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        Properties properties = new Properties();

        properties.load(fileInputStream);
        return properties.getProperty(key);
    }
}
