package com.github.jerrymice.common.entity.utils;


import com.github.jerrymice.common.entity.adpter.JSONCommentTypeAdapter;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.github.jerrymice.common.entity.annotation.ApiField;
import com.github.jerrymice.common.entity.annotation.TableField;
import com.github.jerrymice.common.entity.entity.ResultInfo;
import com.github.jerrymice.common.entity.page.Paginator;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tumingjian
 * 生成JSON数据,还会根据注解生成注释内容
 * 可以用ApiField或TableField注解为对象添加JSON字段注释内容
 */
public class APIDocGenerateUtils {
    public static Gson gson = getDefaultGsonBuild().create();

    /**
     * 创建一个默认的GsonBuilder,方便生成自定义的Gson.
     * @return 返回一个GsonBuilder
     */
    public static GsonBuilder getDefaultGsonBuild() {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls().setFieldNamingStrategy(new FieldNamingStrategy() {

            @Override
            public String translateName(Field f) {
//			f.getAnnotation(annotationClass)
                if (f.getAnnotation(ApiField.class) != null) {
                    if (!f.getAnnotation(ApiField.class).name().equals(ApiField.DEFAULT_NAME))
                        return f.getAnnotation(ApiField.class).name() + "|" + f.getAnnotation(TableField.class).comment()
                        		+"|"+f.getAnnotation(TableField.class).jdbcType()+"|"+f.getAnnotation(TableField.class).length();
                    else
                        return f.getName() + "|" + f.getAnnotation(ApiField.class).comment()
                        		+"|"+f.getAnnotation(ApiField.class).jdbcType()+"|"+f.getAnnotation(ApiField.class).length();
                } else {
                    String str = f.getAnnotation(TableField.class) == null ? f.getName() : f.getName() + "|" + f.getAnnotation(TableField.class).comment()
                    		+"|"+f.getAnnotation(TableField.class).jdbcType()+"|"+f.getAnnotation(TableField.class).length();
                    return str;
                }

            }

        }).setPrettyPrinting().registerTypeAdapter(String.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(Integer.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(Timestamp.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(java.sql.Date.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(Boolean.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(BigDecimal.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(Number.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(boolean.class, new JSONCommentTypeAdapter<String>())
                .registerTypeAdapter(Date.class, new JSONCommentTypeAdapter<String>());
        return gsonBuilder;
    }

    /**
     * 分页JSON生成
     * @param classes 要生成分页数据的class
     * @return  返回一个被ResultInfo包装的类型
     * @throws Exception 反射异常,或Gson转换异常
     */
    public static String toJSONStringByPage(Class<?> classes) throws Exception {
        Paginator paginator = new Paginator();
        List<Object> data = new ArrayList<Object>();
        Object o = classes.newInstance();
        data.add(o);
        paginator.setDatas(data);
        ResultInfo resultInfo = new ResultInfo(true).setObject(paginator);
        String s = gson.toJson(resultInfo);
        String s1 = formatJsonComment(s);
        return s1;
    }

    /**
     * 无分页对象生成
     * @param classes 要生成数据的class
     * @return  返回一个被ResultInfo包装的类型
     * @throws Exception 反射异常,或Gson转换异常
     */
    public static String toJSONStringByObject(Class<?> classes) throws Exception {
        Object o = classes.newInstance();
        ResultInfo resultInfo = new ResultInfo(true).setObject(o);
        String s = gson.toJson(resultInfo);
        String s1 = formatJsonComment(s);
        return s1;
    }

    /**
     * Array对象生成
     * @param classes 要生成数据的class
     * @return  返回一个被ResultInfo包装的类型
     * @throws Exception 反射异常,或Gson转换异常
     */
    public static String toJSONStringByArray(Class<?> classes) throws Exception {
        Object o = classes.newInstance();
        ArrayList<Object> objects = new ArrayList<Object>();
        objects.add(o);
        ResultInfo resultInfo = new ResultInfo(true).setObject(objects);
        String s = gson.toJson(resultInfo);
        String s1 = formatJsonComment(s);
        return s1;
    }

    /**
     * 普通对象生成
     * @param classes 要生成数据的class
     * @return  返回一个包含对象所有公开字段的json
     * @throws Exception 反射异常,或Gson转换异常
     */
    public static String toJSONStringByClass(Class<?> classes) throws Exception {
        Object o = classes.newInstance();
        String s = gson.toJson(o);
        String s1 = formatJsonComment(s);
        return s1;
    }

    /**
     *生成ResultInfo为true的JSON字符串
     * @return 生成JSON字段串的返回值
     */
    public static String toSuccessJSONString() {
        String s = gson.toJson(new ResultInfo<String>(true));
        String s1 = formatJsonComment(s);
        return s1;
    }

    /**
     *生成ResultInfo为false的JSON字符串
     * @return 生成JSON字段串的返回值
     */
    public static String toFailSONString() {
        String s = gson.toJson(new ResultInfo<String>(false).setMessage("错误消息"));
        String s1 = formatJsonComment(s);
        return s1;
    }

    /**
     *自定义的对象转JSON
     * @param o 要生是JSON的对象
     * @return 返回JSON内容
     */
    public static String toJSONStringByObject(Object o) {
        String s = gson.toJson(o);
        String s1 = formatJsonComment(s);
        return s1;
    }

    /**
     * 格式化注释内容
     * @param s  要格式化的内容
     * @return 返回格式化后的内容
     */
    private static String formatJsonComment(String s) {
        String s1 = s.replaceAll("(\"[\\s\\S]*?\":[\\s\\S]*?),(//[\\s\\S]*?)\",{0,1}", "$1\", $2")
                .replaceAll("\"null\"", "null").replaceAll("\"([\\s\\S]*?)\\|[\\s\\S]*?\"", "\"$1\"");
        return s1;
    }
}
