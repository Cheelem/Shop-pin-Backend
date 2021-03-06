package cn.edu.neu.shop.pin.service;


import cn.edu.neu.shop.pin.mapper.PinSettingsProductCategoryMapper;
import cn.edu.neu.shop.pin.model.PinSettingsProductCategory;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService extends AbstractService<PinSettingsProductCategory> {

    private final PinSettingsProductCategoryMapper pinSettingsProductCategoryMapper;

    public ProductCategoryService(PinSettingsProductCategoryMapper pinSettingsProductCategoryMapper) {
        this.pinSettingsProductCategoryMapper = pinSettingsProductCategoryMapper;
    }

    /**
     * 层级获取商品分类表
     *
     * @param layer 层
     * @return List
     */
    public List<PinSettingsProductCategory> getProductCategoryByLayer(Integer layer) {
        if (layer == null || layer.equals(0)) {
            return pinSettingsProductCategoryMapper.getAllParentProductCategory();
        } else {
            return pinSettingsProductCategoryMapper.getAllSubProductCategory();
        }

    }

    /**
     * 根据父分类获取商品分类表
     *
     * @param parentId 父级分类ID
     * @return List
     */
    private List<PinSettingsProductCategory> getProductCategoryByParentId(Integer parentId) {
        if (parentId == null || parentId.equals(0)) {
            return pinSettingsProductCategoryMapper.getAllParentProductCategory();
        } else {
            PinSettingsProductCategory category = new PinSettingsProductCategory();
            category.setParentCategoryId(parentId);
            return pinSettingsProductCategoryMapper.select(category);
        }

    }

    /**
     * 根据获取嵌套商品分类表
     *
     * @return JSONArray 嵌套的商品分类
     */
    public JSONArray getProductCategoryAll() {
        List<PinSettingsProductCategory> parents = pinSettingsProductCategoryMapper.getAllParentProductCategory();
        JSONArray resultArray = new JSONArray();
        for (PinSettingsProductCategory parentCategory : parents) {
            JSONObject categoryJSON = (JSONObject) JSONObject.toJSON(parentCategory);
            categoryJSON.put("subCategories", getProductCategoryByParentId(parentCategory.getId()));
            resultArray.add(categoryJSON);
        }
        return resultArray;
    }

    public JSONArray getProductCategory() {
        List<JSONObject> parents = pinSettingsProductCategoryMapper.getParentProductCategory();
        JSONArray resultArray = new JSONArray();
        for (JSONObject parent : parents) {
            List<JSONObject> children = pinSettingsProductCategoryMapper.getSubProductCategory(parent.getInteger("id"));
            JSONObject tempItem = new JSONObject();
            tempItem.put("parent", parent);
            tempItem.put("child", children);
            resultArray.add(tempItem);
        }
        return resultArray;
    }
}
