package util;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @description: 数据库工具类
 */
public class DBHelp {
    private static Properties prop = new Properties ();
    private static DataSource ds = null;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    static {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("connect.properties"));
            ds = BasicDataSourceFactory.createDataSource(prop);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return 连接
     * @throws SQLException
     */
    public static Connection getConnection () throws SQLException {
        Connection conn = null;
        try {
            conn = threadLocal.get();
            if (conn == null) {
                conn = ds.getConnection();
                threadLocal.set(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 设置PreparedStatement参数
     * @param pstmt
     * @param objects
     * @throws SQLException
     */
    private static void setParm(PreparedStatement pstmt, Object...objects) throws SQLException {
        for (int i=0; i<objects.length; i++) {
            pstmt.setObject(i+1, objects[i]);
        }
    }

    /**
     * 关闭连接
     * @param rs
     * @param stmt
     * @param conn
     * @throws SQLException
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) throws SQLException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 增删改
     * @param sql SQL语句，可带参也可无参
     * @param objects
     * @return 成功数字大于0，失败返回0
     * @throws SQLException
     */
    public static int executeUpdate (String sql, Object...objects) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            setParm(pstmt, objects);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close (null, pstmt, null);
        }
        return result;
    }

    /**
     * 查询处理结果集，返回对象结果集
     * @param sql
     * @param orm 行映射接口
     * @param objects 可带参，也可不带参
     * @return 对象结果集
     * @throws SQLException
     */
    public static List<Object> executeQuery (String sql, RowMapperObject orm, Object...objects) throws  SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Object> list = new ArrayList<Object>();
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            setParm(pstmt, objects);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object o = orm.rowMapperObject(rs);
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, pstmt, null);
        }
        return list;
    }
}
