package com.site.jwt.entity;

public class test {
    public static void main(String[] args) {



        System.out.println("TID              原始项目集                   整理后项目集");
        System.out.println("100              {f,a,c,d,g,i,m,p}          {f,c,a,m,p}");
        System.out.println("200              {a,b,c,f,l,o}              {f,c,a,b,o}");
        System.out.println("300              {b,f,h,j,m,p}              {f,b,m,p}");
        System.out.println("400              {b,c,k,m,o,s}              {c,b,m,o}");
        System.out.println("500              {a,f,c,e,l,    ,o,p}          {f,c,a,o,p}");


        System.out.println("=======================FP-tree算法========================");


        System.out.println("项              条件模式基本                   条件FP-tree             产生的频繁模式");
        System.out.println("P               {[fcam,1][fao,1][cbm,1]}      <c,3>                 [cp,3]");
        System.out.println("m               {[fcam,1][fao,1][cbm,1]}      <c,3>                 [c0,3]");
        System.out.println("b               {[fcam,1][fao,1][cbm,1]}       null                 null");
        System.out.println("a               {[fcam,1][fao,1][cbm,1]}       null                 null");
        System.out.println("c               {[fcam,1][fao,1][cbm,1]}      <fc,3>                [fa,3],[ca,3],[fca,3]");
        System.out.println("c               {[fcam,1][fao,1][cbm,1]}      <f,3>                 [fc,3]");
        System.out.println("f               null                           null                 null");
    }

}
