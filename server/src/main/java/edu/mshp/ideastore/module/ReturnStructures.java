package edu.mshp.ideastore.module;

import java.util.HashMap;
import java.util.Map;

/**
 * Генератор типичных структур
 * @author rugrisser
 */
public class ReturnStructures {

    /**
     * Генерация объекта с полем <i>status</i> и значением <b>ok</b>
     * @return Объект со статусом
     */
    public static Map<String, Object> returnStatusOk() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "ok");

        return result;
    }
}
