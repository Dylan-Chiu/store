package com.test.store.service;

import com.alibaba.fastjson.JSON;
import com.test.store.entity.Order;
import com.test.store.entity.OrderDetail;
import com.test.store.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UpdateGoodsService updateGoodsService;

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    public String createOrder(Order order) {
        HashMap<String, Integer> message = new HashMap<String, Integer>();
        //使用uuid给order加上订单编号
        order.setOrder_id(UUIDUtils.getStringUUID());

        //给order初始状态，1代表成功创建
        order.setStatus(1);

        //订单创建时间
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = simpleDateFormat.format(new Date().getTime());
        order.setOrder_time(new Date());

        //检查库存，并更改Goods表
        try {
            updateGoodsService.updateGoods(order.getDetail());
        } catch (Exception e) {
            message.put("status", -3);//状态码-3代表库存不够
            return JSON.toJSONString(message);
        }


        //写入order表
        String sql_insert_order = "insert into `order` values(?,?,?,?)";
        jdbcTemplate.update(sql_insert_order, order.getOrder_id(),
                order.getUsername(), order.getStatus(), order.getOrder_time());

        //写入order_detail表
        List<OrderDetail> detail = order.getDetail();
        String sql_insert_detail = "insert into `order_detail` values(?,?,?)";
        for (OrderDetail d : detail) {
            jdbcTemplate.update(sql_insert_detail, order.getOrder_id(), d.getGoodsId(), d.getAmount());
        }
        message.put("status", 1);
        return JSON.toJSONString(message);
    }

    /**
     * 根据id获取订单
     *
     * @param id
     * @return
     */
    public Order getOrderById(String id) {
        //创建订单对象
        Order order = new Order();
        order.setOrder_id(id);

        //获取订单表头信息
        String sql_getOrder = "select * from `order` where order_id = ?";
        Map<String, Object> orderData = jdbcTemplate.queryForList(sql_getOrder, id).get(0);
        order.setUsername((String) orderData.get("username"));
        order.setStatus((Integer) orderData.get("status"));
        order.setOrder_time((Date) orderData.get("order_time"));

        //处理一下获取到order_time后面有.0的情况
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        try {
            time = sdf.parse(order.getOrder_time().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setOrder_time(time);
        order.setDetail(getDetailById(id));
        return order;
    }

    /**
     * 获取订单详细信息
     * @param id
     * @return
     */
    public List<OrderDetail> getDetailById(String id) {
        String sql_getDetail = "select goods_id,amount,`name`, price*amount as totalPrice from order_detail,goods where order_detail.goods_id = goods.id and order_id = ?";
        List<Map<String, Object>> detail_src = jdbcTemplate.queryForList(sql_getDetail, id);
        List<OrderDetail> detail = new ArrayList<OrderDetail>();
        for (Map<String, Object> d : detail_src) {
            OrderDetail orderDetail = new OrderDetail((int) d.get("goods_id"), (String) d.get("name"), (int) d.get("amount"), Double.valueOf(d.get("totalPrice").toString()));
            detail.add(orderDetail);
        }
        return detail;
    }

    /**
     * 根据用户名获取订单列表
     *
     * @param username
     * @return
     */
    public List<Order> getOrderListByUsername(String username) {
        //获取该用户的订单列表
        String sql_get_OrderId_list = "SELECT order_id FROM `order` WHERE username = ?";
        List<Map<String, Object>> idList_src = jdbcTemplate.queryForList(sql_get_OrderId_list, username);
        ArrayList<String> idList = new ArrayList<>();
        for (Map<String, Object> idMap : idList_src) {
            String id = (String) idMap.get("order_id");
            idList.add(id);
        }

        //根据idList获取订单列表
        List<Order> orderList = getOrderByIdList(idList);
        System.out.println(JSON.toJSONString(orderList));
        return orderList;
    }

    /**
     * 根据id列表获取订单列表
     *
     * @param idList
     * @return
     */
    public List<Order> getOrderByIdList(List<String> idList) {
        ArrayList<Order> orderList = new ArrayList<>();
        for (String id : idList) {
            Order order = getOrderById(id);
            orderList.add(order);
        }
        return orderList;
    }

    /**
     * 获取所有的订单列表
     *
     * @return
     */
    public List<Order> getAllOrder() {
        String sql_get_all_id = "SELECT order_id FROM `order`";
        List<Map<String, Object>> idList_src = jdbcTemplate.queryForList(sql_get_all_id);
        List<String> idList = new ArrayList<>();
        for (Map<String, Object> idMap : idList_src) {
            String id = (String) idMap.get("order_id");
            idList.add(id);
        }
        List<Order> orderList = getOrderByIdList(idList);
        return orderList;
    }

    /**
     * 修改订单状态
     *
     * @param id
     * @param status
     */
    public boolean modOrderStatus(String id, int status) {
        String sql = "update `order` set `status` = ? where order_id = ?";
        int update = jdbcTemplate.update(sql, status, id);
        return update > 0;
    }

    /**
     * 根据当前页码和条目数，返回订单列表
     *
     * @param curPage
     * @param limit
     * @return
     */
    public List<Order> getLimitOrder(int curPage, int limit) {
        String sql_get_limit_id = "SELECT order_id FROM `order` limit ?,?";
        int pageSize = limit;
        int start = (curPage - 1) * pageSize;
        List<Map<String, Object>> idList_src = jdbcTemplate.queryForList(sql_get_limit_id, start, pageSize);
        List<String> idList = new ArrayList<String>();
        for (Map<String, Object> idMap : idList_src) {
            String id = (String) idMap.get("order_id");
            idList.add(id);
        }
        List<Order> orderList = getOrderByIdList(idList);
        return orderList;
    }

    /**
     * 获取订单总数目
     * @return
     */
    public Integer getCount() {
        String sql = "select count(*) from `order`";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }

    /**
     * 改变订单状态
     * @param id
     * @param status
     * @return
     */
    public boolean changeStatus(String id, int status) {
        String sql = "update `order` set `status` = ? where order_id = ?";
        int update = jdbcTemplate.update(sql, status, id);
        return update > 0;
    }
}
