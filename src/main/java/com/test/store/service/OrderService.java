package com.test.store.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.test.store.entity.Order;
import com.test.store.entity.OrderDetail;
import com.test.store.mapper.OrderDetailMapper;
import com.test.store.mapper.OrderMapper;
import com.test.store.util.IdentityUtils;
import com.test.store.util.StatusCodeUtils;
import com.test.store.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private UpdateGoodsService updateGoodsService;

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    public String createOrder(Order order) {
        //使用uuid给order加上订单编号
        order.setOrder_id(UUIDUtils.getStringUUID());

        //给order初始状态，1代表成功创建
        order.setStatus(1);

        //订单创建时间
        order.setOrder_time(new Date());

        //检查库存，并更改Goods表
        try {
            updateGoodsService.updateGoods(order.getDetail());
        } catch (Exception e) {
            return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.STOCK_ERROR);
        }

        orderMapper.insert(order);

        //写入order_detail表
        List<OrderDetail> detail = order.getDetail();
        for (OrderDetail d : detail) {
            OrderDetail newDetail = new OrderDetail();
            newDetail.setOrderId(order.getOrder_id());
            newDetail.setGoodsId(d.getGoodsId());
            newDetail.setAmount(d.getAmount());
            orderDetailMapper.insert(newDetail);
        }
        return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.SUCCESS_1);
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
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_id", id);
        Map<String, Object> orderData = orderMapper.selectMaps(orderQueryWrapper).get(0);

        order.setUsername((String) orderData.get("username"));
        order.setStatus((Integer) orderData.get("status"));
        order.setOrder_time((Date) orderData.get("order_time"));
        order.setAgent((String) orderData.get("agent"));

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
        double totalPrice = getTotalPrice(order.getDetail());
        order.setTotalPrice(totalPrice);
        return order;
    }

    public double getTotalPrice(List<OrderDetail> details) {
        double total = 0;
        for (OrderDetail detail : details) {
            total += detail.getTotalPrice();
        }
        return total;
    }

    /**
     * 获取订单详细信息
     *
     * @param id
     * @return
     */
    public List<OrderDetail> getDetailById(String id) {
        String sql_getDetail = "select goods_id,amount,`name`, price*amount as totalPrice from order_detail,goods where order_detail.goods_id = goods.id and order_id = ?";
        List<Map<String, Object>> detail_src = jdbcTemplate.queryForList(sql_getDetail, id);
        List<OrderDetail> detail = new ArrayList<OrderDetail>();
        for (Map<String, Object> d : detail_src) {
            OrderDetail orderDetail = new OrderDetail(null,(int) d.get("goods_id"), (String) d.get("name"), (int) d.get("amount"), Double.valueOf(d.get("totalPrice").toString()));
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
    public List<Order> getLimitOrderListByUsername(String username, int curPage, int pageSize) {

        //记录的起始下标
        int start = (curPage - 1) * pageSize;

        //获取该用户的订单列表
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("username", username);
        orderQueryWrapper.last("limit " + start + "," + pageSize);
        List<Map<String, Object>> idList_src = orderMapper.selectMaps(orderQueryWrapper);
        ArrayList<String> idList = new ArrayList<>();
        for (Map<String, Object> idMap : idList_src) {
            String id = (String) idMap.get("order_id");
            idList.add(id);
        }

        //根据idList获取订单列表
        List<Order> orderList = getOrderByIdList(idList);
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
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> idList_src = orderMapper.selectMaps(orderQueryWrapper);
        List<String> idList = new ArrayList<>();
        for (Map<String, Object> idMap : idList_src) {
            String id = (String) idMap.get("order_id");
            idList.add(id);
        }
        List<Order> orderList = getOrderByIdList(idList);
        return orderList;
    }

    /**
     * 根据当前页码和条目数，返回订单列表
     *
     * @param curPage
     * @param limit
     * @return
     */
    public List<Order> getLimitOrder(int curPage, int limit) {
        int pageSize = limit;
        int start = (curPage - 1) * pageSize;
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.last("limit " + start + "," + pageSize);
        List<Map<String, Object>> idList_src = orderMapper.selectMaps(orderQueryWrapper);

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
     *
     * @return
     */
    public Integer getCount() {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        Long count = orderMapper.selectCount(orderQueryWrapper);
        return Math.toIntExact(count);
    }

    /**
     * 改变订单状态
     *
     * @param id
     * @param status
     * @return
     */
    public boolean changeStatus(HttpServletRequest request, String id, int status) {
        String agent = IdentityUtils.getUsername(request);

        UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<>();
        orderUpdateWrapper.eq("order_id", id);
        Order order = new Order();
        order.setStatus(status);
        order.setAgent(agent);
        int update = orderMapper.update(order, orderUpdateWrapper);
        return update > 0;
    }
}
