package com.test.store.contoller;

import com.alibaba.fastjson.JSON;
import com.test.store.entity.Goods;
import com.test.store.entity.OrderDetail;
import com.test.store.entity.Page;
import com.test.store.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/Goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("getAllGoods")
    public String getAllGoods(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        return goodsService.getAllGoods(username);
    }

    /**
     * 获取部分商品信息
     *
     * @param request
     * @param strPage  当前页数
     * @param strLimit 每页的条目数
     * @return
     */
    @RequestMapping("getGoods")
    public Page getGoods(HttpServletRequest request,
                         @RequestParam(value = "page", required = false) String strPage,
                         @RequestParam(value = "limit", required = false) String strLimit) {
        Page page = new Page();

        //写入初始状态码
        page.setCode(0);//0代表正常

        //获取用户名，验证登陆状态
        String username = (String) request.getSession().getAttribute("username");
        page.setUsername(username);
        if (username == null) {
            page.setCode(-1);//未登录
            page.setMsg("未登录");
        }

        //获取当前页码
        int curPage = 1;
        if (strPage != null) {
            curPage = Integer.valueOf(strPage);
        }
        page.setCurPage(curPage);

        //获取当页商品数
        int limit = 12;
        if (strLimit != null) {
            limit = Integer.valueOf(strLimit);
        }

        //获取商品信息
        int pageSize = limit;
        int start = (curPage - 1) * pageSize;
        List<Map<String, Object>> goodsList = goodsService.getLimitGoods(start, pageSize);
        for (Map<String, Object> goods : goodsList) {
//            System.out.println(goods);
            goods.put("img", "/goodsImage/" + goods.get("img_name"));
        }
        page.setData(goodsList);

        //判定goodsList为空的情况
        if (goodsList.isEmpty()) {
            page.setCurPage(1);
            /* 这里是为了这种情况：删除了最后一页的数据导致当前页goodsList为空
            设置CurPage为1 前端检查到 自动跳到第一页 */
        }

        //获取总页码
        int count = goodsService.getGoodsCount();
        page.setCount(count);

        return page;
    }


    /**
     * 删除货物
     * @param request
     * @param strPage
     * @param strLimit
     * @param params
     * @return
     */
    @RequestMapping("delGoods")
    public String DelGoods(HttpServletRequest request,
                           @RequestParam(value = "page", required = false) String strPage,
                           @RequestParam(value = "limit", required = false) String strLimit,
                           @RequestBody Map<String, Object> params) {
        //获取待删除商品id数组
        List<Integer> delGoodsList = new ArrayList<>();
        List<Map> goodsList = (List<Map>) params.get("data");
        List<OrderDetail> detail = new ArrayList<OrderDetail>();
        for (Map map : goodsList) {
            int id = (int) map.get("id");
            delGoodsList.add(id);
        }

        goodsService.delGoods(delGoodsList);
        return JSON.toJSONString(getGoods(request, strPage, strLimit));
    }


    /**
     * 修改货物，由于需要img文件，所以使用form-data格式传输
     * @param request
     * @param id
     * @param name
     * @param category
     * @param stock
     * @param price
     * @param introduction
     * @param img
     * @return
     */
    @RequestMapping("modGoods")
    public String modGoods(HttpServletRequest request,
                           @RequestParam(value = "id", required = false) String id,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "stock", required = false) String stock,
                           @RequestParam(value = "price", required = false) String price,
                           @RequestParam(value = "introduction", required = false) String introduction,
                           @RequestParam(value = "img", required = false) MultipartFile img) {
        //如果没改图片 那img就是null
        String imgName;
        if(img != null) {
            imgName = img.getOriginalFilename();
        } else {
            imgName = null;
        }
        Goods goods = new Goods(Integer.parseInt(id), name, category, Integer.valueOf(stock), Double.valueOf(price), introduction, imgName);
        return goodsService.modGoods(goods, img);
    }

    /**
     * 添加商品，由于需要img文件，所以使用form-data格式传输
     * @param request
     * @param name
     * @param category
     * @param stock
     * @param price
     * @param introduction
     * @param img
     * @return
     */
    @RequestMapping("addGoods")
    public String addGoods(HttpServletRequest request,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "stock", required = false) String stock,
                           @RequestParam(value = "price", required = false) String price,
                           @RequestParam(value = "introduction", required = false) String introduction,
                           @RequestParam(value = "img", required = false) MultipartFile img) {
        String imgName = img.getOriginalFilename();
        Goods goods = new Goods(name, category, Integer.valueOf(stock), Double.valueOf(price), introduction, imgName);
        return goodsService.addGoods(goods, img);
    }
}