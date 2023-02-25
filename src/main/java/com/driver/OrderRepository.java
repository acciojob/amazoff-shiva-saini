//package com.driver;
//
//import io.swagger.models.auth.In;
//
//import javax.management.OperationsException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class OrderRepository {
//    HashMap<String,Order> orderHashMap;
//    HashMap<String,DeliveryPartner> deliveryPartnerHashMap;
//    HashMap<String, List<Order>> partnerOrder;
//    public OrderRepository(){
//        orderHashMap = new HashMap<>();
//        deliveryPartnerHashMap = new HashMap<>();
//        partnerOrder = new HashMap<>();
//
//    }
//
//    public void addOrder(Order ord){
//        orderHashMap.put(ord.getId(),ord);
//    }
//
//    public void addDeleveryPartner(DeliveryPartner part){
//        deliveryPartnerHashMap.put(part.getId(),part);
//    }
//
//    public void OrderToPartner(String orderId,String partnerId){
//      List<Order> ords = null;
//
//    }
//
//    public Order getOrderById(String orderId){
//        return orderHashMap.get(orderId);
//    }
//
//    public DeliveryPartner getDeleveryPartnerById(String partnerId){
//        return deliveryPartnerHashMap.get(partnerId);
//    }
//
//    public int getNoOfOrdersAssToPartner(String partnerId){
//        return partnerOrder.get(partnerId).size();
//    }
//    public List<Order> getListOfOrdersAssToPartner(String partnerId){
//        return partnerOrder.get(partnerId);
//    }
//
//    public List<Order> allOrders(){
//       HashMap<String,Order> all = new HashMap<>();
//       for(String partnerId:partnerOrder.keySet()){
//           List<Order>  ord = partnerOrder.get(partnerId);
//           for(Order o:ord){
//               if(!all.containsKey(o.getId())){
//                   all.put(o.getId(),o);
//               }
//           }
//       }
//
//       for(String orderId:orderHashMap.keySet()){
//           Order o = orderHashMap.get(orderId);
//           if(!all.containsKey(o.getId())){
//               all.put(o.getId(),o);
//           }
//       }
//       List<Order> allOrders = new ArrayList<>();
//       for(String orderId:orderHashMap.keySet()){
//           Order o = orderHashMap.get(orderId);
//           allOrders.add(o);
//       }
//       return allOrders;
//    }
//
//    public int unAssignedOrders(){
//        HashMap<String,Order> Ass = new HashMap<>();
//        for(String partnerId:partnerOrder.keySet()){
//            List<Order> l = partnerOrder.get(partnerId);
//            for(Order o:l){
//                if(!Ass.containsKey(o.getId())){
//                    Ass.put(o.getId(), o);
//                }
//            }
//        }
//        int cnt = 0;
//        for(String orderId:orderHashMap.keySet()){
//            Order o = orderHashMap.get(orderId);
//            if(!Ass.containsKey(o.getId())){
//                cnt++;
//            }
//        }
//        return cnt;
//    }
//}









package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;
@Repository
public class OrderRepository {

    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> partnerDb = new HashMap<>();
    HashMap<String, List<String>> pairDb = new HashMap<>();
    HashMap<String, String> assignedDb = new HashMap<>(); // <orderId, partnerId>

    public String addOrder(Order order) {
        orderDb.put(order.getId(), order);
        return "Added";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId, partner);
        return "Added";
    }

    public String addOrderPartnerPair(String orderId, String partnerId) {

        List<String> list = pairDb.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        pairDb.put(partnerId, list);
        assignedDb.put(orderId, partnerId);
        DeliveryPartner partner = partnerDb.get(partnerId);
        partner.setNumberOfOrders(list.size());
        return "Added";

    }

    public Order getOrderById(String orderId) {

        for (String s : orderDb.keySet()) {
            if (s.equals(orderId)) {
                return orderDb.get(s);
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (partnerDb.containsKey(partnerId)) {
            return partnerDb.get(partnerId);
        }
        return null;

    }

    public int getOrderCountByPartnerId(String partnerId) {

        int orders = pairDb.getOrDefault(partnerId, new ArrayList<>()).size();
        return orders;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {


        List<String> orders = pairDb.getOrDefault(partnerId, new ArrayList<>());
        return orders;
    }

    public List<String> getAllOrders() {

        List<String> orders = new ArrayList<>();
        for (String s : orderDb.keySet()) {
            orders.add(s);
        }
        return orders;

    }

    public int getCountOfUnassignedOrders() {

        int countOfOrders = orderDb.size() - assignedDb.size();
        return countOfOrders;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {

        int countOfOrders = 0;
        List<String> list = pairDb.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String s : list) {
            Order order = orderDb.get(s);
            if (order.getDeliveryTime() > deliveryTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {

        String time = "";
        List<String> list = pairDb.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = orderDb.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour = deliveryTime / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }

        int min = deliveryTime % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }

        time = sHour + ":" + sMin;

        return time;

    }

    public String deletePartnerById(String partnerId) {

        partnerDb.remove(partnerId);

        List<String> list = pairDb.getOrDefault(partnerId, new ArrayList<>());
        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            assignedDb.remove(s);
        }
        pairDb.remove(partnerId);
        return "Deleted";
    }

    public String deleteOrderById(String orderId) {

        orderDb.remove(orderId);
        String partnerId = assignedDb.get(orderId);
        assignedDb.remove(orderId);
        List<String> list = pairDb.get(partnerId);

        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            if (s.equals(orderId)) {
                itr.remove();
            }
        }
        pairDb.put(partnerId, list);

        return "Deleted";
    }

}