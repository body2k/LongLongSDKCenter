
package com.longlong.common.utils;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;


public class XmlUtil {


    private final Document document;


    private final Element root;

    public XmlUtil(String filePath) throws DocumentException {

        this.document = getDocument(filePath);
        this.root = this.document.getRootElement();
    }

    public XmlUtil(byte[] value) throws DocumentException {

        this.document = getDocument(value);
        this.root = this.document.getRootElement();
    }

    private Document getDocument(String filePath) throws DocumentException {
        File file = new File(filePath);
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
    }


    private Document getDocument(byte[] value) throws DocumentException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
        SAXReader reader = new SAXReader();
        Document document = reader.read(byteArrayInputStream);
        return document;
    }

/**
 * 添加节点
 * */
/**
 * 修改节点
 * */
/**
 * 删除节点
 * */
    /**
     * 获取根节点
     */
    public Element getRootElement() {
        return document.getRootElement();
    }

    /**
     * 获取根节点标签
     */
    public String getRootElementName() {
        return document.getRootElement().getName();
    }

    /**
     * 返回给定本地名称和任何命名空间的第一个元素。`
     */
    public Element getElement(String param) {
        Element element = root.element(param);
        return element;

    }

    public String getElementText(String param) {
        String elementText = root.elementText(param);
        return elementText;

    }

    /**
     * 返回给定本地名称和任何命名空间的第一个元素值
     */
    public String getElementValue(String param) {
        String value = root.element(param).getText();
        return value;

    }
/**
 * 创建XML
 * */
/**
 * 解析XML
 * */

}
