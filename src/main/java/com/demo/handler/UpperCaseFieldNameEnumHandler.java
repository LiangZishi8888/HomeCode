package com.demo.handler;
import org.apache.ibatis.type.EnumTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpperCaseFieldNameEnumHandler<S extends Enum<S>> extends EnumTypeHandler<S> {

    private final Class<S> targetType;

    public UpperCaseFieldNameEnumHandler(Class<S> type) {
        super(type);
        this.targetType = type;
    }

    @Override
    public S getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            S es = super.getNullableResult(rs, columnName);
            return es;
        } catch (Exception e) {
            if(e instanceof SQLException)
                throw e;
        }
        String s = rs.getString(columnName).toUpperCase();
        return s == null ? null : Enum.valueOf(targetType, s);
    }

    @Override
    public S getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            S es = super.getNullableResult(rs, columnIndex);
            return es;
        } catch (Exception e) {
            if(e instanceof SQLException)
                throw e;
        }
        String s = rs.getString(columnIndex).toUpperCase();
        return s == null ? null : Enum.valueOf(targetType, s);
    }

    @Override
    public S getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            S es = super.getNullableResult(cs, columnIndex);
            return es;
        } catch (Exception e) {
            if(e instanceof SQLException)
                throw e;
        }
        String s = cs.getString(columnIndex).toUpperCase();
        return s == null ? null : Enum.valueOf(targetType, s);
    }
}
