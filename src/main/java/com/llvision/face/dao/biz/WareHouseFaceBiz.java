package com.llvision.face.dao.biz;

import com.llvision.face.base.BaseBiz;
import com.llvision.face.dao.mapper.WareHouseFaceMapper;
import com.llvision.face.entity.OffLineFace;
import com.llvision.face.entity.WareHouseFace;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 16:12
 * @Version 1.0
 */
@Service
public class WareHouseFaceBiz extends BaseBiz<WareHouseFaceMapper, WareHouseFace> {

    public List<WareHouseFace> getListByWarehouseId(Integer warehouseId) {
        return mapper.getListByWarehouseId(warehouseId);
    }

    public int delByPersonId(Long personId) {
        return mapper.delByPersonId(personId);
    }

    public int delByLibId(Long personId) {
        return mapper.delByLibId(personId);
    }


}
