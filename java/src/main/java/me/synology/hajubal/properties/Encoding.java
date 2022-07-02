package me.synology.hajubal.properties;

import java.nio.charset.StandardCharsets;
import java.util.PropertyResourceBundle;

/**
 * <pre></pre>
 * properties 파일의 인코딩에 따라 데이터를 불러 오는데 문제 없는지 테스트
 *
 * java doc https://docs.oracle.com/javase/7/docs/api/java/util/PropertyResourceBundle.html
 *
 * PropertyResourceBundle은 속성 파일을 나타내는 InputStream 또는 Reader에서 구성할 수 있습니다.
 * InputStream에서 PropertyResourceBundle 인스턴스를 구성하려면 입력 스트림을 ISO-8859-1로 인코딩해야 합니다.
 * 이 경우 ISO-8859-1 인코딩에서 나타낼 수 없는 문자는 Java™ Language Specification의 섹션 3.3에 정의된 대로
 * Unicode Excape로 표시되어야 하지만 Reader를 사용하는 다른 생성자는 이러한 제한이 없습니다.
 *
 * </pre>
 */
public class Encoding {

    public static void main(String[] args) throws Exception {
        streamTest();
    }

    public static void streamTest() throws Exception {
        //UTF-8 encoding 파일
        PropertyResourceBundle resourceBundle = new PropertyResourceBundle(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.properties"));

        System.out.println("en = " + resourceBundle.getString("en"));
        System.out.println("kor1 = " + resourceBundle.getString("kor1"));

        //ANSI encoding 파일
        resourceBundle = new PropertyResourceBundle(Thread.currentThread().getContextClassLoader().getResourceAsStream("test3.properties"));

        System.out.println("en = " + resourceBundle.getString("en"));
        System.out.println("kor1 = " + new String(resourceBundle.getString("kor1").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
    }
}
