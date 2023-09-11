package com.miko.bookapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static Field[] extractFields(Object object) {
        List<Field> fieldsList = new ArrayList<>();
        Class<?> currentClass = object.getClass();

        while (currentClass != null) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            fieldsList.addAll(Arrays.asList(declaredFields));
            currentClass = currentClass.getSuperclass();
        }

        Field[] fieldsArray = new Field[fieldsList.size()];
        return fieldsList.toArray(fieldsArray);
    }
}
