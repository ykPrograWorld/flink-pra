package org.example.bean;

import java.util.ArrayList;
import java.util.Random;

public class ItemId {

    private static  Random r;
    private static ArrayList<String> itemList;
    static {
        r = new Random();
        itemList = new ArrayList<>();
        itemList.add("o1");
        itemList.add("o2");
        itemList.add("o3");
        itemList.add("o4");
        itemList.add("o5");
        itemList.add("o6");
    }

    public static String getItemId(){
//        ArrayList<String> itemList = getItemList();
        return itemList.get(r.nextInt(6));
    }

//    public static ArrayList<String> getItemList(){
//        ArrayList<String> itemList = new ArrayList<>();
//        itemList.add("o1");
//        itemList.add("o2");
//        itemList.add("o3");
//        itemList.add("o4");
//        itemList.add("o5");
//        itemList.add("o6");
//        return itemList;
//    }
}
