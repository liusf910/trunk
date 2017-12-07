package com.ln.tms.handler;

import com.ln.tms.enums.BaseEnum;
import org.apache.ibatis.javassist.Modifier;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * EnumTypeHandler - 枚举类型转换
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class EnumTypeHandler<E extends BaseEnum> extends BaseTypeHandler<BaseEnum> {

    private Method codeOf;

    public EnumTypeHandler(Class<E> enumType) {
        if (enumType == null)
            throw new IllegalArgumentException("Type argument cannot be null");

        String className = enumType.getName();

        try {
            this.codeOf = enumType.getDeclaredMethod("codeOf", new Class[]{Integer.TYPE});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code) required.");
        }

        if (!Modifier.isStatic(this.codeOf.getModifiers())) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code) required.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.code());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.codeOf(rs.getInt(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.codeOf(rs.getInt(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.codeOf(cs.getInt(columnIndex));
    }


    private E codeOf(int code) {
        try {
            return (E) this.codeOf.invoke((Object) null, new Object[]{Integer.valueOf(code)});
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }
}