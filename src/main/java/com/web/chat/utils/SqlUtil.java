package com.web.chat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.web.chat.dao.entity.ColumnAttribute;
import com.web.chat.dao.entity.ParameterAttribute;

import java.util.*;

/**
 * sql操作
 */
public class SqlUtil {
    public static final String SELECT = "SELECT";//查询
    public static final String INSERT = "INSERT";//插入
    public static final String UPDATE = "UPDATE";//修改
    public static final String DELETE = "DELETE";//删除
    public static final String ORACLE = "ORACLE";
    public static final String MYSQL = "MYSQL";
    private static final List<String> DATE_TYPE = Arrays.asList("DATETIME", "TIMESTAMP", "DATE", "TIME", "YEAR");//日期关键字
    private static final List<String> GROUP_CODE = Arrays.asList("COUNT", "SUM", "MAX", "MIN");//分组关键字
    private String sqlType;//sql类型
    private String dbType;//数据库类型
    private String tableName;//表名(必要时带上用户名)
    private String sqlDemo;//sql语句模板
    private Map params;//请求参数
    private List parameterList;//请求参数属性
    private List columnList;//列属性

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlDemo() {
        return sqlDemo;
    }

    public void setSqlDemo(String sqlDemo) {
        this.sqlDemo = sqlDemo;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public List getParameterList() {
        return parameterList;
    }

    public void setParameterList(List parameterList) {
        this.parameterList = parameterList;
    }

    public List getColumnList() {
        return columnList;
    }

    public void setColumnList(List columnList) {
        this.columnList = columnList;
    }

    /**
     * 无参构造函数
     */
    public SqlUtil() {
    }

    /**
     * 构造函数--所有属性
     *
     * @param sqlType
     * @param dbType
     * @param tableName
     * @param sqlDemo
     * @param params
     * @param parameterList
     * @param columnList
     */
    public SqlUtil(String sqlType, String dbType, String tableName, String sqlDemo, Map params, List parameterList, List columnList) {
        this.sqlType = sqlType;
        this.dbType = dbType;
        this.tableName = tableName;
        this.sqlDemo = sqlDemo;
        this.params = params;
        this.parameterList = parameterList;
        this.columnList = columnList;
    }

    /**
     * 构造函数--默认oracle
     *
     * @param sqlType
     * @param tableName
     * @param sqlDemo
     * @param params
     * @param parameterList
     * @param columnList
     */
    public SqlUtil(String sqlType, String tableName, String sqlDemo, Map params, List parameterList, List columnList) {
        this.sqlType = sqlType;
        this.dbType = ORACLE;
        this.tableName = tableName;
        this.sqlDemo = sqlDemo;
        this.params = params;
        this.parameterList = parameterList;
        this.columnList = columnList;
    }

    /**
     * 返回sql
     *
     * @param sqlUtils
     * @return
     */
    public static String getSql(SqlUtil sqlUtils) {
        String sql = null;
        switch (sqlUtils.getSqlType()) {
            case SELECT:
                sql = select(sqlUtils);
                break;
            case INSERT:
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
            default:
                break;
        }
        return sql;
    }

    /**
     * 生产查询
     *
     * @param sqlUtils
     * @return
     */
    private static String select(SqlUtil sqlUtils) {
        //生成过滤条件
        StringBuilder condition = new StringBuilder();
        createCondition(sqlUtils, condition);

        //生成查询
        StringBuilder select = new StringBuilder();
        createSelect(sqlUtils, select);

        return String.valueOf(new StringBuilder().append(select
                .toString().replaceAll("@and", condition.toString())));
    }

    /**
     * 构建查询语句
     *
     * @param sqlUtils
     * @param select
     * @return
     */
    private static StringBuilder createSelect(SqlUtil sqlUtils, StringBuilder select) {
        if (select == null) {
            select = new StringBuilder();
        }
        if (StringUtil.isNotEmpty(sqlUtils.getSqlDemo())) {
            //返回sqlDemo
            return select.append(sqlUtils.getSqlDemo());
        }
        if (StringUtil.isEmpty(sqlUtils.getTableName())) {
            //没有表名
            return select;
        }
        List<ColumnAttribute> columnList = sqlUtils.getColumnList();
        if (StringUtil.isEmpty(columnList)) {
            //没有列
            return select;
        }
        StringBuilder columns = new StringBuilder();
        int i = 0;
        for (ColumnAttribute columnAttribute : columnList) {
            i++;
            //字段
            StringBuilder code = new StringBuilder();
            //表名
            if (StringUtil.isNotEmpty(columnAttribute.getTableName())) {
                code.append(columnAttribute.getTableName())
                        .append(".");
            }
            code.append(columnAttribute.getCode());

            //格式化
            if (StringUtil.isNotEmpty(columnAttribute.getFormat())) {
                columns.append(columnAttribute.getFormat()
                        .replaceAll("@param", code.toString()))
                        .append(" ");
            } else if (StringUtil.isNotEmpty(columnAttribute.getType())) {
                //时间类型
                if (DATE_TYPE.contains(columnAttribute.getType().toUpperCase())) {
                    columns.append(getDateCode(sqlUtils, true)
                            .replaceAll("@param", code.toString()));
                } else {
                    columns.append(code)
                            .append(" ");
                }
            } else {
                columns.append(code)
                        .append(" ");
            }
            columns.append(columnAttribute.getCode());
            if (i < columnList.size()) {
                columns.append(",");
            }
        }
        return select.append(" select @columns from @table where 1=1 @and "
                .replaceAll("@columns", columns.toString())
                .replaceAll("@table", sqlUtils.getTableName()));
    }


    /**
     * 生成过滤条件
     *
     * @param sqlUtils
     * @param condition
     * @return
     */
    private static StringBuilder createCondition(SqlUtil sqlUtils, StringBuilder condition) {
        if (condition == null) {
            condition = new StringBuilder();
        }
        Map paramsMap = sqlUtils.getParams();
        List<ParameterAttribute> paramsList = sqlUtils.getParameterList();
        if (paramsMap == null || paramsList == null) {
            return condition;
        }
        Iterator it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            for (ParameterAttribute params : paramsList) {
                if (key.equals(params.getKey())) {
                    //字段
                    StringBuilder code = new StringBuilder();
                    //表名
                    if (StringUtil.isNotEmpty(params.getTableName())) {
                        code.append(params.getTableName())
                                .append(".");
                    }
                    //默认字段名
                    if (StringUtil.isNotEmpty(params.getCode())) {
                        code.append(params.getCode());
                    } else {
                        //将key转化为字段名
                        code.append(StringUtil.humpToLine(key));
                    }

                    //为空
                    if (value == null || (("[]".equals(value) || "".equals(value)) && params.getOperator().contains("in"))) {
                        if (params.isFill()) {
                            condition.append(" and ")
                                    .append(code)
                                    .append(" is null ");
                        }
                        break;
                    }

                    //非空
                    condition.append(" and ");

                    //默认格式
                    if (StringUtil.isNotEmpty(params.getFormat())) {
                        condition.append(params.getFormat()
                                .replaceAll("@param", code.toString()));
                    } else if (StringUtil.isNotEmpty(params.getType())) {
                        //日期类型(默认,年月日)
                        if (DATE_TYPE.contains(params.getType().toUpperCase())) {
                            condition.append(getDateCode(sqlUtils, true)
                                    .replaceAll("@param", code.toString()));
                        } else {
                            condition.append(code);
                        }
                    } else {
                        condition.append(code);
                    }

                    //值处理
                    if (params.getOperator().contains("in")) {
                        JSONArray array = new JSONArray();
                        if (value.toString().indexOf("[") != 0) {
                            //字符串拼接类型
                            JSONArray.toJSON(value.toString().split(","));
                        } else {
                            //数组转字符串类型
                            array = JSON.parseArray(String.valueOf(value));
                        }
                        StringBuilder inValue = new StringBuilder();
                        for (Object i : array) {
                            if (i == null) {
                                inValue.append("null,");
                                continue;
                            }
                            inValue.append("'" + i + "',");
                        }
                        //去除最后一个逗号;
                        value = inValue.toString().substring(0, inValue.toString().length() - 1);
                    }
                    condition.append(getOpration(params.getOperator())
                            .replaceAll("@value", value.toString()));
                    break;
                }
            }
        }
        return condition;
    }

    /**
     * 获取时间类型
     *
     * @param sqlUtils
     * @param toChar
     * @return
     */
    private static String getDateCode(SqlUtil sqlUtils, boolean toChar) {
        switch (sqlUtils.getSqlType()) {
            case ORACLE:
                return toChar ? " to_char(@param,'yyyy-mm-dd') " : " to_date('@param','yyyy-mm-dd') ";
            case MYSQL:
                return toChar ? " date_format(@param,'%Y-%m-%d') " : " str_to_date('@param','%Y-%m-%d') ";
            default:
                return toChar ? " @param " : " '@param' ";
        }
    }

    /**
     * 获取条件关系
     *
     * @param opration
     * @return
     */
    private static String getOpration(String opration) {
        String str = " @opration '@value' ";
        switch (opration) {
            case ""://默认为等于
                return str.replaceAll("@opration", "=");
            case "llike"://左包含
                return str.replaceAll("@opration", "like")
                        .replaceAll("@value", "@value%");
            case "rlike"://右包含
                return str.replaceAll("@opration", "like")
                        .replaceAll("@value", "%@value");
            case "like"://包含
            case "not like"://不包含
                return str.replaceAll("@opration", opration)
                        .replaceAll("@value", "%@value%");
            case "in"://在范围内
            case "not in"://不在范围内
                return str.replaceAll("@opration", opration)
                        .replaceAll("'@value'", "(@value)");
            default:
                return str.replaceAll("@opration", opration);
        }
    }

    public static void main(String[] args) {
       /* StringBuilder condition = new StringBuilder();
        createCondition(new SqlUtils(), condition);
        System.out.println(condition.toString());*/


    }
}
