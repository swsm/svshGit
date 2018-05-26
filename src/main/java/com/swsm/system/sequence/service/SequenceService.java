package com.swsm.system.sequence.service;

import com.core.dao.impl.BaseDaoImpl;
import com.core.exception.BaseException;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.format.DateUtil;
import com.swsm.system.sequence.dao.impl.SequenceDaoImpl;
import com.swsm.system.sequence.model.SeqGenInfo;
import com.swsm.system.sequence.model.SequenceModel;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;


/**
 * <p>
 * ClassName: SequenceService
 * </p>
 * <p>
 * Description: 系列号生成Service
 * </p>
 */
@Service("sequenceService")
public class SequenceService extends EntityServiceImpl<SequenceModel> {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(SequenceService.class);

    /**
     * 配置文件路径（默认classes的编译路径下/seqGenInfo.xml）
     */
    private String seqGenInfoConfigFilePath = "/seqGenInfo.xml";

    /**
     * 生成机制信息map
     */
    private Map<String, SeqGenInfo> seqGenInfoMap = null;
    
    public Map<String, SeqGenInfo> getSeqGenInfoMap() {
		return seqGenInfoMap;
	}
	/**
     * 7位日期格式化
     */
    private final static String DATE_MASK_7="yyyyMdd";
    
    /**
     * 8位日期格式化
     */
    private final static String DATE_MASK_8="yyyyMMdd";

    /**
     * 
     * <p>
     * Description: 构造函数,读取序号生成机制配置信息
     * </p>
     */
    public SequenceService() {
        loadSeqGenInfoConfig();
        isValid();
    }

    @Autowired
    @Qualifier("sequenceDao")
    @Override
    public void setBaseDao(BaseDaoImpl<SequenceModel, String> baseDaoImpl) {
        this.baseDaoImpl = baseDaoImpl;
    }
    /**
     * 实例化sequenceDao
     */
    @Autowired
    @Qualifier("sequenceDao")
    private SequenceDaoImpl sequenceDao;

    /**
     * 设置配置文件路径（默认classes的编译路径下/seqGenInfo.xml）
     * 
     * @param seqGenInfoConfigFilePath 配置文件路径
     */
    public void setSeqGenInfoConfigFilePath(String seqGenInfoConfigFilePath) {
        this.seqGenInfoConfigFilePath = seqGenInfoConfigFilePath;
        // 重新载入序号配置信息
        loadSeqGenInfoConfig();
    }

    /**
     * 获得配置文件路径（默认classes的编译路径下/seqGenInfo.xml）
     * 
     * @return 配置文件路径
     */
    public String getSeqGenInfoConfigFilePath() {
        return this.seqGenInfoConfigFilePath;
    }

    /**
     * 
     * <p>
     * Description: 读取配置文件，生成配置文件Map
     * </p>
     */
    @SuppressWarnings("unchecked")
    private void loadSeqGenInfoConfig() {
        URL url;
        url = SequenceService.class.getResource(this.seqGenInfoConfigFilePath);
        if (url == null) {
            throw new RuntimeException("could not find file for seqGenInfo.xml");
        }
        logger.debug(url + "");
        XStream xstream;
        xstream = new XStream();
        xstream.alias("map", Hashtable.class);
        xstream.alias("seqGenInfo", SeqGenInfo.class);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(url.getPath());
            Reader reader;
            reader = new InputStreamReader(fis);
            this.seqGenInfoMap = (Map<String, SeqGenInfo>) xstream.fromXML(reader);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new RuntimeException("read seqGenInfo config error", fileNotFoundException);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 
     * <p>
     * Description: 判断配置是否有效，剔除掉错误的配置
     * </p>
     */
    private void isValid() {
        Map<String, SeqGenInfo> tempMap;
        tempMap = this.seqGenInfoMap;
        this.seqGenInfoMap = new HashMap<String, SeqGenInfo>();
        if (tempMap != null) {
            for (Map.Entry<String, SeqGenInfo> entry : tempMap.entrySet()) {
                String key;
                key = entry.getKey();
                SeqGenInfo seqGenInfo;
                seqGenInfo = entry.getValue();
                if (seqGenInfo.isValid()) {
                    this.seqGenInfoMap.put(key, seqGenInfo);
                }
            }
        }
    }

    /**
     * 
     * <p>
     * Description: 把时间转换成相应格式的字符串
     * </p>
     * 
     * @param date 时间
     * @param formatStr 格式
     * @return 格式化的日期格式字符串
     */
    private String formatDate(Date date, String formatStr) {
        if (date == null) {
            logger.warn("seqDate is null,please check seqDate!");
            return null;
        }
        if (formatStr == null) {
            logger.warn("formatStr is null,please check formatStr!");
            return null;
        }
        if(DATE_MASK_7.equals(formatStr)){
        	return DateUtil.getDateStr7(date);
        }else{
        	return DateUtil.getDateStr(date, formatStr);
        }

    }

    /**
     * 
     * <p>
     * Description: 格式化数字
     * </p>
     * @param seqLength 数字格式化length
     * @return 格式化的字符串
     */
    private DecimalFormat formatInt(int seqLength) {
        DecimalFormat format;
        format = new DecimalFormat(StringUtils.repeat("0", seqLength));
        return format;
    }

    /**
     * 
     * <p>
     * Description: 获得序列的下一个号
     * </p>
     * 
     * @param tableName 表名称
     * @param seqDate 序列日期
     * @param secondCode 二级代码一般是根据产线、物料属性等不同
     * @param optUser 操作人
     * @return 需要的编号
     * @throws BaseException 业务异常
     */
    public String makeNextNo(String tableName, Date seqDate, String secondCode, String optUser) throws BaseException {
        // 检查是否有输入表名
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("getNext id has no input tableName");
        }
        SeqGenInfo seqGenInfo;
        seqGenInfo = (SeqGenInfo) this.seqGenInfoMap.get(tableName);
        // 检查是否有改表的序号配置
        if (seqGenInfo == null) {
            logger.warn("tableName==" + tableName);
            throw new IllegalArgumentException("can't get seqGenInfo from seqGenInfo.xml");
        }
        if (!seqGenInfo.getHasSecondCode()) {
            secondCode = null;
        }
        String seqDateStr;
        seqDateStr = this.formatDate(seqDate, seqGenInfo.getDateMask());
        /**存在没有日期的情况 modify by tangsanlin 2017-08-11
        if (seqDateStr == null) {
            return null;
        }
         */
        String loopDateStr;
        loopDateStr = this.formatDate(seqDate, seqGenInfo.getLoop());
        List<SequenceModel> sequenceModels;
        sequenceModels = this.findSequenceModels(tableName, loopDateStr, secondCode);
        int seqCount;
        seqCount = sequenceModels.size();
        if (seqCount > 1) {
            throw new IllegalArgumentException("sequence data duplicated in database");
        }
        SequenceModel result;
        // 如果序列表中不存在id记录,则新建
        if (seqCount == 0) {
            result = this.insertSequenceModel(seqGenInfo, loopDateStr, secondCode, optUser);
        } else {
            result = this.updateSequenceModel(sequenceModels.get(0), optUser);
        }
        String nextId = "";
        if (!StringUtils.isEmpty(seqGenInfo.getPrefix())) {
            nextId += seqGenInfo.getPrefix();
        }
        if (secondCode != null) {
            nextId += (seqGenInfo.getSeperator() + secondCode);
        }
        if (seqDate != null) {
            nextId += (seqGenInfo.getSeperator() + seqDateStr);
        }
        nextId += (seqGenInfo.getSeperator() + result.getMaxSeq());
        logger.info("return code is : " + nextId);
        return nextId;
    }

    public SequenceDaoImpl getSequenceDao() {
		return sequenceDao;
	}

	/**
     * 
     * <p>
     * Description: 插入SequenceModel对象数据
     * </p>
     * 
     * @param seqGenInfo 单据规则配置对象
     * @param loopDateStr 日期字符格式
     * @param secondCode 二级CODE
     * @param optUser 用户信息
     * @return 序列模型
     */
    public SequenceModel insertSequenceModel(SeqGenInfo seqGenInfo, String loopDateStr, String secondCode,
            String optUser) {
        Date date;
        date = new Date();
        SequenceModel seqModel;
        seqModel = new SequenceModel();
        seqModel.setTableName(seqGenInfo.getTableName());
        seqModel.setSeqDate(loopDateStr);
        seqModel.setSecondCode(secondCode);
        seqModel.setCreateUser(optUser);
        seqModel.setCreateDate(date);
        seqModel.setUpdateUser(optUser);
        seqModel.setUpdateDate(date);
        seqModel.setDelFlag("0");
        DecimalFormat df;
        df = this.formatInt(seqGenInfo.getSeqLength());
        seqModel.setMaxSeq(df.format(1));
        this.baseDaoImpl.save(seqModel);
        return seqModel;
    }

    /**
     * 
     * <p>
     * Description: 更新序列模型
     * </p>
     * 
     * @param sequenceModel 序列模型
     * @param optUser 用户信息
     * @return 序列模型
     */
    public SequenceModel updateSequenceModel(SequenceModel sequenceModel, String optUser) {
        long maxId;
        maxId = Long.parseLong(sequenceModel.getMaxSeq()) + 1;
        DecimalFormat df;
        df = this.formatInt(sequenceModel.getMaxSeq().length());
        sequenceModel.setMaxSeq(df.format(maxId));
        sequenceModel.setUpdateUser(optUser);
        sequenceModel.setUpdateDate(new Date());
        this.baseDaoImpl.update(sequenceModel);
        return sequenceModel;
    }

    /**
     * 
     * <p>
     * Description: 根据表名，循环日期，二级码查找序列表中存在的序列记录
     * </p>
     * 
     * @param tableName 表名
     * @param loopDateStr 循环日期
     * @param secondCode 二级码
     * @return 序列记录
     */
    private List<SequenceModel> findSequenceModels(String tableName, String loopDateStr, String secondCode) {
        List<SequenceModel> maxSeqList;
        maxSeqList = this.sequenceDao.findSequenceModels(tableName, loopDateStr, secondCode);
        return maxSeqList;
    }
}
