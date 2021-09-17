package com.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HbaseUtil {
    public HbaseUtil() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        //createTable("test", "myfc1");
        //insert("test", "id1", "myfc1", "name", "lisi");
        //dropTable("test");
        //HbaseUtil.insert("test", "tes", "myfc1", "key", "stes");
        scanAll("test");
        //String byGet = byGet("test", "1.jpeg", "myfc1", "key");
        //System.out.printf("value: ", byGet.length());
        //String test = valueGet("test", "1.jpeg");
        //String cell = getCell("test", "1.jpeg", "myfc1", "key");
        //System.out.printf("value: " + cell);
    }

    private static Configuration conf;
    private static Connection con;
    private static Table table;
    private static ResultScanner scanner;
    //private static Connection connection;

    private static Admin admin;

    //扫描全表
    public static void scanAll(String tableName) throws Exception {
        con = ConnectionFactory.createConnection(conf);
        table = con.getTable(TableName.valueOf(tableName));
        // 通过Scan对象创建扫描器，扫描表中所有数据
        scanner = table.getScanner(new Scan());
        // 打印扫描结果
        printScanResults();
        con.close();
    }

    // 初始化连接 conf
    static {
        conf = HBaseConfiguration.create(); // 获得配制文件对象
        conf.set("hbase.zookeeper.quorum", "10.10.13.113");
        try {
            con = ConnectionFactory.createConnection(conf);// 获得连接对象
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //admin
    static {
        try {
            admin = con.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获得连接
    public static Connection getCon() {
        if (con == null || con.isClosed()) {
            try {
                con = ConnectionFactory.createConnection(conf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    // 关闭连接
    public static void close() {
        if (con != null) {
            try {
                con.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 创建表
    public static void createTable(String tableName, String... FamilyColumn) {
        TableName tn = TableName.valueOf(tableName);
        try {
            Admin admin = getCon().getAdmin();
            HTableDescriptor htd = new HTableDescriptor(tn);
            for (String fc : FamilyColumn) {
                HColumnDescriptor hcd = new HColumnDescriptor(fc);
                htd.addFamily(hcd);
            }
            admin.createTable(htd);
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 删除表
    public static void dropTable(String tableName) {
        TableName tn = TableName.valueOf(tableName);
        try {
            Admin admin = con.getAdmin();
            admin.disableTable(tn);
            admin.deleteTable(tn);
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 插入或者更新数据
    public static boolean insert(String tableName, String rowKey,String family, String qualifier, String value) {
        try {
            Table t = getCon().getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier),
                    Bytes.toBytes(value));
            t.put(put);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HbaseUtil.close();
        }
        return false;
    }

    // 删除
    public static boolean del(String tableName, String rowKey, String family, String qualifier) {
        try {
            Table t = getCon().getTable(TableName.valueOf(tableName));
            Delete del = new Delete(Bytes.toBytes(rowKey));
            if (qualifier != null) {
                del.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            } else if (family != null) {
                del.addFamily(Bytes.toBytes(family));
            }
            t.delete(del);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HbaseUtil.close();
        }
        return false;
    }

    //删除一行
    public static boolean del(String tableName, String rowKey) {
        return del(tableName, rowKey, null, null);
    }

    //删除一行下的一个列族
    public static boolean del(String tableName, String rowKey, String family) {
        return del(tableName, rowKey, family, null);
    }

    // 数据读取
    //
    public static String valueGet(String tablename, String rowkey) throws IOException {
        Table table = con.getTable(TableName.valueOf(tablename));
        Get get = new Get(rowkey.getBytes());
        // 使用HTable得到resultcanner实现类的对象
        Result result1 = table.get(get);
        List<Cell> cells = result1.listCells();
        if (cells != null) {
            for (Cell cell : cells) {

            /*//得到rowkey
            System.out.println("行键:" + Bytes.toString(CellUtil.cloneRow(cell)));
            //得到列族
            System.out.println("列族:" + Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println("列:" + Bytes.toString(CellUtil.cloneQualifier(cell)));
            */
                System.out.println("值:" + Bytes.toString(CellUtil.cloneValue(cell)));
                //System.out.println("----------------------******----------------------");
                return Bytes.toString(CellUtil.cloneValue(cell));
            }
        }

        return null;
    }

    /**
     * 获取指定行指定列 (cell) 的最新版本的数据
     *
     * @param tableName    表名
     * @param rowKey       唯一标识
     * @param columnFamily 列族
     * @param qualifier    列标识
     */
    public static String getCell(String tableName, String rowKey, String columnFamily, String qualifier) {
        try {
            Table table = con.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            if (!get.isCheckExistenceOnly()) {
                get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
                Result result = table.get(get);
                byte[] resultValue = result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
                return Bytes.toString(resultValue);
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //取到一个值
    public static String byGet(String tableName, String rowKey, String family,
                               String qualifier) {
        try {
            Table t = getCon().getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            Result r = t.get(get);
            return Bytes.toString(CellUtil.cloneValue(r.listCells().get(0)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //取到一个族列的值
    public static Map<String, String> byGet(String tableName, String rowKey, String family) {
        Map<String, String> result = null;
        try {
            Table t = getCon().getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes(family));
            Result r = t.get(get);
            List<Cell> cs = r.listCells();
            result = cs.size() > 0 ? new HashMap<String, String>() : result;
            for (Cell cell : cs) {
                result.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //取到多个族列的值
    public static Map<String, Map<String, String>> byGet(String tableName, String rowKey) {
        Map<String, Map<String, String>> results = null;
        try {
            Table t = getCon().getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            Result r = t.get(get);
            List<Cell> cs = r.listCells();
            results = cs.size() > 0 ? new HashMap<String, Map<String, String>>() : results;
            for (Cell cell : cs) {
                String familyName = Bytes.toString(CellUtil.cloneFamily(cell));
                if (results.get(familyName) == null) {
                    results.put(familyName, new HashMap<String, String>());
                }
                results.get(familyName).put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    // 格式化打印扫描到的数据
    private static void printScanResults() {
        for (Result row : scanner) {
            //System.out.println(row);
            for (Cell cell : row.listCells()) {
                System.out.println(
                        "RowKey:"
                                + Bytes.toString(row.getRow())
                                + " Family:"
                                + Bytes.toString(CellUtil.cloneFamily(cell))
                                + " Qualifier:"
                                + Bytes.toString(CellUtil.cloneQualifier(cell))
                                + " Value:"
                                + Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
    }

}
