package com.swsm.system.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.service.impl.EntityServiceImpl;
import com.swsm.system.model.Accessory;
import com.swsm.system.service.IAccessoryService;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * <p>ClassName: DictService</p>
 * <p>Description: 附件的业务处理类</p>
 */
@Service("accessoryService")
public class AccessoryServiceImpl  extends EntityServiceImpl<Accessory> implements IAccessoryService {

    
    @Autowired
    @Qualifier("accessoryDao")
    @Override
    public void setBaseDao(BaseDaoImpl<Accessory, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }

    @Override
    public String uploadAccessory(FileItem item, String fileName, String filePath, 
            String relativeClassId, String username) throws Exception {
        String fileNameNoSuffix;
        fileNameNoSuffix = fileName.substring(0, fileName.lastIndexOf("."));
        String fileSuffix;
        fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        //保存上传文件
        String currentTimeMillis;
        currentTimeMillis = System.currentTimeMillis() + "";
        filePath = filePath.replaceAll(fileNameNoSuffix, fileNameNoSuffix + "_"
                + currentTimeMillis);
        item.write(new File(filePath));
        Accessory accessory;
        accessory = new Accessory();
        accessory.setAccessoryName(fileNameNoSuffix + "_" + currentTimeMillis + "." + fileSuffix);
        accessory.setAccessoryPath(filePath);
        accessory.setRelativeClassId(currentTimeMillis + "");
        accessory.setUploadDate(new Date());
        accessory.setCreateUser(username);
        accessory.setCreateDate(new Date());
        accessory.setDelFlag(DEL_FALSE);
        this.baseDaoImpl.save(accessory);
        
        return "success";
    }

    @Override
    public Accessory getAccessoryByAccessoryName(String accessoryName) {
        return this.baseDaoImpl.findUniqueByProperty(Accessory.class, "accessoryName", accessoryName);
    }
    
    
}
