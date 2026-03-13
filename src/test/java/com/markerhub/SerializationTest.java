package com.markerhub;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rpc.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)//SpringBoot 2.X 默认使用Junit4
@SpringBootTest(classes = VueblogApplication.class)
public class SerializationTest {
    @Test
    public void test_java() throws Exception
    {
        User u1=new User();
        u1.setUsername("meme");

        ByteArrayOutputStream b1=new ByteArrayOutputStream();
        ObjectOutputStream o1=new ObjectOutputStream(b1);
        o1.writeObject(u1);
        System.out.println(b1.toString());

//        ObjectInputStream b2=new ObjectInputStream(b1.toByteArray());
        ByteArrayInputStream b2=new ByteArrayInputStream(b1.toByteArray());
        ObjectInputStream o2=new ObjectInputStream(b2);
        User u2=(User) o2.readObject();
        System.out.println(u2.getUsername());

    }
    @Test
    public void test_kryo() throws Exception
    {
        System.out.println("——————————————————");
        Kryo kryo = new Kryo();
        kryo.register(User.class);
        kryo.register(LocalDateTime.class);
        User object = new User();
        object.setUsername("Hello Kryo!");
        Output output = new Output(new FileOutputStream("file.bin"));
        kryo.writeObject(output, object);
        output.close();
        Input input = new Input(new FileInputStream("file.bin"));
        User object2 = kryo.readObject(input, User.class);
        input.close();
        System.out.println(object2.getUsername());

    }
}
