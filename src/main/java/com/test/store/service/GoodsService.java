package com.test.store.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.test.store.entity.Goods;
import com.test.store.mapper.GoodsMapper;
import com.test.store.util.StatusCodeUtils;
import com.test.store.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 返回所有商品数据
     *
     * @return
     */
    public String getAllGoods(String username) {
        HashMap<String, Object> message = new HashMap<String, Object>();

        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        List<Goods> goods = goodsMapper.selectList(goodsQueryWrapper);

        message.put("data", goods);
        message.put("count", goods.size());
        message.put("username", username);

        //判定goodsList为空的情况
        if (goods.size() == 0) {
            message.put("code", StatusCodeUtils.DATA_IS_EMPTY);
        } else {
            message.put("code", StatusCodeUtils.SUCCESS_0);
        }

        message.put("msg", "");
        return JSON.toJSONString(message);
    }

    /**
     * 返回Goods数据库从start开始，长度为length的数据
     *
     * @param start
     * @param length
     * @return
     */
    public List<Goods> getLimitGoods(int start, int length) {
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.last(" limit " + start + "," + length);
        List<Goods> goods = goodsMapper.selectList(goodsQueryWrapper);
        return goods;
    }

    /**
     * 获取商品总条目数
     *
     * @return
     */
    public Integer getGoodsCount() {
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        long count = goodsMapper.selectCount(goodsQueryWrapper);
        return Math.toIntExact(count);
    }

    /**
     * 删除对应id的货物记录
     * 删除的时候不能让其他事务干扰，所以打包成一个事务，这样写应该可以吧
     *
     * @param delGoodsList 货物id的列表
     */

    @Transactional(rollbackFor = Exception.class)
    public int delGoods(List<Integer> delGoodsList) {
        int update = 0;
        for (Integer id : delGoodsList) {
            update += goodsMapper.deleteById(id);
        }
        return update;
    }

    public String modGoods(Goods goods, MultipartFile img) {
        if (img != null) { //图片不为空的情况
            String newImageName = saveGoodsImg(img);
            String sql_update = "UPDATE `goods` set name = ?,category = ?,stock = ?,price = ?,introduction = ?,img_name = ? WHERE id = ?";
            int update = jdbcTemplate.update(sql_update, goods.getName(), goods.getCategory(), goods.getStock(),
                    goods.getPrice(), goods.getIntroduction(), newImageName, goods.getId());
        } else { // 图片为null
            String sql_update = "UPDATE `goods` set name = ?,category = ?,stock = ?,price = ?,introduction = ? WHERE id = ?";
            int update = jdbcTemplate.update(sql_update, goods.getName(), goods.getCategory(), goods.getStock(),
                    goods.getPrice(), goods.getIntroduction(), goods.getId());

        }
        return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.SUCCESS_0);
    }

    public String addGoods(Goods goods, MultipartFile img) {
        String newImageName = saveGoodsImg(img);
        goods.setImgName(newImageName);
        goodsMapper.insert(goods);
        return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.SUCCESS_0);
    }

    /**
     * 由于需要保存到本地，且文件名不能重复，所以自动重命名，然后保存到static/goodsImage目录下
     *
     * @param img 一张从前端获取到的图片文件
     * @return
     */
    public String saveGoodsImg(MultipartFile img) {
        //获取原文件名
        String originalFilename = img.getOriginalFilename();

        //获取使用uuid生成的十位数字文件名
        String fileName = UUIDUtils.getStringUUID();

        //获取文件的后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());

        //拼接成新文件名
        String newFileName = fileName + "." + suffix;

        //获取项目根路径并转到static/goodsImage
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/goodsImage";

        try {
            img.transferTo(new File(path + "\\" + newFileName));
        } catch (Exception e) {
            System.out.println("文件保存发生异常！");
        }
        return newFileName;
    }



}
