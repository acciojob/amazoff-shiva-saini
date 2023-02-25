//package com.driver;
//
//public class Order {
//
//    private String id;
//    private int deliveryTime;
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public void setDeliveryTime(int deliveryTime) {
//        this.deliveryTime = deliveryTime;
//    }
//
//    public Order(String id, String deliveryTime) {
//            this.id = id;
//            int h = Integer.parseInt(deliveryTime.substring(0,2));
//            int m = Integer.parseInt(deliveryTime.substring(3,deliveryTime.length()));
//            this.deliveryTime = (h * 60 + m);
//        // The deliveryTime has to converted from string to int and then stored in the attribute
//        //deliveryTime  = HH*60 + MM
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public int getDeliveryTime() {return deliveryTime;}
//}



package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        int time =Integer.parseInt(deliveryTime.substring(0,2))*60+Integer.parseInt(deliveryTime.substring(3));
        this.deliveryTime=time;

    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}