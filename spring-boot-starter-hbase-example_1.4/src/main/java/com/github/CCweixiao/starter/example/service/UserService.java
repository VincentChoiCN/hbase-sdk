package com.github.CCweixiao.starter.example.service;

import com.github.CCweixiao.HBaseTemplate;
import com.github.CCweixiao.starter.example.pojo.UserPojo;
import com.github.CCweixiao.util.HBytesUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>用户服务类</p>
 *
 * @author leo.jie (leojie1314@gmail.com)
 */
@Service
public class UserService {
    @Autowired
    private HBaseTemplate hBaseTemplate;

    public UserPojo getUser(String rowName) {
        return hBaseTemplate.getByRowKey(rowName, UserPojo.class);
    }

    public Map<String, Object> findUserMap(String tableName, String rowKey) {
        return hBaseTemplate.getByRowKey(tableName, rowKey);
    }

    public List<UserPojo> findUser() {
        return hBaseTemplate.findAll(10, UserPojo.class);
    }

    public void saveBatchUser() {
        List<UserPojo> userPojoList = new ArrayList<>();
        userPojoList.add(userPojo());
        userPojoList.add(userPojo2());
        try {
            hBaseTemplate.saveBatch(userPojoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUser() {
        UserPojo userPojo = userPojo();
        try {
            hBaseTemplate.save(userPojo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserPojo userPojo() {
        UserPojo userPojo = new UserPojo();
        userPojo.setRowKey("10001");
        userPojo.setAddress(Arrays.asList("北京", "上海"));
        userPojo.setAge(18);
        userPojo.setCost(1000.0d);
        userPojo.setCreateDate(new Date());
        userPojo.setPay(100.0f);
        userPojo.setUsername("leo");
        userPojo.setTimestamp(System.currentTimeMillis());
        Map<String, Object> info = new HashMap<>(2);
        info.put("first_name", "jie");
        info.put("tag", 100);
        userPojo.setInfo(info);
        userPojo.setBigDecimal(new BigDecimal(10000000));
        userPojo.setVip(true);
        userPojo.setRoles(new String[]{"admin", "user", "vip"});
        return userPojo;
    }

    public UserPojo userPojo2() {
        UserPojo userPojo = new UserPojo();
        userPojo.setRowKey("10002");
        userPojo.setAddress(Arrays.asList("北京", "上海"));
        userPojo.setAge(18);
        userPojo.setCost(1000.0d);
        userPojo.setCreateDate(new Date());
        userPojo.setPay(100.0f);
        userPojo.setUsername("leo3");
        userPojo.setTimestamp(System.currentTimeMillis());
        Map<String, Object> info = new HashMap<>(2);
        info.put("first_name", "long");
        info.put("tag", 1030);
        userPojo.setInfo(info);
        userPojo.setBigDecimal(new BigDecimal(10000000000L));
        userPojo.setVip(false);
        userPojo.setRoles(new String[]{"admin", "user", "vip"});
        userPojo.setUserStatus(2);
        return userPojo;
    }

    public List<Map<String, Object>> getDataWithMapper() {
        return hBaseTemplate.get("TEST:LEO_USER", "10002",
                (result, rowNum) -> {
                    List<Cell> cs = result.listCells();
                    List<Map<String, Object>> dataMaps = new ArrayList<>(cs.size());
                    for (Cell cell : cs) {
                        Map<String, Object> resultMap = resultToMap(result, cell);
                        dataMaps.add(resultMap);
                    }
                    return dataMaps;
                });
    }

    private Map<String, Object> resultToMap(Result result, Cell cell) {
        Map<String, Object> resultMap = new HashMap<>(4);
        String fieldName = Bytes.toString(CellUtil.cloneFamily(cell)) + ":" + Bytes.toString(CellUtil.cloneQualifier(cell));
        byte[] value = CellUtil.cloneValue(cell);
        resultMap.put("rowKey", Bytes.toString(result.getRow()));
        resultMap.put("familyName", fieldName);
        resultMap.put("timestamp", cell.getTimestamp());
        resultMap.put("value", HBytesUtil.toObject(value, Object.class));
        return resultMap;
    }
}
