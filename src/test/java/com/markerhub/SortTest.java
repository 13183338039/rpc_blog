package com.markerhub;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SortTest {

    @Test
    public void func(){
        List<Integer> l1=new ArrayList<>();
        l1.add(9);
        l1.add(1);
        l1.add(6);
        l1.sort(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1-o2;
                    }
                }
        );
        int[] l2=new int[]{9,1,6};
        Arrays.sort(l2);
        int n=l1.size();
        for(int i=0;i<n;i++){
            System.out.println("l1:"+l1.get(i));
        }
        for(int i=0;i<n;i++){
            System.out.println("l2:"+l2[i]);
        }
    }
}
