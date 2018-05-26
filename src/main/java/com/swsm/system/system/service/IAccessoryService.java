package com.swsm.system.system.service;

import com.swsm.system.system.model.Accessory;
import org.apache.commons.fileupload.FileItem;


/**
 * <p>ClassName: IAccessoryService</p>
 * <p>Description: 附件的接口类</p>
 */
public interface IAccessoryService {
    
    /**
     * <p>Description: 保存附件</p>
     * @param item 文件项
     * @param fileName 附件名称
     * @param filePath 附件保存路径
     * @param relativeClassId 附件有关实体id
     * @param username 登录人用户名
     * @return 操作结果
     * @throws Exception 异常
     */
    public String uploadAccessory(FileItem item, String fileName, String filePath,
            String relativeClassId, String username) throws Exception;
    
    /**
     * <p>Description: 根据附件名称获取附件</p>
     * @param accessoryName 附件名称
     * @return 附件
     */
    public Accessory getAccessoryByAccessoryName(String accessoryName);
}
