package com.llvision.face.dao.biz;

import com.llvision.face.base.BaseBiz;
import com.llvision.face.dao.mapper.PersonMapper;
import com.llvision.face.entity.Person;
import com.llvision.face.utils.PagedLimitUtils;
import com.llvision.face.utils.PagedModelList;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: guoyc
 * @Date: 2019/5/14 16:11
 * @Version 1.0
 */
@Service
public class PersonBiz extends BaseBiz<PersonMapper, Person> {

    public Person getPersonByIdAndAppId(Long id,String appId) {
        return mapper.selectPersonValidByIdAndAppId(id,appId);
    }


    public int del(Long id,String appId) {
        int maxMark = getMaxMark();
        Person person = getPersonByIdAndAppId(id,appId);
        if (person != null) {
            person.setMark(maxMark + 1);
            person.setAction(3);
            person.setUpdateTime(new Date());
            person.setIsDel(1);
            return mapper.updateByPrimaryKeySelective(person);
        } else {
            return 0;
        }
    }

    public int del(Person person) {
        int maxMark = getMaxMark();
        if (person != null) {
            person.setMark(maxMark + 1);
            person.setAction(3);
            person.setUpdateTime(new Date());
            person.setIsDel(1);
            return mapper.updateByPrimaryKeySelective(person);
        } else {
            return 0;
        }
    }

    public int getMaxMark() {
        return mapper.getMaxMark();
    }

    public PagedModelList<Person> getPageList(Integer warehouseId, int pageId, int pageSize) {
        int count = mapper.getPersonCountBywarehouseId(warehouseId);
        List<Person> list = mapper.getPageListBywarehouseId(warehouseId, PagedLimitUtils.getPagedIndex(pageId, pageSize), PagedLimitUtils.getPagedLimit(pageId, pageSize));
        PagedModelList<Person> pml = new PagedModelList<Person>(pageId, pageSize, count);
        pml.addModels(list);
        return pml;
    }


    public List<Person> getPersonByPersonAndWarehouseId(Person person, Integer warehouseId) {
        return mapper.getPersonByPersonAndWarehouseId(person, warehouseId);
    }


    public List<Map<String,Object>> getPersonListByUserId(Integer personId,String appId){
       return mapper.getPersonListByUserId(personId,appId);
    }

}
